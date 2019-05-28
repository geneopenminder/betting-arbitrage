package com.isfootball.branching;

/**
 * Created by Evgeniy Pshenitsin on 11.02.2015.
 */

import com.isfootball.parser.BetType;

import java.util.ArrayList;
import java.util.List;

public class Scheme15 extends BaseSchemeNonUniform {

    //full
    static List<Formula> formulas = new ArrayList<Formula>() {{
        add(new Formula(862, "Ф1(+0,25) – Ф2(0) – Ф2(-0,5)", BetType.F1plus025, BetType.F20, BetType.F2minus05));
        add(new Formula(863, "Ф2(+0,25) – Ф1(0) – Ф1(-0,5)", BetType.F2plus025, BetType.F10, BetType.F1minus05));
        add(new Formula(864, "Ф1(+1,25) – Ф2(-1) – Ф2(-1,5)", BetType.F1plus125, BetType.F2minus1, BetType.F2minus15));
        add(new Formula(865, "Ф2(+1,25) – Ф1(-1) – Ф1(-1,5)", BetType.F2plus125, BetType.F1minus1, BetType.F1minus15));
        add(new Formula(866, "Ф1(+2,25) – Ф2(-2) – Ф2(-2,5)", BetType.F1plus225, BetType.F2minus2, BetType.F2minus25));
        add(new Formula(867, "Ф2(+2,25) – Ф1(-2) – Ф1(-2,5)", BetType.F2plus225, BetType.F1minus2, BetType.F1minus25));
        add(new Formula(868, "Ф1(+3,25) – Ф2(-3) – Ф2(-3,5)", BetType.F1plus325, BetType.F2minus3, BetType.F2minus35));
        add(new Formula(869, "Ф2(+3,25) – Ф1(-3) – Ф1(-3,5)", BetType.F2plus325, BetType.F1minus3, BetType.F1minus35));
        add(new Formula(870, "Ф1(+4,25) – Ф2(-4) – Ф2(-4,5)", BetType.F1plus425, BetType.F2minus4, BetType.F2minus45));
        add(new Formula(871, "Ф2(+4,25) – Ф1(-4) – Ф1(-4,5)", BetType.F2plus425, BetType.F1minus4, BetType.F1minus45));
        add(new Formula(872, "Ф1(+5,25) – Ф2(-5) – Ф2(-5,5)", BetType.F1plus525, BetType.F2minus5, BetType.F2minus55));
        add(new Formula(873, "Ф2(+5,25) – Ф1(-5) – Ф1(-5,5)", BetType.F2plus525, BetType.F1minus5, BetType.F1minus55));
        add(new Formula(874, "Ф1(+6,25) – Ф2(-6) – Ф2(-6,5)", BetType.F1plus625, BetType.F2minus6, BetType.F2minus65));
        add(new Formula(875, "Ф2(+6,25) – Ф1(-6) – Ф1(-6,5)", BetType.F2plus625, BetType.F1minus6, BetType.F1minus65));
        add(new Formula(876, "Ф1(-0,75) – Ф2(+1) – 2х", BetType.F1minus075, BetType.F2plus1, BetType.P2X));
        add(new Formula(877, "Ф2(-0,75) – Ф1(+1) – 1х", BetType.F2minus075, BetType.F1plus1, BetType.P1X));
        add(new Formula(878, "Ф1(-0,75) – Ф2(+1) – Ф2(+0,5)", BetType.F1minus075, BetType.F2plus1, BetType.F2plus05));
        add(new Formula(879, "Ф2(-0,75) – Ф1(+1) – Ф1(+0,5)", BetType.F2minus075, BetType.F1plus1, BetType.F1plus05));
        add(new Formula(880, "Ф1(-1,75) – Ф2(+2) – Ф2(+1,5)", BetType.F1minus175, BetType.F2plus2, BetType.F2plus15));
        add(new Formula(881, "Ф2(-1,75) – Ф1(+2) – Ф1(+1,5)", BetType.F2minus175, BetType.F1plus2, BetType.F1plus15));
        add(new Formula(882, "Ф1(-2,75) – Ф2(+3) – Ф2(+2,5)", BetType.F1minus275, BetType.F2plus3, BetType.F2plus25));
        add(new Formula(883, "Ф2(-2,75) – Ф1(+3) – Ф1(+2,5)", BetType.F2minus275, BetType.F1plus3, BetType.F1plus25));
        add(new Formula(884, "Ф1(-3,75) – Ф2(+4) – Ф2(+3,5)", BetType.F1minus375, BetType.F2plus4, BetType.F2plus35));
        add(new Formula(885, "Ф2(-3,75) – Ф1(+4) – Ф1(+3,5)", BetType.F2minus375, BetType.F1plus4, BetType.F1plus35));
        add(new Formula(886, "Ф1(-4,75) – Ф2(+5) – Ф2(+4,5)", BetType.F1minus475, BetType.F2plus5, BetType.F2plus45));
        add(new Formula(887, "Ф2(-4,75) – Ф1(+5) – Ф1(+4,5)", BetType.F2minus475, BetType.F1plus5, BetType.F1plus45));
        add(new Formula(888, "Ф1(-5,75) – Ф2(+6) – Ф2(+5,5)", BetType.F1minus575, BetType.F2plus6, BetType.F2plus55));
        add(new Formula(889, "Ф2(-5,75) – Ф1(+6) – Ф1(+5,5)", BetType.F2minus575, BetType.F1plus6, BetType.F1plus55));
        add(new Formula(890, "Ф1(-6,75) – Ф2(+7) – Ф2(+6,5)", BetType.F1minus675, BetType.F2plus7, BetType.F2plus65));
        add(new Formula(891, "Ф2(-6,75) – Ф1(+7) – Ф1(+6,5)", BetType.F2minus675, BetType.F1plus7, BetType.F1plus65));
        add(new Formula(892, "Ф1(+1,25) – Ф2(-1) – Г2(-1)", BetType.F1plus125, BetType.F2minus1, BetType.G2minus1));
        add(new Formula(893, "Ф2(+1,25) – Ф1(-1) – Г1(-1)", BetType.F2plus125, BetType.F1minus1, BetType.G1minus1));
        add(new Formula(894, "Ф1(+2,25) – Ф2(-2) – Г2(-2)", BetType.F1plus225, BetType.F2minus2, BetType.G2minus2));
        add(new Formula(895, "Ф2(+2,25) – Ф1(-2) – Г1(-2)", BetType.F2plus225, BetType.F1minus2, BetType.G1minus2));
        add(new Formula(896, "Ф1(+3,25) – Ф2(-3) – Г2(-3)", BetType.F1plus325, BetType.F2minus3, BetType.G2minus3));
        add(new Formula(897, "Ф2(+3,25) – Ф1(-3) – Г1(-3)", BetType.F2plus325, BetType.F1minus3, BetType.G1minus3));
        add(new Formula(898, "Ф1(+4,25) – Ф2(-4) – Г2(-4)", BetType.F1plus425, BetType.F2minus4, BetType.G2minus4));
        add(new Formula(899, "Ф2(+4,25) – Ф1(-4) – Г1(-4)", BetType.F2plus425, BetType.F1minus4, BetType.G1minus4));
        add(new Formula(900, "Ф1(-0,75) – Ф2(+1) – Г2(+1)", BetType.F1minus075, BetType.F2plus1, BetType.G2plus1));
        add(new Formula(901, "Ф2(-0,75) – Ф1(+1) – Г1(+1)", BetType.F2minus075, BetType.F1plus1, BetType.G1plus1));
        add(new Formula(902, "Ф1(-1,75) – Ф2(+2) – Г2(+2)", BetType.F1minus175, BetType.F2plus2, BetType.G2plus2));
        add(new Formula(903, "Ф2(-1,75) – Ф1(+2) – Г1(+2)", BetType.F2minus175, BetType.F1plus2, BetType.G1plus2));
        add(new Formula(904, "Ф1(-2,75) – Ф2(+3) – Г2(+3)", BetType.F1minus275, BetType.F2plus3, BetType.G2plus3));
        add(new Formula(905, "Ф2(-2,75) – Ф1(+3) – Г1(+3)", BetType.F2minus275, BetType.F1plus3, BetType.G1plus3));
        add(new Formula(906, "Ф1(-3,75) – Ф2(+4) – Г2(+4)", BetType.F1minus375, BetType.F2plus4, BetType.G2plus4));
        add(new Formula(907, "Ф2(-3,75) – Ф1(+4) – Г1(+4)", BetType.F2minus375, BetType.F1plus4, BetType.G1plus4));
        add(new Formula(908, "ТБ(0,75) – ТМ(1) – ТМ(0,5)", BetType.T075M, BetType.T1L, BetType.T05L));
        add(new Formula(909, "ТБ(1,75) – ТМ(2) – ТМ(1,5)", BetType.T175M, BetType.T2L, BetType.T15L));
        add(new Formula(910, "ТБ(2,75) – ТМ(3) – ТМ(2,5)", BetType.T275M, BetType.T3L, BetType.T25L));
        add(new Formula(911, "ТБ(3,75) – ТМ(4) – ТМ(3,5)", BetType.T375M, BetType.T4L, BetType.T35L));
        add(new Formula(912, "ТБ(4,75) – ТМ(5) – ТМ(4,5)", BetType.T475M, BetType.T5L, BetType.T45L));
        add(new Formula(913, "ТБ(5,75) – ТМ(6) – ТМ(5,5)", BetType.T575M, BetType.T6L, BetType.T55L));
        add(new Formula(914, "ТБ(6,75) – ТМ(7) – ТМ(6,5)", BetType.T675M, BetType.T7L, BetType.T65L));
        add(new Formula(915, "Т1Б(0,75) – Т1М(1) – Т1М(0,5)", BetType.T1_075M, BetType.T1_1L, BetType.T1_05L));
        add(new Formula(916, "Т1Б(1,75) – Т1М(2) – Т1М(1,5)", BetType.T1_175M, BetType.T1_2L, BetType.T1_15L));
        add(new Formula(917, "Т1Б(2,75) – Т1М(3) – Т1М(2,5)", BetType.T1_275M, BetType.T1_3L, BetType.T1_25L));
        add(new Formula(918, "Т1Б(3,75) – Т1М(4) – Т1М(3,5)", BetType.T1_375M, BetType.T1_4L, BetType.T1_35L));
        add(new Formula(919, "Т1Б(4,75) – Т1М(5) – Т1М(4,5)", BetType.T1_475M, BetType.T1_5L, BetType.T1_45L));
        add(new Formula(920, "Т1Б(5,75) – Т1М(6) – Т1М(5,5)", BetType.T1_575M, BetType.T1_6L, BetType.T1_55L));
        add(new Formula(921, "Т1Б(6,75) – Т1М(7) – Т1М(6,5)", BetType.T1_675M, BetType.T1_7L, BetType.T1_65L));
        add(new Formula(922, "Т2Б(0,75) – Т2М(1) – Т2М(0,5)", BetType.T2_075M, BetType.T2_1L, BetType.T2_05L));
        add(new Formula(923, "Т2Б(1,75) – Т2М(2) – Т2М(1,5)", BetType.T2_175M, BetType.T2_2L, BetType.T2_15L));
        add(new Formula(924, "Т2Б(2,75) – Т2М(3) – Т2М(2,5)", BetType.T2_275M, BetType.T2_3L, BetType.T2_25L));
        add(new Formula(925, "Т2Б(3,75) – Т2М(4) – Т2М(3,5)", BetType.T2_375M, BetType.T2_4L, BetType.T2_35L));
        add(new Formula(926, "Т2Б(4,75) – Т2М(5) – Т2М(4,5)", BetType.T2_475M, BetType.T2_5L, BetType.T2_45L));
        add(new Formula(927, "Т2Б(5,75) – Т2М(6) – Т2М(5,5)", BetType.T2_575M, BetType.T2_6L, BetType.T2_55L));
        add(new Formula(928, "Т2Б(6,75) – Т2М(7) – Т2М(6,5)", BetType.T2_675M, BetType.T2_7L, BetType.T2_65L));
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
                sum.profit = 0.5*K1*C1 + C1/2 + C2;
            } else if (sum.bet == f.var3) {
                sum.sum = C3;
                sum.profit = C3*K3 + C2*K2;
            }
            betsResult.add(new EventSum(sum));
        }
        return betsResult;
    }

    protected BranchResult calculateUniform(List<EventSum> bets, Formula f, Double totalSum) {
        BranchResult branch = new BranchResult();
        branch.isProfit = true;
        branch.totalSum = totalSum;
        //Х1 = 1 /2 + 1 / (2 * К1) + 1 / К3 – К2 * (К1 – 1) / (2 * К3 * К1)
        //X2 = (2 * К1 – К2 * (К1 – 1)) / (2 * К3)
        KKK k = getK(bets, f);
        Double K1 = k.K1;
        Double K2 = k.K2;
        Double K3 = k.K3;

        Double X1 = 0.5 + (1/(2 * K1)) + (1/K3) - (K2*(K1 - 1)/(2 * K3 * K1));
        Double X2 = (2 * K1 - K2 * (K1 - 1)) / (2 * K3);
        Double C1 = totalSum / (X1 * K1);
        Double C2 = totalSum * (K1 - 1) / (2*X1*K1);
        Double C3 = (totalSum * X2) / (X1*K1);
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
            return (K3 * (K1 - 1) - (K1 + 1) / 2) / (K3 - K2 + 1);
        } else if (type == ProfitType.UNIFORM_1_2) {
            return (K1 - 1) / 2;
        } else if (type == ProfitType.UNIFORM_1_3) {
            return (K1 - K3 * (K1 - 1) / 2) / K2;
        } else if (type == ProfitType.UNIQUE_1) {
            return  (1 - (K1 - 1) * (K3 - 1) / 2) / (K2 - 1);
        } else if (type == ProfitType.UNIQUE_2) {
            return (K1 + K3 - K1 * K3) / (K2 - K3);
        } else if (type == ProfitType.UNIQUE_3) {
            return (K1 - 1) / 2;
        } else {
            return 1.0; //TODO
        }
    }

    private Double getUniformSeparateX3(Double K1, Double K2, Double K3, Double X2, ProfitType type) {
        if (type == ProfitType.UNIFORM_2_3) {
            return ((K1 + 1) / 2 - (K2 - 1) * (K1 - 1)) / (K3 - K2  + 1);
        } else if (type == ProfitType.UNIFORM_1_2) {
            return (1 - (K2 -1) * (K1 - 1) / 2) / (K3 - 1);
        } else if (type == ProfitType.UNIFORM_1_3) {
            return (K1 - 1) / 2;
        } else if (type == ProfitType.UNIQUE_1) {
            return (K1 - 1) / 2;
        } else if (type == ProfitType.UNIQUE_2) {
            return K1 - 1 - X2;
        } else if (type == ProfitType.UNIQUE_3) {
            return (K1 - 1) / 2;
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
