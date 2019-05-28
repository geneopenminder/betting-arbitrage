package com.isfootball.branching;

import com.isfootball.jdbc.BetDao;
import com.isfootball.jdbc.Match;
import com.isfootball.model.*;
import com.isfootball.parser.BetType;
import com.isfootball.parser.Teams;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Created by Evgeniy Pshenitsin on 03.02.2015.
 */
public abstract class BaseScheme {

    public static enum ProfitType {
        UNIFORM,
        UNIFORM_1_2,
        UNIFORM_2_3,
        UNIFORM_1_3,
        UNIQUE_1,
        UNIQUE_2,
        UNIQUE_3,
        TUNNEL;
    };

    public boolean isTunnel() {
        return false;
    }

    public abstract List<Formula> getFormulas();

    public abstract Map<ProfitType, BranchResult> calculateByScheme(List<EventSum> bets, Formula f, Double totalSum);

    static DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

    public String getHtmlTable(Date date, Teams team1, Teams team2, Map<EventKey, Map<BetType, BetKey>> events, Double totalSum) {
        Map<EventKey, List<Map<ProfitType, BranchResult>>> result = new HashMap<EventKey, List<Map<ProfitType, BranchResult>>>();
        StringBuilder sb = new StringBuilder();
        for (EventKey event: events.keySet()) {
            List<Map<ProfitType, BranchResult>> eventProfit = new ArrayList<Map<ProfitType, BranchResult>>();
            result.put(event, eventProfit);
            for (Formula f: getFormulas()) {
                List<BetType> formulaBets = new ArrayList<BetType>();
                if (f.var1 != null)
                    formulaBets.add(f.var1);
                if (f.var2 != null)
                    formulaBets.add(f.var2);
                if (f.var3 != null)
                    formulaBets.add(f.var3);
                List<EventSum> bets = getBetsForFormula(formulaBets, events.get(event));
                if (bets != null) {
                    Map<ProfitType, BranchResult> results = calculateByScheme(bets, f, totalSum);
                    if (results != null && results.size() > 0 && results.get(ProfitType.UNIFORM).isProfit) {
                        eventProfit.add(results);
                        //profit!!!!
                        Double totalSumFormula = 0.0;
                        BranchResult resProfit = results.get(ProfitType.UNIFORM);

                        sb.append("<tr>");
                        sb.append("<td>").append(format.format(date)).append("</td>");
                        sb.append("<td>").append(team1 + " - " + team2).append("</td>");
                        sb.append("<td>").append(f.id).append("</td>");
                        EventSum branch1 = resProfit.branches.get(0);
                        sb.append("<td>").append(branch1.site).append(";  ").append(branch1.bet.getRusName()).append("; K - ")
                                .append(String.format("%.5f", branch1.kReal)).append("; C - ").append(String.format("%.2f",branch1.sum)).append("</td>");

                        branch1 = resProfit.branches.get(1);
                        sb.append("<td>").append(branch1.site).append(";  ").append(branch1.bet.getRusName()).append("; K - ")
                                .append(String.format("%.5f", branch1.kReal)).append("; C - ").append(String.format("%.2f",branch1.sum)).append("</td>");

                        branch1 = resProfit.branches.get(2);
                        sb.append("<td>").append(branch1.site).append(";  ").append(branch1.bet.getRusName()).append("; K - ")
                                .append(String.format("%.5f", branch1.kReal)).append("; C - ").append(String.format("%.2f",branch1.sum)).append("</td>");

                        sb.append("<td>").append(String.format("%.2f", branch1.profit)).append("</td>");
                        sb.append("</tr>");

                    }
                } else {
                    //no bets
                }
            }
        }
        final String out = sb.toString();
        //System.out.println(out);
        return out;
    }

