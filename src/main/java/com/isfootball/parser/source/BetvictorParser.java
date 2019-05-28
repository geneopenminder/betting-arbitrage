package com.isfootball.parser.source;

import com.google.gson.Gson;
import com.isfootball.model.BasicEvent;
import com.isfootball.parser.BaseParser;
import com.isfootball.parser.BetSite;
import com.isfootball.pool.BasePoolInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Evgeniy Pshenitsin on 01.04.2015.
 */
public class BetvictorParser extends BaseParser {

    private static final Logger logger = LogManager.getLogger("parser");

    private static Gson gson = new Gson();

    //2015-04-03
    static DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.GERMAN);
    //yyyy-MM-dd

    public BetvictorParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.BETVICTOR;
    }

    @Override
    public List<BasicEvent> getEvents() {
        List<BasicEvent> events = new ArrayList<BasicEvent>();

        WebDriver driver = null;

        //List<Bet188Downloader> workers = new ArrayList<Bet188Downloader>();
        try {
            driver = getWebDriverFromPool();
            driver.get("http://www.betvictor.com/sports/en/football");
            Thread.sleep(10000);
            String htmlSource = driver.getPageSource();
            Document doc = Jsoup.parse(htmlSource);
            returnWebDriverToPool(driver);
            driver = null;
        } catch (Exception e){
            logger.error("BetvictorParser error", e);
        } finally {
            if (driver != null) {
                returnWebDriverToPool(driver);
            }
        }
        return events;
    }
}
