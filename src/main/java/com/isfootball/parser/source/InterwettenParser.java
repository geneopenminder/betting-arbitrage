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
import org.openqa.selenium.WebDriver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by Evgeniy Pshenitsin on 10.02.2015.
 */
public class InterwettenParser extends BaseParser {

    //https://www.interwetten.com/en/sportsbook/

    private static final Logger logger = LogManager.getLogger("parser");

    public InterwettenParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.INTERWETTEN;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();

        try {
            Document doc = Jsoup.connect("https://www.interwetten.com/en/sportsbook/default.aspx")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                    .timeout(10 * 1000).get();
            Element list = doc.getElementById("divSubMenu_10");

            events = list.select("a").parallelStream()
                    .map(href -> { return parseEvent(href);})
                    .filter(event -> event != null)
                    .filter(event -> !event.bets.isEmpty())
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Interwetten exception - ", e);
        }
        return events;
    }

    private BasicEvent parseEvent(Element href) {
        try {
            if (!href.attr("id").isEmpty() && href.attr("id").contains("league")) {
                String hrefSb = href.attr("href");
                final String leagueUrl = "https://www.interwetten.com" + hrefSb;
                Document docLeague = Jsoup.connect(leagueUrl)
                        .ignoreContentType(true)
                        .cookie("ASP.NET_SessionId", "1pi5r4bvsbr0e5kxuzzhzguc")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                        .timeout(10 * 1000)
                        .get();

                Element table = docLeague.getElementById("TBL_Content_" + href.attr("id").substring(7));
                String currDate = "";
                Date day = null;
                for (Element line : table.select("tr")) {
                    Element playtime = line.getElementsByClass("playtime").first();
                    if (playtime != null) {
                        currDate = playtime.text();
                        day = DateFormatter.format(currDate, betSite);
                    }
                    Element more = line.getElementsByClass("more").first();
                    if (more != null) {
                        //event
                        if (more.select("a").first() == null) {
                            continue;
                        }
                        String eventLink = more.select("a").first().attr("href");
                        final String eventUrl = "https://www.interwetten.com" + eventLink;
                        Document eventDoc = Jsoup.connect(eventUrl)
                                .ignoreContentType(true)
                                .cookie("ASP.NET_SessionId", "1pi5r4bvsbr0e5kxuzzhzguc")
                                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                                .timeout(10 * 1000)
                                .get();
                        BasicEvent event = new BasicEvent();
                        event.date = currDate;
                        event.day = day;
                        Element divRoot = eventDoc.getElementById("divRoot").getElementsByClass("colmiddle").first();
                        String[] teams = divRoot.getElementsByClass("text").first().text().split(" - ");
                        if (teams == null || teams.length != 2) {
                            continue;
                        }
                        event.team1 = Teams.getTeam(teams[0].trim());
                        event.team2 = Teams.getTeam(teams[1].trim());
                        if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                            logger.warn("Interwetten - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                            continue;
                        }
                        event.site = BetSite.INTERWETTEN;
                        event.link = eventUrl;
                        for (Element oddsBlock : divRoot.getElementsByClass("even")) {
                            String betType = oddsBlock.getElementsByClass("bets").first().getElementsByClass("info").first().text().trim();
                            if (betType.equals("Match")) {
                                Elements td = oddsBlock.getElementsByClass("bets").first().select("table").first().select("td");
                                if (td.size() == 3) {
                                    String p1BetVal = td.get(0).select("strong").text().replace(",", ".");
                                    event.bets.put(BetType.P1, p1BetVal);
                                    String pXBetVal = td.get(1).select("strong").text().replace(",", ".");
                                    event.bets.put(BetType.X, pXBetVal);
                                    String p2BetVal = td.get(2).select("strong").text().replace(",", ".");
                                    event.bets.put(BetType.P2, p2BetVal);
                                }
                            } else if (betType.equals("Double Chance")) {
                                Elements tds = oddsBlock.getElementsByClass("bets").first().select("table").first().select("td");
                                for (Element td : tds) {
                                    String type = td.select("p").first().select("span").text().trim();
                                    String val = td.select("p").first().select("strong").text().trim().replace(",", ".");
                                    if (type.equals("1X")) {
                                        event.bets.put(BetType.P1X, val);
                                    } else if (type.equals("X2")) {
                                        event.bets.put(BetType.P2X, val);
                                    }
                                }
                            } else if (betType.equals("Double Chance 1/2 - X")) {
                                Elements tds = oddsBlock.getElementsByClass("bets").first().select("table").first().select("td");
                                for (Element td : tds) {
                                    String type = td.select("p").first().select("span").text().trim();
                                    String val = td.select("p").first().select("strong").text().trim().replace(",", ".");
                                    if (type.equals("1/2")) {
                                        event.bets.put(BetType.P12, val);
                                    }
                                }
                            } else if (betType.contains("Asian") && betType.contains("Ball Handicap")) {
                                Elements tds = oddsBlock.getElementsByClass("bets").first().select("table").first().select("td");
                                if (tds.size() == 2) {
                                    //F1
                                    /*String type1 = getForaType(tds.get(0).select("p").first().select("span").text().trim());
                                    String val1 = tds.get(0).select("p").first().select("strong").text().trim().replace(",", ".");
                                    BetType f1 = BetDataMapping.betsTotal.get(type1 + "F1");
                                    if (f1 != null) {
                                        event.bets.put(f1, val1);
                                    }
                                    //F2
                                    String type2 = getForaType(tds.get(1).select("p").first().select("span").text().trim());
                                    String val2 = tds.get(1).select("p").first().select("strong").text().trim().replace(",", ".");
                                    BetType f2 = BetDataMapping.betsTotal.get(type2 + "F2");
                                    if (f2 != null) {
                                        event.bets.put(f2, val2);
                                    }*/
                                }
                            } else if (betType.equals("How many goals")) {
                                Elements tds = oddsBlock.getElementsByClass("bets").first().select("table").first().select("td");
                                if (tds.size() == 2) {
                                    String goalsF1 = tds.get(0).select("p").first().select("span").text().trim();
                                    String type = "";
                                    if (goalsF1.equalsIgnoreCase("NO goal")) {
                                        type = "0.5";
                                    } else {
                                        type = tds.get(0).select("p").first().select("span").text().trim().split("-")[1];
                                        type = Double.toString(Double.parseDouble(type) + 0.5);
                                    }
                                    BetType tL = BetDataMapping.betsTotal.get(type + "L");
                                    if (tL != null) {
                                        event.bets.put(tL, tds.get(0).select("p").first().select("strong").text().trim().replace(",", "."));
                                    }
                                    BetType tM = BetDataMapping.betsTotal.get(type + "M");
                                    if (tM != null) {
                                        event.bets.put(tM, tds.get(1).select("p").first().select("strong").text().trim().replace(",", "."));
                                    }
                                }

                            } else if (betType.contains("Handicap")) {
                                String[] handicapType = betType.substring(9).trim().split(":");
                                if (handicapType.length == 2) {
                                    Elements td = oddsBlock.getElementsByClass("bets").first().select("table").first().select("td");
                                    if (td.size() == 3) {
                                        String gWeight = Integer.toString(Integer.parseInt(handicapType[0]) - Integer.parseInt(handicapType[1]));
                                        BetType bet = BetDataMapping.gandicaps.get("1H" + gWeight);
                                        if (bet != null) {
                                            String g1BetVal = td.get(0).select("strong").text().replace(",", ".");
                                            event.bets.put(bet, g1BetVal);
                                        }
                                        bet = BetDataMapping.gandicaps.get("XH" + gWeight);
                                        if (bet != null) {
                                            String gXBetVal = td.get(1).select("strong").text().replace(",", ".");
                                            event.bets.put(bet, gXBetVal);
                                        }
                                        bet = BetDataMapping.gandicaps.get("2H" + invert(gWeight));
                                        if (bet != null) {
                                            String g2BetVal = td.get(2).select("strong").text().replace(",", ".");
                                            event.bets.put(bet, g2BetVal);
                                        }
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
            logger.error("Interwetten exception - ", e);
        }
        return null;
    }

    private String invert(String C) {
        if (C.contains("-")) {
            return C.replace("-","");
        } else return "-" + C;
    }

    private String getForaType(String input) {
        int firstPos = input.indexOf("(");
        if (firstPos != -1) {
            int secondPos = input.indexOf(")");
            if (secondPos != -1) {
                return input.substring(firstPos + 1, secondPos).replace("+", "").replace(",",".");
            }
        }
        return "";
    }

}
