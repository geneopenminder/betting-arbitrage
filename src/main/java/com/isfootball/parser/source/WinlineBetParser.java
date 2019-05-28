package com.isfootball.parser.source;

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
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Evgeniy Pshenitsin on 23.02.2015.
 */
public class WinlineBetParser extends BaseParser {

    //http://winlinebet.com/

    private static final Logger logger = LogManager.getLogger("parser");

    List<String> links = new ArrayList<String>() {{
        add("http://winlinebet.com/stavki/sport/futbol/angliya/");
        add("http://winlinebet.com/stavki/sport/futbol/germaniya/");
        add("http://winlinebet.com/stavki/sport/futbol/ispaniya/");
        add("http://winlinebet.com/stavki/sport/futbol/italiya/");
        add("http://winlinebet.com/stavki/sport/futbol/mezhdunarodnye/");
        add("http://winlinebet.com/stavki/sport/futbol/mezhdunarodnye_kluby/");
        add("http://winlinebet.com/stavki/sport/futbol/rossiya/");
        add("http://winlinebet.com/stavki/sport/futbol/francziya/");
        add("http://winlinebet.com/stavki/sport/futbol/regiony/argentina/");
        add("http://winlinebet.com/stavki/sport/futbol/regiony/belijgiya/");
        add("http://winlinebet.com/stavki/sport/futbol/regiony/braziliya/");
        add("http://winlinebet.com/stavki/sport/futbol/regiony/gollandiya_/");
    }};

    final int STAGE_ODDS = 1;
    final int STAGE_FORA_GND = 2;
    final int STAGE_SCORE = 3;
    final int STAGE_TOTAL_GOALS = 4;

    final int SUB_STAGE_OUT = 9000;
    final int SUB_STAGE_ODDS_P = 1000;
    final int SUB_STAGE_ODDS_PX = 1001;
    final int SUB_STAGE_ODDS_TOTAL = 1002;
    final int SUB_STAGE_ODDS_TOTAL_ASIAN = 1003;
    final int SUB_STAGE_ODDS_TOTAL_F1 = 1004;
    final int SUB_STAGE_ODDS_TOTAL_F2 = 1005;
    final int SUB_STAGE_ODDS_TOTAL_IND = 1006;
    final int SUB_STAGE_SCORE_ALL = 1007;
    final int SUB_STAGE_FORA = 1008;
    final int SUB_STAGE_FORA_0 = 1009;
    final int SUB_STAGE_FORA_GND = 1010;

    ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(50, getThreadFactory(WinlineBetParser.class.getSimpleName()));

