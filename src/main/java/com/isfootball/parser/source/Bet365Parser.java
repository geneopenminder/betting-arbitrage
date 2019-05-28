package com.isfootball.parser.source;

import com.isfootball.model.BasicEvent;
import com.isfootball.parser.BaseParser;
import com.isfootball.parser.BetSite;
import com.isfootball.parser.BetType;
import com.isfootball.parser.Teams;
import com.isfootball.pool.BasePoolInterface;
import com.isfootball.processing.EventSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Evgeniy Pshenitsin on 06.09.2015.
 */
public class Bet365Parser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    static DateFormat format = new SimpleDateFormat("dd MMMyyyy", Locale.ENGLISH);
    //3 May 2015

    ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(50, getThreadFactory(Bet365Parser.class.getSimpleName()));

    //List<BetfairMatchDownloader> workers = new ArrayList<BetfairMatchDownloader>();
    //List<BetfairLeagueDownloader> workersLeague = new ArrayList<BetfairLeagueDownloader>();

    public Bet365Parser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.BET365;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();

        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            driver.get("https://mobile.bet365.com/#type=Splash;key=1;ip=0;lng=1");
            //<li class="sporticon_1" data-nav="1">Soccer</li>
            WebElement splashContent = driver.findElement(By.id("SplashContent"));

            int counter = 0;
            while (splashContent == null && counter++ < 20) {
                Thread.sleep(200);
                splashContent = driver.findElement(By.id("SplashContent"));
            }
            if (splashContent != null) {
                List<WebElement> oddsTypes = splashContent.findElements(By.cssSelector("div.enhancedPod.splash"));
                for (WebElement e: oddsTypes) {
                    final String text = e.findElement(By.tagName("h1")).getText().trim();
                    final String eventWrapperId = e.findElement(By.cssSelector("div.eventWrapper")).getAttribute("id");
                    if (text.equalsIgnoreCase("Full Time Result")) {
                        //full time
                        List<WebElement> ftLinks = e.findElements(By.cssSelector("div.podSplashRow"));
                        for (WebElement ftLink: ftLinks) {
                            final String dataSportskey = ftLink.getAttribute("data-sportskey");
                            final String ftText = ftLink.getText().trim();
                            final String linkLeague = "https://mobile.bet365.com/V6/sport/coupon/coupon.aspx?zone=3&isocode=RU&tzi=1&key=" +
                                    dataSportskey +
                                    "&ip=0&gn=0&cid=1&lng=1&ct=158&clt=9996&ot=1&pk=1";
                            Document doc = Jsoup.connect(linkLeague)
                                    .userAgent("Mozilla/5.0 (Linux; Android 4.0.4; Galaxy Nexus Build/IMM76B) AppleWebKit/535.19")
                                    .timeout(10 * 1000).get();
                            Elements divs = doc.getElementById("Coupon").select("div.liveAlertKey").first().select("div");
                            Date time = null;
                            for (Element el: divs) {
                                if (el.hasClass("podHeaderRow")) {
                                    String date = el.select("div.wideLeftColumn").first().text().trim().substring(4);
                                    date = date + Calendar.getInstance().get(Calendar.YEAR);
                                    time = format.parse(date);
                                } else if (el.hasClass("podEventRow")) {
                                    if (time == null) {
                                        continue;
                                    }
                                    BasicEvent event = new BasicEvent();
                                    String[] teams = el.select("div.wideLeftColumn").first().html().split("<br>");
                                    if (teams != null) {
                                        //replaceAll("(\r\n|\n)",
                                        event.team1 = Teams.getTeam(teams[0].replaceAll("\r\n|\n", "").trim());
                                        event.team2 = Teams.getTeam(teams[1].replaceAll("\r\n|\n", "").trim());
                                        if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                                            logger.warn("Bet365 - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                                            continue;
                                        }
                                    }
                                    event.site = BetSite.BET365;
                                    event.day = time;
                                    event.date = time.toString();
                                    event.link = linkLeague;
                                    Elements tds = el.select("div.priceColumn");
                                    if (tds.size() == 3) {
                                        String p1 = tds.get(0).text().trim();
                                        event.bets.put(BetType.P1, decimalFromFractional(p1));
                                        String X = tds.get(1).text().trim();
                                        event.bets.put(BetType.X, decimalFromFractional(X));
                                        String p2 = tds.get(2).text().trim();
                                        event.bets.put(BetType.P2, decimalFromFractional(p2));
                                        events.add(event);
                                        EventSender.sendEvent(event);
                                    }
                                }
                            }
                        }
                    } else if (text.equalsIgnoreCase("Goals Over/Under")) {

                    }

                }
            } else {
                logger.error("Bet365Parser SplashContent is null!!!");
            }
        } catch (Exception e) {
            logger.error("Bet365Parser exception - ", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    private static String decimalFromFractional(String fractional) {
        if (!fractional.contains("/")) {
            return null;
        } else {
            String[] parts = fractional.split("/");
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
            otherSymbols.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("####.##", otherSymbols);
            return df.format(1.0 + Double.parseDouble(parts[0])/Double.parseDouble(parts[1]));
        }
    }

}
