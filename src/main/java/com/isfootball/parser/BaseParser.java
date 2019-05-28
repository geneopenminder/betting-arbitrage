package com.isfootball.parser;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.isfootball.jdbc.*;
import com.isfootball.model.BasicEvent;
import com.isfootball.pool.BasePoolInterface;
import com.isfootball.processing.EventSender;
import com.isfootball.processing.SiteUpdateProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Evgeniy Pshenitsin on 05.01.2015.
 */
public abstract class BaseParser implements Callable<List<BasicEvent>> {

    private static final Logger logger = LogManager.getLogger("parser");

    public abstract List<BasicEvent> getEvents();

    protected BasePoolInterface<WebDriver> pool;

    protected BetSite betSite;

    public BaseParser(BasePoolInterface<WebDriver> pool) {
        this.pool = pool;
    }

    protected WebDriver getWebDriverFromPool() throws Exception {
        return pool.take();
    }

    protected void returnWebDriverToPool(WebDriver obj) {
        try {
            pool.free(obj);
        } catch (Exception e) {
            logger.fatal("Can't back object to pool", e);
        }
    }

    protected void recreateDiedWebDriver(WebDriver obj) {
        try {
            //pool.recreate(obj);
        } catch (Exception e) {
            logger.fatal("Can't back object to pool", e);
        }
    }

    public List<BasicEvent> call() {
        final Thread currentThread = Thread.currentThread();
        currentThread.setName(this.getClass().getName());
        final long start = System.currentTimeMillis();
        List<BasicEvent> events = null;
        try {
            logger.info("start getEvents() " + this.getClass().getName());
            events = getEvents();
        } catch (Exception e) {
            logger.fatal(this.getClass().getName() + " fatal error getEvents() - " + e);
        }
        if (events == null) { //TODO
            events = new ArrayList<BasicEvent>();
        }
        final int updateTime = (int)((System.currentTimeMillis() - start) / 1000);
        logger.info(this.getClass().getName() + " getEvents() time - " + updateTime + " sec.; size - " + events.size());

        /*if (Server.isMaster) {
            for (BasicEvent e: events) {
                EventsUpdateProcessor.putEvent(e);
            }
        }*/
        //save updates
        SiteUpdates update = new SiteUpdates();
        update.setEventsNumber(events.size());
        update.setSite(betSite.toString());
        update.setUpdateTime(updateTime);
        SiteUpdateProcessor.sendUpdate(update);
        return events;
    }

    public static ThreadFactory getThreadFactory(String className) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("" + className + "-thread-%d").build();
        return namedThreadFactory;
    }

    public BetSite getBetSite() {
        return betSite;
    }

    protected void sendEvent(BasicEvent event) {
        EventSender.sendEvent(event);
    }

}