    public String getHtmlBranches(Map<EventKey, Map<BetType, BetKey>> events, Double totalSum) {
        Map<EventKey, List<Map<ProfitType, BranchResult>>> result = new HashMap<EventKey, List<Map<ProfitType, BranchResult>>>();
        StringBuilder sb = new StringBuilder();
        for (EventKey event: events.keySet()) {
            List<Map<ProfitType, BranchResult>> eventProfit = new ArrayList<Map<ProfitType, BranchResult>>();
            result.put(event, eventProfit);
            for (Formula f: getFormulas()) {
                List<BetType> formulaBets = new ArrayList<BetType>();
                if (f.var1 != null)
                    formulaBets.add(f.var1);
                if (f.var2 != null)
                    formulaBets.add(f.var2);
                if (f.var3 != null)
                    formulaBets.add(f.var3);
                List<EventSum> bets = getBetsForFormula(formulaBets, events.get(event));
                if (bets != null) {
                    Map<ProfitType, BranchResult> results = calculateByScheme(bets, f, totalSum);
                    if (results != null && results.size() > 0 && results.get(ProfitType.UNIFORM).isProfit) {
                        eventProfit.add(results);
                        //profit!!!!
                        Double totalSumFormula = 0.0;
                        sb.append("Branch start ------------------------------------------------<br>");
                        BranchResult resProfit = results.get(ProfitType.UNIFORM);
                        sb.append("<br><b>formula - ").append(f.id).append(";").append(f.title).append("</b>");
                        sb.append("<br><b>Равномерное распределение:</b>");
                        sb.append("<br>total sum - ").append(resProfit.totalSum);
                        EventSum branch1 = resProfit.branches.get(0);
                        sb.append("<br>var 1 - ").append(branch1.site).append(";  ").append(branch1.bet.getRusName()).append("; K - ").append(branch1.kReal).append("; C - ").append(branch1.sum);
                        totalSumFormula += branch1.sum;
                        branch1 = resProfit.branches.get(1);
                        sb.append("<br>var 2 - ").append(branch1.site).append(";  ").append(branch1.bet.getRusName()).append("; K - ").append(branch1.kReal).append("; C - ").append(branch1.sum);
                        totalSumFormula += branch1.sum;
                        branch1 = resProfit.branches.get(2);
                        sb.append("<br>var 3 - ").append(branch1.site).append(";  ").append(branch1.bet.getRusName()).append("; K - ").append(branch1.kReal).append("; C - ").append(branch1.sum);
                        totalSumFormula += branch1.sum;
                        sb.append("<br><b>Profit: ").append(branch1.profit).append("</b>");
                        sb.append("<br>totalSumFormula(Should be equals total sum): ").append(totalSumFormula);

                        resProfit = results.get(ProfitType.UNIFORM_1_2);
                        if (resProfit != null) {
                            sb.append("<br><b>Равномерное распределение по двум исходам 1-2:</b>");
                            fillProfitNonUniform(resProfit, sb);
                        }
                        resProfit = results.get(ProfitType.UNIFORM_1_3);
                        if (resProfit != null) {
                            sb.append("<br><b>Равномерное распределение по двум исходам 1-3:</b>");
                            fillProfitNonUniform(resProfit, sb);
                        }
                        resProfit = results.get(ProfitType.UNIFORM_2_3);
                        if (resProfit != null) {
                            sb.append("<br><b>Равномерное распределение по двум исходам 2-3:</b>");
                            fillProfitNonUniform(resProfit, sb);
                        }
                        resProfit = results.get(ProfitType.UNIQUE_1);
                        if (resProfit != null) {
                            sb.append("<br><b>Равномерное распределение на один исход 1:</b>");
                            fillProfitNonUniform(resProfit, sb);
                        }
                        resProfit = results.get(ProfitType.UNIQUE_2);
                        if (resProfit != null) {
                            sb.append("<br><b>Равномерное распределение на один исход 2:</b>");
                            fillProfitNonUniform(resProfit, sb);
                        }

                        resProfit = results.get(ProfitType.UNIQUE_3);
                        if (resProfit != null) {
                            sb.append("<br><b>Равномерное распределение на один исход 3:</b>");
                            fillProfitNonUniform(resProfit, sb);
                        }
                        sb.append("<br>Branch end ---------------------------------------------------\n");
                    }
                } else {
                    //no bets
                }
            }
        }
        final String out = sb.toString();
        //System.out.println(out);
        return out;
    }

    public Map<EventKey, List<Map<ProfitType, BranchResult>>> calculateBranches(Map<EventKey, Map<BetType, BetKey>> events, Double totalSum) {
        Map<EventKey, List<Map<ProfitType, BranchResult>>> result = new HashMap<EventKey, List<Map<ProfitType, BranchResult>>>();
        for (EventKey event: events.keySet()) {
            List<Map<ProfitType, BranchResult>> eventProfit = new ArrayList<Map<ProfitType, BranchResult>>();
            result.put(event, eventProfit);
            for (Formula f: getFormulas()) {
                List<BetType> formulaBets = new ArrayList<BetType>();
                if (f.var1 != null)
                    formulaBets.add(f.var1);
                if (f.var2 != null)
                    formulaBets.add(f.var2);
                if (f.var3 != null)
                    formulaBets.add(f.var3);
                List<EventSum> bets = getBetsForFormula(formulaBets, events.get(event));
                if (bets != null) {
                    Map<ProfitType, BranchResult> results = calculateByScheme(bets, f, totalSum);
                    if (results != null && results.size() > 0 && results.get(ProfitType.UNIFORM).isProfit) {
                        eventProfit.add(results);
                    }
                } else {
                    //no bets
                }
            }
        }
        return result;
    }

