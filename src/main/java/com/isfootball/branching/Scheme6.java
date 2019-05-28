package com.isfootball.branching;

/**
 * Created by Evgeniy Pshenitsin on 11.02.2015.
 */

import com.isfootball.parser.BetType;

import java.util.ArrayList;
import java.util.List;

public class Scheme6 extends BaseSchemeNonUniform {

    //full
    static List<Formula> formulas = new ArrayList<Formula>() {{
        add(new Formula(307, "Ф1(+0,25) – 2х – П2", BetType.F1plus025, BetType.P2X, BetType.P2));
        add(new Formula(308, "Ф2(+0,25) – 1х – П1", BetType.F2plus025, BetType.P1X, BetType.P1));
        add(new Formula(309, "Ф1(+0,25) – 2х – Ф2(-0,5)", BetType.F1plus025, BetType.P2X, BetType.F2minus05));
        add(new Formula(310, "Ф2(+0,25) – 1х – Ф1(-0,5)", BetType.F2plus025, BetType.P1X, BetType.F1minus05));
        add(new Formula(311, "Ф1(+1,25) – П2 – Ф2(-1,5)", BetType.F1plus125, BetType.P2, BetType.F2minus15));
        add(new Formula(312, "Ф2(+1,25) – П1 – Ф1(-1,5)", BetType.F2plus125, BetType.P1, BetType.F1minus15));
        add(new Formula(313, "Ф1(+1,25) – Ф2(-0,5) – Ф2(-1,5)", BetType.F1plus125, BetType.F2minus05, BetType.F2minus15));
        add(new Formula(314, "Ф2(+1,25) – Ф1(-0,5) – Ф1(-1,5)", BetType.F2plus125, BetType.F1minus05, BetType.F1minus15));
        add(new Formula(315, "Ф1(+2,25) – Ф2(-1,5) – Ф2(-2,5)", BetType.F1plus225, BetType.F2minus15, BetType.F2minus25));
        add(new Formula(316, "Ф2(+2,25) – Ф1(-1,5) – Ф1(-2,5)", BetType.F2plus225, BetType.F1minus15, BetType.F1minus25));
        add(new Formula(317, "Ф1(+3,25) – Ф2(-2,5) – Ф2(-3,5)", BetType.F1plus325, BetType.F2minus25, BetType.F2minus35));
        add(new Formula(318, "Ф2(+3,25) – Ф1(-2,5) – Ф1(-3,5)", BetType.F2plus325, BetType.F1minus25, BetType.F1minus35));
        add(new Formula(319, "Ф1(+4,25) – Ф2(-3,5) – Ф2(-4,5)", BetType.F1plus425, BetType.F2minus35, BetType.F2minus45));
        add(new Formula(320, "Ф2(+4,25) – Ф1(-3,5) – Ф1(-4,5)", BetType.F2plus425, BetType.F1minus35, BetType.F1minus45));
        add(new Formula(321, "Ф1(+5,25) – Ф2(-4,5) – Ф2(-5,5)", BetType.F1plus525, BetType.F2minus45, BetType.F2minus55));
        add(new Formula(322, "Ф2(+5,25) – Ф1(-4,5) – Ф1(-5,5)", BetType.F2plus525, BetType.F1minus45, BetType.F1minus55));
        add(new Formula(323, "Ф1(+6,25) – Ф2(-5,5) – Ф2(-6,5)", BetType.F1plus625, BetType.F2minus55, BetType.F2minus65));
        add(new Formula(324, "Ф2(+6,25) – Ф1(-5,5) – Ф1(-6,5)", BetType.F2plus625, BetType.F1minus55, BetType.F1minus65));
        add(new Formula(325, "Ф1(-0,75) – Ф2(+1,5) – 2х", BetType.F1minus075, BetType.F2plus15, BetType.P2X));
        add(new Formula(326, "Ф2(-0,75) – Ф1(+1,5) – 1х", BetType.F2minus075, BetType.F1plus15, BetType.P1X));
        add(new Formula(327, "Ф1(-0,75) – Ф2(+1,5) – Ф2(+0,5)", BetType.F1minus075, BetType.F2plus15, BetType.F2plus05));
        add(new Formula(328, "Ф2(-0,75) – Ф1(+1,5) – Ф1(+0,5)", BetType.F2minus075, BetType.F1plus15, BetType.F1plus05));
        add(new Formula(329, "Ф1(-1,75) – Ф2(+2,5) – Ф2(+1,5)", BetType.F1minus175, BetType.F2plus25, BetType.F2plus15));
        add(new Formula(330, "Ф2(-1,75) – Ф1(+2,5) – Ф1(+1,5)", BetType.F2minus175, BetType.F1plus25, BetType.F1plus15));
        add(new Formula(331, "Ф1(-2,75) – Ф2(+3,5) – Ф2(+2,5)", BetType.F1minus275, BetType.F2plus35, BetType.F2plus25));
        add(new Formula(332, "Ф2(-2,75) – Ф1(+3,5) – Ф1(+2,5)", BetType.F2minus275, BetType.F1plus35, BetType.F1plus25));
        add(new Formula(333, "Ф1(-3,75) – Ф2(+4,5) – Ф2(+3,5)", BetType.F1minus375, BetType.F2plus45, BetType.F2plus35));
        add(new Formula(334, "Ф2(-3,75) – Ф1(+4,5) – Ф1(+3,5)", BetType.F2minus375, BetType.F1plus45, BetType.F1plus35));
        add(new Formula(335, "Ф1(-4,75) – Ф2(+5,5) – Ф2(+4,5)", BetType.F1minus475, BetType.F2plus55, BetType.F2plus45));
        add(new Formula(336, "Ф2(-4,75) – Ф1(+5,5) – Ф1(+4,5)", BetType.F2minus475, BetType.F1plus55, BetType.F1plus45));
        add(new Formula(337, "Ф1(-5,75) – Ф2(+6,5) – Ф2(+5,5)", BetType.F1minus575, BetType.F2plus65, BetType.F2plus55));
        add(new Formula(338, "Ф2(-5,75) – Ф1(+6,5) – Ф1(+5,5)", BetType.F2minus575, BetType.F1plus65, BetType.F1plus55));
        add(new Formula(339, "Ф1(+1,25) – Ф2(-0,5) – Г2(-1)", BetType.F1plus125, BetType.F2minus05, BetType.G2minus1));
        add(new Formula(340, "Ф2(+1,25) – Ф1(-0,5) – Г1(-1)", BetType.F2plus125, BetType.F1minus05, BetType.G1minus1));
        add(new Formula(341, "Ф1(+1,25) – П2 – Г2(-1)", BetType.F1plus125, BetType.P2, BetType.G2minus1));
        add(new Formula(342, "Ф2(+1,25) – П1 – Г1(-1)", BetType.F2plus125, BetType.P1, BetType.G1minus1));
        add(new Formula(343, "Ф1(+2,25) – Ф2(-1,5) – Г2(-2)", BetType.F1plus225, BetType.F2minus15, BetType.G2minus2));
        add(new Formula(344, "Ф2(+2,25) – Ф1(-1,5) – Г1(-2)", BetType.F2plus225, BetType.F1minus15, BetType.G1minus2));
        add(new Formula(345, "Ф1(+3,25) – Ф2(-2,5) – Г2(-3)", BetType.F1plus325, BetType.F2minus25, BetType.G2minus3));
        add(new Formula(346, "Ф2(+3,25) – Ф1(-2,5) – Г1(-3)", BetType.F2plus325, BetType.F1minus25, BetType.G1minus3));
        add(new Formula(347, "Ф1(+4,25) – Ф2(-3,5) – Г2(-4)", BetType.F1plus425, BetType.F2minus35, BetType.G2minus4));
        add(new Formula(348, "Ф2(+4,25) – Ф1(-3,5) – Г1(-4)", BetType.F2plus425, BetType.F1minus35, BetType.G1minus4));
        add(new Formula(349, "Ф1(+2,25) – Г2(-1) – Г2(-2)", BetType.F1plus225, BetType.G2minus1, BetType.G2minus2));
        add(new Formula(350, "Ф2(+2,25) – Г1(-1) – Г1(-2)", BetType.F2plus225, BetType.G1minus1, BetType.G1minus2));
        add(new Formula(351, "Ф1(+3,25) – Г2(-2) – Г2(-3)", BetType.F1plus325, BetType.G2minus2, BetType.G2minus3));
        add(new Formula(352, "Ф2(+3,25) – Г1(-2) – Г1(-3)", BetType.F2plus325, BetType.G1minus2, BetType.G1minus3));
        add(new Formula(353, "Ф1(+4,25) – Г2(-3) – Г2(-4)", BetType.F1plus425, BetType.G2minus3, BetType.G2minus4));
        add(new Formula(354, "Ф2(+4,25) – Г1(-3) – Г1(-4)", BetType.F2plus425, BetType.G1minus3, BetType.G1minus4));
        add(new Formula(355, "Ф1(-0,75) – Г2(+2) – 2х", BetType.F1minus075, BetType.G2plus2, BetType.P2X));
        add(new Formula(356, "Ф2(-0,75) – Г1(+2) – 1х", BetType.F2minus075, BetType.G1plus2, BetType.P1X));
        add(new Formula(357, "Ф1(-0,75) – Г2(+2) – Ф2(+0,5)", BetType.F1minus075, BetType.G2plus2, BetType.F2plus05));
        add(new Formula(358, "Ф2(-0,75) – Г1(+2) – Ф1(+0,5)", BetType.F2minus075, BetType.G1plus2, BetType.F1plus05));
        add(new Formula(359, "Ф1(-1,75) – Г2(+3) – Ф2(+1,5)", BetType.F1minus175, BetType.G2plus3, BetType.F2plus15));
        add(new Formula(360, "Ф2(-1,75) – Г1(+3) – Ф1(+1,5)", BetType.F2minus175, BetType.G1plus3, BetType.F1plus15));
        add(new Formula(361, "Ф1(-2,75) – Г2(+4) – Ф2(+2,5)", BetType.F1minus275, BetType.G2plus4, BetType.F2plus25));
        add(new Formula(362, "Ф2(-2,75) – Г1(+4) – Ф1(+2,5)", BetType.F2minus275, BetType.G1plus4, BetType.F1plus25));
        add(new Formula(363, "Ф1(-0,75) – Г2(+2) – Г2(+1)", BetType.F1minus075, BetType.G2plus2, BetType.G2plus1));
        add(new Formula(364, "Ф2(-0,75) – Г1(+2) – Г1(+1)", BetType.F2minus075, BetType.G1plus2, BetType.G1plus1));
        add(new Formula(365, "Ф1(-1,75) – Г2(+3) – Г2(+2)", BetType.F1minus175, BetType.G2plus3, BetType.G2plus2));
        add(new Formula(366, "Ф2(-1,75) – Г1(+3) – Г1(+2)", BetType.F2minus175, BetType.G1plus3, BetType.G1plus2));
        add(new Formula(367, "Ф1(-2,75) – Г2(+4) – Г2(+3)", BetType.F1minus275, BetType.G2plus4, BetType.G2plus3));
        add(new Formula(368, "Ф2(-2,75) – Г1(+4) – Г1(+3)", BetType.F2minus275, BetType.G1plus4, BetType.G1plus3));
        add(new Formula(369, "ТБ(0,75) – ТМ(1,5) – ТМ(0,5)", BetType.T075M, BetType.T15L, BetType.T05L));
        add(new Formula(370, "ТБ(1,75) – ТМ(2,5) – ТМ(1,5)", BetType.T175M, BetType.T25L, BetType.T15L));
        add(new Formula(371, "ТБ(2,75) – ТМ(3,5) – ТМ(2,5)", BetType.T275M, BetType.T35L, BetType.T25L));
        add(new Formula(372, "ТБ(3,75) – ТМ(4,5) – ТМ(3,5)", BetType.T375M, BetType.T45L, BetType.T35L));
        add(new Formula(373, "ТБ(4,75) – ТМ(5,5) – ТМ(4,5)", BetType.T475M, BetType.T55L, BetType.T45L));
        add(new Formula(374, "ТБ(5,75) – ТМ(6,5) – ТМ(5,5)", BetType.T575M, BetType.T65L, BetType.T55L));
        add(new Formula(375, "Т1Б(0,75) – Т1М(1,5) – Т1М(0,5)", BetType.T1_075M, BetType.T1_15L, BetType.T1_05L));
        add(new Formula(376, "Т1Б(1,75) – Т1М(2,5) – Т1М(1,5)", BetType.T1_175M, BetType.T1_25L, BetType.T1_15L));
        add(new Formula(377, "Т1Б(2,75) – Т1М(3,5) – Т1М(2,5)", BetType.T1_275M, BetType.T1_35L, BetType.T1_25L));
        add(new Formula(378, "Т1Б(3,75) – Т1М(4,5) – Т1М(3,5)", BetType.T1_375M, BetType.T1_45L, BetType.T1_35L));
        add(new Formula(379, "Т1Б(4,75) – Т1М(5,5) – Т1М(4,5)", BetType.T1_475M, BetType.T1_55L, BetType.T1_45L));
        add(new Formula(380, "Т1Б(5,75) – Т1М(6,5) – Т1М(5,5)", BetType.T1_575M, BetType.T1_65L, BetType.T1_55L));
        add(new Formula(381, "Т2Б(0,75) – Т2М(1,5) – Т2М(0,5)", BetType.T2_075M, BetType.T2_15L, BetType.T2_05L));
        add(new Formula(382, "Т2Б(1,75) – Т2М(2,5) – Т2М(1,5)", BetType.T2_175M, BetType.T2_25L, BetType.T2_15L));
        add(new Formula(383, "Т2Б(2,75) – Т2М(3,5) – Т2М(2,5)", BetType.T2_275M, BetType.T2_35L, BetType.T2_25L));
        add(new Formula(384, "Т2Б(3,75) – Т2М(4,5) – Т2М(3,5)", BetType.T2_375M, BetType.T2_45L, BetType.T2_35L));
        add(new Formula(385, "Т2Б(4,75) – Т2М(5,5) – Т2М(4,5)", BetType.T2_475M, BetType.T2_55L, BetType.T2_45L));
        add(new Formula(386, "Т2Б(5,75) – Т2М(6,5) – Т2М(5,5)", BetType.T2_575M, BetType.T2_65L, BetType.T2_55L));
        add(new Formula(387, "ТМ(1,25) – ТБ(0,5) – ТБ(1,5)", BetType.T125L, BetType.T05M, BetType.T15M));
        add(new Formula(388, "ТМ(2,25) – ТБ(1,5) – ТБ(2,5)", BetType.T225L, BetType.T15M, BetType.T25M));
        add(new Formula(389, "ТМ(3,25) – ТБ(2,5) – ТБ(3,5)", BetType.T325L, BetType.T25M, BetType.T35M));
        add(new Formula(390, "ТМ(4,25) – ТБ(3,5) – ТБ(4,5)", BetType.T425L, BetType.T35M, BetType.T45M));
        add(new Formula(391, "ТМ(5,25) – ТБ(4,5) – ТБ(5,5)", BetType.T525L, BetType.T45M, BetType.T55M));
        add(new Formula(392, "ТМ(6,25) – ТБ(5,5) – ТБ(6,5)", BetType.T625L, BetType.T55M, BetType.T65M));
        add(new Formula(393, "Т1М(1,25) – Т1Б(0,5) – Т1Б(1,5)", BetType.T1_125L, BetType.T1_05M, BetType.T1_15M));
        add(new Formula(394, "Т1М(2,25) – Т1Б(1,5) – Т1Б(2,5)", BetType.T1_225L, BetType.T1_15M, BetType.T1_25M));
        add(new Formula(395, "Т1М(3,25) – Т1Б(2,5) – Т1Б(3,5)", BetType.T1_325L, BetType.T1_25M, BetType.T1_35M));
        add(new Formula(396, "Т1М(4,25) – Т1Б(3,5) – Т1Б(4,5)", BetType.T1_425L, BetType.T1_35M, BetType.T1_45M));
        add(new Formula(397, "Т1М(5,25) – Т1Б(4,5) – Т1Б(5,5)", BetType.T1_525L, BetType.T1_45M, BetType.T1_55M));
        add(new Formula(398, "Т1М(6,25) – Т1Б(5,5) – Т1Б(6,5)", BetType.T1_625L, BetType.T1_55M, BetType.T1_65M));
        add(new Formula(399, "Т2М(1,25) – Т2Б(0,5) – Т2Б(1,5)", BetType.T2_125L, BetType.T2_05M, BetType.T2_15M));
        add(new Formula(400, "Т2М(2,25) – Т2Б(1,5) – Т2Б(2,5)", BetType.T2_225L, BetType.T2_15M, BetType.T2_25M));
        add(new Formula(401, "Т2М(3,25) – Т2Б(2,5) – Т2Б(3,5)", BetType.T2_325L, BetType.T2_25M, BetType.T2_35M));
        add(new Formula(402, "Т2М(4,25) – Т2Б(3,5) – Т2Б(4,5)", BetType.T2_425L, BetType.T2_35M, BetType.T2_45M));
        add(new Formula(403, "Т2М(5,25) – Т2Б(4,5) – Т2Б(5,5)", BetType.T2_525L, BetType.T2_45M, BetType.T2_55M));
        add(new Formula(404, "Т2М(6,25) – Т2Б(5,5) – Т2Б(6,5)", BetType.T2_625L, BetType.T2_55M, BetType.T2_65M));
    }};

