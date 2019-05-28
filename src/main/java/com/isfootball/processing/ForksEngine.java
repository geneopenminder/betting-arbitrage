package com.isfootball.processing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.isfootball.branching.BaseScheme;
import com.isfootball.branching.BranchResult;
import com.isfootball.branching.EventSum;
import com.isfootball.branching.Formula;
import com.isfootball.jdbc.BetDao;
import com.isfootball.jdbc.Match;
import com.isfootball.model.FinalEvent;
import com.isfootball.model.JSONEvent;
import com.isfootball.model.JSONOutcomeProfit;
import com.isfootball.model.JSONProfit;
import com.isfootball.notify.EmailSender;
import com.isfootball.parser.BetSite;
import com.isfootball.parser.BetType;
import com.isfootball.parser.StaticCache;
import com.isfootball.parser.Teams;
import com.isfootball.processing.BetUpdateConsumer.BetUpdate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by Evgeniy Pshenitsin on 04.10.2015.
 */
public class ForksEngine {

    private static final Logger logger = LogManager.getLogger("server");

    private static Gson gson;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeSpecialFloatingPointValues();
        gson = gsonBuilder.setPrettyPrinting().create();
    }

    public static class BetVal implements Comparable<BetVal> {
        public BetSite site;
        public Double val;
        public long updated;
        public BetType type;

        public BetVal(BetSite site, Double val, long updated, BetType type) {
            this.site = site;
            this.val = val;
            this.updated = updated;
            this.type = type;
        }

        @Override
        public int compareTo(BetVal o) {
            if (o.site.equals(this.site)) {
                return 0;
            }
            if (this.val.compareTo(o.val) == 0) {
                return -1;
            }
            return this.val.compareTo(o.val);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof BetVal)) {
                return false;
            }

            BetVal b = (BetVal) o;

            if (b.site.equals(this.site)) {
                return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return site.hashCode();
        }

    }

    public static class FormulaBetVal {
        public BetSite site;
        public Double val;
        public BetType type;

        public FormulaBetVal(BetSite site, Double val, BetType type) {
            this.site = site;
            this.val = val;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof FormulaBetVal)) {
                return false;
            }

            FormulaBetVal b = (FormulaBetVal) o;

            if (b.type.equals(this.type) &&
                    b.site.equals(this.site)) {
                return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return type.hashCode();
        }

    }

    public static class BetsHolder {

        public TreeSet<BetVal> bets = new TreeSet<>();
        List<FormulaHolder> formulas;

        public Match match; //TODO
        public boolean isProfit;
    }

    public static class FormulaHolder {
        public Formula f;
        BetsHolder[] betHolders;
        List<ForkHolder> forks;

        Set<FormulaBetVal> vals;
        boolean isProfit = false;
        public long bornTime;
        int forkId = 0;

        public boolean isFilled() {
            if (f.var3 == null && vals.size() == 2) {
                return true;
            } else if (vals.size() == 3) {
                return true;
            }
            return false;
        }

        public FormulaHolder() {
        }

        public FormulaHolder(Formula f) {
            this.f = f;
            this.vals = new HashSet<>();
        }
    }

    public static class ForkHolder {
        public int forkId = 0;

        public long matchId;

        FormulaHolder formulaHolder;
        public BetSite[] sites;
        public FormulaBetVal[] vals;
        public long bornTime;

        public JSONProfit jsonProfit;

        public int getFormulaId() {
            return formulaHolder.f.id;
        }

        public ForkHolder(FormulaHolder formulaHolder) {
            this.formulaHolder = formulaHolder;
            if (formulaHolder.f.isTwoArgs()) {
                sites  = new BetSite[2];
                vals = new FormulaBetVal[2];
            } else {
                sites  = new BetSite[3];
                vals = new FormulaBetVal[3];
            }
        }

        public boolean isSame(ForkHolder holder) {
            if (formulaHolder.f.id != holder.formulaHolder.f.id) {
                return false;
            }

            if (formulaHolder.f.isTwoArgs()
                    && sites[0].equals(holder.sites[0])
                    && sites[1].equals(holder.sites[1])) {
                return true;
            } else if (sites[0].equals(holder.sites[0])
                    && sites[1].equals(holder.sites[1])
                    && sites[2].equals(holder.sites[2])){
                return true;
            }
            return false;
        }

    }

    private static ConcurrentMap<Long, ConcurrentMap<BetType, BetsHolder>> actualBets = new ConcurrentHashMap<>();

    private static ConcurrentMap<Long, FinalEvent> matches = new ConcurrentHashMap<>();

    private static ConcurrentMap<Integer, ForkHolder> actualForks = new ConcurrentHashMap();

    private static AtomicInteger forkIdSeq = new AtomicInteger();

    private final static ArrayBlockingQueue<BetUpdate> betUpdates = new ArrayBlockingQueue<BetUpdate>(20000);

    public static Map<Integer, ForkHolder> getActualForks() {
        return actualForks;
    }

    public static Map<Long, FinalEvent> getMatches() {
        Map<Long, FinalEvent> copy = matches.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> new FinalEvent(e.getValue())));
        return copy;
    }

    public static ConcurrentMap<Long, ConcurrentMap<BetType, BetsHolder>> getActualBets() {
        return actualBets;
    }

    public static List<BettingNotifyRule> rules = new ArrayList<BettingNotifyRule>() {{
        add( new BettingNotifyRule(Arrays.asList(BetSite.PINNACLE, BetSite.MARATHONBET), "asmmaster@mail.ru", 1.04));
        add( new BettingNotifyRule(Arrays.asList(BetSite.PINNACLE), "asmmaster@mail.ru", 1.00005));
        add( new BettingNotifyRule(Arrays.asList(BetSite.MARATHONBET), "asmmaster@mail.ru", 1.00005));
        add( new BettingNotifyRule(Arrays.asList(BetSite.BETCITY), "asmmaster@mail.ru", 1.00005));
        add( new BettingNotifyRule(Arrays.asList(BetSite.PLANETWIN365), "asmmaster@mail.ru", 1.00005));
        add( new BettingNotifyRule(Arrays.asList(BetSite.SMARKETS), "asmmaster@mail.ru", 1.00005));
        add( new BettingNotifyRule(Arrays.asList(BetSite.BETCITY, BetSite.MARATHONBET), "asmmaster@mail.ru", 1.05));
        add( new BettingNotifyRule(Arrays.asList(BetSite.MATCHBOOK, BetSite.SMARKETS, BetSite.BETVICTOR), "asmmaster@mail.ru", 1.05));
        add( new BettingNotifyRule(Arrays.asList(BetSite.MATCHBOOK), "asmmaster@mail.ru", 1.01));
    }};

    public static void pushUpdate(BetUpdate upd) {
        try {
            betUpdates.offer(upd, 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.fatal("ForksEngine pushUpdate", e);
        }
    }

    private static Map<Integer, BaseScheme> schemes = new HashMap<Integer, BaseScheme>() {{
        StaticCache.allSchemes.forEach( s -> {
            s.getFormulas().forEach( f -> {
                put(f.id, s);
            });

        });
    }};

    private static class CleanOlbBetsTask extends TimerTask{

        @Override
        public void run() {
            final long start = System.nanoTime();

            synchronized (ForksEngine.class) {
                final long now = System.currentTimeMillis();

                try {
                    actualBets.values()
                            .parallelStream()
                            .forEach(map -> {
                                map.values()
                                        .stream()
                                        .forEach((BetsHolder holder) -> {
                                            holder.bets.stream()
                                                    .collect(Collectors.toList()).stream()
                                                    //.filter(betVal -> (now - betVal.updated) > 10 * 1000) //test 10 sec
                                                    .filter(betVal -> (now - betVal.updated) > betVal.site.getTtl())
                                                    .forEach(betVal -> {
                                                        BetsHolder localH = holder;
                                                        localH.bets.remove(betVal);
                                                        logger.info("clean: remove bet: " + betVal.site + ";" + betVal.val +
                                                                ";" + betVal.type + " ; match - " + holder.match.getId());

                                                        //remove forks
                                                        holder.formulas.stream()
                                                                .filter(f -> !f.forks.isEmpty())
                                                                .forEach(f -> {
                                                                    final FormulaBetVal forkBet = new FormulaBetVal(betVal.site, betVal.val, betVal.type);
                                                                    f.forks.stream()
                                                                            .collect(Collectors.toList()).stream()
                                                                            .filter(fork -> Arrays.asList(fork.vals).contains(forkBet))
                                                                            .forEach(fork -> {
                                                                                logger.info("clean: fork live time: " + (System.currentTimeMillis() - fork.bornTime) / 1000);
                                                                                f.forks.remove(fork);
                                                                                actualForks.remove(fork.forkId);
                                                                                fork.formulaHolder.forks.remove(fork);
                                                                            });
                                                                });
                                                    });
                                        });
                            });
                }
                catch (Exception e) {
                    logger.info("CleanOlbBetsTask error : ", e);
                }
            }

            final long time = System.nanoTime() - start;
            logger.info("CleanOlbBetsTask time : " + time);
        }

    }

    public static void init() {
        List<Match> matches = BetDao.getMatches();

        matches.forEach(match -> {
            processMatch(match);
        });

        new Timer("CleanOlbBetsTask").scheduleAtFixedRate(new CleanOlbBetsTask(), 180 * 1000, 60 * 1000);

        //test
        //new Timer("CleanOlbBetsTask").scheduleAtFixedRate(new CleanOlbBetsTask(), 1000, 5 * 1000);

        Thread thread = new Thread(new Runnable(
        ) {
            @Override
            public void run() {
                logger.info("ForksEngine thread started");
                while (true) {
                    try {
                        BetUpdate upd = betUpdates.poll(1000, TimeUnit.MINUTES);
                        logger.info("ForksEngine update bet" + upd.val + "; " + upd.site);
                        if (upd.val <= 1.0) {
                            logger.info("ForksEngine update bet low" + upd.val + "; " + upd.site);
                        } else {
                            processBetUpdate2(upd);
                        }
                        logger.info("ForksEngine eventQueue size: " + betUpdates.size());
                    } catch (Exception e) {
                        logger.error("ForksEngine error", e);
                    }
                }
            }
        });
        thread.start();
    }

    private static void processMatch(Match match) {
        synchronized (ForksEngine.class) {

            FinalEvent e = new FinalEvent();
            e.date = match.getMatchDay();
            e.team1 = Teams.valueOf(match.getTeam1());
            e.team2 = Teams.valueOf(match.getTeam2());
            matches.put(match.getId(), e);

            ConcurrentMap<BetType, BetsHolder> betHolders = new ConcurrentHashMap<>();

            final List<FormulaHolder> formulaHolders = new ArrayList<>();

            StaticCache.allSchemes.forEach(scheme -> {
                scheme.getFormulas().forEach(f -> {

                    final FormulaHolder holder = new FormulaHolder();

                    formulaHolders.add(holder);

                    holder.f = f;
                    holder.vals = new HashSet<FormulaBetVal>();
                    holder.betHolders = f.isTwoArgs() ? new BetsHolder[2] : new BetsHolder[3];
                    holder.forks = new ArrayList<ForkHolder>();

                    BetsHolder betsHolder1 = betHolders.get(f.var1);
                    if (betsHolder1 == null) {
                        betsHolder1 = new BetsHolder();
                        betsHolder1.bets = new TreeSet<BetVal>(); //public TreeSet<BetVal> bets;
                        betsHolder1.formulas = new ArrayList<FormulaHolder>(); //List<FormulaHolder> formulas;
                        betsHolder1.formulas.add(holder);
                        betsHolder1.match = match;
                        betsHolder1.isProfit = false;
                        betHolders.put(f.var1, betsHolder1);
                    } else {
                        betsHolder1.formulas.add(holder);
                    }
                    holder.betHolders[0] = betsHolder1;

                    BetsHolder betsHolder2 = betHolders.get(f.var2);
                    if (betsHolder2 == null) {
                        betsHolder2 = new BetsHolder();
                        betsHolder2.bets = new TreeSet<BetVal>(); //public TreeSet<BetVal> bets;
                        betsHolder2.formulas = new ArrayList<FormulaHolder>(); //List<FormulaHolder> formulas;
                        betsHolder2.formulas.add(holder);
                        betsHolder2.match = match;
                        betsHolder2.isProfit = false;
                        betHolders.put(f.var2, betsHolder2);
                    } else {
                        betsHolder2.formulas.add(holder);
                    }
                    holder.betHolders[1] = betsHolder2;

                    if (!f.isTwoArgs()) {
                        BetsHolder betsHolder3 = betHolders.get(f.var3);
                        if (betsHolder3 == null) {
                            betsHolder3 = new BetsHolder();
                            betsHolder3.bets = new TreeSet<BetVal>(); //public TreeSet<BetVal> bets;
                            betsHolder3.formulas = new ArrayList<FormulaHolder>(); //List<FormulaHolder> formulas;
                            betsHolder3.formulas.add(holder);
                            betsHolder3.match = match;
                            betsHolder3.isProfit = false;
                            betHolders.put(f.var3, betsHolder3);
                        } else {
                            betsHolder3.formulas.add(holder);
                        }
                        holder.betHolders[2] = betsHolder3;
                    }
                });
            });

            actualBets.put(match.getId(), betHolders);

            //fill bet vals

            final long now = System.currentTimeMillis();
            match.getBets().forEach((site, bets) -> {
                bets.forEach((betType, val) -> {
                    if (val.getBetVal() <= 1.0) {
                        logger.info("bet <= 1.0; " + site);
                    } else {
                        BetsHolder betHolder = betHolders.get(betType);
                        if (betHolder != null) {
                            BetVal newVal = new BetVal(site, val.getBetVal(), now, betType);
                            betHolder.bets.add(newVal);
                            //calculate(betHolder);
                        } else {
                            logger.info("unused bet: " + betType);
                        }
                    }
                });
            });

            calculate(formulaHolders, match);
        }
    }

    private static void calculate(List<FormulaHolder> fHolder, Match match) {
        fHolder.forEach( (FormulaHolder f) -> {
            BaseScheme s = schemes.get(f.f.id);
            List<EventSum> bets = new ArrayList<>(3);
            if (f.f.isTwoArgs() &&
                    !f.betHolders[0].bets.isEmpty() &&
                    !f.betHolders[1].bets.isEmpty()) {
                f.betHolders[0].bets.forEach( betVal1 -> {
                    f.betHolders[1].bets.forEach(betVal2 -> {
                        bets.clear();
                        bets.add(new EventSum(betVal1.site, betVal1.type, betVal1.val));
                        bets.add(new EventSum(betVal2.site, betVal2.type, betVal2.val));

                        Map<BaseScheme.ProfitType, BranchResult> results = s.calculateByScheme(bets, f.f, 10000.0);
                        if (results != null && results.size() > 0
                                && ((!s.isTunnel() && results.get(BaseScheme.ProfitType.UNIFORM).isProfit)
                                || (s.isTunnel() && results.get(BaseScheme.ProfitType.TUNNEL).isProfit))) {
                            //check NAN
                            for (BaseScheme.ProfitType profitType: results.keySet()) {
                                BranchResult bRes = results.get(profitType);
                                JSONOutcomeProfit outcome = bRes.getJSONOutcomeProfit(profitType);
                                if (outcome.profitRate != null && outcome.profitRate.equals(Double.NaN)) {
                                    continue;
                                }
                                if (outcome.profitRate1 != null && outcome.profitRate1.equals(Double.NaN)) {
                                    continue;
                                }
                                if (outcome.profitRate2 != null && outcome.profitRate2.equals(Double.NaN)) {
                                    continue;
                                }
                                if (outcome.profitRate3 != null && outcome.profitRate3.equals(Double.NaN)) {
                                    continue;
                                }
                            }

                            ForkHolder forkHolder = new ForkHolder(f);
                            forkHolder.forkId = forkIdSeq.incrementAndGet();

                            forkHolder.sites[0] = betVal1.site;
                            forkHolder.sites[1] = betVal2.site;

                            forkHolder.vals[0] = new FormulaBetVal(betVal1.site, betVal1.val, betVal1.type);
                            forkHolder.vals[1] = new FormulaBetVal(betVal2.site, betVal2.val, betVal2.type);

                            forkHolder.bornTime = System.currentTimeMillis();

                            JSONProfit jsonProfit = new JSONProfit();
                            forkHolder.matchId = match.getId();
                            jsonProfit.formulaId = f.f.id;
                            jsonProfit.forkId = forkHolder.forkId;
                            for (BaseScheme.ProfitType profitType: results.keySet()) {
                                BranchResult bRes = results.get(profitType);
                                JSONOutcomeProfit outcome = bRes.getJSONOutcomeProfit(profitType);
                                if (outcome != null) {
                                    jsonProfit.profits.put(profitType, outcome);
                                }
                                if (profitType.equals(BaseScheme.ProfitType.UNIFORM)) {
                                    jsonProfit.profitRate = outcome.profitRate;
                                } else if (profitType.equals(BaseScheme.ProfitType.TUNNEL)) {
                                    jsonProfit.profitRate = outcome.profitRate;
                                }
                            }
                            forkHolder.jsonProfit = jsonProfit;

                            f.forks.add(forkHolder);
                            actualForks.put(forkHolder.forkId, forkHolder);

                            /*rules.stream().forEach( rule -> {
                                if (rule.sites.containsAll(forkHolder.sites)) {
                                    EmailSender.send(new EmailSender.Message(rule.email, forkHolder));
                                }
                            });*/
                        }
                    });
                });
            } else if (!f.f.isTwoArgs()
                    && !f.betHolders[0].bets.isEmpty()
                    && !f.betHolders[1].bets.isEmpty()
                    && !f.betHolders[2].bets.isEmpty()){
                //TODO optimize
                f.betHolders[0].bets.forEach( betVal1 -> {
                    f.betHolders[1].bets.forEach(betVal2 -> {
                        f.betHolders[2].bets.forEach(betVal3 -> {
                            bets.clear();
                            bets.add(new EventSum(betVal1.site, betVal1.type, betVal1.val));
                            bets.add(new EventSum(betVal2.site, betVal2.type, betVal2.val));
                            bets.add(new EventSum(betVal3.site, betVal3.type, betVal3.val));

                            Map<BaseScheme.ProfitType, BranchResult> results = s.calculateByScheme(bets, f.f, 10000.0);
                            if (results != null && results.size() > 0
                                    && ((!s.isTunnel() && results.get(BaseScheme.ProfitType.UNIFORM).isProfit)
                                    || (s.isTunnel() && results.get(BaseScheme.ProfitType.TUNNEL).isProfit))) {

                                //check NAN
                                for (BaseScheme.ProfitType profitType: results.keySet()) {
                                    BranchResult bRes = results.get(profitType);
                                    JSONOutcomeProfit outcome = bRes.getJSONOutcomeProfit(profitType);
                                    if (outcome.profitRate != null && outcome.profitRate.equals(Double.NaN)) {
                                        continue;
                                    }
                                    if (outcome.profitRate1 != null && outcome.profitRate1.equals(Double.NaN)) {
                                        continue;
                                    }
                                    if (outcome.profitRate2 != null && outcome.profitRate2.equals(Double.NaN)) {
                                        continue;
                                    }
                                    if (outcome.profitRate3 != null && outcome.profitRate3.equals(Double.NaN)) {
                                        continue;
                                    }
                                }

                                ForkHolder forkHolder = new ForkHolder(f);
                                forkHolder.forkId = forkIdSeq.incrementAndGet();

                                forkHolder.sites[0] = betVal1.site;
                                forkHolder.sites[1] = betVal2.site;
                                forkHolder.sites[2] = betVal3.site;

                                forkHolder.vals[0] = new FormulaBetVal(betVal1.site, betVal1.val, betVal1.type);
                                forkHolder.vals[1] = new FormulaBetVal(betVal2.site, betVal2.val, betVal2.type);
                                forkHolder.vals[2] = new FormulaBetVal(betVal3.site, betVal3.val, betVal3.type);

                                forkHolder.bornTime = System.currentTimeMillis();

                                JSONProfit jsonProfit = new JSONProfit();
                                forkHolder.matchId = match.getId();
                                jsonProfit.formulaId = f.f.id;
                                jsonProfit.forkId = forkHolder.forkId;
                                for (BaseScheme.ProfitType profitType: results.keySet()) {
                                    BranchResult bRes = results.get(profitType);
                                    JSONOutcomeProfit outcome = bRes.getJSONOutcomeProfit(profitType);
                                    if (outcome != null) {
                                        jsonProfit.profits.put(profitType, outcome);
                                    }
                                    if (profitType.equals(BaseScheme.ProfitType.UNIFORM)) {
                                        jsonProfit.profitRate = outcome.profitRate;
                                    }
                                }
                                forkHolder.jsonProfit = jsonProfit;

                                f.forks.add(forkHolder);
                                actualForks.put(forkHolder.forkId, forkHolder);

                                /*rules.stream().forEach(rule -> {
                                    if (rule.sites.containsAll(forkHolder.sites)) {
                                        EmailSender.send(new EmailSender.Message(rule.email, forkHolder));
                                    }
                                });*/
                            }
                        });
                    });
                });
            }
            //no bets for calculation
        });
    }

    private static Optional<ForkHolder> calculateBetsForFormula(List<EventSum> bets, FormulaHolder f) {
        final BaseScheme s = schemes.get(f.f.id);
        Map<BaseScheme.ProfitType, BranchResult> results = s.calculateByScheme(bets, f.f, 10000.0);
        if (results != null && results.size() > 0
                && ((!s.isTunnel() && results.get(BaseScheme.ProfitType.UNIFORM).isProfit)
                || (s.isTunnel() && results.get(BaseScheme.ProfitType.TUNNEL).isProfit))) {
            ForkHolder forkHolder = new ForkHolder(f);
            //forkHolder.forkId = forkIdSeq.incrementAndGet();
            //forkHolder.formulaHolder = f;

            //check NAN
            for (BaseScheme.ProfitType profitType: results.keySet()) {
                BranchResult bRes = results.get(profitType);
                JSONOutcomeProfit outcome = bRes.getJSONOutcomeProfit(profitType);
                if (outcome.profitRate != null && outcome.profitRate.equals(Double.NaN)) {
                    return Optional.empty();
                }
                if (outcome.profitRate1 != null && outcome.profitRate1.equals(Double.NaN)) {
                    return Optional.empty();
                }
                if (outcome.profitRate2 != null && outcome.profitRate2.equals(Double.NaN)) {
                    return Optional.empty();
                }
                if (outcome.profitRate3 != null && outcome.profitRate3.equals(Double.NaN)) {
                    return Optional.empty();
                }
            }



            forkHolder.sites[0] = bets.get(0).site;
            forkHolder.sites[1] = bets.get(1).site;

            final EventSum betVal1 = bets.get(0);
            final EventSum betVal2 = bets.get(1);

            forkHolder.vals[0] = new FormulaBetVal(betVal1.site, betVal1.kFromSite, betVal1.bet);
            forkHolder.vals[1] = new FormulaBetVal(betVal2.site, betVal2.kFromSite, betVal2.bet);

            if (!f.f.isTwoArgs()) {
                forkHolder.sites[2] = bets.get(2).site;
                final EventSum betVal3 = bets.get(2);
                forkHolder.vals[2] = new FormulaBetVal(betVal3.site, betVal3.kFromSite, betVal3.bet);
            }

            forkHolder.bornTime = System.currentTimeMillis();

            JSONProfit jsonProfit = new JSONProfit();
            jsonProfit.formulaId = f.f.id;
            for (BaseScheme.ProfitType profitType: results.keySet()) {
                BranchResult bRes = results.get(profitType);
                JSONOutcomeProfit outcome = bRes.getJSONOutcomeProfit(profitType);
                if (outcome != null) {
                    jsonProfit.profits.put(profitType, outcome);
                }
                if (profitType.equals(BaseScheme.ProfitType.UNIFORM)) {
                    jsonProfit.profitRate = outcome.profitRate;
                } else if (profitType.equals(BaseScheme.ProfitType.TUNNEL)) {
                    jsonProfit.profitRate = outcome.profitRate;
                }
            }
            forkHolder.jsonProfit = jsonProfit;
            return Optional.of(forkHolder);
        } else {
            return Optional.empty();
        }

    }

    private static void processBetUpdate2(BetUpdate upd) {
        final long start = System.nanoTime();
        synchronized (ForksEngine.class) {
            Map<BetType, BetsHolder> bets = actualBets.get(upd.matchId);
            if (bets == null) {
                //add new match
                processMatch(BetDao.getMatchWithBets(upd.matchId));
                logger.info("new match: " + upd.matchId);
            } else {
                final BetsHolder holder = bets.get(upd.bet);
                if (holder == null) {
                    logger.fatal("bet outside of formulas: " + upd.bet);
                    return;
                } else {
                    final BetVal newVal = new BetVal(upd.site, upd.val, System.currentTimeMillis(), upd.bet);
                    final boolean isNewBet; //TODO rewrite
                    //if (!upd.isSame) {
                        if (holder.bets.contains(newVal)) {
                            BetVal oldVal = holder.bets.stream().filter( betVal -> betVal.equals(newVal)).collect(Collectors.toList()).get(0); //TODO )
                            if (oldVal.val.equals(newVal.val)) {
                                logger.info("new val is the same!!! ooops");
                                holder.bets.remove(newVal); //only update time
                                holder.bets.add(newVal); //only update time
                                return;
                            }
                            isNewBet = false;
                        } else {
                            isNewBet = true;
                        }
                    logger.info("ForksEngine update bet: " + upd.val + "; " + upd.site + "betType: " + upd.bet + "; isNewBet - " + isNewBet + "; match - " + upd.matchId);
                    //} else {
                    //    holder.bets.remove(newVal); //TODO!!!!!!
                    //    holder.bets.add(newVal); //only update time
                    //    return;
                    //}
                    final List<EventSum> localBets = new ArrayList<>(3);

                    holder.formulas
                            .stream()
                            .forEach(formula -> {
                                List<BetsHolder> otherHolders = Arrays.asList(formula.betHolders).stream()
                                        .filter( h -> !h.equals(holder)).collect(Collectors.toList());

                                List<ForkHolder> localForks = new ArrayList<ForkHolder>();
                                if (formula.f.isTwoArgs()) {
                                    otherHolders.get(0).bets.stream().forEach(betVal2 -> {
                                        localBets.clear();
                                        localBets.add(new EventSum(newVal.site, newVal.type, newVal.val));
                                        localBets.add(new EventSum(betVal2.site, betVal2.type, betVal2.val));

                                        Optional<ForkHolder> result = calculateBetsForFormula(reorderForF(localBets, formula.f), formula);
                                        if (result.isPresent()) {
                                            localForks.add(result.get());
                                        }
                                    });
                                } else {
                                    otherHolders.get(0).bets.stream().forEach(betVal2 -> {
                                        otherHolders.get(1).bets.stream().forEach(betVal3 -> {
                                            localBets.clear();
                                            localBets.add(new EventSum(newVal.site, newVal.type, newVal.val));
                                            localBets.add(new EventSum(betVal2.site, betVal2.type, betVal2.val));
                                            localBets.add(new EventSum(betVal3.site, betVal3.type, betVal3.val));

                                            Optional<ForkHolder> result = calculateBetsForFormula(reorderForF(localBets, formula.f), formula);
                                            if (result.isPresent()) {
                                                localForks.add(result.get());
                                            }
                                        });
                                    });
                                }

                                if (!isNewBet) {
                                    List<ForkHolder> currentForks = new ArrayList<ForkHolder>(formula.forks);
                                    currentForks.forEach(curFork -> {
                                        Optional<ForkHolder> fork =
                                                localForks.stream()
                                                        .filter(localFork -> {
                                                            if (localFork.isSame(curFork)) {
                                                                return true;
                                                            }
                                                            return false;
                                                        }).findFirst();
                                        if (fork.isPresent()) {
                                            //fork still alive, update
                                            curFork.jsonProfit = fork.get().jsonProfit;
                                            curFork.jsonProfit.forkId = curFork.forkId;
                                            curFork.vals = fork.get().vals;
                                            curFork.sites = fork.get().sites;
                                            localForks.remove(fork.get());
                                        } else {
                                            //fork has die, remove (
                                            logger.info("bets: fork live time: " + (System.currentTimeMillis() - curFork.bornTime) / 1000);
                                            formula.forks.remove(curFork);
                                            actualForks.remove(curFork.forkId);
                                            curFork.formulaHolder.forks.remove(curFork);
                                        }
                                    });
                                }

                                //new forks
                                localForks.forEach( newFork -> {
                                    newFork.forkId = forkIdSeq.incrementAndGet();
                                    newFork.matchId = upd.matchId;
                                    formula.forks.add(newFork);
                                    newFork.formulaHolder = formula;
                                    actualForks.put(newFork.forkId, newFork);

                                    /*rules.stream().forEach(rule -> {
                                        if (rule.sites.containsAll(Arrays.asList(newFork.sites))
                                                && rule.limit < newFork.jsonEvent.profitRate) {
                                            EmailSender.send(new EmailSender.Message(rule.email, newFork));
                                        }
                                    });*/
                                });
                            });

                    holder.bets.remove(newVal); //update time TODO
                    holder.bets.add(newVal); //update time
                }
            }
        }
        final long time = System.nanoTime() - start;
        logger.info("processBetUpdate2 time : " + time);
    }

    private static List<EventSum> reorderForF(List<EventSum> bets, Formula f) {
        final List<EventSum> localBets = new ArrayList<>(3);
        if (f.var1 != null) {
            for (EventSum bet: bets) {
                if (bet.bet.equals(f.var1)) {
                    localBets.add(bet);
                }
            }
        }
        if (f.var2 != null) {
            for (EventSum bet: bets) {
                if (bet.bet.equals(f.var2)) {
                    localBets.add(bet);
                }
            }
        }
        if (f.var3 != null) {
            for (EventSum bet: bets) {
                if (bet.bet.equals(f.var3)) {
                    localBets.add(bet);
                }
            }
        }
        return localBets;

    }
}
