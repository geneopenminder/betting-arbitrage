package com.isfootball.parser;

import com.isfootball.jdbc.*;
import com.isfootball.model.BasicEvent;
import com.isfootball.model.EventKey;
import com.isfootball.server.GetBranchesHandler;
import com.isfootball.server.Server;
import com.isfootball.utils.HttpClientInitializer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by Evgeniy Pshenitsin on 22.08.2015.
 */
public class RemoteParser2 implements Callable<Map<BetSite, EventsUpdate>> {

    private static final Logger logger = LogManager.getLogger("parser");

    public static class RemoteEvents2 {

        public Map<BetSite, EventsUpdate> currentEvents;
    }

    private String host = "local";

    public RemoteParser2(String host) {
        this.host = host;
    }

    public Map<BetSite, EventsUpdate> call() {
        final long start = System.currentTimeMillis();
        Map<BetSite, EventsUpdate> events = null;
        try {
            events = getEvents();
        } catch (Exception e) {
            logger.fatal(this.getClass().getName() + host + " fatal error getEvents() - " + e);
        }
        if (events == null) { //TODO
            events = new HashMap<BetSite, EventsUpdate>();
        }
        if (Server.isMaster) {
            for (BetSite betSite : events.keySet()) {
                logger.info("save bets for site: " + betSite);
                if (betSite == null) {
                    logger.fatal("null bet site");
                    continue;
                }
                for (BasicEvent e : events.get(betSite).events) {
                    EventsUpdateProcessor.putEvent(e);
                }
                //save updates
                SiteUpdates update = new SiteUpdates();
                update.setEventsNumber(events.get(betSite).events.size());
                update.setSite(betSite.toString());
                update.setUpdateTime((int) ((System.currentTimeMillis() - start) / 1000));
                BetDao.saveSiteUpdate(update);
            }
        }
        logger.info(this.getClass().getName() + " " + host + " getEvents() time - " + ((System.currentTimeMillis() - start) / 1000) + " sec.; size - " + events.size());
        return events;
    }

    public Map<BetSite, EventsUpdate> getEvents() {

        final String link = "http://" + host + ":8089/getremoteupdate";
        GetMethod get = new GetMethod(link);
        HttpClient httpClient = new HttpClient(HttpClientInitializer.getConnectionManager());
        Map<BetSite, EventsUpdate> localEvents = new HashMap<BetSite, EventsUpdate>();
        get.setQueryString(new NameValuePair[]{
                new NameValuePair("secret_code", "123123")
        });
        try {
            int returnCode = httpClient.executeMethod(get);
            if (returnCode == HttpStatus.SC_OK) {
                String ret = IOUtils.toString(new InputStreamReader(get.getResponseBodyAsStream(), "UTF-8"));
                RemoteEvents2 result = GetBranchesHandler.gson.fromJson(ret, RemoteEvents2.class);
                if (result != null) {
                    localEvents = result.currentEvents;
                }
            }
        } catch (Exception e) {
            logger.error("RemoteParser2() exception", e);
        }
        return localEvents;
    }

}
