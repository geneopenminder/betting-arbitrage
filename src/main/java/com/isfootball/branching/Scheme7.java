package com.isfootball.branching;

/**
 * Created by Evgeniy Pshenitsin on 11.02.2015.
 */

import com.isfootball.model.BetKey;
import com.isfootball.model.EventKey;
import com.isfootball.parser.BetType;
import com.isfootball.parser.Teams;

import java.util.*;

public class Scheme7 extends BaseSchemeUniformOnly {

    //full
    static List<Formula> formulas = new ArrayList<Formula>() {{
        add(new Formula(405, "П1 – 2х", BetType.P1, BetType.P2X, null));
        add(new Formula(406, "П2 – 1X", BetType.P2, BetType.P1X, null));
        add(new Formula(407, "12 – X", BetType.P12, BetType.X, null));
        add(new Formula(408, "Ф1(-0,5) – Ф2(+0,5)", BetType.F1minus05, BetType.F2plus05, null));
        add(new Formula(409, "Ф2(-0,5) – Ф1(+0,5)", BetType.F2minus05, BetType.F1plus05, null));
        //add(new Formula(410, "Ф1(-1) – Ф2(+1)", BetType.F1minus1, BetType.F2plus1, null));
        //add(new Formula(411, "Ф2(+1) – Ф1(-1)", BetType.F2plus1, BetType.F1minus1, null));
        add(new Formula(412, "Ф1(-1,5) – Ф2(+1,5)", BetType.F1minus15, BetType.F2plus15, null));
        add(new Formula(413, "Ф2(-1,5) – Ф1(+1,5)", BetType.F2minus15, BetType.F1plus15, null));
        //add(new Formula(414, "Ф1(-2) – Ф2(+2)", BetType.F1minus2, BetType.F2plus2, null));
        //add(new Formula(415, "Ф2(+2) – Ф1(-2)", BetType.F2plus2, BetType.F1minus2, null));
        add(new Formula(416, "Ф1(-2,5) – Ф2(+2,5)", BetType.F1minus25, BetType.F2plus25, null));
        add(new Formula(417, "Ф2(-2,5) – Ф1(+2,5)", BetType.F2minus25, BetType.F1plus25, null));
        //add(new Formula(418, "Ф1(-3) – Ф2(+3)", BetType.F1minus3, BetType.F2plus3, null));
        //add(new Formula(419, "Ф2(+3) – Ф1(-3)", BetType.F2plus3, BetType.F1minus3, null));
        add(new Formula(420, "Ф1(-3,5) – Ф2(+3,5)", BetType.F1minus35, BetType.F2plus35, null));
        add(new Formula(421, "Ф2(-3,5) – Ф1(+3,5)", BetType.F2minus35, BetType.F1plus35, null));
        //add(new Formula(422, "Ф1(-4) – Ф2(+4)", BetType.F1minus4, BetType.F2plus4, null));
        //add(new Formula(423, "Ф2(+4) – Ф1(-4)", BetType.F2plus4, BetType.F1minus4, null));
        add(new Formula(424, "Ф1(-4,5) – Ф2(+4,5)", BetType.F1minus45, BetType.F2plus45, null));
        add(new Formula(425, "Ф2(-4,5) – Ф1(+4,5)", BetType.F2minus45, BetType.F1plus45, null));
        //add(new Formula(426, "Ф1(-5) – Ф2(+5)", BetType.F1minus5, BetType.F2plus5, null));
        //add(new Formula(427, "Ф2(+5) – Ф1(-5)", BetType.F2plus5, BetType.F1minus5, null));
        add(new Formula(428, "Ф1(-5,5) – Ф2(+5,5)", BetType.F1minus55, BetType.F2plus55, null));
        add(new Formula(429, "Ф2(-5,5) – Ф1(+5,5)", BetType.F2minus55, BetType.F1plus55, null));
        //add(new Formula(430, "Ф1(-6) – Ф2(+6)", BetType.F1minus6, BetType.F2plus6, null));
        //add(new Formula(431, "Ф2(+6) – Ф1(-6)", BetType.F2plus6, BetType.F1minus6, null));
        add(new Formula(432, "Ф1(-6,5) – Ф2(+6,5)", BetType.F1minus65, BetType.F2plus65, null));
        add(new Formula(433, "Ф2(-6,5) – Ф1(+6,5)", BetType.F2minus65, BetType.F1plus65, null));
        //add(new Formula(434, "Ф1(-7) – Ф2(+7)", BetType.F1minus7, BetType.F2plus7, null));
        //add(new Formula(435, "Ф2(+7) – Ф1(-7)", BetType.F2plus7, BetType.F1minus7, null));
        add(new Formula(436, "ТБ(0,5) – ТМ(0,5)", BetType.T05M, BetType.T05L, null));
        //add(new Formula(437, "ТБ(1) – ТМ(1)", BetType.T1M, BetType.T1L, null));
        add(new Formula(438, "ТБ(1,5) – ТМ(1,5)", BetType.T15M, BetType.T15L, null));
        //add(new Formula(439, "ТБ(2) – ТМ(2)", BetType.T2M, BetType.T2L, null));
        add(new Formula(440, "ТБ(2,5) – ТМ(2,5)", BetType.T25M, BetType.T25L, null));
        //add(new Formula(441, "ТБ(3) – ТМ(3)", BetType.T3M, BetType.T3L, null));
        add(new Formula(442, "ТБ(3,5) – ТМ(3,5)", BetType.T35M, BetType.T35L, null));
        //add(new Formula(443, "ТБ(4) – ТМ(4)", BetType.T4M, BetType.T4L, null));
        add(new Formula(444, "ТБ(4,5) – ТМ(4,5)", BetType.T45M, BetType.T45L, null));
        //add(new Formula(445, "ТБ(5) – ТМ(5)", BetType.T5M, BetType.T5L, null));
        add(new Formula(446, "ТБ(5,5) – ТМ(5,5)", BetType.T55M, BetType.T55L, null));
        //add(new Formula(447, "ТБ(6) – ТМ(6)", BetType.T6M, BetType.T6L, null));
        add(new Formula(448, "ТБ(6,5) – ТМ(6,5)", BetType.T65M, BetType.T65L, null));
        //add(new Formula(449, "ТБ(7) – ТМ(7)", BetType.T7M, BetType.T7L, null));
        add(new Formula(450, "Т1Б(0,5) – Т1М(0,5)", BetType.T1_05M, BetType.T1_05L, null));
        //add(new Formula(451, "Т1Б(1) – Т1М(1)", BetType.T1_1M, BetType.T1_1L, null));
        add(new Formula(452, "Т1Б(1,5) – Т1М(1,5)", BetType.T1_15M, BetType.T1_15L, null));
        //add(new Formula(453, "Т1Б(2) – Т1М(2)", BetType.T1_2M, BetType.T1_2L, null));
        add(new Formula(454, "Т1Б(2,5) – Т1М(2,5)", BetType.T1_25M, BetType.T1_25L, null));
        //add(new Formula(455, "Т1Б(3) – Т1М(3)", BetType.T1_3M, BetType.T1_3L, null));
        add(new Formula(456, "Т1Б(3,5) – Т1М(3,5)", BetType.T1_35M, BetType.T1_35L, null));
        //add(new Formula(457, "Т1Б(4) – Т1М(4)", BetType.T1_4M, BetType.T1_4L, null));
        add(new Formula(458, "Т1Б(4,5) – Т1М(4,5)", BetType.T1_45M, BetType.T1_45L, null));
        //add(new Formula(459, "Т1Б(5) – Т1М(5)", BetType.T1_5M, BetType.T1_5L, null));
        add(new Formula(460, "Т1Б(5,5) – Т1М(5,5)", BetType.T1_55M, BetType.T1_55L, null));
        //add(new Formula(461, "Т1Б(6) – Т1М(6)", BetType.T1_6M, BetType.T1_6L, null));
        add(new Formula(462, "Т1Б(6,5) – Т1М(6,5)", BetType.T1_65M, BetType.T1_65L, null));
        //add(new Formula(463, "Т1Б(7) – Т1М(7)", BetType.T1_7M, BetType.T1_7L, null));
        add(new Formula(464, "Т2Б(0,5) – Т2М(0,5)", BetType.T2_05M, BetType.T2_05L, null));
        //add(new Formula(465, "Т2Б(1) – Т2М(1)", BetType.T2_1M, BetType.T2_1L, null));
        add(new Formula(466, "Т2Б(1,5) – Т2М(1,5)", BetType.T2_15M, BetType.T2_15L, null));
        //add(new Formula(467, "Т2Б(2) – Т2М(2)", BetType.T2_2M, BetType.T2_2L, null));
        add(new Formula(468, "Т2Б(2,5) – Т2М(2,5)", BetType.T2_25M, BetType.T2_25L, null));
        //add(new Formula(469, "Т2Б(3) – Т2М(3)", BetType.T2_3M, BetType.T2_3L, null));
        add(new Formula(470, "Т2Б(3,5) – Т2М(3,5)", BetType.T2_35M, BetType.T2_35L, null));
        //add(new Formula(471, "Т2Б(4) – Т2М(4)", BetType.T2_4M, BetType.T2_4L, null));
        add(new Formula(472, "Т2Б(4,5) – Т2М(4,5)", BetType.T2_45M, BetType.T2_45L, null));
        //add(new Formula(473, "Т2Б(5) – Т2М(5)", BetType.T2_5M, BetType.T2_5L, null));
        add(new Formula(474, "Т2Б(5,5) – Т2М(5,5)", BetType.T2_55M, BetType.T2_55L, null));
        //add(new Formula(475, "Т2Б(6) – Т2М(6)", BetType.T2_6M, BetType.T2_6L, null));
        add(new Formula(476, "Т2Б(6,5) – Т2М(6,5)", BetType.T2_65M, BetType.T2_65L, null));
        //add(new Formula(477, "Т2Б(7) – Т2М(7)", BetType.T2_7M, BetType.T2_7L, null));
        add(new Formula(478, "Г1(-1) – Г2(+2)", BetType.G1minus1, BetType.G2plus2, null));
        add(new Formula(480, "Г2(-1) – Г1(+2)", BetType.G2minus1, BetType.G1plus2, null));
        add(new Formula(481, "Г1(-2) – Г2(+3)", BetType.G1minus2, BetType.G2plus3, null));
        add(new Formula(482, "Г2(-2) – Г1(+3)", BetType.G2minus2, BetType.G1plus3, null));
        add(new Formula(483, "Г1(-3) – Г2(+4)", BetType.G1minus3, BetType.G2plus4, null));
        add(new Formula(484, "Г2(-3) – Г1(+4)", BetType.G2minus3, BetType.G1plus4, null));
        add(new Formula(485, "Ф1(-0,5) – Г2(+1)", BetType.F1minus05, BetType.G2plus1, null));
        add(new Formula(486, "Ф2(-0,5) – Г1(+1)", BetType.F2minus05, BetType.G1plus1, null));
        add(new Formula(487, "П1 – Г2(+1)", BetType.P1, BetType.G2plus1, null));
        add(new Formula(488, "П2 – Г1(+1)", BetType.P2, BetType.G1plus1, null));
        add(new Formula(489, "Ф1(-1,5) – Г2(+2)", BetType.F1minus15, BetType.G2plus2, null));
        add(new Formula(490, "Ф2(-1,5) – Г1(+2)", BetType.F2minus15, BetType.G1plus2, null));
        add(new Formula(491, "Ф1(-2,5) – Г2(+3)", BetType.F1minus25, BetType.G2plus3, null));
        add(new Formula(492, "Ф2(-2,5) – Г1(+3)", BetType.F2minus25, BetType.G1plus3, null));
        add(new Formula(493, "Ф1(-3,5) – Г2(+4)", BetType.F1minus35, BetType.G2plus4, null));
        add(new Formula(494, "Ф2(-3,5) – Г1(+4)", BetType.F2minus35, BetType.G1plus4, null));
        add(new Formula(495, "Г1(-1) – Ф2(+1,5)", BetType.G1minus1, BetType.F2plus15, null));
        add(new Formula(496, "Г2(-1) – Ф1(+1,5)", BetType.G2minus1, BetType.F1plus15, null));
        add(new Formula(497, "Г1(-2) – Ф2(+2,5)", BetType.G1minus2, BetType.F2plus25, null));
        add(new Formula(498, "Г2(-2) – Ф1(+2,5)", BetType.G2minus2, BetType.F1plus25, null));
        add(new Formula(499, "Г1(-3) – Ф2(+3,5)", BetType.G1minus3, BetType.F2plus35, null));
        add(new Formula(500, "Г2(-3) – Ф1(+3,5)", BetType.G2minus3, BetType.F1plus35, null));
    }};

