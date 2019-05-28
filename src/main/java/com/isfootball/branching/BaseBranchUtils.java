package com.isfootball.branching;

import com.isfootball.jdbc.Bet;
import com.isfootball.jdbc.BetDao;
import com.isfootball.jdbc.Match;
import com.isfootball.model.*;
import com.isfootball.parser.BetSite;
import com.isfootball.parser.BetType;
import com.isfootball.parser.StaticCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

/**
 * Created by Evgeniy Pshenitsin on 03.02.2015.
 */
public class BaseBranchUtils {

    private static final Logger logger = LogManager.getLogger("branching");

    public List<EventSum> getBetsForScheme(List<BetType> bets, List<BasicEvent> events) {
        List<EventSum> result = new ArrayList<EventSum>();
        for (BetType b: bets) {
            Double maxK = 0.0;
            BetSite site = null;
            //todo revert bets
            for (BasicEvent e: events) {
                String k = e.bets.get(b);
                if (k != null) {
                    Double kVal = Double.parseDouble(k);
                    if (kVal > maxK) {
                        maxK = kVal;
                        site = e.site;
                    }
                }
            }
            if (maxK.equals(0.0)) {
                return null;
            } else {
                EventSum eventS = new EventSum();
                eventS.bet = b;
                eventS.site = site;
                eventS.kReal = maxK;
                result.add(eventS);
            }
        }
        return result;
    }

    public static Map<EventKey, Map<BetType, BetKey>> prepareBetsMap(List<BasicEvent> events, List<BetSite> sList, Map<BetSite, Double> cms) {
        Map<EventKey, Map<BetType, BetKey>> bets = new HashMap<EventKey, Map<BetType, BetKey>>();
        EventKey key = new EventKey();
        for (BasicEvent event: events) {
            if (event == null) {
                logger.error("event null!!!!!!!!!!!!!!!!!!");
                continue;
            }
            if (event.team1 == null || event.team2 == null) {
                logger.error("teams null error!! " + event.site == null ? "site nul!!!" : event.site );
                continue;
            }
            if (sList != null && !sList.contains(event.site)) {
                continue;
            }
            key.copyEvent(event);
            Map<BetType, BetKey> temp = bets.get(key);
            if (temp == null) {
                temp = new HashMap<BetType, BetKey>();
                for(BetType eventBet: event.bets.keySet()) {
                    final String kStr = event.bets.get(eventBet);
                    if (kStr != null && !kStr.isEmpty()) {
                        try {
                            final Double d = Double.parseDouble(kStr);
                            if (d == Double.NaN) {
                                logger.error("nan double", kStr);
                                continue;
                            }
                            if (d == 0.0) {
                                logger.error("zero double", kStr);
                                continue;
                            }
                            if (d == 1.0) {
                                logger.error("1.0 double", kStr);
                                continue;
                            }
                            Double cmsSiteRate = cms.get(event.site);
                            if (cmsSiteRate == null) {
                                temp.put(eventBet, new BetKey(event.site, d, event.link));
                            } else {
                                Double realK = d * ((100.0 - cmsSiteRate) / 100.0);
                                temp.put(eventBet, new BetKey(event.site, realK, d, event.link));
                            }
                        } catch (Exception e) {
                            logger.error("BaseBranchUtils " + eventBet +"{" + event.site + ";" + kStr.trim() + "} exception - ", e);
                            continue;
                        }
                    }
                }
                bets.put(new EventKey(event), temp);
            } else {
                for(BetType eventBet: event.bets.keySet()) {
                    final String kStr = event.bets.get(eventBet);
                    Double betK = 0.0;
                    if (kStr != null && !kStr.isEmpty()) {
                        try {
                            betK = Double.parseDouble(kStr);
                            if (betK == Double.NaN) {
                                logger.error("nan double", kStr);
                                continue;
                            }
                            if (betK == 0.0) {
                                logger.error("zero double", kStr);
                                continue;
                            }
                            if (betK == 1.0) {
                                logger.error("1.0 double", kStr);
                                continue;
                            }
                        } catch (Exception e) {
                            logger.error("BaseBranchUtils " + eventBet + "{" + event.site + ";" + kStr.trim() + "} exception - ", e);
                            continue;
                        }
                        BetKey tempKey = temp.get(eventBet);
                        Double cmsSiteRate = cms.get(event.site);
                        if (cmsSiteRate != null) {
                            Double realK = betK * ((100.0 - cmsSiteRate) / 100.0);
                            if ((tempKey == null) || (tempKey.kReal < realK)) {
                                temp.put(eventBet, new BetKey(event.site, realK, betK, event.link));
                            }
                        } else {
                            if ((tempKey == null) || (tempKey.kReal < betK)) {
                                temp.put(eventBet, new BetKey(event.site, betK, event.link));
                            }
                        }
                    }
                }
            }
        }
        return bets;
    }

