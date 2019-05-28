package com.isfootball.branching;

/**
 * Created by Evgeniy Pshenitsin on 11.02.2015.
 */

import com.isfootball.parser.BetType;

import java.util.ArrayList;
import java.util.List;

public class Scheme11 extends BaseSchemeNonUniform {

    //full
    static List<Formula> formulas = new ArrayList<Formula>() {{
        add(new Formula(614, "Ф1(-0,25) – х – Ф2(-0,25)", BetType.F1minus025, BetType.X, BetType.F2minus025));
        add(new Formula(615, "Ф2(-0,25) – х – Ф1(-0,25)", BetType.F2minus025, BetType.X, BetType.F1minus025));
        add(new Formula(616, "Ф1(-1,25) – П1 – Ф2(+0,75)", BetType.F1minus125, BetType.P1, BetType.F2plus075));
        add(new Formula(617, "Ф2(-1,25) – П2 – Ф1(+0,75)", BetType.F2minus125, BetType.P2, BetType.F1plus075));
        add(new Formula(618, "Ф1(-2,25) – Ф1(-1,5) – Ф2(+1,75)", BetType.F1minus225, BetType.F1minus15, BetType.F2plus175));
        add(new Formula(619, "Ф2(-2,25) – Ф2(-1,5) – Ф1(+1,75)", BetType.F2minus225, BetType.F2minus15, BetType.F1plus175));
        add(new Formula(620, "Ф1(-3,25) – Ф1(-2,5) – Ф2(+2,75)", BetType.F1minus325, BetType.F1minus25, BetType.F2plus275));
        add(new Formula(621, "Ф2(-3,25) – Ф2(-2,5) – Ф1(+2,75)", BetType.F2minus325, BetType.F2minus25, BetType.F1plus275));
        add(new Formula(622, "Ф1(-4,25) – Ф1(-3,5) – Ф2(+3,75)", BetType.F1minus425, BetType.F1minus35, BetType.F2plus375));
        add(new Formula(623, "Ф2(-4,25) – Ф2(-3,5) – Ф1(+3,75)", BetType.F2minus425, BetType.F2minus35, BetType.F1plus375));
        add(new Formula(624, "Ф1(-5,25) – Ф1(-4,5) – Ф2(+4,75)", BetType.F1minus525, BetType.F1minus45, BetType.F2plus475));
        add(new Formula(625, "Ф2(-5,25) – Ф2(-4,5) – Ф1(+4,75)", BetType.F2minus525, BetType.F2minus45, BetType.F1plus475));
        add(new Formula(626, "Ф1(-6,25) – Ф1(-5,5) – Ф2(+5,75)", BetType.F1minus625, BetType.F1minus55, BetType.F2plus575));
        add(new Formula(627, "Ф2(-6,25) – Ф2(-5,5) – Ф1(+5,75)", BetType.F2minus625, BetType.F2minus55, BetType.F1plus575));
        add(new Formula(628, "Ф1(-1,25) – Гх(-1) – Ф2(+0,75)", BetType.F1minus125, BetType.GXminus1, BetType.F2plus075));
        add(new Formula(629, "Ф2(-1,25) – Гх(+1) – Ф1(+0,75)", BetType.F2minus125, BetType.GXplus1, BetType.F1plus075));
        add(new Formula(630, "Ф1(-2,25) – Гх(-2) – Ф2(+1,75)", BetType.F1minus225, BetType.GXminus2, BetType.F2plus175));
        add(new Formula(631, "Ф2(-2,25) – Гх(+2) – Ф1(+1,75)", BetType.F2minus225, BetType.GXplus2, BetType.F1plus175));
        add(new Formula(632, "Ф1(-3,25) – Гх(-3) – Ф2(+2,75)", BetType.F1minus325, BetType.GXminus3, BetType.F2plus275));
        add(new Formula(633, "Ф2(-3,25) – Гх(+3) – Ф1(+2,75)", BetType.F2minus325, BetType.GXplus3, BetType.F1plus275));
        add(new Formula(634, "Ф1(-4,25) – Гх(-4) – Ф2(+3,75)", BetType.F1minus425, BetType.GXminus4, BetType.F2plus375));
        add(new Formula(635, "Ф2(-4,25) – Гх(+4) – Ф1(+3,75)", BetType.F2minus425, BetType.GXplus4, BetType.F1plus375));
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
                sum.profit = C2*K2 + 0.5*C3 + 0.5*C1;
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
        //Х1 = 1 / К1 + 1 / К2 + 1 / К3 – 1 / (2 * К2 * К1) – 1 / (2 * К2 * К3)
        //Х2 = (К1 – 1 / 2 – К1 /  (2 * К3)) / К2

        KKK k = getK(bets, f);
        Double K1 = k.K1;
        Double K2 = k.K2;
        Double K3 = k.K3;

        Double X1 = 1 / K1 + 1 / K2 + 1 / K3 - 1 / (2 * K2 * K1) - 1 / (2 * K2 * K3);
        Double X2 = (K1 - 0.5 - K1 /  (2 * K3)) / K2;
        Double C1 = totalSum / (X1 * K1);
        Double C2 = (totalSum * X2) / (X1 * K1);
        Double C3 = totalSum / (X1 * K3);
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
            return ((K3 - 0.5) * (K1 - 1) - 0.5) / (K2 + K3 - 0.5);
        } else if (type == ProfitType.UNIFORM_1_2) {
            return (K1 - 0.5 - X4) / (K2 + X4);
        } else if (type == ProfitType.UNIFORM_1_3) {
            return (K1 + K3) / (2 * K3 * (K2 - 1));
        } else if (type == ProfitType.UNIQUE_1) {
            return K3 / (2 * (K3 - 1) * (K2 - 1) - 1);
        } else if (type == ProfitType.UNIQUE_2) {
            return (K1 - 1 - K1 / K3);
        } else if (type == ProfitType.UNIQUE_3) {
            return 1 / (2 * (K2 - 1));
        } else {
            return 1.0; //TODO
        }
    }

    private Double getUniformSeparateX3(Double K1, Double K2, Double K3, Double X2, Double X4, ProfitType type) {
        if (type == ProfitType.UNIFORM_2_3) {
            return  (K1 - 1 - X2);
        } else if (type == ProfitType.UNIFORM_1_2) {
            return 2 * (1 + X2) * X4;
        } else if (type == ProfitType.UNIFORM_1_3) {
            return K1 / K3;
        } else if (type == ProfitType.UNIQUE_1) {
            return 2 * (K2 - 1) * X2 - 1;
        } else if (type == ProfitType.UNIQUE_2) {
            return K1 / K3;
        } else if (type == ProfitType.UNIQUE_3) {
            return K1 - 1 - X2;
        } else {
            return 1.0; //TODO
        }
    }
    private Double getUniformSeparateX4(Double K3, ProfitType type) {
        if (type == ProfitType.UNIFORM_1_2) {
            return 1 / (2 * (K3 - 1));
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
        Double X3 = getUniformSeparateX3(k.K1, k.K2, k.K3, X2, X4, type);

        Double C1 = getUniformSeparateC1(totalSum, X2, X3);
        Double C2 = getUniformSeparateC2(totalSum, X2, X3);
        Double C3 = getUniformSeparateC3(totalSum, X2, X3);
        branch.branches = fillProfit(bets, f, C1, C2, C3, k.K1, k.K2 ,k.K3);
        return branch;
    }

}
