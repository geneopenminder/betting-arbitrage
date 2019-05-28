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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Evgeniy Pshenitsin on 10.04.2015.
 */
public class ZenitbetParser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    //Apr 05 **:**
    static DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(50, getThreadFactory(ZenitbetParser.class.getSimpleName()));

    //pass

    final String login = "1417379";
    final String pass = "CarpiccleGis23";


    public ZenitbetParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.ZENITHBET;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            driver.get("http://zenitbet.com/account/login");


            driver.findElement(By.id("login2")).sendKeys(login);
            driver.findElement(By.id("password2")).sendKeys(pass);
            driver.findElement(By.id("auth1")).click();
            Thread.sleep(8000);

            driver.get("http://zenitbet.com/line/view");
            driver.findElement(By.id("sid1")).click();
            driver.findElement(By.id("do")).click();
            Thread.sleep(10000);
            String htmlSource = driver.getPageSource();
            Document doc = Jsoup.parse(htmlSource);
            Element sid1 = doc.getElementById("sid1");
            int counter = 0;
            while (sid1 == null && counter++ < 30) {
                Thread.sleep(300);
                htmlSource = driver.getPageSource();
                doc = Jsoup.parse(htmlSource);
                sid1 = doc.getElementById("sid1");
            }
            if (sid1 != null) {
                Elements matches = sid1.select("tr.o");
                for (Element match: matches) {
                    try {
                        Elements bets = match.select("td");
                        if (bets.size() == 16) {
                            BasicEvent event = new BasicEvent();
                            event.date = bets.get(0).text().substring(0,5) + "/" + Calendar.getInstance().get(Calendar.YEAR);
                            event.day = format.parse(event.date);
                            String[] teams = bets.get(1).text().split("-");
                            event.team1 = Teams.getTeam(teams[0].trim());
                            event.team2 = Teams.getTeam(teams[1].trim());
                            if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                                logger.warn("ZenitbetParser - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                                continue;
                            }
                            event.site = BetSite.ZENITHBET;
                            event.link = "https://zenitbet.com/line/view";
                            String p1 = bets.get(2).text().replace(",", ".");
                                if (p1 != null) {
                                    event.bets.put(BetType.P1, p1);
                                }
                            String x = bets.get(3).text().replace(",", ".");
                            if (x != null) {
                                event.bets.put(BetType.X, x);
                            }
                            String p2 = bets.get(4).text().replace(",", ".");
                            if (p2 != null) {
                                event.bets.put(BetType.P2, p2);
                            }
                            String p1x = bets.get(5).text().replace(",", ".");
                            if (p1x != null) {
                                event.bets.put(BetType.P1X, p1x);
                            }
                            String p12 = bets.get(6).text().replace(",", ".");
                            if (p12 != null) {
                                event.bets.put(BetType.P12, p12);
                            }
                            String p2x = bets.get(7).text().replace(",", ".");
                            if (p2x != null) {
                                event.bets.put(BetType.P2X, p2x);
                            }

                            //fora
                            String fora1Type = bets.get(8).text().trim() + "F1";
                            BetType f1 = BetDataMapping.betsTotal.get(fora1Type);
                            String fora1Val = bets.get(9).text().replace(",", ".");
                            if (fora1Val != null && f1 != null) {
                                event.bets.put(f1, fora1Val);
                            }
                            String fora2Type = bets.get(10).text().trim() + "F2";
                            BetType f2 = BetDataMapping.betsTotal.get(fora2Type);
                            String fora2Val = bets.get(11).text().replace(",", ".");
                            if (fora2Val != null && f2 != null) {
                                event.bets.put(f2, fora2Val);
                            }
                            String id = bets.get(15).select("a").first().attr("data-gid");
                            Object addBets = (Object)((JavascriptExecutor) driver).executeScript("window.addBets = \"\";");
                            ((JavascriptExecutor) driver)
                                    .executeScript("z.ajaxf.send({url : '/line/loadross', data: {sid : 1, gid : ['" + id +"']}, " +
                                            "dataType : 'json',async : true,type: 'get',lock: {},loading : {}, " +
                                            "success: function(data) {window.addBets = data;}, " +
                                            "error: {}, complete: {}});");
                            Thread.sleep(3000);
                            Map<String, Map<String, Map<String, Object>>> addBetsPage = (Map)((JavascriptExecutor) driver).executeScript("return window.addBets;");
                            int waitCount = 0;
                            while (addBetsPage == null && waitCount++ < 30) {
                                Thread.sleep(300);
                                addBetsPage = (Map)((JavascriptExecutor) driver).executeScript("return window.addBets;");
                            }
                            if (addBetsPage != null) {
                                for (String key: addBetsPage.get("odd").get("1").keySet()) {
                                    String oddType = (String)addBetsPage.get("odd").get("1").get(key);
                                    Map<String, Object> betsMap = (Map)addBetsPage.get("games").get(id).get("bets");
                                    Map<String, String> betsVal = (Map)betsMap.get(key);
                                    String cf = betsVal.get("cf").replace(",", ".");
                                    String type = betsVal.get("d1");
                                    BetType bet = null;
                                    if (oddType.contains("Фора") && !oddType.contains("тайм")) {
                                        if (oddType.startsWith("Фора 1")) {
                                            bet = BetDataMapping.betsTotal.get(type + "F1");
                                        } else if (oddType.startsWith("Фора 2")) {
                                            bet = BetDataMapping.betsTotal.get(type + "F2");
                                        }
                                    } else if ((oddType.contains("Меньше") || oddType.contains("Больше")) && !oddType.contains("тайм")) {
                                        if (oddType.startsWith("Меньше")) {
                                            bet = BetDataMapping.betsTotal.get(type + "L");
                                        } else if (oddType.startsWith("Больше")) {
                                            bet = BetDataMapping.betsTotal.get(type + "M");
                                        }
                                    }
                                    if (bet != null && cf != null) {
                                        event.bets.put(bet, cf);
                                    }
                                }
                            }
                            events.add(event);
                            EventSender.sendEvent(event);
                        }
                    } catch (Exception e) {
                        logger.error("ZenitbetParser match parse error");
                    }
                }
            } else {
                logger.error("ZenitbetParser can't get sid1");
            }
            returnWebDriverToPool(driver);
            driver = null;
        } catch (Exception e) {
            logger.error("ZenitbetParser exception - ", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }
}
