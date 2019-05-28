package com.isfootball.parser;

import com.isfootball.jdbc.Bet;
import com.isfootball.jdbc.BetDao;
import com.isfootball.jdbc.Match;
import com.isfootball.jdbc.SiteUpdates;
import com.isfootball.model.BasicEvent;
import com.isfootball.model.EventKey;
import com.isfootball.parser.source.*;
import com.isfootball.pool.*;
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
import org.openqa.selenium.WebDriver;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Evgeniy Pshenitsin on 18.03.2015.
 */
public class RemoteParser extends BaseParser  {

    private static final Logger logger = LogManager.getLogger("parser");

    public static class RemoteEvents {

        public int status;

        public List<BasicEvent> events;
    }

    private static RemoteParser instance = null;

    public static RemoteParser getInstance() {
        return instance;
    }

    public final ExecutorService selfExecutor = Executors.newFixedThreadPool(1);

    public Future<List<BasicEvent>> selfFuture = null;

    private static List<BasicEvent> events = null;

    private static volatile int status = 4; //TODO

    public static final int STATUS_INCORRECT_REQ = 0;
    public static final int STATUS_STARTED = 1;
    public static final int STATUS_FAIL_START = 2;
    public static final int STATUS_IN_PROGRESS = 3;
    public static final int STATUS_FINISHED = 4;
    public static final int STATUS_FINISHED_WITH_ERROR = 5;

    private String host = "local";

    public static List<BaseParser> parsers = null;

    public static List<Future<List<BasicEvent>>> parserStates;

    List<BasePoolInterface<WebDriver>> pools = null;

    public static ConcurrentMap<Future, String> futureThreads = new ConcurrentHashMap<Future, String>();

    public static void createRemoteParser(List<BasePoolInterface<WebDriver>> pools, List<BaseParser> parsers, String host) {
        instance = new RemoteParser(pools, parsers, host);
    }

    private static class SelfCallable implements Callable<List<BasicEvent>> {

        protected List<BasePoolInterface<WebDriver>> pools;
        List<BaseParser> parsers;

        public SelfCallable(List<BasePoolInterface<WebDriver>> pools, List<BaseParser> parsers) {
            this.pools = pools;
            this.parsers = parsers;

        }
            private static final Logger logger = LogManager.getLogger("parser");

            public List<BasicEvent> call() {
            final long start = System.currentTimeMillis();
            List<BasicEvent> localEvents = new ArrayList<BasicEvent>();
            try {
                ExecutorService parserTasksPool = Executors.newFixedThreadPool(30, BaseParser.getThreadFactory("SelfCallable"));
                parserStates = new ArrayList<Future<List<BasicEvent>>>();
                logger.info("start remote threads for parse");
                for (BaseParser p: parsers) {
                    Future f = parserTasksPool.submit(p);
                    futureThreads.put(f, p.getClass().getName());
                    parserStates.add(f);
                }
                try {
                    for (Future<List<BasicEvent>> f: parserStates) {
                        localEvents.addAll(f.get());
                    }
                } catch (Exception e) {
                    logger.error("get remote bets: future get() exception - ", e);
                }
                parserTasksPool.shutdownNow();
                futureThreads.clear();
                logger.info("remote threads for parse finished");
            } catch (Exception e) {
                logger.fatal(this.getClass().getName() + " fatal error SelfCallable - " + e);
            }
            for (BasePoolInterface pool: pools) {
                pool.destroyAll();
            }
            logger.info(this.getClass().getName() + " SelfCallable time - " + ((System.currentTimeMillis() - start) / 1000) + " sec.");
            synchronized (RemoteParser.class) {
                if (localEvents == null) {
                    events = null;
                    status = STATUS_FINISHED_WITH_ERROR;
                } else {
                    events = localEvents;
                    status = STATUS_FINISHED;
                }
            }
            return localEvents;
        }
    }

    public RemoteParser(List<BasePoolInterface<WebDriver>> pools, List<BaseParser> parsers, String host) {
        super(null);
        this.parsers = parsers;
        this.host = host;
        this.pools = pools;
    }

