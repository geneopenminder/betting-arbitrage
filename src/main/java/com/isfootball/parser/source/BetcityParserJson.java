package com.isfootball.parser.source;

import com.google.gson.Gson;
import com.isfootball.model.BasicEvent;
import com.isfootball.parser.*;
import com.isfootball.pool.BasePoolInterface;
import com.isfootball.processing.EventSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Evgeniy Pshenitsin on 14.04.2015.
 */
public class BetcityParserJson extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(50, getThreadFactory(BetcityParserJson.class.getSimpleName()));

    public BetcityParserJson(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.BETCITY;
    }

    private static class BetcityDownloader extends BaseDownloader {

        BetcityParserJson parser;
        List<String> leagues;

        public BetcityDownloader(BetcityParserJson parser, List<String> leagues) {
            this.parser = parser;
            this.leagues = leagues;
        }

        @Override
        public List<BasicEvent> download() {
            List<BasicEvent> events = new ArrayList<BasicEvent>();
            WebDriver driver = null;
            try {
                driver = parser.getWebDriverFromPool();
                //driver.manage().addCookie(new Cookie("clng", "1"));
                //https://www.betsbc.com/bets/bets.php?line_id[]=1 - old
                //http://betcity.by/new/#/line/line_ids=a:1
                //https://betsbc.com/app/#/line/?line_id%5B%5D=1
                driver.get("http://betcity.by/new/#/line/line_ids=a:1");
                Thread.sleep(3000);
                String pSource = driver.getPageSource();
                List<WebElement> checkboxes = driver.findElements(By.xpath("//input[@ng-click='changeOne']"));
                for (WebElement checkbox: checkboxes) {
                    final String value = checkbox.getAttribute("id");
                    for (String leagueId: leagues) {
                        if (value.equalsIgnoreCase(leagueId)) {
                            checkbox.click();
                        }
                    }
                }
                driver.findElement(By.id("btn_submit")).click(); //Show
                Thread.sleep(3000);
                pSource = driver.getPageSource();
                Document doc  = Jsoup.parse(driver.getPageSource());
                Elements tables = doc.select("table");

                List<Element> mainTables = new ArrayList<>();

                for (Element table: tables) {
                    try {
                        Attributes atr = table.attributes();
                        final boolean isHeader = atr.asList().stream().anyMatch((Attribute a) -> {
                            if (a.getKey().equals("id") && a.getValue().startsWith("c")) {
                                return true;
                            }
                            return false;
                        });

                        if (isHeader) {
                            String headTitle = table.select("tr").first().select("td").text();
                            if (headTitle.contains("Женщины") ||
                                    headTitle.contains("21-го года") ||
                                    headTitle.contains("17 лет") ||
                                    headTitle.contains("18 лет") ||
                                    headTitle.contains("19 лет") ||
                                    headTitle.contains("20 лет") ||
                                    headTitle.contains("Статистика") ||
                                    headTitle.contains("Итоговый") ||
                                    headTitle.contains("больше") ||
                                    headTitle.contains("голов") ||
                                    headTitle.contains("Кто") ||
                                    headTitle.contains("молодёжных") ||
                                    headTitle.contains("УЕФА")
                                    || headTitle.contains("Лига Европы")) {
                                continue;
                            }
                            mainTables.add(table);
                        }

                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }



                for (Element table: mainTables) {
                    try {

                        Element mainBody = table.select("tbody").stream().filter(element -> {
                            final boolean isHeader = element.attributes().asList().stream().anyMatch((Attribute a) -> {
                                if (a.getKey().equals("id") && a.getValue().startsWith("event-repeat")) {
                                    return true;
                                }
                                return false;
                            });
                            return isHeader;
                        }).findFirst().get();

                        if (mainBody == null) {
                            continue;
                        }
                        String date = "";
                        for (Element trLine : mainBody.select("tr")) {
                            if (trLine.hasAttr("data-reactid") && trLine.select("td").size() == 1) {
                                date = trLine.text().isEmpty() ? date: trLine.text();
                            } else if (trLine.hasAttr("id") && trLine.attr("id").startsWith("main_out_")) {
                                Elements td = trLine.select("tr").first().select("td");
                                if (td.size() == 18) {
                                    BasicEvent event = new BasicEvent();
                                    String team1 = td.get(1).text().trim();
                                    String team2 = td.get(2).text().trim();
                                    event.team1 = Teams.getTeam(team1.trim());
                                    event.team2 = Teams.getTeam(team2.trim());
                                    if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                                        logger.warn("BetcityParser - unknown teams: " + team1 + ":" + event.team1 + ";" + team2 + ":" + event.team2);
                                        continue;
                                    }
                                    event.date = date;
                                    event.day = DateFormatter.format(date, BetSite.BETCITY);
                                    if (event.day == null) {continue;}
                                    event.site = BetSite.BETCITY;
                                    event.link = "betsbc.com";
                                    String p1 = td.get(3).text().trim();
                                    if (p1 != null && !p1.isEmpty()) {
                                        event.bets.put(BetType.P1, p1);
                                    }
                                    String pX = td.get(4).text().trim();
                                    if (pX != null && !pX.isEmpty()) {
                                        event.bets.put(BetType.X, pX);
                                    }
                                    String p2 = td.get(5).text().trim();
                                    if (p2 != null && !p2.isEmpty()) {
                                        event.bets.put(BetType.P2, p2);
                                    }
                                    String p1X = td.get(6).text().trim();
                                    if (p1X != null && !p1X.isEmpty()) {
                                        event.bets.put(BetType.P1X, p1X);
                                    }
                                    String p12 = td.get(7).text().trim();
                                    if (p12 != null && !p12.isEmpty()) {
                                        event.bets.put(BetType.P12, p12);
                                    }
                                    String p2X = td.get(8).text().trim();
                                    if (p2X != null && !p2X.isEmpty()) {
                                        event.bets.put(BetType.P2X, p2X);
                                    }

                                    BetType fora1Bet = BetDataMapping.betsTotal.get(td.get(9).text().replace("+", "").trim() + "F1");
                                    String fora1Val = td.get(10).text().trim();
                                    if (fora1Bet != null && !fora1Val.isEmpty()) {
                                        event.bets.put(fora1Bet, fora1Val);
                                    }
                                    BetType fora2Bet = BetDataMapping.betsTotal.get(td.get(11).text().replace("+", "").trim() + "F2");
                                    String fora2Val = td.get(12).text().trim();
                                    if (fora2Bet != null && !fora2Val.isEmpty()) {
                                        event.bets.put(fora2Bet, fora2Val);
                                    }
                                    //13
                                    String totalVal = td.get(13).text().trim();
                                    BetType totalL = BetDataMapping.betsTotal.get(totalVal + "L");
                                    BetType totalM = BetDataMapping.betsTotal.get(totalVal + "M");
                                    String totalLVal = td.get(14).text().trim();
                                    String totalMVal = td.get(15).text().trim();
                                    if (totalL != null && !totalLVal.isEmpty()) {
                                        event.bets.put(totalL, totalLVal);
                                    }
                                    if (totalM != null && !totalMVal.isEmpty()) {
                                        event.bets.put(totalM, totalMVal);
                                    }

                                    events.add(event);
                                    EventSender.sendEvent(event);

                                    //showdop

                                    /*
                                    String onclick = td.get(16).attr("onclick");
                                    if (onclick != null) {
                                        final String id = "tr" + td.get(16).attr("id").substring(1);
                                        //event.link = id;
                                        try {
                                            ((JavascriptExecutor) driver).executeScript("this." + onclick);
                                            WebElement eee = driver.findElement(By.id(id));
                                            int backCount = 0;
                                            while (eee == null && backCount < 30) {
                                                Thread.sleep(100);
                                                eee = driver.findElement(By.id(id));
                                                backCount++;
                                            }
                                            backCount = 0;
                                            if (eee != null) {
                                                List<WebElement> divs = eee.findElements(By.cssSelector("div"));
                                                for (WebElement div: divs) {
                                                    final String text = div.getText();
                                                    if (text.startsWith("Фора:")) {
                                                        parser.parseFora(text, event);
                                                    } else if (text.contains("Тотал:")) {
                                                        parser.parseTotal(text, event);
                                                    }
                                                }
                                            }
                                        /*Document oddsDop = Jsoup.parse(driver.getPageSource());
                                        Element oddsHtml = oddsDop.getElementById("tr" + td.get(16).attr("id").substring(1));
                                        if (oddsHtml != null) {
                                            Elements divs = oddsHtml.select("div");
                                            for (Element div: divs) {
                                                final String text = div.text();
                                                if (text.contains("Дополнительные форы")) {
                                                    parseFora(text, event);
                                                } else if (text.contains("Дополнительные тоталы")) {
                                                    parseTotal(text, event);
                                                }
                                            }
                                        }
                                            ((JavascriptExecutor) driver).executeScript("this." + onclick);
                                        } catch (Exception e) {
                                            logger.error("BetcityParser add odds error", e);
                                        }
                                    }*/
                                }
                            }
                        }
                    } catch(Exception e) {
                        logger.error("", e);
                    }
                }

            /*Elements hrefs = doc.select("a");
            List<String> ids = new ArrayList<String>();
            for (Element a: hrefs) {
                String onclick = a.attr("onclick");
                String text = a.text();
                if (text.contains("Футбол") &&
                    (text.contains("Кубок ") || text.contains("Лига") || text.contains("Чемпионат")) &&
                     !text.contains("первым") && !text.contains("Спец") && !text.contains("Статистика") && !text.contains("Победитель")
                        && !text.contains("Лучший") && !text.contains("Кто") && !text.contains("Отрыв")) {
                    String onclick = a.attr("onclick");

                }
            }*/


            } catch (Exception e) {
                logger.error("BetcityDownloader exception - ", e);
            } finally {
                if (driver != null) {
                    parser.returnWebDriverToPool(driver);
                }
            }
            return events;
        }

    }

    //json ****************************************************

    public static class BCMainMatches {
        BCReply reply;
        boolean ok;
        int lng;
    };

    public static class BCReply {
        Map<Integer, BCSport> sports;
        String hash;
    };

    public static class BCSport {
        String name_sp;
        int order;
        int id_sp;
        int col_sp;
        Map<Integer, BCChampionship> chmps;
    };

    public static class BCChampionship {
        int id_ch;
        String name_ch;
        int order;
        int is_future;
        int col_ch;
        int cnt_ext_add;
        String next_ev;
        Map<Integer, BCEvent> evts;

    };

    public static class BCEvent {
        int id_ev;
        int st_ev;
        int del_ev;
        int order;
        int is_online;
        int date_ev;
        String date_ev_str;
        int cnt_ext_add;
        long md_ev;
        int var;
        int bet_num;
        int status_ev;
        int id_ht;
        int id_at;
        String stat_link;
        String name_ht;
        String name_at;
        Map<Integer, BCOutcome> main;
    };

    public static class BCOutcome {
        int order;
        String name;
        Map<Integer, BCData> data;
    };

    public static class BCData {
        Map<String, Map<String, Object>> blocks;
    };

    //json ****************************************************

    static Gson gson = new Gson();

    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();

        //System.setProperty("http.proxyHost", "127.0.0.1");
        //System.setProperty("http.proxyPort", "5555");
        WebDriver driver = null;
        final List<BCChampionship> chmapsToParse = new ArrayList<BCChampionship>();

        Proxy proxy = new Proxy(                                      //
                Proxy.Type.SOCKS,                                      //
                InetSocketAddress.createUnresolved("127.0.0.1", 5555) //
        );
        List<BetcityDownloader> workers = new ArrayList<BetcityDownloader>();
        try {
            //http://d.betcity.by/api/v1/bets/line?rev=5&template=3&heads=1&ext=0
            //https://betsbc.com/api/v1/bets/competions?rev=2&lng=0&add=name_ch,name_sp&line_ids=1
            Document doc = Jsoup.connect("http://d.betcity.by/api/v1/bets/competions?rev=2&lng=0&add=name_ch,name_sp&line_ids=1")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                    .header("Accept", "application/x-javascript; charset=utf-8")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "ru-RU,ru")
                    .ignoreContentType(true)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    //.proxy(proxy)
                    .timeout(10 * 1000).get();
            final String matches = doc.text();


            BCMainMatches matchesJson = gson.fromJson(matches, BCMainMatches.class);

            for (BCSport sport: matchesJson.reply.sports.values()) {
                for (Map.Entry<Integer, BCChampionship> match: sport.chmps.entrySet()) {
                    final String chTitle = match.getValue().name_ch;

                    if (chTitle.contains("Женщины") ||
                            chTitle.contains("21-го года") ||
                            chTitle.contains("17 лет") ||
                            chTitle.contains("18 лет") ||
                            chTitle.contains("19 лет") ||
                            chTitle.contains("20 лет") ||
                            chTitle.contains("Статистика") ||
                            chTitle.contains("Итоговый") ||
                            chTitle.contains("больше") ||
                            chTitle.contains("голов") ||
                            chTitle.contains("Кто") ||
                            chTitle.contains("молодёжных") ||

                            chTitle.contains("оставит") ||
                            chTitle.contains("трасфер") ||
                            chTitle.contains("окна 20") ||
                            chTitle.contains("УЕФА")
                            || chTitle.contains("Лига Европы")
                            || chTitle.contains("Победитель")
                            || chTitle.contains("победитель")) {
                        continue;
                    }

                    chmapsToParse.add(match.getValue());

                    Document chDoc = Jsoup.connect("http://d.betcity.by/api/v1/bets/line?rev=5&ext=0")
                            .data("ids", Integer.toString(match.getKey()))
                            .header("Accept", "application/x-javascript; charset=utf-8")
                            .header("Accept-Encoding", "gzip, deflate, br")
                            .header("Accept-Language", "ru-RU,ru")
                            .ignoreContentType(true)
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                            //.proxy(proxy)
                            .post();
                    final String leagueText = chDoc.text();
                    logger.info(leagueText);

                    BCMainMatches leagueJson = gson.fromJson(leagueText, BCMainMatches.class);

                    for (BCSport sport1: leagueJson.reply.sports.values()) {
                        for (Map.Entry<Integer, BCChampionship> match1: sport1.chmps.entrySet()) {
                            //http://d.betcity.by/api/v1/bets/line?rev=5&template=3&ext=0
                            Document chMatch = Jsoup.connect("http://d.betcity.by/api/v1/bets/line?rev=5&template=3&ext=0")
                                    .data("ids", Integer.toString(match1.getKey()))
                                    .header("Accept", "application/x-javascript; charset=utf-8")
                                    .header("Accept-Encoding", "gzip, deflate, br")
                                    .header("Accept-Language", "ru-RU,ru")
                                    .ignoreContentType(true)
                                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                                    //.proxy(proxy)
                                    .post();
                            final String matchText = chMatch.text();
                            BCMainMatches matchJson = gson.fromJson(matchText, BCMainMatches.class);

                            logger.info(matchText);

                            for (BCSport sport3: matchJson.reply.sports.values()) {
                                for (Map.Entry<Integer, BCChampionship> champ: sport3.chmps.entrySet()) {
                                    for(Map.Entry<Integer, BCEvent> matchEntry: champ.getValue().evts.entrySet()) {

                                        BCEvent  bcEvent = matchEntry.getValue();
                                        if (bcEvent.name_ht == null || bcEvent.name_at == null) {
                                            continue;
                                        }
                                        BasicEvent event = new BasicEvent();
                                        event.day = DateFormatter.format(bcEvent.date_ev_str.substring(0, 10), dtf);
                                        event.date = bcEvent.date_ev_str;
                                        event.site = BetSite.BETCITY;
                                        event.link = "betsbc.com";

                                        event.team1 = Teams.getTeam(bcEvent.name_ht);
                                        event.team2 = Teams.getTeam(bcEvent.name_at);
                                        if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                                            logger.warn("BetcityParser - unknown teams: " + bcEvent.name_ht + ":" + event.team1 + ";" + bcEvent.name_at + ":" + event.team2);
                                            continue;
                                        }

                                        for (BCOutcome outcome: bcEvent.main.values()) {
                                            for (Map.Entry keyEntr: outcome.data.entrySet()) {
                                                BCData bcData = (BCData)keyEntr.getValue();
                                                for (Map.Entry profit: bcData.blocks.entrySet()) {
                                                    final String profitCat = (String)profit.getKey();
                                                    if (profitCat.equals("Wm")) {
                                                        Map prMap = (Map)profit.getValue();
                                                        Set<Map.Entry> set = prMap.entrySet();
                                                        for (Map.Entry profitVal: set) {
                                                            BetType bet = null;
                                                            if (profitVal.getKey().toString().equals("P1")) {
                                                                bet = BetType.P1;
                                                            } else if (profitVal.getKey().toString().equals("P2")) {
                                                                bet = BetType.P2;
                                                            } else if (profitVal.getKey().toString().equals("X")) {
                                                                bet = BetType.X;
                                                            }
                                                            if (bet != null) {
                                                                Map betMap = (Map) profitVal.getValue();
                                                                Set<Map.Entry> setK = betMap.entrySet();
                                                                for (Map.Entry k : setK) {
                                                                    if (k.getKey().toString().equals("kf")) {
                                                                        event.bets.put(bet, k.getValue().toString());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if (profitCat.equals("WXm")) {
                                                        Map prMap = (Map)profit.getValue();
                                                        Set<Map.Entry> set = prMap.entrySet();
                                                        for (Map.Entry profitVal: set) {
                                                            BetType bet = null;
                                                            if (profitVal.getKey().toString().equals("1X")) {
                                                                bet = BetType.P1X;
                                                            } else if (profitVal.getKey().toString().equals("X2")) {
                                                                bet = BetType.P2X;
                                                            } else if (profitVal.getKey().toString().equals("12")) {
                                                                bet = BetType.P12;
                                                            }
                                                            if (bet != null) {
                                                                Map betMap = (Map) profitVal.getValue();
                                                                Set<Map.Entry> setK = betMap.entrySet();
                                                                for (Map.Entry k : setK) {
                                                                    if (k.getKey().toString().equals("kf")) {
                                                                        event.bets.put(bet, k.getValue().toString());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                    if (profitCat.equals("F1m")) {
                                                        Map prMap = (Map)profit.getValue();
                                                        Set set1 = prMap.entrySet();
                                                        Object[] set = set1.toArray();
                                                        List<Object> listF = Arrays.asList(set);
                                                        for (int i = 0; i < set.length; i++) {
                                                            BetType bet = null;
                                                            if (((Map.Entry)set[i]).getKey().toString().equals("Kf_F1")) {
                                                                bet = BetDataMapping.betsTotal.get(((Map.Entry)set[i+1]).getValue() + "F1");
                                                            } else if (((Map.Entry)set[i]).getKey().toString().equals("Kf_F2")) {
                                                                bet = BetDataMapping.betsTotal.get(((Map.Entry)set[i+1]).getValue() + "F2");
                                                            }
                                                            if (bet != null) {
                                                                Map betMap = (Map)((Map.Entry)set[i]).getValue();
                                                                Set<Map.Entry> setK = betMap.entrySet();
                                                                for (Map.Entry k : setK) {
                                                                    if (k.getKey().toString().equals("kf")) {
                                                                        event.bets.put(bet, k.getValue().toString());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                    if (profitCat.equals("T1m")) {
                                                        Map prMap = (Map)profit.getValue();
                                                        Set set1 = prMap.entrySet();
                                                        Object[] set = set1.toArray();
                                                        List<Object> listF = Arrays.asList(set);
                                                        for (int i = 0; i < set.length; i++) {
                                                            BetType bet = null;
                                                            if (((Map.Entry)set[i]).getKey().toString().equals("Tm")) {
                                                                bet = BetDataMapping.betsTotal.get(((Map.Entry)set[1]).getValue() + "L");
                                                            } else if (((Map.Entry)set[i]).getKey().toString().equals("Tb")) {
                                                                bet = BetDataMapping.betsTotal.get(((Map.Entry)set[1]).getValue() + "M");
                                                            }
                                                            if (bet != null) {
                                                                Map betMap = (Map)((Map.Entry)set[i]).getValue();
                                                                Set<Map.Entry> setK = betMap.entrySet();
                                                                for (Map.Entry k : setK) {
                                                                    if (k.getKey().toString().equals("kf")) {
                                                                        event.bets.put(bet, k.getValue().toString());
                                                                    }
                                                                }
                                                            }
                                                        }
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

                }
            }


        } catch (Exception e) {
            logger.error("BetcityParser exception - ", e);
        }
        return events;
    }

}