    @Override
    public List<Formula> getFormulas() {
        return formulas;
    }

    public List<EventSum> fillProfit(List<EventSum> bets, Formula f, Double C1, Double C2, Double K1, Double K2) {
        List<EventSum> betsResult = new ArrayList<EventSum>();
        for (EventSum sum: bets) {
            if (sum.bet == f.var1) {
                sum.sum = C1;
                sum.profit = C1*K1;
            } else if (sum.bet == f.var2) {
                sum.sum = C2;
                sum.profit = C2*K2;
            }
            betsResult.add(new EventSum(sum));
        }
        return betsResult;
    }

    protected BranchResult calculateUniform(List<EventSum> bets, Formula f, Double totalSum) {
        BranchResult branch = new BranchResult();
        branch.isProfit = true;
        branch.totalSum = totalSum;
        KKK k = getK(bets, f);
        Double K1 = k.K1;
        Double K2 = k.K2;

        Double X1 = 1 / K1 + 1 / K2;

        Double C1 = totalSum / (X1 * K1);
        Double C2 = totalSum / (X1 * K2);


        branch.branches = fillProfit(bets, f, C1, C2, K1, K2);
        for (EventSum sum: branch.branches) {
            if (sum.profit <= totalSum) {
                branch.isProfit = false;
            }
        }
        return branch;
    }

