package com.isfootball.server;

/**
 * Created by Evgeniy Pshenitsin on 14.03.2015.
 */
import com.isfootball.jdbc.DBConnection;
import com.isfootball.jdbc.EventsUpdateProcessor;
import com.isfootball.model.BasicEvent;
import com.isfootball.notify.EmailSender;
import com.isfootball.parser.*;
import com.isfootball.parser.direct.SMarketsXmlParser;
import com.isfootball.parser.source.*;
import com.isfootball.pool.*;
import com.isfootball.processing.*;
import com.isfootball.utils.HttpClientInitializer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.glassfish.grizzly.filterchain.FilterChainBuilder;
import org.glassfish.grizzly.http.CompressionConfig;
import org.glassfish.grizzly.http.server.*;
import org.glassfish.grizzly.http.util.HeaderValue;
import org.glassfish.grizzly.memory.PooledMemoryManager;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.utils.IdleTimeoutFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.openqa.selenium.WebDriver;

import java.util.*;
import java.util.concurrent.*;

public class Server {

    private static final Logger logger = LogManager.getLogger("server");

    public static final HeaderValue SERVER_VERSION =
            HeaderValue.newHeaderValue("GRZLY").prepare();

    static final RequestExecutorProvider EXECUTOR_PROVIDER =
            new RequestExecutorProvider.SameThreadProvider();

    public static boolean isMaster = true;
    public static boolean isParser = false;

    static List<ParserStartConfig> startConfig = new ArrayList<ParserStartConfig>();

    static List<RemoteParserStartConfig> startConfigRemote = new ArrayList<RemoteParserStartConfig>();

