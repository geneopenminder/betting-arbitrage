package com.isfootball.parser.source;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.isfootball.model.BasicEvent;
import com.isfootball.parser.*;
import com.isfootball.pool.BasePoolInterface;
import com.isfootball.processing.EventSender;
import com.isfootball.utils.HttpClientInitializer;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.openqa.selenium.WebDriver;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Evgeniy Pshenitsin on 22.01.2015.
 */
public class MatchBookParser extends BaseParser {

    private static Gson gson = new Gson();

    private static final Logger logger = LogManager.getLogger("parser");

    static DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", Locale.ENGLISH);

    public static class MBookMain {
        public long id;
        public List<MBookMain> metaTags;
        public String name;
        public long treeId;

    };

    public class EventMain {
        public Integer offset;
        public Integer total;
        public Integer perPage;
        public List<Event> events = new ArrayList<Event>();
        public Map<String, Object> additionalProperties = new HashMap<String, Object>();
    }

    public static class Event {
        public long id;
        public String name;
        public String start;
        public String status;
        public Integer sportId;
        public List<Integer> categoryId = new ArrayList<Integer>();
        public Boolean inRunningFlag;
        public Boolean allowLiveBetting;
        public List<Market> markets = new ArrayList<Market>();
        public List<MBookMain> metaTags = new ArrayList<MBookMain>();
        public Map<String, Object> additionalProperties = new HashMap<String, Object>();

    };

    public class Market {

        public String name;
        public List<Runner> runners = new ArrayList<Runner>();
        public String start;
        public String status;
        public String type;
        public Long eventId;
        public Long id;
        public String gradingType;
        public Boolean inRunningFlag;
        public Boolean allowLiveBetting;
        public Map<String, Object> additionalProperties = new HashMap<String, Object>();
    };

    public class Runner {

        public String name;
        public String status;
        public List<Price> prices = new ArrayList<Price>();
        public Long eventId;
        public Long id;
        public Integer marketId;
        public Map<String, Object> additionalProperties = new HashMap<String, Object>();
    };

    public class Price {

        public String currency;
        public Double odds;
        public String side;
        public Double availableAmount;
        public String oddsType;
        public Double decimalOdds;
        public String exchangeType;
        public Map<String, Object> additionalProperties = new HashMap<String, Object>();
    };

    public class TokenAuth {
        public String sessionToken;
        public String userId;
        public String role;
    };

    public MatchBookParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.MATCHBOOK;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        String sessionToken = "";
        //get token

        //HttpHost proxy = new HttpHost("127.0.0.1", 8080, "http");

