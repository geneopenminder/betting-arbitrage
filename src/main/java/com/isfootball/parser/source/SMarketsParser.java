package com.isfootball.parser.source;

import com.google.gson.Gson;
import com.isfootball.model.BasicEvent;
import com.isfootball.parser.*;
import com.isfootball.pool.BasePoolInterface;
import com.isfootball.processing.EventSender;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by Evgeniy Pshenitsin on 28.03.2015.
 */
public class SMarketsParser extends BaseParser {

    //http://smarkets.s3.amazonaws.com/oddsfeed.xml

    private static final Logger logger = LogManager.getLogger("parser");

    private static Gson gson = new Gson();

    //static DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.GERMAN);
    //26 Apr 11:00
    static DateFormat format = new SimpleDateFormat("dd MMMyyyy", Locale.ENGLISH);
    //yyyy-MM-dd

    public SMarketsParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.SMARKETS;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        try {
            events = getMatches();

            events.parallelStream().forEach(event -> fillOdds(event));

        } catch (Exception e) {
            logger.error("SMarketsParser error", e);
        }
        return events.stream()
                .filter(e -> e != null)
                .filter(e -> e.bets.size() > 0)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static BasicEvent fillOdds(BasicEvent event) {
        try {
            Document match = Jsoup.connect(event.link)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                    .get();
            //check Live
            Element live = match.getElementById("masthead");
            //"span.start").first();
            String liveTxt = live.text();
            if (live != null && liveTxt.contains("Live Now!")) {
                event.bets.clear();
                return null;
            }
            Elements modules = match.select("div.module.market");
            for (Element module: modules) {
                String title = module.select("h3").first().select("a.single_market").text();
                if (title.equalsIgnoreCase("Winner")) {
                    Elements table = module.select("table.market_depth").first().select("tr");
                    if (table.size() == 4) {
                        String p1 = parseBet(table.get(1));
                        if (p1 != null) {
                            event.bets.put(BetType.P1, p1);
                        }
                        String pX = parseBet(table.get(2));
                        if (pX != null) {
                            event.bets.put(BetType.X, pX);
                        }
                        String p2 = parseBet(table.get(3));
                        if (p2 != null) {
                            event.bets.put(BetType.P2, p2);
                        }
                        String p1X = parseBetX(table.get(3));
                        if (p1X != null) {
                            event.bets.put(BetType.P1X, p1X);
                        }
                        String p12 = parseBetX(table.get(2));
                        if (p12 != null) {
                            event.bets.put(BetType.P12, p12);
                        }
                        String p2X = parseBetX(table.get(1));
                        if (p2X != null) {
                            event.bets.put(BetType.P2X, p2X);
                        }
                    }
                } else if (title.contains("Over/under") || title.contains("O/U")) {
                    Elements table = module.select("table.market_depth").first().select("tr");
                    if (table.size() == 3) {
                        BetPair bet = getTotalBet(table.get(1));
                        if (bet != null && bet.bet != null && bet.k != null) {
                            event.bets.put(bet.bet, bet.k);
                        }
                        bet = getTotalBet(table.get(2));
                        if (bet != null && bet.bet != null && bet.k != null) {
                            event.bets.put(bet.bet, bet.k);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("SMarketsParser fillOdds error", e);
        }
        EventSender.sendEvent(event);
        return null;
    }

    static class BetPair {
        public BetType bet;
        public String k;
    }

    private static BetPair getTotalBet(Element tr) {
        BetPair ret = new BetPair();
        try {
            String title = tr.select("th").first().text();
            if (title.contains("Under")) {
                title = title.replace("Under", "").replace("goals", "").trim() + "L";
            } else if (title.contains("Over")) {
                title = title.replace("Over", "").replace("goals", "").trim() + "M";
            }
            ret.bet = BetDataMapping.betsTotal.get(title);
            if (!tr.select("a.offer.best").first().hasClass("empty")) {
                ret.k = tr.select("a.offer.best").first().select("strong.price").text();
            } else {
                return null;
            }
            return ret;
        } catch (Exception e) {
        }
        return null;
    }

    private static String parseBet(Element tr) {
        try {
            if (!tr.select("a.offer.best").first().hasClass("empty")) {
                String betK = tr.select("a.offer.best").first().select("strong.price").text();
                return betK;
            }
        } catch (Exception e) {
        }
        return null;
    }

    private static String parseBetX(Element tr) {
        try {
            if (!tr.select("a.bid.best").first().hasClass("empty")) {
                Double betK = Double.parseDouble(tr.select("a.bid.best").first().select("strong.price").text());
                betK = betK / (betK - 1.0);
                return Double.toString(betK);
            }
        } catch (Exception e) {
        }
        return null;
    }

    private List<BasicEvent> getMatches() {
        //https://smarkets.com/_navigation?slug=football
        Set<String> matches = new HashSet<String>();
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        //GetMethod get = new GetMethod("https://smarkets.com/_navigation");
        //HttpClient httpClient = new HttpClient(HttpClientInitializer.getConnectionManager());

        //get.setQueryString(new NameValuePair[]{
        //        new NameValuePair("slug", "football"),
        //});
        try {

            HttpGet httpget = new HttpGet("https://smarkets.com/_navigation?slug=football");
            DefaultHttpClient httpclient = new DefaultHttpClient();

            HttpResponse response = httpclient.execute(httpget);

            //int returnCode = httpClient.executeMethod(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                String ret = EntityUtils.toString(response.getEntity(), "UTF-8");
                //String ret = IOUtils.toString(new InputStreamReader(get.getResponseBodyAsStream(), "UTF-8"));
                NavMenu navMenu = gson.fromJson(ret, NavMenu.class);
                for (Football f: navMenu.navigation_data.football) {
                    httpget = new HttpGet("https://smarkets.com/_navigation?slug=" + f.child);
                    response = httpclient.execute(httpget);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        ret = EntityUtils.toString(response.getEntity(), "UTF-8");
                        ret = ret.replaceFirst(f.child, "football");
                        NavMenu navMenuLeague = gson.fromJson(ret, NavMenu.class);
                        for (Football fLeague: navMenuLeague.navigation_data.football) {
                            if (fLeague.child == null) {
                                continue;
                            }
                            httpget = new HttpGet("https://smarkets.com/_navigation?slug=" + fLeague.child);
                            response = httpclient.execute(httpget);
                            if (response.getStatusLine().getStatusCode() == 200) {
                                ret = EntityUtils.toString(response.getEntity(), "UTF-8");
                                ret = ret.replaceFirst(fLeague.child, "football");
                                Matches navMenuChild = gson.fromJson(ret, Matches.class);
                                if (navMenuChild.navigation_data.football != null && !navMenuChild.navigation_data.football.isEmpty()
                                        && navMenuChild.navigation_data.football != null) {
                                    for (Children match : navMenuChild.navigation_data.football) {
                                        if (!matches.contains(match.url)) {
                                            matches.add(match.url);
                                            BasicEvent event = new BasicEvent();
                                            String[] teams = match.title.split("vs.");
                                            event.team1 = Teams.getTeam(teams[0].trim());
                                            event.team2 = Teams.getTeam(teams[1].trim());
                                            if (event.team1 != Teams.TEAM_UNKNOWN && event.team2 != Teams.TEAM_UNKNOWN) {
                                                matches.add(match.url);
                                                event.site = BetSite.SMARKETS;
                                                event.date = match.date;
                                                //event.day = format.parse(match.date_iso.substring(0, 10));
                                                //event.day = new Date(match.timestamp);
                                                event.day = format.parse(match.date.substring(0, 6) + Calendar.getInstance().get(Calendar.YEAR));
                                                event.link = "https://smarkets.com" + match.url;
                                                events.add(event);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        logger.error("getMatches error get league");
                    }
                }
            } else {
                logger.error("getMatches error get menu");
            }
        } catch (Exception e) {
            logger.error("getMatches error", e);
        }
        return events;
    }

    static class NavMenu {
        public NavigationData navigation_data;
    }

    static class NavigationDataMatches {
        public List<Children> football;
    }

    static class Matches {
        public NavigationDataMatches navigation_data;
    }

    static class NavigationData {
        public List<Football> football;
    }

    static class Football {
        public String child; //link
        public int number;
        public String title;
        public String type;
        public String url;
        public List<Children> children;
        public String date;
        public long timestamp;
    }

    static class Children {
        public String date;
        public String date_iso;
        public long timestamp;
        public String title;
        public String url;
    }

}
