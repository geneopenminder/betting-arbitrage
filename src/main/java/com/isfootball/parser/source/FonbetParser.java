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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Evgeniy Pshenitsin on 06.08.2016.
 */
public class FonbetParser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    Map<String, String> monthes = new HashMap<String, String>() {{
        put("января", "01");
        put("февраля", "02");
        put("марта", "03");
        put("апреля", "04");
        put("мая", "05");
        put("июня", "06");
        put("июля", "07");
        put("августа", "08");
        put("сентября", "09");
        put("октября", "10");
        put("ноября", "11");
        put("декабря", "12");
    }};

    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    public FonbetParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();
        List<String> links = new ArrayList<String>();
        WebDriver driver = null;
        try {
            driver = getWebDriverFromPool();
            driver.get("http://www.bkfon.ru/ru/line/");
            Thread.sleep(3000);
            List<WebElement> frames = driver.findElements(By.tagName("iframe"));
            if (frames.size() > 1) {
                driver.switchTo().frame(frames.get(frames.size() - 1));
            }
            String page = driver.getPageSource();
            returnWebDriverToPool(driver);
            driver = null;

            Document doc = Jsoup.parse(page);

            Element mLine = doc.getElementById("lineTable");
            List<Element> lines = mLine.select("tr");
            boolean isFootballLine = false;
            for (Element line: lines) {
                if (line.hasClass("trSegment")) {
                    final String lName = line.select("tr.trSegment").text();
                    if (lName.contains("Футбол")) {
                        if (lName.contains("17") ||
                                lName.contains("19") ||
                                lName.contains("Жен.")) {
                            continue;
                        }
                        isFootballLine = true;
                    } else {
                        break;
                    }
                }

                if(isFootballLine && line.hasClass("trEvent")) {
                    BasicEvent event = new BasicEvent();
                    String teamsLine = line.getElementsByClass("eventCellName").first().select("div.event").text();
                    String[] teams = teamsLine.substring(line.getElementsByClass("eventCellName").first().select("span.eventNumber").text().length()).split("—");
                    event.team1 = Teams.getTeam(teams[0].trim());
                    event.team2 = Teams.getTeam(teams[1].trim());
                    if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                        logger.warn("FonbetParser - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                        continue;
                    }
                    Date date = new Date();
                    String evtTime = line.getElementsByClass("eventTime").text();
                    if (evtTime.contains("Сегодня")) {
                        date = new Date();
                    } else if (evtTime.contains("Завтра")) {
                        DateTime dt = new DateTime().plusDays(1);
                        date = dt.toDate();
                    } else {
                        String dayStr = evtTime.split(" ")[0].toLowerCase().trim();
                        if (dayStr.length() == 1) {
                            dayStr = "0" + dayStr;
                        }
                        String monthNum = monthes.get(evtTime.split(" ")[1].toLowerCase().trim());

                        String dateStr = dayStr + "-" + monthNum + "-" + new DateTime().getYear();
                        date = DateFormatter.format(dateStr, dtf);

                    }

                    event.day = date;
                    event.date = evtTime;
                    event.site = BetSite.FONEBET;


                    List<Element> cells = line.select("td.eventCellValue");
                    if (cells.size() == 10) {
                        event.bets.put(BetType.P1, cells.get(0).text().replace("\u00A0", "").trim());
                        event.bets.put(BetType.X, cells.get(1).text().replace("\u00A0", "").trim());
                        event.bets.put(BetType.P2, cells.get(2).text().replace("\u00A0", "").trim());
                        event.bets.put(BetType.P1X, cells.get(3).text().replace("\u00A0", "").trim());
                        event.bets.put(BetType.P12, cells.get(4).text().replace("\u00A0", "").trim());
                        event.bets.put(BetType.P2X, cells.get(5).text().replace("\u00A0","").trim());
                        events.add(event);
                        EventSender.sendEvent(event);

                    }





                }


            }
        } catch (Exception e) {
            logger.error("FonbetParser exception - ", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }


}
