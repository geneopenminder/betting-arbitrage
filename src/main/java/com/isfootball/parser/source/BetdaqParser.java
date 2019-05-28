package com.isfootball.parser.source;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.openqa.selenium.WebDriver;
import org.xmappr.Element;
import org.xmappr.Elements;
import org.xmappr.RootElement;
import org.xmappr.Xmappr;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Evgeniy Pshenitsin on 12.05.2015.
 */
public class BetdaqParser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    //05-14-2015 17:45
    static DateFormat format = new SimpleDateFormat("MM-dd-yyyy hh:mm", Locale.ENGLISH);

    ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(10, getThreadFactory(BetdaqParser.class.getSimpleName()));


    private Pattern pattern;
    private Matcher matcher;
    private Pattern patternLive;

    private static final String TIME24HOURS_PATTERN =
            "([01]?[0-9]|2[0-3]):[0-5][0-9]";

    private static final String LIVE_PATTERN =
            "^\\([0-9]-[0-9]\\)";

    public BetdaqParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        pattern = Pattern.compile(TIME24HOURS_PATTERN);
        patternLive = Pattern.compile(LIVE_PATTERN);
        this.betSite = BetSite.BETDAQ;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        Set<String> leagues = new HashSet<String>();
        Set<String> matches = new HashSet<String>();
        try {
            //driver = getWebDriverFromPool();
            final String mainLink = "http://www.betdaq.com/UI/3.91//Common/MenuData.aspx?";
            //driver.get(mainLink);
            //Thread.sleep(1000);
            //String htmlSource = driver.getPageSource();


            GetMethod get = new GetMethod(mainLink);
            HttpClient httpClient = new HttpClient(HttpClientInitializer.getConnectionManager());
            get.setQueryString(new NameValuePair[]{
                    new NameValuePair("EventName", "Soccer"),
                    new NameValuePair("ParentId", "100003"),
                    new NameValuePair("currentPreferences", "3"),
                    new NameValuePair("multiples", "false"),
                    new NameValuePair("TimeZone", "-180"),
                    new NameValuePair("Language", "en"),
                    new NameValuePair("Currency", "GBP"),
            });
            try {
                int returnCode = httpClient.executeMethod(get);
                if (returnCode == HttpStatus.SC_OK) {
                    String ret = IOUtils.toString(new InputStreamReader(get.getResponseBodyAsStream(), "UTF-8"));
                    List<String> ids = getParents(ret);
                    for (String id: ids) {
                        get = new GetMethod(mainLink);
                        get.setQueryString(new NameValuePair[]{
                                new NameValuePair("EventName", "Soccer"),
                                new NameValuePair("ParentId", id),
                                new NameValuePair("currentPreferences", "3"),
                                new NameValuePair("multiples", "false"),
                                new NameValuePair("TimeZone", "-180"),
                                new NameValuePair("Language", "en"),
                                new NameValuePair("Currency", "GBP"),
                        });
                        returnCode = httpClient.executeMethod(get);
                        if (returnCode == HttpStatus.SC_OK) {
                            ret = IOUtils.toString(new InputStreamReader(get.getResponseBodyAsStream(), "UTF-8"));
                            leagues.addAll(getParents(ret));
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("BetdaqParser error", e);
            }
            for (String league: leagues) {
                get = new GetMethod(mainLink);
                get.setQueryString(new NameValuePair[]{
                        new NameValuePair("EventName", "Soccer"),
                        new NameValuePair("ParentId", league),
                        new NameValuePair("currentPreferences", "3"),
                        new NameValuePair("multiples", "false"),
                        new NameValuePair("TimeZone", "-180"),
                        new NameValuePair("Language", "en"),
                        new NameValuePair("Currency", "GBP"),
                });
                int returnCode = httpClient.executeMethod(get);
                if (returnCode == HttpStatus.SC_OK) {
                    String ret = IOUtils.toString(new InputStreamReader(get.getResponseBodyAsStream(), "UTF-8"));
                    matches.addAll(getParents(ret));
                }
            }

            events =
                    matches.parallelStream().map(match -> {
                        //http://www.betdaq.com/UI/3.91/MDC/PPCssMarkets.aspx?eId=2211212&
                        // ppStyles=BETDAQ&displayPL=false&singleClickBettingEnabled=false&singleClickBettingAmount=0&
                        // overlay=false&multiplesEnabled=true&oddsSystem=0&adjustTimeZone=false&timeZoneOffset=0&
                        // dateRange1=7000/12/31%2000:00:00&dateRange2=7000/12/31%2000:00:00&selInfo=true&RequestedTime=22:10:6&
                        // eventCardAutoReloaded=true&ClientDateTime=Tue%20May%2012%202015%2022:10:06%20GMT+0300&TimeZone=-180&Language=en&Currency=GBP
                        try {
                            final String matchLink = "http://www.betdaq.com/UI/3.91/MDC/PPCssMarkets.aspx";
                            GetMethod getInt = new GetMethod(matchLink);
                            getInt.setQueryString(new NameValuePair[]{
                                    new NameValuePair("displayPL", "false"),
                                    new NameValuePair("singleClickBettingEnabled", "false"),
                                    new NameValuePair("singleClickBettingAmount", "0"),
                                    new NameValuePair("overlay", "false"),
                                    new NameValuePair("multiplesEnabled", "true"),
                                    new NameValuePair("oddsSystem", "0"),
                                    new NameValuePair("adjustTimeZone", "false"),
                                    new NameValuePair("timeZoneOffset", "0"),
                                    new NameValuePair("dateRange1", "7000/12/31%2000:00:00"),
                                    new NameValuePair("dateRange2", "7000/12/31%2000:00:00"),
                                    new NameValuePair("selInfo", "true"),
                                    new NameValuePair("RequestedTime", "22:18:6"),
                                    new NameValuePair("eventCardAutoReloaded", "true"),
                                    new NameValuePair("TimeZone", "-180"),
                                    new NameValuePair("Language", "en"),
                                    new NameValuePair("Currency", "GBP"),


                                    new NameValuePair("eId", match),
                                    new NameValuePair("marketType", "AllMarkets"),
                                    new NameValuePair("betMarketId", "undefined"),
                                    new NameValuePair("sId", "undefined"),
                                    new NameValuePair("type", "undefined"),
                                    new NameValuePair("odds", "undefined"),
                                    new NameValuePair("rightNavTab", "undefined"),
                                    new NameValuePair("pId", ""),
                                    new NameValuePair("bId", "1"),
                                    new NameValuePair("expressBet", "false"),
                                    new NameValuePair("timeFeed", "" + System.currentTimeMillis()),
                                    new NameValuePair("currentPreferences", "3"),
                                    new NameValuePair("rootPath", "/UI/3.91/"),
                                    new NameValuePair("langCode", "en"),
                                    new NameValuePair("ccyCode", "GBP"),
                                    new NameValuePair("ccySymbol", "%C2%A3"),
                                    new NameValuePair("minOffer", "5"),
                                    new NameValuePair("ppPath", "/UI/3.91/MDC"),
                                    new NameValuePair("ppHelp", "/GBE.Help/BrokerDirectory/BETDAQ/en/"),
                                    new NameValuePair("ppStyles", "BETDAQ"),

                            });
                            int returnCode = httpClient.executeMethod(getInt);
                            if (returnCode == HttpStatus.SC_OK) {
                                String ret = IOUtils.toString(new InputStreamReader(getInt.getResponseBodyAsStream(), "UTF-8"));
                                return parseEvent(ret);
                            }

                        } catch (Exception e) {
                            logger.error("BetdaqParser error", e);
                        }
                        return null;
                    })
                            .filter(event -> event != null)
                            .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("BetdaqParser exception - ", e);
        }
        return events;
    }

    private BasicEvent parseEvent(String json) {
        try {
            BasicEvent event = new BasicEvent();
            GsonBuilder gsonBuilder = new GsonBuilder();
            //gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
            gsonBuilder.registerTypeAdapter(SO[].class, new SODeserializer());
            //gsonBuilder.registerTypeAdapter(Market[].class, new MarketDeserializer());

            //Gson gson = new GsonBuilder().registerTypeAdapterFactory(new ArrayAdapterFactory()).create();

            Gson gson = gsonBuilder.create();
            EventJson data = new EventJson();
            try {
                data = gson.fromJson(json, EventJson.class);
            } catch (JsonSyntaxException e) {
                EventJsonSMKT dataS = gson.fromJson(json, EventJsonSMKT.class);
                if (dataS != null) {
                    data.PPCssData = new PPCssData();
                    data.PPCssData.ArrayOfEventClassifier = new ArrayOfEventClassifier();
                    data.PPCssData.ArrayOfEventClassifier.EventClassifier = new EventClassifierJSON();
                    data.PPCssData.ArrayOfEventClassifier.EventClassifier.eCN = dataS.PPCssData.ArrayOfEventClassifier.EventClassifier.eCN;
                    data.PPCssData.ArrayOfEventClassifier.EventClassifier.eST = dataS.PPCssData.ArrayOfEventClassifier.EventClassifier.eST;
                    data.PPCssData.ArrayOfEventClassifier.EventClassifier.mkt = new Market[1];
                    data.PPCssData.ArrayOfEventClassifier.EventClassifier.mkt[0] = dataS.PPCssData.ArrayOfEventClassifier.EventClassifier.mkt;
                }
            }

            if (data != null) {
                if (data.PPCssData.ArrayOfEventClassifier.EventClassifier.eCN.contains("(Live)")) {
                    logger.info("BetdaqParser live: " + data.PPCssData.ArrayOfEventClassifier.EventClassifier.eCN);
                    return null;
                }
                if (data.PPCssData.ArrayOfEventClassifier.EventClassifier.eCN == null) {
                    logger.error("BetdaqParser teams null");
                    return null;
                }
                String teamsData = data.PPCssData.ArrayOfEventClassifier.EventClassifier.eCN;
                if (teamsData.startsWith("(")) {
                    teamsData = teamsData.substring(teamsData.indexOf(")") + 1);
                }
                String time = teamsData.substring(0, 5);
                if (pattern.matcher(time).matches()) {
                    teamsData = teamsData.substring(5).trim();
                }
                String[] teams = teamsData.split(" v ");
                int team1Add = teams[0].indexOf("(");
                int team2Add = teams[1].indexOf("(");
                event.team1 = Teams.getTeam(teams[0].substring(0, team1Add == -1 ? teams[0].trim().length() : team1Add).trim());
                event.team2 = Teams.getTeam(teams[1].substring(0, team2Add == -1 ? teams[1].trim().length() : team2Add).trim());
                if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                    logger.warn("BetdaqParser - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                    //System.out.println("BetdaqParser - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                    return null;
                }
                event.site = BetSite.BETDAQ;
                Matcher m = patternLive.matcher(data.PPCssData.ArrayOfEventClassifier.EventClassifier.eCN);
                if (m.find()) {
                    logger.info("BetdaqParser live: " + data.PPCssData.ArrayOfEventClassifier.EventClassifier.eCN);
                    return null;
                }
                event.link = "www.betdaq.com";
                if (data.PPCssData.ArrayOfEventClassifier.EventClassifier.eST == null) {
                    logger.error("BetdaqParser date null");
                    return null;
                }
                event.date = data.PPCssData.ArrayOfEventClassifier.EventClassifier.eST;
                event.day = format.parse(event.date.trim());

                DateTime now = new DateTime(DateTimeZone.forOffsetHours(+1));
                now = now.minusHours(2);
                DateTime eventDate = new DateTime(event.day);
                if (eventDate.isBefore(now)) {
                    logger.info("BetdaqParser live: " + data.PPCssData.ArrayOfEventClassifier.EventClassifier.eCN);
                    return null;
                }
                for (Market market: data.PPCssData.ArrayOfEventClassifier.EventClassifier.getMarkets()) {
                    if (market.mn.equalsIgnoreCase("Match Odds")) {
                        if (market.sel.size() == 3) {
                            if (!market.sel.get(0).getFSO().isEmpty()) {
                                if (market.sel.get(0).sN != null && teams[0].contains(market.sel.get(0).sN)) {
                                    String p1 = market.sel.get(0).getFSO().get(0).p;
                                    event.bets.put(BetType.P1, p1);
                                    event.bets.put(BetType.P2X, Double.toString(market.sel.get(0).getASO().get(0).dP / (market.sel.get(0).getASO().get(0).dP - 1.0)));
                                } else {
                                    String p2 = market.sel.get(0).getFSO().get(0).p;
                                    event.bets.put(BetType.P2, p2);
                                    event.bets.put(BetType.P1X, Double.toString(market.sel.get(0).getASO().get(0).dP / (market.sel.get(0).getASO().get(0).dP - 1.0)));
                                }
                            }
                            if (!market.sel.get(1).getFSO().isEmpty()) {
                                String x = market.sel.get(1).getFSO().get(0).p;
                                event.bets.put(BetType.X, x);
                                event.bets.put(BetType.P12, Double.toString(market.sel.get(1).getASO().get(0).dP / (market.sel.get(1).getASO().get(0).dP - 1.0)));
                            }
                            if (!market.sel.get(2).getFSO().isEmpty()) {
                                if (market.sel.get(2).sN != null && teams[1].contains(market.sel.get(2).sN)) {
                                    String p2 = market.sel.get(2).getFSO().get(0).p;
                                    event.bets.put(BetType.P2, p2);
                                    event.bets.put(BetType.P1X, Double.toString(market.sel.get(2).getASO().get(0).dP / (market.sel.get(2).getASO().get(0).dP - 1.0)));
                                } else {
                                    String p1 = market.sel.get(2).getFSO().get(0).p;
                                    event.bets.put(BetType.P1, p1);
                                    event.bets.put(BetType.P2X, Double.toString(market.sel.get(2).getASO().get(0).dP / (market.sel.get(2).getASO().get(0).dP - 1.0)));
                                }
                            }
                        }
                    } else if (market.mn.startsWith("Asian Handicap")) {
                        if (market.sel.size() == 2) {
                            String type = getOverUnder(market.mn.substring(market.mn.indexOf("(") + 1, market.mn.indexOf(")")));
                            BetType f1 = BetDataMapping.betsTotal.get(type + "F1");
                            if (type.contains("-")) {
                                type = type.replace("-", "");
                            } else {
                                type = "-" + type;
                            }
                            BetType f2 = BetDataMapping.betsTotal.get(type.replace("+","") + "F2");
                            if (f1 != null && !market.sel.get(0).getFSO().isEmpty()) {
                                event.bets.put(f1, market.sel.get(0).getFSO().get(0).p);
                            }
                            if (f2 != null && !market.sel.get(1).getFSO().isEmpty()) {
                                event.bets.put(f2, market.sel.get(1).getFSO().get(0).p);
                            }
                        }
                    } else if (market.mn.startsWith("Under/Over")) {
                        if (market.sel.size() == 2) {
                            String type = getOverUnder(market.mn.substring(market.mn.indexOf("(") + 1, market.mn.indexOf(")")));
                            BetType underBet = BetDataMapping.betsTotal.get(type + "L");
                            BetType overBet = BetDataMapping.betsTotal.get(type + "M");
                            //Under/Over - Goals (2.5)
                            if (underBet != null && !market.sel.get(0).getFSO().isEmpty()) {
                                event.bets.put(underBet, market.sel.get(0).getFSO().get(0).p);
                            }
                            if (overBet != null && !market.sel.get(1).getFSO().isEmpty()) {
                                event.bets.put(overBet, market.sel.get(1).getFSO().get(0).p);
                            }
                        }
                    }
                }
            }
            EventSender.sendEvent(event);
            return event;
        } catch (Exception e) {
            logger.error("BetdaqParser parseEvent error", e);
        }
        return null;
    }

    private String getOverUnder(String type) {
        //+0/0.5
        String sign = "";
        if (type.contains("-")) {
            sign = "-";
        }
        type = type.replace("+", "");
        type = type.replace("-", "");
        String[] vals = type.split("/");
        if (vals.length != 2) {
            return sign + type.trim();
        }
        try {
            Double res = (Double.parseDouble(vals[0]) + Double.parseDouble(vals[1])) / 2.0;
            return sign + res.toString();
        } catch (Exception e) {
            logger.error("BetdaqParser asianTotalType exception - ", e);
        }
        return "UNK";
    }

    private List<String> getParents(String xml) {
        List<String> ids = new ArrayList<String>();
        Xmappr xm = new Xmappr(Root.class);
        Root parent = (Root) xm.fromXML(new StringReader(xml));
        if (parent.eventClassifiers != null) {
            for (EventClassifier clf : parent.eventClassifiers) {
                ids.add(clf.Id);
            }
        }
        return ids;
    }

    @RootElement(name="Root")
    static public class Root {

        @Elements({
                @Element(name="EventClassifier", targetType=EventClassifier.class)
        })
        public List<EventClassifier> eventClassifiers;
    }

    static public class EventClassifier {

        @Element
        public String Id;

    }

    //JSON

    static class ArrayAdapter<T> extends TypeAdapter<List<T>> {
        private Class<T> adapterclass;

        public ArrayAdapter(Class<T> adapterclass) {
            this.adapterclass = adapterclass;
        }

        public List<T> read(JsonReader reader) throws IOException {

            List<T> list = new ArrayList<T>();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(new ArrayAdapterFactory())
                    .create();

            if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                T inning = gson.fromJson(reader, adapterclass);
                list.add(inning);

            } else if (reader.peek() == JsonToken.BEGIN_ARRAY) {

                reader.beginArray();
                while (reader.hasNext()) {
                    T inning = gson.fromJson(reader, adapterclass);
                    list.add(inning);
                }
                reader.endArray();

            }

            return list;
        }

        public void write(JsonWriter writer, List<T> value) throws IOException {

        }

    };

    static class ArrayAdapterFactory implements TypeAdapterFactory {

        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {

            TypeAdapter<T> typeAdapter = null;

            try {
                if (type.getRawType() == List.class)
                    typeAdapter = new ArrayAdapter(
                            (Class) ((ParameterizedType) type.getType())
                                    .getActualTypeArguments()[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return typeAdapter;


        }

    };

    static class EventJsonSMKT {
        public PPCssDataSMKT PPCssData;
    };

    static class PPCssDataSMKT {
        public ArrayOfEventClassifierSMKT ArrayOfEventClassifier;
    };

    static class ArrayOfEventClassifierSMKT {
        public EventClassifierJSONSMKT EventClassifier;
    };

    static class EventClassifierJSONSMKT {
        public String eCN;
        public String eST;

        @SerializedName("mkt")
        public Market mkt;

        public List<Market> getMarkets() {
            if (mkt == null) {
                return new ArrayList<Market>();
            }
            return new LinkedList<Market>( Arrays.asList(mkt));
        }

    };



    static class EventJson {
        public PPCssData PPCssData;
    };

    static class PPCssData {
        public ArrayOfEventClassifier ArrayOfEventClassifier;
    };

    static class ArrayOfEventClassifier {
        public EventClassifierJSON EventClassifier;
    };

    static class EventClassifierJSON {
        public String eCN;
        public String eST;

        @SerializedName("mkt")
        public Market[] mkt;

        public List<Market> getMarkets() {
            if (mkt == null) {
                return new ArrayList<Market>();
            }
            return new LinkedList<Market>( Arrays.asList(mkt));
        }

    };

    static class Market {
        public List<Selection> sel;
        public String mn;
        public Double pF;
        public int mT;
        public int nOW;
    };

    static class Selection {
        @SerializedName("aSO")
        public SO[] aSO;

        @SerializedName("fSO")
        public SO[] fSO;

        public String sN;

        public List<SO> getASO() {
            if (aSO == null) {
                return new ArrayList<SO>();
            }
            return new LinkedList<SO>( Arrays.asList(aSO));
        }

        public List<SO> getFSO() {
            if (fSO == null) {
                return new ArrayList<SO>();
            }
            return new LinkedList<SO>( Arrays.asList(fSO));
        }

        public int mId;
    };

    public static class SODeserializer implements JsonDeserializer<SO[]> {

        @Override
        public SO[] deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonArray) {
                return new Gson().fromJson(json, SO[].class);
            }
            SO child = context.deserialize(json, SO.class);
            return new SO[] { child };
        }
    }

    public static class MarketDeserializer implements JsonDeserializer<Market[]> {

        @Override
        public Market[] deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonArray) {
                return new Gson().fromJson(json, Market[].class);
            }
            Market child = context.deserialize(json, Market.class);
            return new Market[] { child };
        }
    }

    static class SO {
        public Double dP;
        public String p;
        public Double rA;
    };

}