    public static Map<EventKey, Map<BetType, BetKey>> prepareBetsMapMatches(List<Match> matches, List<BetSite> sList, Map<BetSite, Double> cms) {
        Map<EventKey, Map<BetType, BetKey>> bets = new HashMap<EventKey, Map<BetType, BetKey>>();
        for (Match match: matches) {
            EventKey key = new EventKey(match);
            if (key.team1 == null || key.team2 == null) {
                logger.fatal("teams null error!!");
                continue;
            }
            Map<BetType, BetKey> temp = bets.get(key);
            if (temp == null) {
                temp = new HashMap<BetType, BetKey>();
            }

            for (BetSite site : match.getBets().keySet()) {
                if (sList != null && !sList.contains(site)) {
                    continue;
                }
                for (BetType betType: match.getBets().get(site).keySet()) {
                    Bet bet = match.getBets().get(site).get(betType);
                    BetKey tempKey = temp.get(betType);
                    Double cmsSiteRate = cms.get(site);
                    if (cmsSiteRate != null) {
                        Double realK = bet.getBetVal() * ((100.0 - cmsSiteRate) / 100.0);
                        if ((tempKey == null) || (tempKey.kReal < realK)) {
                            temp.put(betType, new BetKey(site, realK, bet.getBetVal(), site.getName()));
                        }
                    } else {
                        if ((tempKey == null) || (tempKey.kReal < bet.getBetVal())) {
                            temp.put(betType, new BetKey(site, bet.getBetVal(), site.getName()));
                        }
                    }
                }
            }
            bets.put(key, temp);
        }
        return bets;
    }

    public static Map<EventKey, Map<BetType, BetKey>> prepareBetsMap(List<BasicEvent> events, List<BetSite> sList) {
        Map<EventKey, Map<BetType, BetKey>> bets = new HashMap<EventKey, Map<BetType, BetKey>>();
        EventKey key = new EventKey();
        for (BasicEvent event: events) {
            if (sList != null && !sList.contains(event.site)) {
                continue;
            }
            if (event == null) {
                continue;
            }

            if (event.team1 == null || event.team2 == null) {
                logger.error("teams null error!! " + event.site);
                continue;
            }
            key.copyEvent(event);
            Map<BetType, BetKey> temp = bets.get(key);
            if (temp == null) {
                temp = new HashMap<BetType, BetKey>();
                for(BetType eventBet: event.bets.keySet()) {
                    final String kStr = event.bets.get(eventBet);
                    if (kStr != null && !kStr.isEmpty()) {
                        try {
                            final Double d = Double.parseDouble(kStr);
                            if (d == Double.NaN) {
                                logger.error("nan double", kStr);
                                continue;
                            }
                            if (d == 0.0) {
                                logger.error("zero double", kStr);
                                continue;
                            }
                            if (d == 1.0) {
                                logger.error("1.0 double", kStr);
                                continue;
                            }
                            temp.put(eventBet, new BetKey(event.site, d, event.link));
                        } catch (Exception e) {
                            logger.error("BaseBranchUtils " + eventBet +"{" + event.site + ";" + kStr.trim() + "} exception - ", e);
                            continue;
                        }
                    }
                }
                bets.put(new EventKey(event), temp);
            } else {
                for(BetType eventBet: event.bets.keySet()) {
                    final String kStr = event.bets.get(eventBet);
                    Double betK = 0.0;
                    if (kStr != null && !kStr.isEmpty()) {
                        try {
                            betK = Double.parseDouble(kStr);
                            if (betK == Double.NaN) {
                                logger.error("nan double", kStr);
                                continue;
                            }
                            if (betK == 0.0) {
                                logger.error("zero double", kStr);
                                continue;
                            }
                            if (betK == 1.0) {
                                logger.error("1.0 double", kStr);
                                continue;
                            }
                        } catch (Exception e) {
                            logger.error("BaseBranchUtils " + eventBet + "{" + event.site + ";" + kStr.trim() + "} exception - ", e);
                            continue;
                        }
                        BetKey tempKey = temp.get(eventBet);
                        if ((tempKey == null) || (tempKey.kReal < betK)) {
                            temp.put(eventBet, new BetKey(event.site, betK, event.link));
                        }
                    }
                }
            }
        }
        return bets;
    }