    @Override
    public List<Formula> getFormulas() {
        return formulas;
    }

    public List<EventSum> fillProfit(List<EventSum> bets, Formula f, Double C1, Double C2, Double C3,
                                     Double K1, Double K2, Double K3) {
        List<EventSum> betsResult = new ArrayList<EventSum>();
        for (EventSum sum: bets) {
            if (sum.bet == f.var1) {
                sum.sum = C1;
                sum.profit = C1*K1;
            } else if (sum.bet == f.var2) {
                sum.sum = C2;
                sum.profit = K2*C2 + C1*0.5 + 0.5*C1*K1;
            } else if (sum.bet == f.var3) {
                sum.sum = C3;
                sum.profit = C3*K3 + K2*C2;
            }
            betsResult.add(new EventSum(sum));
        }
        return betsResult;
    }

    protected BranchResult calculateUniform(List<EventSum> bets, Formula f, Double totalSum) {
        BranchResult branch = new BranchResult();
        branch.isProfit = true;
        branch.totalSum = totalSum;
        //Х1 = 1 / К1 + (К1 – 1) / (2 * К1 * К2) + (К1 + 1) / (2 * К1 * К3)
        KKK k = getK(bets, f);
        Double K1 = k.K1;
        Double K2 = k.K2;
        Double K3 = k.K3;

        Double X1 = 1 / K1 + (K1 - 1) / (2 * K1 * K2) + (K1 + 1) / (2 * K1 * K3);
        Double C1 = totalSum / (X1*K1);
        Double C2 = totalSum * (K1 - 1) / (X1 * 2 * K1 * K2)	;
        Double C3 = totalSum * (K1 + 1) / (X1 * 2 * K1 * K3);
        branch.branches = fillProfit(bets, f, C1, C2, C3, K1, K2 ,K3);
        for (EventSum sum: branch.branches) {
            if (sum.profit <= totalSum) {
                branch.isProfit = false;
            }
        }
        return branch;
    }

