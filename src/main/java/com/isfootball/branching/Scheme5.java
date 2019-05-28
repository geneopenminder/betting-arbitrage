package com.isfootball.branching;

/**
 * Created by Evgeniy Pshenitsin on 11.02.2015.
 */

import com.isfootball.parser.BetType;

import java.util.ArrayList;
import java.util.List;

public class Scheme5 extends BaseSchemeNonUniform {

    //full
    static List<Formula> formulas = new ArrayList<Formula>() {{
        add(new Formula(207, "Ф1(-0,25) – 2х – П2", BetType.F1minus025, BetType.P2X, BetType.P2));
        add(new Formula(208, "Ф2 (-0,25) – 1х – П1", BetType.F2minus025, BetType.P1X, BetType.P1));
        add(new Formula(209, "Ф1(-1,25) – Ф2(+1,5) – 2х", BetType.F1minus125, BetType.F2plus15, BetType.P2X));
        add(new Formula(210, "Ф2(-1,25) – Ф1(+1,5) – 1х", BetType.F2minus125, BetType.F1plus15, BetType.P1X));
        add(new Formula(211, "Ф1(-2,25) – Ф2(+2,5) – Ф2(+1,5)", BetType.F1minus225, BetType.F2plus25, BetType.F2plus15));
        add(new Formula(212, "Ф2(-2,25) – Ф1(+2,5) – Ф1(+1,5)", BetType.F2minus225, BetType.F1plus25, BetType.F1plus15));
        add(new Formula(213, "Ф1(-3,25) – Ф2(+3,5) – Ф2(+2,5)", BetType.F1minus325, BetType.F2plus35, BetType.F2plus25));
        add(new Formula(214, "Ф2(-3,25) – Ф1(+3,5) – Ф1(+2,5)", BetType.F2minus325, BetType.F1plus35, BetType.F1plus25));
        add(new Formula(215, "Ф1(-4,25) – Ф2(+4,5) – Ф2(+3,5)", BetType.F1minus425, BetType.F2plus45, BetType.F2plus35));
        add(new Formula(216, "Ф2(-4,25) – Ф1(+4,5) – Ф1(+3,5)", BetType.F2minus425, BetType.F1plus45, BetType.F1plus35));
        add(new Formula(217, "Ф1(-5,25) – Ф2(+5,5) – Ф2(+4,5)", BetType.F1minus525, BetType.F2plus55, BetType.F2plus45));
        add(new Formula(218, "Ф2(-5,25) – Ф1(+5,5) – Ф1(+4,5)", BetType.F2minus525, BetType.F1plus55, BetType.F1plus45));
        add(new Formula(219, "Ф1(-6,25) – Ф2(+6,5) – Ф2(+5,5)", BetType.F1minus625, BetType.F2plus65, BetType.F2plus55));
        add(new Formula(220, "Ф2(-6,25) – Ф1(+6,5) – Ф1(+5,5)", BetType.F2minus625, BetType.F1plus65, BetType.F1plus55));
        add(new Formula(221, "Ф1(-1,25) – Ф2(+1,5) – Ф2(+0,5)", BetType.F1minus125, BetType.F2plus15, BetType.F2plus05));
        add(new Formula(222, "Ф2(-1,25) – Ф1(+1,5) – Ф1(+0,5)", BetType.F2minus125, BetType.F1plus15, BetType.F1plus05));
        add(new Formula(223, "Ф1(-0,25) – 2х – Ф2(-0,5)", BetType.F1minus025, BetType.P2X, BetType.F2minus05));
        add(new Formula(224, "Ф2 (-0,25) – 1х – Ф1(-0,5)", BetType.F2minus025, BetType.P1X, BetType.F1minus05));
        add(new Formula(225, "Ф1(-0,25) – Ф2(+0,5) – Ф2(-0,5)", BetType.F1minus025, BetType.F2plus05, BetType.F2minus05));
        add(new Formula(226, "Ф2 (-0,25) – Ф1(+0,5) – Ф1(-0,5)", BetType.F2minus025, BetType.F1plus05, BetType.F1minus05));
        add(new Formula(227, "Ф1(+0,75) – П2 – Ф2(-1,5)", BetType.F1plus075, BetType.P2, BetType.F2minus15));
        add(new Formula(228, "Ф2(+0,75) – П1 – Ф1(-1,5)", BetType.F2plus075, BetType.P1, BetType.F1minus15));
        add(new Formula(229, "Ф1(+0,75) – Ф2(-0,5) – Ф2(-1,5)", BetType.F1plus075, BetType.F2minus05, BetType.F2minus15));
        add(new Formula(230, "Ф2(+0,75) – Ф1(-0,5) – Ф1(-1,5)", BetType.F2plus075, BetType.F1minus05, BetType.F1minus15));
        add(new Formula(231, "Ф1(+1,75) – Ф2(-1,5) – Ф2(-2,5)", BetType.F1plus175, BetType.F2minus15, BetType.F2minus25));
        add(new Formula(232, "Ф2(+1,75) – Ф1(-1,5) – Ф1(-2,5)", BetType.F2plus175, BetType.F1minus15, BetType.F1minus25));
        add(new Formula(233, "Ф1(+2,75) – Ф2(-2,5) – Ф2(-3,5)", BetType.F1plus275, BetType.F2minus25, BetType.F2minus35));
        add(new Formula(234, "Ф2(+2,75) – Ф1(-2,5) – Ф1(-3,5)", BetType.F2plus275, BetType.F1minus25, BetType.F1minus35));
        add(new Formula(235, "Ф1(+3,75) – Ф2(-3,5) – Ф2(-4,5)", BetType.F1plus375, BetType.F2minus35, BetType.F2minus45));
        add(new Formula(236, "Ф2(+3,75) – Ф1(-3,5) – Ф1(-4,5)", BetType.F2plus375, BetType.F1minus35, BetType.F1minus45));
        add(new Formula(237, "Ф1(+4,75) – Ф2(-4,5) – Ф2(-5,5)", BetType.F1plus475, BetType.F2minus45, BetType.F2minus55));
        add(new Formula(238, "Ф2(+4,75) – Ф1(-4,5) – Ф1(-5,5)", BetType.F2plus475, BetType.F1minus45, BetType.F1minus55));
        add(new Formula(239, "Ф1(+5,75) – Ф2(-5,5) – Ф2(-6,5)", BetType.F1plus575, BetType.F2minus55, BetType.F2minus65));
        add(new Formula(240, "Ф2(+5,75) – Ф1(-5,5) – Ф1(-6,5)", BetType.F2plus575, BetType.F1minus55, BetType.F1minus65));
        add(new Formula(241, "Ф1(+0,75) – П2 – Г2(-1)", BetType.F1plus075, BetType.P2, BetType.G2minus1));
        add(new Formula(242, "Ф2(+0,75) – П1 – Г1(-1)", BetType.F2plus075, BetType.P1, BetType.G1minus1));
        add(new Formula(243, "Ф1(+0,75) – Ф2(-0,5) – Г2(-1)", BetType.F1plus075, BetType.F2minus05, BetType.G2minus1));
        add(new Formula(244, "Ф2(+0,75) – Ф1(-0,5) – Г1(-1)", BetType.F2plus075, BetType.F1minus05, BetType.G1minus1));
        add(new Formula(245, "Ф1(+1,75) – Ф2(-1,5) – Г2(-2)", BetType.F1plus175, BetType.F2minus15, BetType.G2minus2));
        add(new Formula(246, "Ф2(+1,75) – Ф1(-1,5) – Г1(-2)", BetType.F2plus175, BetType.F1minus15, BetType.G1minus2));
        add(new Formula(247, "Ф1(+2,75) – Ф2(-2,5) – Г2(-3)", BetType.F1plus275, BetType.F2minus25, BetType.G2minus3));
        add(new Formula(248, "Ф2(+2,75) – Ф1(-2,5) – Г1(-3)", BetType.F2plus275, BetType.F1minus25, BetType.G1minus3));
        add(new Formula(249, "Ф1(+3,75) – Ф2(-3,5) – Г2(-4)", BetType.F1plus375, BetType.F2minus35, BetType.G2minus4));
        add(new Formula(250, "Ф2(+3,75) – Ф1(-3,5) – Г1(-4)", BetType.F2plus375, BetType.F1minus35, BetType.G1minus4));
        add(new Formula(251, "Ф1(+1,75) – Г2(-1) – Г2(-2)", BetType.F1plus175, BetType.G2minus1, BetType.G2minus2));
        add(new Formula(252, "Ф2(+1,75) – Г1(-1) – Г1(-2)", BetType.F2plus175, BetType.G1minus1, BetType.G1minus2));
        add(new Formula(253, "Ф1(+2,75) – Г2(-2) – Г2(-3)", BetType.F1plus275, BetType.G2minus2, BetType.G2minus3));
        add(new Formula(254, "Ф2(+2,75) – Г1(-2) – Г1(-3)", BetType.F2plus275, BetType.G1minus2, BetType.G1minus3));
        add(new Formula(255, "Ф1(+3,75) – Г2(-3) – Г2(-4)", BetType.F1plus375, BetType.G2minus3, BetType.G2minus4));
        add(new Formula(256, "Ф2(+3,75) – Г1(-3) – Г1(-4)", BetType.F2plus375, BetType.G1minus3, BetType.G1minus4));
        add(new Formula(257, "Ф1(-1,25) – Ф2(+1,5) – Г2(+1)", BetType.F1minus125, BetType.F2plus15, BetType.G2plus1));
        add(new Formula(258, "Ф2(-1,25) – Ф1(+1,5) – Г1(+1)", BetType.F2minus125, BetType.F1plus15, BetType.G1plus1));
        add(new Formula(259, "Ф1(-2,25) – Ф2(+2,5) – Г2(+2)", BetType.F1minus225, BetType.F2plus25, BetType.G2plus2));
        add(new Formula(260, "Ф2(-2,25) – Ф1(+2,5) – Г1(+2)", BetType.F2minus225, BetType.F1plus25, BetType.G1plus2));
        add(new Formula(261, "Ф1(-3,25) – Ф2(+3,5) – Г2(+3)", BetType.F1minus325, BetType.F2plus35, BetType.G2plus3));
        add(new Formula(262, "Ф2(-3,25) – Ф1(+3,5) – Г1(+3)", BetType.F2minus325, BetType.F1plus35, BetType.G1plus3));
        add(new Formula(263, "Ф1(-4,25) – Ф2(+4,5) – Г2(+4)", BetType.F1minus425, BetType.F2plus45, BetType.G2plus4));
        add(new Formula(264, "Ф2(-4,25) – Ф1(+4,5) – Г1(+4)", BetType.F2minus425, BetType.F1plus45, BetType.G1plus4));
        add(new Formula(265, "Ф1(-1,25) – Г2(+2) – Г2(+1)", BetType.F1minus125, BetType.G2plus2, BetType.G2plus1));
        add(new Formula(266, "Ф2(-1,25) – Г1(+2) – Г1(+1)", BetType.F2minus125, BetType.G1plus2, BetType.G1plus1));
        add(new Formula(267, "Ф1(-2,25) – Г2(+3) – Г2(+2)", BetType.F1minus225, BetType.G2plus3, BetType.G2plus2));
        add(new Formula(268, "Ф2(-2,25) – Г1(+3) – Г1(+2)", BetType.F2minus225, BetType.G1plus3, BetType.G1plus2));
        add(new Formula(269, "Ф1(-3,25) – Г2(+4) – Г2(+3)", BetType.F1minus325, BetType.G2plus4, BetType.G2plus3));
        add(new Formula(270, "Ф2(-3,25) – Г1(+4) – Г1(+3)", BetType.F2minus325, BetType.G1plus4, BetType.G1plus3));
        add(new Formula(271, "ТМ(0,75) – ТБ(0,5) – ТБ (1,5)", BetType.T075L, BetType.T05M, BetType.T15M));
        add(new Formula(272, "ТМ(1,75) – ТБ(1,5) – ТБ(2,5)", BetType.T175L, BetType.T15M, BetType.T25M));
        add(new Formula(273, "ТМ(2,75) – ТБ(2,5) – ТБ(3,5)", BetType.T275L, BetType.T25M, BetType.T35M));
        add(new Formula(274, "ТМ(3,75) – ТБ(3,5) – ТБ (4,5)", BetType.T375L, BetType.T35M, BetType.T45M));
        add(new Formula(275, "ТМ(4,75) – ТБ(4,5) – ТБ(5,5)", BetType.T475L, BetType.T45M, BetType.T55M));
        add(new Formula(276, "ТМ(5,75) – ТБ(5,5) – ТБ(6,5)", BetType.T575L, BetType.T55M, BetType.T65M));
        add(new Formula(277, "Т1М(0,75) – Т1Б(0,5) – Т1Б (1,5)", BetType.T1_075L, BetType.T1_05M, BetType.T1_15M));
        add(new Formula(278, "Т1М(1,75) – Т1Б(1,5) – Т1Б(2,5)", BetType.T1_175L, BetType.T1_15M, BetType.T1_25M));
        add(new Formula(279, "Т1М(2,75) – Т1Б(2,5) – Т1Б(3,5)", BetType.T1_275L, BetType.T1_25M, BetType.T1_35M));
        add(new Formula(280, "Т1М(3,75) – Т1Б(3,5) – Т1Б (4,5)", BetType.T1_375L, BetType.T1_35M, BetType.T1_45M));
        add(new Formula(281, "Т1М(4,75) – Т1Б(4,5) – Т1Б(5,5)", BetType.T1_475L, BetType.T1_45M, BetType.T1_55M));
        add(new Formula(282, "Т1М(5,75) – Т1Б(5,5) – Т1Б(6,5)", BetType.T1_575L, BetType.T1_55M, BetType.T1_65M));
        add(new Formula(283, "Т2М(0,75) – Т2Б(0,5) – Т2Б (1,5)", BetType.T2_075L, BetType.T2_05M, BetType.T2_15M));
        add(new Formula(284, "Т2М(1,75) – Т2Б(1,5) – Т2Б(2,5)", BetType.T2_175L, BetType.T2_15M, BetType.T2_25M));
        add(new Formula(285, "Т2М(2,75) – Т2Б(2,5) – Т2Б(3,5)", BetType.T2_275L, BetType.T2_25M, BetType.T2_35M));
        add(new Formula(286, "Т2М(3,75) – Т2Б(3,5) – Т2Б (4,5)", BetType.T2_375L, BetType.T2_35M, BetType.T2_45M));
        add(new Formula(287, "Т2М(4,75) – Т2Б(4,5) – Т2Б(5,5)", BetType.T2_475L, BetType.T2_45M, BetType.T2_55M));
        add(new Formula(288, "Т2М(5,75) – Т2Б(5,5) – Т2Б(6,5)", BetType.T2_575L, BetType.T2_55M, BetType.T2_65M));
        add(new Formula(289, "ТБ(1,25) – ТМ(1,5) – ТМ(0,5)", BetType.T125M, BetType.T15L, BetType.T05L));
        add(new Formula(290, "ТБ(2,25) – ТМ(2,5) – ТМ(1,5)", BetType.T225M, BetType.T25L, BetType.T15L));
        add(new Formula(291, "ТБ(3,25) – ТМ(3,5) – ТМ(2,5)", BetType.T325M, BetType.T35L, BetType.T25L));
        add(new Formula(292, "ТБ(4,25) – ТМ(4,5) – ТМ(3,5)", BetType.T425M, BetType.T45L, BetType.T35L));
        add(new Formula(293, "ТБ(5,25) – ТМ(5,5) – ТМ(4,5)", BetType.T525M, BetType.T55L, BetType.T45L));
        add(new Formula(294, "ТБ(6,25) – ТМ(6,5) – ТМ(5,5)", BetType.T625M, BetType.T65L, BetType.T55L));
        add(new Formula(295, "Т1Б(1,25) – Т1М(1,5) – Т1М(0,5)", BetType.T1_125M, BetType.T1_15L, BetType.T1_05L));
        add(new Formula(296, "Т1Б(2,25) – Т1М(2,5) – Т1М(1,5)", BetType.T1_125M, BetType.T1_25L, BetType.T1_15L));
        add(new Formula(297, "Т1Б(3,25) – Т1М(3,5) – Т1М(2,5)", BetType.T1_125M, BetType.T1_35L, BetType.T1_25L));
        add(new Formula(298, "Т1Б(4,25) – Т1М(4,5) – Т1М(3,5)", BetType.T1_125M, BetType.T1_45L, BetType.T1_35L));
        add(new Formula(299, "Т1Б(5,25) – Т1М(5,5) – Т1М(4,5)", BetType.T1_125M, BetType.T1_55L, BetType.T1_45L));
        add(new Formula(300, "Т1Б(6,25) – Т1М(6,5) – Т1М(5,5)", BetType.T1_125M, BetType.T1_65L, BetType.T1_55L));
        add(new Formula(301, "Т2Б(1,25) – Т2М(1,5) – Т2М(0,5)", BetType.T2_125M, BetType.T2_15L, BetType.T2_05L));
        add(new Formula(302, "Т2Б(2,25) – Т2М(2,5) – Т2М(1,5)", BetType.T2_225M, BetType.T2_25L, BetType.T2_15L));
        add(new Formula(303, "Т2Б(3,25) – Т2М(3,5) – Т2М(2,5)", BetType.T2_325M, BetType.T2_35L, BetType.T2_25L));
        add(new Formula(304, "Т2Б(4,25) – Т2М(4,5) – Т2М(3,5)", BetType.T2_425M, BetType.T2_45L, BetType.T2_35L));
        add(new Formula(305, "Т2Б(5,25) – Т2М(5,5) – Т2М(4,5)", BetType.T2_525M, BetType.T2_55L, BetType.T2_45L));
        add(new Formula(306, "Т2Б(6,25) – Т2М(6,5) – Т2М(5,5)", BetType.T2_625M, BetType.T2_65L, BetType.T2_55L));
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
                sum.profit = K2*C2 + C1*0.5;
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
        //Х1 = 1 / К1 + 1 / (2 * К1 * К3) + ((К1 – 0,5) / (К1 * К2)
        KKK k = getK(bets, f);
        Double K1 = k.K1;
        Double K2 = k.K2;
        Double K3 = k.K3;

        Double X1 = 1 / K1 + 1 / (2 * K1 * K3) + ((K1 - 0.5) / (K1 * K2));
        Double C1 = totalSum/(X1*K1);
        Double C2 = totalSum*(K1 - 0.5) / (X1 * K1 * K2);
        Double C3 = totalSum/(X1*2*K1*K3);
        branch.branches = fillProfit(bets, f, C1, C2, C3, K1, K2 ,K3);
        for (EventSum sum: branch.branches) {
            if (sum.profit <= totalSum) {
                branch.isProfit = false;
            }
        }
        return branch;
    }

    private Double getUniformSeparateX2(Double K1, Double K2, Double K3, ProfitType type) {
        if (type == ProfitType.UNIFORM_2_3) {
            return K1 - 1 - 1 / (2 * K3);
        } else if (type == ProfitType.UNIFORM_1_2) {
            return (K1 - 0.5) / K2;
        } else if (type == ProfitType.UNIFORM_1_3) {
            return (K1 + K3 / 2) / (K3 * (K2 - 1) + K2);
        } else if (type == ProfitType.UNIQUE_1) {
            return (0.5 * (K3 + 1)) / ((K2 - 1) * K3);
        } else if (type == ProfitType.UNIQUE_2) {
            return (K1 + K3 - K1 * K3) / (K2 - K3);
        } else if (type == ProfitType.UNIQUE_3) {
            return (K1 - 0.5) / K2;
        } else {
            return 1.0; //TODO
        }
    }

    private Double getUniformSeparateX3(Double K1, Double K2, Double K3, Double X2, ProfitType type) {
        if (type == ProfitType.UNIFORM_2_3) {
            return 1 / (2 * K3);
        } else if (type == ProfitType.UNIFORM_1_2) {
            return (1 - (K2 - 1) * X2) / (K3 - 1);
        } else if (type == ProfitType.UNIFORM_1_3) {
            return (K2 - 1) * X2 - 0.5;
        } else if (type == ProfitType.UNIQUE_1) {
            return (1 - (K2 - 1) * X2) / (K3 - 1);
        } else if (type == ProfitType.UNIQUE_2) {
            return K1 - 1 - X2;
        } else if (type == ProfitType.UNIQUE_3) {
            return K1 - 1 - X2;
        } else {
            return 1.0; //TODO
        }
    }
    protected BranchResult calculateUniformSeparate(List<EventSum> bets, Formula f, Double totalSum, ProfitType type) {
        BranchResult branch = new BranchResult();
        branch.isProfit = true;
        branch.totalSum = totalSum;
        KKK k = getK(bets, f);

        Double X2 = getUniformSeparateX2(k.K1, k.K2, k.K3, type);
        Double X3 = getUniformSeparateX3(k.K1, k.K2, k.K3, X2, type);

        Double C1 = getUniformSeparateC1(totalSum, X2, X3);
        Double C2 = getUniformSeparateC2(totalSum, X2, X3);
        Double C3 = getUniformSeparateC3(totalSum, X2, X3);
        branch.branches = fillProfit(bets, f, C1, C2, C3, k.K1, k.K2 ,k.K3);
        return branch;
    }

}
