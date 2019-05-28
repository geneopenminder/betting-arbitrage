package com.isfootball.branching;

/**
 * Created by Evgeniy Pshenitsin on 11.02.2015.
 */

import com.isfootball.parser.BetType;

import java.util.ArrayList;
import java.util.List;

public class Scheme20 extends BaseSchemeUniformOnly {

    //full
    static List<Formula> formulas = new ArrayList<Formula>() {{
        add(new Formula(935, "Ф1(0) – 12 – Ф2(+0,25)", BetType.F10, BetType.P12, BetType.F2plus025));
        add(new Formula(936, "Ф2(0) – 12 – Ф1(+0,25)", BetType.F20, BetType.P12, BetType.F1plus025));
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
                sum.profit = C1 + 0.5*C3 + 0.5*C3*K3;
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
        KKK k = getK(bets, f);
        Double K1 = k.K1;
        Double K2 = k.K2;
        Double K3 = k.K3;

        Double X2 = (1.0 + ((K3 + 1.0) * K1) / (2.0 * K3) - K1) / K2;
        Double X3 = K1 / K3;

        Double C1 = totalSum / (1.0 + X2 + X3);
        Double C2 = (totalSum * X2) / (1.0 + X2 + X3);
        Double C3 = (totalSum * X3) / (1.0 + X2 + X3);

        branch.branches = fillProfit(bets, f, C1, C2, C3, K1, K2 ,K3);
        for (EventSum sum: branch.branches) {
            if (sum.profit <= totalSum) {
                branch.isProfit = false;
            }
        }
        return branch;
    }

}
