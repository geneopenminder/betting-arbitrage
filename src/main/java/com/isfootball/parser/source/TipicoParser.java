package com.isfootball.parser.source;

import com.isfootball.model.BasicEvent;
import com.isfootball.parser.*;
import com.isfootball.pool.BasePoolInterface;
import com.isfootball.processing.EventSender;
import com.isfootball.utils.HttpClientInitializer;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Evgeniy Pshenitsin on 26.04.2015.
 */
public class TipicoParser extends BaseParser {


    private static final Logger logger = LogManager.getLogger("parser");

    //2015-04-26
    static DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    ExecutorService downloadersTaskPool = Executors.newFixedThreadPool(10, getThreadFactory(TipicoParser.class.getSimpleName()));

    public TipicoParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.TIPICO;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();

        //List<Bet188Downloader> workers = new ArrayList<Bet188Downloader>();
        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            driver.get("https://www.tipico.com/en/online-sports-betting/");
            Thread.sleep(7000);
            driver.findElement(By.partialLinkText("Football")).click();
            Thread.sleep(7000);
            driver.findElement(By.id("jq-further-1101")).click();
            Thread.sleep(7000);
            Document doc = Jsoup.parse(driver.getPageSource());
            Elements items = doc.select("a.nav_2");
            List<String> leaguesHrefs = new ArrayList<String>();
            for (Element item: items) {
                if (item.attr("href") != null && item.attr("href").contains("football")) {
                    leaguesHrefs.add(item.attr("href"));
                }
            }

