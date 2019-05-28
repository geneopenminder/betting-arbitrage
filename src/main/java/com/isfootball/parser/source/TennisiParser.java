package com.isfootball.parser.source;

import com.isfootball.model.BasicEvent;
import com.isfootball.parser.*;
import com.isfootball.pool.BasePoolInterface;
import com.isfootball.processing.EventSender;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

import java.util.*;

/**
 * Created by Evgeniy Pshenitsin on 14.01.2015.
 */
public class TennisiParser  extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    //http://www.tennisi.com/mtg2/cgi/free.Welcome?lang=rus

    //Tue Feb 17 01:01:27 GST 2015

    public static Map<String, Integer> months = new HashMap<String, Integer>() {{
        put("янв", 0);
        put("фев", 1);
        put("мар", 2);
        put("апр", 3);
        put("мая", 4);
        put("май", 4);
        put("июн", 5);
        put("июл", 6);
        put("авг", 7);
        put("сен", 8);
        put("окт", 9);
        put("ноя", 10);
        put("дек", 11);
    }};

    static String currLink = null;

    public TennisiParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.TENNISI;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();


        final String linkBase = "http://www.tennisi.com/mtg2/cgi/";
        List<String> links = new ArrayList<String>();
        //System.setProperty("http.proxyHost", "127.0.0.1");
        //System.setProperty("http.proxyPort", "8089");
        WebDriver driver = null;

        try {
            //driver = getWebDriverFromPool();
            //driver.get("http://www.tennisi.com/mtg2/cgi/!book2_free.BetsLines?gameid=5&lang=rus&categoryid=137" +
            //        "&eventid=&parentid=&showold=&pidstmpid=0&betgroupid=-1&age=0");
            //final String source = driver.getPageSource();

            //Document doc = Jsoup.parse(source);
            Document doc = Jsoup.connect("http://www.tennisi.com/mtg2/cgi/!book2_free.BetsLines?gameid=5&lang=rus&categoryid=137" +
                    "&eventid=&parentid=&showold=&pidstmpid=0&betgroupid=-1&age=0")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                    .timeout(10 * 1000).get();

            Iterator<Element> tables = doc.select("table").iterator();
            tables.next();
            tables.next();
            tables.next();
            tables.next();
            while(tables.hasNext()) {
                Element table = tables.next();
                Iterator<Element> ite = table.select("tr").iterator();
                while (ite.hasNext()) {
                    Element row = ite.next();
                    Iterator<Element> cols = row.select("td").iterator();
                    while (cols.hasNext()) {
                        Element col = cols.next();
                        String num = col.text().replace("\u00a0", ""); //delete &nbsp;
                        if (num.length() == 3 && NumberUtils.isDigits(num)) {
                            String link = row.select("a").first().attr("href");
                            links.add(link);
                        }
                    }
                    String htm = row.html();
                }
            }

            for (String link: links) {
                currLink = link;
                BasicEvent event = new BasicEvent();
                event.site = BetSite.TENNISI;
                event.link = linkBase + link;
                //driver.get(linkBase + link);
                //Document docDetail = Jsoup.parse(driver.getPageSource());
                Document docDetail = Jsoup.connect(linkBase + link)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                .timeout(10 * 1000).get();
                String teamsData = docDetail.getElementsByClass("pageheader2").text().trim();
                if (teamsData.contains("Любая") || teamsData.contains("Любой")
                        || teamsData.contains("состояние") || teamsData.contains("Угловые") || teamsData.contains("голам") ||
                teamsData.contains("ЖК") || teamsData.contains("владения") || teamsData.contains("Офсайды") ||
                        teamsData.contains("Удары") || teamsData.contains("Фолы") || teamsData.contains("(мол)")) {
                    continue;
                }
                String[] teams = teamsData.split(" - ");
                if (teams.length != 2) {
                    continue;
                }
                if (teams[0].contains("(")) {
                    teams[0] = teams[0].substring(0, teams[0].indexOf("("));
                }
                if (teams[1].contains("(")) {
                    teams[1] = teams[1].substring(0, teams[1].indexOf("("));
                }
                event.team1 = Teams.getTeam(teams[0].trim());
                event.team2 = Teams.getTeam(teams[1].trim());
                if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                    logger.warn("Tennisi - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                    continue;
                }
                Element mainTable = null;
                Iterator<Element> rowBets = null;
                final String id = link.substring(link.indexOf("eventid") + 8, link.indexOf("&more"));
                Element tableMain = docDetail.getElementById("el" + id);

                Element oddsTabel = null;
                Element allTable = null;
                for (Element table : docDetail.select("table")) {
                    rowBets = table.select("tr").iterator();
                    for (Element rowScore : table.select("tr")) {
                        if (!rowScore.hasClass("trm" + id)) {
                            continue;
                        } else {
                            oddsTabel = rowScore.select("table").first();
                            allTable = table;
                            break;
                        }
                    }
                }
                if (oddsTabel == null) {
                    continue;
                }

                String d = allTable.select("tr").get(2).text().replace("\u00a0", "").trim();;
                if (d.equalsIgnoreCase("Сегодня")) {
                    event.date = new Date().toString();
                    event.day = new Date();
                } else {
                    String[] dateData = d.split(" ");
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateData[0]));
                    c.set(Calendar.MONTH, months.get(dateData[1].substring(0, 3).toLowerCase()));
                    event.date = c.getTime().toString();
                    event.day = c.getTime();
                }

                String p1 = tableMain.select("td").get(3).text().trim().replace("\u00a0", "");
                if (checkNotZero(p1)) {
                    event.bets.put(BetType.P1, p1);
                }
                String x = tableMain.select("td").get(4).text().trim().replace("\u00a0", "");
                if (checkNotZero(x)) {
                    event.bets.put(BetType.X, x);
                }
                String p2 = tableMain.select("td").get(5).text().trim().replace("\u00a0", "");
                if (checkNotZero(p2)) {
                    event.bets.put(BetType.P2, p2);
                }

                String p1X = tableMain.select("td").get(7).text().trim().replace("\u00a0", "");
                if (checkNotZero(p1X)) {
                    event.bets.put(BetType.P1X, p1X);
                }
                String x2 = tableMain.select("td").get(8).text().trim().replace("\u00a0", "");
                if (checkNotZero(x2)) {
                    event.bets.put(BetType.P12, x2);
                }
                String p2X = tableMain.select("td").get(9).text().trim().replace("\u00a0", "");
                if (checkNotZero(p2X)) {
                    event.bets.put(BetType.P2X, p2X);
                }
                events.add(event);
                EventSender.sendEvent(event);
                if (true) {
                    continue;
                }

            for (Element row : oddsTabel.select("tr")) {
                    //final String text = rowScore.select("tr").text();

                    /*if (text.contains("Ставки")) {
                        mainTable = tableMain;
                        break;
                    } else if (text.contains("Ничья")) {
                        Elements rows = table.select("tr");
                        for (int i = 4; i < rows.size(); i++) {
                            Element row = rows.get(i);
                            Elements tds = row.select("td");
                            if (tds.size() == 8) {
                                String score = tds.get(0).text().trim().replace(" - ", ":").replace("\u00a0", "");
                                String val;
                                BetType scoreBet;
                                if (!score.isEmpty()) {
                                    val = tds.get(1).text().trim().replace("\u00a0", "");
                                    scoreBet = BetDataMapping.totalScore.get(score);
                                    if (scoreBet != null && checkNotZero(val)) {
                                        //event.bets.put(scoreBet, val);
                                    }
                                }
                                score = tds.get(3).text().trim().replace(" - ", ":").replace("\u00a0", "");
                                if (!score.isEmpty()) {
                                    val = tds.get(4).text().trim().replace("\u00a0", "");
                                    scoreBet = BetDataMapping.totalScore.get(score);
                                    if (scoreBet != null && checkNotZero(val)) {
                                        //event.bets.put(scoreBet, val);
                                    }
                                }
                                score = tds.get(6).text().trim().replace(" - ", ":").replace("\u00a0", "");
                                if (!score.isEmpty()) {
                                    val = tds.get(7).text().trim().replace("\u00a0", "");
                                    scoreBet = BetDataMapping.totalScore.get(score);
                                    if (scoreBet != null && checkNotZero(val)) {
                                        //event.bets.put(scoreBet, val);
                                    }
                                }
                            }
                        }
                    }
                }
                if (mainTable == null) {
                    continue;
                }*/
                    //rowBets.next();
                    //while(rowBets.hasNext()) {
                    //Element row = rowBets.next();


                    try {
                        List<Element> cols = row.select("td");
                        final int colsCount = cols.size();
                        if (colsCount == 1) {
                            String text = row.select("b").text();
                            //final String text = row.text();
                            if (text.contains("Победа с форой")) {
                                Element fora1 = rowBets.next();
                                final String fora1Str = fora1.select("td").first().text();
                                final String fora1Num = (fora1Str.substring(fora1Str.indexOf("форой") + 5)).trim().replace("\u00a0", "").replace("+", "");
                                final String fora1StrVal = fora1.select("td").get(1).text().replace("\u00a0", "");
                                BetType bet = BetDataMapping.betsTotal.get(fora1Num + "F1");
                                if (bet != null && checkNotZero(fora1StrVal)) {
                                    event.bets.put(bet, fora1StrVal);
                                }
                                Element fora2 = rowBets.next();
                                final String fora2Str = fora2.select("td").first().text();
                                final String fora2Num = (fora2Str.substring(fora2Str.indexOf("форой") + 5)).trim().replace("\u00a0", "").replace("+", "");
                                final String fora2StrVal = fora2.select("td").get(1).text().replace("\u00a0", "");
                                BetType bet2 = BetDataMapping.betsTotal.get(fora2Num + "F2");
                                if (bet2 != null && checkNotZero(fora2StrVal)) {
                                    event.bets.put(bet2, fora2StrVal);
                                }
                            } else if (text.contains("Азиатская фора")) {
                                Element fora1 = rowBets.next();
                                final String fora1Str = fora1.select("td").first().text();
                                final String fora1Num = (fora1Str.substring(fora1Str.indexOf("форой") + 5)).trim().replace("\u00a0", "").replace("+", "");
                                final String fora1StrVal = fora1.select("td").get(1).text().replace("\u00a0", "");
                                BetType bet = BetDataMapping.betsTotal.get(fora1Num + "F1");
                                if (bet != null && checkNotZero(fora1StrVal)) {
                                    event.bets.put(bet, fora1StrVal);
                                }
                                Element fora2 = rowBets.next();
                                final String fora2Str = fora2.select("td").first().text();
                                final String fora2Num = (fora2Str.substring(fora2Str.indexOf("форой") + 5)).trim().replace("\u00a0", "").replace("+", "");
                                final String fora2StrVal = fora2.select("td").get(1).text().replace("\u00a0", "");
                                BetType bet2 = BetDataMapping.betsTotal.get(fora2Num + "F2");
                                if (bet2 != null && checkNotZero(fora2StrVal)) {
                                    event.bets.put(bet2, fora2StrVal);
                                }
                            } else if (text.contains("Тотал матча")) {
                                if (!text.contains("азия")) {
                                    Element totalL = rowBets.next();
                                    final String totalLStr = totalL.select("td").first().text();
                                    final String totalLNum = (totalLStr.substring(totalLStr.indexOf("меньше") + 6)).trim().replace("\u00a0", "");
                                    final String totalLStrVal = totalL.select("td").get(1).text().replace("\u00a0", "");
                                    BetType betL = BetDataMapping.betsTotal.get(totalLNum + "L");
                                    if (betL != null && checkNotZero(totalLStrVal)) {
                                        event.bets.put(betL, totalLStrVal);
                                    }
                                    Element totalM = rowBets.next();
                                    final String totalMStr = totalM.select("td").first().text();
                                    final String totalMNum = (totalMStr.substring(totalMStr.indexOf("больше") + 6)).trim().replace("\u00a0", "");
                                    final String totalMStrVal = totalM.select("td").get(1).text().replace("\u00a0", "");
                                    BetType betM = BetDataMapping.betsTotal.get(totalMNum + "M");
                                    if (betM != null && checkNotZero(totalLStrVal)) {
                                        event.bets.put(betM, totalMStrVal);
                                    }
                                } else {
                                    Element totalL = rowBets.next();
                                    final String totalLStr = totalL.select("td").first().text();
                                    final String totalLNum = (totalLStr.substring(totalLStr.indexOf("меньше") + 6, totalLStr.length() - 5)).trim().replace("\u00a0", "");
                                    final String totalLStrVal = totalL.select("td").get(1).text().replace("\u00a0", "");
                                    BetType betL = BetDataMapping.betsTotal.get(totalLNum + "L");
                                    if (betL != null && checkNotZero(totalLStrVal)) {
                                        event.bets.put(betL, totalLStrVal);
                                    }
                                    Element totalM = rowBets.next();
                                    final String totalMStr = totalM.select("td").first().text();
                                    final String totalMNum = (totalMStr.substring(totalMStr.indexOf("больше") + 6, totalMStr.length() - 5)).trim().replace("\u00a0", "");
                                    final String totalMStrVal = totalM.select("td").get(1).text().replace("\u00a0", "");
                                    BetType betM = BetDataMapping.betsTotal.get(totalMNum + "M");
                                    if (betM != null && checkNotZero(totalMStrVal)) {
                                        event.bets.put(betM, totalMStrVal);
                                    }
                                }
                            } else if (text.contains("Тотал") && text.contains(teams[0].trim()) && !text.contains("проигрыш") && !text.contains("тайм")
                                    && !text.contains("Результат")) {
                                //Total 1
                                Element totalL = rowBets.next();
                                final String totalLStr = totalL.select("td").first().text();
                                final String totalLNum = (totalLStr.substring(totalLStr.indexOf("меньше") + 6)).trim().replace("\u00a0", "");
                                final String totalLStrVal = totalL.select("td").get(1).text().replace("\u00a0", "");
                                BetType betL = BetDataMapping.betsTotal.get("T1_" + totalLNum + "L");
                                if (betL != null && checkNotZero(totalLStrVal)) {
                                    event.bets.put(betL, totalLStrVal);
                                }
                                Element totalM = rowBets.next();
                                final String totalMStr = totalM.select("td").first().text();
                                final String totalMNum = (totalMStr.substring(totalMStr.indexOf("больше") + 6)).trim().replace("\u00a0", "");
                                final String totalMStrVal = totalM.select("td").get(1).text().replace("\u00a0", "");
                                BetType betM = BetDataMapping.betsTotal.get("T1_" + totalMNum + "M");
                                if (betM != null && checkNotZero(totalLStrVal)) {
                                    event.bets.put(betM, totalMStrVal);
                                }
                            } else if (text.contains("Тотал") && text.contains(teams[1].trim()) && !text.contains("проигрыш") && !text.contains("тайм")
                                    && !text.contains("Результат")) {
                                //Total 2
                                Element totalL = rowBets.next();
                                final String totalLStr = totalL.select("td").first().text();
                                final String totalLNum = (totalLStr.substring(totalLStr.indexOf("меньше") + 6)).trim().replace("\u00a0", "");
                                final String totalLStrVal = totalL.select("td").get(1).text().replace("\u00a0", "");
                                BetType betL = BetDataMapping.betsTotal.get("T2_" + totalLNum + "L");
                                if (betL != null && checkNotZero(totalLStrVal)) {
                                    event.bets.put(betL, totalLStrVal);
                                }
                                Element totalM = rowBets.next();
                                final String totalMStr = totalM.select("td").first().text();
                                final String totalMNum = (totalMStr.substring(totalMStr.indexOf("больше") + 6)).trim().replace("\u00a0", "");
                                final String totalMStrVal = totalM.select("td").get(1).text().replace("\u00a0", "");
                                BetType betM = BetDataMapping.betsTotal.get("T2_" + totalMNum + "M");
                                if (betM != null && checkNotZero(totalLStrVal)) {
                                    event.bets.put(betM, totalMStrVal);
                                }
                            } else if (text.contains("Двойной исход") && !text.contains("(")) {
                            } else if (text.equalsIgnoreCase(teamsData)) {
                            }
                        }
                        //}
                    } catch (Exception e) {
                        logger.error("Tennnisi exception - ", e);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Tennnisi exception - ", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    public boolean checkNotZero(String value) {
        try {
            Double odds = Double.parseDouble(value);
            if (odds != 0.0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("Tennisis parse exception - ", e);
            return false;
        }
    }

}
