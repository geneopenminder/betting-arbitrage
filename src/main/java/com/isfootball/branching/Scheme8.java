package com.isfootball.branching;

/**
 * Created by Evgeniy Pshenitsin on 11.02.2015.
 */

import com.isfootball.parser.BetType;

import java.util.ArrayList;
import java.util.List;

public class Scheme8 extends BaseSchemeNonUniform {

    //full
    static List<Formula> formulas = new ArrayList<Formula>() {{
        add(new Formula(501, "П1 – х – П2", BetType.P1, BetType.X, BetType.P2));
        //add(new Formula(502, "П2 – х – П1", BetType.P2, BetType.X, BetType.P1));
        add(new Formula(503, "Ф1(-0,5) – х – П2", BetType.F1minus05, BetType.X, BetType.P2));
        add(new Formula(504, "Ф2(-0,5) – х – П1", BetType.F2minus05, BetType.X, BetType.P1));
        add(new Formula(505, "Ф1(-0,5) – х – Ф2(-0,5)", BetType.F1minus05, BetType.X, BetType.F2minus05));
        add(new Formula(506, "Ф2(-0,5) – х – Ф1(-0,5)", BetType.F2minus05, BetType.X, BetType.F1minus05));
        add(new Formula(507, "Г1(-1) – Гх(-1) – Г2(+1)", BetType.G1minus1, BetType.GXminus1, BetType.G2plus1));
        add(new Formula(508, "Г1(-2) – Гх(-2) – Г2(+2)", BetType.G1minus2, BetType.GXminus2, BetType.G2plus2));
        add(new Formula(509, "Г1(-3) – Гх(-3) – Г2(+3)", BetType.G1minus3, BetType.GXminus3, BetType.G2plus3));
        add(new Formula(510, "Г1(-4) – Гх(-4) – Г2(+4)", BetType.G1minus4, BetType.GXminus4, BetType.G2plus4));
        add(new Formula(511, "Г2(-1) – Гх(+1) – Г1(+1)", BetType.G2minus1, BetType.GXplus1, BetType.G1plus1));
        add(new Formula(512, "Г2(-2) – Гх(+2) – Г1(+2)", BetType.G2minus2, BetType.GXplus2, BetType.G1plus2));
        add(new Formula(513, "Г2(-3) – Гх(+3) – Г1(+3)", BetType.G2minus3, BetType.GXplus3, BetType.G1plus3));
        add(new Formula(514, "Г2(-4) – Гх(+4) – Г1(+4)", BetType.G2minus4, BetType.GXplus4, BetType.G1plus4));
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
                sum.profit = C2*K2;
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
        //Х1 = 1 / К1 + 1 / К2 + 1 / К3
        KKK k = getK(bets, f);
        Double K1 = k.K1;
        Double K2 = k.K2;
        Double K3 = k.K3;

        Double X1 = 1 / K1 + 1 / K2 + 1 / K3;
        Double C1 = totalSum / (X1 * K1);
        Double C2 = totalSum / (X1 * K2);
        Double C3 = totalSum / (X1 * K3);
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
            return K3 * (K1 - 1) / (K2 + K3);
        } else if (type == ProfitType.UNIFORM_1_2) {
            return K1 / K2;
        } else if (type == ProfitType.UNIFORM_1_3) {
            return (1 + K1 / K3) / (K2 - 1);
        } else if (type == ProfitType.UNIQUE_1) {
            return K3 / (K2 * K3 - K2 - K3);
        } else if (type == ProfitType.UNIQUE_2) {
            return (K1 - 1 - K1 / K3);
        } else if (type == ProfitType.UNIQUE_3) {
            return K1 / K2;
        } else {
            return 1.0; //TODO
        }
    }

    private Double getUniformSeparateX3(Double K1, Double K2, Double K3, Double X2, ProfitType type) {
        if (type == ProfitType.UNIFORM_2_3) {
            return K2 * (K1 - 1) / (K2 + K3);
        } else if (type == ProfitType.UNIFORM_1_2) {
            return (1 + X2) / (K3 - 1);
        } else if (type == ProfitType.UNIFORM_1_3) {
            return K1 / K3;
        } else if (type == ProfitType.UNIQUE_1) {
            return (1 + X2) / (K3 - 1);
        } else if (type == ProfitType.UNIQUE_2) {
            return K1 / K3;
        } else if (type == ProfitType.UNIQUE_3) {
            return (K2 - 1) * X2 - 1;
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