    @Override
    public KKK getK(List<EventSum> bets, Formula f) {
        KKK k = new KKK();
        for (EventSum sum: bets) {
            if (sum.bet == f.var1) {
                k.K1 = sum.kReal;
            } else if (sum.bet == f.var2) {
                k.K2 = sum.kReal;
            }
        }
        return k;
    }

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
                    if (results != null && results.size() > 0  && results.get(ProfitType.UNIFORM).isProfit) {
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
                if (results != null && results.size() > 0  && results.get(ProfitType.UNIFORM).isProfit) {
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
                    if (results != null && results.size() > 0 && results.get(ProfitType.UNIFORM).isProfit) {
                        eventProfit.add(results);
                        //profit!!!!
                        Double totalSumFormula = 0.0;
                        BranchResult resProfit = results.get(ProfitType.UNIFORM);

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
        StringBuilder sb = new StringBuilder();
        for (EventKey event: events.keySet()) {
            for (Formula f: getFormulas()) {
                List<BetType> formulaBets = new ArrayList<BetType>();
                if (f.var1 != null)
                    formulaBets.add(f.var1);
                if (f.var2 != null)
                    formulaBets.add(f.var2);
                List<EventSum> bets = getBetsForFormula(formulaBets, events.get(event));
                if (bets != null) {
                    Map<ProfitType, BranchResult> results = calculateByScheme(bets, f, totalSum);
                    if (results != null && results.size() > 0  && results.get(ProfitType.UNIFORM).isProfit) {
                        //profit!!!!
                        sb.append("Profit start ------------------------------------------------<br>");
                        BranchResult resProfit = results.get(ProfitType.UNIFORM);
                        sb.append("<br>formula - ").append(f.id).append(";").append(f.title);
                        sb.append("<br>total sum - ").append(resProfit.totalSum);
                        EventSum branch1 = resProfit.branches.get(0);
                        sb.append("<br>var 1 - ").append(branch1.site).append(";  ").append(branch1.bet.getRusName()).append("; K - ").append(branch1.kReal).append("; C - ").append(branch1.sum);
                        branch1 = resProfit.branches.get(1);
                        sb.append("<br>var 2 - ").append(branch1.site).append(";  ").append(branch1.bet.getRusName()).append("; K - ").append(branch1.kReal).append("; C - ").append(branch1.sum);
                        sb.append("<br>Profit sum: ").append(branch1.profit);
                        sb.append("<br>Profit end ---------------------------------------------------<br>");
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
