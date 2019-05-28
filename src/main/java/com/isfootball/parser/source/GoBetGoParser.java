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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Evgeniy Pshenitsin on 15.01.2015.
 */
public class GoBetGoParser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    public GoBetGoParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        //this.betSite = BetSite.GOBETGO;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        List<String> links = new ArrayList<String>();
        List<String> betLinks = new ArrayList<String>();
        try {
            Document doc = Jsoup.connect("http://gobetgo.com/sports/AjaxSubMenu?gencat=sports&curcat=SOCCER&sid=7")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                    .timeout(10 * 1000).get();

            Iterator<Element> ul = doc.select("li").iterator();
            while (ul.hasNext()) {
                Element l = ul.next();
                String sgroupid = l.select("a").first().attr("data-event-group-id");
                links.add("http://gobetgo.com/sports/GetSport1X2Bets?sgroupid=" + sgroupid + "&currentViewType=Europe");
            }

            for (String link: links ){
                Document docChamp = Jsoup.connect(link)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                        .timeout(10 * 1000).get();
                Element eventsChamp = docChamp.getElementsByClass("eventstable").select("table").first();
                List<Element> details = eventsChamp.select("tr");
                for (Element bets: details) {
                    BasicEvent event = new BasicEvent();
                    Element td = bets.select("td").first();
                    String betId = td.getElementsByClass("parentView1x2Bets").attr("evt-id");
                    String dateStr = td.getElementsByClass("date").text();
                    event.date = dateStr.substring(0, dateStr.indexOf("\u00a0"));
                    if (betId.isEmpty()) {
                        continue;
                    }
                    final String betLink = "http://gobetgo.com/Sports/GetDetailedBets?sEventId=" + betId + "&currentViewType=Europe";
                    betLinks.add("http://gobetgo.com/Sports/GetDetailedBets?sEventId=" + betId + "&currentViewType=Europe");

                    Document docBet = Jsoup.connect(betLink)
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                            .timeout(10 * 1000).get();
                    String header = docBet.select("header").first().text();
                    String teamsStr = header.substring(0, header.indexOf("BACK"));
                    String[] teams = teamsStr.split(" v ");
                    event.team1 = Teams.getTeam(teams[0].trim());
                    event.team2 = Teams.getTeam(teams[1].trim());
                    if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                        logger.warn("GoBetGo - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                        continue;
                    }
                    //event.site = "gobetgo.com";
                    event.link = betLink;
                    List<Element> headers = docBet.getElementsByClass("panel");
                    for (Element h: headers) {

                        final String hText = h.text();
                        if (hText.contains("Match Odds")) {
                            event.bets.put(BetType.P1, h.getElementsByClass("parentBetNode").get(0).attr("oddval"));
                            event.bets.put(BetType.X, h.getElementsByClass("parentBetNode").get(1).attr("oddval"));
                            event.bets.put(BetType.P2, h.getElementsByClass("parentBetNode").get(2).attr("oddval"));
                        } else if (hText.contains("Double Chance")) {
                            event.bets.put(BetType.P1X, h.getElementsByClass("parentBetNode").get(0).attr("oddval"));
                            event.bets.put(BetType.P12, h.getElementsByClass("parentBetNode").get(1).attr("oddval"));
                            event.bets.put(BetType.P2X, h.getElementsByClass("parentBetNode").get(2).attr("oddval"));
                        } else if (hText.contains("Over/Under")) {
                            for (Element odd: h.getElementsByClass("or_team1")) {
                                String gtt = odd.attr("gtt");
                                String oddval = odd.attr("oddval");
                                if (!oddval.equals("0.0")) {
                                    if (gtt.contains("Over")) {
                                        String totalType = gtt.substring(5, 8);
                                        BetType bet = BetDataMapping.betsTotal.get(totalType + "M");
                                        event.bets.put(bet, oddval);
                                    } else {
                                        String totalType = gtt.substring(6, 9);
                                        BetType bet = BetDataMapping.betsTotal.get(totalType + "L");
                                        event.bets.put(bet, oddval);
                                    }
                                }
                            }
                        }
                    }
                    EventSender.sendEvent(event);
                    events.add(event);
                }
            }
        } catch (Exception e) {
            logger.error("GoBetGo exception - ", e);
        }
        return events;
    }
}
