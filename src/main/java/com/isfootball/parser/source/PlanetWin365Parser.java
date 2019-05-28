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
 * Created by Evgeniy Pshenitsin on 20.01.2015.
 */
public class PlanetWin365Parser extends BaseParser {

    //http://planetwin365.com

    //31 January 2015

    static Map<String, String> months = new HashMap<String, String>() {{
        put("January", "01");
        put("February", "02");
        put("March", "03");
        put("April", "04");
        put("May", "05");
        put("June", "06");
        put("July", "07");
        put("August", "08");
        put("September", "09");
        put("October", "10");
        put("November", "11");
        put("December", "12");
    }};

    public static String replaceMonth(String date) {
        return date.replace("January", "01")
                .replace("February", "02")
                .replace("March", "03")
                .replace("April", "04")
                .replace("May", "05")
                .replace("June", "06")
                .replace("July", "07")
                .replace("August", "08")
                .replace("September", "09")
                .replace("October", "10")
                .replace("November", "11")
                .replace("December", "12");
    }

    private static final Logger logger = LogManager.getLogger("parser");

    public PlanetWin365Parser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.PLANETWIN365;
    }

    @Override
    public List<BasicEvent> getEvents() {
        final List<BasicEvent> events = Collections.synchronizedList(new ArrayList<BasicEvent>());

        //System.setProperty("http.proxyHost", "141.8.193.211");
        //System.setProperty("http.proxyPort", "8888");
        try {
            /*Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("141.8.193.211", 8888));
            URL url = new URL("http://planetwin365.com/Sport/Groups.aspx?IDSport=342&Antepost=0");
            HttpURLConnection uc = (HttpURLConnection)url.openConnection(proxy);
            uc.connect();

            String page = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null){
                page = page + line;
            }
            uc.disconnect();*/

            Document doc = Jsoup.connect("http://planetwin365.com/Sport/Groups.aspx?IDSport=342&Antepost=0")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                    .cookie("ISBets_CurrentCulture", "2")
                    .timeout(10 * 1000).get();
            //Document doc = Jsoup.parse(page);
            Element menu = doc.getElementsByClass("groupsTblGroups").first();
            List<Element> spans = menu.select("span");
            List<String> leagueIds = new ArrayList<String>();

            for (Element span : spans) {
                if (!span.hasClass("checkboxGroups")) {
                    continue;
                }
                Element input = span.select("input").first();
                if (input == null) {
                    continue;
                }
                String eventHref = input.attr("value");
                Element href = span.select("a").first();
                if (!eventHref.isEmpty() && !href.text().contains("U21 Premier League")
                        && !href.text().contains("Women")) {
                    leagueIds.add(eventHref);
                } else {
                    final String name = href.text();
                    logger.warn("PlanetWin365 - skip league" + name);
                }
            }
            leagueIds.parallelStream()
                    .forEach(league -> {
                        try {
                            final String leagueLink = "http://planetwin365.com/ControlsSkin/OddsEvent.aspx?ShowLinkFastBet=0&showDate=1&showGQ=1&EventID=" +
                                    league + "&GroupSep=undefined";

                            Document docLeague = Jsoup.connect(leagueLink)
                                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                                    .cookie("ISBets_CurrentCulture", "2")
                                    .timeout(10 * 1000).get();
                            Element div = docLeague.getElementsByClass("divQt").first();
                            String leagueName = docLeague.select("div.sezQuota").text();
                            if (leagueName.contains("U21") ||
                                    leagueName.contains("U20") ||
                                    leagueName.contains("U19") ||
                                    leagueName.contains("Women")) {
                                //bad league
                            } else {
                                String date = null;
                                int dateCounter = 0;
                                Iterator<Element> tables = div.select("table").iterator();
                                while (tables.hasNext()) {
                                    Element table = tables.next();
                                    String t = table.html();
                                    if (date == null) {
                                        //first table
                                        date = table.getElementsByClass("cqDateTbl").text();
                                    } else {
                                        date = div.getElementsByClass("DateOdds").get(dateCounter).text();
                                        dateCounter++;
                                    }
                                    tables.next();
                                    tables.next();
                                    for (Element itemA : table.getElementsByClass("dgAItem")) {
                                        String teamsAStr = itemA.getElementsByClass("nmInc").first().select("span").text();
                                        String idA = itemA.getElementsByClass("OddsDetailsClassiQuota").get(1).select("a").attr("onclick");
                                        BasicEvent eventA = new BasicEvent();
                                        eventA.site = BetSite.PLANETWIN365;
                                        eventA.link = "planetwin365.com";
                                        eventA.date = date;
                                        eventA.day = DateFormatter.format(replaceMonth(eventA.date), betSite);
                                        String[] teamsA = teamsAStr.split(" - ");
                                        eventA.team1 = Teams.getTeam(teamsA[0].trim());
                                        eventA.team2 = Teams.getTeam(teamsA[1].trim());
                                        if (eventA.team1 != Teams.TEAM_UNKNOWN && eventA.team2 != Teams.TEAM_UNKNOWN) {
                                            Elements oddsMain = itemA.getElementsByClass("OddsDetailsQuote").first().getElementsByClass("oddsQ");
                                            //P
                                            String P1 = oddsMain.get(0).select("a").text().trim();
                                            eventA.bets.put(BetType.P1, P1);
                                            String X = oddsMain.get(1).select("a").text().trim();
                                            eventA.bets.put(BetType.X, X);
                                            String P2 = oddsMain.get(2).select("a").text().trim();
                                            eventA.bets.put(BetType.P2, P2);
                                            //PX
                                            String P1X = oddsMain.get(3).select("a").text().trim();
                                            eventA.bets.put(BetType.P1X, P1X);
                                            String P12 = oddsMain.get(4).select("a").text().trim();
                                            eventA.bets.put(BetType.P12, P12);
                                            String P2X = oddsMain.get(5).select("a").text().trim();
                                            eventA.bets.put(BetType.P2X, P2X);

                                            fillBets(eventA, idA);
                                            events.add(eventA);
                                            EventSender.sendEvent(eventA);
                                        } else {
                                            logger.warn("PlanetWin365 - unknown teams: " + teamsA[0] + ":" + eventA.team1 + ";" + teamsA[1] + ":" + eventA.team2);
                                        }
                                        tables.next();
                                    }

                                    for (Element item : table.getElementsByClass("dgItem")) {
                                        String teamsStr = item.getElementsByClass("nmInc").first().select("span").text();
                                        String id = item.getElementsByClass("OddsDetailsClassiQuota").get(1).select("a").attr("onclick");
                                        BasicEvent event = new BasicEvent();
                                        event.site = BetSite.PLANETWIN365;
                                        event.link = "planetwin365.com";
                                        event.date = date;
                                        event.day = DateFormatter.format(replaceMonth(event.date), betSite);
                                        String[] teams = teamsStr.split(" - ");
                                        event.team1 = Teams.getTeam(teams[0].trim());
                                        event.team2 = Teams.getTeam(teams[1].trim());
                                        if (event.team1 != Teams.TEAM_UNKNOWN && event.team2 != Teams.TEAM_UNKNOWN) {
                                            Elements oddsMain = item.getElementsByClass("OddsDetailsQuote").first().getElementsByClass("oddsQ");
                                            //P
                                            String P1 = oddsMain.get(0).select("a").text().trim();
                                            event.bets.put(BetType.P1, P1);
                                            String X = oddsMain.get(1).select("a").text().trim();
                                            event.bets.put(BetType.X, X);
                                            String P2 = oddsMain.get(2).select("a").text().trim();
                                            event.bets.put(BetType.P2, P2);
                                            //PX
                                            String P1X = oddsMain.get(3).select("a").text().trim();
                                            event.bets.put(BetType.P1X, P1X);
                                            String P12 = oddsMain.get(4).select("a").text().trim();
                                            event.bets.put(BetType.P12, P12);
                                            String P2X = oddsMain.get(5).select("a").text().trim();
                                            event.bets.put(BetType.P2X, P2X);
                                            fillBets(event, id);
                                            events.add(event);
                                            EventSender.sendEvent(event);
                                        } else {
                                            logger.warn("PlanetWin365 - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                                        }
                                        tables.next();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            logger.error("PlanetWin365 exception - ", e);
                        }
                    });
        } catch (Exception e) {
            logger.error("PlanetWin365 exception - ", e);
        }
        return events;
    }


    static void fillBets(BasicEvent event, String idStr) {
        final int index1 = idStr.indexOf("(");
        final int index2 = idStr.indexOf(",");
        final int index3 = idStr.indexOf(",", index2 + 1);
        try {
            String eventId = idStr.substring(index1 + 1, index2);
            String group = idStr.substring(index2 + 1, index3);
            Document bets = Jsoup.connect("http://planetwin365.com/ControlsSkin/OddsSubEvent.aspx?SubEventID=" + eventId
                    + "&IDGruppoQuota=" + group + "&btnID=s_w_PC_cCoupon_btnAddCoupon&txtID=s_w_PC_cCoupon_txtIDQuota")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                    .cookie("ISBets_CurrentCulture", "2")
                    .timeout(10 * 1000).get();


            Element e = bets.getElementsByClass("SETQCon").first();
            for (Element sed : e.getElementsByClass("SEItem")) {
                String section = sed.getElementsByClass("SECQ").text();
                for (Element dds : sed.getElementsByClass("SEOdd")) {
                    if (section.equalsIgnoreCase("Correct Score")) {
                        /*String scoreTypeStr = dds.getElementsByClass("SEOddsTQ").first().text();
                        String scoreVal = dds.select("a").text();
                        Bet scoreBet = BetDataMapping.totalScore.get(scoreTypeStr);
                        if (scoreBet != null)
                            event.bets.put(scoreBet, scoreVal);
                            */
                    } else if (section.contains(" FT")) {
                        String totalTypeStr = dds.getElementsByClass("SEOddsTQ").first().text();
                        String totalVal = dds.select("a").text();
                        if (totalVal == null || totalVal.trim().isEmpty()) {
                            continue;
                        }
                        if (totalTypeStr.contains("Over")) {
                            String totalType = totalTypeStr.substring(5, 8);
                            BetType bet = BetDataMapping.betsTotal.get(totalType + "M");
                            event.bets.put(bet, totalVal);
                        } else if (totalTypeStr.contains("Under")) {
                            String totalType = totalTypeStr.substring(6, 9);
                            BetType bet = BetDataMapping.betsTotal.get(totalType + "L");
                            event.bets.put(bet, totalVal);
                        } else if (totalTypeStr.contains("Ov")) {
                            String totalType = totalTypeStr.substring(3, 7);
                            BetType bet = BetDataMapping.betsTotal.get(totalType + "M");
                            event.bets.put(bet, totalVal);
                        } else if (totalTypeStr.contains("Un")) {
                            String totalType = totalTypeStr.substring(3, 7);
                            BetType bet = BetDataMapping.betsTotal.get(totalType + "L");
                            event.bets.put(bet, totalVal);
                        }
                    } else if (section.contains(" HND")) {
                        String totalTypeStr = dds.getElementsByClass("SEOddsTQ").first().text();
                        String totalVal = dds.select("a").text();
                        if (!totalVal.isEmpty()) {
                            if (totalTypeStr.contains("1AH")) {
                                String totalType = totalTypeStr.substring(4).replaceAll("\\)", "").replace("+", "");
                                BetType bet = BetDataMapping.betsTotal.get(totalType + "F1");
                                event.bets.put(bet, totalVal);
                            } else if (totalTypeStr.contains("2AH")) {
                                String totalType = totalTypeStr.substring(4);
                                if (totalType.contains("+")) {
                                    totalType = totalType.replace("+", "-").replaceAll("\\)", "");
                                } else {
                                    totalType = totalType.replace("-", "").replaceAll("\\)", "");
                                }
                                BetType bet = BetDataMapping.betsTotal.get(totalType + "F2");
                                event.bets.put(bet, totalVal);
                            }
                        }
                    } else if (section.contains("Handicap")) {
                        String totalTypeStr = dds.getElementsByClass("SEOddsTQ").first().text();
                        String totalVal = dds.select("a").text();
                        if (!totalVal.isEmpty()) {
                            //logger.error(totalTypeStr);
                            Integer gType = getTotalTypeForGandicap(totalTypeStr.substring(totalTypeStr.indexOf("(") + 1, totalTypeStr.indexOf(")")));
                            if (gType == null) {
                                continue;
                            }
                            BetType bet = null;
                            if (totalTypeStr.contains("1H")) {
                                bet = BetDataMapping.gandicaps.get("1H" + Integer.toString(gType));
                            } else if (totalTypeStr.contains("2H")) {
                                bet = BetDataMapping.gandicaps.get("2H" + Integer.toString(0 - gType));
                            } else if (totalTypeStr.contains("XH")) {
                                bet = BetDataMapping.gandicaps.get("XH" + Integer.toString(gType));
                            }
                            if (bet != null) {
                                event.bets.put(bet, totalVal);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("PlanetWin365 exception - ", e);
        }
    }

    private static Integer getTotalTypeForGandicap(String hnd) {
        try {
            String[] vals = hnd.split(":");
            if (vals.length == 2) {
                Integer result = Integer.parseInt(vals[0]) - Integer.parseInt(vals[1]);
                return result;
            }
        } catch (Exception e) {
            logger.error("PlanetWin365 getTotalTypeForGandicap err - " + hnd, e);
        }
        return null;
    }
}
