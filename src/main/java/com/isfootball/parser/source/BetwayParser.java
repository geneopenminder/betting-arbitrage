package com.isfootball.parser.source;

import com.isfootball.model.BasicEvent;
import com.isfootball.parser.*;
import com.isfootball.pool.BasePoolInterface;
import com.isfootball.processing.EventSender;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Evgeniy Pshenitsin on 18.04.2015.
 */
public class BetwayParser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    //2015-04-19
    static DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(50, getThreadFactory(BetwayParser.class.getSimpleName()));

    public BetwayParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.BETWAY;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();

        List<BetwayDownloader> workers = new ArrayList<BetwayDownloader>();
        WebDriver driver = null;
        try {
            //driver = getWebDriverFromPool();
            //driver.manage().addCookie(new Cookie("clng", "1"));
            //driver.get("https://www.betsbc.com/bets/bets.php?line_id[]=1");
            Document doc = Jsoup.connect("https://sports.betway.com/")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                    .get();

            Set<String> hrefs = new HashSet<String>();
            Elements a = doc.select("a");
            for (Element href: a) {
                String attr = href.attr("href");
                if (attr != null && attr.startsWith("/soccer/") && StringUtils.countMatches(attr, "/") < 4) {
                    hrefs.add(attr);
                }
            }
            driver = getWebDriverFromPool();
            for (String league: hrefs) {
                final String path = "https://sports.betway.com/#" + league;
                driver.get(path);
                Thread.sleep(300);
                String pageSource = driver.getPageSource();
                Document leagueDoc = Jsoup.parse(pageSource);
                Element availablemarketsorts = leagueDoc.getElementById("availablemarketsorts");
                if (availablemarketsorts == null) {
                    continue;
                }
                Element bettypetable2 = leagueDoc.getElementById("bettypetable2");
                if (bettypetable2 == null) {
                    bettypetable2 = leagueDoc.getElementById("multibettypetable");
                    if (bettypetable2 == null) {
                        continue;
                    }
                }
                Element oddsbody = bettypetable2.select("tbody.oddsbody").first();
                if (oddsbody == null) {
                    continue;
                }
                Elements tr = oddsbody.select("tr");
                String date = "";
                for (Element row: tr) {
                    if (row.hasClass("header")) {
                        continue;
                    } else if (row.hasClass("date")) {
                        date = row.text();
                    } else {
                        try {
                            //check live
                            BasicEvent event = new BasicEvent();
                            event.date = date;
                            event.day = format.parse(date);
                            String[] teams = row.select("td.market_title").first().select("a.event_name").first().text().split(" - ");
                            event.team1 = Teams.getTeam(teams[0].trim());
                            event.team2 = Teams.getTeam(teams[1].trim());
                            if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                                logger.warn("BetwayParser - unknown teams: " + teams[0].trim() + ":" + event.team1 + ";" + teams[1].trim() + ":" + event.team2);
                                continue;
                            }
                            //mid_icon_live_sel
                            if (row.select("td.market_title").first().select("img.market_icon").attr("src").contains("mid_icon_live_sel")) {
                                continue;
                            }
                            String p1 = row.select("td.oddsbox.outcome-td.1").first().text();
                            if (p1 != null) {
                                event.bets.put(BetType.P1, p1);
                            }
                            String px = row.select("td.oddsbox.outcome-td.x").first().text();
                            if (px != null) {
                                event.bets.put(BetType.X, px);
                            }
                            String p2 = row.select("td.oddsbox.outcome-td.2").first().text();
                            if (p2 != null) {
                                event.bets.put(BetType.P2, p2);
                            }
                            event.site = BetSite.BETWAY;
                            //event.link = path;
                            events.add(event);
                            String addLink = "https://sports.betway.com/#" + row.select("a.event_market_count").first().attr("href");
                            event.link = addLink;
                            //parseOdds(event);
                        } catch (Exception e) {
                            //logger.error("BetwayParser parse event error", e);
                        }
                    }
                }
            }
            returnWebDriverToPool(driver);
            driver = null;
            for (BasicEvent event: events) {
                workers.add(new BetwayDownloader(this, event));
            }
            events.clear();
            List<Future<BasicEvent>> parserStates = new ArrayList<Future<BasicEvent>>();
            for (BetwayDownloader tasks: workers) {
                parserStates.add(downloadersTaskPool.submit(tasks));
            }
            for (Future<BasicEvent> f: parserStates) {
                try {
                    BasicEvent event = f.get();
                    if (event != null) {
                        events.add(f.get());
                    }
                    f.cancel(true);
                } catch (Exception e) {
                    logger.error("BetwayParser processGetBets: future get() exception - ", e);
                    f.cancel(true);
                }
            }
            //downloadersTaskPool.shutdown();
        } catch (UnreachableBrowserException e) {
            logger.error("BetwayParser worker error - UnreachableBrowserException", e);
            recreateDiedWebDriver(driver);
            driver = null;
        } catch (Exception e) {
            logger.error("BetwayParser exception - ", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        for (BasicEvent event: events) {
            for (BetType b: event.bets.keySet()) {
                String k = event.bets.get(b);
                if (k.contains("/")) {
                    event.bets.put(b, decimalFromFractional(k));
                }
            }
        }
        return events;
    }

    private String decimalFromFractional(String fractional) {
        if (!fractional.contains("/")) {
            return null;
        } else {
            String[] parts = fractional.split("/");
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
            otherSymbols.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("####.####", otherSymbols);
            return df.format(1.0 + Double.parseDouble(parts[0])/Double.parseDouble(parts[1]));
        }
    }

    private static class BetwayDownloader extends BaseDownloaderSingle {

        BetwayParser parser;
        BasicEvent event;

        public BetwayDownloader(BetwayParser parser, BasicEvent event) {
            this.parser = parser;
            this.event = event;
        }

        @Override
        public BasicEvent download() {
            parser.parseOdds(event);
            return event;
        }
    }

    protected void parseOdds(BasicEvent event) {
        if (event.link != null) {
            WebDriver driver = null;
            try {
                driver = getWebDriverFromPool();
                driver.get(event.link);
                Thread.sleep(1000);
                Document match = Jsoup.parse(driver.getPageSource());
                Element eventtable2 = match.getElementById("eventtable2");
                if (eventtable2 == null) {
                    eventtable2 = match.getElementById("multibettypetable");
                    if (eventtable2 == null) {
                        int i = 0;
                        while(eventtable2 == null && i++ < 20) {
                            Thread.sleep(200);
                            eventtable2 = match.getElementById("eventtable2");
                        }
                        if (eventtable2 == null) {
                            return;
                        }
                    }
                }
                if (eventtable2 != null) {
                    Element tbody = eventtable2.select("tbody.oddsbody").first();
                    Elements trs = tbody.select("tr.market-grp-row.market-group-other");
                    for (int i = 0; i < trs.size(); i++) {
                        Element tr = trs.get(i);
                        Elements th = tr.select("th");
                        if (!th.isEmpty()) {
                            if (th.get(0).text().trim().equalsIgnoreCase("Double Chance")) {
                                i++;
                                tr = trs.get(i);
                                if (tr.select("td.oddsbox.outcome-td.1x") != null) {
                                    String p1x = tr.select("td.oddsbox.outcome-td.1x").first().text();
                                    if (p1x != null) {
                                        event.bets.put(BetType.P1X, p1x);
                                    }
                                }
                                if (tr.select("td.oddsbox.outcome-td.12") != null) {
                                    String p12 = tr.select("td.oddsbox.outcome-td.12").first().text();
                                    if (p12 != null) {
                                        event.bets.put(BetType.P12, p12);
                                    }
                                }
                                if (tr.select("td.oddsbox.outcome-td.2x") != null) {
                                    String p2x = tr.select("td.oddsbox.outcome-td.2x").first().text();
                                    if (p2x != null) {
                                        event.bets.put(BetType.P2X, p2x);
                                    }
                                }
                            } else if (th.get(0).text().trim().startsWith("Handicap")) {

                            } else if (th.get(0).text().trim().startsWith("Total Goals")) {
                                try {
                                    i++;
                                    Element td = trs.get(i).select("td.odds-table-extra").first();
                                    String over = td.select("td.oddsbox.inputbox_odds.outcome-td.over").first().text();
                                    String under = td.select("td.oddsbox.inputbox_odds.outcome-td.under").first().text();
                                    String total = th.get(0).text().trim().substring(12).trim();
                                    BetType totalL = BetDataMapping.betsTotal.get(total + "L");
                                    BetType totalM = BetDataMapping.betsTotal.get(total + "M");
                                    if (totalL != null && !under.isEmpty()) {
                                        event.bets.put(totalL, under);
                                    }
                                    if (totalM != null && !over.isEmpty()) {
                                        event.bets.put(totalM, over);
                                    }
                                } catch (Exception e) {
                                    logger.error("BetwayParser total parse error", e);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                //logger.error("BetwayParser parseOdds error", e);
            } finally {
                if (driver != null) {
                    returnWebDriverToPool(driver);
                }
            }
        }
        EventSender.sendEvent(event);
    }

}
