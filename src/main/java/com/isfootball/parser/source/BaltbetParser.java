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
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.*;

/**
 * Created by Evgeniy Pshenitsin on 02.05.2015.
 */
public class BaltbetParser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(50, getThreadFactory(BaltbetParser.class.getSimpleName()));

    private ConcurrentMap<Long, Long> parsedEvents = null;

    public BaltbetParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.BALTBET;
    }

    private static class BaltbetDownloader extends BaseDownloader {

        BaltbetParser parser;

        public BaltbetDownloader(BaltbetParser parser) {
            this.parser = parser;
        }

        @Override
        public List<BasicEvent> download() {
            return parser.getEventsInternal();
        }
    }

    private List<BasicEvent> getEventsInternal() {
        final long threadId = Thread.currentThread().getId();

        List<BasicEvent> events = new ArrayList<>();
        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            driver.get("http://www.baltbet.com/Line2.aspx?group=1");
            Thread.sleep(1000);
            String htmlSource = driver.getPageSource();

            List<WebElement> tablesW = driver.findElements(By.cssSelector("table.lvmain.coef-tobasket"));

            Document doc = Jsoup.parse(htmlSource);
            Elements tables = doc.select("table.lvmain.coef-tobasket");
            boolean isWoman = false;
            for (Element table: tables) {
                Elements matches = table.select("tr");
                for (Element tr: matches) {

                    if (tr.hasClass("head")) {
                        final String leagueName = tr.text();
                        if (leagueName.contains("Женщины")) {
                            isWoman = true;
                        } else {
                            isWoman = false;
                        }
                        continue;
                    }
                    if (isWoman) {
                        continue;
                    }
                    Elements td = tr.select("td");
                    if (td.size() > 5) {
                        try {
                            //get match id
                            final long matchId = Long.parseLong(td.get(0).text().trim());
                            if (parsedEvents.putIfAbsent(matchId, threadId) != null) {
                                continue;
                            }

                            BasicEvent event = new BasicEvent();
                            event.date = td.get(1).text().trim();
                            event.day = DateFormatter.format(event.date, betSite);
                            String[] teams = td.get(3).text().split("-");
                            event.team1 = Teams.getTeam(teams[0].trim());
                            event.team2 = Teams.getTeam(teams[1].trim());
                            if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                                logger.warn("BaltbetParser - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                                continue;
                            }
                            event.site = BetSite.BALTBET;
                            event.link = "http://www.baltbet.com/Line2.aspx?group=1";
                            String p1 = td.get(4).text().trim().replace(",",".");
                            if (!p1.isEmpty()) {
                                event.bets.put(BetType.P1, p1);
                            }
                            String px = td.get(5).text().trim().replace(",",".");
                            if (!px.isEmpty()) {
                                event.bets.put(BetType.X, px);
                            }
                            String p2 = td.get(6).text().trim().replace(",", ".");
                            if (!p2.isEmpty()) {
                                event.bets.put(BetType.P2, p2);
                            }

                            String p1x = td.get(7).text().trim().replace(",",".");
                            if (!p1x.isEmpty()) {
                                event.bets.put(BetType.P1X, p1x);
                            }
                            String p12 = td.get(8).text().trim().replace(",", ".");
                            if (!p12.isEmpty()) {
                                event.bets.put(BetType.P12, p12);
                            }
                            String p2x = td.get(9).text().trim().replace(",", ".");
                            if (!p2x.isEmpty()) {
                                event.bets.put(BetType.P2X, p2x);
                            }

                            BetType f1Bet = BetDataMapping.betsTotal.get(td.get(10).text().trim().replace("+", "").replace(",",".") + "F1");
                            String f1Val = td.get(11).text().trim().replace(",", ".");
                            if (f1Bet != null && !f1Val.isEmpty()) {
                                event.bets.put(f1Bet, f1Val);
                            }
                            BetType f2Bet = BetDataMapping.betsTotal.get(td.get(12).text().trim().replace("+","").replace(",",".") + "F2");
                            String f2Val = td.get(13).text().trim().replace(",",".");
                            if (f2Bet != null && !f2Val.isEmpty()) {
                                event.bets.put(f2Bet, f2Val);
                            }

                            String total = td.get(14).text().trim().replace(",", ".");
                            BetType totalU = BetDataMapping.betsTotal.get(total + "L");
                            BetType totalO = BetDataMapping.betsTotal.get(total + "M");
                            String totalOVal = td.get(15).text().trim().replace(",", ".");
                            String totalUVal = td.get(16).text().trim().replace(",", ".");
                            if (totalU != null && !totalUVal.isEmpty()) {
                                event.bets.put(totalU, totalUVal);
                            }
                            if (totalO != null && !totalOVal.isEmpty()) {
                                event.bets.put(totalO, totalOVal);
                            }

                            Element a = td.get(td.size() - 1).select("a").first();
                            if (a != null) {
                                //javascript:PreShowAddCoefs('1318056')
                                try {
                                    String command = a.attr("href").trim().substring(11) + ";";
                                    ((JavascriptExecutor) driver).executeScript(command);
                                    Thread.sleep(700);
                                    Document addOdds = Jsoup.parse(driver.getPageSource());
                                    //addrow1318056
                                    Element moreOdds = addOdds.getElementById("addrow" + command.substring(command.indexOf("(") + 2,command.indexOf(")") - 1));
                                    if (moreOdds != null) {
                                        Element lv1 = moreOdds.select("table.lv1").first();
                                        if (lv1 != null) {
                                            Elements trs = lv1.select("tr");
                                            for (int i = 1; i < trs.size(); i++) {
                                                Element oddsLine = trs.get(i);
                                                Elements values = oddsLine.select("td");
                                                if (values.size() == 7) {
                                                    BetType f1 = BetDataMapping.betsTotal.get(values.get(0).text().trim().replace("+", "").replace(",",".") + "F1");
                                                    String f1V = values.get(1).text().trim().replace(",", ".");
                                                    if (f1 != null && !f1V.isEmpty()) {
                                                        event.bets.put(f1, f1V);
                                                    }
                                                    BetType f2 = BetDataMapping.betsTotal.get(values.get(2).text().trim().replace("+","").replace(",",".") + "F2");
                                                    String f2V = values.get(3).text().trim().replace(",",".");
                                                    if (f2 != null && !f2V.isEmpty()) {
                                                        event.bets.put(f2, f2V);
                                                    }
                                                    String totalMore = values.get(4).text().trim().replace(",", ".");
                                                    BetType totalUnder = BetDataMapping.betsTotal.get(totalMore + "L");
                                                    BetType totalOver = BetDataMapping.betsTotal.get(totalMore + "M");
                                                    String totalOverVal = values.get(5).text().trim().replace(",", ".");
                                                    String totalUnderVal = values.get(6).text().trim().replace(",", ".");
                                                    if (totalUnder != null && !totalUnderVal.isEmpty()) {
                                                        event.bets.put(totalUnder, totalUnderVal);
                                                    }
                                                    if (totalOver != null && !totalOverVal.isEmpty()) {
                                                        event.bets.put(totalOver, totalOverVal);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    ((JavascriptExecutor) driver).executeScript(command);
                                    Thread.sleep(500);
                                } catch (Exception e) {
                                    logger.error("BaltbetParser execute script err", e);
                                }
                                events.add(event);
                                EventSender.sendEvent(event);
                            }
                        } catch (Exception e) {
                            logger.error("BaltbetParser parse event error", e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("BaltbetParser exception - ", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();

        parsedEvents = new ConcurrentHashMap<Long, Long>();

        List<BaltbetDownloader> workers = new ArrayList<BaltbetDownloader>();
        try {
            for (int i = 0; i < 5; i++) {
                workers.add(new BaltbetDownloader(this));
            }
            List<Future<List<BasicEvent>>> parserStates = new ArrayList<Future<List<BasicEvent>>>();
            for (BaltbetDownloader tasks: workers) {
                parserStates.add(downloadersTaskPool.submit((Callable)tasks));
            }
            for (Future<List<BasicEvent>> f: parserStates) {
                try {
                    List<BasicEvent> event = f.get();
                    if (event != null) {
                        events.addAll(f.get());
                    }
                    f.cancel(true);
                } catch (Exception e) {
                    logger.error("BaltbetParser processGetBets: future get() exception - ", e);
                    f.cancel(true);
                }
            }

        } catch (Exception e) {
            logger.error("BaltbetParser exception - ", e);
        }
        return events;
    }

}
