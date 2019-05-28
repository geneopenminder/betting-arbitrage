package com.isfootball.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Evgeniy Pshenitsin on 21.03.2015.
 */
public class PhantomJSFactory extends BaseObjectFactory<WebDriver> {

    private static final Logger logger = LogManager.getLogger("parser");

    static public DesiredCapabilities DesireCaps;

    static {
        DesireCaps = new DesiredCapabilities();
        //DesireCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/opt/phantomjs/bin/phantomjs");
        DesireCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "C:\\webdriver\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");

        String [] phantomJsArgs = {"--ignore-ssl-errors=true --ssl-protocol=any --load-images=false"};
        DesireCaps.setCapability(
                PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS,
                phantomJsArgs);

        String userAgent = "Mozilla/5.0 (Windows NT 6.0) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.41 Safari/535.1";
        System.setProperty("phantomjs.page.settings.userAgent", userAgent);
        DesireCaps.setCapability("phantomjs.page.settings.resourceTimeout", "60000");
        DesireCaps.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0");
        ArrayList<String> cliArgsCap = new ArrayList<String>();
        cliArgsCap.add("--proxy=127.0.0.1:5555");
        cliArgsCap.add("--proxy-type=socks5");
        DesireCaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
    }

    @Override
    public WebDriver create() {
        WebDriver driver = null;

        try {
            driver = new PhantomJSDriver(DesireCaps);
            driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            //driver.manage().window().setSize(new Dimension(1920, 1080));
            //driver.manage().window().maximize();
            driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.fatal("PhantomJSDriver create error!!!", e);
        }
        logger.info("WebDriver created obj");
        return driver;
    }

    @Override
    public void beforeFree(WebDriver obj) {
        logger.info("beforeFree() - about:blank");
        obj.get("about:blank");
    };

    @Override
    public void destroy(WebDriver obj) {
        logger.info("WebDriver destroyObject obj");
        obj.quit();
    }

    @Override
    public void destroyAll() {
        try {
            logger.info("destroyAll - pkill phantomjs");
            Process proc = Runtime.getRuntime().exec("pkill phantomjs");
        } catch (Exception e) {
            logger.error("destroyAll - pkill phantomjs exceptione", e);
        }
    }
}
