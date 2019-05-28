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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Evgeniy Pshenitsin on 13.02.2015.
 */
public class Bet188Parser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    static DateFormat format = new SimpleDateFormat("dd / MMyyyy", Locale.ENGLISH);

    ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(50, getThreadFactory(Bet188Parser.class.getSimpleName()));

    public Bet188Parser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.BET_188;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();

        List<Bet188Downloader> workers = new ArrayList<Bet188Downloader>();
        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            //https://www.188bet.co.uk/en-gb/sports/football
            driver.get("http://188bet.com/en-gb/sports/1/select-competition/default");
            /*WebElement frame = driver.findElement(By.id("sbkFrame"));
            if (frame != null) {
                driver.switchTo().frame(frame);
            }*/
            Thread.sleep(500);
            String htmlSource = driver.getPageSource();
            Document leagues = Jsoup.parse(htmlSource);
            List<String> idS = new ArrayList<String>();
            Elements leaguesE = leagues.select("input.cpact.cp");
            int tryNumber = 0;
            while((leaguesE == null || leaguesE.isEmpty()) && tryNumber++ < 10) {
                Thread.sleep(500);
                htmlSource = driver.getPageSource();
                leagues = Jsoup.parse(htmlSource);
                leaguesE = leagues.select("input.cpact.cp");
            }
            if (leaguesE == null || leaguesE.isEmpty()) {
                logger.error("empty leagues");
                return events;
            }
            for (Element league: leagues.select("input")) {
                String id = league.attr("value");
                if (id != null && !id.isEmpty())
                    idS.add(id);
            }

            if (idS.isEmpty()) {
                logger.info("Bet188Parser idS empty!!!");
            }
            StringBuilder sbAllLeaguesLink = new StringBuilder();
            sbAllLeaguesLink.append("http://188bet.com/en-gb/sports/1/competition/full-time-asian-handicap-and-over-under?competitionids=");
            //http://sb.188bet.com/en-gb/sports/1/competition/full-time-asian-handicap-and-over-under?competitionids=26726,26766,27325,27487,30754
            for (String idLeague: idS) {
                sbAllLeaguesLink.append(idLeague).append(",");
            }
            driver.get(sbAllLeaguesLink.toString());
            Thread.sleep(5000);
            /*frame = driver.findElement(By.id("sbkFrame"));
            if (frame != null) {
                driver.switchTo().frame(frame);
            }*/
            Thread.sleep(5000);
            htmlSource = driver.getPageSource();
            Document matches = Jsoup.parse(htmlSource);
            Element sport = matches.getElementById("container1");
            tryNumber = 0;
            while (sport == null && tryNumber++ < 10) {
                Thread.sleep(500);
                htmlSource = driver.getPageSource();
                matches = Jsoup.parse(htmlSource);
                sport = matches.getElementById("container1");
            }
            if (sport == null) {
                logger.error("empty leagues sport");
                return events;
            }
            List<String> matchIds = new ArrayList<String>();
            for (Element league: sport.getElementsByClass("comp-container")) {
                for (Element match: league.select("tbody")) {
                    String notInclude = match.getElementsByClass("selection-txt").first().text();
                    if (!notInclude.contains("To Qualify")) {
                        String eventId = match.attr("id");
                        matchIds.add(eventId);
                    }
                }
            }
            if (matchIds.isEmpty()) {
                logger.info("Bet188Parser matchIds empty!!!");
            }
            returnWebDriverToPool(driver);
            driver = null;
            for (String matchId: matchIds) {
                final String linkMatch = "http://188bet.com/en-gb/sports/" + matchId.substring(1);
                //BasicEvent event = parseEvent(linkMatch);
                workers.add(new Bet188Downloader(this, linkMatch));
            }
            List<Future<List<BasicEvent>>> parserStates = new ArrayList<Future<List<BasicEvent>>>();
            for (Bet188Downloader tasks: workers) {
                parserStates.add(downloadersTaskPool.submit((Callable)tasks));
            }
            for (Future<List<BasicEvent>> f: parserStates) {
                try {
                        List<BasicEvent> event = f.get();
                        if (event != null) {
                            events.addAll(f.get());
                        }
                    f.cancel(true);
                } catch (Exception e) {
                    logger.error("processGetBets: future get() exception - ", e);
                    f.cancel(true);
                }
            }
            //downloadersTaskPool.shutdown();
        } catch (Exception e) {
            logger.error("Bet188Parser exception - ", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    private static class Bet188Downloader extends BaseDownloader {

        Bet188Parser parser;
        String link;

        public Bet188Downloader(Bet188Parser parser, String link) {
            this.parser = parser;
            this.link = link;
        }

        @Override
        public List<BasicEvent> download() {
            List<BasicEvent> events = new ArrayList<>();
            BasicEvent event = parser.parseEvent(link);
            if (event != null) {
                EventSender.sendEvent(event);
                events.add(event);
            }
            return events;
        }
    }

    private BasicEvent parseEvent(String link) {
        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            driver.get(link);
            /*WebElement frame = driver.findElement(By.id("sbkFrame"));
            if (frame != null) {
                driver.switchTo().frame(frame);
            }*/
            //Thread.sleep(500);
            String matchSource = driver.getPageSource();
            Document matchBets = Jsoup.parse(matchSource);
            Element matchInfo = matchBets.getElementsByClass("mb-match-info").first();
            int count = 0;
            while (matchInfo == null && count++ < 20) {
                Thread.sleep(200);
                matchSource = driver.getPageSource();
                matchBets = Jsoup.parse(matchSource);
                matchInfo = matchBets.getElementsByClass("mb-match-info").first();
            }
            if (matchInfo == null) {
                logger.error("Bet188Parser cant load match page - " + link);
                return null;
            }
            String team1 = matchInfo.getElementById("teamH").text().trim();
            String team2 = matchInfo.getElementById("teamA").text().trim();
            String time = matchInfo.getElementsByClass("mb-date").first().text().trim();
            BasicEvent event = new BasicEvent();
            event.site = BetSite.BET_188;
            event.link = link;
            event.team1 = Teams.getTeam(team1.trim());
            event.team2 = Teams.getTeam(team2.trim());
            if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                logger.warn("Bet188Parser - unknown teams: " + team1 + ":" + event.team1 + ";" + team2 + ":" + event.team2);
                return null;
            }
            event.date = time + " " + Calendar.getInstance().get(Calendar.YEAR);
            event.day = format.parse(event.date + Calendar.getInstance().get(Calendar.YEAR));
            Element oddsDiv = matchBets.getElementById("mb-div");
            List<Element> divTitles = oddsDiv.getElementsByClass("bettype-title");
            List<Element> oddsTable = oddsDiv.select("table");
            for (int i = 0; i < divTitles.size(); i++) {
                final String oddsType = divTitles.get(i).getElementsByClass("bettype-txt").first().text().trim();
                if (oddsType.equals("Asian Handicap")) {
                    for (Element line : oddsTable.get(i + 1).select("tr")) {
                        Elements homeAway = line.select("td");
                        if (homeAway.size() == 2) {
                            String f1OddsVal = homeAway.get(0).getElementsByClass("odds").first().text().trim();
                            String f1OddsTypeUnparsed = homeAway.get(0).getElementsByClass("ou").first().text().trim();
                            BetType f1Bet = BetDataMapping.betsTotal.get(getOverUnder(f1OddsTypeUnparsed) + "F1");
                            if (f1Bet != null) {
                                event.bets.put(f1Bet, f1OddsVal);
                            }
                            String f2OddsVal = homeAway.get(1).getElementsByClass("odds").first().text().trim();
                            String f2OddsTypeUnparsed = homeAway.get(1).getElementsByClass("ou").first().text().trim();
                            BetType f2Bet = BetDataMapping.betsTotal.get(getOverUnder(f2OddsTypeUnparsed) + "F2");
                            if (f2Bet != null) {
                                event.bets.put(f2Bet, f2OddsVal);
                            }
                        }
                    }
                } else if (oddsType.equals("Goals - Over / Under")) {
                    for (Element line : oddsTable.get(i + 1).select("tr")) {
                        Elements overUnder = line.select("td");
                        if (overUnder.size() == 2) {
                            String tMOddsVal = overUnder.get(0).getElementsByClass("odds").first().text().trim();
                            String tMOddsTypeUnparsed = overUnder.get(0).getElementsByClass("ou").first().text().trim();
                            BetType tMBet = BetDataMapping.betsTotal.get(getOverUnder(tMOddsTypeUnparsed) + "M");
                            if (tMBet != null) {
                                event.bets.put(tMBet, tMOddsVal);
                            }
                            String tLOddsVal = overUnder.get(1).getElementsByClass("odds").first().text().trim();
                            String tLOddsTypeUnparsed = overUnder.get(1).getElementsByClass("ou").first().text().trim();
                            BetType tLBet = BetDataMapping.betsTotal.get(getOverUnder(tLOddsTypeUnparsed) + "L");
                            if (tLBet != null) {
                                event.bets.put(tLBet, tLOddsVal);
                            }
                        }
                    }
                } else if (oddsType.equals("1 X 2")) {
                    Element pLine = oddsTable.get(i + 1).select("tr").first();
                    Elements pOdds = pLine.select("td");
                    if (pOdds.size() == 3) {
                        String P1Odds = pOdds.get(0).getElementsByClass("odds").first().text().trim();
                        event.bets.put(BetType.P1, P1Odds);
                        String PXOdds = pOdds.get(1).getElementsByClass("odds").first().text().trim();
                        event.bets.put(BetType.X, PXOdds);
                        String P2Odds = pOdds.get(2).getElementsByClass("odds").first().text().trim();
                        event.bets.put(BetType.P2, P2Odds);
                    }
                } else if (oddsType.equals("Double Chance")) {
                    Elements xOdds = oddsTable.get(i + 1).select("tr");
                    if (xOdds.size() == 3) {
                        String P1xOdds = xOdds.get(0).getElementsByClass("odds").first().text().trim();
                        event.bets.put(BetType.P1X, P1xOdds);
                        String P12Odds = xOdds.get(1).getElementsByClass("odds").first().text().trim();
                        event.bets.put(BetType.P12, P12Odds);
                        String P2XOdds = xOdds.get(2).getElementsByClass("odds").first().text().trim();
                        event.bets.put(BetType.P2X, P2XOdds);
                    }
                } /*else if (oddsType.equals("Correct Score")) {
                        Elements oddsScore = oddsTable.get(i).getElementsByClass("has-odds");
                        for (Element odds: oddsScore) {
                            String score = odds.getElementsByClass("mb-selection").first().text().trim();
                            score = score.replace(" - ", ":");
                            String oddsVal = odds.getElementsByClass("odds").first().text().trim();
                            Bet scoreBet = BetDataMapping.totalScore.get(score);
                            if (scoreBet != null) {
                                event.bets.put(scoreBet, oddsVal);
                            }
                        }
                    }*/
            }
            return event;
        } catch (Exception e) {
            logger.error("Bet188Parser", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
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
            logger.error("Bet188Parser asianTotalType exception - ", e);
        }
        return "UNK";
    }

}
