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
import org.openqa.selenium.WebDriver;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Evgeniy Pshenitsin on 27.01.2015.
 */
public class RuUnibetParser extends BaseParser {


    //https://ru2.unibet.com/betting

    //2015-02-17T19:45Z
    static DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm'Z'", Locale.ENGLISH);


    private static Gson gson = new Gson();

    private static final Logger logger = LogManager.getLogger("parser");

    public static class MainGroup {
        public Group group;
    };

    public static class Group {
        public int id;
        public int boCount;
        public List<GroupLeague> groups;
    };

    public static class GroupLeague {
        public int id;
        public int boCount;
        public String name;
        public String englishName;
        public String sport;
        public int eventCount;
        public List<GroupEvent> groups;
    };

    public static class GroupEvent {
        public int id;
        public int boCount;
        public String name;
        public String englishName;
        public int sortOrder;
        public String sport;
        public String termKey;
        public int eventCount;
        public List<GroupEvent> groups;
    };

    public static class League {
        public List<Event> events;
        public Range range;
    };

    public static class Event {
        LeagueEvent event;
        public List<BetOffers> betoffers;
    }

    public static class Range {
        public int start;
        public int size;
        public int total;
    };

    public static class LeagueEvent {
        public int id;
        public String name;
        public String homeName;
        public String awayName;
        public String start;
        public String group;
        public String type;
        public int nonLiveBoCount;
        public boolean streamed;
        public boolean liveBetOffers;
        public boolean openForLiveBetting;
        public String boUri;
        public int groupId;
        public boolean hideStartNo;
        public int sportId;
        public String sport;
        public List<Stream> streams;
        public List<Paths> path;
        public String englishName;
        public String state;
    };

    public class Paths {
        public int id;
        public String name;
        public String englishName;

    };

    public static class Stream {
        public int channelId;
    };

    public static class BetOffers {
        public int id;
        public String closed;
        public Criterion criterion;
        public BetOfferType betOfferType;
        public int eventId;
        public List<Outcome> outcomes;
        public boolean main;

    };

    public static class Outcome {
        public int id;
        public String label;
        public int odds;
        public String type;
        public int line;
        public int betOfferId;
        public String changedDate;
        public String oddsFractional;
        public int oddsAmerican;
    };

    public static class BetOfferType {
        public int id;
        public String name;
    };

    public static class Criterion {
        public int id;
        public String label;
        public boolean isDefault;
    };


    public static class Bet {
        public List<BetOffer> betoffers;

    };

    public static class BetOffer {
        public int id;
        public String closed;
        public Criterion criterion;
        public BetOfferType betOfferType;

    };