    public WinlineBetParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.WINLINEBET;
    }

    @Override
    public List<BasicEvent> getEvents() {

        List<BasicEvent> events = new ArrayList<BasicEvent>();

        WebDriver driver = null;

        List<WinLineDownloader> workers = new ArrayList<WinLineDownloader>();

        try {
            driver = getWebDriverFromPool();
            driver.get("http://winlinebet.com/stavki/sport/futbol/");
            String main = driver.getPageSource();
            Document doc = Jsoup.parse(main);
            Elements a = doc.select("a");
            for (Element link: a) {
                String href = link.attr("href");
                if (href.contains("stavki/sport/futbol")) {
                    links.add(href);
                }
            }
            for (String link : links) {
                driver.get(link);
                Thread.sleep(200);
                String htmlSource = driver.getPageSource();
                Document matchBets = Jsoup.parse(htmlSource);
                Element table = matchBets.getElementById("TABLEN");
                int tryNumber = 0;
                while (table == null && tryNumber++ < 10) {
                    Thread.sleep(500);
                }
                if (table == null) {
                    logger.error("error download mathecs list from - " + link);
                }
                for (Element divMatch : table.getElementsByAttribute("name")) {
                    String elem = divMatch.text();
                    if (divMatch.attr("name").matches("n([0-9]{1,2})")) {
                        Element matchTitle = divMatch.getElementsByClass("EU1000000").first();
                        if (matchTitle == null) {
                            continue;
                        }
                        String[] teams = matchTitle.text().trim().split(" - ");
                        if (teams.length != 2) {
                            continue;
                        }
                        BasicEvent event = new BasicEvent();
                        event.date = divMatch.getElementsByClass("ix4").text();
                        try {
                            event.day = DateFormatter.format(event.date + new DateTime().getYear(), betSite);
                        } catch (Exception e) {
                            logger.error("Winline wrong date exception ", e);
                        }
                        event.team1 = Teams.getTeam(teams[0].trim());
                        event.team2 = Teams.getTeam(teams[1].trim());
                        if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                            logger.warn("WinlineBet - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                            continue;
                        }
                        event.site = BetSite.WINLINEBET;
                        String onclick = matchTitle.attr("onclick");
                        String matchId = onclick.substring(onclick.indexOf("(") + 1, onclick.indexOf(")"));
                        final String matchBetsLink = "http://winlinebet.com/plus/" + matchId;
                        workers.add(new WinLineDownloader(this, matchBetsLink, divMatch));
                    }
                }
            }
            returnWebDriverToPool(driver);
            driver = null;

            List<Future<BasicEvent>> parserStates = new ArrayList<Future<BasicEvent>>();
            for (WinLineDownloader tasks: workers) {
                parserStates.add(downloadersTaskPool.submit(tasks));
            }
            //Thread.sleep(10 * 60 * 1000);
            for (Future<BasicEvent> f: parserStates) {
                try {
                        BasicEvent event = f.get();
                        if (event != null) {
                            events.add(f.get());
                        }
                    f.cancel(true);
                } catch (Exception e) {
                    logger.error("processGetBets: future get() exception - ", e);
                    f.cancel(true);
                }
            }
            //downloadersTaskPool.shutdownNow();
        } catch (UnreachableBrowserException e) {
            logger.error("WinlineBet worker error - UnreachableBrowserException", e);
            recreateDiedWebDriver(driver);
            driver = null;
        } catch (Exception e) {
            logger.error("WinlineBet exception", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    private static class WinLineDownloader extends BaseDownloaderSingle {

        WinlineBetParser parser;
        String link;
        Element divMatch;

        public WinLineDownloader(WinlineBetParser parser, String link, Element divMatch) {
            this.parser = parser;
            this.link = link;
            this.divMatch = divMatch;

        }

        @Override
        public BasicEvent download() {
            return parser.parseEvent(divMatch, link);
        }
    }

    private BasicEvent parseEvent(Element divMatch, String matchLink) {
        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            String elem = divMatch.text();
            if (divMatch.attr("name").matches("n([0-9]{1,2})")) {
                Element matchTitle = divMatch.getElementsByClass("EU1000000").first();
                if (matchTitle == null) {
                    return null;
                }
                String[] teams = matchTitle.text().trim().split(" - ");
                if (teams.length != 2) {
                    return null;
                }
                BasicEvent event = new BasicEvent();
                event.date = divMatch.getElementsByClass("ix4").text();
                try {
                    event.day = DateFormatter.format(event.date + new DateTime().getYear(), betSite);
                } catch (Exception e) {
                    logger.error("Winline wrong date exception ", e);
                }
                event.team1 = Teams.getTeam(teams[0].trim());
                event.team2 = Teams.getTeam(teams[1].trim());
                if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                    logger.warn("WinlineBet - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                    return null;
                }
                event.site = BetSite.WINLINEBET;
                String onclick = matchTitle.attr("onclick");
                //String matchId = onclick.substring(onclick.indexOf("(") + 1, onclick.indexOf(")"));
                event.link = matchLink;
                driver.get(matchLink);
                Thread.sleep(200);
                String betsSource = driver.getPageSource();
                Document bets = Jsoup.parse(betsSource);
                Element rospis = bets.getElementById("ROSPIS");
                int tryNumber = 0;
                while (rospis == null && tryNumber++ < 20) {
                    Thread.sleep(500);
                }
                returnWebDriverToPool(driver);
                driver = null;
                if (rospis == null) {
                    logger.error("download match error for link - " + matchLink);
                }
                int stage = 0;
                int subStage = 0;
                Iterator<Element> divs = rospis.select("div").iterator();
                boolean hasTotalTimePOdds = false;
                while (divs.hasNext()) {
                    Element div = divs.next();
                    if (div.hasClass("free5")) {
                        final String stageName = div.text().trim();
                        if (stageName.equals("Исход")) {
                            stage = STAGE_ODDS;
                        } else if (stageName.equals("Фора/Гандикап")) {
                            stage = STAGE_FORA_GND;
                        } else if (stageName.equals("Счет")) {
                            stage = STAGE_SCORE;
                        } else if (stageName.equals("Тотал/Голы")) {
                            stage = STAGE_TOTAL_GOALS;
                        }
                    }
                    if (stage == STAGE_ODDS) {
                        if (div.hasClass("fr2")) {
                            final String oddsP = div.text().trim().replace("\u00a0", " ");
                            if (oddsP.equals("Исход 1X2")) {
                                subStage = SUB_STAGE_ODDS_P;
                            } else if (oddsP.equals("Двойной шанс")) {
                                subStage = SUB_STAGE_ODDS_PX;
                            } else {
                                subStage = SUB_STAGE_OUT;
                            }
                        }

                        if (subStage == SUB_STAGE_ODDS_P) {
                            if (div.text().contains("Матч")) {
                                hasTotalTimePOdds = true;
                            }
                            if (div.hasClass("plus333") && hasTotalTimePOdds) {
                                String oddsValP = div.text().substring(0, div.text().indexOf("#"));
                                if (oddsValP.trim().equals("-")) {
                                    oddsValP = "0.0";
                                }
                                if (!event.bets.containsKey(BetType.P1)) {
                                    event.bets.put(BetType.P1, oddsValP);
                                } else if (!event.bets.containsKey(BetType.X)) {
                                    event.bets.put(BetType.X, oddsValP);
                                } else if (!event.bets.containsKey(BetType.P2)) {
                                    event.bets.put(BetType.P2, oddsValP);
                                    subStage = 0;
                                }
                            }
                        } else if (subStage == SUB_STAGE_ODDS_PX) {
                            if (div.hasClass("plus333")) {
                                String oddsValP = div.text().substring(0, div.text().indexOf("#"));
                                if (oddsValP.trim().equals("-")) {
                                    oddsValP = "0.0";
                                }
                                if (!event.bets.containsKey(BetType.P1X)) {
                                    event.bets.put(BetType.P1X, oddsValP);
                                } else if (!event.bets.containsKey(BetType.P12)) {
                                    event.bets.put(BetType.P12, oddsValP);
                                } else if (!event.bets.containsKey(BetType.P2X)) {
                                    event.bets.put(BetType.P2X, oddsValP);
                                    stage = 0; //TODO
                                }
                            }
                        }
                    } else if (stage == STAGE_FORA_GND) {
                        if (div.hasClass("fr2")) {
                            final String oddsF = div.text().trim().replace("\u00a0", " ");
                            if (oddsF.equals("Фора")) {
                                subStage = SUB_STAGE_FORA;
                            } else if (oddsF.equals("Фора (0)")) {
                                subStage = SUB_STAGE_FORA_0;
                            } else if (oddsF.equals("Европейский Гандикап")) {
                                subStage = SUB_STAGE_FORA_GND;
                            } else {
                                subStage = 0;
                            }
                        }
                        if (subStage == SUB_STAGE_FORA) {
                            Elements cell4shortDivs = div.getElementsByClass("cell4short");
                            if (cell4shortDivs != null && cell4shortDivs.size() == 2) {
                                Element f1 = cell4shortDivs.get(0);
                                String oddsValF1 = f1.getElementsByClass("plus333").first().text().substring(0, f1.getElementsByClass("plus333").first().text().indexOf("#"));
                                String f1Type = f1.getElementsByClass("plus222").first().text();
                                BetType f1Bet = BetDataMapping.betsTotal.get(foraType(f1Type) + "F1");
                                if (f1Bet != null && !oddsValF1.trim().equals("-")) {
                                    event.bets.put(f1Bet, oddsValF1);
                                }
                                Element f2 = cell4shortDivs.get(1);
                                String oddsValF2 = f2.getElementsByClass("plus333").first().text().substring(0, f2.getElementsByClass("plus333").first().text().indexOf("#"));
                                String f2Type = f2.getElementsByClass("plus222").first().text();
                                BetType f2Bet = BetDataMapping.betsTotal.get(foraType(f2Type) + "F2");
                                if (f2Bet != null && !oddsValF2.trim().equals("-")) {
                                    event.bets.put(f2Bet, oddsValF2);
                                }
                            }
                        }
                    } else if (stage == STAGE_TOTAL_GOALS) {
                        if (div.hasClass("fr2")) {
                            final String oddsF = div.text().trim().replace("\u00a0", " ");
                            if (oddsF.equals("Тотал")) {
                                subStage = SUB_STAGE_ODDS_TOTAL;
                            } else if (oddsF.equals("Азиатский тотал")) {
                                subStage = SUB_STAGE_ODDS_TOTAL_ASIAN;
                            } else {
                                subStage = 0;
                            }

                                /*else if (oddsF.equals("Тотал хозяев")) {
                                    subStage = SUB_STAGE_ODDS_TOTAL_F1;
                                } else if (oddsF.equals("Тотал гостей")) {
                                    subStage = SUB_STAGE_ODDS_TOTAL_F2;
                                } else if (oddsF.contains("Индивидуальный")) {
                                    subStage = SUB_STAGE_ODDS_TOTAL_IND;
                                }*/
                        }
                        if (subStage == SUB_STAGE_ODDS_TOTAL) {
                            Elements cell6shortDivs = div.getElementsByClass("cell6short");
                            if (cell6shortDivs != null && cell6shortDivs.size() == 2) {
                                Element tM = cell6shortDivs.get(0);
                                String tMType = tM.getElementsByClass("plus222").first().text().substring(2);
                                String oddsValTM = tM.getElementsByClass("plus333").first().text().substring(0, tM.getElementsByClass("plus333").first().text().indexOf("#"));
                                BetType TMBet = BetDataMapping.betsTotal.get(tMType.trim() + "M");
                                if (TMBet != null && !oddsValTM.trim().equals("-")) {
                                    event.bets.put(TMBet, oddsValTM);
                                }
                                Element tL = cell6shortDivs.get(1);
                                String tLType = tL.getElementsByClass("plus222").first().text().substring(2);
                                String oddsValTL = tL.getElementsByClass("plus333").first().text().substring(0, tL.getElementsByClass("plus333").first().text().indexOf("#"));
                                BetType TLBet = BetDataMapping.betsTotal.get(tLType.trim() + "L");
                                if (TLBet != null && !oddsValTL.trim().equals("-")) {
                                    event.bets.put(TLBet, oddsValTL);
                                }
                            }
                        } else if (subStage == SUB_STAGE_ODDS_TOTAL_ASIAN) {
                            Elements cell4shortDivs = div.getElementsByClass("cell4short");
                            if (cell4shortDivs != null && cell4shortDivs.size() == 2) {
                                Element tM = cell4shortDivs.get(0);
                                String tMType = tM.getElementsByClass("plus222").first().text().substring(2);
                                String oddsValTM = tM.getElementsByClass("plus333").first().text().substring(0, tM.getElementsByClass("plus333").first().text().indexOf("#"));
                                BetType TMBet = BetDataMapping.betsTotal.get(asianTotalType(tMType.trim()) + "M");
                                if (TMBet != null && !oddsValTM.trim().equals("-")) {
                                    event.bets.put(TMBet, oddsValTM);
                                }
                                Element tL = cell4shortDivs.get(1);
                                String tLType = tL.getElementsByClass("plus222").first().text().substring(2);
                                String oddsValTL = tL.getElementsByClass("plus333").first().text().substring(0, tL.getElementsByClass("plus333").first().text().indexOf("#"));
                                BetType TLBet = BetDataMapping.betsTotal.get(asianTotalType(tLType.trim()) + "L");
                                if (TLBet != null && !oddsValTL.trim().equals("-")) {
                                    event.bets.put(TLBet, oddsValTL);
                                }
                            }
                        }
                    } else if (stage == STAGE_SCORE) {
                        if (div.hasClass("fr2")) {
                            final String oddsF = div.text().trim().replace("\u00a0", " ");
                            if (oddsF.equals("Точный счет")) {
                                subStage = SUB_STAGE_SCORE_ALL;
                            } else {
                                subStage = 0;
                            }
                            if (subStage == SUB_STAGE_SCORE_ALL) {

                            }
                        }
                        if (subStage == SUB_STAGE_SCORE_ALL) {
                                    /*if (div.hasClass("cell6short")) {
                                        String scoreType = div.getElementsByClass("plus222").first().text().trim();
                                        String val = div.getElementsByClass("plus333").first().text().trim();
                                        if (!val.equals("-")) {
                                            String oddsValTL = val.substring(0, val.indexOf("#"));
                                            Bet scoreBet = BetDataMapping.totalScore.get(scoreType);
                                            if (scoreBet != null) {
                                                event.bets.put(scoreBet, oddsValTL);
                                            }
                                        }
                                    }*/
                        }
                    }
                }
                EventSender.sendEvent(event);
                return event;
            }
        } catch (UnreachableBrowserException e) {
            logger.error("WinlineBet worker error - UnreachableBrowserException", e);
            recreateDiedWebDriver(driver);
            driver = null;
        } catch (Exception e) {
            logger.error("Winlinebet parse error", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return null;
    }

    private String foraType(String type) {
        String sign = "+";
        if (type.contains("+")) {
            sign = "+";
        } else if (type.contains("-")) {
            sign = "-";
        }
        int lastIndexofSign = type.lastIndexOf(sign);
        if (lastIndexofSign > 0) {
            String val1 = type.substring(0, lastIndexofSign);
            String val2 = type.substring(lastIndexofSign);
            try {
                Double res = (Double.parseDouble(val1) + Double.parseDouble(val2)) / 2.0;
                return res.toString();
            } catch (Exception e) {
                logger.error("WinlineBet foraType err exception - ", e);
            }
            return "0.0"; //TODO
        } else {
            return type.replace("+", "").trim();
        }

    }

    private String asianTotalType(String type) {
        String[] vals = type.split("-");
        if (vals.length != 2) {
            return type.trim();
        }
        try {
            Double res = (Double.parseDouble(vals[0]) + Double.parseDouble(vals[1])) / 2.0;
            return res.toString();
        } catch (Exception e) {
            logger.error("WinlineBet asianTotalType err exception - ", e);
        }
        return "UNK";
    }

}