            //
            for (String link: leaguesHrefs) {
                final String linkFinal = "https://www.tipico.com" + link;
                driver.get(linkFinal);
                Thread.sleep(2000);
                Document leagueDoc = Jsoup.parse(driver.getPageSource());
                //e_active t_row jq-event-row-cont
                Elements matches = leagueDoc.select("div.e_active.t_row.jq-event-row-cont");
                for (Element match: matches) {
                    Elements metas = match.select("meta");
                    String[] teams = metas.get(0).attr("content").split(" - ");
                    if (teams.length == 2) {
                        String team1 = teams[0].trim();
                        String team2 = teams[1].trim();
                        BasicEvent event = new BasicEvent();
                        event.team1 = Teams.getTeam(team1);
                        event.team2 = Teams.getTeam(team2);
                        if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                            logger.warn("TipicoParser - unknown teams: " + team1 + ":" + event.team1 + ";" + team2 + ":" + event.team2);
                            continue;
                        }
                        //check live
                        Element aLive = match.select("div.pulsation").first();
                        if (aLive != null) {
                            //LIVE
                            continue;
                        }
                        event.link = linkFinal;
                        event.site = BetSite.TIPICO;
                        for (Element meta: metas) {
                            if (meta.attr("itemprop") != null && meta.attr("itemprop").equalsIgnoreCase("startDate")) {
                                event.date = meta.attr("content").trim();
                                event.day = format.parse(event.date.substring(0, 10));
                                break;
                            }
                        }
                        if (event.day == null) {
                            continue;
                        }
                        Element mainOdds = match.select("div.bl.br.left.cf").first();
                        if (mainOdds == null) {
                            mainOdds = match.select("div.bl.pad_2.left ").first();
                        }
                        if (mainOdds != null) {
                            Elements odds;
                            if (mainOdds.select("div.multi_row").first() != null) {
                                odds = mainOdds.select("div.multi_row").first().select("div");
                            } else {
                                odds = mainOdds.select("div.qbut");
                            }
                            if (odds.size() == 3) {
                                String p1 = odds.get(0).text().replace(",", ".").trim();
                                event.bets.put(BetType.P1, p1);
                                String x = odds.get(1).text().replace(",", ".").trim();
                                event.bets.put(BetType.X, x);
                                String p2 = odds.get(2).text().replace(",", ".").trim();
                                event.bets.put(BetType.P2, p2);
                            }
                        }
                        Element limitsHover = match.select("div.t_more.bl.align_c.right").first();
                        if (limitsHover != null) {
                            /*String onclick = limitsHover.attr("onclick");
                            WebElement click = driver.findElement(By.xpath("//div[@onclick='" + onclick + "']"));
                            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", click);
                            Thread.sleep(1000);
                            click.click();
                            Thread.sleep(2000);
                            driver.findElement(By.xpath("//div[@onclick='" + onclick + "']")).click();
                            Thread.sleep(2000);*/
                            //jq-special-bet-layer-category-popup
                            Element special = match.select("div.e_active.jq-special-bet-layer-category-popup.e_comp_ref").first();
                            if (special != null) {
                                String id = special.attr("e:url").split("-")[1].replace("/", "").trim();
                                String[] parts = linkFinal.split("/");
                                parseOdds(event, id, parts[parts.length - 1]);
                            }
                        }
                        events.add(event);
                    }
                }
            }
        } catch (UnreachableBrowserException e) {
            logger.error("TipicoParser worker error - UnreachableBrowserException", e);
            recreateDiedWebDriver(driver);
            driver = null;
        } catch (Exception e) {
            logger.error("TipicoParser exception - ", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }

    private void parseOdds(BasicEvent event, String matchId, String league) {
        final String postUrl = "https://www.tipico.com/spring/complete/www_tipico_com_en_online_sports_betting_football_" + league + "_30";
        try {
            HttpClient httpClient = new HttpClient(HttpClientInitializer.getConnectionManager());
            PostMethod post = new PostMethod(postUrl);
            //_gat
            //de_INT
            post.addRequestHeader(new Header("Cookie", "cls=en_INT"));
            StringRequestEntity requestEntity = new StringRequestEntity(
                    "_=@@sbLayer-" + matchId +"/program/specialBet/specialBetLayer3%253Factive%253Dfalse%2526eventId%253D" + matchId +
                            "%2526excludedResultSetIds%253D2009155910%2526topEventLayer%253Dfalse","application/x-www-form-urlencoded", "UTF-8");
            post.setRequestEntity(requestEntity);
            int returnCode = httpClient.executeMethod(post);
            if (returnCode == HttpStatus.SC_OK) {
                String ret = IOUtils.toString(new InputStreamReader(post.getResponseBodyAsStream(), "UTF-8"));
                StringBuilder html = new StringBuilder();
                html.append("<!DOCTYPE html><html><body>");
                html.append(ret);
                html.append("</body></html>");
                Document doc = Jsoup.parse(html.toString());
                Elements bets = doc.select("div.e_active.row.t_more_row.info");
                for (Element bet: bets) {
                    String betType = bet.select("div.left").get(1).select("span").first().text().trim();
                    if (betType.equalsIgnoreCase("Double chance")) {
                        Elements xOdds = bet.select("div.t_more_cell.cf");
                        if (xOdds.size() == 3) {
                            String p1x = xOdds.get(0).select("div.qbut").first().text().trim();
                            if (!p1x.isEmpty()) {
                                event.bets.put(BetType.P1X, p1x.replace(",","."));
                            }
                            String p12 = xOdds.get(1).select("div.qbut").first().text().trim();
                            if (!p12.isEmpty()) {
                                event.bets.put(BetType.P12, p12.replace(",","."));
                            }
                            String p2x = xOdds.get(2).select("div.qbut").first().text().trim();
                            if (!p2x.isEmpty()) {
                                event.bets.put(BetType.P2X, p2x.replace(",","."));
                            }
                        }
                    } else if (betType.contains("Over/Under bet") && !betType.contains("half")) {
                        String total = betType.substring(betType.indexOf("(") + 1, betType.indexOf(")")).replace(",", ".").trim();
                        BetType totalL = BetDataMapping.betsTotal.get(total + "L");
                        BetType totalM = BetDataMapping.betsTotal.get(total + "M");
                        Elements totalOdds = bet.select("div.t_more_cell.cf");
                        if (totalOdds.size() == 2) {
                            String over = totalOdds.get(0).select("div.qbut").first().text().trim();
                            if (!over.isEmpty()&& totalM != null) {
                                event.bets.put(totalM, over.replace(",", "."));
                            }
                            String under = totalOdds.get(1).select("div.qbut").first().text().trim();
                            if (!under.isEmpty()&& totalL != null) {
                                event.bets.put(totalL, under.replace(",", "."));
                            }
                        }
                    } else if (betType.equalsIgnoreCase("Draw no bet")) {
                        Elements foras = bet.select("div.t_more_cell.cf");
                        if (foras.size() == 2) {
                            String f1 = foras.get(0).select("div.qbut").first().text().trim();
                            if (!f1.isEmpty()) {
                                event.bets.put(BetType.F10, f1.replace(",", "."));
                            }
                            String f2 = foras.get(1).select("div.qbut").first().text().trim();
                            if (!f2.isEmpty()) {
                                event.bets.put(BetType.F20, f2.replace(",", "."));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("TipicoParser parseOdds error", e);
        }
    }

}
