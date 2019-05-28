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
 * Created by Evgeniy Pshenitsin on 13.02.2015.
 */
public class BetInParser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    public static Map<String, BetType> foraMap = new HashMap<String, BetType>() {{
        put("1(-4)", BetType.F1minus4);
        put("1(-3.5/4)", BetType.F1minus375);
        put("1(-3.5)", BetType.F1minus35);
        put("1(-3/3.5)", BetType.F1minus325);
        put("1(-3)", BetType.F1minus3);
        put("1(-2.5/3)", BetType.F1minus275);
        put("1(-2.5)", BetType.F1minus25);
        put("1(-2/2.5)", BetType.F1minus225);
        put("1(-2)", BetType.F1minus2);
        put("1(-1.5/2)", BetType.F1minus175);
        put("1(-1.5)", BetType.F1minus15);
        put("1(-1/1.5)", BetType.F1minus125);
        put("1(-1)", BetType.F1minus1);
        put("1(-0.5/1)", BetType.F1minus075);
        put("1(-0.5)", BetType.F1minus05);
        put("1(-0/0.5)", BetType.F1minus025);
        put("1(0)", BetType.F10);
        put("1(+0/0.5)", BetType.F1plus025);
        put("1(+0.5)", BetType.F1plus05);
        put("1(-0.5)", BetType.F1minus05);
        put("1(+0.5/1)", BetType.F1plus075);
        put("1(-0.5/1)", BetType.F1minus075);
        put("1(+1)", BetType.F1plus1);
        put("1(+1/1.5)", BetType.F1plus125);
        put("1(+1.5)", BetType.F1plus15);
        put("1(+1.5/2)", BetType.F1plus175);
        put("1(+2)", BetType.F1plus2);
        put("1(+2/2.5)", BetType.F1plus225);
        put("1(+2.5)", BetType.F1plus25);
        put("1(+2.5/3)", BetType.F1plus275);
        put("1(+3)", BetType.F1plus3);
        put("1(+3/3.5)", BetType.F1plus325);
        put("1(+3.5)", BetType.F1plus35);
        put("1(+3.5/4)", BetType.F1plus375);
        put("1(+4)", BetType.F1plus4);

