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
import org.openqa.selenium.WebDriver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Evgeniy Pshenitsin on 12.04.2015.
 */
public class CoralParser extends BaseParser {


    private static final Logger logger = LogManager.getLogger("parser");

    //Mon 10 Aug
    static DateFormat format = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

    ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(30, getThreadFactory(CoralParser.class.getSimpleName()));

    public CoralParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.CORAL;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        List<CoralDownloader> workers = new ArrayList<CoralDownloader>();

        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            //System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
            //driver = new ChromeDriver();
            driver.get("http://sports.coral.co.uk/football");
            driver.navigate().to("http://sports.coral.co.uk/football");
            Thread.sleep(3000);
            driver.findElement(By.id("feat-tomorrows")).click();
            Thread.sleep(3000);
            String htmlSource = driver.getPageSource();
            Document doc = Jsoup.parse(htmlSource);
            Element matches = doc.getElementById("football-matches-wrapper").select("div.in-play-odds").first()
                    .select("div.second-tabs-content.all-matches-80-min-handicap").first().select("div.matches").first();
            Calendar c = Calendar.getInstance();
            //c.add(Calendar.DATE, 1);
            if (matches != null) {
                fillMatches(matches, events, c.getTime());
            }
            driver.get("http://sports.coral.co.uk/football");
            Thread.sleep(3000);
            driver.findElement(By.id("feat-future-dropdown")).click();
            Thread.sleep(3000);
            htmlSource = driver.getPageSource();
            doc = Jsoup.parse(htmlSource);
            Elements ul = doc.getElementById("next-matches-dropdown").select("li");
            List<String> ids = new ArrayList<String>();
            for (Element li: ul) {
                String id = li.select("a").first().attr("id");
                ids.add(id);
            }
            for (String id: ids) {
                c.add(Calendar.DATE, 1);
                driver.get("http://sports.coral.co.uk/football");
                Thread.sleep(3000);
                driver.findElement(By.id("feat-future-dropdown")).click();
                Thread.sleep(3000);
                driver.findElement(By.id(id)).click();
                Thread.sleep(3000);
                htmlSource = driver.getPageSource();
                doc = Jsoup.parse(htmlSource);
                try {
                    matches = doc.getElementById("football-matches-wrapper").select("div.in-play-odds").first()
                            .select("div.second-tabs-content.all-matches-80-min-handicap").first().select("div.matches").first();
                } catch (Exception e) {
                    logger.error("CoralParser no events for id - " + id);
                    matches = null;
                }
                if (matches != null) {
                    fillMatches(matches, events, id.equalsIgnoreCase("F5") ? null: c.getTime()); //F5 future matches
                }
            }
            returnWebDriverToPool(driver);
            driver = null;
            for (BasicEvent event: events) {
                //parseOdds(event);
                workers.add(new CoralDownloader(this, event));
            }
            List<Future<BasicEvent>> parserStates = new ArrayList<Future<BasicEvent>>();
            for (CoralDownloader tasks: workers) {
                parserStates.add(downloadersTaskPool.submit(tasks));
            }
            events = new ArrayList<BasicEvent>();
            for (Future<BasicEvent> f: parserStates) {
                try {
                    BasicEvent event = f.get();
                    if (event != null) {
                        events.add(f.get());
                    }
                    f.cancel(true);
                } catch (Exception e) {
                    logger.error("CoralParser processGetBets: future get() exception - ", e);
                    f.cancel(true);
                }
            }
            //downloadersTaskPool.shutdown();
        } catch (Exception e) {
            logger.error("CoralParser exception - ", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    private static class CoralDownloader extends BaseDownloaderSingle {

        CoralParser parser;
        BasicEvent event;

        public CoralDownloader(CoralParser parser, BasicEvent event) {
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
            driver = getWebDriverFromPool();
            driver.get(event.link);
            Thread.sleep(3000);
            driver.findElement(By.id("site_pref_decimal")).click();
            String htmlSource = driver.getPageSource();
            returnWebDriverToPool(driver);
            driver = null;
            Document doc = Jsoup.parse(htmlSource);
            Element other = doc.getElementById("other-markets-wrapper");
            if (other != null) {
                Elements threeWay = other.select("div.three-way-market");
                for (Element three: threeWay) {
                    String type = three.select("span").first().text();
                    if (type != null && type.equalsIgnoreCase("Double Chance")) {
                        Elements td = three.select("tr.body-row").first().select("td");
                        if (td.size() == 3) {
                            String p1x = td.get(0).select("span.odds-decimal").first().text().trim();
                            if (p1x != null) {
                                event.bets.put(BetType.P1X, p1x);
                            }
                            String p12 = td.get(1).select("span.odds-decimal").first().text().trim();
                            if (p12 != null) {
                                event.bets.put(BetType.P12, p12);
                            }
                            String p2X = td.get(2).select("span.odds-decimal").first().text().trim();
                            if (p2X != null) {
                                event.bets.put(BetType.P2X, p2X);
                            }
                        }
                    }
                }
                Elements blocks = other.select("div.block-content.ev-layout");
                for (Element block: blocks) {
                    String type = block.select("span").first().text();
                    if (type.equalsIgnoreCase("Total Goals Over/Under")) {
                        Elements bodyRows = block.select("tr.body-row");
                        for (Element row: bodyRows) {
                            Elements first = row.select("td.first");
                            Elements last = row.select("td.last");
                            if (first.size() == 2 && last.size() == 2) {
                                String totalM = first.get(0).text().substring(5).trim();
                                String totalMVal = last.get(0).select("span.odds-decimal").text().trim();
                                BetType betM = BetDataMapping.betsTotal.get(totalM + "M");
                                if (betM != null && totalMVal != null) {
                                    event.bets.put(betM, totalMVal);
                                }
                                String totalL = first.get(1).text().substring(6).trim();
                                String totalLVal = last.get(1).select("span.odds-decimal").text().trim();
                                BetType betL = BetDataMapping.betsTotal.get(totalL + "L");
                                if (betL != null && totalLVal != null) {
                                    event.bets.put(betL, totalLVal);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("CoralParser parseOdds error for link - " + event.link, e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        EventSender.sendEvent(event);
        return event;
    }

    private void fillMatches(Element matches, List<BasicEvent> events, Date date) {
        Elements matchesList = matches.select("div.match.featured-match");
        for (Element match:  matchesList) {
            try {
                BasicEvent event = new BasicEvent();
                event.date = match.select("div.date-time").first().text().trim();
                //Mon 10 Aug
                if (date != null) {
                    event.day = date;
                } else {
                    String day = event.date.substring(4, 10).trim() + " " + Calendar.getInstance().get(Calendar.YEAR);
                    event.day = format.parse(day);
                }
                String[] teams = match.select("div.bet-title").first().text().split(" v ");
                event.team1 = Teams.getTeam(teams[0].trim());
                event.team2 = Teams.getTeam(teams[1].trim());
                if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                    logger.warn("CoralParser - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                    continue;
                }
                events.add(event);
                event.link = match.select("div.go-to-bets").first().select("a").first().attr("href");
                event.site = BetSite.CORAL;
                String p1 = match.select("div.home-odds").first().select("span.odds-decimal").text();
                if (p1 != null) {
                    event.bets.put(BetType.P1, p1);
                }
                String pX = match.select("div.draw-odds").first().select("span.odds-decimal").text();
                if (pX != null) {
                    event.bets.put(BetType.X, pX);
                }
                String p2 = match.select("div.away-odds").first().select("span.odds-decimal").text();
                if (p2 != null) {
                    event.bets.put(BetType.P2, p2);
                }
            } catch (Exception e) {
                logger.error("CoralParser error parse match", e);
            }
        }
    }
}
