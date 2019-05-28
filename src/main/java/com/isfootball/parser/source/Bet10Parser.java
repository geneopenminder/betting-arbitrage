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
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Evgeniy Pshenitsin on 22.04.2015.
 */
public class Bet10Parser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(10, getThreadFactory(Bet10Parser.class.getSimpleName()));

    public Bet10Parser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.BET_10;
    }

    private static class Bet10Downloader extends BaseDownloader {

        Bet10Parser parser;
        List<Element> checkList;

        public Bet10Downloader(Bet10Parser parser, List<Element> checkList) {
            this.parser = parser;
            this.checkList = checkList;
        }

        @Override
        public List<BasicEvent> download() {
            return parser.parseLeagues(checkList);
        }
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();

        List<Bet10Downloader> workers = new ArrayList<Bet10Downloader>();
        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            driver.get("http://ru.10bet.com/sports/%D1%84%D1%83%D1%82%D0%B1%D0%BE%D0%BB/");
            Thread.sleep(20000);
            String pageSource = driver.getPageSource();
            /*List<WebElement> frames = driver.findElements(By.tagName("iframe"));
            if (frames.size() > 1) {
                driver.switchTo().frame(frames.get(frames.size() - 1));
                try {
                    driver.findElement(By.className("wb-close")).click();
                } catch (Exception e) {
                    logger.info("Bet10Parser exception:",e);
                }
            }*/
            Thread.sleep(20000);
            Document doc = Jsoup.parse(pageSource);
            //
            /*try {
                String funcCloseFuckingWindow = "if (typeof(parent.lpMTagConfig.lpm.utils)!='undefined'){var evData = {htmlEvent: event, elemId:this.id, entityId:3231712};\n" +
                        "\t\t\treturn parent.lpMTagConfig.lpm.utils.publishEvent('LPM_CLOSE_CLICKED', evData);}";
                ((JavascriptExecutor) driver).executeScript(funcCloseFuckingWindow);
            } catch (Exception e) {

            }*/
            //logger.info(driver.getPageSource());
            Element allLeagues = doc.select("li.all_leagues").first();
            if (allLeagues != null) {
                List<String> matchLinks = new ArrayList<String>();
                Elements leagueCheck = allLeagues.select("li.league_check");


                List<List<Element>> partitions = new ArrayList<List<Element>>();
                int count = 0;//max 10
                List<Element> part = new ArrayList<Element>();
                for (Element check: leagueCheck) {
                    part.add(check);
                    if (count++ > 8) {
                        partitions.add(part);
                        count = 0;
                        part = new ArrayList<Element>();
                    }
                }
                if (!part.isEmpty()) {
                    partitions.add(part);
                }

                returnWebDriverToPool(driver);
                driver = null;
                logger.info("Bet10Parser part size: " + partitions.size());
                for (List<Element> checkList: partitions) {
                    //parseOdds(event);
                    workers.add(new Bet10Downloader(this, checkList));
                }
                List<Future<List<BasicEvent>>> parserStates = new ArrayList<Future<List<BasicEvent>>>();
                for (Bet10Downloader tasks: workers) {
                    parserStates.add(downloadersTaskPool.submit(tasks));
                }
                events = new ArrayList<BasicEvent>();
                for (Future<List<BasicEvent>> f: parserStates) {
                    try {
                        List<BasicEvent> eventList = f.get();
                        if (eventList != null) {
                            events.addAll(f.get());
                        }
                        f.cancel(true);
                    } catch (Exception e) {
                        logger.error("Bet10 processGetBets: future get() exception - ", e);
                        f.cancel(true);
                    }
                }
            } else {
                logger.error("Bet10Parser no leagues error!");
            }

        } catch (Exception e) {
            logger.error("Bet10Parser exception - ", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    private List<BasicEvent> parseLeagues(List<Element> checkList) {
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            driver.get("http://ru.10bet.com/sports/%D1%84%D1%83%D1%82%D0%B1%D0%BE%D0%BB/");
            Thread.sleep(15000);
            /*List<WebElement> frames = driver.findElements(By.tagName("iframe"));
            if (frames.size() > 1) {
                driver.switchTo().frame(frames.get(frames.size() - 1));
                try {
                    driver.findElement(By.className("wb-close")).click();
                } catch (Exception e) {

                }
            }*/
            Thread.sleep(15000);
            for (Element check: checkList) {
                try {
                    String func = check.select("img").first().attr("onclick").split(";")[0];
                    ((JavascriptExecutor) driver).executeScript(func);
                } catch (Exception e) {
                    logger.error("Bet10Parser onclick league error", e);
                    continue; //TODO
                }
            }
            try {
                driver.findElement(By.className("view_selected")).click();
            } catch (Exception e) {
                logger.error("Bet10Parser parseLeagues;", e);
                return events;
            }
            Thread.sleep(15000);
            Document leaguesDoc = Jsoup.parse(driver.getPageSource());
            Elements typesBg = leaguesDoc.select("div.types_bg");
            if (!typesBg.isEmpty()) {
                for (Element bg : typesBg) {
                    if (!bg.hasClass("groupList")) {
                        Elements time = bg.select("div.time");
                        Elements matches = bg.select("div.bets");
                        if (time.size() == matches.size()) {
                            for (int i = 1; i < time.size(); i++) {
                                try {
                                    BasicEvent event = new BasicEvent();
                                    event.date = time.get(i).text().trim();
                                    event.day = DateFormatter.format(event.date.substring(0, 5) + "/" + new DateTime().getYear(), betSite);
                                    if (event.day == null) {
                                        continue;
                                        //live
                                    }
                                    String teams[] = matches.get(i).select("dt.team_betting").first().text().split(" vs ");
                                    event.team1 = Teams.getTeam(teams[0].trim());
                                    event.team2 = Teams.getTeam(teams[1].trim());
                                    if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                                        logger.warn("Bet10Parser - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                                        continue;
                                    }
                                    event.site = BetSite.BET_10;
                                    event.link = "ru.10bet.com";
                                    logger.info("Bet10 add event");
                                    //javascript:LeagueWindow.openMasterEvent(13607,4214901)
                                    try {
                                        Element mainOdds = matches.get(i).select("dd").first();
                                        if (mainOdds != null) {
                                            Elements odds = mainOdds.select("li");
                                            if (odds.size() == 3) {
                                                event.bets.put(BetType.P1, odds.get(0).text().trim());
                                                event.bets.put(BetType.X, odds.get(1).text().trim());
                                                event.bets.put(BetType.P2, odds.get(2).text().trim());
                                            }
                                        }
                                        if (matches.get(i).select("span.more").select("a").first() != null) {
                                            String more = matches.get(i).select("span.more").select("a").first().attr("href");
                                            //javascript:
                                            ((JavascriptExecutor) driver).executeScript(more.substring(11) + ";");
                                            String id = "wlg_" + more.substring(more.indexOf("(") + 1, more.indexOf(","));
                                            Thread.sleep(1500);
                                            Document docMatchOdds = Jsoup.parse(driver.getPageSource());
                                            Element aaa = docMatchOdds.select("a.wb-close").first();
                                            if (aaa != null) {

                                            }
                                            Element addOdds = docMatchOdds.getElementById(id);
                                            if (addOdds != null) {
                                                Elements betTypes = addOdds.select("div.bet_type");
                                                for (Element betType : betTypes) {
                                                    if (betType.hasClass("double_chance") && betType.select("span").first() != null
                                                            && betType.select("span").first().text().equalsIgnoreCase("Двойной шанс")) {
                                                        Elements dd = betType.select("dd");
                                                        if (dd.size() == 3) {
                                                            event.bets.put(BetType.P1X, dd.get(0).text().trim());
                                                            event.bets.put(BetType.P12, dd.get(2).text().trim());
                                                            event.bets.put(BetType.P2X, dd.get(1).text().trim());
                                                        }
                                                    } else if (betType.hasClass("over_time") && betType.select("span").first() != null
                                                            && betType.select("span").first().text().equalsIgnoreCase("Всего голов")) {
                                                        Elements lis = betType.select("li");
                                                        for (Element li : lis) {
                                                            parseTotal(event, li);
                                                        }
                                                    } else if (betType.hasClass("asians") && betType.select("span").first() != null &&
                                                            betType.select("span").first().text().contains("Окончательный результат")) {
                                                        Elements uls = betType.select("ul.ulAssianGap");
                                                        for (Element ul : uls) {
                                                            parseFora(event, ul);
                                                        }
                                                    }
                                                }
                                            }
                                            String funcClose = "MasterEventWindow.closeWindow(" + more.substring(more.indexOf(",") + 1, more.indexOf(")")) + ");";
                                            ((JavascriptExecutor) driver).executeScript(funcClose);
                                            //MasterEventWindow.closeWindow(4234451)
                                            //li id="wlg_13607"
                                        }
                                    } catch (Exception e) {
                                        logger.error("Bet10Parser close windows error", e);
                                    }
                                    events.add(event);
                                    EventSender.sendEvent(event);
                                } catch (Exception e) {
                                    logger.error("Bet10Parser parse event error", e);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Bet10Parser exception - ", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    private void parseTotal(BasicEvent event, Element total) {
        try {
            String[] dt = total.select("dt").first().text().split(" ");
            BetType totalBet = null;
            if (dt[0].trim().equalsIgnoreCase("Больше")) {
                totalBet = BetDataMapping.betsTotal.get(dt[1].trim() + "M");
            } else if (dt[0].trim().equalsIgnoreCase("Меньше")) {
                totalBet = BetDataMapping.betsTotal.get(dt[1].trim() + "L");
            }
            String totalVal = total.select("dd").first().text().trim();
            if (totalBet != null && !totalVal.isEmpty()) {
                event.bets.put(totalBet, totalVal);
            }
        } catch (Exception e) {
            logger.error("Bet10Parser parseTotal error", e);
        }

    }

    private void parseFora(BasicEvent event, Element fora) {
        try {
            Elements first = fora.select("li.first");
            if (first.size() == 2) {
                Element fora1 = first.get(0);
                String fora1val = fora1.select("dd").first().text().trim();
                String typeFinal = "";
                if (fora1.select("dt").first().text().contains(",")) {
                    String[] fora1type = fora1.select("dt").first().text().split(",");
                    typeFinal = Double.toString((Double.parseDouble(fora1type[0]) + Double.parseDouble(fora1type[1])) / 2.0);
                } else {
                    typeFinal = fora1.select("dt").first().text().replace("+", "");
                }
                BetType f1Bet = BetDataMapping.betsTotal.get(typeFinal + "F1");
                if (f1Bet != null && !fora1val.isEmpty()) {
                    event.bets.put(f1Bet, fora1val);
                }

                Element fora2 = first.get(1);
                String fora2val = fora2.select("dd").first().text().trim();
                String type2Final = "";
                if (fora2.select("dt").first().text().contains(",")) {
                    String[] fora2type = fora2.select("dt").first().text().split(",");
                    type2Final = Double.toString((Double.parseDouble(fora2type[0]) + Double.parseDouble(fora2type[1])) / 2.0);
                } else {
                    type2Final = fora2.select("dt").first().text().replace("+", "");
                }
                BetType f2Bet = BetDataMapping.betsTotal.get(type2Final + "F2");
                if (f2Bet != null && !fora2val.isEmpty()) {
                    event.bets.put(f2Bet, fora2val);
                }
            }
        } catch (Exception e) {
            logger.error("Bet10Parser parseFora error", e);
        }
    }

}