    public void calculateBranchesForEvent(Map<BetType, BetKey> event, List<Map<ProfitType, BranchResult>> eventProfit, Double totalSum) {
        for (Formula f: getFormulas()) {
            List<BetType> formulaBets = new ArrayList<BetType>();
            if (f.var1 != null)
                formulaBets.add(f.var1);
            if (f.var2 != null)
                formulaBets.add(f.var2);
            if (f.var3 != null)
                formulaBets.add(f.var3);
            List<EventSum> bets = getBetsForFormula(formulaBets, event);
            if (bets != null) {
                Map<ProfitType, BranchResult> results = calculateByScheme(bets, f, totalSum);
                if (results != null && results.size() > 0 && results.get(ProfitType.UNIFORM).isProfit) {
                    eventProfit.add(results);
                }
            } else {
                //no bets
            }
        }
    }

    public List<MatchLines> calculateLinesForEvent(Match match) {

        return getFormulas().parallelStream().map( f -> {
            MatchLines mLine = new MatchLines();
            List<BetLine> lines = BetDao.getAllSitesBetsLine(f, match.getId());
            for (BetLine line: lines) {
                //check for bet absent
                if (f.var3 != null && line.var3 == null) {
                    continue;
                }
                List<EventSum> bets = new ArrayList<>();
                if (f.var1 != null && line.var1 != null) {
                    EventSum eventS = new EventSum();
                    eventS.bet = f.var1;
                    eventS.site = line.site1;
                    eventS.kReal = line.var1;
                    eventS.link = "";
                    eventS.kFromSite = line.var1;
                    bets.add(eventS);
                }
                if (f.var2 != null && line.var2 != null) {
                    EventSum eventS = new EventSum();
                    eventS.bet = f.var2;
                    eventS.site = line.site2;
                    eventS.kReal = line.var2;
                    eventS.link = "";
                    eventS.kFromSite = line.var2;
                    bets.add(eventS);
                }
                if (f.var3 != null && line.var3 != null) {
                    EventSum eventS = new EventSum();
                    eventS.bet = f.var3;
                    eventS.site = line.site3;
                    eventS.kReal = line.var3;
                    eventS.link = "";
                    eventS.kFromSite = line.var3;
                    bets.add(eventS);
                }

                Map<ProfitType, BranchResult> results = calculateByScheme(bets, f, 10000.0);
                if (results != null && results.size() > 0 && results.get(ProfitType.UNIFORM).isProfit) {
                    mLine.fLines.add(new MatchLines.FormulaLine(f, results, line.added));
                }
            }
            return mLine;
        }).collect(Collectors.toList());
    }

    private void fillProfitNonUniform(BranchResult resProfit, StringBuilder sb) {
        Double totalSumFormula = 0.0;
        sb.append("<br><b>total sum - ").append(resProfit.totalSum).append("</b>");
        EventSum branch1 = resProfit.branches.get(0);
        sb.append("<br>var 1 - ").append(branch1.site).append(";  ").append(branch1.bet.getRusName()).append("; K - ").append(branch1.kReal).append("; C - ").append(branch1.sum);
        totalSumFormula += branch1.sum;
        branch1 = resProfit.branches.get(1);
        sb.append("<br>var 2 - ").append(branch1.site).append(";  ").append(branch1.bet.getRusName()).append("; K - ").append(branch1.kReal).append("; C - ").append(branch1.sum);
        totalSumFormula += branch1.sum;
        branch1 = resProfit.branches.get(2);
        sb.append("<br>var 3 - ").append(branch1.site).append(";  ").append(branch1.bet.getRusName()).append("; K - ").append(branch1.kReal).append("; C - ").append(branch1.sum);
        totalSumFormula += branch1.sum;
        sb.append("<br><b>Profit 1: ").append(resProfit.branches.get(0).profit).append("</b>");
        sb.append("<br><b>Profit 2: ").append(resProfit.branches.get(1).profit).append("</b>");
        sb.append("<br><b>Profit 3: ").append(resProfit.branches.get(2).profit).append("</b>");
        sb.append("<br>totalSumFormula(Should be equals total sum): ").append(totalSumFormula);
    }

    public List<EventSum> getBetsForFormula(List<BetType> bets, Map<BetType, BetKey> eventBets) {
        List<EventSum> result = new ArrayList<EventSum>();
        for (BetType b: bets) {
            //todo revert bets
            BetKey temp = eventBets.get(b);
            if (temp == null) {
                return null;
            }
            EventSum eventS = new EventSum();
            eventS.bet = b;
            eventS.site = temp.site;
            eventS.kReal = temp.kReal;
            eventS.link = temp.link;
            eventS.kFromSite = temp.kFromSite;
            result.add(eventS);
        }
        return result;
    }

    public KKK getK(List<EventSum> bets, Formula f) {
        KKK k = new KKK();
        for (EventSum sum: bets) {
            if (sum.bet == f.var1) {
                k.K1 = sum.kReal;
            } else if (sum.bet == f.var2) {
                k.K2 = sum.kReal;
            } else if (sum.bet == f.var3) {
                k.K3 = sum.kReal;
            }
        }
        return k;
    }

}
