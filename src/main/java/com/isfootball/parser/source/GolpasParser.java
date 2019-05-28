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
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Evgeniy Pshenitsin on 11.05.2015.
 */
public class GolpasParser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    //11.05.2015
    static DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

    //ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(10, getThreadFactory(Bet188Parser.class.getSimpleName()));

    public GolpasParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.GOLPAS;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();

        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            driver.get("http://www.golpas.com/m/account/line/?&f[tg][]=1");
            /*Document doc = Jsoup.connect("http://www.golpas.com/m/account/line/?&f[tg][]=1")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                    .timeout(10 * 1000).get();*/
            Document doc = Jsoup.parse(driver.getPageSource());
            Elements aaa = doc.select("a.ui-btn.ui-btn-icon-right.ui-icon-carat-r");
            Set<String> matches = new HashSet<String>();
            for (Element a: aaa) {
               if (!a.attr("href").isEmpty()) {
                    String countryLink = "http://www.golpas.com" + a.attr("href");
                    driver.get(countryLink);
                    /*Document docCountry = Jsoup.connect(countryLink)
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
                            .timeout(10 * 1000).get();*/
                    Document docCountry = Jsoup.parse(driver.getPageSource());
                    Elements leaguesHrefs = docCountry.select("a.ui-btn.ui-btn-icon-right.ui-icon-carat-r");
                    for (Element leagueHref: leaguesHrefs) {
                        if (leagueHref.select("span.ui-body-inherit.ui-li-date").first() != null) {
                            matches.add(leagueHref.attr("href"));
                        } else if (!leagueHref.attr("href").isEmpty() && !leagueHref.attr("href").equalsIgnoreCase("/m/account/line/?&f[tg][]=1")) {
                            parseLeague(driver, matches, leagueHref.attr("href"));
                        }
                    }
                }
            }
            for (String match: matches) {
                BasicEvent event = parseMatch(driver, match);
                if (event != null) {
                    events.add(event);
                }
            }
        } catch (UnreachableBrowserException e) {
            logger.error("GolpasParser worker error - UnreachableBrowserException", e);
            recreateDiedWebDriver(driver);
            driver = null;

        } catch (Exception e) {
            logger.error("GolpasParser exception - ", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    private void parseLeague(WebDriver driver, Set<String> matches, String link) {
        String league = "http://www.golpas.com" + link;
        driver.get(league);
        Document leagueDoc = Jsoup.parse(driver.getPageSource());
        Elements matchesA = leagueDoc.select("a.ui-btn.ui-btn-icon-right.ui-icon-carat-r");
        for (Element match: matchesA) {
            if (match.select("span.ui-body-inherit.ui-li-date").first() != null) {
                matches.add(match.attr("href"));
                //parseMatch(driver, match.attr("href")); //TODO
            }
        }
    }

    private BasicEvent parseMatch(WebDriver driver, String link) {
        final String machLink = "http://www.golpas.com" + link;
        BasicEvent  event = new BasicEvent();
        driver.get(machLink);
        Document matchDoc = Jsoup.parse(driver.getPageSource());
        try {
            Element data = matchDoc.select("div.data").first();
            if (data != null) {
                String[] teams = data.select("h2").first().text().split(" - ");
                event.team1 = Teams.getTeam(teams[0].trim());
                event.team2 = Teams.getTeam(teams[1].trim());
                if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                    logger.warn("GolpasParser - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                    return null;
                }
                event.site = BetSite.GOLPAS;
                event.link = machLink;
                event.date = data.select("b").first().text().trim().substring(0,5) + "." + Calendar.getInstance().get(Calendar.YEAR);
                event.day = format.parse(event.date);
                Elements bets = data.select("div.coefs").first().select("a.wrap.ui-link.ui-btn.ui-btn-inline");
                Double f1Main = null;
                Double f2Main = null;
                Double totalMain = null;
                Elements mainOddsK = data.select("span.attach");
                if (mainOddsK.size() > 3) {
                    f1Main = Double.parseDouble(mainOddsK.get(0).select("span.green").first().text().trim());
                    f2Main = Double.parseDouble(mainOddsK.get(1).select("span.green").first().text().trim());
                    totalMain = Double.parseDouble(mainOddsK.get(2).select("span.green").first().text().trim());
                }
                /*for (Element mainOdd: mainOddsK) {
                    if (mainOdd.hasAttr("pos")) {
                        String pos = mainOdd.attr("pos");
                        if (pos.equalsIgnoreCase("6")) {
                            f1Main = Double.parseDouble(mainOdd.select("span.green").first().text().trim());
                        } else if (pos.equalsIgnoreCase("8")) {
                            f2Main = Double.parseDouble(mainOdd.select("span.green").first().text().trim());
                        } else if (pos.equalsIgnoreCase("10")) {
                            totalMain = Double.parseDouble(mainOdd.select("span.green").first().text().trim());
                        }
                    }
                }*/
                for (Element betLink: bets) {
                    String betType = betLink.select("span.coef_name").first().text().trim();
                    String betK =  betLink.select("span.nobold.green").first().text().trim();
                    if (!betType.isEmpty() && !betK.isEmpty()) {
                        BetType bet = null;
                        if (betType.equalsIgnoreCase("п1")) {
                            bet = BetType.P1;
                        } else if (betType.equalsIgnoreCase("Х")) {
                            bet = BetType.X;
                        } else if (betType.equalsIgnoreCase("п2")) {
                            bet = BetType.P2;
                        } else if (betType.equalsIgnoreCase("1Х")) {
                            bet = BetType.P1X;
                        } else if (betType.equalsIgnoreCase("12")) {
                            bet = BetType.P12;
                        } else if (betType.equalsIgnoreCase("Х2")) {
                            bet = BetType.P2X;
                        } else if (betType.equalsIgnoreCase("КФ1")) {
                            bet = BetDataMapping.betsTotal.get(f1Main + "F1");
                        } else if (betType.equalsIgnoreCase("КФ2")) {
                            bet = BetDataMapping.betsTotal.get(f2Main + "F2");
                        } else if (betType.equalsIgnoreCase("Б")) {
                            bet = BetDataMapping.betsTotal.get(totalMain + "M");
                        } else if (betType.equalsIgnoreCase("М")) {
                            bet = BetDataMapping.betsTotal.get(totalMain + "L");
                        }
                        if (bet != null) {
                            event.bets.put(bet, betK);
                        }
                    }
                }
                try {
                    Elements addOdds = data.select("div.ui-collapsible-no-padding.ui-collapsible.ui-collapsible-inset.ui-corner-all.ui-collapsible-themed-content");
                    for (Element more : addOdds) {
                        String text = more.select("h4").first().select("span.wrap").first().text().trim();
                        if (text.equalsIgnoreCase("Дополнительные коэффициенты")) {
                            Elements names = more.select("div.ui-collapsible-content.ui-body-inherit").first().select("div.ui-bar.ui-bar-b");
                            Elements odds = more.select("div.ui-collapsible-content.ui-body-inherit").first().select("div.coefs.ui-body.ui-body-a");
                            if (names.size() == (odds.size() - 1)) {
                                for (int i = 0; i < names.size(); i++) {
                                    String name = names.get(i).text().trim();
                                    if (name.equalsIgnoreCase("Фора с покупкой команда1")) {
                                        //f1
                                        Elements f1Vals = odds.get(i).select("a.wrap.ui-link.ui-btn");
                                        for (Element f1Val : f1Vals) {
                                            String betType = f1Val.select("span.coef_name").first().text().trim();
                                            String betK = f1Val.select("span.nobold.green").first().text().trim();
                                            if (!betType.isEmpty() && !betK.isEmpty()) {
                                                Double f1Final = Double.parseDouble(betType.replace("+", "").replace(",", ".")) + f1Main;
                                                BetType f1Bet = BetDataMapping.betsTotal.get(f1Final + "F1");
                                                if (f1Bet != null) {
                                                    event.bets.put(f1Bet, betK);
                                                }
                                            }
                                        }
                                    } else if (name.equalsIgnoreCase("Фора с покупкой команда2")) {
                                        //F2
                                        Elements f2Vals = odds.get(i).select("a.wrap.ui-link.ui-btn");
                                        for (Element f2Val : f2Vals) {
                                            String betType = f2Val.select("span.coef_name").first().text().trim();
                                            String betK = f2Val.select("span.nobold.green").first().text().trim();
                                            if (!betType.isEmpty() && !betK.isEmpty()) {
                                                Double f2Final = Double.parseDouble(betType.replace("+", "").replace(",", ".")) + f2Main;
                                                BetType f2Bet = BetDataMapping.betsTotal.get(f2Final + "F2");
                                                if (f2Bet != null) {
                                                    event.bets.put(f2Bet, betK);
                                                }
                                            }
                                        }
                                    } else if (name.equalsIgnoreCase("Тотал с покупкой")) {
                                        //total
                                        Elements totalVals = odds.get(i).select("a.wrap.ui-link.ui-btn");
                                        for (Element total : totalVals) {
                                            String betType = total.select("span.coef_name").first().text().trim();
                                            String betK = total.select("span.nobold.green").first().text().trim();
                                            if (!betType.isEmpty() && !betK.isEmpty()) {
                                                String ML = "";
                                                if (betType.startsWith("Б")) {
                                                    ML = "M";
                                                } else if (betType.startsWith("М")) {
                                                    ML = "L";
                                                }
                                                Double totalFinal = Double.parseDouble(betType.substring(1).replace("+", "").replace(",", ".")) + totalMain;
                                                betType =  totalFinal + ML;
                                                BetType totalBet = BetDataMapping.betsTotal.get(betType);
                                                if (totalBet != null) {
                                                    event.bets.put(totalBet, betK);
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("GolpasParser more odds error link - " + link, e);
                }
                EventSender.sendEvent(event);
                return event;
            }
        } catch (Exception e) {
            logger.error("GolpasParser parseMatch error for link - " + machLink, e);
        }
        return null;
    }
}
