package com.isfootball.branching;

/**
 * Created by Evgeniy Pshenitsin on 11.02.2015.
 */

import com.isfootball.parser.BetType;

import java.util.ArrayList;
import java.util.List;

public class Scheme3 extends BaseSchemeNonUniform {

    //full
    static List<Formula> formulas = new ArrayList<Formula>() {{
        add(new Formula(123, "Ф1(-0,25) – х – П2", BetType.F1minus025, BetType.X, BetType.P2));
        add(new Formula(124, "Ф2(-0,25) – х – П1", BetType.F2minus025, BetType.X, BetType.P1));
        add(new Formula(125, "Ф1(-0,25) – х – Ф2(-0,5)", BetType.F1minus025, BetType.X, BetType.F2minus05));
        add(new Formula(126, "Ф2(-0,25) – х – Ф1(-0,5)", BetType.F2minus025, BetType.X, BetType.F1minus05));
        add(new Formula(127, "Ф1(-1,25) – Гх(-1) – Ф2(+0,5)", BetType.F1minus125, BetType.GXminus1, BetType.F2plus05));
        add(new Formula(128, "Ф2(-1,25) – Гх(+1) – Ф1(+0,5)", BetType.F2minus125, BetType.GXplus1, BetType.F1plus05));
        add(new Formula(129, "Ф1(-2,25) – Гх(-2) – Ф2(+1,5)", BetType.F1minus225, BetType.GXminus2, BetType.F2plus15));
        add(new Formula(130, "Ф2(-2,25) – Гх(+2) – Ф1(+1,5)", BetType.F2minus225, BetType.GXplus2, BetType.F1plus15));
        add(new Formula(131, "Ф1(-3,25) – Гх(-3) – Ф2(+2,5)", BetType.F1minus325, BetType.GXminus3, BetType.F2plus25));
        add(new Formula(132, "Ф2(-3,25) – Гх(+3) – Ф1(+2,5)", BetType.F2minus325, BetType.GXplus3, BetType.F1plus25));
        add(new Formula(133, "Ф1(-4,25) – Гх(-4) – Ф2(+3,5)", BetType.F1minus425, BetType.GXminus4, BetType.F2plus35));
        add(new Formula(134, "Ф2(-4,25) – Гх(+4) – Ф1(+3,5)", BetType.F2minus425, BetType.GXplus4, BetType.F1plus35));
        add(new Formula(135, "Ф1(-1,25) – Гх(-1) – Г2(+1)", BetType.F1minus125, BetType.GXminus1, BetType.G2plus1));
        add(new Formula(136, "Ф2(-1,25) – Гх(+1) – Г1(+1)", BetType.F2minus125, BetType.GXplus1, BetType.G1plus1));
        add(new Formula(137, "Ф1(-2,25) – Гх(-2) – Г2(+2)", BetType.F1minus225, BetType.GXminus2, BetType.G2plus2));
        add(new Formula(138, "Ф2(-2,25) – Гх(+2) – Г1(+2)", BetType.F2minus225, BetType.GXplus2, BetType.G1plus2));
        add(new Formula(139, "Ф1(-3,25) – Гх(-3) – Г2(+3)", BetType.F1minus325, BetType.GXminus3, BetType.G2plus3));
        add(new Formula(140, "Ф2(-3,25) – Гх(+3) – Г1(+3)", BetType.F2minus325, BetType.GXplus3, BetType.G1plus3));
        add(new Formula(141, "Ф1(-4,25) – Гх(-4) – Г2(+4)", BetType.F1minus425, BetType.GXminus4, BetType.G2plus4));
        add(new Formula(142, "Ф2(-4,25) – Гх(+4) – Г1(+4)", BetType.F2minus425, BetType.GXplus4, BetType.G1plus4));
        add(new Formula(143, "Ф1(+0,75) – Гх(+1) – Ф2(-1,5)", BetType.F1plus075, BetType.GXplus1, BetType.F2minus15));
        add(new Formula(144, "Ф2(+0,75) – Гх(-1) – Ф1(-1,5)", BetType.F2plus075, BetType.GXminus1, BetType.F1minus15));
        add(new Formula(145, "Ф1(+1,75) – Гх(+2) – Ф2(-2,5)", BetType.F1plus175, BetType.GXplus2, BetType.F2minus25));
        add(new Formula(146, "Ф2(+1,75) – Гх(-2) – Ф1(-2,5)", BetType.F2plus175, BetType.GXminus2, BetType.F1minus25));
        add(new Formula(147, "Ф1(+2,75) – Гх(+3) – Ф2(-3,5)", BetType.F1plus275, BetType.GXplus3, BetType.F2minus35));
        add(new Formula(148, "Ф2(+2,75) – Гх(-3) – Ф1(-3,5)", BetType.F2plus275, BetType.GXminus3, BetType.F1minus35));
        add(new Formula(149, "Ф1(+3,75) – Гх(+4) – Ф2(-4,5)", BetType.F1plus375, BetType.GXplus4, BetType.F2minus45));
        add(new Formula(150, "Ф2(+3,75) – Гх(-4) – Ф1(-4,5)", BetType.F2plus375, BetType.GXminus4, BetType.F1minus45));
        add(new Formula(151, "Ф1(+0,75) – Гх(+1) – Г2(-1)", BetType.F1plus075, BetType.GXplus1, BetType.G2minus1));
        add(new Formula(152, "Ф2(+0,75) – Гх(-1) – Г1(-1)", BetType.F2plus075, BetType.GXminus1, BetType.G1minus1));
        add(new Formula(153, "Ф1(+1,75) – Гх(+2) – Г2(-2)", BetType.F1plus175, BetType.GXplus2, BetType.G2minus2));
        add(new Formula(154, "Ф2(+1,75) – Гх(-2) – Г1(-2)", BetType.F2plus175, BetType.GXminus2, BetType.G1minus2));
        add(new Formula(155, "Ф1(+2,75) – Гх(+3) – Г2(-3)", BetType.F1plus275, BetType.GXplus3, BetType.G2minus3));
        add(new Formula(156, "Ф2(+2,75) – Гх(-3) – Г1(-3)", BetType.F2plus275, BetType.GXminus3, BetType.G1minus3));
        add(new Formula(157, "Ф1(+3,75) – Гх(+4) – Г2(-4)", BetType.F1plus375, BetType.GXplus4, BetType.G2minus4));
        add(new Formula(158, "Ф2(+3,75) – Гх(-4) – Г2(+4)", BetType.F2plus375, BetType.GXminus4, BetType.G2minus4));

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
                sum.profit = C2*K2 + C1*0.5;
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
        //Х1 = 1 / К1 + 1 / К3 + (К1 – 0,5) / (К1 * К2)
        KKK k = getK(bets, f);
        Double K1 = k.K1;
        Double K2 = k.K2;
        Double K3 = k.K3;

        Double X1 = 1/K1 + 1/K3 + (K1 - 0.5)/(K1*K2);
        Double C1 = totalSum/(X1*K1);
        Double C2 = (totalSum*(K1 - 0.5))/(X1*K1*K2);
        Double C3 = totalSum/(X1*K3);
        branch.branches = fillProfit(bets, f, C1, C2, C3, K1, K2 ,K3);
        for (EventSum sum: branch.branches) {
            if (sum.profit <= totalSum) {
                branch.isProfit = false;
            }
        }
        return branch;
    }

    private Double getUniformSeparateX2(Double K1, Double K2, Double K3, Double X4, ProfitType type) {
        if (type == ProfitType.UNIFORM_2_3) {
            return (K3 * (K1 - 1) - 0.5) / (K3 + K2);
        } else if (type == ProfitType.UNIFORM_1_2) {
            return (K1 - 0.5) / K2;
        } else if (type == ProfitType.UNIFORM_1_3) {
            return (2 * K1 + K3) / (2 * K3 * (K2 - 1));
        } else if (type == ProfitType.UNIQUE_1) {
            return (X4 + 0.5) / (K2 - 1 - X4);
        } else if (type == ProfitType.UNIQUE_2) {
            return (K1 - 1 - K1 / K3);
        } else if (type == ProfitType.UNIQUE_3) {
            return (K1 - 0.5) / K2;
        } else {
            return 1.0; //TODO
        }
    }

    private Double getUniformSeparateX3(Double K1, Double K3, Double X2, Double X4, ProfitType type) {
        if (type == ProfitType.UNIFORM_2_3) {
            return (K1 - 1) - X2;
        } else if (type == ProfitType.UNIFORM_1_2) {
            return (1 + X2) / (K3 - 1);
        } else if (type == ProfitType.UNIFORM_1_3) {
            return K1 / K3;
        } else if (type == ProfitType.UNIQUE_1) {
            return (1 + X2) * X4;
        } else if (type == ProfitType.UNIQUE_2) {
            return K1 / K3;
        } else if (type == ProfitType.UNIQUE_3) {
            return K1 - 1 - X2;
        } else {
            return 1.0; //TODO
        }
    }
    private Double getUniformSeparateX4(Double K3, ProfitType type) {
        if (type == ProfitType.UNIQUE_1) {
            return 1 / (K3 - 1);
        } else {
            return 1.0; //TODO
        }
    }

    protected BranchResult calculateUniformSeparate(List<EventSum> bets, Formula f, Double totalSum, ProfitType type) {
        BranchResult branch = new BranchResult();
        branch.isProfit = true;
        branch.totalSum = totalSum;
        KKK k = getK(bets, f);

        Double X4 = getUniformSeparateX4(k.K3, type);
        Double X2 = getUniformSeparateX2(k.K1, k.K2, k.K3, X4, type);
        Double X3 = getUniformSeparateX3(k.K1, k.K3, X2, X4, type);

        Double C1 = getUniformSeparateC1(totalSum, X2, X3);
        Double C2 = getUniformSeparateC2(totalSum, X2, X3);
        Double C3 = getUniformSeparateC3(totalSum, X2, X3);
        branch.branches = fillProfit(bets, f, C1, C2, C3, k.K1, k.K2 ,k.K3);
        return branch;
    }

}
