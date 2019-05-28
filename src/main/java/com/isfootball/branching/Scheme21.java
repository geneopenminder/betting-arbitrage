package com.isfootball.branching;

/**
 * Created by Evgeniy Pshenitsin on 11.02.2015.
 */

import com.isfootball.parser.BetType;

import java.util.ArrayList;
import java.util.List;

public class Scheme21 extends BaseSchemeUniformOnly {

    //full
    static List<Formula> formulas = new ArrayList<Formula>() {{
        add(new Formula(937, "ТМ(1,25) – ТБ(0,75) – ТБ(1,5)", BetType.T125L, BetType.T075M, BetType.T15M));
        add(new Formula(938, "ТМ(2,25) – ТБ(1,75) – ТБ(2,5)", BetType.T225L, BetType.T175M, BetType.T25M));
        add(new Formula(939, "ТМ(3,25) – ТБ(2,75) – ТБ(3,5)", BetType.T325L, BetType.T275M, BetType.T35M));
        add(new Formula(940, "ТМ(4,25) – ТБ(3,75) – ТБ(4,5)", BetType.T425L, BetType.T375M, BetType.T45M));
        add(new Formula(941, "ТМ(5,25) – ТБ(4,75) – ТБ(5,5)", BetType.T525L, BetType.T475M, BetType.T55M));
        add(new Formula(942, "ТМ(6,25) – ТБ(5,75) – ТБ(6,5)", BetType.T625L, BetType.T575M, BetType.T65M));
        add(new Formula(943, "Т1М(1,25) – Т1Б(0,75) – Т1Б(1,5)", BetType.T1_125L, BetType.T1_075M, BetType.T1_15M));
        add(new Formula(944, "Т1М(2,25) – Т1Б(1,75) – Т1Б(2,5)", BetType.T1_225L, BetType.T1_175M, BetType.T1_25M));
        add(new Formula(945, "Т1М(3,25) – Т1Б(2,75) – Т1Б(3,5)", BetType.T1_325L, BetType.T1_275M, BetType.T1_35M));
        add(new Formula(946, "Т1М(4,25) – Т1Б(3,75) – Т1Б(4,5)", BetType.T1_425L, BetType.T1_375M, BetType.T1_45M));
        add(new Formula(947, "Т1М(5,25) – Т1Б(4,75) – Т1Б(5,5)", BetType.T1_525L, BetType.T1_475M, BetType.T1_55M));
        add(new Formula(948, "Т1М(6,25) – Т1Б(5,75) – Т1Б(6,5)", BetType.T1_625L, BetType.T1_575M, BetType.T1_65M));
        add(new Formula(950, "Т2М(1,25) – Т2Б(0,75) – Т2Б(1,5)", BetType.T2_125L, BetType.T2_075M, BetType.T2_15M));
        add(new Formula(951, "Т2М(2,25) – Т2Б(1,75) – Т2Б(2,5)", BetType.T2_225L, BetType.T2_175M, BetType.T2_25M));
        add(new Formula(952, "Т2М(3,25) – Т2Б(2,75) – Т2Б(3,5)", BetType.T2_325L, BetType.T2_275M, BetType.T2_35M));
        add(new Formula(953, "Т2М(4,25) – Т2Б(3,75) – Т2Б(4,5)", BetType.T2_425L, BetType.T2_375M, BetType.T2_45M));
        add(new Formula(954, "Т2М(5,25) – Т2Б(4,75) – Т2Б(5,5)", BetType.T2_525L, BetType.T2_475M, BetType.T2_55M));
        add(new Formula(955, "Т2М(6,25) – Т2Б(5,75) – Т2Б(6,5)", BetType.T2_625L, BetType.T2_575M, BetType.T2_65M));
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
                sum.profit = C1/2 + C1*K1/2 + C2/2+ C2*K2/2;
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

        Double X2 = (K1 - 1) / (K2 + 1);
        Double X3 = (K1 - K2 * X2) / K3;

        Double C1 = totalSum / (1 + X2 + X3);
        Double C2 = (X2 * totalSum) / (1 + X2 + X3);
        Double C3 = (X3 * totalSum) / (1 + X2 + X3);

        branch.branches = fillProfit(bets, f, C1, C2, C3, K1, K2 ,K3);
        for (EventSum sum: branch.branches) {
            if (sum.profit <= totalSum) {
                branch.isProfit = false;
            }
        }
        return branch;
    }

}
