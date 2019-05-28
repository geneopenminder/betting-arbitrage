package com.isfootball.parser.source;

import com.isfootball.model.BasicEvent;
import com.isfootball.parser.*;
import com.isfootball.pool.BasePoolInterface;
import com.isfootball.processing.EventProducer;
import com.isfootball.processing.EventSender;
import com.isfootball.utils.HttpClientInitializer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Evgeniy Pshenitsin on 17.02.2015.
 */
public class MarathonBetParser extends BaseParser {

    //https://www.marathonbet.com/su/popular/Football/

    private static final Logger logger = LogManager.getLogger("parser");

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

    static DateFormat format = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH); //17/02/2015

    public MarathonBetParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.MARATHONBET;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();

        WebDriver driver = null;

        try {
            driver = getWebDriverFromPool();
            driver.get("https://www.marathonbet.com/su/popular/Football/");
            Thread.sleep(7000);
            String htmlSource = driver.getPageSource();
            Document doc = Jsoup.parse(htmlSource);
            Element container =  doc.getElementById("container_EVENTS");

            int count = 0;

            while (container == null && count++ < 30) {
                Thread.sleep(100);
                htmlSource = driver.getPageSource();
                doc = Jsoup.parse(htmlSource);
                container =  doc.getElementById("container_EVENTS");
            }
            returnWebDriverToPool(driver);
            driver = null;
            Elements leagues = container.getElementsByClass("category-container");
            for (Element league: leagues) {
                Element table = league.select("table").first();
                if (table == null) {
                    continue;
                }
                try {
                    Thread.sleep(100);
                    fillLeagueEvents(events, table);
                } catch (Exception e) {
                    logger.error("Marathon error", e);
                }
            }
        } catch (UnreachableBrowserException e) {
            logger.error("Marathon worker error - UnreachableBrowserException", e);
            recreateDiedWebDriver(driver);
            driver = null;
        } catch (Exception e) {
            logger.error("Marathon exception - ", e);
            e.printStackTrace();
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    public void fillLeagueEvents(List<BasicEvent> events, Element table) throws Exception {
        HttpClient httpClient = new HttpClient(HttpClientInitializer.getConnectionManager());
        for(Element match: table.select("tbody")) {
            final String eventId = match.attr("data-event-treeid");
            if (eventId != null && !eventId.isEmpty()) {
                String team1 = null;
                String team2 = null;
                Elements teamElem = match.getElementsByClass("today-member-name");
                if (teamElem == null || teamElem.size() == 0) {
                    team1 = match.getElementsByClass("member-name").get(0).text().trim();
                    team2 = match.getElementsByClass("member-name").get(1).text().trim();
                } else {
                    team1 = match.getElementsByClass("today-member-name").get(0).text().trim();
                    team2 = match.getElementsByClass("today-member-name").get(1).text().trim();
                }
                BasicEvent event = new BasicEvent();
                event.team1 = Teams.getTeam(team1);
                event.team2 = Teams.getTeam(team2);
                if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                    logger.warn("MarathonBet - unknown teams: " + team1 + ":" + event.team1 + ";" + team2 + ":" + event.team2);
                    continue;
                }
                event.site = BetSite.MARATHONBET;
                event.link = "marathonbet.com";
                String date = match.getElementsByClass("date").first().text();
                if (date.length() < 8) {
                    continue;
                }
                date = date.substring(0, 2) + dateMonthes.get(date.substring(3, 6).toLowerCase()) + Calendar.getInstance().get(Calendar.YEAR);
                event.day = format.parse(date);
                Elements mainOdds = match.select("tr").first().select("td");//.getElementsByClass("price");
                for (Element mainOdd: mainOdds) {
                    if (mainOdd.hasClass("price")) {
                        String attr = mainOdd.attr("data-mutable-id");
                        if (attr != null) {
                            if (attr.equals("S1mainRow")) {
                                String p1 = mainOdd.text().trim();
                                event.bets.put(BetType.P1, p1);
                            } else if (attr.equals("S2mainRow")) {
                                String x = mainOdd.text().trim();
                                event.bets.put(BetType.X, x);
                            } else if (attr.equals("S3mainRow")) {
                                String p2 = mainOdd.text().trim();
                                event.bets.put(BetType.P2, p2);
                            } else if (attr.equals("S4mainRow")) {
                                String p1X = mainOdd.text().trim();
                                event.bets.put(BetType.P1X, p1X);
                            } else if (attr.equals("S5mainRow")) {
                                String p12 = mainOdd.text().trim();
                                event.bets.put(BetType.P12, p12);
                            } else if (attr.equals("S6mainRow")) {
                                String p2X = mainOdd.text().trim();
                                event.bets.put(BetType.P2X, p2X);
                            }
                        }
                    }

                }
                /*String onclick = match.getElementsByClass("command").first().attr("onclick");
                if (onclick == null || onclick.isEmpty()) {
                    events.add(event);
                    EventSender.sendEvent(event);
                    continue;
                }*/
                Thread.sleep(200);
                try {
                    //curl -i -H "application/x-www-form-urlencoded" -d "treeId=3908321&columnSize=12" -X POST "https://www.marathonbet.com/su/markets.htm"
                    PostMethod get = new PostMethod("https://www.marathonbet.com/su/markets.htm");
                    get.setRequestBody(new NameValuePair[]{
                            new NameValuePair("treeId", eventId),
                            new NameValuePair("columnSize", "12"),
                    });
                    //get.addParameter("treeId", eventId);
                    //get.addParameter("columnSize", "12");

                    get.setRequestHeader("Accept", "application/json");
                    get.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                    get.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");

                    int returnCode = httpClient.executeMethod(get);
                    if (returnCode == HttpStatus.SC_OK) {
                        String ret = IOUtils.toString(new InputStreamReader(get.getResponseBodyAsStream(), "UTF-8"));
                        ret = ret.substring(ret.indexOf(":") + 2, ret.length() - 2).trim().replaceAll("\\\\", "");
                        Document matchBets = Jsoup.parse(ret);
                        for (Element betCat : matchBets.getElementsByClass("market-inline-block-table-wrapper")) {
                            Element catNameElem = betCat.getElementsByClass("name-field").first();
                            if (catNameElem == null) {
                                continue;
                            }
                            final String catName = catNameElem.text().trim();
                            if (catName.equalsIgnoreCase("Cчет матча")) {
                                        /*for (Element row: betCat.select("table").first().select("tr")) {
                                            Elements td = row.select("th");
                                            if (td.size() > 0) {
                                                //header
                                            } else {
                                                td = row.select("td");
                                                if (td.size() > 1) {
                                                    for (Element galsBet : td) {
                                                        String teamGoals = galsBet.getElementsByClass("coeff-handicap").first().text();
                                                        if (teamGoals.isEmpty()) {
                                                            continue;
                                                        }
                                                        String betVal = galsBet.getElementsByClass("coeff-price").
                                                                first().select("span").first().attr("data-selection-price");
                                                        teamGoals = teamGoals.trim().replace(" - ", ":").trim()
                                                                .replaceAll("\\(", "").replaceAll("\\)", "");
                                                        Bet betGoal = BetDataMapping.totalScore.get(teamGoals);
                                                        if (betGoal != null) {
                                                            event.bets.put(betGoal, betVal);
                                                        }
                                                    }
                                                }
                                            }
                                        }*/
                            } else if (catName.equalsIgnoreCase("Разница в счете")) {

                            } else if (catName.equalsIgnoreCase("Победа с учетом форы")) {
                                for (Element row : betCat.select("table").first().select("tr")) {
                                    Elements td = row.select("th");
                                    if (td.size() > 0) {
                                        //header
                                    } else {
                                        td = row.select("td");
                                        if (td.size() == 2) {
                                            Elements elementsByClass = td.get(0).getElementsByClass("coeff-handicap");
                                            if (elementsByClass != null && !elementsByClass.isEmpty()) {
                                                String F1 = elementsByClass.first().text();
                                                if (F1.isEmpty()) {
                                                    continue;
                                                } else {
                                                    F1 = F1.trim();
                                                }
                                                String F1Val = td.get(0).getElementsByClass("coeff-price").
                                                        first().select("span").first().attr("data-selection-price").trim();
                                                F1 = F1.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\+", "");
                                                BetType F1Bet = BetDataMapping.betsTotal.get(F1 + "F1");
                                                if (F1Bet != null) {
                                                    event.bets.put(F1Bet, F1Val);
                                                }
                                            }
                                            elementsByClass = td.get(1).getElementsByClass("coeff-handicap");
                                            if (elementsByClass != null && !elementsByClass.isEmpty()) {
                                                String F2 = elementsByClass.first().text();
                                                if (F2.isEmpty()) {
                                                    continue;
                                                } else {
                                                    F2 = F2.trim();
                                                }
                                                String F2Val = td.get(1).getElementsByClass("coeff-price").
                                                        first().select("span").first().attr("data-selection-price").trim();
                                                F2 = F2.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\+", "");
                                                BetType F2Bet = BetDataMapping.betsTotal.get(F2 + "F2");
                                                if (F2Bet != null) {
                                                    event.bets.put(F2Bet, F2Val);
                                                }
                                            }
                                        }
                                    }
                                }

                            } else if (catName.equalsIgnoreCase("Победа с учетом азиатской форы")) {
                                for (Element row : betCat.select("table").first().select("tr")) {
                                    Elements td = row.select("th");
                                    if (td.size() > 0) {
                                        //header
                                    } else {
                                        td = row.select("td");
                                            if (td.size() == 2) {
                                                String F1 = td.get(0).getElementsByClass("coeff-handicap").first().text();
                                                if (F1.isEmpty()) {
                                                    continue;
                                                } else {
                                                    F1 = F1.trim();
                                                }
                                                String F1Val = td.get(0).getElementsByClass("coeff-price").
                                                        first().select("span").first().attr("data-selection-price").trim();
                                                F1 = F1.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\+", "");
                                                String[] foraParts = F1.split(",");
                                                F1 = new BigDecimal((Double.parseDouble(foraParts[0]) + Double.parseDouble(foraParts[1])) / 2).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                                                BetType F1Bet = BetDataMapping.betsTotal.get(F1 + "F1");
                                                if (F1Bet != null) {
                                                    event.bets.put(F1Bet, F1Val);
                                                }
                                                String F2 = td.get(1).getElementsByClass("coeff-handicap").first().text();
                                                if (F2.isEmpty()) {
                                                    continue;
                                                } else {
                                                    F2 = F2.trim();
                                                }
                                                String F2Val = td.get(1).getElementsByClass("coeff-price").
                                                        first().select("span").first().attr("data-selection-price").trim();
                                                F2 = F2.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\+", "");
                                                String[] foraParts2 = F2.split(",");
                                                F2 = new BigDecimal((Double.parseDouble(foraParts2[0]) + Double.parseDouble(foraParts2[1])) / 2).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                                                BetType F2Bet = BetDataMapping.betsTotal.get(F2 + "F2");
                                                if (F2Bet != null) {
                                                    event.bets.put(F2Bet, F2Val);
                                                }
                                            }
                                    }
                                }

                            } else if (catName.equalsIgnoreCase("Тотал голов")) {
                                for (Element row : betCat.select("table").first().select("tr")) {
                                    Elements td = row.select("th");
                                    if (td.size() > 0) {
                                        //header
                                    } else {
                                        td = row.select("td");
                                        if (td.size() == 2) {
                                            Elements elementsByClass = td.get(0).getElementsByClass("coeff-handicap");
                                            if (elementsByClass.isEmpty()) {
                                                break;
                                            }
                                            String TL = elementsByClass.first().text();
                                            if (TL.isEmpty()) {
                                                continue;
                                            } else {
                                                TL = TL.trim();
                                            }
                                            String TLVal = td.get(0).getElementsByClass("coeff-price").
                                                    first().select("span").first().attr("data-selection-price").trim();
                                            TL = TL.replaceAll("\\(", "").replaceAll("\\)", "");
                                            BetType F1Bet = BetDataMapping.betsTotal.get(TL + "L");
                                            if (F1Bet != null) {
                                                event.bets.put(F1Bet, TLVal);
                                            }
                                            String TM = td.get(1).getElementsByClass("coeff-handicap").first().text();
                                            if (TM.isEmpty()) {
                                                continue;
                                            } else {
                                                TM = TM.trim();
                                            }
                                            String TMVal = td.get(1).getElementsByClass("coeff-price").
                                                    first().select("span").first().attr("data-selection-price").trim();
                                            TM = TM.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\+", "");
                                            BetType F2Bet = BetDataMapping.betsTotal.get(TM + "M");
                                            if (F2Bet != null) {
                                                event.bets.put(F2Bet, TMVal);
                                            }
                                        }
                                    }
                                }
                            } else if (catName.contains("Тотал голов") && catName.contains(team1)) {
                                //total team1
                                for (Element row : betCat.select("table").first().select("tr")) {
                                    Elements td = row.select("th");
                                    if (td.size() > 0) {
                                        //header
                                    } else {
                                        td = row.select("td");
                                        if (td.size() == 2) {
                                            Elements elementsByClass = td.get(0).getElementsByClass("coeff-handicap");
                                            if (elementsByClass.isEmpty()) {
                                                break;
                                            }
                                            String TL = elementsByClass.first().text();
                                            if (TL.isEmpty()) {
                                                continue;
                                            } else {
                                                TL = TL.trim();
                                            }
                                            String TLVal = td.get(0).getElementsByClass("coeff-price").
                                                    first().select("span").first().attr("data-selection-price").trim();
                                            TL = TL.replaceAll("\\(", "").replaceAll("\\)", "");
                                            BetType F1Bet = BetDataMapping.betsTotal.get("T1_" + TL + "L");
                                            if (F1Bet != null) {
                                                event.bets.put(F1Bet, TLVal);
                                            }
                                            String TM = td.get(1).getElementsByClass("coeff-handicap").first().text();
                                            if (TM.isEmpty()) {
                                                continue;
                                            } else {
                                                TM = TM.trim();
                                            }
                                            String TMVal = td.get(1).getElementsByClass("coeff-price").
                                                    first().select("span").first().attr("data-selection-price").trim();
                                            TM = TM.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\+", "");
                                            BetType F2Bet = BetDataMapping.betsTotal.get("T1_" + TM + "M");
                                            if (F2Bet != null) {
                                                event.bets.put(F2Bet, TMVal);
                                            }
                                        }
                                    }
                                }
                            } else if (catName.contains("Тотал голов") && catName.contains(team2)) {
                                //total team2
                                for (Element row : betCat.select("table").first().select("tr")) {
                                    Elements td = row.select("th");
                                    if (td.size() > 0) {
                                        //header
                                    } else {
                                        td = row.select("td");
                                        if (td.size() == 2) {
                                            Elements elementsByClass = td.get(0).getElementsByClass("coeff-handicap");
                                            if (elementsByClass.isEmpty()) {
                                                break;
                                            }
                                            String TL = elementsByClass.first().text();
                                            if (TL.isEmpty()) {
                                                continue;
                                            } else {
                                                TL = TL.trim();
                                            }
                                            String TLVal = td.get(0).getElementsByClass("coeff-price").
                                                    first().select("span").first().attr("data-selection-price").trim();
                                            TL = TL.replaceAll("\\(", "").replaceAll("\\)", "");
                                            BetType F1Bet = BetDataMapping.betsTotal.get("T2_" + TL + "L");
                                            if (F1Bet != null) {
                                                event.bets.put(F1Bet, TLVal);
                                            }
                                            String TM = td.get(1).getElementsByClass("coeff-handicap").first().text();
                                            if (TM.isEmpty()) {
                                                continue;
                                            } else {
                                                TM = TM.trim();
                                            }
                                            String TMVal = td.get(1).getElementsByClass("coeff-price").
                                                    first().select("span").first().attr("data-selection-price").trim();
                                            TM = TM.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\+", "");
                                            BetType F2Bet = BetDataMapping.betsTotal.get("T2_" + TM + "M");
                                            if (F2Bet != null) {
                                                event.bets.put(F2Bet, TMVal);
                                            }
                                        }
                                    }
                                }
                            } else if (catName.equalsIgnoreCase("Азиатский тотал голов")) {
                                for (Element row : betCat.select("table").first().select("tr")) {
                                    Elements td = row.select("th");
                                    if (td.size() > 0) {
                                        //header
                                    } else {
                                        td = row.select("td");
                                        if (td.size() == 2) {
                                            Elements elementsByClass = td.get(0).getElementsByClass("coeff-handicap");
                                            if (elementsByClass.isEmpty()) {
                                                break;
                                            }
                                            String TL = elementsByClass.first().text();
                                            if (TL.isEmpty()) {
                                                continue;
                                            } else {
                                                TL = TL.trim();
                                            }
                                            String TLVal = td.get(0).getElementsByClass("coeff-price").
                                                    first().select("span").first().attr("data-selection-price").trim();
                                            TL = TL.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\+", "");
                                            String[] totalParts = TL.split(",");
                                            TL = new BigDecimal((Double.parseDouble(totalParts[0]) + Double.parseDouble(totalParts[1])) / 2).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                                            BetType FLBet = BetDataMapping.betsTotal.get(TL + "L");
                                            if (FLBet != null) {
                                                event.bets.put(FLBet, TLVal);
                                            }
                                            String TM = td.get(1).getElementsByClass("coeff-handicap").first().text();
                                            if (TM.isEmpty()) {
                                                continue;
                                            } else {
                                                TM = TM.trim();
                                            }
                                            String TMVal = td.get(1).getElementsByClass("coeff-price").
                                                    first().select("span").first().attr("data-selection-price").trim();
                                            TM = TM.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\+", "");
                                            String[] totalPartsM = TM.split(",");
                                            TM = new BigDecimal((Double.parseDouble(totalPartsM[0]) + Double.parseDouble(totalPartsM[1])) / 2).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                                            BetType FMBet = BetDataMapping.betsTotal.get(TM + "M");
                                            if (FMBet != null) {
                                                event.bets.put(FMBet, TMVal);
                                            }
                                        }
                                    }
                                }
                            }
                    }
                }
            } catch (Exception e) {

            }
            events.add(event);
            EventSender.sendEvent(event);
            }
        }
    }
}
