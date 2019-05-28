package com.isfootball.parser.source;

import com.google.gson.Gson;
import com.isfootball.model.BasicEvent;
import com.isfootball.parser.*;
import com.isfootball.pool.BasePoolInterface;
import com.isfootball.processing.EventSender;
import com.isfootball.utils.HttpClientInitializer;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Evgeniy Pshenitsin on 25.03.2015.
 */
public class BetfairParserEuro extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    static DateFormat format = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
    //Sat, 28 Mar 21:00

    ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(3);

    List<BetfairDownloader> workers = new ArrayList<BetfairDownloader>();

    private static Gson gson = new Gson();

    public BetfairParserEuro(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.BETFAIR;
    }


    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        WebDriver driver = null;
        //List<Bet188Downloader> workers = new ArrayList<Bet188Downloader>();
        try {
            driver = getWebDriverFromPool();
            driver.get("http://soccer.betfair.com");
            Thread.sleep(20000);
            String htmlSource = driver.getPageSource();
            Document doc = Jsoup.parse(htmlSource);
            returnWebDriverToPool(driver);
            driver = null;
            logger.info("BetfairParser main page");
            /*Document doc = Jsoup.connect("http://soccer.betfair.com") // --https://www.betfair.com/sport/football
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                    .cookie("betexPtk", "betexCurrency=GBP~betexTimeZone=Europe/Berlin~betexRegion=GBR~betexLocale=en")
                    .get();*/

            Element allSportsTreeContainer = doc.getElementById("allSportsTreeContainer");
            Elements leagues = allSportsTreeContainer.select("td.MenuEvent.MenuNode_");
            for (Element td: leagues) {
                String id = td.attr("id");
                String text = td.text();
                logger.info(id + text);
            }

            Set<String> links = new HashSet<String>();
            for (Element a: doc.select("a")) {
                String link = a.attr("href");
                if (link != null && link.contains("football/event")) {
                    links.add(link);
                    logger.info("BetfairParser add link" + link);
                    workers.add(new BetfairDownloader(this, link));
                }
            }
            List<Future<BasicEvent>> parserStates = new ArrayList<Future<BasicEvent>>();
            for (BetfairDownloader tasks: workers) {
                parserStates.add(downloadersTaskPool.submit(tasks));
            }
            //Thread.sleep(10 * 60 * 1000);
            for (Future<BasicEvent> f: parserStates) {
                try {
                    BasicEvent event = f.get();
                    if (event != null) {
                        events.add(f.get());
                    }
                    f.cancel(true);
                } catch (Exception e) {
                    logger.error("BetfairParser processGetBets: future get() exception - ", e);
                    f.cancel(true);
                }
            }
            downloadersTaskPool.shutdown();

        } catch (Exception e) {
            logger.error("BetfairParser exception", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    private static class BetfairDownloader implements Callable<BasicEvent> {

        BetfairParserEuro parser;
        String link;

        public BetfairDownloader(BetfairParserEuro parser, String link) {
            this.parser = parser;
            this.link = link;

        }

        @Override
        public BasicEvent call() {
            return parser.parseEvent(link);
        }
    }

    public BasicEvent parseEvent(String link) {
        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            String matchLink = "https://www.betfair.com" + link;
            Document match = Jsoup.connect(matchLink)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                    .cookie("betexPtk", "betexCurrency=GBP~betexTimeZone=Europe/Berlin~betexRegion=GBR~betexLocale=en")
                    .cookie("betexPtkSess", "betexCurrencySessionCookie=GBP~betexRegionSessionCookie=GBR~betexTimeZoneSessionCookie=Europe/Moscow" +
                            "~betexLocaleSessionCookie=en_GB~betexSkin=standard~betexBrand=betfair")
                    .get();
            Element matchDate = match.getElementsByClass("ui-countdown").first();
            if (matchDate == null) {
                return null;
            }
            String date = matchDate.text();
            if (date.contains("Starting")) {
                return null;
            }
            BasicEvent event = new BasicEvent();
            if (match.getElementsByClass("home-runner").first() == null ||
                    match.getElementsByClass("away-runner").first() == null) {
                return null;
            }
            String team1 = match.getElementsByClass("home-runner").first().text().trim();
            String team2 = match.getElementsByClass("away-runner").first().text().trim();
            event.team1 = Teams.getTeam(team1);
            event.team2 = Teams.getTeam(team2);
            if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                logger.warn("Betfair - unknown teams: " + team1 + ":" + event.team1 + ";" + team2 + ":" + event.team2);
                return null;
            }
            event.link = matchLink;
            event.date = date;
            event.site = BetSite.BETFAIR;
            if (date.contains("KO")) {
                event.day = new Date();
            } else {
                event.day = format.parse(date.substring(5, 11) + " " + Calendar.getInstance().get(Calendar.YEAR));
            }

            String matchLInk = "https://www.betfair.com/sport/football/event?eventId=" + matchLink.substring(matchLink.lastIndexOf("=") + 1).trim() +
                    "&action=changeMarketGroup&selectedGroup=all_markets&selectedTabType=market&modules=marketgroups";

            driver.get(matchLInk);
            Thread.sleep(3000);
            String htmlSource = driver.getPageSource();
            int tryNum = 0;
            while (driver.findElement(By.id("select-odds-setting")) == null && tryNum++ < 20) {
                Thread.sleep(200);
            }
            driver.get(matchLInk);
            Document addOdds = Jsoup.parse(htmlSource);
            Elements oddsByType = addOdds.select("div.mod-minimarketview.mod-minimarketview-minimarketview.yui3-minimarketview-content");
            tryNum = 0;
            while (oddsByType.isEmpty() && tryNum++ < 20) {
                Thread.sleep(200);
                htmlSource = driver.getPageSource();
                addOdds = Jsoup.parse(htmlSource);
                oddsByType = addOdds.select(".mod-minimarketview.mod-minimarketview-minimarketview.yui3-minimarketview-content");
            }
            if (oddsByType != null) {
                for (Element odd : oddsByType) {
                    Element title = odd.select("span.title").first();
                    if (title != null) {
                        String oddsTitle = title.text().trim();
                        if (oddsTitle.equalsIgnoreCase("Match Odds")) {
                            Elements a = odd.select("a.com-bet-button");
                            if (a.size() == 3) {
                                String p1 = decimalFromFractional(a.get(0).text().trim());
                                String px = decimalFromFractional(a.get(1).text().trim());
                                String p2 = decimalFromFractional(a.get(2).text().trim());
                                if (p1 != null) {
                                    event.bets.put(BetType.P1, p1);
                                }
                                if (px != null) {
                                    event.bets.put(BetType.X, px);
                                }
                                if (p2 != null) {
                                    event.bets.put(BetType.P2, p2);
                                }
                            }
                        } else if (oddsTitle.equalsIgnoreCase("Match Odds and Both teams to Score")) {
                            Elements a = odd.select("a.com-bet-button");
                            if (a.size() == 3) {
                                String p1X = decimalFromFractional(a.get(0).text().trim());
                                String p12 = decimalFromFractional(a.get(1).text().trim());
                                String p2X = decimalFromFractional(a.get(2).text().trim());
                                if (p1X != null) {
                                    event.bets.put(BetType.P1X, p1X);
                                }
                                if (p12 != null) {
                                    event.bets.put(BetType.P12, p12);
                                }
                                if (p2X != null) {
                                    event.bets.put(BetType.P2X, p2X);
                                }
                            }
                        } else if (oddsTitle.equalsIgnoreCase("Over/Under")) {
                            Elements li = odd.select("li.runner-item");
                            for (Element item : li) {
                                Element span = item.select("span.runner-name").first();
                                if (span != null) {
                                    String totalType = span.text().trim();
                                    BetType total = parseTotal(totalType);
                                    Element val = item.select("span.ui-runner-price").first();
                                    if (val != null && !val.text().replace("\u00a0", "").trim().isEmpty() && total != null) {
                                        event.bets.put(total, decimalFromFractional(val.text().replace("\u00a0", "").trim()));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            EventSender.sendEvent(event);
            return event;
        } catch (Exception e) {
            logger.error("BetfairParser parselink error - " + link, e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return null;
    }

    private String decimalFromFractional(String fractional) {
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

    private BetType parseTotal(String totalStr) {
        BetType total = null;
        String totalTypeVal = totalStr.split(" ")[1];
        if (totalStr.contains("Over")) {
            total = BetDataMapping.betsTotal.get(totalTypeVal + "M");
        } else {
            total = BetDataMapping.betsTotal.get(totalTypeVal + "L");
        }
        return total;
    }

    private void changeOddsFormat(String xsrftoken) {
        HttpClient httpClient = new HttpClient(HttpClientInitializer.getConnectionManager());
        PostMethod post = new PostMethod("https://www.betfair.com/sport/pref/");
        post.addRequestHeader(new Header("Cookie", "xsrftoken=" + xsrftoken));
        try {
            StringRequestEntity requestEntity = new StringRequestEntity(
                    "{\"displayBetsAsDecimal\":true}",
                    "application/json",
                    "UTF-8");
            post.setRequestEntity(requestEntity);
            post.setQueryString(new NameValuePair[]{
                    new NameValuePair("xsrftoken", xsrftoken),
            });
            int returnCode = httpClient.executeMethod(post);
        } catch (Exception e) {
            logger.error("changeOddsFormat error", e);
        }

    }


                    /*try {
                    //https://www.betfair.com/sport/football/event?eventId=27399532&action=changeMarketGroup&selectedGroup=all_markets&selectedTabType=market
                    GetMethod get = new GetMethod("https://www.betfair.com/sport/football/event");
                    HttpClient httpClient = new HttpClient(HttpClientInitializer.getConnectionManager());
                    //get.addRequestHeader(new Header("Cookie", "alt=json"));
                    get.setQueryString(new NameValuePair[]{
                            new NameValuePair("eventId", matchLink.substring(matchLink.lastIndexOf("=") + 1).trim()),
                            new NameValuePair("action", "changeMarketGroup"),
                            new NameValuePair("selectedGroup", "all_markets"),
                            new NameValuePair("selectedTabType", "market"),
                            //new NameValuePair("alt", "json"),
                    });
                    int returnCode = httpClient.executeMethod(get);
                    if (returnCode == HttpStatus.SC_OK) {
                        String ret = IOUtils.toString(new InputStreamReader(get.getResponseBodyAsStream(), "UTF-8"));

                                //mod-minimarketview mod-minimarketview-minimarketview yui3-minimarketview-content
                                //mod-minimarketview mod-minimarketview-minimarketview yui3-minimarketview-content
                        /*
                        String var = ret.substring(ret.lastIndexOf("platformConfig"));
                        var = var.substring(var.indexOf("=") + 1);
                        var = var.substring(0, var.lastIndexOf("script") - 2);
                        var = var.substring(0, var.lastIndexOf(";"));
                        logger.info(var);
                        var = var.replaceAll("arguments\":\\{", "argumentssss\":{");
                        PlatformConfig config = gson.fromJson(var, PlatformConfig.class);
                        Page page = config.page;
                        for (Instruction i: page.config.instructions) {
                            if (i.arguments != null) {
                                for (Argument a: i.arguments) {
                                    if (a.runners != null && a.runners.size() == 3) {
                                        if (a.marketType.equalsIgnoreCase("MATCH_ODDS")) {
                                            Collections.sort(a.runners, new RunnerComparator());

                                            String p1 = a.runners.get(0).prices.back.get(0).price;
                                            String px = a.runners.get(1).prices.back.get(0).price;
                                            String p2 = a.runners.get(2).prices.back.get(0).price;
                                            event.bets.put(Bet.P1, p1);
                                            event.bets.put(Bet.X, px);
                                            event.bets.put(Bet.P2, p2);
                                        }
                                    }
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    logger.error("BetfairParser network exception", e);
                }*/
                /*Select oddsSettings = new Select(driver.findElement(By.id("select-odds-setting")));
                oddsSettings.selectByValue("decimal");
                Thread.sleep(10000);
                htmlSource = driver.getPageSource();
                Document select = Jsoup.parse(htmlSource);
                Element selectE = select.getElementById("select-odds-setting");
                String option = selectE.select("option").get(1).text();
                tryNum = 0;
                while(!option.equals("Decimal") && tryNum++ < 20) {
                    Thread.sleep(200);
                    htmlSource = driver.getPageSource();
                    select = Jsoup.parse(htmlSource);
                    selectE = select.getElementById("select-odds-setting");
                    option = selectE.select("option").get(1).text();
                }*/


    class RunnerComparator implements Comparator<Runner>
    {
        public int compare (Runner r1, Runner r2)
        {
            if (r1.prices.back.get(0).fraction.numerator <
                    r2.prices.back.get(0).fraction.numerator) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    static class PlatformConfig {
        public Page page;
    }

    static class Page {
        public Config config;
    }

    static class Config {
        public List<Instruction> instructions;
    }

    static class Instruction {
        public List<Argument> arguments;
        //public Argument arguments;
    }

    static class Argument {
        public String status;
        public String marketName;
        public String marketType;
        public String eventId;

        public List<Runner> runners;
    }

    static class Runner {
        public String status;
        public Price prices;

    }

    static class Price {
        public List<Back> back;
    }

    static class Back {
        public Fraction fraction;
        public Fraction displayFraction;
        public String price;
    }

    static class Fraction {
        public int numerator;
        public int denominator;
    }


}
