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
import java.util.concurrent.*;

/**
 * Created by Evgeniy Pshenitsin on 01.05.2015.
 */
public class CashPointParser extends BaseParser {


    private static final Logger logger = LogManager.getLogger("parser");

    //02.05.15
    static DateFormat format = new SimpleDateFormat("dd.MM.yy", Locale.ENGLISH);

    ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(50, getThreadFactory(CashPointParser.class.getSimpleName()));

    public CashPointParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.CASHPOINT;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        List<CashPointDownloader> workers = new ArrayList<CashPointDownloader>();

        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            driver.get("https://www.cashpoint.com/en/site/index.html");
            Thread.sleep(5000);
            //System.setProperty("http.proxyHost", "127.0.0.1");
            //System.setProperty("http.proxyPort", "8091");
            Document doc = Jsoup.parse(driver.getPageSource());
            /*Document doc = Jsoup.connect("https://www.cashpoint.com/en/site/index.html") // --
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                    .get();*/

            Elements aaa = doc.select("a.menu_link");
            Set<String> links = new HashSet<String>();
            for (Element a: aaa) {
                String href = a.attr("href");
                if (href != null && href.contains("sportid=1") && href.startsWith("/en/bets/")) {
                    links.add(href);
                }
            }
            for (String link: links) {
                String finalLink = "https://www.cashpoint.com" + link;
                driver.get(finalLink);
                doc = Jsoup.parse(driver.getPageSource());
                Element containerBetsGroups = doc.getElementById("container_bets_groups");
                if (containerBetsGroups != null) {
                    Elements liveRow = containerBetsGroups.select("div.live_row");
                    for (Element row: liveRow) {
                        try {
                            //
                            Element result = row.select("div.live_td.live_results_games").first();
                            if (result != null && !result.text().trim().isEmpty()) {
                                continue;
                            }
                            String date = row.select("div.live_game_time").first().select("span.upcoming_date").first().text().trim().substring(0,8);
                            if (date.isEmpty()) {
                                logger.error("CashPointParser empty date");
                                continue;
                            }
                            BasicEvent event = new BasicEvent();
                            event.date = date;
                            event.day = format.parse(date);
                            Elements teams = row.select("div.live_group_name_2").first().select("div");
                            if (teams.size() != 3) {
                                logger.error("CashPointParser empty teams");
                                continue;
                            }
                            String team1 = teams.get(1).text().trim();
                            String team2 = teams.get(2).text().trim();
                            event.team1 = Teams.getTeam(team1);
                            event.team2 = Teams.getTeam(team2);
                            if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                                logger.warn("CashPointParser - unknown teams: " + team1 + ":" + event.team1 + ";" + team2 + ":" + event.team2);
                                continue;
                            }
                            events.add(event);
                            event.site = BetSite.CASHPOINT;
                            event.link = finalLink;
                            Elements odds = row.select("div.live_right_section").first().select("div.live_odds_container");
                            if (odds.size() == 5) {
                                String p1 = odds.get(0).text().trim();
                                if (!p1.isEmpty()) {
                                    event.bets.put(BetType.P1, p1);
                                }
                                String x = odds.get(1).text().trim();
                                if (!x.isEmpty()) {
                                    event.bets.put(BetType.X, x);
                                }
                                String p2 = odds.get(2).text().trim();
                                if (!p2.isEmpty()) {
                                    event.bets.put(BetType.P2, p2);
                                }

                                String over = odds.get(3).text().trim();
                                String under = odds.get(4).text().trim();
                                String type = row.select("div.live_right_section").first().select("div.live_td.live_frame").text().trim();
                                BetType overTotal = BetDataMapping.betsTotal.get(type + "M");
                                BetType underTotal = BetDataMapping.betsTotal.get(type + "L");
                                if (overTotal != null && !over.isEmpty()) {
                                    event.bets.put(overTotal, over);
                                }
                                if (underTotal != null && !under.isEmpty()) {
                                    event.bets.put(underTotal, under);
                                }
                                Element more = row.select("div.live_buttons2.live_btn_2").first();
                                if (more != null && !more.text().trim().isEmpty()) {
                                    Element a = more.select("a.xtra_counter.showXtra").first();
                                    if (a != null && !a.attr("href").isEmpty()) {
                                        event.link = a.attr("href");
                                        //parseOdds(event);
                                    }
                                }
                            }
                            //
                        } catch (Exception e) {
                            logger.error("CashPointParser error", e);
                        }
                    }
                }
            }
            returnWebDriverToPool(driver);
            driver = null;
            for (BasicEvent event: events) {
                if (event.link.contains("xtra")) {
                    workers.add(new CashPointDownloader(this, event));
                }
            }
            List<Future<BasicEvent>> parserStates = new ArrayList<Future<BasicEvent>>();
            for (CashPointDownloader tasks: workers) {
                parserStates.add(downloadersTaskPool.submit(tasks));
            }
            for (Future<BasicEvent> f: parserStates) {
                try {
                    f.get();
                    f.cancel(true);
                } catch (Exception e) {
                    logger.error("processGetBets: future get() exception - ", e);
                    f.cancel(true);
                }
            }
            //downloadersTaskPool.shutdown();
        } catch (UnreachableBrowserException e) {
            logger.error("CashPointParser worker error - UnreachableBrowserException", e);
            recreateDiedWebDriver(driver);
            driver = null;
        } catch (Exception e) {
            logger.error("CashPointParser exception - ", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    private static class CashPointDownloader extends BaseDownloaderSingle {

        CashPointParser parser;
        BasicEvent event;

        public CashPointDownloader(CashPointParser parser, BasicEvent event) {
            this.parser = parser;
            this.event = event;
        }

        @Override
        public BasicEvent download() {
            return parser.parseOdds(event);
        }
    }

    private BasicEvent parseOdds(BasicEvent event) {
        WebDriver driver = null;
        try {
            String finalLink = "https://www.cashpoint.com" + event.link;
            event.link = finalLink;
            driver = getWebDriverFromPool();
            driver.get(finalLink);
            Document doc = Jsoup.parse(driver.getPageSource());
            Element containerXtra = doc.getElementById("container_xtra");
            if (containerXtra != null) {
                Elements tables = containerXtra.select("table.sportbet_extra_list_table");
                for (Element table: tables) {
                    String title = table.select("td.sportbet_extra_c1").first().text();
                    if (title.startsWith("Double Chance")) {
                        Elements oddsX = table.select("td.sportbet_extra_c2");
                        if (oddsX.size() == 3) {
                            if (oddsX.get(0).select("div.sportbet_content_rate_right").first() != null) {
                                String p1x = oddsX.get(0).select("div.sportbet_content_rate_right").first().text().trim();
                                if (!p1x.isEmpty()) {
                                    event.bets.put(BetType.P1X, p1x);
                                }
                            }
                            if (oddsX.get(1).select("div.sportbet_content_rate_right").first() != null) {
                                String p12 = oddsX.get(1).select("div.sportbet_content_rate_right").first().text().trim();
                                if (!p12.isEmpty()) {
                                    event.bets.put(BetType.P12, p12);
                                }
                            }
                            if (oddsX.get(2).select("div.sportbet_content_rate_right").first() != null) {
                                String p2x = oddsX.get(2).select("div.sportbet_content_rate_right").first().text().trim();
                                if (!p2x.isEmpty()) {
                                    event.bets.put(BetType.P2X, p2x);
                                }
                            }
                        }
                    } else if (title.startsWith("Over/Under") || title.startsWith("Over Under")) {
                        Elements totals = table.select("td.sportbet_extra_c2");
                        if (totals.size() == 3) {
                            String over = totals.get(0).select("div.sportbet_content_rate_right").first().text().trim();
                            String under = totals.get(2).select("div.sportbet_content_rate_right").first().text().trim();
                            String type = title.substring(10, 14).trim();
                            BetType overTotal = BetDataMapping.betsTotal.get(type + "M");
                            BetType underTotal = BetDataMapping.betsTotal.get(type + "L");
                            if (overTotal != null && !over.isEmpty()) {
                                event.bets.put(overTotal, over);
                            }
                            if (underTotal != null && !under.isEmpty()) {
                                event.bets.put(underTotal, under);
                            }
                        }

                    }
                }
            }
        } catch (UnreachableBrowserException e) {
            logger.error("CashPointParser worker error - UnreachableBrowserException", e);
            recreateDiedWebDriver(driver);
            driver = null;
        } catch (Exception e) {
            logger.error("CashPointParser parse odds error", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        EventSender.sendEvent(event);
        return null;
    }
}
