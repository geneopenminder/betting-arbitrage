package com.isfootball.branching;

/**
 * Created by Evgeniy Pshenitsin on 11.02.2015.
 */

import com.isfootball.parser.BetSite;
import com.isfootball.parser.BetType;

import java.util.*;

public class SchemeTunnel3 extends BaseSchemeTunnel {

    //full
    static List<Formula> formulas = new ArrayList<Formula>() {{
        add(new Formula(10071,	"Ф1(+0,25) – Ф2(+0,25)", BetType.F1plus025, BetType.F2plus025,null));
        add(new Formula(10072,	"Ф1(-0,75) – Ф2(+1,25)", BetType.F1minus075, BetType.F2plus125,null));
        add(new Formula(10073,	"Ф2(-0,75) – Ф1(+1,25)", BetType.F2minus075, BetType.F1plus125,null));
        add(new Formula(10074,	"Ф1(-1,75) – Ф2(+2,25)", BetType.F1minus175, BetType.F2plus225,null));
        add(new Formula(10075,	"Ф2(-1,75) – Ф1(+2,25)", BetType.F2minus175, BetType.F1plus225,null));
        add(new Formula(10076,	"Ф1(-2,75) – Ф2(+3,25)", BetType.F1minus275, BetType.F2plus325,null));
        add(new Formula(10077,	"Ф2(-2,75) – Ф1(+3,25)", BetType.F2minus275, BetType.F1plus325,null));
        add(new Formula(10078,	"Ф1(-3,75) – Ф2(+4,25)", BetType.F1minus375, BetType.F2plus425,null));
        add(new Formula(10079,	"Ф2(-3,75) – Ф1(+4,25)", BetType.F2minus375, BetType.F1plus425,null));
        add(new Formula(10080,	"Ф1(-4,75) – Ф2(+5,25)", BetType.F1minus475, BetType.F2plus525,null));
        add(new Formula(10081,	"Ф2(-4,75) – Ф1(+5,25)", BetType.F2minus475, BetType.F1plus525,null));
        add(new Formula(10082,	"Ф1(-5,75) – Ф2(+6,25)", BetType.F1minus575, BetType.F2plus625,null));
        add(new Formula(10083,	"Ф2(-5,75) – Ф1(+6,25)", BetType.F2minus575, BetType.F1plus625,null));
        add(new Formula(10084,	"ТБ (0,75) – ТМ (1,25)", BetType.T075M, BetType.T125L,null));
        add(new Formula(10085,	"ТБ (1,75) – ТМ (2,25)", BetType.T175M, BetType.T25L,null));
        add(new Formula(10086,	"ТБ (2,75) – ТМ (3,25)", BetType.T275M, BetType.T325L,null));
        add(new Formula(10087,	"ТБ (3,75) – ТМ (4,25)", BetType.T375M, BetType.T425L,null));
        add(new Formula(10088,	"ТБ (4,75) – ТМ (5,25)", BetType.T475M, BetType.T525L,null));
        add(new Formula(10089,	"ТБ (5,75) – ТМ (6,25)", BetType.T575M, BetType.T625L,null));
        add(new Formula(10090,	"Т1Б (0,75) – Т1М (1,25)", BetType.T1_075M, BetType.T1_125L,null));
        add(new Formula(10091,	"Т1Б (1,75) – Т1М (2,25)", BetType.T1_175M, BetType.T1_225L,null));
        add(new Formula(10092,	"Т1Б (2,75) – Т1М (3,25)", BetType.T1_275M, BetType.T1_325L,null));
        add(new Formula(10093,	"Т1Б (3,75) – Т1М (4,25)", BetType.T1_375M, BetType.T1_425L,null));
        add(new Formula(10094,	"Т1Б (4,75) – Т1М (5,25)", BetType.T1_475M, BetType.T1_525L,null));
        add(new Formula(10095,	"Т1Б (5,75) – Т1М (6,25)", BetType.T1_575M, BetType.T1_625L,null));
        add(new Formula(10096,	"Т2Б (0,75) – Т2М (1,25)", BetType.T2_075M, BetType.T2_125L,null));
        add(new Formula(10097,	"Т2Б (1,75) – Т2М (2,25)", BetType.T2_175M, BetType.T2_225L,null));
        add(new Formula(10098,	"Т2Б (2,75) – Т2М (3,25)", BetType.T2_275M, BetType.T2_325L,null));
        add(new Formula(10099,	"Т2Б (3,75) – Т2М (4,25)", BetType.T2_375M, BetType.T2_425L,null));
        add(new Formula(10100,	"Т2Б (4,75) – Т2М (5,25)", BetType.T2_475M, BetType.T2_525L,null));
        add(new Formula(10101,  "Т2Б (5,75) – Т2М (6,25)", BetType.T2_575M, BetType.T2_625L,null));
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
        EventSum middle = new EventSum();
        middle.sum = 0.0; //TODO
        middle.profit = 0.5*C1 + 0.5*C1*K1 + 0.5*C2 + 0.5*C2*K2;
        middle.isMiddle = true;
        //middle.site = BetSite.NONE;
        //middle.link = "TUNNEL_DELETE";
        //middle.bet = BetType.X;
        //middle.kReal = 1.0;

        betsResult.add(middle);
        return betsResult;
    }

    protected BranchResult calculateTunnel(List<EventSum> bets, Formula f, Double totalSum) {
        BranchResult branch = new BranchResult();
        branch.isProfit = true;
        branch.totalSum = totalSum;
        KKK k = getK(bets, f);
        Double K1 = k.K1;
        Double K2 = k.K2;

        //Х1 = 1 / К1 + 1 / К2
        Double X1 = 1 / K1 + 1 / K2;

        Double C1 = totalSum / (X1 * K1);
        Double C2 = totalSum / (X1 * K2);


        branch.branches = fillProfit(bets, f, C1, C2, K1, K2);
        for (EventSum sum: branch.branches) {
            if (sum.profit <= totalSum * 0.95) {
                branch.isProfit = false;
            }
        }

        //TODO
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

}
