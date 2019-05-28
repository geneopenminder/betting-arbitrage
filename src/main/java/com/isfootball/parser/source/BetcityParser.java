package com.isfootball.parser.source;

import com.isfootball.model.BasicEvent;
import com.isfootball.parser.*;
import com.isfootball.pool.BasePoolInterface;
import com.isfootball.processing.EventSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.openqa.selenium.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by Evgeniy Pshenitsin on 14.04.2015.
 */
public class BetcityParser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(50, getThreadFactory(BetcityParser.class.getSimpleName()));

    public BetcityParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.BETCITY;
    }

    private static class BetcityDownloader extends BaseDownloader {

        BetcityParser parser;
        List<String> leagues;

        public BetcityDownloader(BetcityParser parser, List<String> leagues) {
            this.parser = parser;
            this.leagues = leagues;
        }

        @Override
        public List<BasicEvent> download() {
            List<BasicEvent> events = new ArrayList<BasicEvent>();
            WebDriver driver = null;
            try {
                driver = parser.getWebDriverFromPool();
                //driver.manage().addCookie(new Cookie("clng", "1"));
                //https://www.betsbc.com/bets/bets.php?line_id[]=1 - old
                driver.get("https://betsbc.com/app/#/line/?line_id%5B%5D=1");
                Thread.sleep(3000);
                String pSource = driver.getPageSource();
                List<WebElement> checkboxes = driver.findElements(By.xpath("//input[@ng-click='changeOne']"));
                for (WebElement checkbox: checkboxes) {
                    final String value = checkbox.getAttribute("id");
                    for (String leagueId: leagues) {
                        if (value.equalsIgnoreCase(leagueId)) {
                            checkbox.click();
                        }
                    }
                }
                driver.findElement(By.id("btn_submit")).click(); //Show
                Thread.sleep(3000);
                pSource = driver.getPageSource();
                Document doc  = Jsoup.parse(driver.getPageSource());
                Elements tables = doc.select("table");

                List<Element> mainTables = new ArrayList<>();

                for (Element table: tables) {
                    try {
                        Attributes atr = table.attributes();
                        final boolean isHeader = atr.asList().stream().anyMatch((Attribute a) -> {
                            if (a.getKey().equals("id") && a.getValue().startsWith("c")) {
                                return true;
                            }
                            return false;
                        });

                        if (isHeader) {
                            String headTitle = table.select("tr").first().select("td").text();
                            if (headTitle.contains("Женщины") ||
                                    headTitle.contains("21-го года") ||
                                    headTitle.contains("17 лет") ||
                                    headTitle.contains("18 лет") ||
                                    headTitle.contains("19 лет") ||
                                    headTitle.contains("20 лет") ||
                                    headTitle.contains("Статистика") ||
                                    headTitle.contains("Итоговый") ||
                                    headTitle.contains("больше") ||
                                    headTitle.contains("голов") ||
                                    headTitle.contains("Кто") ||
                                    headTitle.contains("молодёжных") ||
                                    headTitle.contains("УЕФА")
                                    || headTitle.contains("Лига Европы")) {
                                continue;
                            }
                            mainTables.add(table);
                        }

                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }



                for (Element table: mainTables) {
                    try {

                        Element mainBody = table.select("tbody").stream().filter(element -> {
                            final boolean isHeader = element.attributes().asList().stream().anyMatch((Attribute a) -> {
                                if (a.getKey().equals("id") && a.getValue().startsWith("event-repeat")) {
                                    return true;
                                }
                                return false;
                            });
                            return isHeader;
                        }).findFirst().get();

                        if (mainBody == null) {
                            continue;
                        }
                        String date = "";
                        for (Element trLine : mainBody.select("tr")) {
                            if (trLine.hasAttr("data-reactid") && trLine.select("td").size() == 1) {
                                date = trLine.text().isEmpty() ? date: trLine.text();
                            } else if (trLine.hasAttr("id") && trLine.attr("id").startsWith("main_out_")) {
                                Elements td = trLine.select("tr").first().select("td");
                                if (td.size() == 18) {
                                    BasicEvent event = new BasicEvent();
                                    String team1 = td.get(1).text().trim();
                                    String team2 = td.get(2).text().trim();
                                    event.team1 = Teams.getTeam(team1.trim());
                                    event.team2 = Teams.getTeam(team2.trim());
                                    if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                                        logger.warn("BetcityParser - unknown teams: " + team1 + ":" + event.team1 + ";" + team2 + ":" + event.team2);
                                        continue;
                                    }
                                    event.date = date;
                                    event.day = DateFormatter.format(date, BetSite.BETCITY);
                                    if (event.day == null) {continue;}
                                    event.site = BetSite.BETCITY;
                                    event.link = "betsbc.com";
                                    String p1 = td.get(3).text().trim();
                                    if (p1 != null && !p1.isEmpty()) {
                                        event.bets.put(BetType.P1, p1);
                                    }
                                    String pX = td.get(4).text().trim();
                                    if (pX != null && !pX.isEmpty()) {
                                        event.bets.put(BetType.X, pX);
                                    }
                                    String p2 = td.get(5).text().trim();
                                    if (p2 != null && !p2.isEmpty()) {
                                        event.bets.put(BetType.P2, p2);
                                    }
                                    String p1X = td.get(6).text().trim();
                                    if (p1X != null && !p1X.isEmpty()) {
                                        event.bets.put(BetType.P1X, p1X);
                                    }
                                    String p12 = td.get(7).text().trim();
                                    if (p12 != null && !p12.isEmpty()) {
                                        event.bets.put(BetType.P12, p12);
                                    }
                                    String p2X = td.get(8).text().trim();
                                    if (p2X != null && !p2X.isEmpty()) {
                                        event.bets.put(BetType.P2X, p2X);
                                    }

                                    BetType fora1Bet = BetDataMapping.betsTotal.get(td.get(9).text().replace("+", "").trim() + "F1");
                                    String fora1Val = td.get(10).text().trim();
                                    if (fora1Bet != null && !fora1Val.isEmpty()) {
                                        event.bets.put(fora1Bet, fora1Val);
                                    }
                                    BetType fora2Bet = BetDataMapping.betsTotal.get(td.get(11).text().replace("+", "").trim() + "F2");
                                    String fora2Val = td.get(12).text().trim();
                                    if (fora2Bet != null && !fora2Val.isEmpty()) {
                                        event.bets.put(fora2Bet, fora2Val);
                                    }
                                    //13
                                    String totalVal = td.get(13).text().trim();
                                    BetType totalL = BetDataMapping.betsTotal.get(totalVal + "L");
                                    BetType totalM = BetDataMapping.betsTotal.get(totalVal + "M");
                                    String totalLVal = td.get(14).text().trim();
                                    String totalMVal = td.get(15).text().trim();
                                    if (totalL != null && !totalLVal.isEmpty()) {
                                        event.bets.put(totalL, totalLVal);
                                    }
                                    if (totalM != null && !totalMVal.isEmpty()) {
                                        event.bets.put(totalM, totalMVal);
                                    }

                                    events.add(event);
                                    EventSender.sendEvent(event);

                                    //showdop

                                    /*
                                    String onclick = td.get(16).attr("onclick");
                                    if (onclick != null) {
                                        final String id = "tr" + td.get(16).attr("id").substring(1);
                                        //event.link = id;
                                        try {
                                            ((JavascriptExecutor) driver).executeScript("this." + onclick);
                                            WebElement eee = driver.findElement(By.id(id));
                                            int backCount = 0;
                                            while (eee == null && backCount < 30) {
                                                Thread.sleep(100);
                                                eee = driver.findElement(By.id(id));
                                                backCount++;
                                            }
                                            backCount = 0;
                                            if (eee != null) {
                                                List<WebElement> divs = eee.findElements(By.cssSelector("div"));
                                                for (WebElement div: divs) {
                                                    final String text = div.getText();
                                                    if (text.startsWith("Фора:")) {
                                                        parser.parseFora(text, event);
                                                    } else if (text.contains("Тотал:")) {
                                                        parser.parseTotal(text, event);
                                                    }
                                                }
                                            }
                                        /*Document oddsDop = Jsoup.parse(driver.getPageSource());
                                        Element oddsHtml = oddsDop.getElementById("tr" + td.get(16).attr("id").substring(1));
                                        if (oddsHtml != null) {
                                            Elements divs = oddsHtml.select("div");
                                            for (Element div: divs) {
                                                final String text = div.text();
                                                if (text.contains("Дополнительные форы")) {
                                                    parseFora(text, event);
                                                } else if (text.contains("Дополнительные тоталы")) {
                                                    parseTotal(text, event);
                                                }
                                            }
                                        }
                                            ((JavascriptExecutor) driver).executeScript("this." + onclick);
                                        } catch (Exception e) {
                                            logger.error("BetcityParser add odds error", e);
                                        }
                                    }*/
                                }
                            }
                        }
                    } catch(Exception e) {
                        logger.error("", e);
                    }
                }

            /*Elements hrefs = doc.select("a");
            List<String> ids = new ArrayList<String>();
            for (Element a: hrefs) {
                String onclick = a.attr("onclick");
                String text = a.text();
                if (text.contains("Футбол") &&
                    (text.contains("Кубок ") || text.contains("Лига") || text.contains("Чемпионат")) &&
                     !text.contains("первым") && !text.contains("Спец") && !text.contains("Статистика") && !text.contains("Победитель")
                        && !text.contains("Лучший") && !text.contains("Кто") && !text.contains("Отрыв")) {
                    String onclick = a.attr("onclick");

                }
            }*/


            } catch (Exception e) {
                logger.error("BetcityDownloader exception - ", e);
            } finally {
                if (driver != null) {
                    parser.returnWebDriverToPool(driver);
                }
            }
            return events;
        }

    }


    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();

        List<BetcityDownloader> workers = new ArrayList<BetcityDownloader>();
        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            //driver.manage().addCookie(new Cookie("clng", "1"));
            /*Cookie ck = new Cookie("__utma", "58397497.631096822.1466261036.1469946984.1469949315.5");
            ck = new Cookie("__utmz", "58397497.1466261036.1.1.utmcsr=0jagqjzu1z\n" +
                    "                    .alfa.betcityaccess.com|utmccn=(referral)|utmcmd=referral|utmcct=/home/");
            driver.manage().addCookie(ck);
            ck = new Cookie("cud", "WJa60lec01q8phM8CDGwAg=\n" +
                    "            =");
            driver.manage().addCookie(ck);
            ck = new Cookie("PHPSESSID", "v4d8teeqjgt9igis3tagknjjo2");
            driver.manage().addCookie(ck);
            ck = new Cookie("__utmc", "58397497");
            driver.manage().addCookie(ck);
            ck = new Cookie("dev", "1d2c537facd0224f78130ede5f6ddfca");
            driver.manage().addCookie(ck);*/


            driver.get("https://betsbc.com/app/#/line/?line_id%5B%5D=1");
            //driver.switchTo().frame(driver.findElement(By.cssSelector("frame[src='/app/#/home/']")));
            //driver.switchTo().frame(frames.get(frames.size() - 1));
            //driver.findElement(By.id("f1")).click();
            //driver.findElement(By.xpath("//input[@value='Показать']")).click();
            //logger.info(driver.getPageSource());
            //driver.findElement(By.id(id)).click();
            //this.check_all_l2();
            final String source = driver.getPageSource();
            Thread.sleep(1000);
            List<WebElement> checkboxes = driver.findElements(By.xpath("//input[@ng-click='changeOne']"));

            final int portion = 10;
            int count = 0;
            List<String> leagues = new ArrayList<>();
            for (WebElement checkbox: checkboxes) {
                if (count++ < portion) {
                    leagues.add(checkbox.getAttribute("id"));
                } else {
                    count = 0;
                    workers.add(new BetcityDownloader(this, leagues));
                    leagues = new ArrayList<>();
                }
            }

            returnWebDriverToPool(driver);
            driver = null;

            List<Future<List<BasicEvent>>> parserStates = new ArrayList<Future<List<BasicEvent>>>();
            for (BetcityDownloader tasks: workers) {
                tasks.download();
                //parserStates.add(downloadersTaskPool.submit((Callable)tasks));
            }
            for (Future<List<BasicEvent>> f: parserStates) {
                try {
                    List<BasicEvent> event = f.get();
                    if (event != null) {
                        events.addAll(f.get());
                    }
                    f.cancel(true);
                } catch (Exception e) {
                    logger.error("BetcityParser processGetBets: future get() exception - ", e);
                    f.cancel(true);
                }
            }

        } catch (Exception e) {
            logger.error("BetcityParser exception - ", e);
        } finally {
            if (driver != null) {
               returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    private void parseFora(String text, BasicEvent event) {
        try {
            String textFinal = text;
            //Дополнительные форы: Гремиу П-А: (-4.5) 9.3; Кампиненсе: (+4.5) 1.03;
            int half = text.indexOf(";");
            if (half != -1) {
                String[] fora1 = text.substring(text.indexOf("(") + 1, half).split("\\)");
                BetType fora1Bet = BetDataMapping.betsTotal.get(fora1[0].replace("+", "").trim() + "F1");
                String fora1Val = fora1[1].trim();
                if (fora1Bet != null && !fora1Val.isEmpty()) {
                    event.bets.put(fora1Bet, fora1Val);
                }

                String[] fora2 = text.substring(text.indexOf("(", half) + 1).split("\\)");
                BetType fora2Bet = BetDataMapping.betsTotal.get(fora2[0].replace("+", "").trim() + "F2");
                String fora2Val = fora2[1].replace(";", "").trim();
                if (fora2Bet != null && !fora2Val.isEmpty()) {
                    event.bets.put(fora2Bet, fora2Val);
                }

            }
        } catch (Exception e) {
            logger.error("BetcityParser fora error: " + text, e);
        }
    }

    //                                                    //Дополнительные тоталы: (0.5) Мен 12.5; Бол 1.01;
    private void parseTotal(String text, BasicEvent event) {
        try {
            String textFinal = text;
            //Дополнительные тоталы: (0.5) Мен 12.5; Бол 1.01;
            int half = text.indexOf(";");
            if (half != -1) {
                String total = text.substring(text.indexOf("(") + 1, text.indexOf(")"));
                BetType totalL = BetDataMapping.betsTotal.get(total + "L");
                BetType totalM = BetDataMapping.betsTotal.get(total + "M");
                String[] totalLVal = text.substring(text.indexOf(")") + 1, half).split(" ");
                String totalLValFinal = totalLVal[totalLVal.length - 1].trim();
                if (totalL != null && !totalLValFinal.isEmpty()) {
                    event.bets.put(totalL, totalLValFinal);
                }
                String[]  totalMVal = text.substring(half + 1).split(" ");
                String totalMValFinal = totalMVal[totalMVal.length - 1].replace(";", "").trim();
                if (totalM != null && !totalMValFinal.isEmpty()) {
                    event.bets.put(totalM, totalMValFinal);
                }
            }
        } catch (Exception e) {
            logger.error("BetcityParser total error: " + text, e);
        }
    }

}
