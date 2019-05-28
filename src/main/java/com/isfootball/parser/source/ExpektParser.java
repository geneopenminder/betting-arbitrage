package com.isfootball.parser.source;

import com.isfootball.model.BasicEvent;
import com.isfootball.parser.*;
import com.isfootball.pool.BasePoolInterface;
import com.isfootball.processing.EventSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Evgeniy Pshenitsin on 02.05.2015.
 */
public class ExpektParser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(50, getThreadFactory(ExpektParser.class.getSimpleName()));

    public ExpektParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.EXPEKT;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();

        List<ExpektDownloader> workers = new ArrayList<ExpektDownloader>();
        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            driver.get("https://en.expekt.com/sport/");
            Thread.sleep(3000);
            String htmlSource = driver.getPageSource();
            Document doc = Jsoup.parse(htmlSource);
            Elements hrefs = doc.select("a.event-name");
            Set<String> leagues = new HashSet<String>();
            for (Element a: hrefs) {
                if (a.attr("href").startsWith("/football/")) {
                    leagues.add(a.attr("href"));
                }
            }
            Set<String> matches = new HashSet<String>();
            for (String league: leagues) {
                String finalLink = "https://en.expekt.com" + league;
                driver.get(finalLink);
                //Thread.sleep(300);
                doc = Jsoup.parse(driver.getPageSource());
                Elements moreOdds = doc.select("a.qTip.more-odds.extra-info");
                for (Element more: moreOdds) {
                    matches.add(more.attr("href"));
                    //parseMatch(more.attr("href")); //DELETE
                }
            }

            returnWebDriverToPool(driver);
            driver = null;
            for (String match: matches) {
                workers.add(new ExpektDownloader(this, match));
            }
            List<Future<BasicEvent>> parserStates = new ArrayList<Future<BasicEvent>>();
            for (ExpektDownloader tasks: workers) {
                parserStates.add(downloadersTaskPool.submit(tasks));
            }
            for (Future<BasicEvent> f: parserStates) {
                try {
                    BasicEvent event = f.get();
                    if (event != null) {
                        events.add(f.get());
                    }
                    f.cancel(true);
                } catch (Exception e) {
                    logger.error("ExpektParser processGetBets: future get() exception - ", e);
                    f.cancel(true);
                }
            }
            //downloadersTaskPool.shutdown();
        } catch (UnreachableBrowserException e) {
        } catch (Exception e) {
            logger.error("ExpektParser exception - ", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    private static class ExpektDownloader extends BaseDownloaderSingle {

        ExpektParser parser;
        String link;

        public ExpektDownloader(ExpektParser parser, String link) {
            this.parser = parser;
            this.link = link;
        }

        @Override
        public BasicEvent download() {
            return parser.parseMatch(link);
        }
    }

    private BasicEvent parseMatch(String link) {
        BasicEvent event = new BasicEvent();
        event.link = "https://en.expekt.com" + link;
        event.site = BetSite.EXPEKT;
        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            driver.get(event.link);
            //Thread.sleep(500);
            Document match = Jsoup.parse(driver.getPageSource());
            String[] teams = match.select("div.section-header-left").first().select("h1").first().text().split(" - ");
            event.team1 = Teams.getTeam(teams[0].trim());
            event.team2 = Teams.getTeam(teams[1].trim());
            if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                logger.warn("ExpektParser - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                return null;
            }
            event.date = match.select("time.time").first().text().trim();
            event.day = DateFormatter.format(event.date.substring(event.date.indexOf(" "), event.date.indexOf("-")).trim(), betSite);
            Elements betsEntries = match.select("div.entry.bet-type-entry");
            for (Element entry: betsEntries) {
                String title = entry.select("div.section-title").first().text();
                if (title.equalsIgnoreCase("Match Result")) {
                    Elements mainOdds = entry.select("a.oddButtonWrap");
                    if (mainOdds.size() == 3) {
                        String p1 = mainOdds.get(0).select("span.oddValue").first().text().trim();
                        if (!p1.isEmpty()) {
                            event.bets.put(BetType.P1, p1);
                        }
                        String pX = mainOdds.get(1).select("span.oddValue").first().text().trim();
                        if (!pX.isEmpty()) {
                            event.bets.put(BetType.X, pX);
                        }
                        String p2 = mainOdds.get(2).select("span.oddValue").first().text().trim();
                        if (!p2.isEmpty()) {
                            event.bets.put(BetType.P2, p2);
                        }
                    }
                } else if (title.equalsIgnoreCase("Double Chance")) {
                    Elements mainOdds = entry.select("a.oddButtonWrap");
                    if (mainOdds.size() == 3) {
                        String p1x = mainOdds.get(0).select("span.oddValue").first().text().trim();
                        if (!p1x.isEmpty()) {
                            event.bets.put(BetType.P1X, p1x);
                        }
                        String p12 = mainOdds.get(1).select("span.oddValue").first().text().trim();
                        if (!p12.isEmpty()) {
                            event.bets.put(BetType.P12, p12);
                        }
                        String p2x = mainOdds.get(2).select("span.oddValue").first().text().trim();
                        if (!p2x.isEmpty()) {
                            event.bets.put(BetType.P2X, p2x);
                        }
                    }
                } else if (title.equalsIgnoreCase("Over/Under")) {
                    Elements rows = entry.select("tr");
                    for (Element row: rows) {
                        if (row.hasClass("expand-selection-bet") || row.hasClass("colapse-selection-row")) {
                            String totalType = row.select("td.nameScorer").first().text().trim();
                            BetType total = null;
                            if (totalType.startsWith("Under")) {
                                total = BetDataMapping.betsTotal.get(totalType.substring(5).trim() + "L");
                            } else if (totalType.startsWith("Over")) {
                                total = BetDataMapping.betsTotal.get(totalType.substring(4).trim() + "M");
                            }
                            Elements td = row.select("td");
                            String totalVal = td.get(1).select("span.oddValue").first().text().trim();
                            if (total != null && !totalVal.isEmpty()) {
                                event.bets.put(total, totalVal);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("ExpektParser exception - ", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        EventSender.sendEvent(event);
        return event;
    }

}