    public static Map<EventKey, Map<BetType, BetKey>> prepareBetsMapMatch(List<Match> matches, List<BetSite> sList) {
        Map<EventKey, Map<BetType, BetKey>> bets = new HashMap<EventKey, Map<BetType, BetKey>>();
        for (Match match: matches) {
            EventKey key = new EventKey(match);
            if (key.team1 == null || key.team2 == null) {
                logger.fatal("teams null error!!");
                continue;
            }
            Map<BetType, BetKey> temp = bets.get(key);
            if (temp == null) {
                temp = new HashMap<BetType, BetKey>();
            }

            for (BetSite site : match.getBets().keySet()) {
                if (sList != null && !sList.contains(site)) {
                    continue;
                }
                for (BetType betType: match.getBets().get(site).keySet()) {
                    Bet bet = match.getBets().get(site).get(betType);
                    BetKey tempKey = temp.get(betType);
                    if ((tempKey == null) || (tempKey.kReal < bet.getBetVal())) {
                        temp.put(betType, new BetKey(bet.getBetSite(), bet.getBetVal(), bet.getBetSite().getName()));
                    }
                }
            }
            bets.put(key, temp);
        }
        return bets;
    }

    public static Map<BetType, BetKey> prepareBetsMapMatch(Match match, BetSite site) {
        Map<BetType, BetKey> bets = new HashMap<BetType, BetKey>();
            for (BetType betType: match.getBets().get(site).keySet()) {
                Bet bet = match.getBets().get(site).get(betType);
                BetKey tempKey = bets.get(betType);
                if ((tempKey == null) || (tempKey.kReal < bet.getBetVal())) {
                    bets.put(betType, new BetKey(bet.getBetSite(), bet.getBetVal(), bet.getBetSite().getName()));
                }
            }
        return bets;
    }

