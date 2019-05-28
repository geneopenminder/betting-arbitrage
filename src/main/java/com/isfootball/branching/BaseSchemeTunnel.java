package com.isfootball.branching;

import com.isfootball.model.BetKey;
import com.isfootball.model.EventKey;
import com.isfootball.parser.BetType;
import com.isfootball.parser.Teams;

import java.util.*;

/**
 * Created by Evgeniy Pshenitsin on 19.02.2015.
 */
public abstract class BaseSchemeTunnel extends BaseScheme {

    public boolean isTunnel() {
        return true;
    }

    @Override
    public Map<ProfitType, BranchResult> calculateByScheme(List<EventSum> bets, Formula f, Double totalSum) {
        Map<ProfitType, BranchResult> result = new HashMap<ProfitType, BranchResult>();
        BranchResult uniform = calculateTunnel(bets, f, totalSum);
        result.put(ProfitType.TUNNEL, uniform);
        return result;
    }

    protected abstract BranchResult calculateTunnel(List<EventSum> bets, Formula f, Double totalSum);

    @Override
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
                List<EventSum> bets = getBetsForFormula(formulaBets, events.get(event));
                if (bets != null) {
                    Map<ProfitType, BranchResult> results = calculateByScheme(bets, f, totalSum);
                    if (results != null && results.size() > 0  && results.get(ProfitType.TUNNEL).isProfit) {
                        eventProfit.add(results);
                    }
                } else {
                    //no bets
                }
            }
        }
        return result;
    }

    @Override
    public void calculateBranchesForEvent(Map<BetType, BetKey> event, List<Map<ProfitType, BranchResult>> eventProfit, Double totalSum) {
        for (Formula f: getFormulas()) {
            List<BetType> formulaBets = new ArrayList<BetType>();
            if (f.var1 != null)
                formulaBets.add(f.var1);
            if (f.var2 != null)
                formulaBets.add(f.var2);
            List<EventSum> bets = getBetsForFormula(formulaBets, event);
            if (bets != null) {
                Map<ProfitType, BranchResult> results = calculateByScheme(bets, f, totalSum);
                if (results != null && results.size() > 0  && results.get(ProfitType.TUNNEL).isProfit) {
                    eventProfit.add(results);
                }
            } else {
                //no bets
            }
        }
    }

    @Override
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
                    if (results != null && results.size() > 0 && results.get(ProfitType.TUNNEL).isProfit) {
                        eventProfit.add(results);
                        //profit!!!!
                        Double totalSumFormula = 0.0;
                        BranchResult resProfit = results.get(ProfitType.TUNNEL);

                        sb.append("<tr>");
                        sb.append("<td>").append(format.format(date)).append("</td>");
                        sb.append("<td>").append(team1.getEng() + " - " + team2.getEng()).append("</td>");
                        sb.append("<td>").append(f.id).append("</td>");
                        EventSum branch1 = resProfit.branches.get(0);
                        sb.append("<td>").append(branch1.site).append(";  ").append(branch1.bet.getRusName()).append("; K - ")
                                .append(String.format("%.3f", branch1.kReal)).append("; C - ").append(branch1.sum).append("</td>");

                        branch1 = resProfit.branches.get(1);
                        sb.append("<td>").append(branch1.site).append(";  ").append(branch1.bet.getRusName()).append("; K - ")
                                .append(String.format("%.3f", branch1.kReal)).append("; C - ").append(branch1.sum).append("</td>");

                        sb.append("<td>none").append("</td>");

                        sb.append("<td>").append(String.format("%.2f", branch1.profit)).append("</td>");
                        sb.append("</tr>");

                    }
                } else {
                    //no bets
                    return null;
                }
            }
        }
        final String out = sb.toString();
        //System.out.println(out);
        return out;
    }

    @Override
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
                    if (results != null && results.size() > 0 && results.get(ProfitType.TUNNEL).isProfit) {
                        eventProfit.add(results);
                        //profit!!!!
                        Double totalSumFormula = 0.0;
                        sb.append("Branch start ------------------------------------------------<br>");
                        BranchResult resProfit = results.get(ProfitType.TUNNEL);
                        sb.append("<br><b>formula - ").append(f.id).append(";").append(f.title).append("</b>");
                        sb.append("<br><b>Коридоры:</b>");
                        sb.append("<br>total sum - ").append(resProfit.totalSum);
                        sb.append("<br><b>total sum - ").append(resProfit.totalSum).append("</b>");
                        EventSum branch1 = resProfit.branches.get(0);
                        sb.append("<br>var 1 - ").append(branch1.site).append(";  ").append(branch1.bet.getRusName()).append("; K - ").append(branch1.kReal).append("; C - ").append(branch1.sum);
                        totalSumFormula += branch1.sum;
                        branch1 = resProfit.branches.get(1);
                        sb.append("<br>var 2 - ").append(branch1.site).append(";  ").append(branch1.bet.getRusName()).append("; K - ").append(branch1.kReal).append("; C - ").append(branch1.sum);
                        totalSumFormula += branch1.sum;
                        //totalSumFormula += branch1.sum;
                        sb.append("<br><b>Profit 1: ").append(resProfit.branches.get(0).profit).append("</b>");
                        sb.append("<br><b>Profit 2: ").append(resProfit.branches.get(1).profit).append("</b>");
                        sb.append("<br><b>Middle: ").append(resProfit.branches.get(2).profit).append("</b>");
                        sb.append("<br>totalSumFormula(Should be equals total sum): ").append(totalSumFormula);
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

}
