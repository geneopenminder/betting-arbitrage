package com.isfootball.parser.source;

import com.google.gson.Gson;
import com.isfootball.model.BasicEvent;
import com.isfootball.parser.*;
import com.isfootball.pool.BasePoolInterface;
import com.isfootball.processing.EventSender;
import com.isfootball.utils.HttpClientInitializer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;

import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Evgeniy Pshenitsin on 05.01.2015.
 */
public class OneXBetParser extends BaseParser {

    //https://1-x-bet.com/
    private static Gson gson = new Gson();

    //30.01.2015
    static DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

    private static final Logger logger = LogManager.getLogger("parser");

    public static class BetsInfo {
      Value Value;
    };

    public static class Value {
        List<Event> Events;
    };

    public static class Event {
        String B;
        String C;
        int G;
        String P;
        String P1;
        int T;
    };

    public OneXBetParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.ONEXBET;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        List<String> leagues = new ArrayList<String>();

        try {
            //https://1-x-bet.com/line/Futbol/
            Document doc = Jsoup.connect("https://xbet101.com/line/Football/") //
                    .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:45.0) Gecko/20100101 Firefox/44.0")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .get();
            Element game1 = doc.getElementById("nSp_1"); //game_1
            for (Element li: game1.select("li")) {
                final String leagueLink = li.select("a").first().attr("href");
                leagues.add(leagueLink);
            }
            for (String leagueLink: leagues) {
                if (leagueLink.contains("let/")
                        || leagueLink.contains("Reserve")
                        || leagueLink.contains("Juniori")
                        || leagueLink.contains("juniori")
                        || leagueLink.contains("Primavera")
                        || leagueLink.contains("primavera")
                        || leagueLink.contains("U21")
                        || leagueLink.contains("U20")
                        || leagueLink.contains("U18")
                        || leagueLink.contains("U19")
                        || leagueLink.contains("Youth")
                        || leagueLink.contains("Women")
                        || leagueLink.contains("Zhenschiny")
                        || leagueLink.contains("Molodezhnyy")
                        || leagueLink.contains("Rezervnaya")
                        || leagueLink.contains("Alternativnye")
                        || leagueLink.contains("do-21")
                        || leagueLink.contains("do-20")
                        || leagueLink.contains("do-19")
                        || leagueLink.contains("-let")
                        || leagueLink.contains("Alternativnye")
                        || leagueLink.contains("U17")
                        || leagueLink.equalsIgnoreCase("line/Football/")) {
                    continue;
                }
                final String leagueLinkFull = "https://xbet101.com/" + leagueLink;
                try {
                    doc = Jsoup.connect(leagueLinkFull)
                            .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:45.0) Gecko/20100101 Firefox/44.0")
                            .header("Accept-Encoding", "gzip, deflate, br")
                            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                            .get();
                } catch (Exception e) {
                    logger.error("OneXBetParser error - " + leagueLinkFull, e);
                    continue;
                }
                Iterator<Element> tables = doc.getElementsByClass("hot_live_bet").select("table").iterator();
                while (tables.hasNext()) {
                    Element table = tables.next();
                    Element e = table.getElementsByClass("name").first();
                    if (e != null) {
                        BasicEvent event = new BasicEvent();
                        event.site = BetSite.ONEXBET;
                        event.link = leagueLinkFull;
                        event.date = table.getElementsByClass("score").text().substring(0,5) + "." + Calendar.getInstance().get(Calendar.YEAR);
                        event.day = format.parse(event.date);
                        final String teamsLine = e.getElementsByClass("gname").text();
                        if (teamsLine.contains("Хозяева") || teamsLine.contains("Угловые") ||
                                teamsLine.contains("карточки")) {
                            break;
                        }
                        String[] teams = teamsLine.split(" — ");
                        if (teams.length != 2) {
                            logger.error("OneXBetParser teams line error - " + teamsLine);
                            continue;
                        }
                        final int locationIndex1 = teams[0].indexOf("(");
                        if (locationIndex1 != -1) {
                            event.team1 = Teams.getTeam(teams[0].substring(0, locationIndex1).trim());
                        } else {
                            if (teams[0].contains("ФК")) {
                                event.team1 = Teams.getTeam(teams[0].trim().substring(3));
                            } else {
                                event.team1 = Teams.getTeam(teams[0].trim());
                            }
                        }
                        final int locationIndex2 = teams[1].indexOf("(");
                        if (locationIndex2 != -1) {
                            event.team2 = Teams.getTeam(teams[1].substring(0, locationIndex2).trim());
                        } else {
                            if (teams[1].contains("ФК")) {
                                event.team2 = Teams.getTeam(teams[1].trim().substring(3));
                            } else {
                                event.team2 = Teams.getTeam(teams[1].trim());
                            }
                        }
                        if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                            logger.warn("OneXBet - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                            continue;
                        }
                        Element betId = table.getElementsByClass("score").first();
                        //final String id = betId.getElementsByClass("hot_table_plus").first().attr("id").substring(3);
                        final String id = betId.getElementsByClass("hot_table_plus").first().attr("data-idgame").trim();
                        final String linkBets = "https://1xbet86.com/LineFeed/GetGame";
                        //HttpClientInitializer.setupConnection();
                        Thread.sleep(100);
                        GetMethod get = new GetMethod(linkBets);
                        HttpClient httpClient = new HttpClient(HttpClientInitializer.getConnectionManager());
                        get.setQueryString(new NameValuePair[]{
                                new NameValuePair("id", id),
                                new NameValuePair("lng", "ru"),
                        });
                        try {
                            int returnCode = httpClient.executeMethod(get);
                            if (returnCode == HttpStatus.SC_OK) {
                                String ret = IOUtils.toString(new InputStreamReader(get.getResponseBodyAsStream(), "UTF-8"));
                                OneXBetParser.BetsInfo info = gson.fromJson(ret, OneXBetParser.BetsInfo.class);

                                for (OneXBetParser.Event eventBet : info.Value.Events) {
                                    BetType bet = null;
                                    if (eventBet.G == 17) {
                                        if (eventBet.T == 10) {
                                            bet = BetDataMapping.betsTotal.get(eventBet.P + "L");
                                        } else if (eventBet.T == 9) {
                                            bet = BetDataMapping.betsTotal.get(eventBet.P + "M");
                                        }
                                    } else if (eventBet.G == 15) {
                                        //ind total 1
                                        if (eventBet.T == 11) {
                                            bet = BetDataMapping.betsTotal.get("T1_" + eventBet.P + "M");
                                        } else if (eventBet.T == 12) {
                                            bet = BetDataMapping.betsTotal.get("T1_" + eventBet.P + "L");
                                        }

                                    } else if (eventBet.G == 62) {
                                        //ind total 2
                                        if (eventBet.T == 13) {
                                            bet = BetDataMapping.betsTotal.get("T2_" + eventBet.P + "M");
                                        } else if (eventBet.T == 14) {
                                            bet = BetDataMapping.betsTotal.get("T2_" + eventBet.P + "L");
                                        }
                                    } else if (eventBet.G == 1) {
                                        if (eventBet.T == 1) {
                                            bet = BetType.P1;
                                        } else if (eventBet.T == 2) {
                                            bet = BetType.X;
                                        } else if (eventBet.T == 3) {
                                            bet = BetType.P2;
                                        }
                                    } else if (eventBet.G == 2) {
                                        if (eventBet.T == 7) {
                                            bet = BetDataMapping.betsTotal.get(eventBet.P + "F1");
                                        } else if (eventBet.T == 8) {
                                            bet = BetDataMapping.betsTotal.get(eventBet.P + "F2");
                                        }
                                    } else if (eventBet.G == 8) {
                                        if (eventBet.T == 4) {
                                            bet = BetType.P1X;
                                        } else if (eventBet.T == 5) {
                                            bet = BetType.P12;
                                        } else if (eventBet.T == 6) {
                                            bet = BetType.P2X;
                                        }
                                    } else if (eventBet.G == 27) {
                                        if (eventBet.T == 424) {
                                            //G1
                                            bet = BetDataMapping.gandicaps.get("1H" + eventBet.P);
                                        } else if (eventBet.T == 425) {
                                            //GX
                                            bet = BetDataMapping.gandicaps.get("XH" + eventBet.P);
                                        } else if (eventBet.T == 426) {
                                            //G2
                                            bet = BetDataMapping.gandicaps.get("2H" + invert(eventBet.P));
                                        }
                                    }
                                    if (bet != null) {
                                        event.bets.put(bet, eventBet.C);
                                    }
                                }
                                //betsTotal
                            }
                        } catch (Exception ex) {
                            logger.error("OneXBet net exception - ", e);
                        }
                        EventSender.sendEvent(event);
                        events.add(event);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("OneXBetParser exception - ", e);
        }
        return events;
    }

    private String invert(String C) {
        if (C.contains("-")) {
            return C.replace("-","");
        } else return "-" + C;
    }

}
