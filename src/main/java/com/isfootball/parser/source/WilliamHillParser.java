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
import org.openqa.selenium.WebElement;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by Evgeniy Pshenitsin on 19.09.2015.
 */
public class WilliamHillParser  extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    static DateFormat format = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

    ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(50, getThreadFactory(WilliamHillParser.class.getSimpleName()));

    static Map<String, String> dateMonthes = new HashMap<String, String>() {{
        put("янв","01");
        put("фев","02");
        put("мар","03");
        put("апр","04");
        put("мая","05");
        put("июн","06");
        put("июл","07");
        put("авг","08");
        put("сен","09");
        put("окт","10");
        put("ноя","11");
        put("дек","12");
    }};

    public WilliamHillParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.WILLIAM_HILL;
    }

    private static class WHDownloader extends BaseDownloader {

        WilliamHillParser parser;
        String link;

        public WHDownloader(WilliamHillParser parser, String link) {
            this.parser = parser;
            this.link = link;
        }

        @Override
        public List<BasicEvent> download() {
            return parser.parseLeague(link);
        }
    }

    private List<BasicEvent> parseLeague(String link) {
        WebDriver driver = null;
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        try {
            driver = getWebDriverFromPool();
            driver.get(link);
            /*WebElement popText = driver.findElement(By.id("popText"));
            if (popText != null) {
                Thread.sleep(300);
                ((JavascriptExecutor) driver).executeScript("javascript:hidePopUp(-1);");
                Thread.sleep(500);
            }*/
            Document doc = Jsoup.parse(driver.getPageSource());
            returnWebDriverToPool(driver);
            driver = null;
            Element  tup_type_1_mkt_grps = doc.getElementById("tup_type_1_mkt_grps");
            Element table = tup_type_1_mkt_grps.select("table.tableData").first();

            //WebElement tup_type_1_mkt_grps = driver.findElement(By.id("tup_type_1_mkt_grps"));
            //WebElement table = tup_type_1_mkt_grps.findElement(By.cssSelector("table.tableData"));
            Elements matches = table.select("tr.rowOdd");
            for (Element match: matches) {
                try {
                    BasicEvent event = new BasicEvent();
                    Elements leftPads = match.select("td");
                    if (leftPads.size() == 11) {
                        //check for live today
                        Element pad = leftPads.get(0);
                        if (pad.select("a.liveat").first() != null) {
                            event.day = new Date();
                            event.date = new Date().toString();
                        } else {
                            String dateText = pad.text().trim();
                            if (dateText.equalsIgnoreCase("Today")) {
                                event.day = new Date();
                                event.date = event.day.toString();
                            } else {
                                event.date = dateText + " " + Calendar.getInstance().get(Calendar.YEAR);
                                event.day = format.parse(event.date);
                            }
                        }
                        event.site = BetSite.WILLIAM_HILL;
                        Element teamsPad = leftPads.get(2);
                        event.link = teamsPad.select("a").attr("href");
                        String[] teams = teamsPad.text().split(" v ");
                        if (teams.length == 2) {
                            event.team1 = Teams.getTeam(teams[0].replace("\u00A0","").trim());
                            event.team2 = Teams.getTeam(teams[1].replace("\u00A0", "").trim());
                            if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                                logger.warn("WilliamHillParser - unknown teams: " + teams[0].trim() + ":" + event.team1 + ";" + teams[1].trim() + ":" + event.team2);
                                continue;
                            }
                        } else {
                            continue;
                        }
                        //List<WebElement> bets = match.findElements(By.cssSelector("td.col"));
                        String p1 = leftPads.get(4).text().trim();
                        event.bets.put(BetType.P1, decimalFromFractional(p1));
                        String x = leftPads.get(5).text().trim();
                        event.bets.put(BetType.X, decimalFromFractional(x));
                        String p2 = leftPads.get(6).text().trim();
                        event.bets.put(BetType.P2, decimalFromFractional(p2));
                        events.add(event);
                    }
                } catch (Exception eee) {
                    logger.error("WilliamHillParser", eee);
                }
            }

            events.parallelStream().forEach(e -> fillEvent(e));

        } catch (Exception e) {
            logger.error("WilliamHillParser", e);
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

    private void fillEvent(BasicEvent event) {
        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            driver.get(event.link);
            Document doc = Jsoup.parse(driver.getPageSource());
            returnWebDriverToPool(driver);
            driver = null;

            Elements marketHolderExpanded = doc.getElementById("allMarketsTab").select("div.marketHolderExpanded");

            marketHolderExpanded
                    .stream()
                    .forEach(marketHolder -> {
                        try {
                            final String title = marketHolder.select("th.leftPad.title").select("span").last().text().trim();
                            if (title.equalsIgnoreCase("Double Chance")) {
                                Elements tds = marketHolder.select("tbody").first().select("td");
                                if (tds.size() == 3) {
                                    String p1X = tds.get(0).select("div.eventprice").text().trim();
                                    event.bets.put(BetType.P1X, decimalFromFractional(p1X));
                                    String p2X = tds.get(1).select("div.eventprice").text().trim();
                                    event.bets.put(BetType.P2X, decimalFromFractional(p2X));
                                    String p12 = tds.get(2).select("div.eventprice").text().trim();
                                    event.bets.put(BetType.P12, decimalFromFractional(p12));
                                }
                            } else if (title.equalsIgnoreCase("Match Handicaps")) {
                                Elements trs = marketHolder.select("tbody").first().select("tr");
                                trs.stream().forEach( tr -> {
                                    Elements tds = tr.select("td");
                                    if (tds.size() == 3) {
                                        //g1
                                        final String price1 = tds.get(0).select("div.eventprice").text().trim();
                                        String text1 = tds.get(0).select("div.eventselection").text().trim();
                                        text1 = text1.substring(text1.length() - 8, text1.length() - 5);
                                        BetType g1 = BetDataMapping.gandicaps.get("1H" + text1.trim().replace("+", ""));
                                        if (g1 != null && price1 != null) {
                                            event.bets.put(g1, decimalFromFractional(price1));
                                        }

                                        //gx
                                        final String price3 = tds.get(1).select("div.eventprice").text().trim();
                                        String text3 = tds.get(1).select("div.eventselection").text().trim();
                                        text3 = text3.split("goal")[0];
                                        text3 = text3.substring(text3.indexOf("(") + 1).trim();
                                        BetType gX = BetDataMapping.gandicaps.get("XH" + text3);
                                        if (gX != null && price3 != null) {
                                            event.bets.put(gX, decimalFromFractional(price3));
                                        }

                                        //g2
                                        final String price2 = tds.get(2).select("div.eventprice").text().trim();
                                        String text2 = tds.get(2).select("div.eventselection").text().trim();
                                        text2 = text2.substring(text2.length() - 8, text2.length() - 5).trim();
                                        BetType g2 = BetDataMapping.gandicaps.get("2H" + text2);
                                        if (g2 != null && price2 != null) {
                                            event.bets.put(g2, decimalFromFractional(price2));
                                        }
                                    }
                                });
                            } else if (title.equalsIgnoreCase("Total Match Goals Under/Over")) {
                                Elements tds = marketHolder.select("tbody").first().select("td");
                                tds.stream().forEach(td -> {
                                    final String price = td.select("div.eventprice").text().trim();
                                    final String type = td.select("div.eventselection").text().trim();
                                    BetType bet = null;
                                    if (type.contains("Under")) {
                                        bet = BetDataMapping.betsTotal.get(type.substring(5).trim() + "L");
                                    } else {
                                        bet = BetDataMapping.betsTotal.get(type.substring(4).trim() + "M");
                                    }
                                    if (bet != null && price != null) {
                                        event.bets.put(bet, decimalFromFractional(price));
                                    }
                                });
                            }
                        } catch (Exception e) {
                            logger.error("WilliamHillParser exception - ", e);
                        }
                    });
        } catch (Exception e) {
            logger.error("WilliamHillParser", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        EventSender.sendEvent(event);
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();

        List<WHDownloader> workers = new ArrayList<WHDownloader>();
        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            driver.get("http://sports.williamhill.com/bet/en-gb/betting/y/5/Football.html");
            /*WebElement popText = driver.findElement(By.id("popText"));
            if (popText != null) {
                Thread.sleep(200);
                ((JavascriptExecutor) driver).executeScript("javascript:hidePopUp(-1);");
                Thread.sleep(500);
            }*/
            WebElement listContainer = driver.findElement(By.cssSelector("div.listContainer"));
            List<WebElement> leagues = listContainer.findElements(By.cssSelector("a"));
            List<String> leagueHrefs = new ArrayList<>();
            for (WebElement league: leagues) {
                final String name = league.getText();
                final String link = league.getAttribute("href");
                leagueHrefs.add(link);
            }
            returnWebDriverToPool(driver);
            driver = null;

            ForkJoinPool pool = new ForkJoinPool(8);
            Object result = pool.submit(() ->
                            leagueHrefs.parallelStream()
                        .filter(link -> !link.contains("Women"))
                                    .filter(link -> !link.contains("Junior"))
                            .filter(link -> !link.contains("U19"))
                            .filter(link -> !link.contains("U20"))
                            .filter(link -> !link.contains("U21"))
                        .map(href -> {
                            return parseLeague(href);
                        })
                        .collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll)
                        //.collect(Collectors.toList())
                ).get();
            events = (List)result;

            /*final List<BasicEvent> eventsToProcess = events;

            pool.submit(() -> {
                eventsToProcess
                        .parallelStream()
                        .filter(event -> event.link != null)
                        .map(event -> {
                            fillEvent(event);
                            return event;
                        });
            }).get();*/
        } catch (Exception e) {
            logger.error("WilliamHillParser exception - ", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }


}
