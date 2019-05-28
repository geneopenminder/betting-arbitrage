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
import org.openqa.selenium.WebDriver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by Evgeniy Pshenitsin on 03.03.2015.
 */
public class OlimpkzParser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    public OlimpkzParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.OLIMPIKZ;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        List<String> links = new ArrayList<String>();
        try {
            Document doc = Jsoup.connect("http://olimpkz.com/betting/soccer")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                    .cookie("curr_lang", "2")
                    .cookie("use_DST", "0")
                    .cookie("curr_tmz", "-120")
                    .timeout(10 * 1000).get();
            Element table = doc.getElementsByClass("smallwnd3").first();
            if (table != null) {
                for (Element tr: table.select("tr")) {
                    Element a = tr.select("a").first();
                    if (a != null) {
                        String leagueLink = tr.select("a").first().attr("href");
                        final String text = tr.select("a").first().text();
                        if (text.contains("забьет") || text.contains("больше")
                                || text.contains("Итоги")) {
                            continue;
                        }
                        links.add(leagueLink);
                    }
                }
            }
            events = links.parallelStream()
                    .map(leagueLink -> { return parseEvent(leagueLink);})
                    .filter(event -> event != null)
                    .filter(event -> !event.bets.isEmpty())
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Olimpikz exception - ", e);
        }
        return events;
    }

    private static BasicEvent parseEvent(String leagueLink) {
        try {
            final String link = "http://olimpkz.com/" + leagueLink;
            Document docLeague = Jsoup.connect(link)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                    .cookie("curr_lang", "2")
                    .cookie("use_DST", "0")
                    .cookie("curr_tmz", "-120")
                    .timeout(10 * 1000).get();
            Element coeffTable = docLeague.getElementsByClass("koeftable2").first();
            if (coeffTable != null) {
                Iterator<Element> trI = coeffTable.select("tr").iterator();
                while(trI.hasNext()) {
                    Element e = trI.next();
                    if (e.hasClass("hi")) {
                        BasicEvent event = new BasicEvent();
                        event.date = e.select("td").first().text().substring(0,10);
                        event.day = DateFormatter.format(event.date, BetSite.OLIMPIKZ);
                        Element txtmed = e.select("td").get(1).getElementsByClass("txtmed").first();
                        if (txtmed != null) {
                            txtmed.text("");
                        }
                        String[] teams = e.select("td").get(1).select("b").text().split(" - ");
                        event.team1 = Teams.getTeam(teams[0].trim());
                        event.team2 = Teams.getTeam(teams[1].trim());
                        if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                            e = trI.next();
                            logger.warn("Olimpikz - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                            continue;
                        }
                        event.link = link;
                        event.site = BetSite.OLIMPIKZ;
                        //bets
                        e = trI.next();
                        Element tab = e.getElementsByClass("tab").first();
                        for (Element mainOdd: tab.select("nobr")) {
                            String oddsContent = mainOdd.text();
                            if (oddsContent != null && !oddsContent.isEmpty()) {
                                String[] typeVal = oddsContent.split("-");
                                if (typeVal.length >= 2) {
                                    final String type = typeVal[0].trim();
                                    String val = typeVal[1].replace("\u00a0", "").trim();
                                    BetType bet = null;
                                    if (type.equalsIgnoreCase("1") && typeVal.length == 2) {
                                        event.bets.put(BetType.P1, val);
                                    } else if (type.equalsIgnoreCase("X") && typeVal.length == 2) {
                                        event.bets.put(BetType.X, val);
                                    } else if (type.equalsIgnoreCase("2") && typeVal.length == 2) {
                                        event.bets.put(BetType.P2, val);
                                    } else if (type.equalsIgnoreCase("1X") && typeVal.length == 2) {
                                        event.bets.put(BetType.P1X, val);
                                    } else if (type.equalsIgnoreCase("12") && typeVal.length == 2) {
                                        event.bets.put(BetType.P12, val);
                                    } else if (type.equalsIgnoreCase("X2") && typeVal.length == 2) {
                                        event.bets.put(BetType.P2X, val);
                                    } else if (type.contains("H1")) {
                                        bet = BetDataMapping.betsTotal.get(getForaOddsType(oddsContent) + "F1");
                                        val = oddsContent.substring(oddsContent.lastIndexOf("-") + 1).replaceAll("\u00a0", "").trim();
                                    } else if (type.contains("H2")) {
                                        bet = BetDataMapping.betsTotal.get(getForaOddsType(oddsContent) + "F2");
                                        val = oddsContent.substring(oddsContent.lastIndexOf("-") + 1).replaceAll("\u00a0", "").trim();
                                    } else if ((type.contains("Tot(") || type.contains("Total "))
                                            && !type.contains("half") && !type.contains("scored") && !type.contains("goals")) {
                                        String totalType = getForaOddsType(oddsContent);
                                        BetType betTL = BetDataMapping.betsTotal.get(totalType + "L");
                                        BetType betTM = BetDataMapping.betsTotal.get(totalType + "M");
                                        if (betTM != null) {
                                            event.bets.put(betTM, typeVal[2].replace("\u00a0", "").trim());
                                        }
                                        if (betTL != null) {
                                            event.bets.put(betTL, getTotalL(typeVal[1]));
                                        }
                                    } else if (!type.contains("under") && !type.contains("over") && !type.contains("score") && !type.contains("half")
                                            && !type.contains("goal") && type.contains("(")) {
                                        if (type.contains(teams[0].trim())) {
                                            String foraType = getForaOddsType(oddsContent);
                                            if (!foraType.equals("0")) {
                                                bet = BetDataMapping.betsTotal.get(foraType + "F1");
                                                val = oddsContent.substring(oddsContent.lastIndexOf("-") + 1).replaceAll("\u00a0", "").trim();
                                            }
                                        } else if (type.contains(teams[1].trim())) {
                                            String foraType = getForaOddsType(oddsContent);
                                            if (!foraType.equals("0")) {
                                                bet = BetDataMapping.betsTotal.get(foraType + "F2");
                                                val = oddsContent.substring(oddsContent.lastIndexOf("-") + 1).replaceAll("\u00a0", "").trim();
                                            }
                                        }
                                    }
                                    if (bet != null) {
                                        event.bets.put(bet, val.replace("\u00a0", "").trim());
                                    }
                                }
                            }
                        }
                        EventSender.sendEvent(event);
                        return event;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Olimpikz exception - ", e);
        }
        return null;
    }

    private static String getForaOddsType(String oddsContent){
        //Ф1(-1.5) -
        int firstPos = oddsContent.indexOf("(");
        if (firstPos != -1) {
            int secondPos = oddsContent.indexOf(")");
            if (secondPos != -1) {
                return oddsContent.substring(firstPos + 1, secondPos);
            }
        }
        return "";
    }

    private static String getTotalL(String totalContent) {
        return totalContent.substring(0, totalContent.trim().indexOf(" ") + 1).replace("\u00a0", "").trim();
    }

}
