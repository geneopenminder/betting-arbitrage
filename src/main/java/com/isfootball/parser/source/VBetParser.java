package com.isfootball.parser.source;

import com.isfootball.model.BasicEvent;
import com.isfootball.parser.BaseParser;
import com.isfootball.parser.BetSite;
import com.isfootball.parser.Teams;
import com.isfootball.pool.BasePool;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Evgeniy Pshenitsin on 25.04.2015.
 */
public class VBetParser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    //03/05
    static DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(10, getThreadFactory(VBetParser.class.getSimpleName()));

    public VBetParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        //this.betSite = BetSite.VBET;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();

        //List<Bet188Downloader> workers = new ArrayList<Bet188Downloader>();
        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            driver.get("http://www.vbet.com/#/sport/?type=0");
            Thread.sleep(5000);
            Document doc = Jsoup.parse(driver.getPageSource());
            Element sportSoccer = doc.getElementById("sport-Soccer");
            if (sportSoccer != null) {
                WebElement soccer = driver.findElement(By.id("sport-Soccer"));
                List<WebElement> allLinks = soccer.findElements(By.cssSelector(".ng-scope,.ng-isolate-scope")); //compitition-b
                //List<WebElement> allLinks = soccer.findElements(By.className("ng-scope")); //compitition-b
                //By.cssSelector(".dataLabel,.dataLabelWide");
                //List<WebElement> allLinks = soccer.findElements(By.xpath("li[@class='ng-scope']"));
                for (WebElement link: allLinks) {
                    link.click();
                    //driver.findElement(By.className("view_selected")).click();
                    Thread.sleep(2000);
                    //scroll-contain prematch
                    //mini-market-b column-3 no-stats

                    List<WebElement> matches = driver.findElements(By.cssSelector(".mini-market-b,.column-3,.no-stats"));
                    if (!matches.isEmpty()) {
                        for (WebElement matchLink: matches) {
                            matchLink.click();
                            Thread.sleep(3000);
                            WebElement matchOdds = driver.findElement(By.cssSelector(".mini-market-left,.ng-scope"));
                            if (matchOdds != null) {
                                Document docMatch = Jsoup.parse(driver.getPageSource());
                                Element match = docMatch.select("div.mini-market-left.ng-scope").first();
                                if (match !=null) {
                                    Elements odds = match.select("div.mini-market-b.ng-scope");
                                    if (!odds.isEmpty()) {
                                        //detect teams
                                        Elements teams = odds.get(0).select("li.ng-scope");
                                        BasicEvent event = new BasicEvent();
                                        if (teams.size() == 3) {
                                            String team1  = teams.get(0).attr("title").trim();
                                            String team2  = teams.get(2).attr("title").trim();
                                            event.team1 = Teams.getTeam(team1);
                                            event.team2 = Teams.getTeam(team2);
                                            if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                                                logger.warn("VBetParser - unknown teams: " + team1 + ":" + event.team1 + ";" + team2 + ":" + event.team2);
                                                continue;
                                            }
                                            events.add(event);
                                            EventSender.sendEvent(event);
                                        }
                                    }
                                }
                            }
                        }

                    }

                    //compitition-b ng-scope ng-isolate-scope
                    /*List<WebElement> leagueLInks = driver.findElements(By.xpath("//li[@class='ng-scope' and @class='active']/div[@class='compitition-b']"));
                    for (WebElement league: leagueLInks) {
                        league.click();
                        Thread.sleep(2000);
                        league.click();
                    }*/
                    link.click();
                }

                //Elements ngScope =  sportSoccer.select("li.ng-scope");


            } else {
                logger.error("VBetParser sport-Soccer is null");
            }
        } catch (Exception e) {
            logger.error("VBetParser exception - ", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

}
