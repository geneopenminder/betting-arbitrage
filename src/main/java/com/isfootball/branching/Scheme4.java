package com.isfootball.branching;

/**
 * Created by Evgeniy Pshenitsin on 11.02.2015.
 */

import com.isfootball.parser.BetType;

import java.util.ArrayList;
import java.util.List;

public class Scheme4 extends BaseSchemeNonUniform {

    //full
    static List<Formula> formulas = new ArrayList<Formula>() {{
        add(new Formula(159, "Ф1(+0,25) – х – П2", BetType.F1plus025, BetType.X, BetType.P2));
        add(new Formula(160, "Ф2(+0,25) – х – П1", BetType.F2plus025, BetType.X, BetType.P1));
        add(new Formula(161, "Ф1(+1,25) – Ф1(+1,5) – Ф2(-1,5)", BetType.F1plus125, BetType.F1plus15, BetType.F2minus15));
        add(new Formula(162, "Ф2(+1,25) – Ф2(+1,5) – Ф1(-1,5)", BetType.F2plus125, BetType.F2plus15, BetType.F1minus15));
        add(new Formula(163, "Ф1(+2,25) – Ф1(+2,5) – Ф2(-2,5)", BetType.F1plus225, BetType.F1plus25, BetType.F2minus25));
        add(new Formula(164, "Ф2(+2,25) – Ф2(+2,5) – Ф1(-2,5)", BetType.F2plus225, BetType.F2plus25, BetType.F1minus25));
        add(new Formula(165, "Ф1(+3,25) – Ф1(+3,5) – Ф2(-3,5)", BetType.F1plus325, BetType.F1plus35, BetType.F2minus35));
        add(new Formula(166, "Ф2(+3,25) – Ф2(+3,5) – Ф1(-3,5)", BetType.F2plus325, BetType.F2plus35, BetType.F1minus35));
        add(new Formula(167, "Ф1(+4,25) – Ф1(+4,5) – Ф2(-4,5)", BetType.F1plus425, BetType.F1plus45, BetType.F2minus45));
        add(new Formula(168, "Ф2(+4,25) – Ф2(+4,5) – Ф1(-4,5)", BetType.F2plus425, BetType.F2plus45, BetType.F1minus45));
        add(new Formula(169, "Ф1(+5,25) – Ф1(+5,5) – Ф2(-5,5)", BetType.F1plus525, BetType.F1plus55, BetType.F2minus55));
        add(new Formula(170, "Ф2(+5,25) – Ф2(+5,5) – Ф1(-5,5)", BetType.F2plus525, BetType.F2plus55, BetType.F1minus55));
        add(new Formula(171, "Ф1(+6,25) – Ф1(+6,5) – Ф2(-6,5)", BetType.F1plus625, BetType.F1plus65, BetType.F2minus65));
        add(new Formula(172, "Ф2(+6,25) – Ф2(+6,5) – Ф1(-6,5)", BetType.F2plus625, BetType.F2plus65, BetType.F1minus65));
        add(new Formula(173, "Ф1(+0,25) – х – Ф2(-0,5)", BetType.F1plus025, BetType.X, BetType.F2minus05));
        add(new Formula(174, "Ф2(+0,25) – х – Ф1(-0,5)", BetType.F2plus025, BetType.X, BetType.F1minus05));
        add(new Formula(175, "Ф1(-0,75) – Гх(-1) – Ф2(+0,5)", BetType.F1minus075, BetType.GXminus1, BetType.F2plus05));
        add(new Formula(176, "Ф2(-0,75) – Гх(+1) – Ф1(+0,5)", BetType.F2minus075, BetType.GXplus1, BetType.F1plus05));
        add(new Formula(177, "Ф1(-1,75) – Гх(-2) – Ф2(+1,5)", BetType.F1minus175, BetType.GXminus2, BetType.F2plus15));
        add(new Formula(178, "Ф2(-1,75) – Гх(+2) – Ф1(+1,5)", BetType.F2minus175, BetType.GXplus2, BetType.F1plus15));
        add(new Formula(179, "Ф1(-2,75) – Гх(-3) – Ф2(+2,5)", BetType.F1minus275, BetType.GXminus3, BetType.F2plus25));
        add(new Formula(180, "Ф2(-2,75) – Гх(+3) – Ф1(+2,5)", BetType.F2minus275, BetType.GXplus3, BetType.F1plus25));
        add(new Formula(181, "Ф1(-3,75) – Гх(-4) – Ф2(+3,5)", BetType.F1minus375, BetType.GXminus4, BetType.F2plus35));
        add(new Formula(182, "Ф2(-3,75) – Гх(+4) – Ф1(+3,5)", BetType.F2minus375, BetType.GXplus4, BetType.F1plus35));
        add(new Formula(183, "Ф1(-0,75) – Гх(-1) – Г2(+1)", BetType.F1minus075, BetType.GXminus1, BetType.G2plus1));
        add(new Formula(184, "Ф2(-0,75) – Гх(+1) – Г1(+1)", BetType.F2minus075, BetType.GXplus1, BetType.G1plus1));
        add(new Formula(185, "Ф1(-1,75) – Гх(-2) – Г2(+2)", BetType.F1minus175, BetType.GXminus2, BetType.G2plus2));
        add(new Formula(186, "Ф2(-1,75) – Гх(+2) – Г1(+2)", BetType.F2minus175, BetType.GXplus2, BetType.G1plus2));
        add(new Formula(187, "Ф1(-2,75) – Гх(-3) – Г2(+3)", BetType.F1minus275, BetType.GXminus3, BetType.G2plus3));
        add(new Formula(188, "Ф2(-2,75) – Гх(+3) – Г1(+3)", BetType.F2minus275, BetType.GXplus3, BetType.G1plus3));
        add(new Formula(189, "Ф1(-3,75) – Гх(-4) – Г2(+4)", BetType.F1minus375, BetType.GXminus4, BetType.G2plus4));
        add(new Formula(190, "Ф2(-3,75) – Гх(+4) – Г1(+4)", BetType.F2minus375, BetType.GXplus4, BetType.G1plus4));
        add(new Formula(191, "Ф1(+1,25) – Гх(+1) – Ф2(-1,5)", BetType.F1plus125, BetType.GXplus1, BetType.F2minus15));
        add(new Formula(192, "Ф2(+1,25) – Гх(-1) – Ф1(-1,5)", BetType.F2plus125, BetType.GXminus1, BetType.F1minus15));
        add(new Formula(193, "Ф1(+2,25) – Гх(+2) – Ф2(-2,5)", BetType.F1plus225, BetType.GXplus2, BetType.F2minus25));
        add(new Formula(194, "Ф2(+2,25) – Гх(-2) – Ф1(-2,5)", BetType.F2plus225, BetType.GXminus2, BetType.F1minus25));
        add(new Formula(195, "Ф1(+3,25) – Гх(+3) – Ф2(-3,5)", BetType.F1plus325, BetType.GXplus3, BetType.F2minus35));
        add(new Formula(196, "Ф2(+3,25) – Гх(-3) – Ф1(-3,5)", BetType.F2plus325, BetType.GXminus3, BetType.F1minus35));
        add(new Formula(197, "Ф1(+4,25) – Гх(+4) – Ф2(-4,5)", BetType.F1plus425, BetType.GXplus4, BetType.F2minus45));
        add(new Formula(198, "Ф2(+4,25) – Гх(-4) – Ф1(-4,5)", BetType.F2plus425, BetType.GXminus4, BetType.F1minus45));
        add(new Formula(199, "Ф1(+1,25) – Гх(+1) – Г2(-1)", BetType.F1plus125, BetType.GXplus1, BetType.G2minus1));
        add(new Formula(200, "Ф2(+1,25) – Гх(-1) – Г1(-1)", BetType.F2plus125, BetType.GXminus1, BetType.G1minus1));
        add(new Formula(201, "Ф1(+2,25) – Гх(+2) – Г2(-2)", BetType.F1plus225, BetType.GXplus2, BetType.G2minus2));
        add(new Formula(202, "Ф2(+2,25) – Гх(-2) – Г1(-2)", BetType.F2plus225, BetType.GXminus2, BetType.G1minus2));
        add(new Formula(203, "Ф1(+3,25) – Гх(+3) – Г2(-3)", BetType.F1plus325, BetType.GXplus3, BetType.G2minus3));
        add(new Formula(204, "Ф2(+3,25) – Гх(-3) – Г1(-3)", BetType.F2plus325, BetType.GXminus3, BetType.G1minus3));
        add(new Formula(205, "Ф1(+4,25) – Гх(+4) – Г2(-4)", BetType.F1plus425, BetType.GXplus4, BetType.G2minus4));
        add(new Formula(206, "Ф2(+4,25) – Гх(-4) – Г1(-4)", BetType.F2plus425, BetType.GXminus4, BetType.G1minus4));
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
                sum.profit = C3*K3;
            }
            betsResult.add(new EventSum(sum));
        }
        return betsResult;
    }

    protected BranchResult calculateUniform(List<EventSum> bets, Formula f, Double totalSum) {
        BranchResult branch = new BranchResult();
        branch.isProfit = true;
        branch.totalSum = totalSum;
        //Х1 = 1 / К1 + 1 / К3 + (К1 – 1) / (2 * К1 * К2)
        KKK k = getK(bets, f);
        Double K1 = k.K1;
        Double K2 = k.K2;
        Double K3 = k.K3;

        Double X1 = 1 / K1 + 1 / K3 + (K1 - 1) / (2 * K1 * K2);
        Double C1 = totalSum/(X1*K1);
        Double C2 = totalSum*(K1 - 1) / (X1 * 2 * K1 * K2);
        Double C3 = totalSum/(X1*K3);
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
            return (K3 * (K1 - 1) - (K1 + 1)/2) / (K2 + K3);
        } else if (type == ProfitType.UNIFORM_1_2) {
            return (K1 - 1) / (2 * K2);
        } else if (type == ProfitType.UNIFORM_1_3) {
            return (K3 - K1 * K3 + 2 * K1) / (2 * K3 * (K2 - 1));
        } else if (type == ProfitType.UNIQUE_1) {
            return (K1 + K3 - K1 * K3 + 1) / (2 * (K2 * K3 - K2 - K3));
        } else if (type == ProfitType.UNIQUE_2) {
            return (K1 - 1 - K1 / K3);
        } else if (type == ProfitType.UNIQUE_3) {
            return (K1 - 1) / (2 * K2);
        } else {
            return 1.0; //TODO
        }
    }

    private Double getUniformSeparateX3(Double K1, Double K3, Double X2, ProfitType type) {
        if (type == ProfitType.UNIFORM_2_3) {
            return (K1 - 1) - X2;
        } else if (type == ProfitType.UNIFORM_1_2) {
            return (1 + X2) / (K3 - 1);
        } else if (type == ProfitType.UNIFORM_1_3) {
            return K1 / K3;
        } else if (type == ProfitType.UNIQUE_1) {
            return (1 + X2) / (K3 - 1);
        } else if (type == ProfitType.UNIQUE_2) {
            return K1 / K3;
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
        Double X3 = getUniformSeparateX3(k.K1, k.K3, X2, type);

        Double C1 = getUniformSeparateC1(totalSum, X2, X3);
        Double C2 = getUniformSeparateC2(totalSum, X2, X3);
        Double C3 = getUniformSeparateC3(totalSum, X2, X3);
        branch.branches = fillProfit(bets, f, C1, C2, C3, k.K1, k.K2 ,k.K3);
        return branch;
    }

}