    public static Map<EventKey, Map<BetType, BetKey>> prepareBetsMap(List<BasicEvent> events) {
        Map<EventKey, Map<BetType, BetKey>> bets = new HashMap<EventKey, Map<BetType, BetKey>>();
        EventKey key = new EventKey();
        for (BasicEvent event: events) {
            //if (event.bets.isEmpty()) {
            //    continue;
            //}
            if (event == null) {
                logger.error("WTFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
                continue;
            }
            if (event.team1 == null || event.team2 == null) {
                logger.error("teams null error!! " + event.site == null ? "" : event.site);
                continue;
            }
            key.copyEvent(event);
            Map<BetType, BetKey> temp = bets.get(key);
            if (temp == null) {
                temp = new HashMap<BetType, BetKey>();
                for(BetType eventBet: event.bets.keySet()) {
                    final String kStr = event.bets.get(eventBet);
                    if (kStr != null && !kStr.isEmpty()) {
                        try {
                            final Double d = Double.parseDouble(kStr);
                            if (d == Double.NaN) {
                                logger.error("nan double", kStr);
                                continue;
                            }
                            if (d == 0.0) {
                                logger.error("zero double", kStr);
                                continue;
                            }
                            if (d == 1.0) {
                                logger.error("1.0 double", kStr);
                                continue;
                            }
                            temp.put(eventBet, new BetKey(event.site, d, event.link));
                        } catch (Exception e) {
                            logger.error("BaseBranchUtils " + eventBet +"{" + event.site + ";" + kStr.trim() + "} exception - ", e);
                            continue;
                        }
                    }
                }
                bets.put(new EventKey(event), temp);
            } else {
                for(BetType eventBet: event.bets.keySet()) {
                    final String kStr = event.bets.get(eventBet);
                    Double betK = 0.0;
                    if (kStr != null && !kStr.isEmpty()) {
                        try {
                            betK = Double.parseDouble(kStr);
                            if (betK == Double.NaN) {
                                logger.error("nan double", kStr);
                                continue;
                            }
                            if (betK == 0.0) {
                                logger.error("zero double", kStr);
                                continue;
                            }
                            if (betK == 1.0) {
                                logger.error("1.0 double", kStr);
                                continue;
                            }
                        } catch (Exception e) {
                            logger.error("BaseBranchUtils " + eventBet + "{" + event.site + ";" + kStr.trim() + "} exception - ", e);
                            continue;
                        }
                        BetKey tempKey = temp.get(eventBet);
                        if ((tempKey == null) || (tempKey.kReal < betK)) {
                            temp.put(eventBet, new BetKey(event.site, betK, event.link));
                        }
                    }
                }
            }
        }
        return bets;
    }

    public static Map<EventKey, Map<BetType, List<BetKey>>> prepareBetsMapAll(List<BasicEvent> events) {
        Map<EventKey, Map<BetType, List<BetKey>>> bets = new HashMap<EventKey, Map<BetType, List<BetKey>>>();
        EventKey key = new EventKey();
        for (BasicEvent event: events) {
            key.copyEvent(event);
            Map<BetType, List<BetKey>> temp = bets.get(key);
            if (temp == null) {
                temp = new HashMap<BetType, List<BetKey>>();
                for(BetType eventBet: event.bets.keySet()) {
                    final String kStr = event.bets.get(eventBet);
                    if (kStr != null && !kStr.isEmpty()) {
                        try {
                            Double betK = Double.parseDouble(kStr);
                            if (betK == Double.NaN) {
                                logger.error("nan double", kStr);
                                continue;
                            }
                            if (betK == 0.0) {
                                logger.error("zero double", kStr);
                                continue;
                            }
                            if (betK == 1.0) {
                                logger.error("1.0 double", kStr);
                                continue;
                            }
                            BetKey bk = new BetKey(event.site, betK, event.link);
                            List<BetKey> list = new ArrayList<BetKey>();
                            list.add(bk);
                            temp.put(eventBet, list);
                        } catch (Exception e) {
                            logger.error("BaseBranchUtils " + eventBet + "{" + event.site + ";" + kStr.trim() + "} exception - ", e);
                            continue;
                        }
                    }
                }
                bets.put(new EventKey(event), temp);
            } else {
                for(BetType eventBet: event.bets.keySet()) {
                    final String kStr = event.bets.get(eventBet);
                    Double betK = 0.0;
                    if (kStr != null && !kStr.isEmpty()) {
                        try {
                            betK = Double.parseDouble(kStr);
                            if (betK == Double.NaN) {
                                logger.error("nan double", kStr);
                                continue;
                            }
                            if (betK == 0.0) {
                                logger.error("zero double", kStr);
                                continue;
                            }
                            if (betK == 1.0) {
                                logger.error("1.0 double", kStr);
                                continue;
                            }
                        } catch (Exception e) {
                            logger.error("BaseBranchUtils " + eventBet + "{" + event.site + ";" + kStr.trim() + "} exception - ", e);
                            continue;
                        }
                        List<BetKey> tempKey = temp.get(eventBet);
                        if ((tempKey == null)) {
                            List<BetKey> list = new ArrayList<BetKey>();
                            list.add(new BetKey(event.site, betK, event.link));
                            temp.put(eventBet, list);
                        } else {
                            tempKey.add(new BetKey(event.site, betK, event.link));
                        }
                    }
                }
            }
        }
        return bets;
    }

    public static Map<EventKey, List<Map<BaseScheme.ProfitType, BranchResult>>> calculateAll(List<BaseScheme> shemes, Map<EventKey, Map<BetType, BetKey>> events) {
        Map<EventKey, List<Map<BaseScheme.ProfitType, BranchResult>>> result = new HashMap<EventKey, List<Map<BaseScheme.ProfitType, BranchResult>>>();
        for (EventKey event : events.keySet()) {
            List<Map<BaseScheme.ProfitType, BranchResult>> eventProfit = new ArrayList<Map<BaseScheme.ProfitType, BranchResult>>();
            result.put(event, eventProfit);
            for (BaseScheme s : shemes) {
                s.calculateBranchesForEvent(events.get(event), eventProfit, 10000.0);
            }
        }
        return result;
    }

    public static List<Map<BaseScheme.ProfitType, BranchResult>> calculateAllForEvent(List<BaseScheme> shemes, Map<BetType, BetKey> event) {
        List<Map<BaseScheme.ProfitType, BranchResult>> eventProfit = new ArrayList<Map<BaseScheme.ProfitType, BranchResult>>();
        for (BaseScheme s : shemes) {
            s.calculateBranchesForEvent(event, eventProfit, 10000.0);
        }
        return eventProfit;
    }

    public static List<MatchLines.FormulaLine> calculateMatchLines(final List<BaseScheme> shemes, long matchId) {
        Match match = BetDao.getMatch(matchId);

        try {
            List<MatchLines> lines = new ArrayList<MatchLines>();
            shemes.forEach(s -> {
                List<MatchLines> l = s.calculateLinesForEvent(match);
                if (!l.isEmpty()) {
                    lines.addAll(l);
                }
            });
            List<MatchLines.FormulaLine> fLines = new ArrayList<>();
            lines.stream().filter(l -> l.fLines != null && !l.fLines.isEmpty())
                    .forEach(l -> {
                fLines.addAll(l.fLines);
            });
            return fLines;
        } catch (Exception e) {
            logger.error("calculateAllLines error", e);
        }
        return new ArrayList<MatchLines.FormulaLine>();
    }

    public static List<JSONEvent> getAllAsJSON(Map<EventKey, List<Map<BaseScheme.ProfitType, BranchResult>>> result) {
        List<JSONEvent> finalList = new ArrayList<JSONEvent>();

        List<BranchResult> sortedBranches = new ArrayList<>();

        for (EventKey event: result.keySet()) {
            for (Map<BaseScheme.ProfitType, BranchResult> profitsMap: result.get(event)) {
                if (profitsMap.get(BaseScheme.ProfitType.UNIFORM) != null &&
                        profitsMap.get(BaseScheme.ProfitType.UNIFORM).getJSONOutcomeProfit(BaseScheme.ProfitType.UNIFORM) != null) {
                    sortedBranches.add(profitsMap.get(BaseScheme.ProfitType.UNIFORM));
                }
            }
        }
        int size = 200;
        sortedBranches.sort(new BranchResultComparator());
        sortedBranches = sortedBranches.subList(sortedBranches.size() < size ? 0 : sortedBranches.size() - size,
                sortedBranches.size());

        for (EventKey event: result.keySet()) {
            JSONEvent jsonEvent = event.getJSON();
            for (Map<BaseScheme.ProfitType, BranchResult> profitsMap: result.get(event)) {
                if (profitsMap.get(BaseScheme.ProfitType.UNIFORM) != null &&
                    !sortedBranches.contains(profitsMap.get(BaseScheme.ProfitType.UNIFORM))) {
                    continue;
                }
                JSONProfit jsonProfit = new JSONProfit();
                for (BaseScheme.ProfitType profitType: profitsMap.keySet()) {
                    BranchResult bRes = profitsMap.get(profitType);
                    JSONOutcomeProfit outcome = bRes.getJSONOutcomeProfit(profitType);
                    if (outcome != null) {
                        jsonProfit.profits.put(profitType, outcome);
                    }
                }
                if (jsonProfit.profits == null || jsonProfit.profits.isEmpty()) {
                    logger.error("getAllAsJSON profits null");
                } else {
                    jsonEvent.profits.add(jsonProfit);
                }
            }
            if(!jsonEvent.profits.isEmpty()) {
                finalList.add(jsonEvent);
            }
        }

        return finalList;
    }

    public static JSONResult getAllJSON(Map<EventKey, List<Map<BaseScheme.ProfitType, BranchResult>>> result) {
        List<JSONEvent> finalList = new ArrayList<JSONEvent>();
        JSONResult res = new JSONResult();

        List<BranchResult> sortedBranches = new ArrayList<>();

        for (EventKey event: result.keySet()) {
            for (Map<BaseScheme.ProfitType, BranchResult> profitsMap: result.get(event)) {
                if (profitsMap.get(BaseScheme.ProfitType.UNIFORM) != null &&
                        profitsMap.get(BaseScheme.ProfitType.UNIFORM).getJSONOutcomeProfit(BaseScheme.ProfitType.UNIFORM) != null) {
                    sortedBranches.add(profitsMap.get(BaseScheme.ProfitType.UNIFORM));
                }
            }
        }
        int size = 200;
        sortedBranches.sort(new BranchResultComparator());
        res.numBranches = sortedBranches.size();
        sortedBranches = sortedBranches.subList(sortedBranches.size() < size ? 0 : sortedBranches.size() - size,
                sortedBranches.size());

        for (EventKey event: result.keySet()) {
            JSONEvent jsonEvent = event.getJSON();
            for (Map<BaseScheme.ProfitType, BranchResult> profitsMap: result.get(event)) {
                if (profitsMap.get(BaseScheme.ProfitType.UNIFORM) != null &&
                        !sortedBranches.contains(profitsMap.get(BaseScheme.ProfitType.UNIFORM))) {
                    continue;
                }
                JSONProfit jsonProfit = new JSONProfit();
                for (BaseScheme.ProfitType profitType: profitsMap.keySet()) {
                    BranchResult bRes = profitsMap.get(profitType);
                    JSONOutcomeProfit outcome = bRes.getJSONOutcomeProfit(profitType);
                    if (outcome != null) {
                        jsonProfit.profits.put(profitType, outcome);
                    }
                }
                if (jsonProfit.profits == null || jsonProfit.profits.isEmpty()) {
                    logger.error("getAllAsJSON profits null");
                } else {
                    jsonEvent.profits.add(jsonProfit);
                }
            }
            if(!jsonEvent.profits.isEmpty()) {
                finalList.add(jsonEvent);
            }
        }
        res.data = finalList;
        return res;
    }

    public static JSONResult getAllJSON(List<Map<BaseScheme.ProfitType, BranchResult>> profitsList) {
        List<JSONEvent> finalList = new ArrayList<JSONEvent>();
        JSONResult res = new JSONResult();

        List<BranchResult> sortedBranches = new ArrayList<>();

        for (Map<BaseScheme.ProfitType, BranchResult> map: profitsList) {
            if (map.get(BaseScheme.ProfitType.UNIFORM) != null &&
                    map.get(BaseScheme.ProfitType.UNIFORM).getJSONOutcomeProfit(BaseScheme.ProfitType.UNIFORM) != null) {
                sortedBranches.add(map.get(BaseScheme.ProfitType.UNIFORM));
            }
        }
        sortedBranches.sort(new BranchResultComparator());
        res.numBranches = sortedBranches.size();

        JSONEvent jsonEvent = new JSONEvent();

        for (Map<BaseScheme.ProfitType, BranchResult> profitsMap: profitsList) {
            if (profitsMap.get(BaseScheme.ProfitType.UNIFORM) != null &&
                        !sortedBranches.contains(profitsMap.get(BaseScheme.ProfitType.UNIFORM))) {
                    continue;
                }
            JSONProfit jsonProfit = new JSONProfit();
            for (BaseScheme.ProfitType profitType: profitsMap.keySet()) {
                BranchResult bRes = profitsMap.get(profitType);
                JSONOutcomeProfit outcome = bRes.getJSONOutcomeProfit(profitType);
                if (outcome != null) {
                    jsonProfit.profits.put(profitType, outcome);
                }
            }
            if (jsonProfit.profits == null || jsonProfit.profits.isEmpty()) {
                logger.error("getAllAsJSON profits null");
            } else {
                jsonEvent.profits.add(jsonProfit);
            }
        }
        finalList.add(jsonEvent);
        res.data = finalList;
        return res;
    }

    static class BranchResultComparator implements Comparator<BranchResult> {
        @Override
        public int compare(BranchResult a, BranchResult b) {
            return a.getJSONOutcomeProfit(BaseScheme.ProfitType.UNIFORM).profitRate.
                    compareTo(b.getJSONOutcomeProfit(BaseScheme.ProfitType.UNIFORM).profitRate);
        }
    }

}
