package com.isfootball.parser.source;

import com.google.gson.Gson;
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
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Evgeniy Pshenitsin on 27.02.2015.
 */
public class PinParser extends BaseParser {

    private static Gson gson = new Gson();

    private static final Logger logger = LogManager.getLogger("parser");

    //3/10/2015 11:45 AM
    static DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);


    private static class MoneyLine {
        public LineRequest LineRequest;
        public Sport Sport;
    };

    private static class LineRequest {
        public List<Integer> MarketTypes;
        public List<Integer> WagerTypes;
        public List<Integer> PeriodNumbers;
        public Boolean IsParlay;
        public int TimeZoneId;

    };

    private static class Sport {
        public int SportId;
        public String SportName;
        public int SortOrder;
        public List<Market> Markets;

    };

    public static class Market {
        public String MarketName;
        public int Market;
        public Map<String, GamesContainer> GamesContainers;
        public List<String> HeaderLabels;
        public boolean HideMarket;

    };

    private static class GamesContainer {
        public boolean IsVisible;
        public int SportType;
        public int LeagueId;
        public String Name;
        public String DisplayName;
        public List<GameLine> GameLines;

    };

    private static class GameLine {
        public String Rot;
        public String EvId;
        public int Lvl;
        public Team TmH;
        public Team TmA;
        public String Alt;
        public String DispDate;
        public String Date;
        public List<String> WagerMap;

    };

    private static class Team {
        public String Fc;
        public String Txt;
        public String Pd;
        public String Ex;
        public String Sc;
    };

    static int cbt = 400;
    static int ert = 1552;

    public PinParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.PINNACLE;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();

        //WebDriver driver = new ChromeDriver();
        WebDriver driver = null;//new ChromeDriver();

        try {
            driver = getWebDriverFromPool();
            //driver.get("http://sb.188bet.com/en-gb/sports/1/competition/full-time-asian-handicap-and-over-under?competitionids=26726,30631,28809,26326,29593,27325,29398,26470,26244,27487,29490,30754,27668,27317,30756,26334,26140,50991,27068,30757,27942,31006,26826,30711,29402,29366,30657,26723,27540,29485,26472,27202,27967,27314,34222,28188,29061,27166,30758,28301,30665,26228,38741,28586,28649,27250,28641,26396,28366,27164,28136,48444,29106,26380,26793,46705,26213,26067,26526,28487,28732,27691,26619,26840,28864,27076,39439,28777,26218,26986,26993,28138,26124,26862,28524,27582,27609");
            driver.get("https://www.pin1111.com/");
            Thread.sleep(5000);
            driver.findElement(By.id("loginButton")).click();
            Thread.sleep(10000);
            driver.findElement(By.className("customerId")).sendKeys("EP767675");
            driver.findElement(By.className("password")).sendKeys("Moon_Fly5");
            driver.findElement(By.className("loginSubmit")).click();
            Thread.sleep(5000);
            driver.findElement(By.xpath("//a[@class='general_button green loginNext']")).click();
            //
            Thread.sleep(5000);
            String htmlSource = driver.getPageSource();
            Document leagues = Jsoup.parse(htmlSource);
            driver.get("https://members.pin1111.com/Sportsbook/Asia/en-US/Bet/Soccer/Moneyline/Double/null/null/Regular/SportsBookAll/Decimal/7/#tab=Menu&sport=");
            Thread.sleep(3000);
            String lines = driver.getPageSource();
            //Document linesDoc = Jsoup.parse(lines);
            /*driver.manage().window().maximize();
            int i = 0 ;
            while(i++ < 20) {
                new Actions(driver).sendKeys(Keys.PAGE_DOWN).perform();
                Thread.sleep(500);
            }
            i = 0;
            while(i++ < 30) {
                new Actions(driver).sendKeys(Keys.PAGE_UP).perform();
                Thread.sleep(100);
            }*/
            //Element idLInes = linesDoc.getElementById("lines");

            Map<String, Object> json = (Map<String, Object>)((JavascriptExecutor) driver).executeScript("return LINES.state.currentSportJson;");
            List<Map<String, Object>> markets = (List)json.get("Markets");
            Map<String, Object> gamesContainers = (Map<String, Object>)markets.get(1).get("GamesContainers");
            //today events
            fillEvents(gamesContainers, events, driver);
            //early markets
            gamesContainers = (Map<String, Object>)markets.get(2).get("GamesContainers");
            fillEvents(gamesContainers, events, driver);
            /*if (idLInes != null) {
                fillBets(driver, idLInes , "Today_29", events);
                idLInes = linesDoc.getElementById("lines");
                driver.navigate().refresh();
                fillBets(driver, idLInes , "Early_29", events);
            }*/
        } catch (Exception e) {
            logger.error("Pin exception - ", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    private void fillEvents(Map<String, Object> gamesContainers, List<BasicEvent> events, WebDriver driver) throws Exception {
        for (String containerKey: gamesContainers.keySet()) {
            Map<String, List<Map>> container = (Map)gamesContainers.get(containerKey);

            for (Map<String, Object> line : container.get("GameLines")) {
                BasicEvent event = new BasicEvent();
                //event.date
                final String team1 = (String)((Map)line.get("TmH")).get("Txt");
                event.team1 = Teams.getTeam(team1.trim());
                final String team2 = (String)((Map)line.get("TmA")).get("Txt");
                event.team2 = Teams.getTeam(team2.trim());
                if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                    logger.warn("Pin - unknown teams: " + team1 + ":" + event.team1 + ";" + team2 + ":" + event.team2);
                    continue;
                }
                event.site = BetSite.PINNACLE;
                event.link = "pin1111.com";
                event.date = (String)line.get("Date"); //line.Date;
                //String day = ((String) line.get("DispDate")).substring(0, 5) + "/2015";
                event.day = format.parse(event.date.split(" ")[0]);
                event.bets.put(BetType.P1, getOdds((String)((List)line.get("WagerMap")).get(0)));
                event.bets.put(BetType.X, getOdds((String)((List)line.get("WagerMap")).get(1)));
                //event.bets.put(BetType.P2, getOdds((String)((List)line.get("WagerMap")).get(2))); TODO
                //additional
                String additionalOddsLink = "https://members.pin1111.com" + ((String)line.get("Alt")).split("\"")[1];

                Object addBets = (Object)((JavascriptExecutor) driver).executeScript("window.addBets = \"\";");
                Thread.sleep(100);
                ((JavascriptExecutor) driver)
                        .executeScript("return LINES.ajax.post(\"" + additionalOddsLink + "\")." +
                                "done(function(d) {window.addBets = d;}).fail(function(d) {window.addBets = d});");
                Thread.sleep(100);

                String addBetsPage = (String)((JavascriptExecutor) driver).executeScript("return window.addBets;");
                int waitCount = 0;
                while (addBetsPage.length() < 10 && waitCount++ < 30) {
                    Thread.sleep(100);
                    addBetsPage = (String)((JavascriptExecutor) driver).executeScript("return window.addBets;");
                }
                if (addBetsPage != null) {
                    //check for teams
                    if (addBetsPage.contains(team1) || addBetsPage.contains(team2)) {
                        fillAddLines(event, addBetsPage);
                    } else {
                        logger.fatal("Pin incorrect add lines");
                    }
                }
                EventSender.sendEvent(event);
                events.add(event);
                //PostMethod addPost = new PostMethod(additionalOddsLink);
            }
        }
    }

    public void fillAddLines(BasicEvent event, String body) throws Exception {

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><body>");
        html.append(body);
        html.append("</body></html>");


        Document smallDoc = Jsoup.parse(html.toString());
        Element spreadTotal = smallDoc.getElementsByClass("spreadTotal").first();
        if (spreadTotal != null) {
            for (Element overUnderLine: spreadTotal.select("tr")) {
                if (overUnderLine.hasClass("periodLabel") && !overUnderLine.select("th").text().equalsIgnoreCase("Match- Handicap/Over Under")) {
                    break;
                }
                if (!overUnderLine.hasClass("periodLabel") && !overUnderLine.hasClass("columnLabel")) {
                    Elements lineTd = overUnderLine.select("td");
                    if (lineTd != null && lineTd.size() == 7) {
                        //fora
                        String fora1Href = lineTd.get(2).select("a").first().attr("href");
                        if (fora1Href.contains("line=")) {
                            String fora1 = fora1Href.split("line=")[1];
                            BetType betF1 = BetDataMapping.betsTotal.get(fora1 + "F1");
                            String fora1Val = lineTd.get(2).text();
                            if (betF1 != null && fora1Val != null && !fora1Val.isEmpty()) {
                                event.bets.put(betF1, fora1Val);
                            }
                        }
                        String fora2Href = lineTd.get(3).select("a").first().attr("href");
                        if (fora2Href.contains("line=")) {
                            String fora2 = fora2Href.split("line=")[1];
                            BetType betF2 = BetDataMapping.betsTotal.get(fora2 + "F2");
                            String fora2Val = lineTd.get(3).text();
                            if (betF2 != null && fora2Val != null && !fora2Val.isEmpty()) {
                                event.bets.put(betF2, fora2Val);
                            }
                        }
                        //total
                        String totalOverHref = lineTd.get(5).select("a").first().attr("href");
                        if (totalOverHref.contains("line=")) {
                            String totalOver = totalOverHref.split("line=")[1];
                            BetType betOver = BetDataMapping.betsTotal.get(totalOver + "M");
                            String totalOverVal = lineTd.get(5).text();
                            if (betOver != null && totalOverVal != null && !totalOverVal.isEmpty()) {
                                event.bets.put(betOver, totalOverVal);
                            }
                        }
                        String totalUnderHref = lineTd.get(6).select("a").first().attr("href");
                        if (totalUnderHref.contains("line=")) {
                            String totalUnder = totalUnderHref.split("line=")[1];
                            BetType betUnder = BetDataMapping.betsTotal.get(totalUnder + "L");
                            String totalUnderVal = lineTd.get(6).text();
                            if (betUnder != null && totalUnderVal != null && !totalUnderVal.isEmpty()) {
                                event.bets.put(betUnder, totalUnderVal);
                            }
                        }
                    }
                }
            }
        }
    }

    public void fillBets(WebDriver driver, Element idLInes , String lines, List<BasicEvent> events) throws Exception {
        Element today = idLInes.getElementById(lines);
        if (today != null) {
            Element table = today.select("table").first();
            int pdCount = 0;
            boolean multiPD = true;
            Elements evRows = table.select("tr");
            for (Element evRow: table.getElementsByClass("evRow")) {
                Elements tds = evRow.select("td");
                if (tds != null && tds.size() == 9) {
                    String[] teams = tds.get(1).text().split("-vs-");
                    BasicEvent event = new BasicEvent();
                    if (lines.equals("Today_29")) {
                        event.day = new Date();
                        event.date = event.day.toString();
                    } else if (lines.equals("Early_29")) {
                        event.date = tds.get(0).text().trim();
                        event.day = format.parse(event.date.substring(0, 5) + "/" + Calendar.getInstance().get(Calendar.YEAR));
                    }
                    event.team1 = Teams.getTeam(teams[0].trim());
                    event.team2 = Teams.getTeam(teams[1].trim());
                    if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                        //continue;
                    }
                    event.bets.put(BetType.P1, tds.get(2).text());
                    event.bets.put(BetType.X, tds.get(3).text());
                    event.bets.put(BetType.P2, tds.get(4).text());
                    final String href = tds.get(8).select("a").first().attr("href");
                    clickLinkByHref(driver, href, multiPD, multiPD);
                    if (pdCount++ < 4) {
                        multiPD = false;
                    } else {
                        pdCount = 0;
                        multiPD = true;
                    }
                    //driver.findElement(By.cssSelector("a[href*='" + href + "']")).click();
                    Thread.sleep(200);

                    Document smallDoc = Jsoup.parse(driver.getPageSource());
                    Element spreadTotal = smallDoc.getElementsByClass("spreadTotal").first();
                    int count = 0;
                    while (spreadTotal == null && count++ < 20) {
                        Thread.sleep(200);
                        smallDoc = Jsoup.parse(driver.getPageSource());
                        spreadTotal = smallDoc.getElementsByClass("spreadTotal").first();
                    }
                    if (spreadTotal != null) {
                        for (Element overUnderLine: spreadTotal.select("tr")) {
                            if (overUnderLine.hasClass("periodLabel") && !overUnderLine.select("th").text().equalsIgnoreCase("Match- Handicap / Over Under")) {
                                break;
                            }
                            if (!overUnderLine.hasClass("periodLabel") && !overUnderLine.hasClass("columnLabel")) {
                                Elements lineTd = overUnderLine.select("td");
                                if (lineTd != null && lineTd.size() == 7) {
                                    //fora
                                    String fora1Href = lineTd.get(2).select("a").first().attr("href");
                                    if (fora1Href.contains("line=")) {
                                        String fora1 = fora1Href.split("line=")[1];
                                        BetType betF1 = BetDataMapping.betsTotal.get(fora1 + "F1");
                                        String fora1Val = lineTd.get(2).text();
                                        if (betF1 != null && fora1Val != null && !fora1Val.isEmpty()) {
                                            event.bets.put(betF1, fora1Val);
                                        }
                                    }
                                    String fora2Href = lineTd.get(3).select("a").first().attr("href");
                                    if (fora2Href.contains("line=")) {
                                        String fora2 = fora2Href.split("line=")[1];
                                        BetType betF2 = BetDataMapping.betsTotal.get(fora2 + "F2");
                                        String fora2Val = lineTd.get(3).text();
                                        if (betF2 != null && fora2Val != null && !fora2Val.isEmpty()) {
                                            event.bets.put(betF2, fora2Val);
                                        }
                                    }
                                    //total
                                    String totalOverHref = lineTd.get(5).select("a").first().attr("href");
                                    if (totalOverHref.contains("line=")) {
                                        String totalOver = totalOverHref.split("line=")[1];
                                        BetType betOver = BetDataMapping.betsTotal.get(totalOver + "M");
                                        String totalOverVal = lineTd.get(5).text();
                                        if (betOver != null && totalOverVal != null && !totalOverVal.isEmpty()) {
                                            event.bets.put(betOver, totalOverVal);
                                        }
                                    }
                                    String totalUnderHref = lineTd.get(6).select("a").first().attr("href");
                                    if (totalUnderHref.contains("line=")) {
                                        String totalUnder = totalUnderHref.split("line=")[1];
                                        BetType betUnder = BetDataMapping.betsTotal.get(totalUnder + "L");
                                        String totalUnderVal = lineTd.get(6).text();
                                        if (betUnder != null && totalUnderVal != null && !totalUnderVal.isEmpty()) {
                                            event.bets.put(betUnder, totalUnderVal);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ((JavascriptExecutor) driver).executeScript("LINES.helpers.alternateLines.close();");
                    events.add(event);
                }
            }
        }
    }

    private void clickLinkByHref(WebDriver driver, String href, boolean multiPD, boolean isRec) {
        List<WebElement> anchors = driver.findElements(By.tagName("a"));
        Iterator<WebElement> i = anchors.iterator();

        while(i.hasNext()) {
            WebElement anchor = i.next();
            try {
                final String hrefAttr = anchor.getAttribute("href");
                if (hrefAttr != null && hrefAttr.contains(href)) {
                    Actions actions = new Actions(driver);
                    //Actions actionsMove = new Actions(driver);
                    //actionsMove.moveToElement(anchor);
                    //actionsMove.perform();
                    actions.moveToElement(anchor).click().perform();
                    if (multiPD) {
                        new Actions(driver).sendKeys(Keys.DOWN).perform();
                        new Actions(driver).sendKeys(Keys.DOWN).perform();
                        new Actions(driver).sendKeys(Keys.DOWN).perform();
                        new Actions(driver).sendKeys(Keys.DOWN).perform();
                        new Actions(driver).sendKeys(Keys.DOWN).perform();
                        new Actions(driver).sendKeys(Keys.DOWN).perform();
                        new Actions(driver).sendKeys(Keys.DOWN).perform();
                        Thread.sleep(200);
                        if (isRec) {
                            clickLinkByHref(driver, href, false, false);
                        }
                    }
                    break;
                }
            } catch (Exception e) {
                logger.error("Pin webDriver exception - ", e);
            }
        }
    }

        /*
        String sessionToken = "";
        //get token

        HttpClient httpClient = new HttpClient(HttpClientInitializer.getConnectionManager());
        //authorize(httpClient);
        login();
        //HttpPost postHttp = new HttpPost("https://www.matchbook.com/bpapi/rest/security/session/");
        GetMethod getData = new GetMethod("https://members.pin1111.com/Sportsbook/Asia/en-US/GetLines/Soccer/Moneyline/0/null/null/Regular/SportsBookAll/Decimal/7/false/?");
        getData.setQueryString(new NameValuePair[] {
                new NameValuePair("buySellLevels", "{}"),
        });

        getData.addRequestHeader(new Header("Cookie", "PCTR=637195278844525542; UserPrefsCookie=device=d&languageGroup=asian&languageId=1&linesTypeView=d&priceStyle" +
                "=Decimal; LastPageCookie=url=http://www.pin1111.com/default.aspx&time=3/13/2015 12:51:03 PM; UserAccess" +
                "=0; GRP=!qvH72fSqKL/NoB/huYL6pw2e6Lf7lY+a7UAPRkFgPv3G4MUX0mGonA+Od+dt; TS0164fdd7=01ecdb00b7897d2f22" +
                "089c1267a7c1cd5c3e032359e9c36d37d7050e5d2d6210a9f83ceea2bc8c613b7b6bd9fab43db29272a01c22f5105d568467" +
                "2fa2c07f3179a841f945f328d6b3cd5061652c1f0c6024c23c4b7729418099169f0f5567c8849aa0a01d1d857b8ba38259d39f37df205f3aa54010b071bef75f6d1f5c679287f89bc055bcdc8057fb3e5c82bfe6dc2ed742b2b9" +
                "; ADRUM=s=1426272606418&r=https%3A%2F%2Fwww.pin1111.com%2F%3F0; enhanced=pass; bootstraprun=no; Webmetrics-RUMn" +
                "=s=1426272606417&r=https%3A%2F%2Fwww.pin1111.com%2F; ADRUM_BT=R%3a116%7cclientRequestGUID%3a1bcd7fcd-86cc-48e2-8786-c6c68fcdca81" +
                "%7cbtId%3a783%7cbtERT%3a2; custid=id=EP767675&hash=F4D159EF7FFC2D5177C409A7B4FEC56A"));
        try {
            int returnCode = httpClient.executeMethod(getData);
            if (returnCode == HttpStatus.SC_OK) {
                String retLeague = IOUtils.toString(new InputStreamReader(getData.getResponseBodyAsStream(), "UTF-8"));
                logger.info(retLeague);
                MoneyLine moneyLine = gson.fromJson(retLeague, MoneyLine.class);
                logger.info(moneyLine.toString());
                for (String containerKey: moneyLine.Sport.Markets.get(2).GamesContainers.keySet()) {
                    GamesContainer container = moneyLine.Sport.Markets.get(2).GamesContainers.get(containerKey);
                    for (GameLine line: container.GameLines) {
                        BasicEvent event = new BasicEvent();
                        //event.date
                        event.team1 = Teams.getTeam(line.TmH.Txt.trim());
                        event.team2 = Teams.getTeam(line.TmA.Txt.replace("\\\\r\\\\n", "").trim());
                        if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                            continue;
                        }
                        event.site = "pinnacle";
                        event.date = line.Date;
                        String day = line.DispDate.substring(0,5) + "/2015";
                        event.day = format.parse(day);
                        event.bets.put(Bet.P1, getOdds(line.WagerMap.get(0)));
                        event.bets.put(Bet.X, getOdds(line.WagerMap.get(1)));
                        event.bets.put(Bet.P2, getOdds(line.WagerMap.get(2)));
                        //additional
                        String additionalOddsLink = "https://members.pin1111.com" + line.Alt.split("\"")[1];
                        PostMethod addPost = new PostMethod(additionalOddsLink);
                        UUID reqId = UUID.randomUUID();
                        addPost.addRequestHeader(new Header("Cookie", "PCTR=637195278844525542; UserPrefsCookie=device=d&languageGroup=asian&languageId=1&linesTypeView=d&priceStyle" +
                                "=Decimal; LastPageCookie=url=http://www.pin1111.com/default.aspx&time=3/13/2015 12:51:03 PM; UserAccess" +
                                "=0; GRP=!qvH72fSqKL/NoB/huYL6pw2e6Lf7lY+a7UAPRkFgPv3G4MUX0mGonA+Od+dt; TS0164fdd7=01ecdb00b7897d2f22" +
                                "089c1267a7c1cd5c3e032359e9c36d37d7050e5d2d6210a9f83ceea2bc8c613b7b6bd9fab43db29272a01c22f5105d568467" +
                                "2fa2c07f3179a841f945f328d6b3cd5061652c1f0c6024c23c4b7729418099169f0f5567c8849aa0a01d1d857b8ba38259d39f37df205f3aa54010b071bef75f6d1f5c679287f89bc055bcdc8057fb3e5c82bfe6dc2ed742b2b9" +
                                "; ADRUM=s=1426272606418&r=https%3A%2F%2Fwww.pin1111.com%2F%3F0; enhanced=pass; bootstraprun=no; Webmetrics-RUM" +
                                "=s=1426272606417&r=https%3A%2F%2Fwww.pin1111.com%2F; custid=id=EP767675&hash=F4D159EF7FFC2D5177C409A7B4FEC56A"));
                        addPost.addRequestHeader(new Header("Cookie", "R:120|clientRequestGUID:" + reqId + "|btId:601|btERT:" + ++ert));
                        addPost.addRequestHeader(new Header("Accept", "text/plain, ; q=0.01"));
                        addPost.addRequestHeader(new Header("Accept-Encoding", "gzip, deflate"));
                        addPost.addRequestHeader(new Header("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3"));
                        addPost.addRequestHeader(new Header("Cache-Control", "no-cache"));
                        addPost.addRequestHeader(new Header("Connection", "keep-alive"));
                        addPost.addRequestHeader(new Header("Content-Length", "9"));
                        addPost.addRequestHeader(new Header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"));
                        addPost.addRequestHeader(new Header("Host", "members.pin1111.com"));
                        addPost.addRequestHeader(new Header("Pragma", "no-cache"));
                        addPost.addRequestHeader(new Header("Referer", "https://members.pin1111.com/Sportsbook/Asia/en-US/Bet/Soccer/Moneyline/Double/null/null/Regular/SportsBookAll/Decimal/7/"));
                        addPost.addRequestHeader(new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0"));
                        addPost.addRequestHeader(new Header("X-Requested-With", "XMLHttpRequest"));
                        StringRequestEntity requestEntity = new StringRequestEntity("test=test");
                        addPost.setRequestEntity(requestEntity);
                        returnCode = httpClient.executeMethod(addPost);
                        if (returnCode == HttpStatus.SC_OK) {
                            StringBuilder smallHtml = new StringBuilder();
                            smallHtml.append("<!DOCTYPE html><html><body>");
                            GZIPInputStream zippedInputStream =  new GZIPInputStream(addPost.getResponseBodyAsStream());
                            //String authRet = IOUtils.toString(new InputStreamReader(post.getResponseBodyAsStream(), "UTF-8"));
                            String ret = IOUtils.toString(new InputStreamReader(zippedInputStream));
                            smallHtml.append(ret);
                            smallHtml.append("</body></html>");
                            logger.info(ret);
                            Document smallDoc = Jsoup.parse(smallHtml.toString());
                            Element spreadTotal = smallDoc.getElementsByClass("spreadTotal").first();
                            if (spreadTotal != null) {
                                for (Element overUnderLine: spreadTotal.select("tr")) {
                                    if (overUnderLine.hasClass("periodLabel") && !overUnderLine.select("th").text().equalsIgnoreCase("Match- Handicap / Over Under")) {
                                        break;
                                    }
                                    if (!overUnderLine.hasClass("periodLabel") && !overUnderLine.hasClass("columnLabel")) {
                                        Elements lineTd = overUnderLine.select("td");
                                        if (lineTd != null && lineTd.size() == 7) {
                                            //fora
                                            String fora1 = lineTd.get(2).select("a").first().attr("href").split("line=")[1];
                                            String fora2 = lineTd.get(3).select("a").first().attr("href").split("line=")[1];
                                            Bet betF1 = BetDataMapping.betsTotal.get(fora1 + "F1");
                                            Bet betF2 = BetDataMapping.betsTotal.get(fora2 + "F2");
                                            String fora1Val = lineTd.get(2).text();
                                            String fora2Val = lineTd.get(3).text();
                                            if (betF1 != null && fora1Val != null && !fora1Val.isEmpty()) {
                                                event.bets.put(betF1, fora1Val);
                                            }
                                            if (betF2 != null && fora2Val != null && !fora2Val.isEmpty()) {
                                                event.bets.put(betF2, fora2Val);
                                            }
                                            //total
                                            String totalOver = lineTd.get(5).select("a").first().attr("href").split("line=")[1];
                                            Bet betOver = BetDataMapping.betsTotal.get(totalOver + "M");
                                            String totalOverVal = lineTd.get(5).text();
                                            String totalUnder = lineTd.get(6).select("a").first().attr("href").split("line=")[1];
                                            Bet betUnder = BetDataMapping.betsTotal.get(totalUnder + "L");
                                            String totalUnderVal = lineTd.get(6).text();
                                            if (betOver != null && totalOverVal != null && !totalOverVal.isEmpty()) {
                                                event.bets.put(betOver, totalOverVal);
                                            }
                                            if (betUnder != null && totalUnderVal != null && !totalUnderVal.isEmpty()) {
                                                event.bets.put(betUnder, totalUnderVal);
                                            }
                                        }

                                    }
                                }

                            }
                            //Element window = new Element();
                        }
                        events.add(event);
                    }
                }
            }
        } catch (Exception e) {
            logger.info("ex + " + e);
            e.printStackTrace();
        }
        return events;

        /*PostMethod post = new PostMethod("https://www.pin1111.com/login/authenticate/Classic/en-US");
        post.addRequestHeader(new Header("ADRUM", "isAjax:true"));
        post.addRequestHeader(new Header("Accept", ""));
        post.addRequestHeader(new Header("Accept-Encoding", "gzip, deflate"));
        post.addRequestHeader(new Header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"));
        post.addRequestHeader(new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0"));
        post.addRequestHeader(new Header("Host", "www.pin1111.com"));
        post.addRequestHeader(new Header("Referer", "https://www.pin1111.com/"));
        post.addRequestHeader(new Header("X-Requested-With", "XMLHttpRequest"));
        post.addRequestHeader(new Header("Cookie", "Cookie:PCTR=637140822655419184; ASP.NET_SessionId=blz5qja0lucggttqp3coag5m; " +
                "UserPrefsCookie=languageId=1&priceStyle=decimal&linesTypeView=s&device=d&languageGroup=all;"));
        try {
            StringRequestEntity requestEntity = new StringRequestEntity(
                    "ctl00%24LDDL=1&ctl00%24PSDDL=decimal&CustomerId=EP767675&Password=Moon_Fly5&AppId=Classic");
            post.setRequestEntity(requestEntity);
            int returnCode = httpClient.executeMethod(post);
            if (returnCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                String retLeague = IOUtils.toString(new InputStreamReader(post.getResponseBodyAsStream(), "UTF-8"));
                logger.info(retLeague);
                //League leagueObj = gson.fromJson(retLeague, League.class);
                GetMethod getLogin = new GetMethod("https://www.pin1111.com/login/Deposit/Classic/en-US?");
                getLogin.setQueryString(new NameValuePair[] {
                        new NameValuePair("CustomerId", "EP767675"),
                        new NameValuePair("NextSteps", "4"),
                        new NameValuePair("IsAjaxRequest", "True"),
                });
                getLogin.addRequestHeader(new Header("ADRUM", "isAjax:true"));
                getLogin.addRequestHeader(new Header("Accept", ""));
                getLogin.addRequestHeader(new Header("Accept-Encoding", "gzip, deflate"));
                getLogin.addRequestHeader(new Header("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3"));
                getLogin.addRequestHeader(new Header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"));
                getLogin.addRequestHeader(new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0"));
                getLogin.addRequestHeader(new Header("X-Requested-With", "XMLHttpRequest"));
                getLogin.addRequestHeader(new Header("Cookie", post.getResponseHeader("Set-Cookie").getValue()));
                returnCode = httpClient.executeMethod(getLogin);
                //if (returnCode == HttpStatus.SC_OK) {
                    retLeague = IOUtils.toString(new InputStreamReader(getLogin.getResponseBodyAsStream(), "UTF-8"));
                    logger.info(retLeague);
                //}

            }
        } catch (Exception e) {

        }
        return events;
    }

    private void authorize(HttpClient httpClient) {
        GetMethod getMain = new GetMethod("https://www.pin1111.com/");
        getMain.addRequestHeader(new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0"));
        getMain.addRequestHeader(new Header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,q=0.8"));
        getMain.addRequestHeader(new Header("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3"));
        getMain.addRequestHeader(new Header("Accept-Encoding", "gzip, deflate"));
        getMain.addRequestHeader(new Header("Cookie", "Webmetrics-RUM=s=1426096981500&r=https%3A%2F%2Fwww.pin1111.com%2F; ADRUM=s=1426096981502&r=https%3A%2F" +
                "%2Fwww.pin1111.com%2F%3F0"));
        getMain.addRequestHeader(new Header("Host", "www.pin1111.com"));
        getMain.addRequestHeader(new Header("Connection", "keep-alive"));
        try {
            int returnCode = HttpStatus.SC_OK; //httpClient.executeMethod(getMain);
            if (returnCode == HttpStatus.SC_OK) {
                PostMethod post = new PostMethod("https://www.pin1111.com/login/authenticate/Classic/en-GB");
                post.addRequestHeader(new Header("ADRUM", "isAjax:true"));
                post.addRequestHeader(new Header("Accept", ""));
                post.addRequestHeader(new Header("Accept-Encoding", "gzip, deflate"));
                post.addRequestHeader(new Header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"));
                post.addRequestHeader(new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0"));
                post.addRequestHeader(new Header("Host", "www.pin1111.com"));
                post.addRequestHeader(new Header("Referer", "https://www.pin1111.com/"));
                post.addRequestHeader(new Header("X-Requested-With", "XMLHttpRequest"));
                post.addRequestHeader(new Header("Cookie", "TS01b38df3=01ecdb00b79e190bc19222bbd20f419d2999668f57c9f9b3b80bb256b3ae9a8b06dffb8fd1723ff" +
                        "631ac9d2e35a78b2beec79a14d4dfe60a7f7dc627e758e77b2a77b0bc6c; Webmetrics-RUM=s=1426097198483&r=https%3A%2F%2Fwww.pin1111.com%2F;" +
                        " ADRUM=s=1426097198485&r=https%3A%2F%2Fwww.pin1111.com%2F%3F0; ASP.NET_SessionId=vuy4w0ui3ubaw2iyok04tbr1; PCTR=637195252531470018; " +
                        "UserPrefsCookie=languageId=2&priceStyle=decimal&linesTypeView=c&device=d&languageGroup=all; " +
                        "LastPageCookie=url=http://www.pin1111.com/default.aspx&time=11/03/2015 12:07:41; " +
                        "TS0164fdd7=01ecdb00b7ef4c85089eb5629c9472b754bb3b7919c9f9b3b80bb256b3ae9a8b06dffb8fd1773f550b06860ce2e5b55dc5b9d4ac6a0f01cea" +
                        "a1a8c3abc65b2204af35f5abce71adeb4b7d3a7254d69aaa569830ad02ae9380828f562f8fc38ceca1c83691f"));
                StringRequestEntity requestEntity = new StringRequestEntity(
                        "ctl00%24LDDL=1&ctl00%24PSDDL=decimal&CustomerId=EP767675&Password=Moon_Fly5&AppId=Classic");
                post.setRequestEntity(requestEntity);
                returnCode = HttpStatus.SC_MOVED_TEMPORARILY; //httpClient.executeMethod(post);
                if (returnCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                    GetMethod getData = new GetMethod("https://members.pin1111.com/Sportsbook/Asia/en-US/GetLines/Soccer/Moneyline/0/null/null/Regular/SportsBookAll/Decimal/7/false/?");
                    getData.setQueryString(new NameValuePair[] {
                            new NameValuePair("buySellLevels", "{}"),
                    });

                    getData.addRequestHeader(new Header("Cookie", "ADRUM_BT=R%3a116%7cclientRequestGUID%3a13f812a0-cc0b-4b71-a7f3-aaaa54f31b9b%7cbtId%3a397%7cbtERT%3a723" +
                            "; PCTR=637195268650950038; UserPrefsCookie=device=d&languageGroup=asian&languageId=1&linesTypeView=d" +
                            "&priceStyle=Decimal; LastPageCookie=url=http://www.pin1111.com/default.aspx&time=11/03/2015 12:34:25" +
                            "; TS0164fdd4=01eedb00b774eec99ba1ad8bc1620f6d786fc63d8103b4839d5c74bad47274830638759a8297bdb73cb1e57" +
                            "708bf972c4de307ecb521f42567aa2a29b838b842b0db931a53bc1466ed4b8ad2860d9d4d84bc5f45122b7f8fa203c54bb99" +
                            "eddd4c30f42ee95372fcc463391b86914645ac7d4843effc56a7ba50616b037983d4b8b3752c267156a8a9fa95a6ca537b5efe891ff52c9" +
                            "; custid=id=EP767675&hash=CB3B674031EFA310BC150E729EB165F3; UserAccess=0; GRP=!iAwW1b7IGAqnpZctniSipadkm1hHZzZcXBlu5AfZ01DeBMX7blWpmu1f" +
                            "/tcZ; bootstraprun=no; Webmetrics-RUM=s=1426098810293&r=https%3A%2F%2Fwww.pin1111.com%2F; ADRUM=s=1426098810294" +
                            "&r=https%3A%2F%2Fwww.pin1111.com%2F%3F0; enhanced=pass"));

                    returnCode = httpClient.executeMethod(getData);
                        if (returnCode == HttpStatus.SC_OK) {}
                }
            }
        } catch (Exception e) {
            logger.info("ex" + e);
        }

    }*/


    private static String getOdds(String oddsA) {
        int firstPos = oddsA.indexOf(">");
        if (firstPos != -1) {
            int secondPos = oddsA.indexOf("<", firstPos);
            if (secondPos != -1) {
                return oddsA.substring(firstPos + 1, secondPos);
            }
        }
        return "0.0";
    }
}