        put("2(-4)", BetType.F2minus4);
        put("2(-3.5/4)", BetType.F2minus375);
        put("2(-3.5)", BetType.F2minus35);
        put("2(-3/3.5)", BetType.F2minus325);
        put("2(-3)", BetType.F2minus3);
        put("2(-2.5/3)", BetType.F2minus275);
        put("2(-2.5)", BetType.F2minus25);
        put("2(-2/2.5)", BetType.F2minus225);
        put("2(-2)", BetType.F2minus2);
        put("2(-1.5/2)", BetType.F2minus175);
        put("2(-1.5)", BetType.F2minus15);
        put("2(-1/1.5)", BetType.F2minus125);
        put("2(-1)", BetType.F2minus1);
        put("2(-0.5/1)", BetType.F2minus075);
        put("2(-0.5)", BetType.F2minus05);
        put("2(-0/0.5)", BetType.F2minus025);
        put("2(0)", BetType.F20);
        put("2(+0/0.5)", BetType.F2plus025);
        put("2(+0.5)", BetType.F2plus05);
        put("2(-0.5)", BetType.F2minus05);
        put("2(+0.5/1)", BetType.F2plus075);
        put("2(-0.5/1)", BetType.F2minus075);
        put("2(+1)", BetType.F2plus1);
        put("2(+1/1.5)", BetType.F2plus125);
        put("2(+1.5)", BetType.F2plus15);
        put("2(+1.5/2)", BetType.F2plus175);
        put("2(+2)", BetType.F2plus2);
        put("2(+2/2.5)", BetType.F2plus225);
        put("2(+2.5)", BetType.F2plus25);
        put("2(+2.5/3)", BetType.F2plus275);
        put("2(+3)", BetType.F2plus3);
        put("2(+3/3.5)", BetType.F2plus325);
        put("2(+3.5)", BetType.F2plus35);
        put("2(+3.5/4)", BetType.F2plus375);
        put("2(+4)", BetType.F2plus4);

    }};

    static DateFormat format = new SimpleDateFormat("dd'/'MM'/'yyyy", Locale.ENGLISH); //17/02/2015

    public BetInParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.BETIN;
    }

    @Override
    public List<BasicEvent> getEvents() {

        List<BasicEvent> events = new ArrayList<BasicEvent>();
        try {
            Document doc = Jsoup.connect("http://sports18.betin.com/en/football")
                    .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:45.0) Gecko/20100101 Firefox/44.0")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .timeout(20 * 1000).get();
            Element list = doc.getElementsByClass("coupon").first();
            for (Element l: list.getElementsByClass("section")) {
                for (Element row: l.select("table").select("td")) {
                    String league = row.getElementsByClass("eventCoupon").first().select("a").attr("href");
                    if (league == null || league.isEmpty()) {
                        continue;
                    }
                    Document docLeague = Jsoup.connect("http://sports18.betin.com/" + league)
                            .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:45.0) Gecko/20100101 Firefox/44.0")
                            .header("Accept-Encoding", "gzip, deflate, br")
                            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                            .timeout(10 * 1000).get();
                    Element container = docLeague.getElementsByClass("blindContainer").first();
                    for (Element rowMatch: container.select("table").first().select("tr")) {
                        final String linkMatch = rowMatch.attr("evt");
                        if (linkMatch != null && !linkMatch.isEmpty()) {
                            final String date = rowMatch.attr("dt");
                            Elements a = rowMatch.getElementsByClass("evtnm").first().select("a");
                            if (a == null || a.size() == 0) {
                                continue;
                            }
                            try {
                                final String linkToMatch = "http://sports18.betin.com/" + a.attr("href");
                                Document match = Jsoup.connect(linkToMatch)
                                        .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:45.0) Gecko/20100101 Firefox/44.0")
                                        .header("Accept-Encoding", "gzip, deflate, br")
                                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                                        .timeout(10 * 1000).get();
                                BasicEvent event = new BasicEvent();
                                event.site = BetSite.BETIN;
                                event.link = linkToMatch;
                                Element betsMap = match.getElementsByClass("coupon").first();
                                String teamsStr = betsMap.getElementsByClass("current").first().text();
                                String[] teams = teamsStr.split(" v ");
                                event.team1 = Teams.getTeam(teams[0].trim());
                                event.team2 = Teams.getTeam(teams[1].trim());
                                if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                                    logger.warn("BetInParser - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                                    continue;
                                }
                                event.day = format.parse(date);
                                Element blindContainer = betsMap.getElementsByClass("blindContainer").first();
                                for (Element rowB : blindContainer.getElementsByClass("section")) {
                                    final String title = rowB.getElementsByClass("sectionHeader").first().select("h3").first().text().trim();
                                    Element betsTable = rowB.select("table").first();
                                    if (title.equals("1 X 2")) {
                                        Elements bets = betsTable.getElementsByClass("odds");
                                        String bet1 = bets.select("a").get(0).attr("val");
                                        String bet2 = bets.select("a").get(1).attr("val");
                                        String bet3 = bets.select("a").get(2).attr("val");
                                        event.bets.put(BetType.P1, bet1);
                                        event.bets.put(BetType.X, bet2);
                                        event.bets.put(BetType.P2, bet3);
                                    } else if (title.equals("Double Chance")) {
                                        Elements bets = betsTable.getElementsByClass("odds");
                                        String bet1 = bets.select("a").get(0).attr("val");
                                        String bet2 = bets.select("a").get(1).attr("val");
                                        String bet3 = bets.select("a").get(2).attr("val");
                                        event.bets.put(BetType.P1X, bet1);
                                        event.bets.put(BetType.P12, bet2);
                                        event.bets.put(BetType.P2X, bet3);
                                    } else if (title.equals("Over/Under")) {
                                        Elements rowsTitle = betsTable.select("tr").get(1).select("th");
                                        Elements rowsVal = betsTable.select("tr").get(2).select("td");
                                        for (int i = 0; i < rowsTitle.size(); i++) {
                                            final String totalNum = rowsTitle.get(i).select("span").first().text().trim();
                                            final String totalOverVal = rowsVal.get(i * 2).select("a").first().attr("val");
                                            final String totalUnderVal = rowsVal.get(i * 2 + 1).select("a").first().attr("val");
                                            BetType tM = BetDataMapping.betsTotal.get(totalNum + "M");
                                            BetType tL = BetDataMapping.betsTotal.get(totalNum + "L");
                                            event.bets.put(tM, totalOverVal);
                                            event.bets.put(tL, totalUnderVal);
                                        }
                                    } else if (title.equals("Correct Score")) {
                                    /*List<Element> scoreTables = new ArrayList<Element>();
                                    scoreTables.add(rowB.getElementsByClass("home").first());
                                    scoreTables.add(rowB.getElementsByClass("draw").first());
                                    scoreTables.add(rowB.getElementsByClass("away").first());
                                    for (Element scoreTable: scoreTables) {
                                        for (Element rowScore: scoreTable.select("td")) {
                                            final String oddLabel = rowScore.getElementsByClass("oddLabel").first().text().trim();
                                            final String scoreVal = rowScore.select("a").first().attr("val");
                                            Bet scoreBet = BetDataMapping.totalScore.get(oddLabel.replace("-", ":"));
                                            event.bets.put(scoreBet, scoreVal);
                                        }
                                    }*/
                                    } else if (title.equals("Handicap 1X2")) {
                                        Elements gRows = betsTable.select("tr");
                                        final int size = gRows.size() / 2;
                                        for (int i = 0; i < size; i++) {
                                            for (int j = 0; j < 2; j++) {
                                                Elements headers = gRows.get(i * 2 + 1).select("th");
                                                Element hRow = null;
                                                if (headers.size() > 1) {
                                                    hRow = headers.get(j);
                                                } else {
                                                    hRow = headers.get(0);
                                                }
                                                String gStr = hRow.select("span").first().text().trim();
                                                for (int k = 0; k < 3; k++) {
                                                    Element rRow = gRows.get(i * 2 + 2).select("td").get(k + (j * 3));
                                                    final String gStrAttr = rRow.select("span").first().text().trim();
                                                    if (gStrAttr.equals("2H")) {
                                                        gStr = invert(gStr);
                                                    }
                                                    final String gVal = rRow.select("a").first().attr("val");
                                                    final String key = gStrAttr + gStr;
                                                    BetType gBet = BetDataMapping.gandicaps.get(key);
                                                    if (gBet != null) {
                                                        event.bets.put(gBet, gVal);
                                                    }
                                                }
                                                if (headers.size() == 1) {
                                                    break;
                                                }
                                            }
                                        }
                                    } else if (title.equals("Asian Handicap")) {
                                        for (Element rowFora : betsTable.select("tr")) {
                                            Elements foras = rowFora.select("th");
                                            if (foras.size() > 0) {
                                                //
                                            } else {
                                                foras = rowFora.select("td");
                                                final String fora1Name = foras.get(1).getElementsByClass("aux1").first().text().trim();
                                                final String fora1Val = foras.get(3).select("a").first().attr("val").trim();
                                                BetType fora1Bet = foraMap.get("1" + fora1Name);
                                                event.bets.put(fora1Bet, fora1Val);
                                                final String fora2Name = foras.get(2).getElementsByClass("aux1").first().text().trim();
                                                final String fora2Val = foras.get(4).select("a").first().attr("val").trim();
                                                BetType fora2Bet = foraMap.get("2" + fora2Name);
                                                event.bets.put(fora2Bet, fora2Val);
                                            }

                                        }
                                    }
                                }
                                EventSender.sendEvent(event);
                                events.add(event);
                            } catch (Exception e) {
                                logger.error("BetInParser exception - ", e);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("BetInParser exception - ", e);
        }
        return events;
    }

    private String invert(String C) {
        if (C.contains("-")) {
            return C.replace("-","+");
        } else {
            return C.replace("+","-");
        }
    }

}
