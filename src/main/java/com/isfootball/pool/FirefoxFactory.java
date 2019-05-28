package com.isfootball.pool;

import com.google.common.collect.ImmutableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Evgeniy Pshenitsin on 15.04.2015.
 */
public class FirefoxFactory extends BaseObjectFactory<WebDriver> {

    private static final Logger logger = LogManager.getLogger("parser");

    static {
        //System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
        //System.setProperty("webdriver.chrome.driver", "/opt/chromedriver/chromedriver");
    }

    private String port;

    public FirefoxFactory(String port) {
        this.port = port;
    }

    public FirefoxFactory() {
    }

    @Override
    public WebDriver create() {
        WebDriver driver = null;

        try {
            //System.setProperty("webdriver.firefox.profile", "webdriver");
            //FirefoxBinary ffox = new FirefoxBinary(new File("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe"));
            FirefoxBinary ffox = new FirefoxBinary(new File("/usr/bin/iceweasel"));
            ffox.setEnvironmentProperty("DISPLAY", ":1");

            FirefoxProfile ffProfile=new FirefoxProfile();
            ffProfile.setPreference("permissions.default.image", 2);
            ffProfile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so", false);


            if (port != null) {

                String PROXY = "127.0.0.1:" + port;

                org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
                proxy.setHttpProxy(PROXY)
                        .setFtpProxy(PROXY)
                        .setSocksProxy(PROXY);
                DesiredCapabilities cap = new DesiredCapabilities();
                cap.setCapability(CapabilityType.PROXY, proxy);
                driver = new FirefoxDriver(ffox, ffProfile, cap);
            } else {
                driver = new FirefoxDriver(ffox, ffProfile);
            }

            //driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            //driver.manage().window().setSize(new Dimension(1920, 1080));
            //driver.manage().window().maximize();
            //driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
            //driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.fatal("FirefoxFactory create error!!!", e);
        }
        logger.info("FirefoxFactory created obj");
        return driver;
    }

    @Override
    public void beforeFree(WebDriver obj) {
        logger.info("beforeFree() - about:blank");
        obj.get("about:blank");
    }

    @Override
    public void destroy(WebDriver obj) {
        logger.info("FirefoxFactory destroyObject obj");
        obj.quit();
    }

    @Override
    public void destroyAll() {
        try {
            logger.info("destroyAll - pkill firefox");
            Process proc = Runtime.getRuntime().exec("pkill firefox");
            //Runtime.getRuntime().exec("swapoff -a");
            //Runtime.getRuntime().exec("swapon -a");
        } catch (Exception e) {
            logger.error("destroyAll - pkill firefox exceptione", e);
        }
    }
}