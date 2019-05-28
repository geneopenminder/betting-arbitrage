package com.isfootball.branching;

/**
 * Created by Evgeniy Pshenitsin on 11.02.2015.
 */

import com.isfootball.parser.BetType;

import java.util.ArrayList;
import java.util.List;

public class Scheme16 extends BaseSchemeNonUniform {

    //full
    static List<Formula> formulas = new ArrayList<Formula>() {{
        add(new Formula(929, "1х – 12 – 2х", BetType.P1X, BetType.P12, BetType.P2X));
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
                sum.profit = C1*K1 + C2*K2;
            } else if (sum.bet == f.var2) {
                sum.sum = C2;
                sum.profit = C1*K1 + C3 + K3;
            } else if (sum.bet == f.var3) {
                sum.sum = C3;
                sum.profit = C2*K2 + K3 + C3;
            }
            betsResult.add(new EventSum(sum));
        }
        return betsResult;
    }

    protected BranchResult calculateUniform(List<EventSum> bets, Formula f, Double totalSum) {
        BranchResult branch = new BranchResult();
        branch.isProfit = true;
        branch.totalSum = totalSum;
        //Х1 = 1 / K1 + 1 / K2 + 1 / K3
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

    protected BranchResult calculateUniformSeparate(List<EventSum> bets, Formula f, Double totalSum, ProfitType type) {
        BranchResult branch = new BranchResult();
        branch.isProfit = true;
        branch.totalSum = totalSum;
        KKK k = getK(bets, f);
        Double K1 = k.K1;
        Double K2 = k.K2;
        Double K3 = k.K3;

        Double C1 = 0.0;
        Double C2 = 0.0;
        Double C3 = 0.0;

        if (type == ProfitType.UNIFORM_1_2) {
            C1 = totalSum - C2 - C3;
            C2 = totalSum / (2 * K2);
            C3 = totalSum / (2 * K3);
        } else if (type == ProfitType.UNIFORM_2_3) {
            C1 = totalSum / (2 * K1);
            C2 = totalSum / (2 * K2);
            C3 = totalSum - C1 - C2;
        } else if (type == ProfitType.UNIFORM_1_3) {
            C1 = totalSum / (2 * K1);
            C3 = totalSum / (2 * K3);
            C2 = totalSum - C1 - C3;
        } else if (type == ProfitType.UNIQUE_1) {
            C1 = totalSum / (1 + K1 / K2 + (K1 / K2 + 1 - K1) / (K3 - 1));
            C2 = (C1 * K1) / K2;
            C3 = totalSum - C1 - C2;
        } else if (type == ProfitType.UNIQUE_2) {
            C1 = totalSum / (1 + K1 / K3 + (K1 / K3 + 1 - K1) / (K2 - 1));
            C3 = (C1 * K1) / K3;
            C2 = totalSum - C1 - C3;
        } else if (type == ProfitType.UNIQUE_3) {
            C2 = totalSum / (1 + K2 / K3 + (K2 / K3 + 1 - K2) / (K1 - 1));
            C3 = (C2 * K2) / K3;
            C1 = totalSum - C2 - C3;
        }

        branch.branches = fillProfit(bets, f, C1, C2, C3, k.K1, k.K2 ,k.K3);
        return branch;
    }

}
