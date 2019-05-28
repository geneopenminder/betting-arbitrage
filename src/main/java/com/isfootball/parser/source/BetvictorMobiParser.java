package com.isfootball.parser.source;

import com.google.gson.Gson;
import com.isfootball.model.BasicEvent;
import com.isfootball.parser.*;
import com.isfootball.pool.BasePoolInterface;
import com.isfootball.processing.EventSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Evgeniy Pshenitsin on 01.04.2015.
 */
public class BetvictorMobiParser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    private static Gson gson = new Gson();

    //static DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.GERMAN);

    public BetvictorMobiParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.BETVICTOR;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();

        try {

            DateTime dateTime = new DateTime(new Date());
            //today
            parseMatches("https://www.betvictor.mobi/sports/240/group/10000/market/1/coupon_meetings", dateTime.toDate(), events);
            //tomorrow
            dateTime.plusDays(1);
            parseMatches("https://www.betvictor.mobi/sports/240/group/10001/market/1/coupon_meetings", dateTime.toDate(), events);


            /*Document doc = Jsoup.connect("https://www.betvictor.mobi/sports/240/meetings")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                    .cookie("price_format_id", "3")
                    .cookie("locale", "en")
                    .timeout(10 * 1000).get();
            Elements leagues = doc.select("a");
            for (Element league: leagues) {
                String link = league.attr("href");
                if (link != null && link.contains("sports")) {
                    Document docLeague = Jsoup.connect("https://www.betvictor.mobi" + link)
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                            .cookie("price_format_id", "3")
                            .cookie("locale", "en")
                            .timeout(10 * 1000).get();
                    Element eventsUl = docLeague.getElementById("events");
                    if (eventsUl != null) {
                        Elements matches = eventsUl.select("a.two-lines");
                        for (Element matchLink: matches) {
                            String linkMatch = matchLink.attr("href");
                            if (linkMatch != null) {
                                Document docMatch = Jsoup.connect("https://www.betvictor.mobi" + linkMatch)
                                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                                        .cookie("price_format_id", "3")
                                        .cookie("locale", "en")
                                        .timeout(10 * 1000).get();
                                BasicEvent event = new BasicEvent();
                                Element markets = docMatch.getElementById("markets");
                                if (markets != null && markets.select("li.list-header.skin.light").first() != null) {
                                    String[] teams = markets.select("li.list-header.skin.light").first().text().split(" v ");
                                    event.team1 = Teams.getTeam(teams[0].trim());
                                    event.team2 = Teams.getTeam(teams[1].trim());
                                    if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                                        logger.warn("BetInParser - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                                        continue;
                                    }

                                    Elements marketsList = markets.select("a");
                                    for (Element a: marketsList) {
                                        String href = a.attr("href").trim();
                                        if (a.text().trim().equalsIgnoreCase("Match Betting - 90 Mins")) {
                                            Document docBets = Jsoup.connect("https://www.betvictor.mobi" + href)
                                                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                                                    .cookie("price_format_id", "3")
                                                    .cookie("locale", "en")
                                                    .timeout(10 * 1000).get();
                                            Element outcomes = docBets.getElementById("outcomes");
                                            if (outcomes != null) {
                                                Elements aaa = outcomes.select("a.match-betting");
                                                if (aaa != null && aaa.size() == 3) {
                                                    Element p1 = aaa.get(0).select("span.price.right").first();
                                                    if (p1 != null) {
                                                        event.bets.put(Bet.P1, p1.text().trim());
                                                    }
                                                    Element pX = aaa.get(1).select("span.price.right").first();
                                                    if (pX != null) {
                                                        event.bets.put(Bet.X, pX.text().trim());
                                                    }
                                                    Element p2 = aaa.get(2).select("span.price.right").first();
                                                    if (p2 != null) {
                                                        event.bets.put(Bet.P2, p2.text().trim());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }*/
        } catch (Exception e){
            logger.error("BetvictorMobiParser error", e);
        }
        return events;
    }

    private void parseMatches(String link, Date date, List<BasicEvent> events) {
        try {
            Document docLeague = Jsoup.connect(link)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                    .cookie("price_format_id", "3")
                    .cookie("locale", "en")
                    .timeout(10 * 1000).get();
            Element eventsUl = docLeague.getElementById("events");
            if (eventsUl != null) {
                Elements matches = eventsUl.select("a.two-lines");
                for (Element matchLink : matches) {
                    String linkMatch = matchLink.attr("href");
                    if (linkMatch != null) {
                        Thread.sleep(100);
                        Document docMatch = Jsoup.connect("https://www.betvictor.mobi" + linkMatch + "?utf8=✓&market_group=-1&commit=Go")
                                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                                .cookie("price_format_id", "3")
                                .cookie("locale", "en")
                                .timeout(10 * 1000).get();
                        BasicEvent event = new BasicEvent();
                        Element markets = docMatch.getElementById("markets");
                        if (markets != null && markets.select("li.list-header.skin.light").first() != null) {
                            String[] teams = markets.select("li.list-header.skin.light").first().text().split(" v ");
                            event.team1 = Teams.getTeam(teams[0].trim());
                            event.team2 = Teams.getTeam(teams[1].trim());
                            if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                                logger.warn("BetInParser - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                                continue;
                            }
                            event.date = date.toString();
                            event.day = date;
                            event.site = BetSite.BETVICTOR;
                            event.link = "https://www.betvictor.mobi" + linkMatch;
                            Elements marketsList = markets.select("a");
                            for (Element a : marketsList) {
                                String href = a.attr("href").trim();
                                String text = a.text().trim();
                                if (text.equalsIgnoreCase("Match Betting - 90 Mins")) {
                                    Document docBets = getOdds(href);
                                    Element outcomes = docBets.getElementById("outcomes");
                                    if (outcomes != null) {
                                        Elements aaa = outcomes.select("a.match-betting");
                                        if (aaa != null && aaa.size() == 3) {
                                            Element p1 = aaa.get(0).select("span.price.right").first();
                                            if (p1 != null) {
                                                event.bets.put(BetType.P1, p1.text().trim());
                                            }
                                            Element pX = aaa.get(1).select("span.price.right").first();
                                            if (pX != null) {
                                                event.bets.put(BetType.X, pX.text().trim());
                                            }
                                            Element p2 = aaa.get(2).select("span.price.right").first();
                                            if (p2 != null) {
                                                event.bets.put(BetType.P2, p2.text().trim());
                                            }
                                        }
                                    }
                                } else if (text.equalsIgnoreCase("Total Goals - 90 Mins")) {
                                    Document docBets = getOdds(href);
                                    Elements matchBetting = docBets.select("a.match-betting");
                                    for (Element total: matchBetting) {
                                        String totalType = total.select("span.mw-75").first().text().trim();
                                        BetType totalBet = null;
                                        if (totalType.contains("Over")) {
                                            totalBet = BetDataMapping.betsTotal.get(totalType.substring(5).trim() + "M");

                                        } else if (totalType.contains("Under")) {
                                            totalBet = BetDataMapping.betsTotal.get(totalType.substring(6).trim() + "L");
                                        }
                                        String totalVal = total.select("span.mw-25").first().text().trim();
                                        if (totalBet != null && !totalVal.isEmpty()) {
                                            event.bets.put(totalBet, totalVal);
                                        }
                                    }
                                } else if (text.equalsIgnoreCase("Double Chance - 90 Mins")) {
                                    Document docBets = getOdds(href);
                                    Elements oddsX = docBets.select("span.mw-25");
                                    if (oddsX.size() == 3) {
                                        String p1x = oddsX.get(0).text().trim();
                                        if (!p1x.isEmpty()) {
                                            event.bets.put(BetType.P1X, p1x);
                                        }
                                        String p12 = oddsX.get(1).text().trim();
                                        if (!p12.isEmpty()) {
                                            event.bets.put(BetType.P12, p12);
                                        }
                                        String p2x = oddsX.get(2).text().trim();
                                        if (!p2x.isEmpty()) {
                                            event.bets.put(BetType.P2X, p2x);
                                        }
                                    }
                                } else if (text.equalsIgnoreCase("Draw No Bet - 90 Mins")) {
                                    Document docBets = getOdds(href);
                                    Elements oddsFora0 = docBets.select("span.mw-25");
                                    if (oddsFora0.size() == 2) {
                                        String f10 = oddsFora0.get(0).text().trim();
                                        if (!f10.isEmpty()) {
                                            event.bets.put(BetType.F10, f10);
                                        }
                                        String f20 = oddsFora0.get(1).text().trim();
                                        if (!f20.isEmpty()) {
                                            event.bets.put(BetType.F20, f20);
                                        }
                                    }
                                } else if (text.equalsIgnoreCase("Asian Handicap - 90 Mins")) {
                                    Document docBets = getOdds(href);
                                    Elements foraTypes = docBets.select("span.mw-75");
                                    Elements foraVals = docBets.select("span.mw-25");
                                    if (foraTypes.size() == foraVals.size()) {
                                        for (int i = 0; i < foraTypes.size(); i++) {
                                            String foraType = getForaType(foraTypes.get(i).text().trim());
                                            String fora12 = (i%2 == 0) ? "F1":"F2";
                                            BetType fBet = BetDataMapping.betsTotal.get(foraType + fora12);
                                            String foraVal = foraVals.get(i).text().trim();
                                            if (fBet != null && !foraVal.isEmpty()) {
                                                event.bets.put(fBet, foraVal);
                                            }
                                        }
                                    }
                                }
                                events.add(event);
                                EventSender.sendEvent(event);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("BetvictorMobiParser parseMatches error", e);
        }
    }

    private String getForaType(String type) {
        //-1.5, -2.0
        try {
            if (type.contains(",")) {
                String[] foraType = type.split(",");
                String type2Final = Double.toString((Double.parseDouble(foraType[0]) + Double.parseDouble(foraType[1])) / 2.0);
                return type2Final;
            } else {
                return type.replace("+", "").trim();
            }
        } catch (Exception e) {
            logger.error("BetvictorMobiParser getForaType error", e);
            return "";
        }
    }

    private Document getOdds(String href) throws Exception {
        Document docBets = Jsoup.connect("https://www.betvictor.mobi" + href)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                .cookie("price_format_id", "3")
                .cookie("locale", "en")
                .timeout(10 * 1000).get();
        return docBets;
    }

}