    public RuUnibetParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.RUUNIBET;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        HttpClient httpClient = new HttpClient(HttpClientInitializer.getConnectionManager());
        try {

            //https://e4-api.kambi.com/offering/api/v3/ub/listView/football/?lang=ru_RU&market=RU&client_id=2&channel_id=1&ncid=1458226512747&betOffers=COMBINED&categoryGroup=COMBINED
            GetMethod get = new GetMethod("https://e4-api.kambi.com/offering/api/v2/ub/group.json");
            get.setQueryString(new NameValuePair[] {
                    new NameValuePair("ncid", "1458298768507"),
                    new NameValuePair("market", "ru"),
                    new NameValuePair("lang", "ru_RU"),
                    new NameValuePair("channel_id", "1"),
            });
            int returnCode = httpClient.executeMethod(get);
            if (returnCode == HttpStatus.SC_OK) {
                String ret = IOUtils.toString(new InputStreamReader(get.getResponseBodyAsStream(), "UTF-8"));
                MainGroup main = gson.fromJson(ret, MainGroup.class);
                for (GroupLeague league: main.group.groups) {
                    if (league.englishName.equals("Football")) {
                        for (GroupEvent event: league.groups) {
                            if (event.groups == null) {
                                final String url = "https://e4-api.kambi.com/offering/api/v3/ub/listView/football/" + event.termKey + "/";
                                events.add(parseLeague(url, httpClient));
                            } else {
                                for (GroupEvent g: event.groups) {
                                    final String url = "https://e4-api.kambi.com/offering/api/v3/ub/listView/football/"+ event.termKey + "/" + g.termKey + "/";
                                    events.add(parseLeague(url, httpClient));
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("RuUnibet exception - ", e);

        }
        return events;
    }

    private BasicEvent parseLeague(String url, HttpClient httpClient) {
        try {
            int returnCode;
            GetMethod getLeague = new GetMethod(url);
            getLeague.setQueryString(new NameValuePair[]{
                    new NameValuePair("lang", "ru_RU"),
                    new NameValuePair("market", "ru"),
                    new NameValuePair("client_id", "2"),
                    //new NameValuePair("ncid", "1458300288325"),
                    //new NameValuePair("betOffers", "BET_OFFER_CATEGORY_SELECTION"),
                    //new NameValuePair("betOfferCategory", "Match Odds"),
                    //new NameValuePair("category", "Match Odds"),
                    //new NameValuePair("categoryGroup", "BET_OFFER_CATEGORY_SELECTION"),
                    new NameValuePair("channel_id", "1"),
            });
            returnCode = httpClient.executeMethod(getLeague);
            if (returnCode == HttpStatus.SC_OK) {
                String retLeague = IOUtils.toString(new InputStreamReader(getLeague.getResponseBodyAsStream(), "UTF-8"));
                League leagueObj = gson.fromJson(retLeague, League.class);
                for (Event event : leagueObj.events) {
                    LeagueEvent lEv = event.event;
                    BasicEvent eventB = new BasicEvent();
                    eventB.site = BetSite.RUUNIBET;
                    eventB.link = "ru1.unibet.com";
                    eventB.date = lEv.start;
                    eventB.day = new Date(Long.parseLong(eventB.date));
                    String[] teams = lEv.englishName.split(" - ");
                    eventB.team1 = Teams.getTeam(teams[0].trim());
                    eventB.team2 = Teams.getTeam(teams[1].trim());
                    if (eventB.team1 == Teams.TEAM_UNKNOWN || eventB.team2 == Teams.TEAM_UNKNOWN) {
                        logger.warn("RuUnibet - unknown teams: " + teams[0] + ":" + eventB.team1 + ";" + teams[1] + ":" + eventB.team2);
                        continue;
                    }
                    //https://e4-api.kambi.com/offering/api/v2/ub/betoffer/event/1002750592.json?lang=ru_RU&market=RU&client_id=2&channel_id=1
                    GetMethod getEventBets = new GetMethod("https://e4-api.kambi.com/offering/api/v2/ub/betoffer/event/" + lEv.id + ".json");
                    getEventBets.setQueryString(new NameValuePair[]{
                            new NameValuePair("lang", "ru_RU"),
                            new NameValuePair("market", "ru"),
                            new NameValuePair("client_id", "2"),
                            new NameValuePair("channel_id", "1"),
                    });
                    returnCode = httpClient.executeMethod(getEventBets);
                    if (returnCode == HttpStatus.SC_OK) {
                        String retEventBets = IOUtils.toString(new InputStreamReader(getEventBets.getResponseBodyAsStream(), "UTF-8"));
                        Event betsObj = gson.fromJson(retEventBets, Event.class);
                        for (BetOffers bet : betsObj.betoffers) {
                            if (bet.criterion.label.equalsIgnoreCase("Матч")) { //Full Time
                                for (Outcome o : bet.outcomes) {
                                    final String oddsVal = new BigDecimal(new Double(o.odds) / 1000).setScale(3, BigDecimal.ROUND_HALF_UP).toString();
                                    if (o.type.equals("OT_ONE")) {
                                        BetType b = BetType.P1;
                                        eventB.bets.put(b, oddsVal);
                                    } else if (o.type.equals("OT_CROSS")) {
                                        BetType b = BetType.X;
                                        eventB.bets.put(b, oddsVal);
                                    } else if (o.type.equals("OT_TWO")) {
                                        BetType b = BetType.P2;
                                        eventB.bets.put(b, oddsVal);
                                    }
                                }

                            } else if (bet.criterion.label.equalsIgnoreCase("Двойной шанс")) {//Double Chance
                                for (Outcome o : bet.outcomes) {
                                    final String oddsVal = new BigDecimal(new Double(o.odds) / 1000).setScale(3, BigDecimal.ROUND_HALF_UP).toString();
                                    if (o.type.equals("OT_ONE_OR_CROSS")) {
                                        BetType b = BetType.P1X;
                                        eventB.bets.put(b, oddsVal);
                                    } else if (o.type.equals("OT_ONE_OR_TWO")) {
                                        BetType b = BetType.P12;
                                        eventB.bets.put(b, oddsVal);
                                    } else if (o.type.equals("OT_CROSS_OR_TWO")) {
                                        BetType b = BetType.P2X;
                                        eventB.bets.put(b, oddsVal);
                                    }
                                }
                            } else if (bet.criterion.label.equalsIgnoreCase("Всего голов")) { //Total Goals
                                if (bet.betOfferType.name.equals("Over/Under")) {
                                    for (Outcome o : bet.outcomes) {
                                        final String oddsVal = new BigDecimal(new Double(o.odds) / 1000).setScale(3, BigDecimal.ROUND_HALF_UP).toString();
                                        if (o.type.equals("OT_OVER")) {
                                            final String line = Integer.toString(o.line);
                                            final String key = new BigDecimal(new Double(line) / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "M";
                                            BetType betTM = BetDataMapping.betsTotal.get(key);
                                            eventB.bets.put(betTM, oddsVal);
                                        } else if (o.type.equals("OT_UNDER")) {
                                            final String line = Integer.toString(o.line);
                                            final String key = new BigDecimal(new Double(line) / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "L";
                                            BetType betTL = BetDataMapping.betsTotal.get(key);
                                            eventB.bets.put(betTL, oddsVal);
                                        }
                                    }
                                }
                            } else if (bet.criterion.label.equalsIgnoreCase("Всего голов, забитых командой хозяев")) { //Total Goals by Home Team
                                //total 1 team
                                if (bet.betOfferType.name.equals("Over/Under")) {
                                    for (Outcome o : bet.outcomes) {
                                        final String oddsVal = new BigDecimal(new Double(o.odds) / 1000).setScale(3, BigDecimal.ROUND_HALF_UP).toString();
                                        if (o.type.equals("OT_OVER")) {
                                            final String line = Integer.toString(o.line);
                                            final String key = "T1_" + new BigDecimal(new Double(line) / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "M";
                                            BetType betTM = BetDataMapping.betsTotal.get(key);
                                            eventB.bets.put(betTM, oddsVal);
                                        } else if (o.type.equals("OT_UNDER")) {
                                            final String line = Integer.toString(o.line);
                                            final String key = "T1_" + new BigDecimal(new Double(line) / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "L";
                                            BetType betTL = BetDataMapping.betsTotal.get(key);
                                            eventB.bets.put(betTL, oddsVal);
                                        }
                                    }
                                }
                            } else if (bet.criterion.label.equalsIgnoreCase("Всего голов, забитых командой гостей")) { //Total Goals by Away Team
                                //total 2 team
                                if (bet.betOfferType.name.equals("Over/Under")) {
                                    for (Outcome o : bet.outcomes) {
                                        final String oddsVal = new BigDecimal(new Double(o.odds) / 1000).setScale(3, BigDecimal.ROUND_HALF_UP).toString();
                                        if (o.type.equals("OT_OVER")) {
                                            final String line = Integer.toString(o.line);
                                            final String key = "T2_" + new BigDecimal(new Double(line) / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "M";
                                            BetType betTM = BetDataMapping.betsTotal.get(key);
                                            eventB.bets.put(betTM, oddsVal);
                                        } else if (o.type.equals("OT_UNDER")) {
                                            final String line = Integer.toString(o.line);
                                            final String key = "T2_" + new BigDecimal(new Double(line) / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "L";
                                            BetType betTL = BetDataMapping.betsTotal.get(key);
                                            eventB.bets.put(betTL, oddsVal);
                                        }
                                    }
                                }
                                //} else if (bet.criterion.label.equals("Correct Score")) {
                                                /*for (Outcome o: bet.outcomes) {
                                                    final String oddsStr = Integer.toString(o.odds);
                                                    final String oddsVal = oddsStr.charAt(0) + "." + oddsStr.substring(1,3);
                                                    Bet scoreBet = BetDataMapping.totalScore.get(o.label.replace("-", ":"));
                                                    if (scoreBet != null)
                                                        eventB.bets.put(scoreBet, oddsVal);
                                                }*/
                            } else if (bet.criterion.label.equalsIgnoreCase("Азиатский гандикап")) { //Asian Handicap
                                final String oddsVal = new BigDecimal(new Double(bet.outcomes.get(0).odds) / 1000).setScale(3, BigDecimal.ROUND_HALF_UP).toString();
                                String line = new BigDecimal(new Double(bet.outcomes.get(0).line) / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                                String key = line + "F1";
                                BetType betF1 = BetDataMapping.betsTotal.get(key);
                                if (betF1 != null) {
                                    eventB.bets.put(betF1, oddsVal);
                                }
                                //two
                                final String odds2Val = new BigDecimal(new Double(bet.outcomes.get(1).odds) / 1000).setScale(3, BigDecimal.ROUND_HALF_UP).toString();
                                String line2 = new BigDecimal(new Double(bet.outcomes.get(1).line) / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                                String key2 = line2 + "F2";
                                BetType betF2 = BetDataMapping.betsTotal.get(key2);
                                if (betF2 != null) {
                                    eventB.bets.put(betF2, odds2Val);
                                }
                            } else if (bet.criterion.label.equalsIgnoreCase("Итоги по Азии")) { //Total Goals - Asian Lines
                                if (bet.betOfferType.name.equals("Asian Over/Under")) {
                                    for (Outcome o : bet.outcomes) {
                                        final String oddsVal = new BigDecimal(new Double(o.odds) / 1000).setScale(3, BigDecimal.ROUND_HALF_UP).toString();
                                        if (o.type.equals("OT_OVER")) {
                                            final String line = Integer.toString(o.line);
                                            final String key = new BigDecimal(new Double(line) / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "M";
                                            BetType betTM = BetDataMapping.betsTotal.get(key);
                                            if (betTM != null) {
                                                eventB.bets.put(betTM, oddsVal);
                                            }
                                        } else if (o.type.equals("OT_UNDER")) {
                                            final String line = Integer.toString(o.line);
                                            final String key = new BigDecimal(new Double(line) / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "L";
                                            BetType betTL = BetDataMapping.betsTotal.get(key);
                                            if (betTL != null) {
                                                eventB.bets.put(betTL, oddsVal);
                                            }
                                        }
                                    }
                                }
                            } else if (bet.criterion.label.equalsIgnoreCase("Тройной гандикап")) { //3-Way Handicap
                                for (Outcome o : bet.outcomes) {
                                    final String oddsVal = new BigDecimal(new Double(o.odds) / 1000).setScale(3, BigDecimal.ROUND_HALF_UP).toString();
                                    final String line = Integer.toString(o.line);
                                    String key = new BigDecimal(new Double(line) / 1000).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
                                    if (o.type.equals("OT_ONE")) {
                                        key = "1H" + key;
                                    } else if (o.type.equals("OT_CROSS")) {
                                        key = "XH" + key;
                                    } else if (o.type.equals("OT_TWO")) {
                                        key = "2H" + invert(key);
                                    }
                                    BetType betG = BetDataMapping.gandicaps.get(key);
                                    if (betG != null) {
                                        eventB.bets.put(betG, oddsVal);
                                    }
                                }
                            }
                        }
                    }
                    if (!eventB.bets.isEmpty()) {
                        EventSender.sendEvent(eventB);
                    }
                    return eventB;
                }
            }
        } catch (Exception e) {
            logger.error("RuUnibet exception - ", e);
        }
        return null;
    }

    private String getOddsVal(String oddsStr) {
        if (oddsStr.length() == 4) {
            return oddsStr.charAt(0) + "." + oddsStr.substring(1,oddsStr.length());
        } else {
            return oddsStr.substring(0, 2) + "." + oddsStr.substring(2,oddsStr.length());
        }
    }

    private String invert(String C) {
        if (C.contains("-")) {
            return C.replace("-","");
        } else return "-" + C;
    }

}
