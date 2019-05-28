package com.isfootball.parser.source;

import com.isfootball.model.BasicEvent;
import com.isfootball.parser.*;
import com.isfootball.pool.BasePoolInterface;
import com.isfootball.processing.EventSender;
import com.isfootball.utils.HttpClientInitializer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Evgeniy Pshenitsin on 07.04.2015.
 */
public class LigaStavokParser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(50, getThreadFactory(LigaStavokParser.class.getSimpleName()));

    public LigaStavokParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.LIGASTAVOK;
    }

    Map<String, String> monthes = new HashMap<String, String>() {{
        put("января", "01");
        put("февраля", "02");
        put("марта", "03");
        put("апреля", "04");
        put("мая", "05");
        put("июня", "06");
        put("июля", "07");
        put("августа", "08");
        put("сентября", "09");
        put("октября", "10");
        put("ноября", "11");
        put("декабря", "12");
    }};

    private static class LigaStavokDownloader extends BaseDownloader  {

        LigaStavokParser parser;
        String link;

        public LigaStavokDownloader(LigaStavokParser parser, String link) {
            this.parser = parser;
            this.link = link;
        }

        @Override
        public List<BasicEvent> download() {
            return parser.parseEvents(link);
        }
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        List<LigaStavokDownloader> workers = new ArrayList<LigaStavokDownloader>();
        WebDriver driver = null;

        try {
            Document doc = Jsoup.connect("http://www.liga-stavok.com/Topics/Soccer")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                    .get();
            Set<String> links = new HashSet<String>();
            /*Elements hrefs = doc.select("a");
            for (Element href: hrefs) {
                String attr = href.attr("href");
                if (attr != null && attr.startsWith("/Topics/Soccer/")) {
                    links.add(attr);
                }
            }*/
            //logger.info("LigaStavokParser doc  - " + doc);
            Elements leagues = doc.getElementById("TopicsListContents").select("li");
            for (Element league: leagues) {
                final String link = league.select("a").first().attr("href");
                if (link != null) {
                    links.add(link);
                }
            }
            logger.info("LigaStavokParser links size - " + links.size());
            for (String link: links) {
                //parseEvents(link);
                workers.add(new LigaStavokDownloader(this, link));
            }
            List<Future<List<BasicEvent>>> parserStates = new ArrayList<Future<List<BasicEvent>>>();
            for (LigaStavokDownloader tasks: workers) {
                parserStates.add(downloadersTaskPool.submit(tasks));
            }
            for (Future<List<BasicEvent>> f: parserStates) {
                try {
                        List<BasicEvent> eventRes = f.get();
                        if (eventRes != null) {
                            events.addAll(f.get());
                        }
                    f.cancel(true);
                } catch (Exception e) {
                    logger.error("LigaStavokParser processGetBets: future get() exception - ", e);
                    f.cancel(true);
                }
            }
            //downloadersTaskPool.shutdown();
        } catch (Exception e) {
            logger.error("LigaStavokParser error", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    private List<BasicEvent> parseEvents(String link) {
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            driver.manage().window().maximize();
            final String finalLink = "http://www.liga-stavok.com" + link;
            driver.get(finalLink);
            Thread.sleep(5000);
            String htmlSource = driver.getPageSource();
            //logger.info(htmlSource);
            Document league = Jsoup.parse(htmlSource);
            Elements tables = league.select("table.line");
            //logger.info("parseEvents tables size - " + tables.size());
            String dateStr = "";
            Date day = null;
            for (Element dateLine : tables) {
                try {
                    Element date = dateLine.select("th.eventTimeCell.leftAlignment").first();
                    if (date != null) {
                        String[] dateElems = date.text().split(" ");
                        if (dateElems.length > 1) {
                            String dayStr = dateElems[dateElems.length - 2];
                            String month = dateElems[dateElems.length - 1];
                            String monthNum = monthes.get(month.toLowerCase().trim());
                            if (monthNum == null) {
                                logger.error("LigaStavokParser month null - " + month);
                                continue;
                            }
                            dateStr = dayStr + "-" + monthNum + "-" + new DateTime().getYear();
                            day = DateFormatter.format(dateStr, betSite);
                        }
                    }
                    Iterator<Element> matches = dateLine.select("tr").iterator();
                    //logger.info("parseEvents matches - " + date);
                    boolean skipHeader = true;
                    Element match = matches.next();
                    while (matches.hasNext()) {
                        if (skipHeader) {
                            skipHeader = false;
                            continue;
                        }

                        Elements bets = match.select("td");
                        //logger.info("parseEvents matches bet size - " + bets.size() + ";" + finalLink);
                        if (bets.size() == 6) { //13
                            BasicEvent event = new BasicEvent();
                            event.date = dateStr;
                            event.day = day;
                            event.site = BetSite.LIGASTAVOK;
                            event.link = finalLink;
                            String[] teams = match.select("td.sportEventCell").first().text().split(" - ");
                            if (teams.length == 2) {
                                event.team1 = Teams.getTeam(teams[0].trim());
                                event.team2 = Teams.getTeam(teams[1].trim());
                                if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                                    logger.warn("LigaStavokParser - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                                    if (matches.hasNext()) {
                                        match = matches.next();
                                    }
                                    continue;
                                }
                                String p1 = bets.get(3).text().replace(",", ".");
                                if (p1 != null) {
                                    event.bets.put(BetType.P1, p1);
                                }
                                String x = bets.get(4).text().replace(",", ".");
                                if (x != null) {
                                    event.bets.put(BetType.X, x);
                                }
                                String p2 = bets.get(5).text().replace(",", ".");
                                if (p2 != null) {
                                    event.bets.put(BetType.P2, p2);
                                }
                                /*String p1x = bets.get(6).text().replace(",", ".");
                                if (p1x != null) {
                                    event.bets.put(Bet.P1X, p1x);
                                }
                                String p12 = bets.get(7).text().replace(",", ".");
                                if (p12 != null) {
                                    event.bets.put(Bet.P12, p12);
                                }
                                String p2x = bets.get(8).text().replace(",", ".");
                                if (p2x != null) {
                                    event.bets.put(Bet.P2X, p2x);
                                }

                                //fora
                                String fora1Type = bets.get(9).text().trim() + "F1";
                                Bet f1 = BetDataMapping.betsTotal.get(fora1Type);
                                String fora1Val = bets.get(10).text().replace(",", ".");
                                if (fora1Val != null && f1 != null) {
                                    event.bets.put(f1, fora1Val);
                                }
                                String fora2Type = bets.get(11).text().trim() + "F2";
                                Bet f2 = BetDataMapping.betsTotal.get(fora2Type);
                                String fora2Val = bets.get(12).text().replace(",", ".");
                                if (fora2Val != null && f2 != null) {
                                    event.bets.put(f2, fora2Val);
                                }*/
                                if (matches.hasNext()) {
                                    match = matches.next();
                                    Element a = match.select("a.optionalOutcomesControlLink").first();
                                    if (a != null) {
                                        String matchId = a.attr("onclick").split(",")[1].replaceAll("'", "").trim();
                                        if (!matchId.isEmpty()) {
                                            getOdds(event, matchId);
                                        }
                                        if (matches.hasNext()) {
                                            match = matches.next();
                                        }
                                    }
                                }
                                EventSender.sendEvent(event);
                                events.add(event);
                            }
                        } else {
                            if (matches.hasNext()) {
                                match = matches.next();
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("LigaStavokParser error parse table - " + link, e);
                }
            }
        } catch (UnreachableBrowserException e) {
            logger.error("LigaStavokParser worker error - UnreachableBrowserException", e);
            recreateDiedWebDriver(driver);
            driver = null;
        } catch (Exception e) {
            logger.error("LigaStavokParser worker error - " + link, e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return  events;
    }

    private void getOdds(BasicEvent event, String matchId) {
        final String postUrl = "http://www.liga-stavok.com/WebServices/Line.asmx/GetOptionalOutcomes";
        try {
            HttpClient httpClient = new HttpClient(HttpClientInitializer.getConnectionManager());
            PostMethod post = new PostMethod(postUrl);
            StringRequestEntity requestEntity = new StringRequestEntity(
                    "strResultPosition=" + matchId + "&strOutcomesGroupsPerRow=3", "application/x-www-form-urlencoded", "UTF-8");
            post.setRequestEntity(requestEntity);
            int returnCode = httpClient.executeMethod(post);
            if (returnCode == HttpStatus.SC_OK) {
                String ret = IOUtils.toString(new InputStreamReader(post.getResponseBodyAsStream(), "UTF-8"));
                String div = ret.substring(ret.indexOf("div") - 4, ret.lastIndexOf("<"));
                div = StringEscapeUtils.unescapeHtml(div);
                StringBuilder html = new StringBuilder();
                html.append("<!DOCTYPE html><html><body>");
                html.append(div);
                html.append("</body></html>");
                Document doc = Jsoup.parse(html.toString());
                Element fullTime = doc.select("div.outcomesBlock.firstBlock").first();
                if (fullTime != null && fullTime.select("h3").first().text().trim().equalsIgnoreCase("Основное время")) {
                    Elements lis = fullTime.select("li");
                    for (Element li: lis) {
                        String text = li.select("p").first().select("b").first().text().trim();
                        if (text.equalsIgnoreCase("Тотал")) {
                            Element nobr = li.select("nobr").first();
                            if (nobr != null) {
                                parseTotal(event, nobr.text());
                            }
                        } else if (text.equalsIgnoreCase("Фора")) {
                            Elements nobr = li.select("nobr");
                            if (nobr.size() == 2) {
                                parseFora(event, nobr.get(0).text());
                                parseFora(event, nobr.get(1).text());
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            logger.error("LigaStavokParser getOdds error", e);
        }
    }

    private void parseFora(BasicEvent event, String fora) {
        try {
            //Ф1(-1,5) — 10,5 ;
            String[] data = fora.split("—");
            String foraVal = data[1].replace(",", ".").replace(";", "").trim();
            String foraType = data[0].substring(data[0].indexOf("(") + 1, data[0].indexOf(")")).replace(",",".");
            String foraTypeTeam = fora.contains("Ф1") == true ? "F1" : "F2";
            BetType bet = BetDataMapping.betsTotal.get(foraType + foraTypeTeam);
            if (bet != null && !foraVal.isEmpty()) {
                event.bets.put(bet, foraVal);
            }
        } catch (Exception e) {
            logger.error("LigaStavokParser parseFora error", e);
        }
    }

    private void parseTotal(BasicEvent event, String total) {
        try {
            //Ф1(-1,5) — 10,5 ;
            String[] data = total.split(";");

            String totalType = total.substring(total.indexOf("(") + 1, total.indexOf(")")).replace(",",".");
            BetType totalL = BetDataMapping.betsTotal.get(totalType + "L");
            BetType totalM = BetDataMapping.betsTotal.get(totalType + "M");

            String totalLVal = data[0].split("—")[1].trim().replace(",",".");
            String totalMVal = data[1].split("—")[1].trim().replace(",",".");

            if (totalL != null && !totalLVal.isEmpty()) {
                event.bets.put(totalL, totalLVal);
            }
            if (totalM != null && !totalMVal.isEmpty()) {
                event.bets.put(totalM, totalMVal);
            }
        } catch (Exception e) {
            logger.error("LigaStavokParser parseTotal error", e);
        }
    }
}