    protected BranchResult calculateUniformSeparate(List<EventSum> bets, Formula f, Double totalSum, ProfitType type) {
        BranchResult branch = new BranchResult();
        branch.isProfit = true;
        branch.totalSum = totalSum;
        KKK k = getK(bets, f);

        Double X2 = 0.0;
        Double X3 = 0.0;
        if (type == ProfitType.UNIFORM_2_3) {
            //revers
            X3 = (k.K1 + 1) / (2 * k.K3);
            X2 = k.K1 - 1 - X3;
        } else if (type == ProfitType.UNIFORM_1_2) {
            X2 =  (k.K1 - 1) / (2 * k.K2);
            X3 = (1 + (1 - k.K2) * X2) / (k.K3 - 1);
        } else if (type == ProfitType.UNIFORM_1_3) {
            X2 =  (k.K1 - (k.K3 * (k.K1 - 1)) / 2) / (k.K3 * (k.K2 - 1) + k.K2);
            X3 = ((k.K2 - 1) * X2) + (k.K1 - 1) / 2;
        } else if (type == ProfitType.UNIQUE_1) {
            X2 =  (2 - (k.K1 - 1) * (k.K3 - 1)) / (2 * k.K3 * (k.K2 - 1));
            X3 = (1 - (k.K2 - 1) * X2) / (k.K3 - 1);
        } else if (type == ProfitType.UNIQUE_2) {
            X2 =  (k.K1 + k.K3 - k.K1 * k.K3) / (k.K2 - k.K3);
            X3 = k.K1 - 1 - X2;
        } else if (type == ProfitType.UNIQUE_3) {
            X2 =  (k.K1 - 1) / (2 * k.K2);
            X3 = k.K1 - 1 - X2;
        }

        Double C1 = getUniformSeparateC1(totalSum, X2, X3);
        Double C2 = getUniformSeparateC2(totalSum, X2, X3);
        Double C3 = getUniformSeparateC3(totalSum, X2, X3);
        branch.branches = fillProfit(bets, f, C1, C2, C3, k.K1, k.K2 ,k.K3);
        return branch;
    }

}