    @Override
    public List<BasicEvent> call() {
        final long start = System.currentTimeMillis();
        List<BasicEvent> events = null;
        try {
            events = getEvents();
        } catch (Exception e) {
            logger.fatal(this.getClass().getName() + host + " fatal error getEvents() - " + e);
        }
        if (events == null) { //TODO
            events = new ArrayList<BasicEvent>();
        }
        logger.info(this.getClass().getName() + " " + host + " getEvents() time - " + ((System.currentTimeMillis() - start) / 1000) + " sec.; size - " + events.size());
        return events;
    }

    @Override
    public List<BasicEvent> getEvents() {

        final String link = "http://" + host + ":8089/getremoteevents";
        GetMethod get = new GetMethod(link);
        HttpClient httpClient = new HttpClient(HttpClientInitializer.getConnectionManager());
        List<BasicEvent> localEvents = new ArrayList<BasicEvent>();
        get.setQueryString(new NameValuePair[]{
                new NameValuePair("secret_code", "123123"),
                new NameValuePair("command", GetBranchesHandler.COMMAND_START),
        });
        try {
            int returnCode = httpClient.executeMethod(get);
            if (returnCode == HttpStatus.SC_OK) {
                String ret = IOUtils.toString(new InputStreamReader(get.getResponseBodyAsStream(), "UTF-8"));
                RemoteEvents result = GetBranchesHandler.gson.fromJson(ret, RemoteEvents.class);
                if (result.status == STATUS_STARTED || result.status == STATUS_IN_PROGRESS) {
                    Thread.sleep(5000);
                    get = new GetMethod(link);
                    get.setQueryString(new NameValuePair[]{
                            new NameValuePair("secret_code", "123123"),
                            new NameValuePair("command", GetBranchesHandler.COMMAND_STATUS),
                    });
                    while (result.status != STATUS_FINISHED && result.status != STATUS_FINISHED_WITH_ERROR) {
                        Thread.sleep(5000);
                        returnCode = httpClient.executeMethod(get);
                        if (returnCode == HttpStatus.SC_OK) {
                            ret = IOUtils.toString(new InputStreamReader(get.getResponseBodyAsStream(), "UTF-8"));
                            result = GetBranchesHandler.gson.fromJson(ret, RemoteEvents.class);
                        }
                    }
                    if (result.status == STATUS_FINISHED) {
                        get = new GetMethod(link);
                        get.setQueryString(new NameValuePair[]{
                                new NameValuePair("secret_code", "123123"),
                                new NameValuePair("command", GetBranchesHandler.COMMAND_RESULT),
                        });
                        returnCode = httpClient.executeMethod(get);
                        if (returnCode == HttpStatus.SC_OK) {
                            ret = IOUtils.toString(new InputStreamReader(get.getResponseBodyAsStream(), "UTF-8"));
                            result = GetBranchesHandler.gson.fromJson(ret, RemoteEvents.class);
                            if (result.events != null) {
                                localEvents.addAll(result.events);
                            } else {
                                logger.error("getremoteevents return null events list after finish");
                            }
                        }
                    } else {
                        logger.error("getremoteevents return bad status - " + result.status);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("getEvents() exception", e);
        }
        return localEvents;
    }

    public RemoteEvents startDownload() {
        RemoteEvents result = new RemoteEvents();
        synchronized (RemoteParser.class) {
            if (status == STATUS_IN_PROGRESS) {
                result.status = STATUS_IN_PROGRESS;
            } else if (status == STATUS_FINISHED || status == STATUS_FINISHED_WITH_ERROR) {
                selfFuture = selfExecutor.submit(new SelfCallable(pools, parsers));
                result.status = STATUS_STARTED;
                status = STATUS_IN_PROGRESS;
            }
        }
        return result;
    }

    public RemoteEvents getStatus() {
        RemoteEvents result = new RemoteEvents();
        if (status == STATUS_IN_PROGRESS) {
            result.status = STATUS_IN_PROGRESS;
        } else if (status == STATUS_FINISHED || status == STATUS_FINISHED_WITH_ERROR) {
            result.status = status;
        } else {
            result.status = status;
        }
        return result;
    }

    public RemoteEvents getResult() {
        RemoteEvents result = new RemoteEvents();
        if (status == STATUS_IN_PROGRESS) {
            result.status = STATUS_IN_PROGRESS;
        } else if (status == STATUS_FINISHED || status == STATUS_FINISHED_WITH_ERROR) {
            result.status = status;
            result.events = events;
        } else {
            result.status = status;
        }
        return result;
    }

}
