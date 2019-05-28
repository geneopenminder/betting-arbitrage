package com.isfootball.parser;

import com.google.gson.GsonBuilder;
import com.isfootball.branching.*;
import com.isfootball.jdbc.*;
import com.isfootball.model.*;
import com.isfootball.parser.direct.SMarketsXmlParser;
import com.isfootball.parser.source.*;
import com.isfootball.pool.*;
import com.isfootball.processing.*;
import com.isfootball.utils.HttpClientInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.WebDriver;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import com.google.gson.*;

public class ParserCore {

    public static int USER_STATUS_UNKNOWN = 0;
    public static int USER_STATUS_REGISTERED = 1;
    public static int USER_STATUS_SUBSCRIBED = 2;

    static {
        HttpClientInitializer.setupConnection();
    }

    private static final Logger logger = LogManager.getLogger("parser");

    public static class ThreadWDInfo {
        public long start;
        public Thread thread;
    };

    private static Gson siteJsonSerializer;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .serializeSpecialFloatingPointValues()
                //.setPrettyPrinting()
                .registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
                    @Override
                    public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                        //TODO
                        if (src.isNaN()) {
                            return new JsonPrimitive((new BigDecimal(0)).setScale(4, BigDecimal.ROUND_HALF_UP).stripTrailingZeros());
                        }
                        return new JsonPrimitive((new BigDecimal(src)).setScale(4, BigDecimal.ROUND_HALF_UP).stripTrailingZeros());
                    }
                });
        siteJsonSerializer = gsonBuilder.create();

    }

    public static final Map<Long, ThreadWDInfo> workers = new ConcurrentHashMap<Long, ThreadWDInfo>();

    public static void putThread(ThreadWDInfo thread) {
        workers.put(thread.thread.getId(), thread);
    }

    public static void removeThread(long id) {
        workers.remove(id);
    }

    static class JSONProfitComparator implements Comparator<JSONProfit> {
        @Override
        public int compare(JSONProfit a, JSONProfit b) {
            return a.profitRate.compareTo(b.profitRate);
        }
    }

    //new api ***************************************
    public static String processGetBetsAllNew(boolean oldFormat, int userStatus, String lang) {
        long start = System.currentTimeMillis();
        String jsonString = "{error}";
        try {

            Map<Integer, ForksEngine.ForkHolder> forks = ForksEngine.getActualForks();

            final Map<Long, FinalEvent> matches = ForksEngine.getMatches();

            forks.values()
                    .stream()
                    .filter((ForksEngine.ForkHolder val) -> {
                        if (val.getFormulaId() >= 10000) { //tunnels
                            return false;
                        }
                        return true;
                    })
                    .forEach((ForksEngine.ForkHolder val) -> {
                        FinalEvent event = matches.get(val.matchId);
                        val.jsonProfit.forkLifeTime = (int) (System.currentTimeMillis() - val.bornTime) / 1000; //TODO make copy
                        event.profits.add(val.jsonProfit);
                    });

            List<JSONEvent> json = matches.values()
                    .stream()
                    .filter(event -> {
                        if (event.profits.isEmpty()) {
                            return false;
                        }
                        return true;
                    })
                    .map(event -> {
                        return new JSONEvent(event,lang);
                        }
                    )
                    .collect(Collectors.toList()) ;

            final int size = json.stream().mapToInt(event -> event.profits.size()).sum();

            //json.sort(new JSONProfitComparator());
            //json = json.subList(json.size() < 200 ? 0 : json.size() - 200,
            //        json.size());

            if (oldFormat) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.serializeSpecialFloatingPointValues();
                Gson gs = gsonBuilder.setPrettyPrinting().create();
                jsonString = gs.toJson(json);
            } else {
                JSONResult jsonResult = new JSONResult();
                jsonResult.data = json;
                jsonResult.numBranches = size;
                jsonString = siteJsonSerializer.toJson(jsonResult);
            }
        } catch (Exception e) {
            logger.fatal("processGetBetsAllNew() error", e);
        }
        long end = System.currentTimeMillis();
        logger.info("processGetBetsAllNew() finished; " + (end - start));
        return jsonString;
    }

    public static String processGetTunnelBetsCustomNew(int userStatus, String lang) {
        long start = System.currentTimeMillis();
        String jsonString = "{error}";
        try {

            Map<Integer, ForksEngine.ForkHolder> forks = ForksEngine.getActualForks();

            final Map<Long, FinalEvent> matches = ForksEngine.getMatches();

            forks.values()
                    .stream()
                    .filter((ForksEngine.ForkHolder val) -> {
                        if (val.getFormulaId() < 10000) { //tunnels
                            return false;
                        }
                        return true;
                    })
                    .forEach((ForksEngine.ForkHolder val) -> {
                        FinalEvent event = matches.get(val.matchId);
                        val.jsonProfit.forkLifeTime = (int) (System.currentTimeMillis() - val.bornTime) / 1000; //TODO make copy
                        event.profits.add(val.jsonProfit);
                    });

            List<JSONEvent> json = matches.values()
                    .stream()
                    .filter(event -> {
                        if (event.profits.isEmpty()) {
                            return false;
                        }
                        return true;
                    })
                    .map(event -> {
                                return new JSONEvent(event, lang);
                            }
                    )
                    .collect(Collectors.toList()) ;

            final int size = json.stream().mapToInt(event -> event.profits.size()).sum();

            //json.sort(new JSONProfitComparator());
            //json = json.subList(json.size() < 200 ? 0 : json.size() - 200,
            //        json.size());

            JSONResult jsonResult = new JSONResult();
            jsonResult.data = json;
            jsonResult.numBranches = size;
            jsonString = siteJsonSerializer.toJson(jsonResult);
        } catch (Exception e) {
            logger.fatal("processGetTunnelBetsCustomNew() error", e);
        }
        long end = System.currentTimeMillis();
        logger.info("processGetTunnelBetsCustomNew() finished; " + (end - start));
        return jsonString;
    }

    public static String processGetBetsCustomNew2(List<BetSite> sList, boolean oldFormat, int userStatus, String lang) {
        long start = System.currentTimeMillis();
        String jsonString = "{error}";
        try {

            Map<Integer, ForksEngine.ForkHolder> forks = ForksEngine.getActualForks();

            final Map<Long, FinalEvent> matches = ForksEngine.getMatches();

            forks.values()
                    .stream()
                    .filter((ForksEngine.ForkHolder val) -> {
                        if (val.getFormulaId() >= 10000) { //tunnels
                            return false;
                        }
                        return true;
                    })
                    .filter(val -> {
                        if (sList == null) {
                            return true;
                        }
                        if (sList.containsAll(Arrays.asList(val.sites))) {
                            return true;
                        }
                        return false;
                    })
                    .forEach((ForksEngine.ForkHolder val) -> {
                        FinalEvent event = matches.get(val.matchId);
                        val.jsonProfit.forkLifeTime = (int) (System.currentTimeMillis() - val.bornTime) / 1000; //TODO make copy
                        event.profits.add(val.jsonProfit);
                    });

            List<JSONEvent> json = matches.values()
                    .stream()
                    .filter(event -> {
                        if (event.profits.isEmpty()) {
                            return false;
                        }
                        return true;
                    })
                    .map(event -> {
                                return new JSONEvent(event, lang);
                            }
                    )
                    .collect(Collectors.toList()) ;

            final int size = json.stream().mapToInt(event -> event.profits.size()).sum();

            if (oldFormat) {
                jsonString = siteJsonSerializer.toJson(json);
            } else {
                JSONResult jsonResult = new JSONResult();
                jsonResult.data = json;
                jsonResult.numBranches = size;
                jsonString = siteJsonSerializer.toJson(jsonResult);
            }
        } catch (Exception e) {
            logger.fatal("processGetBetsCustomNew2() error", e);
        }
        long end = System.currentTimeMillis();
        logger.info("processGetBetsCustomNew2() finished; " + (end - start));
        return jsonString;
    }

    public static String processGetTunnelBetsCustomNew(List<BetSite> sList, int userStatus, String lang) {
        long start = System.currentTimeMillis();
        String jsonString = "{error}";
        try {

            Map<Integer, ForksEngine.ForkHolder> forks = ForksEngine.getActualForks();

            final Map<Long, FinalEvent> matches = ForksEngine.getMatches();

            forks.values()
                    .stream()
                    .filter((ForksEngine.ForkHolder val) -> {
                        if (val.getFormulaId() < 10000) { //tunnels
                            return false;
                        }
                        return true;
                    })
                    .filter(val -> {
                        if (sList == null) {
                            return true;
                        }
                        if (sList.containsAll(Arrays.asList(val.sites))) {
                            return true;
                        }
                        return false;
                    })
                    .forEach((ForksEngine.ForkHolder val) -> {
                        FinalEvent event = matches.get(val.matchId);
                        val.jsonProfit.forkLifeTime = (int) (System.currentTimeMillis() - val.bornTime) / 1000; //TODO make copy
                        event.profits.add(val.jsonProfit);
                    });

            List<JSONEvent> json = matches.values()
                    .stream()
                    .filter(event -> {
                        if (event.profits.isEmpty()) {
                            return false;
                        }
                        return true;
                    })
                    .map(event -> {
                                return new JSONEvent(event, lang);
                            }
                    )
                    .collect(Collectors.toList()) ;

            final int size = json.stream().mapToInt(event -> event.profits.size()).sum();

            JSONResult jsonResult = new JSONResult();
            jsonResult.data = json;
            jsonResult.numBranches = size;
            jsonString = siteJsonSerializer.toJson(jsonResult);
        } catch (Exception e) {
            logger.fatal("processGetTunnelBetsCustomNew() error", e);
        }
        long end = System.currentTimeMillis();
        logger.info("processGetTunnelBetsCustomNew() finished; " + (end - start));
        return jsonString;
    }

    //TODO fork id
    public static String processGetBetsCustomNew2(List<BetSite> sList, Map<BetSite, Double> cms, boolean oldFormat, int userStatus, String lang) {
        long start = System.currentTimeMillis();
        String jsonString = "{error}";
        try {
            ConcurrentMap<Long, ConcurrentMap<BetType, ForksEngine.BetsHolder>> bets = ForksEngine.getActualBets(); //TODO rewrite for use forks

            Map<EventKey, Map<BetType, BetKey>> betsMapPrep = new HashMap<>();

            bets.values().forEach(map -> {
                map.forEach((type, holder) -> {
                    EventKey key = new EventKey(holder.match);
                    Map<BetType, BetKey> betsMap = betsMapPrep.get(key);
                    if (betsMap == null) {
                        betsMap = new HashMap<BetType, BetKey>();
                    }
                    ForksEngine.BetVal val;
                    Optional opt = holder.bets.stream()
                            .filter(betVal -> sList.contains(betVal.site))
                            .max(new Comparator<ForksEngine.BetVal>() {
                                @Override
                                public int compare(ForksEngine.BetVal o1, ForksEngine.BetVal o2) {
                                    Double val1 = o1.val;
                                    Double val2 = o2.val;

                                    if (cms.keySet().contains(o1.site)) {
                                        val1 = o1.val * ((100.0 - cms.get(o1.site)) / 100.0);
                                    }
                                    if (cms.keySet().contains(o2.site)) {
                                        val2 = o2.val * ((100.0 - cms.get(o2.site)) / 100.0);
                                    }
                                    return val1.compareTo(val2);
                                }
                            });
                    if (opt.isPresent()) {
                        val = (ForksEngine.BetVal) opt.get();


                        Double cmsSiteRate = cms.get(val.site);
                        BetKey betKey = new BetKey(val.site, val.val, val.site.getName());

                        if (cmsSiteRate != null) {
                            Double realK = val.val * ((100.0 - cmsSiteRate) / 100.0);
                            betKey = new BetKey(val.site, realK, val.val, val.site.getName());
                        }
                        betsMap.put(type, betKey);
                    }
                    if (!betsMap.isEmpty()) {
                        betsMapPrep.put(key, betsMap);
                    }
                });
            });

            Map<EventKey, List<Map<BaseScheme.ProfitType, BranchResult>>> allBranches = BaseBranchUtils.calculateAll(StaticCache.shemes, betsMapPrep);

            if (oldFormat) {
                List<JSONEvent> json = BaseBranchUtils.getAllAsJSON(allBranches);
                jsonString = siteJsonSerializer.toJson(json);
            } else {

                JSONResult json = BaseBranchUtils.getAllJSON(allBranches);
                jsonString = siteJsonSerializer.toJson(json);
            }
        } catch (Exception e) {
            logger.fatal("processGetBetsCustomNew2 cms () error", e);
        }
        long end = System.currentTimeMillis();
        logger.info("processGetBetsCustomNew2 cms() finished; " + (end - start));
        return jsonString;
    }

    public static String processGetTunnelBetsCustomNew(List<BetSite> sList, Map<BetSite, Double> cms, boolean oldFormat, int userStatus, String lang) {
        long start = System.currentTimeMillis();
        String jsonString = "{error}";
        try {
            ConcurrentMap<Long, ConcurrentMap<BetType, ForksEngine.BetsHolder>> bets = ForksEngine.getActualBets(); //TODO rewrite for use forks

            Map<EventKey, Map<BetType, BetKey>> betsMapPrep = new HashMap<>();

            bets.values().forEach(map -> {
                map.forEach((type, holder) -> {
                    EventKey key = new EventKey(holder.match);
                    Map<BetType, BetKey> betsMap = betsMapPrep.get(key);
                    if (betsMap == null) {
                        betsMap = new HashMap<BetType, BetKey>();
                    }
                    ForksEngine.BetVal val;
                    Optional opt = holder.bets.stream()
                            .filter(betVal -> sList.contains(betVal.site))
                            .max(new Comparator<ForksEngine.BetVal>() {
                                @Override
                                public int compare(ForksEngine.BetVal o1, ForksEngine.BetVal o2) {
                                    Double val1 = o1.val;
                                    Double val2 = o2.val;

                                    if (cms.keySet().contains(o1.site)) {
                                        val1 = o1.val * ((100.0 - cms.get(o1.site)) / 100.0);
                                    }
                                    if (cms.keySet().contains(o2.site)) {
                                        val2 = o2.val * ((100.0 - cms.get(o2.site)) / 100.0);
                                    }
                                    return val1.compareTo(val2);
                                }
                            });
                    if (opt.isPresent()) {
                        val = (ForksEngine.BetVal) opt.get();


                        Double cmsSiteRate = cms.get(val.site);
                        BetKey betKey = new BetKey(val.site, val.val, val.site.getName());

                        if (cmsSiteRate != null) {
                            Double realK = val.val * ((100.0 - cmsSiteRate) / 100.0);
                            betKey = new BetKey(val.site, realK, val.val, val.site.getName());
                        }
                        betsMap.put(type, betKey);
                    }
                    if (!betsMap.isEmpty()) {
                        betsMapPrep.put(key, betsMap);
                    }
                });
            });

            Map<EventKey, List<Map<BaseScheme.ProfitType, BranchResult>>> allBranches = BaseBranchUtils.calculateAll(StaticCache.tunnels, betsMapPrep);

            if (oldFormat) {
                List<JSONEvent> json = BaseBranchUtils.getAllAsJSON(allBranches);
                jsonString = siteJsonSerializer.toJson(json);
            } else {

                JSONResult json = BaseBranchUtils.getAllJSON(allBranches);
                jsonString = siteJsonSerializer.toJson(json);
            }
        } catch (Exception e) {
            logger.fatal("processGetTunnelBetsCustomNew cms () error", e);
        }
        long end = System.currentTimeMillis();
        logger.info("processGetTunnelBetsCustomNew cms() finished; " + (end - start));
        return jsonString;
    }

    /*public static String processGetBetsCustomNew() {
        long start = System.currentTimeMillis();
        String jsonString = "{error}";
        try {
            Map<EventKey, Map<BetType, BetKey>> betsMapPrep = BaseBranchUtils.prepareBetsMapMatch(StaticCache.matches, null);
            Map<EventKey, List<Map<BaseScheme.ProfitType, BranchResult>>> allBranches = BaseBranchUtils.calculateAll(StaticCache.shemes, betsMapPrep);
            JSONResult json = BaseBranchUtils.getAllJSON(allBranches);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeSpecialFloatingPointValues();
            Gson gs = gsonBuilder.setPrettyPrinting().create();
            jsonString = gs.toJson(json);
        } catch (Exception e) {
            logger.fatal("processGetBetsCustomNew() error", e);
        }
        long end = System.currentTimeMillis();
        logger.info("processGetBetsCustomNew() finished; " + (end - start));
        return jsonString;
    }

    public static String processGetBetsCustomNew(List<BetSite> sList) {
        long start = System.currentTimeMillis();
        String jsonString = "{error}";
        try {
            Map<EventKey, Map<BetType, BetKey>> betsMapPrep = BaseBranchUtils.prepareBetsMapMatch(StaticCache.matches, sList);
            Map<EventKey, List<Map<BaseScheme.ProfitType, BranchResult>>> allBranches = BaseBranchUtils.calculateAll(StaticCache.shemes, betsMapPrep);
            JSONResult json = BaseBranchUtils.getAllJSON(allBranches);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeSpecialFloatingPointValues();
            Gson gs = gsonBuilder.setPrettyPrinting().create();
            jsonString = gs.toJson(json);
        } catch (Exception e) {
            logger.fatal("processGetBetsCustomNew() error", e);
        }
        long end = System.currentTimeMillis();
        logger.info("processGetBetsCustomNew() finished; " + (end - start));
        return jsonString;
    }

    public static String processGetBetsCustomNew(List<BetSite> sList, Map<BetSite, Double> cms) {
        long start = System.currentTimeMillis();
        String jsonString = "{error}";
        try {
            Map<EventKey, Map<BetType, BetKey>> betsMapPrep = BaseBranchUtils.prepareBetsMapMatches(StaticCache.matches, sList, cms);
            Map<EventKey, List<Map<BaseScheme.ProfitType, BranchResult>>> allBranches = BaseBranchUtils.calculateAll(StaticCache.shemes, betsMapPrep);
            JSONResult json = BaseBranchUtils.getAllJSON(allBranches);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeSpecialFloatingPointValues();
            Gson gs = gsonBuilder.setPrettyPrinting().create();
            jsonString = gs.toJson(json);
        } catch (Exception e) {
            logger.fatal("processGetBetsCustomNew() error", e);
        }
        long end = System.currentTimeMillis();
        logger.info("processGetBetsCustomNew(cms) finished; " + (end - start));
        return jsonString;
    }*/

    //***********************************************

    public static String processGetBetsCustom() {
        long start = System.currentTimeMillis();
        String jsonString = "{error}";
        try {
            Map<EventKey, Map<BetType, BetKey>> betsMapPrep = BaseBranchUtils.prepareBetsMapMatch(StaticCache.matches, null);
            Map<EventKey, List<Map<BaseScheme.ProfitType, BranchResult>>> allBranches = BaseBranchUtils.calculateAll(StaticCache.shemes, betsMapPrep);
            List<JSONEvent> json = BaseBranchUtils.getAllAsJSON(allBranches);
            long i = 1;
            for (JSONEvent event: json) {
                for (JSONProfit p: event.profits) {
                    p.forkId = i++;
                }
            }
            jsonString = siteJsonSerializer.toJson(json);
        } catch (Exception e) {
            logger.fatal("processGetBetsCustom() error", e);
        }
        long end = System.currentTimeMillis();
        logger.info("processGetBetsCustom() finished; " + (end - start));
        return jsonString;
    }

    public static String processGetBetsCustom(List<BetSite> sList) {
        long start = System.currentTimeMillis();
        String jsonString = "{error}";
        try {
            Map<EventKey, Map<BetType, BetKey>> betsMapPrep = BaseBranchUtils.prepareBetsMapMatch(StaticCache.matches, sList);
            Map<EventKey, List<Map<BaseScheme.ProfitType, BranchResult>>> allBranches = BaseBranchUtils.calculateAll(StaticCache.shemes, betsMapPrep);
            List<JSONEvent> json = BaseBranchUtils.getAllAsJSON(allBranches);
            long i = 1;
            for (JSONEvent event: json) {
                for (JSONProfit p: event.profits) {
                    p.forkId = i++;
                }
            }
            jsonString = siteJsonSerializer.toJson(json);
        } catch (Exception e) {
            logger.fatal("processGetBetsCustom() error", e);
        }
        long end = System.currentTimeMillis();
        logger.info("processGetBetsCustom() finished; " + (end - start));
        return jsonString;
    }

    public static String processGetBetsCustom(List<BetSite> sList, Map<BetSite, Double> cms) {
        long start = System.currentTimeMillis();
        String jsonString = "{error}";
        try {
            Map<EventKey, Map<BetType, BetKey>> betsMapPrep = BaseBranchUtils.prepareBetsMapMatches(StaticCache.matches, sList, cms);
            Map<EventKey, List<Map<BaseScheme.ProfitType, BranchResult>>> allBranches = BaseBranchUtils.calculateAll(StaticCache.shemes, betsMapPrep);
            List<JSONEvent> json = BaseBranchUtils.getAllAsJSON(allBranches);
            long i = 1;
            for (JSONEvent event: json) {
                for (JSONProfit p: event.profits) {
                    p.forkId = i++;
                }
            }
            jsonString = siteJsonSerializer.toJson(json);
        } catch (Exception e) {
            logger.fatal("processGetBetsCustom() error", e);
        }
        long end = System.currentTimeMillis();
        logger.info("processGetBetsCustom(cms) finished; " + (end - start));
        return jsonString;
    }

    public static String processGetTunnelBetsCustom(List<BetSite> sList) {
        long start = System.currentTimeMillis();
        String jsonString = "{error}";
        try {
            Map<EventKey, Map<BetType, BetKey>> betsMapPrep = BaseBranchUtils.prepareBetsMapMatch(StaticCache.matches, sList);
            //Map<EventKey, Map<Bet, List<BetKey>>> betsMapAll = BaseBranchUtils.prepareBetsMapAll(events);
            Map<EventKey, List<Map<BaseScheme.ProfitType, BranchResult>>> allBranches = BaseBranchUtils.calculateAll(StaticCache.tunnels, betsMapPrep);
            List<JSONEvent> json = BaseBranchUtils.getAllAsJSON(allBranches);
            long i = 1;
            for (JSONEvent event: json) {
                for (JSONProfit p: event.profits) {
                    p.forkId = i++;
                }
            }
            jsonString = siteJsonSerializer.toJson(json);
        } catch (Exception e) {
            logger.fatal("processGetTunnelBetsCustom() error", e);
        }
        long end = System.currentTimeMillis();
        logger.info("processGetTunnelBetsCustom() finished; " + (end - start));
        return jsonString;
    }

    public static String processGetTunnelBetsCustom(List<BetSite> sList, Map<BetSite, Double> cms) {
        long start = System.currentTimeMillis();
        String jsonString = "{error}";
        try {
            Map<EventKey, Map<BetType, BetKey>> betsMapPrep = BaseBranchUtils.prepareBetsMapMatches(StaticCache.matches, sList, cms);
            Map<EventKey, List<Map<BaseScheme.ProfitType, BranchResult>>> allBranches = BaseBranchUtils.calculateAll(StaticCache.tunnels, betsMapPrep);
            List<JSONEvent> json = BaseBranchUtils.getAllAsJSON(allBranches);
            long i = 1;
            for (JSONEvent event: json) {
                for (JSONProfit p: event.profits) {
                    p.forkId = i++;
                }
            }
            jsonString = siteJsonSerializer.toJson(json);
        } catch (Exception e) {
            logger.fatal("processGetTunnelBetsCustom() error", e);
        }
        long end = System.currentTimeMillis();
        logger.info("processGetTunnelBetsCustom(cms) finished; " + (end - start));
        return jsonString;
    }

    public static void processGetBets() {
        //BaseGenericObjectPool<WebDriver> pool = new GenericObjectPool<WebDriver>(new WebDriverFactory());
        //pool.setMaxTotal(3);
        //pool.setBlockWhenExhausted(true);

        ExecutorService parserTasksPool = Executors.newFixedThreadPool(4, BaseParser.getThreadFactory("processGetBets"));
        List<Future<List<BasicEvent>>> parserStates = new ArrayList<Future<List<BasicEvent>>>();
        List<BaseParser> parsers = new ArrayList<BaseParser>();
        parsers.add(new RemoteParser(null, null, "clewcms.ru"));
        //parsers.add(new RemoteParser(null, null, "217.147.90.216"));

        parsers.add(new RemoteParser(null, null, "195.154.106.41")); //isfootball

        //parsers.add(new WinlineBetParser(pool)); //++
        //parsers.add(new SBobetParser(pool)); //++
        //parsers.add(new PinParser(pool));
        //parsers.add(new ZenitbetParser(pool)); //++

        //parsers.add(new SMarketsParser(pool));
        //parsers.add(new TennisiParser(pool));
        //parsers.add(new PlanetWin365Parser(pool)); //-------
        //parsers.add(new MarathonBetParser(pool));
        //parsers.add(new BetInParser(pool));
        //parsers.add(new Bet188Parser(pool));
        //parsers.add(new MatchBookParser(pool)); //-------
        //parsers.add(new GoBetGoParser());!
        //parsers.add(new RuUnibetParser(pool));
        //parsers.add(new OneXBetParser(pool)); //-------
        //parsers.add(new InterwettenParser(pool));
        //parsers.add(new OlimpkzParser(pool));
        //parsers.add(new PariMatchParser(pool));
        //parsers.add(new BetfairParser(pool));
        //parsers.add(new LigaStavokParser(pool));
        //parsers.add(new CoralParser(pool));
        //parsers.add(new BetcityParser(pool));
        //parsers.add(new BetwayParser(pool));
        //parsers.add(new CashPointParser(pool));

        List<BasicEvent> events = new ArrayList<BasicEvent>();

        logger.info("start threads for parse");
        for (BaseParser p: parsers) {
            parserStates.add(parserTasksPool.submit(p));
        }
        for (Future<List<BasicEvent>> f: parserStates) {
            try {
                    events.addAll(f.get());
            } catch (Exception e) {
                logger.error("processGetBets: future get() exception - ", e);
            }
        }
        logger.info("threads for parse finished");
        //pool.destroyAll();
        parserTasksPool.shutdownNow();
        logger.info("start calculate branches");
        try {
            Map<EventKey, Map<BetType, BetKey>> betsMapPrep = BaseBranchUtils.prepareBetsMap(events);
            //Map<EventKey, Map<Bet, List<BetKey>>> betsMapAll = BaseBranchUtils.prepareBetsMapAll(events);
            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE html><html><body>");
            sb.append("*******************************************************\n");
            sb.append("Date: ").append(new Date().toString()).append("<br>");
            for (EventKey e : betsMapPrep.keySet()) {
                if (e.date != null) {
                    sb.append("<b>Матч: ").append(e.date).append(";</b><br><b>").append(e.team1).append(" - ").append(e.team2).append("</b>");
                } else {
                    sb.append("<b>Матч: ").append("unknown").append(";</b><br><b>").append(e.team1).append(" - ").append(e.team2).append("</b>");
                }
                sb.append("<br>Коэффициенты:<br>");
                Map<BetType, BetKey> bets = betsMapPrep.get(e);
                for (BetType b : bets.keySet()) {
                    BetKey tempKey = bets.get(b);
                    sb.append(b.getRusName()).append("; K - ").append(tempKey.kReal).append("; ").append(tempKey.site).append("<br>");
                }
                sb.append("<b>Вилки:</b><br>");
                Map<EventKey, Map<BetType, BetKey>> eMap = new HashMap<EventKey, Map<BetType, BetKey>>();
                eMap.put(e, bets);
                for (BaseScheme s : StaticCache.shemes) {
                    sb.append("<b>Схема: ").append(s.getClass().getName()).append("</b><br>");
                    sb.append(s.getHtmlBranches(eMap, 10000.0));
                    sb.append("<br>");
                }
            }
            sb.append("*******************************************************");
            sb.append("</body></html>");
            final String out = sb.toString();
            StaticCache.htmlBranches = out;

            //table
            StringBuilder sbTable = new StringBuilder();
            sbTable.append("<!DOCTYPE html><html>");
            sbTable.append("<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js\"></script>  \n" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"https://cdn.datatables.net/1.10.7/css/jquery.dataTables.css\">\n" +
                    "<script type=\"text/javascript\" charset=\"utf8\" src=\"https://cdn.datatables.net/1.10.7/js/jquery.dataTables.js\"></script>\n" +
                    "<script>\n" +
                    "$(document).ready(function() {\n" +
                    "    $('#example').dataTable( {\n" +
                    "        \"order\": [[ 3, \"desc\" ]]\n" +
                    "    } );\n" +
                    "} );\n" +
                    "</script>\n");
            sbTable.append("<body>");
            sbTable.append("<table forkId=\"example\" class=\"display\" cellspacing=\"0\" width=\"100%\">\n" +
                    "        <thead>\n" +
                    "            <tr>\n" +
                    "                <th>Date</th>\n" +
                    "                <th>Teams</th>\n" +
                    "                <th>formula</th>\n" +
                    "                <th>site1;bet;K;sum</th>\n" +
                    "                <th>site2;bet;K;sum</th>\n" +
                    "                <th>site3;bet;K;sum</th>\n" +
                    "                <th>Profit</th>\n" +
                    "            </tr>\n" +
                    "        </thead>\n" +
                    " \n" +
                    "        <tfoot>\n" +
                    "            <tr>\n" +
                    "                <th>Date</th>\n" +
                    "                <th>Teams</th>\n" +
                    "                <th>formula</th>\n" +
                    "                <th>site1;bet;K;sum</th>\n" +
                    "                <th>site2;bet;K;sum</th>\n" +
                    "                <th>site3;bet;K;sum</th>\n" +
                    "                <th>Profit</th>\n" +
                    "            </tr>\n" +
                    "        </tfoot>\n");
            sbTable.append("<tbody>");

            for (EventKey e : betsMapPrep.keySet()) {
                Map<BetType, BetKey> bets = betsMapPrep.get(e);
                Map<EventKey, Map<BetType, BetKey>> eMap = new HashMap<EventKey, Map<BetType, BetKey>>();
                eMap.put(e, bets);
                for (BaseScheme s : StaticCache.shemes) {
                    if (e.date != null) {
                        String row = s.getHtmlTable(e.date, e.team1, e.team2, eMap, 10000.0);
                        if (row != null) {
                            sbTable.append(row);
                        }
                    }
                }
            }

            sbTable.append("</tbody>");
            sbTable.append("</table>");
            sbTable.append("</body></html>");

            final String outTable = sbTable.toString();

            StaticCache.htmlTable = outTable;

            //calculate branches
            Map<EventKey, List<Map<BaseScheme.ProfitType, BranchResult>>> allBranches = BaseBranchUtils.calculateAll(StaticCache.shemes, betsMapPrep);
            List<JSONEvent> json = BaseBranchUtils.getAllAsJSON(allBranches);
            long i = 1;
            for (JSONEvent event: json) {
                for (JSONProfit p: event.profits) {
                    p.forkId = i++;
                }
            }
            StaticCache.branches = siteJsonSerializer.toJson(json);
            logger.info("calculate branches finished");

            //calculate tunnels
            allBranches = BaseBranchUtils.calculateAll(StaticCache.tunnels, betsMapPrep);
            json = BaseBranchUtils.getAllAsJSON(allBranches);
            i = 1;
            for (JSONEvent event: json) {
                for (JSONProfit p: event.profits) {
                    p.forkId = i++;
                }
            }
            StaticCache.tunnelBranches = siteJsonSerializer.toJson(json);

            logger.info("calculate tunnels finished");
            logger.info("cache updated");
        } catch (Exception e) {
            logger.fatal("Error calculating branches", e);
        }
        //StaticCache.events = events;
        //logger.info(out);

    }

    public static String getMatchesForLines() {
        List<Match> matches = BetDao.getMatchesWithoutBets();
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><body>");
        sb.append("*******************************************************");
        sb.append("<br>");
        matches.forEach(match -> {
            sb.append("<a href=\"").append("/getmatchbetlines?secret_code=123123&match=").append(match.getId()).append("\">")
                    .append(match.getTeam1()).append(" - ").append(match.getTeam2())
                    .append(" ; date - ").append(match.getMatchDay())
                    .append("</a><br>");
        });

        sb.append("*******************************************************");
        sb.append("</body></html>");
        return sb.toString();
    }

    public static String getBetLines(long id) {
        List<MatchLines.FormulaLine> lines = BaseBranchUtils.calculateMatchLines(StaticCache.shemes, id);

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head>");
        sb.append("<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>");
        sb.append("<script type=\"text/javascript\">");
        sb.append("google.load('visualization', '1.1', {packages: ['line']});");
        sb.append("google.setOnLoadCallback(drawChart);");

        sb.append("function drawChart() {");
        sb.append("var data = new google.visualization.DataTable();");
        sb.append("data.addColumn('string', 'Day');");
        sb.append("data.addColumn('number', 'Fork');");

        sb.append("data.addRows([");

        Map<String, List<MatchLines.FormulaLine>> topByDay =
            lines.stream().collect(Collectors.groupingBy(MatchLines.FormulaLine::getDay));

        List<List<MatchLines.FormulaLine>> types = topByDay.entrySet().stream()
                .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
                .map(e -> e.getValue())
                .collect(Collectors.toList());

        types.forEach( b -> {
            MatchLines.FormulaLine top = b.stream()
                    .max((MatchLines.FormulaLine f1, MatchLines.FormulaLine f2) -> f1.getProfit().compareTo(f2.getProfit())).get();
            sb.append("['")
                    .append(MatchLines.format.format(top.date))
                    .append("(")
                    .append(top.f.id).append(")").append("',")
                    .append((top.getProfit() - 1.0) * 100).append("],");
        });
        /*topByDay.forEach((a, b) -> {
            MatchLines.FormulaLine top = b.stream()
                    .max((MatchLines.FormulaLine f1, MatchLines.FormulaLine f2) -> f1.getProfit().compareTo(f2.getProfit())).get();
                sb.append("['").append(a).append("',")
                        .append((top.getProfit() - 1.0) * 100).append("],");
        });*/

        sb.append("]);");
        sb.append("var options = {\n" +
                "        chart: {\n" +
                "          title: 'Forks',\n" +
                "          subtitle: 'in persents (%)'\n" +
                "        },\n" +
                "        width: 1400,\n" +
                "        height: 800\n" +
                "      };");
        sb.append("var chart = new google.charts.Line(document.getElementById('linechart_material'));");
        sb.append("chart.draw(data, options);");
        sb.append("} </script>");
        sb.append("</head><body><div forkId=\"linechart_material\"></div></body></html>");

        return sb.toString();
    }

    public static void calculateBets() {
        long start = System.currentTimeMillis();
        try {
            Map<EventKey, Map<BetType, BetKey>> betsMapPrep = BaseBranchUtils.prepareBetsMapMatch(StaticCache.matches, null);
            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE html><html><body>");
            sb.append("*******************************************************\n");
            sb.append("Date: ").append(new Date().toString()).append("<br>");
            for (EventKey e : betsMapPrep.keySet()) {
                if (e.date != null) {
                    sb.append("<b>Матч: ").append(e.date).append(";</b><br><b>").append(e.team1).append(" - ").append(e.team2).append("</b>");
                } else {
                    sb.append("<b>Матч: ").append("unknown").append(";</b><br><b>").append(e.team1).append(" - ").append(e.team2).append("</b>");
                }
                sb.append("<br>Коэффициенты:<br>");
                Map<BetType, BetKey> bets = betsMapPrep.get(e);
                for (BetType b : bets.keySet()) {
                    BetKey tempKey = bets.get(b);
                    sb.append(b.getRusName()).append("; K - ").append(tempKey.kReal).append("; ").append(tempKey.site).append("<br>");
                }
                sb.append("<b>Вилки:</b><br>");
                Map<EventKey, Map<BetType, BetKey>> eMap = new HashMap<EventKey, Map<BetType, BetKey>>();
                eMap.put(e, bets);
                for (BaseScheme s : StaticCache.shemes) {
                    sb.append("<b>Схема: ").append(s.getClass().getName()).append("</b><br>");
                    sb.append(s.getHtmlBranches(eMap, 10000.0));
                    sb.append("<br>");
                }
            }
            sb.append("*******************************************************");
            sb.append("</body></html>");
            final String out = sb.toString();
            StaticCache.htmlBranches = out;

            //table
            StringBuilder sbTable = new StringBuilder();
            sbTable.append("<!DOCTYPE html><html>");
            sbTable.append("<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js\"></script>  \n" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"https://cdn.datatables.net/1.10.7/css/jquery.dataTables.css\">\n" +
                    "<script type=\"text/javascript\" charset=\"utf8\" src=\"https://cdn.datatables.net/1.10.7/js/jquery.dataTables.js\"></script>\n" +
                    "<script>\n" +
                    "$(document).ready(function() {\n" +
                    "    $('#example').dataTable( {\n" +
                    "        \"order\": [[ 3, \"desc\" ]]\n" +
                    "    } );\n" +
                    "} );\n" +
                    "</script>\n");
            sbTable.append("<body>");
            sbTable.append("<table forkId=\"example\" class=\"display\" cellspacing=\"0\" width=\"100%\">\n" +
                    "        <thead>\n" +
                    "            <tr>\n" +
                    "                <th>Date</th>\n" +
                    "                <th>Teams</th>\n" +
                    "                <th>formula</th>\n" +
                    "                <th>site1;bet;K;sum</th>\n" +
                    "                <th>site2;bet;K;sum</th>\n" +
                    "                <th>site3;bet;K;sum</th>\n" +
                    "                <th>Profit</th>\n" +
                    "            </tr>\n" +
                    "        </thead>\n" +
                    " \n" +
                    "        <tfoot>\n" +
                    "            <tr>\n" +
                    "                <th>Date</th>\n" +
                    "                <th>Teams</th>\n" +
                    "                <th>formula</th>\n" +
                    "                <th>site1;bet;K;sum</th>\n" +
                    "                <th>site2;bet;K;sum</th>\n" +
                    "                <th>site3;bet;K;sum</th>\n" +
                    "                <th>Profit</th>\n" +
                    "            </tr>\n" +
                    "        </tfoot>\n");
            sbTable.append("<tbody>");

            for (EventKey e : betsMapPrep.keySet()) {
                Map<BetType, BetKey> bets = betsMapPrep.get(e);
                Map<EventKey, Map<BetType, BetKey>> eMap = new HashMap<EventKey, Map<BetType, BetKey>>();
                eMap.put(e, bets);
                for (BaseScheme s : StaticCache.shemes) {
                    if (e.date != null) {
                        String row = s.getHtmlTable(e.date, e.team1, e.team2, eMap, 10000.0);
                        if (row != null) {
                            sbTable.append(row);
                        }
                    }
                }
            }

            sbTable.append("</tbody>");
            sbTable.append("</table>");
            sbTable.append("</body></html>");

            final String outTable = sbTable.toString();

            StaticCache.htmlTable = outTable;

            //calculate branches
            Map<EventKey, List<Map<BaseScheme.ProfitType, BranchResult>>> allBranches = BaseBranchUtils.calculateAll(StaticCache.shemes, betsMapPrep);
            List<JSONEvent> json = BaseBranchUtils.getAllAsJSON(allBranches);
            long i = 1;
            for (JSONEvent event: json) {
                for (JSONProfit p: event.profits) {
                    p.forkId = i++;
                }
            }
            StaticCache.branches = siteJsonSerializer.toJson(json);
            logger.info("calculate branches finished");

            //calculate tunnels
            allBranches = BaseBranchUtils.calculateAll(StaticCache.tunnels, betsMapPrep);
            json = BaseBranchUtils.getAllAsJSON(allBranches);
            i = 1;
            for (JSONEvent event: json) {
                for (JSONProfit p: event.profits) {
                    p.forkId = i++;
                }
            }
            StaticCache.tunnelBranches = siteJsonSerializer.toJson(json);
            long end = System.currentTimeMillis();

            logger.info("calculateBets finished; " + (end - start));
            logger.info("calculateBets cache updated");
        } catch (Exception e) {
            logger.fatal("Error calculateBets", e);
        }
    }

    static DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

    public static String calculateBetsTable() {
        long start = System.currentTimeMillis();
        try {
            Map<Integer, ForksEngine.ForkHolder> forks = ForksEngine.getActualForks();

            final Map<Long, FinalEvent> matches = ForksEngine.getMatches();

            forks.values()
                .stream()
                .filter((ForksEngine.ForkHolder val) -> {
                        if (val.getFormulaId() >= 10000) { //tunnels
                            return false;
                        }
                        return true;
                    })
                .forEach((ForksEngine.ForkHolder val) -> {
                    FinalEvent event = matches.get(val.matchId);
                    val.jsonProfit.forkLifeTime = (int) (System.currentTimeMillis() - val.bornTime) / 1000; //TODO make copy
                    event.profits.add(val.jsonProfit);
                });

            List<JSONEvent> json = matches.values()
                    .stream()
                    .filter(event -> {
                        if (event.profits.isEmpty()) {
                            return false;
                        }
                        return true;
                    })
                    .map(event -> {
                                return new JSONEvent(event, null);
                            }
                    )
                    .collect(Collectors.toList()) ;

            //table
            StringBuilder sbTable = new StringBuilder();
            sbTable.append("<!DOCTYPE html><html>");
            sbTable.append("<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js\"></script>  \n" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"https://cdn.datatables.net/1.10.7/css/jquery.dataTables.css\">\n" +
                    "<script type=\"text/javascript\" charset=\"utf8\" src=\"https://cdn.datatables.net/1.10.7/js/jquery.dataTables.js\"></script>\n" +
                    "<script>\n" +
                    "$(document).ready(function() {\n" +
                    "    $('#example').dataTable( {\n" +
                    "        \"order\": [[ 3, \"desc\" ]]\n" +
                    "    } );\n" +
                    "} );\n" +
                    "</script>\n");
            sbTable.append("<body>");
            sbTable.append("<table id=\"example\" class=\"display\" cellspacing=\"0\" width=\"100%\">\n" +
                    "        <thead>\n" +
                    "            <tr>\n" +
                    "                <th>Date ****</th>\n" +
                    "                <th>Teams</th>\n" +
                    "                <th>formula</th>\n" +
                    "                <th>forkLifeTime(seconds)</th>\n" +
                    "                <th>site1;bet;K;sum</th>\n" +
                    "                <th>site2;bet;K;sum</th>\n" +
                    "                <th>site3;bet;K;sum</th>\n" +
                    "                <th>Profit</th>\n" +
                    "            </tr>\n" +
                    "        </thead>\n" +
                    " \n" +
                    "        <tfoot>\n" +
                    "            <tr>\n" +
                    "                <th>Date</th>\n" +
                    "                <th>Teams</th>\n" +
                    "                <th>formula</th>\n" +
                    "                <th>forkLifeTime(seconds)</th>\n" +
                    "                <th>site1;bet;K;sum</th>\n" +
                    "                <th>site2;bet;K;sum</th>\n" +
                    "                <th>site3;bet;K;sum</th>\n" +
                    "                <th>Profit</th>\n" +
                    "            </tr>\n" +
                    "        </tfoot>\n");
            sbTable.append("<tbody>");

            for (JSONEvent j : json ) {
                for (JSONProfit p: j.profits) {
                    JSONOutcomeProfit profit = p.profits.get(BaseScheme.ProfitType.UNIFORM);
                    if (profit == null) {
                        continue;
                    }

                    sbTable.append("<tr>");
                    sbTable.append("<td>").append(format.format(j.date)).append("</td>");
                    sbTable.append("<td>").append(j.team1 + " - " + j.team2).append("</td>");
                    sbTable.append("<td>").append(p.formulaId).append("</td>");
                    sbTable.append("<td>").append(p.forkLifeTime).append("</td>");

                    JSONRate rate1 = profit.factor1;
                    sbTable.append("<td>").append(rate1.site).append(";  ").append(rate1.bet).append("; K - ")
                            .append(String.format("%.4f", rate1.val)).append("; C - ").append(String.format("%.2f", rate1.k * 10000.0)).append("</td>");

                    JSONRate rate2 = profit.factor2;
                    sbTable.append("<td>").append(rate2.site).append(";  ").append(rate2.bet).append("; K - ")
                            .append(String.format("%.4f", rate2.val)).append("; C - ").append(String.format("%.2f", rate2.k * 10000.0)).append("</td>");
                    if (profit.factor3 != null) {
                        JSONRate rate3 = profit.factor3;
                        sbTable.append("<td>").append(rate3.site).append(";  ").append(rate3.bet).append("; K - ")
                                .append(String.format("%.4f", rate3.val)).append("; C - ").append(String.format("%.2f", rate3.k * 10000.0)).append("</td>");
                    } else {
                        sbTable.append("<td>").append("none").append(";  ").append("none").append("; K - ")
                                .append("none").append("; C - ").append("none").append("</td>");
                    }
                    sbTable.append("<td>").append(String.format("%.2f", p.profitRate * 10000.0)).append("</td>");
                    sbTable.append("</tr>");
                }
            }

            sbTable.append("</tbody>");
            sbTable.append("</table>");
            sbTable.append("</body></html>");

            final String outTable = sbTable.toString();

            long end = System.currentTimeMillis();
            logger.info("calculateBetsTable finished; " + (end - start));
            return outTable;
        } catch (Exception e) {
            logger.fatal("Error calculateBetsTable", e);
        }
        return "error";
    }

    public static void main(String[] args) throws InterruptedException {
        //System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
        //DesireCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "C:\\webdriver\\phantomjs-2.0.0-windows\\bin\\phantomjs.exe");
        //Teams.Livingston.getRussian();
        HttpClientInitializer.setupConnection();
        //BaseGenericObjectPool<WebDriver> pool = new GenericObjectPool<WebDriver>(new WebDriverFactory());
        //pool.setMaxTotal(4);
        //pool.setBlockWhenExhausted(true);

        StringBuilder str = new StringBuilder();
        for (BetType bet: BetType.values()) {
            str.append("insert into bet_types (bet, bet_rus) values ('");
            str.append(bet.toString()).append("','").append(bet.getRusName()).append("');").append("\n");
        }

        /*DBConnection.createPool();

        BetUpdateConsumer.initialize();
        ForksEngine.init();

        Thread.sleep(10000);
        processGetBetsCustomNew2(new ArrayList<BetSite>() {{
            add(BetSite.MARATHONBET);
            add(BetSite.BETCITY);
        }}, new HashMap<BetSite, Double>() {{
            put(BetSite.MARATHONBET, 4.1);
            put(BetSite.BETCITY, 0.2);
        }}, false);
        processGetBetsAllNew(false);
        Thread.sleep(3000000);

        /*SiteUpdateProcessor.initializeConsumer();

        SiteUpdates upd = new SiteUpdates();
        upd.setUpdateTime(30);
        upd.setSite(BetSite.BETFAIR.toString());
        upd.setEventsNumber(300);
        upd.setDateAdded(new Date());

        //SiteUpdateProcessor.sendUpdate(upd);

        Thread.sleep(100000);

        BasicEvent event = new BasicEvent();
        event.team1 = Teams.Farenshe;
        event.team2 = Teams.Ayachcho;
        event.date = "today";
        event.day = new Date();
        event.link = "http";
        event.site = BetSite.BETFAIR;
        event.bets.put(BetType.P2X, "10.0");


        EventConsumer.initialize();
        EventSender.initialize();
        EventSender.sendEvent(event);*/


        //getInternalForks();
        //BaseBranchUtils.calculateAllLines(StaticCache.shemes);
        //List<BetLine> res = BetDao.getAllSitesBetsLine(new Formula(2, "Ф2(0) – Х – П1", BetType.F20, BetType.X, BetType.P1), 1480L);


        //BetUpdateConsumer.initialize();
        //BetUpdateProducer.initialize();

        //SiteUpdateProcessor.initialize();
        //EventConsumer.initialize();

        //EventSender.initialize();

        //Server.isMaster = true;

        /*ClassLoader classLoader = FuriousPool.class.getClassLoader();
        URL resource = classLoader.getResource("org/apache/http/message/BasicLineFormatter.class");
        System.out.println(resource);*/

        BasePool<WebDriver> pool = new BasePool<WebDriver>(new ChromeFactory());

        BasePoolConfig config = new BasePoolConfig();
        config.maxActiveObjectsNumber = 1;
        //FuriousPool<WebDriver> poolChrome = new FuriousPool<WebDriver>(new ChromeFactory(), config);
        //FuriousPool<WebDriver> poolChrome = new FuriousPool<WebDriver>(new FirefoxFactory("8087"), config);
        //FuriousPool<WebDriver> poolChrome = new FuriousPool<WebDriver>(new PhantomJSFactory(), config);


        SMarketsXmlParser pin = new SMarketsXmlParser(null);
        //pin.getEvents();
        List<BasicEvent> events = pin.getEvents();
        int i = 0;
        while(i == 0) {
            events = pin.getEvents();
            Thread.sleep(1000 * 30);
        }

        Map<EventKey, Map<BetType, BetKey>> betsMapPrep = BaseBranchUtils.prepareBetsMap(events);
        Map<EventKey, Map<BetType, List<BetKey>>> betsMapAll = BaseBranchUtils.prepareBetsMapAll(events);
        ExecutorService parserTasksPool = Executors.newFixedThreadPool(8);
        List<Future<List<BasicEvent>>> parserStates = new ArrayList<Future<List<BasicEvent>>>();

        List<BaseParser> parsers = new ArrayList<BaseParser>();
        parsers.add(new PinParser(pool));
        parsers.add(new SMarketsParser(pool));
        parsers.add(new TennisiParser(pool));
        parsers.add(new PlanetWin365Parser(pool));
        parsers.add(new MarathonBetParser(pool));
        parsers.add(new BetInParser(pool));
        parsers.add(new Bet188Parser(pool));
        parsers.add(new WinlineBetParser(pool));
        parsers.add(new MatchBookParser(pool));
        //parsers.add(new GoBetGoParser());!
        parsers.add(new RuUnibetParser(pool));
        parsers.add(new OneXBetParser(pool));
        parsers.add(new InterwettenParser(pool));
        parsers.add(new OlimpkzParser(pool));
        parsers.add(new PariMatchParser(pool));


        /*for (BaseParser p: parsers) {
            List e = p.getEvents();
            events.addAll(e);
        }*/
        for (BaseParser p: parsers) {
            parserStates.add(parserTasksPool.submit(p));
        }
        try {
            for (Future<List<BasicEvent>> f: parserStates) {
                events.addAll(f.get());
            }
        } catch (Exception e) {
            logger.error("future exception", e);
        }


        /*DateUtils.isSameDay(new Date(), new Date());

        Map<BasicEvent, Map<String, Map<Bet, String>>> bets = new HashMap<BasicEvent, Map<String, Map<Bet, String>>>();

        Map<Teams, Teams> teams = new HashMap<>();
        for (BasicEvent event: events) {
            Teams temp = teams.get(event.team1);
            if (temp != null) {
                logger.info(event.team1.toString());
                if (temp == event.team2) {
                    logger.info(event.team2.toString());
                }
            } else {
                teams.put(event.team1, event.team2);
            }
            temp = teams.get(event.team2);
            if (temp != null) {
                if (temp == event.team1) {
                    logger.info(event.team2.toString());
                }
            } else {
                teams.put(event.team2, event.team1);
            }
        }
        for (BasicEvent event: events) {
            Map<String, Map<Bet, String>> siteBets = bets.get(event);
            if (siteBets != null) {
                siteBets.put(event.site, event.bets);
            } else {
                siteBets = new HashMap<>();
                siteBets.put(event.site, event.bets);
                bets.put(event, siteBets);
            }
        }

        for (BasicEvent key: bets.keySet()) {
            Map<String, Map<Bet, String>> b = bets.get(key);
            if (b.size() > 1) {
                logger.info("AAAAAAAAAAAAA");
            }
        }*/

        logger.info(betsMapAll.toString());
        //Scheme1 sheme = new Scheme1();
        //sheme.calculateBranches(betsMapPrep, 10000.0);
        StringBuilder sb = new StringBuilder();
        for (EventKey e: betsMapPrep.keySet()) {
            sb.append("Матч: ").append(e.date).append(";\n").append(e.team1).append(" - ").append(e.team2);
            sb.append("\nКоэффициенты:\n");
            Map<BetType, BetKey> bets = betsMapPrep.get(e);
            for (BetType b: bets.keySet()) {
                BetKey tempKey = bets.get(b);
                sb.append(b.getRusName()).append("; K - ").append(tempKey.kReal).append("; ").append(tempKey.site).append("\n");
            }
            sb.append("Вилки:\n");
            Map<EventKey, Map<BetType, BetKey>> eMap = new HashMap<EventKey, Map<BetType, BetKey>>();
            eMap.put(e, bets);
            for (BaseScheme s : StaticCache.shemes) {
                sb.append("Схема: ").append(s.getClass().getName()).append("\n");
                sb.append(s.calculateBranches(eMap, 10000.0));
                sb.append("\n");
            }
        }
        final String out = sb.toString();
        logger.info(out);
    }

    public static void getInternalForks() {
        List<Match> matches = BetDao.getMatchesWithoutBets();

        final Map<BetSite, Long> sitesMap = new HashMap<>();
        final List<JSONResult> profits = new ArrayList<>();
        matches.stream().forEach( match -> {
            Arrays.asList(BetSite.values()).stream().forEach( site -> {
                Match siteBets = BetDao.getMaxBetsForMatch(match.getId(), site);
                siteBets.setTeam1(match.getTeam1());
                siteBets.setTeam2(match.getTeam2());
                final Match localM = match;
                Map<BetType, BetKey> betsMapPrep = BaseBranchUtils.prepareBetsMapMatch(siteBets, site);
                List<Map<BaseScheme.ProfitType, BranchResult>> allBranches = BaseBranchUtils.calculateAllForEvent(StaticCache.shemes, betsMapPrep);
                if (!allBranches.isEmpty()) {
                    JSONResult res = BaseBranchUtils.getAllJSON(allBranches);
                    logger.info("It's works");
                    sitesMap.put(site, 0L);
                    profits.add(res);
                }
            });
        });
        if (profits.isEmpty()) {

        }
    }

}
