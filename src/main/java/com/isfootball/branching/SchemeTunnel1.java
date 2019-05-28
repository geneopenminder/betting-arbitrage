package com.isfootball.branching;

/**
 * Created by Evgeniy Pshenitsin on 11.02.2015.
 */

import com.isfootball.parser.BetSite;
import com.isfootball.parser.BetType;

import java.util.*;

public class SchemeTunnel1 extends BaseSchemeTunnel {

    //full
    static List<Formula> formulas = new ArrayList<Formula>() {{
        add(new Formula(10001, "ТБ(0,5) – ТМ(1,5)", BetType.T05M, BetType.T15L,null));
        add(new Formula(10002, "ТБ(1,5) – ТМ(2,5)", BetType.T15M, BetType.T25L,null));
        add(new Formula(10003, "ТБ(2,5) – ТМ(3,5)", BetType.T25M, BetType.T35L,null));
        add(new Formula(10004, "ТБ(3,5) – ТМ(4,5)", BetType.T35M, BetType.T45L,null));
        add(new Formula(10005, "ТБ(4,5) – ТМ(5,5)", BetType.T45M, BetType.T55L,null));
        add(new Formula(10006, "ТБ(5,5) – ТМ(6,5)", BetType.T55M, BetType.T65L,null));
        add(new Formula(10007, "Т1Б(0,5) – Т1М(1,5)", BetType.T1_05M, BetType.T1_15L,null));
        add(new Formula(10008, "Т1Б(1,5) – Т1М(2,5)", BetType.T1_15M, BetType.T1_25L,null));
        add(new Formula(10009, "Т1Б(2,5) – Т1М(3,5)", BetType.T1_25M, BetType.T1_35L,null));
        add(new Formula(10010, "Т1Б(3,5) – Т1М(4,5)", BetType.T1_35M, BetType.T1_45L,null));
        add(new Formula(10011, "Т1Б(4,5) – Т1М(5,5)", BetType.T1_45M, BetType.T1_55L,null));
        add(new Formula(10012, "Т1Б(5,5) – Т1М(6,5)", BetType.T1_55M, BetType.T1_65L, null));
        add(new Formula(10013, "Т2Б(0,5) – Т2М(1,5)", BetType.T2_05M, BetType.T2_15L,null));
        add(new Formula(10014, "Т2Б(1,5) – Т2М(2,5)", BetType.T2_15M, BetType.T2_25L,null));
        add(new Formula(10015, "Т2Б(2,5) – Т2М(3,5)", BetType.T2_25M, BetType.T2_35L,null));
        add(new Formula(10016, "Т2Б(3,5) – Т2М(4,5)", BetType.T2_35M, BetType.T2_45L, null));
        add(new Formula(10017, "Т2Б(4,5) – Т2М(5,5)", BetType.T2_45M, BetType.T2_55L, null));
        add(new Formula(10018, "Т2Б(5,5) – Т2М(6,5)", BetType.T2_55M, BetType.T2_65L,null));
        add(new Formula(10019, "Ф1(+0,5) – Ф2(+0,5)", BetType.F1plus05, BetType.F2plus05,null));
        add(new Formula(10020, "Ф1(+1,5) – П2", BetType.F1plus15, BetType.P2,null));
        add(new Formula(10021, "Ф2(+1,5) – П1", BetType.F2plus15, BetType.P1,null));
        add(new Formula(10022, "Ф1(+1,5) – Ф2(-0,5)", BetType.F1plus15, BetType.F2minus05, null));
        add(new Formula(10023, "Ф2(+1,5) – Ф1(-0,5)", BetType.F2plus15, BetType.F1minus05, null));
        add(new Formula(10024, "Ф1(+2,5) – Ф2(-1,5)", BetType.F1plus25, BetType.F2minus15, null));
        add(new Formula(10025, "Ф2(+2,5) – Ф1(-1,5)", BetType.F2plus25, BetType.F1minus15, null));
        add(new Formula(10026, "Ф1(+3,5) – Ф2(-2,5)", BetType.F1plus35, BetType.F2minus25, null));
        add(new Formula(10027, "Ф2(+3,5) – Ф1(-2,5)", BetType.F2plus35, BetType.F1minus25, null));
        add(new Formula(10028, "Ф1(+4,5) – Ф2(-3,5)", BetType.F1plus45, BetType.F2minus35,null));
        add(new Formula(10029, "Ф2(+4,5) – Ф1(-3,5)", BetType.F2plus45, BetType.F1minus35,null));
        add(new Formula(10030, "Ф1(+5,5) – Ф2(-4,5)", BetType.F1plus55, BetType.F2minus45,null));
        add(new Formula(10031, "Ф2(+5,5) – Ф1(-4,5)", BetType.F2plus55, BetType.F1minus45, null));
        add(new Formula(10032, "Ф1(+6,5) – Ф2(-5,5)", BetType.F1plus65, BetType.F2minus55,null));
        add(new Formula(10033, "Ф2(+6,5) – Ф1(-5,5)", BetType.F2plus65, BetType.F1minus55, null));
        add(new Formula(10034, "1х – х2", BetType.P1X, BetType.P2X,null));
        add(new Formula(10035, "1х – Ф2(+0,5)", BetType.P1X, BetType.F2plus05, null));
        add(new Formula(10036, "2х – Ф1(+0,5)", BetType.P2X, BetType.F1plus05, null));
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
        middle.profit = C1*K1 + C2*K2;
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
