package com.isfootball.branching;

/**
 * Created by Evgeniy Pshenitsin on 11.02.2015.
 */

import com.isfootball.parser.BetSite;
import com.isfootball.parser.BetType;

import java.util.*;

public class SchemeTunnel2 extends BaseSchemeTunnel {

    //full
    static List<Formula> formulas = new ArrayList<Formula>() {{
        add(new Formula(10037,	"Ф1(0) – х2", BetType.F10, BetType.P2X,null));
        add(new Formula(10038,	"Ф2(0) – 1х", BetType.F20, BetType.P1X,null));
        add(new Formula(10039,	"Ф1(0) – Ф2(+0,5)", BetType.F10, BetType.F2plus05,null));
        add(new Formula(10040,	"Ф2(0) – Ф1(+0,5)", BetType.F20, BetType.F1plus05,null));
        add(new Formula(10041,	"Ф1(-1) – Ф2(+1,5)", BetType.F1minus1, BetType.F2plus15,null));
        add(new Formula(10042,	"Ф2(-1) – Ф1(+1,5)", BetType.F2minus1, BetType.F1plus15,null));
        add(new Formula(10043,	"Ф1(-2) – Ф2(+2,5)", BetType.F1minus2, BetType.F2plus25,null));
        add(new Formula(10044,	"Ф2(-2) – Ф1(+2,5)", BetType.F2minus2, BetType.F1plus25,null));
        add(new Formula(10045,	"Ф1(-3) – Ф2(+3,5)", BetType.F1minus3, BetType.F2plus35,null));
        add(new Formula(10046,	"Ф2(-3) – Ф1(+3,5)", BetType.F2minus3, BetType.F1plus35,null));
        add(new Formula(10047,	"Ф1(-4) – Ф2(+4,5)", BetType.F1minus4, BetType.F2plus45,null));
        add(new Formula(10048,	"Ф2(-4) – Ф1(+4,5)", BetType.F2minus4, BetType.F1plus45,null));
        add(new Formula(10049,	"Ф1(-5) – Ф2(+5,5)", BetType.F1minus5, BetType.F2plus55,null));
        add(new Formula(10050,	"Ф2(-5) – Ф1(+5,5)", BetType.F2minus5, BetType.F1plus55,null));
        add(new Formula(10051,	"Ф1(-6) – Ф2(+6,5)", BetType.F1minus6, BetType.F2plus65,null));
        add(new Formula(10052,	"Ф2(-6) – Ф1(+6,5)", BetType.F2minus6, BetType.F1plus65,null));
        add(new Formula(10053,	"ТБ(1) – ТМ(1,5)", BetType.T1M, BetType.T15L,null));
        add(new Formula(10054,	"ТБ(2) – ТМ(2,5)", BetType.T2M, BetType.T25L,null));
        add(new Formula(10055,	"ТБ(3) – ТМ(3,5)", BetType.T3M, BetType.T35L,null));
        add(new Formula(10056,	"ТБ(4) – ТМ(4,5)", BetType.T4M, BetType.T45L,null));
        add(new Formula(10057,	"ТБ(5) – ТМ(5,5)", BetType.T5M, BetType.T55L,null));
        add(new Formula(10058,	"ТБ(6) – ТМ(6,5)", BetType.T6M, BetType.T65L,null));
        add(new Formula(10059,	"Т1Б(1) – Т1М(1,5)", BetType.T1_1M, BetType.T1_15L,null));
        add(new Formula(10060,	"Т1Б(2) – Т1М(2,5)", BetType.T1_2M, BetType.T1_25L,null));
        add(new Formula(10061,	"Т1Б(3) – Т1М(3,5)", BetType.T1_3M, BetType.T1_35L,null));
        add(new Formula(10062,	"Т1Б(4) – Т1М(4,5)", BetType.T1_4M, BetType.T1_45L,null));
        add(new Formula(10063,	"Т1Б(5) – Т1М(5,5)", BetType.T1_5M, BetType.T1_55L,null));
        add(new Formula(10064,	"Т1Б(6) – Т1М(6,5)", BetType.T1_6M, BetType.T1_65L,null));
        add(new Formula(10065,	"Т2Б(1) – Т2М(1,5)", BetType.T2_1M, BetType.T2_15L,null));
        add(new Formula(10066,	"Т2Б(2) – Т2М(2,5)", BetType.T2_2M, BetType.T2_25L,null));
        add(new Formula(10067,	"Т2Б(3) – Т2М(3,5)", BetType.T2_3M, BetType.T2_35L,null));
        add(new Formula(10068,	"Т2Б(4) – Т2М(4,5)", BetType.T2_4M, BetType.T2_45L,null));
        add(new Formula(10069,	"Т2Б(5) – Т2М(5,5)", BetType.T2_5M, BetType.T2_55L,null));
        add(new Formula(10070,	"Т2Б(6) – Т2М(6,5)", BetType.T2_6M, BetType.T2_65L,null));
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
        middle.profit = C1 + C2*K2;
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
