package com.isfootball.branching;

/**
 * Created by Evgeniy Pshenitsin on 11.02.2015.
 */

import com.isfootball.parser.BetSite;
import com.isfootball.parser.BetType;

import java.util.*;

public class SchemeTunnel4 extends BaseSchemeTunnel {

    //full
    static List<Formula> formulas = new ArrayList<Formula>() {{
        add(new Formula(10102,	"Ф1(0) – Ф2(+0,25)",  BetType.F10, BetType.F2plus025,null));
        add(new Formula(10103,	"Ф1(-1) – Ф2(+1,25)", BetType.F1minus1, BetType.F2plus125,null));
        add(new Formula(10104,	"Ф2(-1) – Ф1(+1,25)", BetType.F2minus1, BetType.F1plus125,null));
        add(new Formula(10105,	"Ф1(-2) – Ф2(+2,25)", BetType.F1minus2, BetType.F2plus225,null));
        add(new Formula(10106,	"Ф2(-2) – Ф1(+2,25)", BetType.F2minus2, BetType.F1plus225,null));
        add(new Formula(10107,	"Ф1(-3) – Ф2(+3,25)", BetType.F1minus3, BetType.F2plus325,null));
        add(new Formula(10108,	"Ф2(-3) – Ф1(+3,25)", BetType.F2minus3, BetType.F1plus325,null));
        add(new Formula(10109,	"Ф1(-4) – Ф2(+4,25)", BetType.F1minus4, BetType.F2plus425,null));
        add(new Formula(10110,	"Ф2(-4) – Ф1(+4,25)", BetType.F2minus4, BetType.F1plus425,null));
        add(new Formula(10111,	"Ф1(-5) – Ф2(+5,25)", BetType.F1minus5, BetType.F2plus525,null));
        add(new Formula(10112,	"Ф2(-5) – Ф1(+5,25)", BetType.F2minus5, BetType.F1plus525,null));
        add(new Formula(10113,	"Ф1(-6) – Ф2(+6,25)", BetType.F1minus6, BetType.F2plus625,null));
        add(new Formula(10114,	"Ф2(-6) – Ф1(+6,25)", BetType.F2minus6, BetType.F1plus625,null));
        add(new Formula(10115,	"ТБ(1) – ТМ(1,25)", BetType.T1M, BetType.T125L,null));
        add(new Formula(10116,	"ТБ(2) – ТМ(2,25)", BetType.T2M, BetType.T225L,null));
        add(new Formula(10117,	"ТБ(3) – ТМ(3,25)", BetType.T3M, BetType.T325L,null));
        add(new Formula(10118,	"ТБ(4) – ТМ(4,25)", BetType.T4M, BetType.T425L,null));
        add(new Formula(10119,	"ТБ(5) – ТМ(5,25)", BetType.T5M, BetType.T525L,null));
        add(new Formula(10120,	"ТБ(6) – ТМ(6,25)", BetType.T6M, BetType.T625L,null));
        add(new Formula(10121,	"Т1Б(1) – Т1М(1,25)", BetType.T1_1M, BetType.T1_125L,null));
        add(new Formula(10122,	"Т1Б(2) – Т1М(2,25)", BetType.T1_2M, BetType.T1_225L,null));
        add(new Formula(10123,	"Т1Б(3) – Т1М(3,25)", BetType.T1_3M, BetType.T1_325L,null));
        add(new Formula(10124,	"Т1Б(4) – Т1М(4,25)", BetType.T1_4M, BetType.T1_425L,null));
        add(new Formula(10125,	"Т1Б(5) – Т1М(5,25)", BetType.T1_5M, BetType.T1_525L,null));
        add(new Formula(10126,	"Т1Б(6) – Т1М(6,25)", BetType.T1_6M, BetType.T1_625L,null));
        add(new Formula(10127,	"Т2Б(1) – Т2М(1,25)", BetType.T2_1M, BetType.T2_125L,null));
        add(new Formula(10128,	"Т2Б(2) – Т2М(2,25)", BetType.T2_2M, BetType.T2_225L,null));
        add(new Formula(10129,	"Т2Б(3) – Т2М(3,25)", BetType.T2_3M, BetType.T2_325L,null));
        add(new Formula(10130,	"Т2Б(4) – Т2М(4,25)", BetType.T2_4M, BetType.T2_425L,null));
        add(new Formula(10131,	"Т2Б(5) – Т2М(5,25)", BetType.T2_5M, BetType.T2_525L,null));
        add(new Formula(10132,	"Т2Б(6) – Т2М(6,25)", BetType.T2_6M, BetType.T2_625L,null));
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
        middle.profit = C1 + 0.5*C2 + 0.5*C2*K2;
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
