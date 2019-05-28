package com.isfootball.pool;

import com.google.common.collect.ImmutableMap;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.filters.ResponseFilter;
import net.lightbody.bmp.proxy.CaptureType;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Evgeniy Pshenitsin on 15.04.2015.
 */
public class ChromeFactory extends BaseObjectFactory<WebDriver> {

    private static final Logger logger = LogManager.getLogger("parser");

    static {
        //System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
        //System.setProperty("webdriver.chrome.driver", "/opt/chromedriver/chromedriver");
    }

    private String port;

    public ChromeFactory() {
    }

    public ChromeFactory(String port) {
        this.port = port;
    }


    @Override
    public WebDriver create() {
        WebDriver driver = null;

        try {
            ChromeDriverService service = new ChromeDriverService.Builder()
                    //.usingDriverExecutable(new File("C:\\chromedriver\\chromedriver.exe"))
                    .usingDriverExecutable(new File("/opt/chromedriver/chromedriver"))
                    //.usingDriverExecutable(new File("/usr/lib/chromium/chromedriver"))
                    .usingAnyFreePort()
                            .withVerbose(true)
                    //.usingDriverExecutable(new File("/usr/bin/chromium"))
                    .withEnvironment(ImmutableMap.of("DISPLAY", ":1"))
                    .build();
            service.start();
            //src.start();

            Map<String, Object> contentSettings = new HashMap<String, Object>();
            contentSettings.put("images", 2);

            Map<String, Object> preferences = new HashMap<String, Object>();
            preferences.put("profile.default_content_settings", contentSettings);

            //DesiredCapabilities caps = DesiredCapabilities.chrome();
            //caps.setCapability("chrome.prefs", preferences);
            /*BrowserMobProxy proxy = new BrowserMobProxyServer();
            proxy.start(0);
            HashSet<CaptureType> disable = new HashSet<CaptureType>();
            disable.add(CaptureType.RESPONSE_BINARY_CONTENT);
            proxy.disableHarCaptureTypes(disable);

            Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
*/
            DesiredCapabilities capabilities = new DesiredCapabilities();
            //capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
            capabilities.setCapability("chrome.switches", "disable-images");

            ChromeOptions options=new ChromeOptions();
            options.addArguments("--disable-images");
            options.addArguments("--lang=ru");
            options.addArguments("user-agent=Mozilla/5.0 (Windows NT 6.3; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");

            //options.addArguments("--start-maximized", "--ignore-certificate-errors", "--disable-images");
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            //--disable-images

            //options.setBinary("/usr/bin/chromium");
            //options.setExperimentalOption("prefs", preferences);
            //options.setExperimentalOption("profile.default_content_settings", "2");
            //options.addArguments("--disable-images");
            //ChromeOptions.CAPABILITY
            //options.addArguments("--proxy-server=socks5://" + "127.0.0.1" + ":" + 8092);
            if (port != null) {
                logger.info("ChromeFactory use port: " + port);
                options.addArguments("--proxy-server=socks5://" + "127.0.0.1" + ":" + port);
            }
            //if (driverOptions.has(PROXY))   options.addArguments("--proxy-server=http://" + driverOptions.get(PROXY));
            driver = new ChromeDriver(service,options);
            //driver = new ChromeDriver(service,capabilities);
            driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            //driver.manage().window().setSize(new Dimension(1920, 1080));
            //driver.manage().window().maximize();
            driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.fatal("ChromeFactory create error!!!", e);
        }
        logger.info("ChromeFactory created obj");
        return driver;
    }

    @Override
    public void beforeFree(WebDriver obj) {
        logger.info("beforeFree() - about:blank");
        try {
            obj.get("about:blank");
        } catch (Exception e) {
            logger.error("beforeFree() error", e);
        }
    }

    @Override
    public void destroy(WebDriver obj) {
        logger.info("ChromeFactory destroyObject obj");
        try {
            obj.quit();
        } catch (Exception e) {
            logger.error("ChromeFactory destroyObject error", e);
        }
    }

    @Override
    public void destroyAll() {
        try {
            logger.info("destroyAll - pkill chromedriver");
            Process proc = Runtime.getRuntime().exec("pkill chromedriver");
            Runtime.getRuntime().exec("pkill chromium");
            //Runtime.getRuntime().exec("swapoff -a");
            //Runtime.getRuntime().exec("swapon -a");
        } catch (Exception e) {
            logger.error("destroyAll - pkill chromedriver exceptione", e);
        }
    }
}