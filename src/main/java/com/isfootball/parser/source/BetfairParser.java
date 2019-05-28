package com.isfootball.parser.source;

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
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Evgeniy Pshenitsin on 25.03.2015.
 */
public class BetfairParser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(50, getThreadFactory(BetfairParser.class.getSimpleName()));

    List<BetfairMatchDownloader> workers = new ArrayList<BetfairMatchDownloader>();
    List<BetfairLeagueDownloader> workersLeague = new ArrayList<BetfairLeagueDownloader>();

    public BetfairParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.BETFAIR;
    }


    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        WebDriver driver = null;

        //List<Bet188Downloader> workers = new ArrayList<Bet188Downloader>();
        try {
            /*driver = getWebDriverFromPool();
            driver.get("https://www.betfair.com/exchange/football?all=REGION");
            Thread.sleep(5000);
            String htmlSource = driver.getPageSource();
            Document doc = Jsoup.parse(htmlSource);
            returnWebDriverToPool(driver);
            driver = null;
            logger.info("BetfairParser main page");*/
            Document doc = Jsoup.connect("https://www.betfair.com/exchange/football?all=REGION") // --
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                    .cookie("betexPtk", "betexCurrency=GBP~betexTimeZone=Europe/Berlin~betexRegion=GBR~betexLocale=en")
                    .get();

            Set<String> links = new HashSet<String>();
            for (Element a: doc.select("a")) {
                String link = a.attr("href");
                if (link != null && link.startsWith("/exchange/football?competitionRegion")) {
                //if (link != null && link.contains("football/event")) {
                    links.add(link);
                    //logger.info("BetfairParser add link" + link);
                    //workers.add(new BetfairMatchDownloader(this, link));
                }
            }
            Set<String> linksLeagues = new HashSet<String>();
            for (String link: links) {
                doc = Jsoup.connect("https://www.betfair.com" + link) // --
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                        .cookie("betexPtk", "betexCurrency=GBP~betexTimeZone=Europe/Berlin~betexRegion=GBR~betexLocale=en")
                        .get();
                for (Element a: doc.select("a")) {
                    String linkLeague = a.attr("href");
                    if (linkLeague != null && linkLeague.startsWith("/exchange/football/competition")) {
                        //if (link != null && link.contains("football/event")) {
                        linksLeagues.add(linkLeague);
                        //logger.info("BetfairParser league link" + linkLeague);
                        //workers.add(new BetfairMatchDownloader(this, link));
                    }
                }
            }
            logger.info("linksLeagues size - " + linksLeagues.size());

            //driver = getWebDriverFromPool();
            //driver.manage().addCookie(new Cookie("betexPtk", "betexCurrency=GBP~betexTimeZone=Europe/Berlin~betexRegion=GBR~betexLocale=en"));
            for (String league: linksLeagues) {
                workersLeague.add(new BetfairLeagueDownloader(this, league));
            }
            List<Future<List<BasicEvent>>> parserStatesLeague = new ArrayList<Future<List<BasicEvent>>>();
            for (BetfairLeagueDownloader tasks: workersLeague) {
                parserStatesLeague.add(downloadersTaskPool.submit(tasks));
            }
            logger.info("leagues wait");
            for (Future<List<BasicEvent>> f: parserStatesLeague) {
                try {
                    events.addAll(f.get());
                    f.cancel(true);
                } catch (Exception e) {
                    logger.error("BetfairParser parseLeagues: future get() exception - ", e);
                    f.cancel(true);
                }
            }
            logger.info("start workers");
            Set<String> linksEvents = new HashSet<String>();
            for (BasicEvent event: events) {
                if (event.link.startsWith("/exchange/plus/#/football") && !linksEvents.contains(event.link)) {
                    linksEvents.add(event.link);
                    logger.info("submit worker");
                    workers.add(new BetfairMatchDownloader(this, event));
                    //parseEvent(event);
                }
            }
            logger.info("workers size - " + workers.size());
            List<Future<BasicEvent>> parserStates = new ArrayList<Future<BasicEvent>>();
            for (BetfairMatchDownloader tasks: workers) {
                parserStates.add(downloadersTaskPool.submit(tasks));
            }
            logger.info("workers wait");
            //Thread.sleep(30000 * 30);
            for (Future<BasicEvent> f: parserStates) {
                try {
                    f.get();
                    f.cancel(true);
                } catch (Exception e) {
                    logger.error("BetfairParser processGetBets: future get() exception - ", e);
                    f.cancel(true);
                }
            }
            //downloadersTaskPool.shutdown();
        } catch (NoSuchWindowException e) {
            logger.error("BetfairParser worker error - UnreachableBrowserException", e);
            recreateDiedWebDriver(driver);
            driver = null;
        } catch (UnreachableBrowserException e) {
            logger.error("BetfairParser worker error - UnreachableBrowserException", e);
            recreateDiedWebDriver(driver);
            driver = null;
        } catch (Exception e) {
            logger.error("BetfairParser exception", e);
        } finally {
            workersLeague.clear();
            workers.clear();
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    private static class BetfairLeagueDownloader extends BaseDownloader {

        BetfairParser parser;
        String league;

        public BetfairLeagueDownloader(BetfairParser parser, String league) {
            this.parser = parser;
            this.league = league;
        }

        @Override
        public List<BasicEvent> download() {
            List<BasicEvent> ret = parser.parseLeague(league);
            return ret;
        }
    }

    public List<BasicEvent> parseLeague(String league) {
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            driver.get("https://www.betfair.com" + league);
            Document doc = Jsoup.parse(driver.getPageSource());
            Elements groups = doc.select("div.mod-new-coupon.mod-new-coupon-new-coupon");
            //
            for (Element group: groups) {
                Element countainer = group.select("div.coupon-container").first();
                if (countainer !=null) {
                    String date = group.select("h2.coupon-name-header").text().trim();
                    Date day = null;
                    if (date.equalsIgnoreCase("Tomorrow") || date.equalsIgnoreCase("Завтра")) {
                        DateTime dateTime = new DateTime(new Date());
                        dateTime.plusDays(1);
                        day = dateTime.toDate();
                    } else if (date.equalsIgnoreCase("Today") || date.equalsIgnoreCase("Сегодня")) {
                        day = new Date();
                        continue; //TODO
                    } else {
                        date = date.split(",")[1].trim().replace(".", "");
                        if (date.indexOf(" ") == 1) {
                            date = "0" + date;
                        }
                        day = DateFormatter.format(date, betSite);
                    }
                    Elements listCoupons = countainer.select("ul.list-coupons");
                    for (Element coupon: listCoupons) {
                        if (coupon.text().contains("Suspended")) {
                            continue;
                        }
                        BasicEvent event = new BasicEvent();
                        event.date = date;
                        event.day = day;
                        String team1 = coupon.select("span.home-team").text().trim();
                        String team2 = coupon.select("span.away-team").text().trim();
                        event.team1 = Teams.getTeam(team1.trim());
                        event.team2 = Teams.getTeam(team2.trim());
                        if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                            logger.warn("BetfairParser - unknown teams: " + team1 + ":" + event.team1 + ";" + team2 + ":" + event.team2);
                            continue;
                        }
                        Elements odds = coupon.select("li.odds.back");
                        if (odds.size() == 3) {
                            String p1 = odds.get(0).text().trim();
                            if (!p1.isEmpty()) {
                                event.bets.put(BetType.P1, p1);
                            }
                            String pX = odds.get(1).text().trim();
                            if (!pX.isEmpty()) {
                                event.bets.put(BetType.X, pX);
                            }
                            String p2 = odds.get(2).text().trim();
                            if (!p2.isEmpty()) {
                                event.bets.put(BetType.P2, p2);
                            }
                        }
                        Element otherMarkets = coupon.select("a.other-markets").first();
                        if (otherMarkets != null) {
                            event.link = "https://www.betfair.com" + otherMarkets.attr("href");
                            //parseEvent(event);
                        } else {
                            event.link = "https://www.betfair.com" + league;
                        }
                        event.site = BetSite.BETFAIR;
                        events.add(event);
                    }
                }

            }
        } catch (NoSuchWindowException e) {
            logger.error("BetfairParser worker error - UnreachableBrowserException", e);
            recreateDiedWebDriver(driver);
            driver = null;
        } catch (UnreachableBrowserException e) {
            logger.error("BetfairParser worker error - UnreachableBrowserException", e);
            recreateDiedWebDriver(driver);
            driver = null;
        } catch (Exception e) {
            logger.error("BetfairParser parseleague error - " + league, e);
        } finally {
            logger.info("parseEvent end");
            if (driver != null) {
                driver.get("about:blank");
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    private static class BetfairMatchDownloader extends BaseDownloaderSingle  {

        BetfairParser parser;
        BasicEvent event;

        public BetfairMatchDownloader(BetfairParser parser, BasicEvent event) {
            this.parser = parser;
            this.event = event;
        }

        @Override
        public BasicEvent download() {
            return parser.parseEvent(event);
        }
    }

    public BasicEvent parseEvent(BasicEvent event) {
        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            String matchLink = "https://www.betfair.com" + event.link;
            driver.get(matchLink);
            Thread.sleep(500);
            Document match = Jsoup.parse(driver.getPageSource());
            Element active = match.select("ul.section.active-section.section-separator").first();
            if (active == null) {
                return null;
            }
            Elements nodes = active.select("li.node");
            for (Element node : nodes) {
                Element a = node.select("a").first();
                if (a != null) {
                    String text = a.text();
                    String link = a.attr("href").trim();
                    if (!link.isEmpty()) {
                        try {
                            if (text.contains("Over/Under")) {
                                driver.get("https://www.betfair.com/exchange/plus/" + link);
                                Thread.sleep(500);
                                Document odds = Jsoup.parse(driver.getPageSource());
                                Elements totals = odds.select("li.bf-row.runner-item.middle.active-runner");
                                String over = totals.get(1).select("button.back.depth-back-2").first().select("span.price").text().trim();
                                String under = totals.get(0).select("button.back.depth-back-2").first().select("span.price").text().trim();
                                String type = text.substring(10, 14).trim();
                                BetType overTotal = BetDataMapping.betsTotal.get(type + "M");
                                BetType underTotal = BetDataMapping.betsTotal.get(type + "L");
                                if (overTotal != null && !over.isEmpty() && !over.equalsIgnoreCase("0")) {
                                    event.bets.put(overTotal, over);
                                }
                                if (underTotal != null && !under.isEmpty() && under.equalsIgnoreCase("0")) {
                                    event.bets.put(underTotal, under);
                                }
                            } else if (text.contains("Double Chance")) {
                                driver.get("https://www.betfair.com/exchange/plus/" + link);
                                Thread.sleep(500);
                                Document odds = Jsoup.parse(driver.getPageSource());
                                Elements x = odds.select("li.bf-row.runner-item.middle.active-runner");
                                if (x.size() == 3) {
                                    String p1x = x.get(0).select("button.back.depth-back-2").first().select("span.price").text().trim();
                                    if (!p1x.isEmpty()) {
                                        event.bets.put(BetType.P1X, p1x);
                                    }
                                    String p12 = x.get(2).select("button.back.depth-back-2").first().select("span.price").text().trim();
                                    if (!p12.isEmpty()) {
                                        event.bets.put(BetType.P12, p12);
                                    }
                                    String p2x = x.get(1).select("button.back.depth-back-2").first().select("span.price").text().trim();
                                    if (!p2x.isEmpty()) {
                                        event.bets.put(BetType.P2X, p2x);
                                    }
                                }
                            } else if (text.contains("Asian Handicap")) {
                                driver.get("https://www.betfair.com/exchange/plus/" + link);
                                Thread.sleep(500);
                                Document odds = Jsoup.parse(driver.getPageSource());
                                String[] teams = odds.select("div.title.default.bf-col-19-24").text().split(" v ");
                                if (teams.length != 2) {
                                    logger.error("BetfairParser parseEvent empty teams");
                                    continue;
                                }
                                Element runner = odds.select("ul.runner-list").first();
                                if (runner != null) {
                                    Elements foras = runner.select("li.bf-row.runner-item.middle.active-runner");
                                    for (Element fora : foras) {
                                        if (fora.select("button.back.depth-back-2").first().select("span.price").first().hasClass("ng-hide")) {
                                            continue;
                                        }
                                        Element foraType = fora.select("div.runner-data-container.runner-info-elem").first();
                                        if (foraType != null) {
                                            String type = foraType.text().trim();
                                            if (type.contains(teams[0])) {
                                                type = type.substring(teams[0].length()).trim();
                                                type = parseFora(type) + "F1";
                                            } else if (type.contains(teams[1])) {
                                                type = type.substring(teams[1].length()).trim();
                                                type = parseFora(type) + "F2";
                                            } else {
                                                continue;
                                            }
                                            BetType bet = BetDataMapping.betsTotal.get(type);
                                            String value = fora.select("button.back.depth-back-2").first().select("span.price").text().trim();
                                            if (bet != null && !value.isEmpty()) {
                                                event.bets.put(bet, value);
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            logger.error("BetfairParser parseEvent error", e);
                        }
                    }
                }
            }
            EventSender.sendEvent(event);
            return event;
        } catch (NoSuchWindowException e) {
            logger.error("BetfairParser worker error - UnreachableBrowserException", e);
            recreateDiedWebDriver(driver);
            driver = null;
        } catch (UnreachableBrowserException e) {
            logger.error("BetfairParser worker error - UnreachableBrowserException", e);
            recreateDiedWebDriver(driver);
            driver = null;
        } catch (Exception e) {
            logger.error("BetfairParser parselink error - " + event.link, e);
        } finally {
            logger.info("parseEvent end");
            if (driver != null) {
                driver.get("about:blank");
                returnWebDriverToPool(driver);
            }
        }
        return null;
    }

    private String parseFora(String fora) {
        //a -3.5 & -4.0F1
        if (!fora.contains("&")) {
            return fora;
        }
        String[] vals = fora.split("&");
        Double res = (Double.parseDouble(vals[0]) + Double.parseDouble(vals[1])) / 2.0;
        return Double.toString(res);
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