    public static void main(String[] args) throws Exception {


        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                logger.error("Uncaught error; ", t, e);
            }
        });

        int port = 8081;

        final List<BasePoolInterface> pools = new ArrayList<>();

        //logging


        if (args.length > 0) {
            String runType = args[0];
            isMaster = false;

            if (runType.equals("web")) {
                logger.info("run as web");

                EmailSender.init();
                DBConnection.createPool();
                BetUpdateConsumer.initialize();
                ForksEngine.init();

                /*final EventsUpdateProcessor processor = new EventsUpdateProcessor();
                processor.updateEvents();

                ScheduledExecutorService watchDog = Executors.newScheduledThreadPool(1);
                final ScheduledFuture<?> wdFuture = watchDog.scheduleAtFixedRate(new Runnable() {
                    public void run() {
                        processor.updateEvents();
                    }
                }, 30, 30, TimeUnit.SECONDS);*/

            } else if (runType.equals("slave1")) {
                port = 8089;
                logger.info("run as slave1");
                /*BasePoolConfig config = new BasePoolConfig();
                config.maxActiveObjectsNumber = 3;
                BasePool<WebDriver> pool = new BasePool<WebDriver>(new ChromeFactory(), config);
                BasePoolConfig configJS = new BasePoolConfig();
                configJS.maxActiveObjectsNumber = 1;
                BasePool<WebDriver> poolJS = new BasePool<WebDriver>(new PhantomJSFactory(), configJS);

                //FuriousPool<WebDriver> poolChrome = new FuriousPool<WebDriver>(new ChromeFactory(), config);

*/
                List<BaseParser> parsers = new ArrayList<BaseParser>();
                //parsers.add(new TennisiParser(pool));
                parsers.add(new PlanetWin365Parser(null)); //remote2
                parsers.add(new BetdaqParser(null));//remote2
                //parsers.add(new Bet188Parser(pool));
                //parsers.add(new CoralParser(pool));
                //parsers.add(new BetcityParser(pool));
                //parsers.add(new BetwayParser(pool));
                //parsers.add(new Bet10Parser(pool));
                //parsers.add(new TipicoParser(pool));
                //parsers.add(new BetvictorMobiParser(pool));
                //parsers.add(new LigaStavokParser(pool));
                RemoteParser.createRemoteParser(
                    new ArrayList<BasePoolInterface<WebDriver>>() {{
                    //add(pool);
                    //add(poolJS);
                    }}, parsers, null);
            } else if (runType.equals("slave2")) {
                port = 8089;
                logger.info("run as slave2");
                BasePoolConfig configJS = new BasePoolConfig();
                configJS.maxActiveObjectsNumber = 2;
                FuriousPool<WebDriver> poolJs = new FuriousPool<WebDriver>(new PhantomJSFactory(), configJS);
                List<BaseParser> parsers = new ArrayList<BaseParser>();
                BasePoolConfig config = new BasePoolConfig();
                config.maxActiveObjectsNumber = 5;
                FuriousPool<WebDriver> poolChrome = new FuriousPool<WebDriver>(new ChromeFactory(), config);

                parsers.add(new PinParser(poolJs)); //++++
                //parsers.add(new PlanetWin365Parser(poolJs)); //-------+++++
                //parsers.add(new MarathonBetParser(poolJs));
                parsers.add(new Bet10Parser(poolChrome));
                //parsers.add(new MatchBookParser(poolJs)); //-------
                RemoteParser.createRemoteParser(
                        new ArrayList<BasePoolInterface<WebDriver>>() {{
                            //add(poolJs);
                            //add(poolChrome);
                        }}, parsers, null);

                /*parsers.add(new BetcityParser(poolChrome));
                parsers.add(new MarathonBetParser(poolJs));
                //parsers.add(new BetfairParser(poolChrome));
                parsers.add(new CashPointParser(poolChrome)); //;+ TODO optimize
                parsers.add(new SMarketsParser(poolJs)); //+
                parsers.add(new PinParser(poolJs));
                //parsers.add(new PlanetWin365Parser(poolJs));
                parsers.add(new Bet188Parser(poolChrome));
                parsers.add(new MatchBookParser(poolJs)); //+
                parsers.add(new RuUnibetParser(poolJs)); //+ TODO optimize
                parsers.add(new OneXBetParser(poolChrome)); //+poolJs
                parsers.add(new InterwettenParser(poolChrome)); //+poolJs
                parsers.add(new OlimpkzParser(poolChrome)); //+poolJs
                parsers.add(new PariMatchParser(poolJs)); //+

                parsers.add(new BetwayParser(poolChrome)); //+
                parsers.add(new BetvictorMobiParser(poolJs)); //+
                //parsers.add(new LigaStavokParser(poolChrome)); //долго думает с phantomjs
                parsers.add(new BaltbetParser(poolJs));
                parsers.add(new ExpektParser(poolJs)); //+++S

                parsers.add(new TipicoParser(poolChrome)); //плохеет с phantomjs - падает, сука, браузер
                parsers.add(new LigaStavokParser(poolChrome)); //долго думает с phantomjs

                parsers.add(new GolpasParser(poolChrome)); //+poolJs
                parsers.add(new BetdaqParser(poolJs));
                parsers.add(new TennisiParser(poolChrome)); //+poolJs
                parsers.add(new BetInParser(poolJs)); //+ TODO optimize

                parsers.add(new WinlineBetParser(poolJs));
                parsers.add(new SBobetParser(poolChrome));
                parsers.add(new ZenitbetParser(poolJs));

                parsers.add(new CoralParser(poolJs)); //poolJs +++S

                //parsers.add(new Bet10Parser(poolJs)); //с англии ёбаная капча

                RemoteParser.createRemoteParser(
                        new ArrayList<BasePool<WebDriver>>() {{
                            add(poolJs);
                            add(poolChrome);
                        }}, parsers, null);*/
            } else if (runType.equals("parser1")) {
                port = 8089;
                logger.info("run as parser1");
                isParser = true;

                SiteUpdateProcessor.initializeProducer();
                EventSender.initialize();

                BasePoolConfig config = new BasePoolConfig();
                config.maxActiveObjectsNumber = 6;
                FuriousPool<WebDriver> poolChrome = new FuriousPool<WebDriver>(new ChromeFactory(), config);
                pools.add(poolChrome);
                //BasePoolConfig configJS = new BasePoolConfig();
                //configJS.maxActiveObjectsNumber = 3;
                //FuriousPool<WebDriver> poolJs = new FuriousPool<WebDriver>(new PhantomJSFactory(), configJS);

                //***********
                startConfig.add(new ParserStartConfig(new MatchBookParser(null), 2, 5)); //+ ~ 20 sec

                //startConfig.add(new ParserStartConfig(new RuUnibetParser(null), 3, 5)); //+ ~60 sec NOT work
                //startConfig.add(new ParserStartConfig(new OneXBetParser(null), 4, 5)); //+ ~ 124 sec TODO optimize remote2 NOT work
                startConfig.add(new ParserStartConfig(new BetInParser(null), 5, 5)); // ~30 sec
                startConfig.add(new ParserStartConfig(new BetvictorMobiParser(null), 1, 7)); //+
                //***********


                //master
                //startConfig.add(new ParserStartConfig(new SMarketsParser(null), 1, 7)); //+  ~60 sec
                //startConfig.add(new ParserStartConfig(new OneXBetParser(null), 0, 10)); //+ ~ 263 sec TODO optimize
                //startConfig.add(new ParserStartConfig(new InterwettenParser(null), 2, 7)); //+ ~ 55sec
                //startConfig.add(new ParserStartConfig(new OlimpkzParser(null), 0, 7)); //+ ~ 22 sec
                //startConfig.add(new ParserStartConfig(new BetdaqParser(null), 1, 30)); //~ TODO ~ 100sec
                startConfig.add(new ParserStartConfig(new TennisiParser(null), 3, 10)); //+ ~ 30 sec
                //startConfig.add(new ParserStartConfig(new PlanetWin365Parser(null), 4, 7)); //~200 sec

                //startConfig.add(new ParserStartConfig(new BetcityParser(poolChrome), 12, 50)); //not work remote2 TODO
                //startConfig.add(new ParserStartConfig(new BetfairParser(poolChrome), 17, 50));
                startConfig.add(new ParserStartConfig(new CashPointParser(poolChrome), 0, 17)); //;+ 217 sec TODO optimize
                startConfig.add(new ParserStartConfig(new SBobetParser(poolChrome), 2, 17)); //200 sec
                startConfig.add(new ParserStartConfig(new BetwayParser(poolChrome), 3, 17)); //+ ~147 sec
                startConfig.add(new ParserStartConfig(new ExpektParser(poolChrome), 6, 17)); //+++S ~250 sec  TODO optimize
                //startConfig.add(new ParserStartConfig(new TipicoParser(poolChrome), 0, 40)); //плохеет с phantomjs - падает, сука, браузер not work remote2 TODO
                //startConfig.add(new ParserStartConfig(new LigaStavokParser(poolChrome), 15, 15)); //долго думает с phantomjs ~~64 sec
                //startConfig.add(new ParserStartConfig(new CoralParser(poolChrome), 12, 40)); // ~507 sec TODO remote not work
                startConfig.add(new ParserStartConfig(new BaltbetParser(poolChrome), 5, 17)); //~702 sec
                startConfig.add(new ParserStartConfig(new WinlineBetParser(poolChrome), 3, 20));//~662 sec
                startConfig.add(new ParserStartConfig(new ZenitbetParser(poolChrome), 0, 20)); //~823 sec
                startConfig.add(new ParserStartConfig(new MarathonBetParser(poolChrome), 0, 5)); //73 sec
                startConfig.add(new ParserStartConfig(new GolpasParser(poolChrome), 7, 17)); //~222 sec
                //startConfig.add(new ParserStartConfig(new PinParser(poolChrome), 0, 10)); //work!!! 117 sec

                startConfig.add(new ParserStartConfig(new Bet188Parser(poolChrome), 2, 15)); //JS ~73 sec //TODO
                startConfig.add(new ParserStartConfig(new PariMatchParser(poolChrome), 3, 15)); //JS  ~288 sec
                startConfig.add(new ParserStartConfig(new Bet10Parser(poolChrome), 3, 15)); //с англии ёбаная капча work!!!
                //startConfig.add(new ParserStartConfig(new WilliamHillParser(poolChrome), 0, 15));

                startConfig.add(new ParserStartConfig(new OlimpkzParser(null), 0, 5)); //+ ~ 6 sec
                //startConfig.add(new ParserStartConfig(new BetcityParserJson(null), 0, 7)); //260 sec

                //separate pools
                BasePoolConfig configBC = new BasePoolConfig();
                configBC.maxActiveObjectsNumber = 8;
                FuriousPool<WebDriver> poolChromeBC = new FuriousPool<WebDriver>(new ChromeFactory("5555"), configBC);
                pools.add(poolChromeBC);

                /*BasePoolConfig configFF = new BasePoolConfig();
                configBC.maxActiveObjectsNumber = 1;
                FuriousPool<WebDriver> poolFF = new FuriousPool<WebDriver>(new FirefoxFactory("5555"), configFF);
                pools.add(poolFF);*/

                /*BasePoolConfig configPH = new BasePoolConfig();
                configPH.maxActiveObjectsNumber = 2;
                FuriousPool<WebDriver> poolPH = new FuriousPool<WebDriver>(new PhantomJSFactory(), configPH);
                pools.add(poolPH);*/



                startConfig.add(new ParserStartConfig(new FonbetParser(poolChromeBC), 2, 15)); //+  ~33 sec
                startConfig.add(new ParserStartConfig(new Bet365Parser(poolChromeBC), 1, 15)); //260 sec
                //startConfig.add(new ParserStartConfig(new BetcityParser(poolChromeBC), 0, 7)); //260 sec
                startConfig.add(new ParserStartConfig(new BetfairParser(poolChromeBC), 4, 18)); //161 sec
                startConfig.add(new ParserStartConfig(new TipicoParser(poolChromeBC), 13, 18)); //TODO odds http req 483 sec
                startConfig.add(new ParserStartConfig(new CoralParser(poolChromeBC), 16, 18)); //82 sec

            } else if (runType.equals("parser2")) {
                port = 8089;
                logger.info("run as parser2");
                isParser = true;

                SiteUpdateProcessor.initializeProducer();
                EventSender.initialize();

                BasePoolConfig config = new BasePoolConfig();
                config.maxActiveObjectsNumber = 12;
                FuriousPool<WebDriver> poolChrome = new FuriousPool<WebDriver>(new ChromeFactory(), config);
                pools.add(poolChrome);

                startConfig.add(new ParserStartConfig(new InterwettenParser(null), 0, 2)); //+ ~ 33sec
                //startConfig.add(new ParserStartConfig(new OlimpkzParser(null), 0, 2)); //+ ~ 6 sec
                //startConfig.add(new ParserStartConfig(new SMarketsParser(null), 1, 3)); //+  ~33 sec забанили

                startConfig.add(new ParserStartConfig(new LigaStavokParser(poolChrome), 2, 3)); //долго думает с phantomjs ~~64 sec
                //startConfig.add(new ParserStartConfig(new MarathonBetParser(poolChrome), 3, 7)); //73 sec TODO
                startConfig.add(new ParserStartConfig(new PinParser(poolChrome), 0, 3)); //work!!! 93 sec
                startConfig.add(new ParserStartConfig(new WilliamHillParser(poolChrome), 1, 10));
                //startConfig.add(new ParserStartConfig(new Bet365Parser(poolChrome), 0, 10));
                //startConfig.add(new ParserStartConfig(new MarathonBetParser(poolChrome), 0, 5)); //73 sec
                //startConfig.add(new ParserStartConfig(new RuUnibetParser(null), 2, 5)); //+ ~60 sec TODO optimize
                //startConfig.add(new ParserStartConfig(new SMarketsXmlParser(null), 0, 7)); //+ ~60 sec TODO optimize
                startConfig.add(new ParserStartConfig(new PlanetWin365Parser(null), 4, 7)); //~200 sec
            } else if (runType.equals("parser3")) {
                port = 8089;
                logger.info("run as parser3");
                isParser = true;

                SiteUpdateProcessor.initializeProducer();
                EventSender.initialize();

                //startConfig.add(new ParserStartConfig(new OneXBetParser(null), 1, 10)); //+ ~ 124 sec TODO optimize remote2
                startConfig.add(new ParserStartConfig(new RuUnibetParser(null), 1, 7));
                startConfig.add(new ParserStartConfig(new SMarketsXmlParser(null), 0, 4)); //+ ~60 sec TODO optimize
            } else if (runType.equals("parser_home")) {
                port = 8089;
                logger.info("run as parser_home");
                isParser = true;

                SiteUpdateProcessor.initializeProducer();
                EventSender.initialize();

                BasePoolConfig configBC = new BasePoolConfig();
                configBC.maxActiveObjectsNumber = 1;
                FuriousPool<WebDriver> poolChromeBC = new FuriousPool<WebDriver>(new ChromeFactory(), configBC);
                pools.add(poolChromeBC);

                /*BasePoolConfig configFF = new BasePoolConfig();
                configBC.maxActiveObjectsNumber = 1;
                FuriousPool<WebDriver> poolFF = new FuriousPool<WebDriver>(new FirefoxFactory("5555"), configFF);
                pools.add(poolFF);*/

                startConfig.add(new ParserStartConfig(new FonbetParser(poolChromeBC), 0, 5)); //260 sec

            }
        } else {
            isParser = true;
            port = 8088;
            DBConnection.createPool();
            EventsUpdateProcessor processor = new EventsUpdateProcessor();
            processor.initEvents();
            new Thread(processor).start();

            SiteUpdateProcessor.initialize();
            EventConsumer.initialize();
            EventSender.initialize();
            BetUpdateProducer.initialize();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    logger.info("calculateAllLines start");
                    //BaseBranchUtils.calculateAllLines(StaticCache.shemes);
                    logger.info("calculateAllLines end");
                }
            }).start();

            logger.info("run as master");

            BasePoolConfig config = new BasePoolConfig();
            config.maxActiveObjectsNumber = 8;
            //FuriousPool<WebDriver> poolChrome = new FuriousPool<WebDriver>(new ChromeFactory(), config);

            BasePoolConfig config2 = new BasePoolConfig();
            config2.maxActiveObjectsNumber = 4;
            //FuriousPool<WebDriver> poolChrome2 = new FuriousPool<WebDriver>(new ChromeFactory(), config);

            BasePoolConfig configFF = new BasePoolConfig();
            configFF.maxActiveObjectsNumber = 4;
            //FuriousPool<WebDriver> poolFF = new FuriousPool<WebDriver>(new FirefoxFactory(), config);

            BasePoolConfig configJS = new BasePoolConfig();
            configJS.maxActiveObjectsNumber = 4;
            //FuriousPool<WebDriver> poolJs = new FuriousPool<WebDriver>(new PhantomJSFactory(), configJS);
            //master
            //startConfig.add(new ParserStartConfig(new SMarketsParser(null), 0, 20)); //+  ~141 sec remote2
            //startConfig.add(new ParserStartConfig(new MatchBookParser(null), 2, 2)); //+ ~ 20 sec

            //**************
            //startConfig.add(new ParserStartConfig(new RuUnibetParser(null), 3, 10)); //+ ~60 sec TODO optimize
            startConfig.add(new ParserStartConfig(new OneXBetParser(null), 2, 10)); //+ ~ 124 sec TODO optimize remote2
            //startConfig.add(new ParserStartConfig(new BetcityParserJson(null), 1, 7)); //260 sec
            //startConfig.add(new ParserStartConfig(new BetdaqParser(null), 0, 20)); //~ TODO ~ 250 sec

            //startConfig.add(new ParserStartConfig(new BetInParser(null), 5, 5)); // ~30 sec
            //startConfig.add(new ParserStartConfig(new BetvictorMobiParser(null), 6, 7)); //+


            //startConfig.add(new ParserStartConfig(new InterwettenParser(null), 8, 20)); //+ ~ 170sec remote2
            //startConfig.add(new ParserStartConfig(new OlimpkzParser(null), 10, 20)); //+ ~ 60 sec remote2
            //startConfig.add(new ParserStartConfig(new TennisiParser(null), 12, 30)); //+ ~ 120 sec

            //ADDDDDDDDDD
            //startConfig.add(new ParserStartConfig(new BetcityParser(poolChrome), 4, 10)); //~60 sec
            //startConfig.add(new ParserStartConfig(new BetfairParser(poolChrome), 8, 20));
            //startConfig.add(new ParserStartConfig(new TipicoParser(poolChrome), 13, 20)); //плохеет с phantomjs - падает, сука, браузер ~400sec
            //startConfig.add(new ParserStartConfig(new CoralParser(poolChrome), 16, 20)); // ~154 sec TODO optimize

            //startConfig.add(new ParserStartConfig(new CashPointParser(poolChrome), 19, 35)); //;+ TODO optimize remote2
            //startConfig.add(new ParserStartConfig(new SBobetParser(poolChrome), 30, 45)); //remote2
            //startConfig.add(new ParserStartConfig(new BetwayParser(poolChrome), 2, 35)); //+ remote2
            //startConfig.add(new ParserStartConfig(new ExpektParser(poolChrome), 10, 40)); //+++S remote2
            //startConfig.add(new ParserStartConfig(new LigaStavokParser(poolChrome), 30, 50)); //долго думает с phantomjs remote2
            //startConfig.add(new ParserStartConfig(new BaltbetParser(poolChrome), 17, 45)); //~702 sec
            //startConfig.add(new ParserStartConfig(new WinlineBetParser(poolChrome), 15, 30));//~740 sec work with chrome OK
            //startConfig.add(new ParserStartConfig(new ZenitbetParser(poolChrome), 17, 40)); //~823 sec remote2
            //startConfig.add(new ParserStartConfig(new MarathonBetParser(poolChrome), 7, 30)); //~534 sec
            //startConfig.add(new ParserStartConfig(new GolpasParser(poolChrome), 7, 30)); //~923 sec
            //startConfig.add(new ParserStartConfig(new PinParser(poolChrome), 10, 10));
            //startConfig.add(new ParserStartConfig(new PlanetWin365Parser(poolChrome), 10, 10));

            //startConfig.add(new ParserStartConfig(new Bet188Parser(poolChrome), 1, 5)); //JS ~80 sec
            //startConfig.add(new ParserStartConfig(new PariMatchParser(poolChrome), 12, 30)); //JS
            //startConfig.add(new ParserStartConfig(new Bet10Parser(poolChrome), 10, 10)); //с англии ёбаная капча

            //startConfig.add(new ParserStartConfig(new RemoteParser(null, null, "clewcms.ru"), 0, 10));

            //startConfig.add(new ParserStartConfig(new RemoteParser(null, null, "195.154.106.41"), 0, 30));
            //startConfigRemote.add(new RemoteParserStartConfig(new RemoteParser2("188.40.70.71"), 10, 20));
            //startConfigRemote.add(new RemoteParserStartConfig(new RemoteParser2("188.40.85.195"), 20, 20));
        }

        HttpClientInitializer.setupConnection();

        final HttpServer httpServer = new HttpServer();
        final NetworkListener networkListener = new NetworkListener(
                "http-listener", "0.0.0.0", port);
        networkListener.setChunkingEnabled(false);
        CompressionConfig cc = networkListener.getCompressionConfig();
        cc.setCompressionMode(CompressionConfig.CompressionMode.ON);
        cc.setCompressionMinSize(50000); // the min number of bytes to compress
        cc.setCompressableMimeTypes("application/json", "text/json", "text/html");
        final TCPNIOTransport transport = networkListener.getTransport();

        // force to not initialize worker thread pool
        transport.setWriteBufferSize(10000000);
        //transport.setWorkerThreadPoolConfig(null);
        //transport.setSelectorRunnersCount(Runtime.getRuntime().availableProcessors() * 2);

        // set PooledMemoryManager
        transport.setMemoryManager(new PooledMemoryManager());

        // always keep-alive
        //networkListener.getKeepAlive().setIdleTimeoutInSeconds(-1);
        //networkListener.getKeepAlive().setMaxRequestsCount(-1);

        // disable transaction timeout
        //networkListener.setTransactionTimeout(-1);

        // remove the features we don't need
        //networkListener.registerAddOn(new SimplifyAddOn());
        // add HTTP pipeline optimization
        //networkListener.registerAddOn(new HttpPipelineOptAddOn());

        // disable file-cache
        //networkListener.getFileCache().setEnabled(false);

        httpServer.addListener(networkListener);

        httpServer.getServerConfiguration().addHttpHandler(
                new GetBranchesHandler(), "/");

        try {
            httpServer.start();
            logger.info("Server started");
            if (isParser) {
                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(60);

                if (!pools.isEmpty()) {
                    /*scheduler.scheduleAtFixedRate(new Runnable() {
                        public void run() {
                            logger.info("recreate pools start");
                            pools.get(0).destroyAll(); //TODO
                            for (BasePoolInterface pool: pools) {
                                pool.recreate();
                            }
                            logger.info("recreate pools end");
                        }
                    }, 120, 120, TimeUnit.MINUTES);*/
                }
                for (ParserStartConfig c: startConfig) {
                    final ScheduledFuture<?> periodicFuture = scheduler.scheduleAtFixedRate(new Runnable() {
                        public void run() {
                            List<BasicEvent> events = c.parser.call();
                            if (events != null) {
                                BetSite site = c.parser.getBetSite();
                                if (site != null) {
                                    if (events.size() != 0) {
                                        StaticCache.currentEventsUpdate.put(site, new EventsUpdate(events, new Date()));
                                    }
                                } else {
                                    //for old remote
                                    /*Map<BetSite, List<BasicEvent>> currentEvents = new HashMap<BetSite, List<BasicEvent>>();
                                    for (BasicEvent event: events) {
                                        if (event.site == null) {
                                            logger.fatal("NO BET SITE!!!!!!!!!!!!!!!!!!!!" + c.parser.getClass().getName());
                                            continue;
                                        }
                                        List<BasicEvent> mapEvents = currentEvents.get(event.site);
                                        if (mapEvents == null) {
                                            mapEvents = new ArrayList<BasicEvent>();
                                            currentEvents.put(event.site, mapEvents);
                                        }
                                        mapEvents.add(event);
                                    }
                                    for (BetSite s: currentEvents.keySet()) {
                                        if (currentEvents.get(s).size() != 0) {
                                            StaticCache.currentEventsUpdate.put(s, new EventsUpdate(currentEvents.get(s), new Date()));
                                        }
                                    }*/
                                }
                            }
                        }
                    }, c.startDelay, c.interval, TimeUnit.MINUTES);
                }

                /*for (RemoteParserStartConfig c: startConfigRemote) {
                    final ScheduledFuture<?> periodicFuture = scheduler.scheduleAtFixedRate(new Runnable() {
                        public void run() {
                            logger.info("update from remote start");
                            try {
                                Map<BetSite, EventsUpdate> events = c.parser.call();
                                if (events != null) {
                                    for (BetSite b : events.keySet()) {
                                        logger.info("update from remote; " + b);
                                        if (events.get(b).events.size() != 0) {
                                            StaticCache.currentEventsUpdate.put(b, events.get(b));
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                logger.info("update from remote exception", e);
                            }
                        }
                    }, c.startDelay, c.interval, TimeUnit.SECONDS);
                }*/

                /*final ScheduledFuture<?> periodicFuture = scheduler.scheduleAtFixedRate(new Runnable() {
                    public void run() {
                        readBets();
                    }
                }, 0, 60, TimeUnit.MINUTES);*/
            }
            synchronized (Server.class) {
                Server.class.wait();
            }
        } finally {
            httpServer.shutdown();
        }
    }

    private static void readBets() {
        DateTime dt = new DateTime();

        if (dt.getHourOfDay() < 7 && dt.getHourOfDay() > 2) {
            logger.info("time to sleep ;)");
            return;
        }
        final long startTime = System.currentTimeMillis();
        logger.info("***** read bets started *****");
        ParserCore.processGetBets();
        logger.info("***** read bets finished *****");
        logger.info("***** read bets time: " + (System.currentTimeMillis() - startTime) / 1000 + " sec. *****");
    }

    private static class SimplifyAddOn implements AddOn {

        @Override
        public void setup(final NetworkListener networkListener,
                          final FilterChainBuilder builder) {
            final int fcIdx = builder.indexOfType(FileCacheFilter.class);
            if (fcIdx != -1) {
                builder.remove(fcIdx);
            }

            final int itIdx = builder.indexOfType(IdleTimeoutFilter.class);
            if (itIdx != -1) {
                builder.remove(itIdx);
            }
        }
    }
}