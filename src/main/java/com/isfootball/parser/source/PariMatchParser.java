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
import org.openqa.selenium.WebDriver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Evgeniy Pshenitsin on 04.03.2015.
 */
public class PariMatchParser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    static DateFormat format = new SimpleDateFormat("dd/MMyyyy", Locale.ENGLISH);

    public PariMatchParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.PARIMATCH;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        //List<String> links = new ArrayList<String>();
        List<String> betLinks = new ArrayList<String>();
        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            driver.get("https://www3.parimatch.com");
            Thread.sleep(10000);
            Document doc = Jsoup.parse(driver.getPageSource());
            returnWebDriverToPool(driver);
            driver = null;
            Elements links = doc.select("a");
            Set<String> leagues = new HashSet<String>();
            for (Element link: links) {
                String href = link.attr("href");
                if (href != null && href.startsWith("/sport/futbol") && !href.contains("itogi") && !href.contains("zhenshhiny")
                        && !href.contains("u-19")) {
                    leagues.add(href);
                }
            }

            for (String link: leagues) {
                String leagueLink = "https://www3.parimatch.com" + link;
                try {
                    driver = getWebDriverFromPool();
                    driver.get(leagueLink);
                    Thread.sleep(6000);
                    doc = Jsoup.parse(driver.getPageSource());
                    returnWebDriverToPool(driver);
                    driver = null;
                } catch (Exception e) {
                    logger.error("PariMatchParser timeout exception for link - " + leagueLink);
                    continue;
                }
                if (doc == null) {
                    logger.error("PariMatchParser doc null for link - " + leagueLink);
                    continue;
                }
                Element oddsList = doc.getElementsByClass("wrapper").first();
                if (oddsList == null) {
                    logger.error("PariMatchParser oddsList null for link - " + leagueLink);
                    continue;
                }
                Iterator<Element> tbody = oddsList.select("tbody").iterator();
                if (tbody == null) {
                    logger.error("PariMatchParser tbody null for link - " + leagueLink);
                    continue;
                }

                //tbody.next(); //skip header
                //get header
                Elements header = tbody.next().select("tr").first().select("th");
                int pxOffset = 0;
                int pOffset = 0;
                int tOffset = 0;
                int fOffset = 0;
                //get odds offsets
                for (int i = header.size() - 1; i >= 0; i--) {
                    String title = header.get(i).text().trim();
                    if (title.equalsIgnoreCase("М")) {
                        String mainTitle = header.get(i-2).text().trim();
                        if (mainTitle.equalsIgnoreCase("iТ")) {
                            //неведомая хуета. пропускаем к ебеням
                            i = i - 2;
                        } else if (mainTitle.equalsIgnoreCase("Т")) {
                            tOffset = i - 2;
                            i = i - 2;
                        }
                    } else if (title.equalsIgnoreCase("X2")) {
                        pxOffset = i - 2;
                        i = i - 2;
                    } else if (title.equalsIgnoreCase("П2")) {
                        pOffset = i - 2;
                        i = i - 2;
                    } else if (title.equalsIgnoreCase("КФ")) {
                        if (header.get(i).hasAttr("colspan") &&
                                header.get(i).attr("colspan").equals("2")) {
                            fOffset = i - 1;
                            i = i - 1;
                            if (pOffset > 0) {
                                pOffset++;
                            }
                            if (pxOffset > 0) {
                                pxOffset++;
                            }
                            if (tOffset > 0) {
                                tOffset++;
                            }
                        } else {
                            fOffset = i - 1;
                            i = i - 1;
                        }
                    }
                }

                while (tbody.hasNext()) {
                    Element tableLine = tbody.next();
                    if (tableLine.hasClass("spacer")) {
                        continue;//skip spacer
                    }
                    if (!tableLine.hasClass("props")) {
                        try {
                            //first line main odds
                            Element mainOddsLine = tableLine.select("tr").first();
                            if (mainOddsLine != null) {
                                Elements tds = mainOddsLine.select("td");
                                BasicEvent event = new BasicEvent();
                                event.date = tds.get(1).text().substring(0, 5).trim();
                                logger.info("PariMatch date: " + event.date);
                                event.day = format.parse(event.date + Calendar.getInstance().get(Calendar.YEAR));
                                String[] teamsHtml = tds.get(2).select("a.om").html().split("<br>");
                                if (teamsHtml.length != 2) {
                                    teamsHtml = tds.get(2).html().split("<br>");
                                }
                                if (teamsHtml.length != 2) {
                                    continue;
                                }
                                String team1 = teamsHtml[0].trim();
                                String team2 = teamsHtml[1].trim();
                                /*String team1 = teamsHtml[0].substring(teamsHtml[0].lastIndexOf(">") + 2,
                                                (teamsHtml[0].contains("(") ? teamsHtml[0].lastIndexOf("(") : teamsHtml[0].length())).trim().replace("<small>", "");
                                String team2 = teamsHtml[1].substring(0,
                                        (teamsHtml[1].contains("(") ? teamsHtml[1].lastIndexOf("(") : teamsHtml[1].length())).trim().replace("<small>", "");*/
                                event.team1 = Teams.getTeam(team1);
                                event.team2 = Teams.getTeam(team2);
                                if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                                    logger.warn("PariMatch - unknown teams: " + team1 + ":" + event.team1 + ";" + team2 + ":" + event.team2);
                                    if (tbody.hasNext()) {
                                        Element additionalLine = tbody.next();
                                        while (tbody.hasNext() && !additionalLine.hasClass("spacer")) {
                                            additionalLine = tbody.next();
                                        }
                                    }
                                    continue;
                                }
                                event.site = BetSite.PARIMATCH;
                                event.link = leagueLink;

                                if (fOffset > 0) {
                                    String[] foras = tds.get(fOffset).html().split("</b>");
                                    if (foras.length == 2) {
                                        try {
                                            BetType fora1Type = BetDataMapping.betsTotal.get(foras[0].substring(3).replace("–", "-").replace("+", "") + "F1");
                                            BetType fora2Type = BetDataMapping.betsTotal.get(foras[1].substring(3).replace("–", "-").replace("+", "") + "F2");
                                            String f1Val = tds.get(fOffset + 1).select("a").first().text().trim();
                                            String f2Val = tds.get(fOffset + 1).select("a").get(1).text().trim();
                                            if (fora1Type != null && !f1Val.isEmpty()) {
                                                event.bets.put(fora1Type, f1Val);
                                            }
                                            if (fora2Type != null && !f2Val.isEmpty()) {
                                                event.bets.put(fora2Type, f2Val);
                                            }
                                        } catch (Exception e) {
                                            logger.error("PariMatch parse fora error", e);
                                        }
                                    }
                                }
                                //td 6 total
                                if (tOffset > 0) {
                                    String totalType = tds.get(tOffset).text().trim();
                                    String totalOverVal = tds.get(tOffset + 1).select("u").text().trim();
                                    String totalUnderVal = tds.get(tOffset + 2).select("u").text().trim();
                                    BetType betTotalOver = BetDataMapping.betsTotal.get(totalType + "M");
                                    BetType betTotalUnder = BetDataMapping.betsTotal.get(totalType + "L");
                                    if (betTotalOver != null) {
                                        event.bets.put(betTotalOver, totalOverVal);
                                    }
                                    if (betTotalUnder != null) {
                                        event.bets.put(betTotalUnder, totalUnderVal);
                                    }
                                }
                                //P
                                if (pOffset > 0) {
                                    if (tds.get(pOffset).hasAttr("colspan")) {
                                        //empty
                                    } else {
                                        String P1 = tds.get(pOffset).text().trim();
                                        if (P1 != null && !P1.isEmpty()) {
                                            event.bets.put(BetType.P1, P1);
                                        }
                                        String PX = tds.get(pOffset + 1).text().trim();
                                        if (PX != null && !PX.isEmpty()) {
                                            event.bets.put(BetType.X, PX);
                                        }
                                        String P2 = tds.get(pOffset + 2).text().trim();
                                        if (P2 != null && !P2.isEmpty()) {
                                            event.bets.put(BetType.P2, P2);
                                        }
                                    }
                                }
                                //PX
                                if (pxOffset > 0) {
                                    if (tds.get(pOffset).hasAttr("colspan")) {
                                        //empty
                                    } else {
                                        String P1X = tds.get(pxOffset).text().trim();
                                        if (P1X != null && !P1X.isEmpty()) {
                                            event.bets.put(BetType.P1X, P1X);
                                        }
                                        String P12 = tds.get(pxOffset + 1).text().trim();
                                        if (P12 != null && !P12.isEmpty()) {
                                            event.bets.put(BetType.P12, P12);
                                        }
                                        String P2X = tds.get(pxOffset + 2).text().trim();
                                        if (P2X != null && !P2X.isEmpty()) {
                                            event.bets.put(BetType.P2X, P2X);
                                        }
                                    }
                                }
                                //fill additional bets
                                Element additionalLine = tbody.next();
                                if (additionalLine.hasClass("props")) {
                                    for (Element tr : additionalLine.select("tr")) {
                                        Element ps = tr.getElementsByClass("ps").first();
                                        Element btb = tr.getElementsByClass("btb").first();
                                        if (btb != null && ps != null) {
                                            String additionalOddsCategory = btb.select("th").text().trim();
                                            if (additionalOddsCategory.equalsIgnoreCase("Add. handicaps:") ||
                                                    additionalOddsCategory.equalsIgnoreCase("Дополнительные форы:")) {
                                                Elements psTable = ps.select("tr").get(1).getElementsByClass("ps").select("tr");
                                                if (psTable.size() == 3) {
                                                    try {
                                                        String[] foras1 = psTable.get(1).select("td").get(1).text().split(";");
                                                        for (String fora1 : foras1) {
                                                            String fora1Type = fora1.substring(fora1.indexOf("(") + 1, fora1.indexOf(")")).trim().replace("+", "").replace("–","-");
                                                            BetType fora1Bet = BetDataMapping.betsTotal.get(fora1Type + "F1");
                                                            String fora1K = fora1.substring(fora1.indexOf(")") + 1).trim();
                                                            if (fora1Bet != null && !fora1K.isEmpty()) {
                                                                event.bets.put(fora1Bet, fora1K);
                                                            }
                                                        }
                                                        String[] foras2 = psTable.get(2).select("td").get(1).text().split(";");
                                                        for (String fora2 : foras2) {
                                                            String fora2Type = fora2.substring(fora2.indexOf("(") + 1, fora2.indexOf(")")).trim().replace("+", "").replace("–","-");
                                                            BetType fora2Bet = BetDataMapping.betsTotal.get(fora2Type + "F2");
                                                            String fora2K = fora2.substring(fora2.indexOf(")") + 1).trim();
                                                            if (fora2Bet != null && !fora2K.isEmpty()) {
                                                                event.bets.put(fora2Bet, fora2K);
                                                            }

                                                        }
                                                    } catch (Exception e) {
                                                        logger.error("PariMatch parse fora error", e);
                                                    }
                                                }
                                            } else if (additionalOddsCategory.equalsIgnoreCase("Add. totals:") ||
                                                    additionalOddsCategory.equalsIgnoreCase("Дополнительные тоталы:")) {
                                                Elements psTable = ps.select("tr").get(1).getElementsByClass("ps").select("tr");
                                                if (psTable.size() == 4) {
                                                    String[] totals = psTable.get(1).text().split(";");
                                                    if (totals.length % 2 == 0) {
                                                        for (int i = 0; i < totals.length / 2; i++) {
                                                            String addTotalType = getTotalType(totals[i * 2]);
                                                            if (addTotalType != null) {
                                                                BetType addBetTotalOver = BetDataMapping.betsTotal.get(addTotalType + "M");
                                                                BetType addBetTotalUnder = BetDataMapping.betsTotal.get(addTotalType + "L");
                                                                if (addBetTotalOver != null) {
                                                                    //event.bets.put(addBetTotalOver, totals[i * 2].split("over")[1].trim());
                                                                    event.bets.put(addBetTotalOver, totals[i * 2].split("больше")[1].trim());
                                                                }
                                                                if (addBetTotalUnder != null) {
                                                                    //event.bets.put(addBetTotalUnder, totals[i * 2 + 1].split("under")[1].trim());
                                                                    event.bets.put(addBetTotalUnder, totals[i * 2 + 1].split("меньше")[1].trim());
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                                additionalLine = tbody.next();
                                while (tbody.hasNext() && !additionalLine.hasClass("spacer")) {
                                    additionalLine = tbody.next();
                                }
                                EventSender.sendEvent(event);
                                events.add(event);
                            }
                        } catch (Exception ex) {
                            logger.error("PariMatch parse event - " + leagueLink, ex);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("PariMatch exception - ", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    private String getTotalType(String totalSource) {
        if (totalSource != null && !totalSource.isEmpty()) {
            String[] typeVal = totalSource.split("over");
            if (typeVal.length != 2) {
                typeVal = totalSource.split("больше");
            }
            if (typeVal.length == 2) {
                return typeVal[0].replace("(", "").replace(")", "").trim();
            }
        }

        return null;
    }

}