        HttpClient httpClient = new HttpClient(HttpClientInitializer.getConnectionManager());
        //HostConfiguration hostConfiguration = httpClient.getHostConfiguration();
        //hostConfiguration.setProxy("127.0.0.1", 8089);
        //httpClient.setHostConfiguration(hostConfiguration);
        //HttpPost postHttp = new HttpPost("https://www.matchbook.com/bpapi/rest/security/session/");
        PostMethod post = new PostMethod("https://www.matchbook.com/bpapi/rest/security/session/");
        //post.addParameter("JSON", "{\"username\":\"IsFootball\",\"password\":\"NeverBeat9\"}");
        //post.addParameter("password", "NeverBeat9");
        //post.setRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        //post.setRequestHeader("Accept-Encoding", "gzip, deflate");
        //post.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
        //post.addRequestHeader(new Header("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3"));
        //post.addRequestHeader(new Header("Cache-Control", "no-cache"));
        //post.addRequestHeader(new Header("Connection", "keep-alive"));
        post.addRequestHeader(new Header("Accept", "application/json, text/javascript, */*; q=0.01"));
        post.addRequestHeader(new Header("Accept-Encoding", "deflate, br"));
        post.addRequestHeader(new Header("Content-Type", "application/json"));
        post.addRequestHeader(new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0"));
        post.addRequestHeader(new Header("X-Requested-With", "XMLHttpRequest"));
        //post.addRequestHeader(new Header("Host", "www.matchbook.com"));
        //post.addRequestHeader(new Header("Pragma", "no-cache"));
        //post.addRequestHeader(new Header("Referer", "https://www.matchbook.com/events/286097/;tag-ids%5B%5D=1&tag-ids%5B%5D=4/"));
        /*post.setQueryString(new NameValuePair[] {
                new NameValuePair("username", "IsFootball"),
                new NameValuePair("password", "NeverBeat9"),
        });*/

        try {
            StringRequestEntity requestEntity = new StringRequestEntity(
                    "{\"username\":\"betarbs\",\"password\":\"betarbs1234\"}",
                    "application/json",
                    "UTF-8");
            post.setRequestEntity(requestEntity);
            int returnCode = httpClient.executeMethod(post);
            if (returnCode == HttpStatus.SC_OK) {
                String authRet = IOUtils.toString(new InputStreamReader(post.getResponseBodyAsStream(), "UTF-8"));


                //String acceptEncodingValue = post.getResponseHeader("Content-Encoding").getValue();
                //if(acceptEncodingValue.indexOf("gzip") == -1){
                //    logger.error("fail gzip");
                //}
                //GZIPInputStream zippedInputStream =  new GZIPInputStream(post.getResponseBodyAsStream());
                //String authRet = IOUtils.toString(new InputStreamReader(post.getResponseBodyAsStream(), "UTF-8"));
                //String authRet = IOUtils.toString(new InputStreamReader(zippedInputStream));
                authRet = authRet.replaceAll("session-token", "sessionToken");
                authRet = authRet.replaceAll("user-id", "userId");
                authRet = authRet.replaceAll("-", "");
                TokenAuth toketAuth = gson.fromJson(authRet, TokenAuth.class);
                sessionToken = toketAuth.sessionToken;

            }
        } catch (Exception e) {
            logger.error("MatchBookParser login exception " + e);
        }
        GetMethod get = new GetMethod("https://www.matchbook.com/edge/rest/navigation/");
        get.setQueryString(new NameValuePair[]{
                new NameValuePair("include-tags", "true"),
                new NameValuePair("language", "en"),
                new NameValuePair("currency", "EUR"),
                new NameValuePair("exchange-type", "back-lay"),
                new NameValuePair("odds-type", "DECIMAL"),
        });
        get.addRequestHeader(new Header("Accept", "application/json, text/javascript, */*; q=0.01"));
        get.addRequestHeader(new Header("Accept-Encoding", "deflate, br"));
        get.addRequestHeader(new Header("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3"));
        get.addRequestHeader(new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0"));
        get.addRequestHeader(new Header("X-Requested-With", "XMLHttpRequest"));
        try {
            int returnCode = httpClient.executeMethod(get);
            if (returnCode == HttpStatus.SC_OK) {
                String ret = IOUtils.toString(new InputStreamReader(get.getResponseBodyAsStream(), "UTF-8"));
                ret = ret.replace("meta-tags","metaTags");
                ret = ret.replace("tree-id", "treeId");
                List<MBookMain> main = gson.fromJson(ret, new TypeToken<List<MBookMain>>(){}.getType());
                for (MBookMain m: main) {
                    if (m.name.equals("Sport")) {
                        for (MBookMain sport: m.metaTags) {
                            if (sport.name.equals("Soccer")) {
                                for (MBookMain country: sport.metaTags) {
                                    for (MBookMain league: country.metaTags) {
                                        String lName = league.name;
                                        //https://www.matchbook.com/bpapi/rest/events/?include-markets=true&include-prices=true&include-runners=true&
                                        // market-per-page=3&price-depth=1&price-order=price+desc&offset=0&tag-ids=32&per-page=300&
                                        // currency=EUR&exchange-type=back-lay&language=en&odds-type=DECIMAL
                                        GetMethod getLeague = new GetMethod("https://www.matchbook.com/bpapi/rest/events/");
                                        getLeague.setQueryString(new NameValuePair[] {
                                                new NameValuePair("include-markets", "true"),
                                                new NameValuePair("include-prices", "true"),
                                                new NameValuePair("include-runners", "true"),
                                                new NameValuePair("market-per-page", "3"),
                                                new NameValuePair("price-depth", "1"),
                                                //new NameValuePair("price-order", "price+desc"),
                                                new NameValuePair("offset", "0"),
                                                new NameValuePair("tag-ids", Long.toString(league.id)),
                                                new NameValuePair("per-page", "18"),
                                                new NameValuePair("offset", "0"),
                                                new NameValuePair("offset", "0"),
                                                new NameValuePair("language", "en"),
                                                new NameValuePair("currency", "EUR"),
                                                new NameValuePair("exchange-type", "back-lay"),
                                                new NameValuePair("odds-type", "DECIMAL"),
                                        });
                                        getLeague.addRequestHeader(new Header("Accept", "application/json, text/javascript, */*; q=0.01"));
                                        getLeague.addRequestHeader(new Header("Accept-Encoding", "deflate, br"));
                                        getLeague.addRequestHeader(new Header("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3"));
                                        getLeague.addRequestHeader(new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0"));
                                        getLeague.addRequestHeader(new Header("X-Requested-With", "XMLHttpRequest"));
                                        int returnCodeL = httpClient.executeMethod(getLeague);
                                        if (returnCodeL == HttpStatus.SC_OK) {
                                            String retL = IOUtils.toString(new InputStreamReader(getLeague.getResponseBodyAsStream(), "UTF-8"));
                                            retL = retL.replace("per-page","perPage");
                                            retL = retL.replace("sport-id","sportId");
                                            retL = retL.replace("category-id","categoryId");
                                            retL = retL.replace("in-running-flag","inRunningFlag");
                                            retL = retL.replace("allow-live-betting","allowLiveBetting");
                                            retL = retL.replace("available-amount","availableAmount");
                                            retL = retL.replace("market-id","marketId");
                                            retL = retL.replace("meta-tags","metaTags");
                                            retL = retL.replace("exchange-type","exchangeType");
                                            retL = retL.replace("decimal-odds","decimalOdds");
                                            retL = retL.replace("event-id","eventId");
                                            retL = retL.replace("grading-type","gradingType");
                                            EventMain leagueData = gson.fromJson(retL, EventMain.class);
                                            for (Event e: leagueData.events) {
                                                long eventId = e.id;
                                                if (!e.name.contains(" vs ")) {
                                                    continue;
                                                }
                                                GetMethod getEvent = new GetMethod("https://www.matchbook.com/bpapi/rest/events/" + eventId);
                                                getEvent.addRequestHeader(new Header("Cookie", "session-token=" + sessionToken));
                                                getEvent.setQueryString(new NameValuePair[] {
                                                        new NameValuePair("include-markets", "true"),
                                                        new NameValuePair("include-prices", "true"),
                                                        new NameValuePair("include-runners", "true"),
                                                        new NameValuePair("market-per-page", "500"),
                                                        new NameValuePair("price-depth", "6"),
                                                        //new NameValuePair("price-order", "price+desc"),
                                                        new NameValuePair("offset", "0"),
                                                        new NameValuePair("tag-ids", Long.toString(league.id)),
                                                        new NameValuePair("per-page", "100"),
                                                        new NameValuePair("offset", "0"),
                                                        new NameValuePair("offset", "0"),
                                                        new NameValuePair("language", "en"),
                                                        new NameValuePair("currency", "EUR"),
                                                        new NameValuePair("exchange-type", "back-lay"),
                                                        new NameValuePair("odds-type", "DECIMAL"),
                                                });
                                                getEvent.addRequestHeader(new Header("Accept", "application/json, text/javascript, */*; q=0.01"));
                                                getEvent.addRequestHeader(new Header("Accept-Encoding", "deflate, br"));
                                                getEvent.addRequestHeader(new Header("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3"));
                                                getEvent.addRequestHeader(new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0"));
                                                getEvent.addRequestHeader(new Header("X-Requested-With", "XMLHttpRequest"));
                                                int returnCodeE = httpClient.executeMethod(getEvent);
                                                if (returnCodeE == HttpStatus.SC_OK) {
                                                    String retE = IOUtils.toString(new InputStreamReader(getEvent.getResponseBodyAsStream(), "UTF-8"));
                                                    retE = retE.replace("per-page","perPage");
                                                    retE = retE.replace("sport-id","sportId");
                                                    retE = retE.replace("category-id","categoryId");
                                                    retE = retE.replace("in-running-flag","inRunningFlag");
                                                    retE = retE.replace("allow-live-betting","allowLiveBetting");
                                                    retE = retE.replace("available-amount","availableAmount");
                                                    retE = retE.replace("market-id","marketId");
                                                    retE = retE.replace("meta-tags","metaTags");
                                                    retE = retE.replace("exchange-type","exchangeType");
                                                    retE = retE.replace("decimal-odds","decimalOdds");
                                                    retE = retE.replace("event-id","eventId");
                                                    retE = retE.replace("grading-type","gradingType");
                                                    Event eventData = gson.fromJson(retE, Event.class);
                                                    if (eventData.status.equals("open")) {
                                                        BasicEvent event = new BasicEvent();
                                                        event.site = BetSite.MATCHBOOK;
                                                        event.link = "www.matchbook.com";
                                                        event.date = eventData.start;
                                                        event.day = format.parse(eventData.start);
                                                        DateTime now = new DateTime(DateTimeZone.forOffsetHours(+1));
                                                        now = now.minusHours(2);
                                                        DateTime eventDate = new DateTime(event.day);
                                                        if (eventDate.isBefore(now)) {
                                                            continue;
                                                        }
                                                        String[] teams = eventData.name.split(" vs ");
                                                        event.team1 = Teams.getTeam(teams[0].trim());
                                                        event.team2 = Teams.getTeam(teams[1].trim());
                                                        if (event.team1 != Teams.TEAM_UNKNOWN && event.team2 != Teams.TEAM_UNKNOWN) {
                                                        } else {
                                                            logger.warn("MatchBook - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                                                            continue;
                                                        }
                                                        for (Market market: eventData.markets) {
                                                            for (int i = 0; i < market.runners.size(); i++) {
                                                                BetType bet = null;
                                                                String name = market.runners.get(i).name.trim();
                                                                if (name.contains("{")) {
                                                                    name = name.substring(0, name.indexOf("{")).trim();
                                                                }
                                                                Double maxOdds = 0.0;
                                                                for (Price price : market.runners.get(i).prices) {
                                                                    if (price.side.equals("back") && price.odds > maxOdds) {
                                                                        maxOdds = price.odds;
                                                                    }
                                                                }
                                                                if(maxOdds.equals(0.0)) {
                                                                    continue;
                                                                }
                                                                if (market.name.equals("Match Odds") && market.runners.size() == 3) {
                                                                    Double maxOddsLay = 100000.0;
                                                                    for (Price price : market.runners.get(i).prices) {
                                                                        if (price.side.equals("lay") && price.odds < maxOddsLay) {
                                                                            maxOddsLay = price.odds;
                                                                        }
                                                                    }
                                                                        if (i == 0) {
                                                                            bet = BetType.P1;
                                                                            event.bets.put(BetType.P2X, Double.toString(maxOddsLay/(maxOddsLay - 1.0)));
                                                                        } else if (i == 1) {
                                                                            bet = BetType.P2;
                                                                            event.bets.put(BetType.P1X, Double.toString(maxOddsLay/(maxOddsLay - 1.0)));
                                                                        } else if (i == 2) {
                                                                            bet = BetType.X;
                                                                            event.bets.put(BetType.P12, Double.toString(maxOddsLay/(maxOddsLay - 1.0)));
                                                                        }
                                                                } else if (market.name.equals("Handicap") && market.runners.size() == 2) {
                                                                            String key = getKeyForAsian(name).replace("+", "").trim();
                                                                            if (i == 0) {
                                                                                bet = BetDataMapping.betsTotal.get(key + "F1");
                                                                            } else if (i == 1) {
                                                                                bet = BetDataMapping.betsTotal.get(key + "F2");
                                                                            }
                                                                } else if (market.name.equals("Total") && market.runners.size() == 2) {
                                                                    String key = getKeyForAsian(name);
                                                                    if (name.contains("Over")) {
                                                                        bet = BetDataMapping.betsTotal.get(key + "M");
                                                                    } else if (name.contains("Under")) {
                                                                        bet = BetDataMapping.betsTotal.get(key + "L");
                                                                    }
                                                                } else if (market.name.equals("Correct Score")) {
                                                                    //bet = BetDataMapping.totalScore.get(name.replaceAll("-", ":").trim());
                                                                }
                                                                if (bet != null) {
                                                                    event.bets.put(bet, maxOdds.toString());
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
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("MatchBookParser exception - ", e);
        }

        return events;
    }

    private String getKeyForAsian(String name) {                                                                            String key = null;
        if (name.split("/").length == 2) {
            ///..25
            String[] values = name.split("/");
            values[0] = values[0].substring(values[0].indexOf("(") + 1);
            values[1] = values[1].substring(0, values[1].trim().length() - 1);
            if (values[0].contains("-")) {
                values[1] = "-" + values[1];
            }
            Double val1 = Double.parseDouble(values[0]);
            Double val2 = Double.parseDouble(values[1]);
            key = new BigDecimal((val1 + val2) / 2.0).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        } else {
            key = name.substring(name.lastIndexOf(" "));
        }
        return key;
    }
}
