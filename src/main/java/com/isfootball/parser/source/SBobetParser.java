package com.isfootball.parser.source;

import com.isfootball.model.BasicEvent;
import com.isfootball.parser.*;
import com.isfootball.pool.BasePoolInterface;
import com.isfootball.processing.EventSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Evgeniy Pshenitsin on 30.03.2015.
 */
public class SBobetParser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(30, getThreadFactory(SBobetParser.class.getSimpleName()));

    public SBobetParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.SBOBET;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        WebDriver driver = null;
        List<SBobetDownloader> workers = new ArrayList<SBobetDownloader>();
        try {
            driver = getWebDriverFromPool();
            driver.get("https://www.sbobet.com/euro/football");
            Thread.sleep(5000);
            String htmlSource = driver.getPageSource();
            Document doc = Jsoup.parse(htmlSource);
            Elements football = doc.getElementById("ms-all-res-1").select("li.UnSel");
            List<String> leagues = new ArrayList<String>();
            for (Element league : football) {
                String href = league.select("a").first().attr("href");
                if (href != null) {
                    leagues.add(href);
                }
            }
            Set<String> matchesSet = new HashSet<String>();
            for (String league : leagues) {
                String leagueLink = "https://www.sbobet.com" + league + "/more";
                driver.get(leagueLink);
                Thread.sleep(1000);
                htmlSource = driver.getPageSource();
                Document docL = Jsoup.parse(htmlSource);
                Elements matches = docL.select("a.IconMarkets");
                int count = 0;
                while ((matches == null || matches.isEmpty()) && count++ < 10) {
                    Thread.sleep(500);
                    htmlSource = driver.getPageSource();
                    docL = Jsoup.parse(htmlSource);
                    matches = docL.select("a.IconMarkets");
                }
                if (matches == null) {
                    continue;
                }
                for (Element matchA : matches) {
                    matchesSet.add(matchA.attr("href"));
                }
            }
            returnWebDriverToPool(driver);
            driver = null;
            //https://www.sbobet.com/euro/football/english-premier-league/1623725/aston-villa-vs-queens-park-rangers
            for (String matchOdds : matchesSet) {
                //parseOdds("https://www.sbobet.com" + matchOdds);

                workers.add(new SBobetDownloader(this, "https://www.sbobet.com" + matchOdds));
            }
            List<Future<BasicEvent>> parserStates = new ArrayList<Future<BasicEvent>>();
            for (SBobetDownloader tasks: workers) {
                parserStates.add(downloadersTaskPool.submit(tasks));
            }
            //Thread.sleep(10 * 60 * 1000);
            for (Future<BasicEvent> f: parserStates) {
                try {
                    BasicEvent event = f.get();//5, TimeUnit.SECONDS);
                    if (event != null) {
                        events.add(f.get());
                    }
                    f.cancel(true);
                } catch (Exception e) {
                    logger.error("SBobetParser processGetBets: future get() exception - ", e);
                    f.cancel(true);
                }
            }
            //downloadersTaskPool.shutdown();
        } catch (Exception e) {
            logger.error("SBobetParser error", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    private static class SBobetDownloader extends BaseDownloaderSingle {

        SBobetParser parser;
        String link;

        public SBobetDownloader(SBobetParser parser, String link) {
            this.parser = parser;
            this.link = link;
        }

        @Override
        public BasicEvent download() {
            return parser.parseOdds(link);
        }
    }

    private BasicEvent parseOdds(String eventLink) {
        BasicEvent event = new BasicEvent();

        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            driver.get(eventLink);
            Thread.sleep(2000);
            Document doc = Jsoup.parse(driver.getPageSource());
            int count = 0;
            Element eventSection = doc.getElementById("event-section");
            while (eventSection == null && count++ < 10) {
                Thread.sleep(500);
                doc = Jsoup.parse(driver.getPageSource());
                eventSection = doc.getElementById("event-section");
            }
            if (eventSection == null) {
                return null;
            }
            String[] teams = eventSection.select("div.HdTitle").first().select("span").first().text().split("-vs-");
            event.team1 = Teams.getTeam(teams[0].trim());
            event.team2 = Teams.getTeam(teams[1].trim());
            if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                logger.warn("SBobetParser - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                return null;
            }
            event.date = eventSection.select("div.EventInfo").first().text();
            event.link = eventLink;
            event.site = BetSite.SBOBET;
            event.day = DateFormatter.format(event.date.substring(0, 6) + new DateTime().getYear(), betSite);
            //Calendar c = Calendar.getInstance();
            //c.setTime(event.day);
            //c.add(Calendar.DATE, -1);
            //event.day = c.getTime();
            Elements markets = eventSection.select("div.MarketT.Open");
            for (Element market: markets) {
                String title = market.select("span").first().text();
                if (title.equalsIgnoreCase("1X2")) {
                    Elements line = market.select("td");
                    if (line.size() == 3) {
                        String p1 = line.get(0).select("span.OddsR").text();
                        if (p1 != null) {
                            event.bets.put(BetType.P1, p1);
                        }
                        String pX = line.get(1).select("span.OddsR").text();
                        if (pX != null) {
                            event.bets.put(BetType.X, pX);
                        }
                        String p2 = line.get(2).select("span.OddsR").text();
                        if (p2 != null) {
                            event.bets.put(BetType.P2, p2);
                        }
                    }
                } else if (title.equalsIgnoreCase("Over Under")) {
                    Elements lines = market.select("td");
                    for (Element line: lines) {
                        final Element type = line.select("span.OddsL").first();
                        final Element val = line.select("span.OddsM").first();
                        final Element k = line.select("span.OddsR").first();
                        if (type != null && val != null && k != null) {
                            BetType bet = null;
                            if (type.text().trim().equalsIgnoreCase("Over")) {
                                bet = BetDataMapping.betsTotal.get(val.text().trim() + "M");
                            } else if (type.text().trim().equalsIgnoreCase("Under")) {
                                bet = BetDataMapping.betsTotal.get(val.text().trim() + "L");
                            }
                            if (bet != null) {
                                event.bets.put(bet, k.text().trim());
                            }

                        }
                    }
                } else if (title.equalsIgnoreCase("Double Chance")) {
                    Elements line = market.select("td");
                    if (line.size() == 3) {
                        String p1X = line.get(0).select("span.OddsR").text();
                        if (p1X != null) {
                            event.bets.put(BetType.P1X, p1X);
                        }
                        String p12 = line.get(1).select("span.OddsR").text();
                        if (p12 != null) {
                            event.bets.put(BetType.P12, p12);
                        }
                        String p2X = line.get(2).select("span.OddsR").text();
                        if (p2X != null) {
                            event.bets.put(BetType.P2X, p2X);
                        }
                    }
                } else if (title.equalsIgnoreCase("Asian Handicap")) {
                    Elements lines = market.select("tr");
                    for (Element line: lines) {
                        Elements tds = line.select("td");
                        if (tds.size() == 2) {
                            //f1
                            final Element valF1 = tds.get(0).select("span.OddsM").first();
                            final Element kF1 = tds.get(0).select("span.OddsR").first();
                            if (valF1 != null && kF1 != null) {
                                BetType f1 = BetDataMapping.betsTotal.get(valF1.text().replace("+", "") + "F1");
                                if (f1 != null) {
                                    event.bets.put(f1, kF1.text().trim());
                                }
                            }
                            //f2
                            final Element valF2 = tds.get(1).select("span.OddsM").first();
                            final Element kF2 = tds.get(1).select("span.OddsR").first();
                            if (valF2 != null && kF2 != null) {
                                BetType f2 = BetDataMapping.betsTotal.get(valF2.text().replace("+", "") + "F2");
                                if (f2 != null) {
                                    event.bets.put(f2, kF2.text().trim());
                                }
                            }
                        }
                    }
                }
            }
            //event-section
            EventSender.sendEvent(event);
            return event;
        } catch (Exception e) {
            logger.error("SBobetParser parseOdds error",e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return null;
    }
}
