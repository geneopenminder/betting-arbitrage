package com.isfootball.branching;

/**
 * Created by Evgeniy Pshenitsin on 11.02.2015.
 */

import com.isfootball.parser.BetType;

import java.util.ArrayList;
import java.util.List;

public class Scheme2 extends BaseSchemeNonUniform {

    //full
    static List<Formula> formulas = new ArrayList<Formula>() {{
        add(new Formula(29, "Ф1(0) – 2х – П2", BetType.F10, BetType.P2X, BetType.P2));
        add(new Formula(30, "Ф2(0) – 1х – П1", BetType.F20, BetType.P1X, BetType.P1));
        add(new Formula(31, "Ф1(0) – Ф2(+0,5) – П2", BetType.F10, BetType.F2plus05, BetType.P2));
        add(new Formula(32, "Ф2(0) – Ф1(+0,5) – П1", BetType.F20, BetType.F1plus05, BetType.P1));
        add(new Formula(33, "Ф1(0) – 2х – Ф2 (-0,5)", BetType.F10, BetType.P2X, BetType.F2minus05));
        add(new Formula(34, "Ф2(0) – 1х – Ф1(-0,5)", BetType.F20, BetType.P1X, BetType.F1minus05));
        add(new Formula(35, "Ф1(0) – Ф2(+0,5) – Ф2(-0,5)", BetType.F10, BetType.F2plus05, BetType.F2minus05));
        add(new Formula(36, "Ф2(0) – Ф1(+0,5) – Ф1(-0,5)", BetType.F20, BetType.F1plus05, BetType.F1minus05));
        add(new Formula(37, "Ф1(+1) – П2 – Ф2(-1,5)", BetType.F1plus1, BetType.P2, BetType.F2minus15));
        add(new Formula(38, "Ф2(+1) – П1 – Ф1(-1,5)", BetType.F2plus1, BetType.P1, BetType.F1minus15));
        add(new Formula(39, "Ф1(+2) – Ф2(-1,5) – Ф2 (-2,5)", BetType.F1plus2, BetType.F2minus15, BetType.F2minus25));
        add(new Formula(40, "Ф2(+2) – Ф1(-1,5) – Ф1(-2,5)", BetType.F2plus2, BetType.F1minus15, BetType.F1minus25));
        add(new Formula(41, "Ф1(+3) – Ф2(-2,5) – Ф2(-3,5)", BetType.F1plus3, BetType.F2minus25, BetType.F2minus35));
        add(new Formula(42, "Ф2(+3) – Ф1(-2,5) – Ф1(-3,5)", BetType.F2plus3, BetType.F1minus25, BetType.F1minus35));
        add(new Formula(43, "Ф1(+4) – Ф2(-3,5) – Ф2(-4,5)", BetType.F1plus4, BetType.F2minus35, BetType.F2minus45));
        add(new Formula(44, "Ф2(+4) – Ф1(-3,5) – Ф2(-4,5)", BetType.F2plus4, BetType.F1minus35, BetType.F2minus45));
        add(new Formula(45, "Ф1(+5) – Ф2(-4,5) – Ф2(-5,5)", BetType.F1plus5, BetType.F2minus45, BetType.F2minus55));
        add(new Formula(46, "Ф2(+5) – Ф1(-4,5) – Ф2(-5,5)", BetType.F2plus5, BetType.F1minus45, BetType.F2minus55));
        add(new Formula(47, "Ф1(+6) – Ф2(-5,5) – Ф2(-6,5)", BetType.F1plus6, BetType.F2minus55, BetType.F2minus65));
        add(new Formula(48, "Ф2(+6) – Ф1(-5,5) – Ф1(-6,5)", BetType.F2plus6, BetType.F1minus55, BetType.F1minus65));
        add(new Formula(49, "Ф1(+1) – П2 – Г2(-1)", BetType.F1plus1, BetType.P2, BetType.G2minus1));
        add(new Formula(50, "Ф2(+1) – П1 – Г1(-1)", BetType.F2plus1, BetType.P1, BetType.G1minus1));
        add(new Formula(51, "Ф1(+2) – Ф2(-1,5) – Г2(-2)", BetType.F1plus2, BetType.F2minus15, BetType.G2minus2));
        add(new Formula(52, "Ф2(+2) – Ф1(-1,5) – Г1(-2)", BetType.F2plus2, BetType.F1minus15, BetType.G1minus2));
        add(new Formula(53, "Ф1(+3) – Ф2(-2,5) – Г2(-3)", BetType.F1plus3, BetType.F2minus25, BetType.G2minus3));
        add(new Formula(54, "Ф2(+3) – Ф1(-2,5) – Г1(-3)", BetType.F2plus3, BetType.F1minus25, BetType.G1minus3));
        add(new Formula(55, "Ф1(+4) – Ф2(-3,5) – Г2(-4)", BetType.F1plus4, BetType.F2minus35, BetType.G2minus4));
        add(new Formula(56, "Ф2(+4) – Ф1(-3,5) – Г1(-4)", BetType.F2plus4, BetType.F1minus35, BetType.G1minus4));
        add(new Formula(57, "Ф1(+2) – Г2(-1) – Г2(-2)", BetType.F1plus2, BetType.G2minus1, BetType.G2minus2));
        add(new Formula(58, "Ф2(+2) – Г1(-1) – Г1(-2)", BetType.F2plus2, BetType.G1minus1, BetType.G1minus2));
        add(new Formula(59, "Ф1(+3) – Г2(-2) – Г2(-3)", BetType.F1plus3, BetType.G2minus2, BetType.G2minus3));
        add(new Formula(60, "Ф2(+3) – Г1(-2) – Г1(-3)", BetType.F2plus3, BetType.G1minus2, BetType.G1minus3));
        add(new Formula(61, "Ф1(+4) – Г2(-3) – Г2(-4)", BetType.F1plus4, BetType.G2minus3, BetType.G2minus4));
        add(new Formula(62, "Ф2(+4) – Г1(-3) – Г1(-4)", BetType.F2plus4, BetType.G1minus3, BetType.G1minus4));
        add(new Formula(63, "Ф1(-1) – Ф2(+1,5) – 2х", BetType.F1minus1, BetType.F2plus15, BetType.P2X));
        add(new Formula(64, "Ф2(-1) – Ф1(+1,5) – 1х", BetType.F2minus1, BetType.F1plus15, BetType.P1X));
        add(new Formula(65, "Ф1(-2) – Ф2(+2,5) – Ф2(+1,5)", BetType.F1minus2, BetType.F2plus25, BetType.F2plus15));
        add(new Formula(66, "Ф2(-2) – Ф1(+2,5) – Ф1(+1,5)", BetType.F2minus2, BetType.F1plus25, BetType.F1plus15));
        add(new Formula(67, "Ф1(-3) – Ф2(+3,5) – Ф2(+2,5)", BetType.F1minus3, BetType.F2plus35, BetType.F2plus25));
        add(new Formula(68, "Ф2(-3) – Ф1(+3,5) – Ф1(+2,5)", BetType.F2minus3, BetType.F1plus35, BetType.F1plus25));
        add(new Formula(69, "Ф1(-4) – Ф2(+4,5) – Ф2(+3,5)", BetType.F1minus4, BetType.F2plus45, BetType.F2plus35));
        add(new Formula(70, "Ф2(-4) – Ф1(+4,5) – Ф1(+3,5)", BetType.F2minus4, BetType.F1plus45, BetType.F1plus35));
        add(new Formula(71, "Ф1(-5) – Ф2(+5,5) – Ф2(+4,5)", BetType.F1minus5, BetType.F2plus55, BetType.F2plus45));
        add(new Formula(72, "Ф2(-5) – Ф1(+5,5) – Ф1(+4,5)", BetType.F2minus5, BetType.F1plus55, BetType.F1plus45));
        add(new Formula(73, "Ф1(-6) – Ф2(+6,5) – Ф2(+5,5)", BetType.F1minus6, BetType.F2plus65, BetType.F2plus55));
        add(new Formula(74, "Ф2(-6) – Ф1(+6,5) – Ф1(+5,5)", BetType.F2minus6, BetType.F1plus65, BetType.F1plus55));
        add(new Formula(75, "Ф1(-1) – Г2(+2) – 2х", BetType.F1minus1, BetType.G2plus2, BetType.P2X));
        add(new Formula(76, "Ф2(-1) – Г1(+2) – 1х", BetType.F2minus1, BetType.G1plus2, BetType.P1X));
        add(new Formula(77, "Ф1(-2) – Г2(+3) – Ф2(+1,5)", BetType.F1minus2, BetType.G2plus3, BetType.F2plus15));
        add(new Formula(78, "Ф2(-2) – Г1(+3) – Ф1(+1,5)", BetType.F2minus2, BetType.G1plus3, BetType.F1plus15));
        add(new Formula(79, "Ф1(-3) – Г2(+4) – Ф2(+2,5)", BetType.F1minus3, BetType.G2plus4, BetType.F2plus25));
        add(new Formula(80, "Ф2(-3) – Г1(+4) – Ф1(+2,5)", BetType.F2minus3, BetType.G1plus4, BetType.F1plus25));
        add(new Formula(81, "Ф1(-1) – Г2(+2) – Г2(+1)", BetType.F1minus1, BetType.G2plus2, BetType.G2plus1));
        add(new Formula(82, "Ф2(-1) – Г1(+2) – Г1(+1)", BetType.F2minus1, BetType.G1plus2, BetType.G1plus1));
        add(new Formula(83, "Ф1(-2) – Г2(+3) – Г2(+2)", BetType.F1minus2, BetType.G2plus3, BetType.G2plus2));
        add(new Formula(84, "Ф2(-2) – Г1(+3) – Г1(+2)", BetType.F2minus2, BetType.G1plus3, BetType.G1plus2));
        add(new Formula(85, "Ф1(-3) – Г2(+4) – Г2(+3)", BetType.F1minus3, BetType.G2plus4, BetType.G2plus3));
        add(new Formula(86, "Ф2(-3) – Г1(+4) – Г1(+3)", BetType.F2minus3, BetType.G1plus4, BetType.G1plus3));
        add(new Formula(87, "ТБ(1) – ТМ(1,5) – ТМ(0,5)", BetType.T1M, BetType.T15L, BetType.T05L));
        add(new Formula(88, "ТБ(2) – ТМ(2,5) – ТМ(1,5)", BetType.T2M, BetType.T25L, BetType.T15L));
        add(new Formula(89, "ТБ(3) – ТМ(3,5) – ТМ(2,5)", BetType.T3M, BetType.T35L, BetType.T25L));
        add(new Formula(90, "ТБ(4) – ТМ(4,5) – ТМ(3,5)", BetType.T4M, BetType.T45L, BetType.T35L));
        add(new Formula(91, "ТБ(5) – ТМ(5,5) – ТМ(4,5)", BetType.T5M, BetType.T55L, BetType.T45L));
        add(new Formula(92, "ТБ(6) – ТМ(6,5) – ТМ(5,5)", BetType.T6M, BetType.T65L, BetType.T55L));
        add(new Formula(93, "Т1Б(1) – Т1М(1,5) – Т1М(0,5)", BetType.T1_1M, BetType.T1_15L, BetType.T1_05L));
        add(new Formula(94, "Т1Б(2) – Т1М(2,5) – Т1М(1,5)", BetType.T1_2M, BetType.T1_25L, BetType.T1_15L));
        add(new Formula(95, "Т1Б(3) – Т1М(3,5) – Т1М(2,5)", BetType.T1_3M, BetType.T1_35L, BetType.T1_25L));
        add(new Formula(96, "Т1Б(4) – Т1М(4,5) – Т1М(3,5)", BetType.T1_4M, BetType.T1_45L, BetType.T1_35L));
        add(new Formula(97, "Т1Б(5) – Т1М(5,5) – Т1М(4,5)", BetType.T1_5M, BetType.T1_55L, BetType.T1_45L));
        add(new Formula(98, "Т1Б(6) – Т1М(6,5) – Т1М(5,5)", BetType.T1_6M, BetType.T1_65L, BetType.T1_55L));
        add(new Formula(99, "Т2Б(1) – Т2М(1,5) – Т2М(0,5)", BetType.T2_1M, BetType.T2_15L, BetType.T2_05L));
        add(new Formula(100, "Т2Б(2) – Т2М(2,5) – Т2М(1,5)", BetType.T2_2M, BetType.T2_25L, BetType.T2_15L));
        add(new Formula(101, "Т2Б(3) – Т2М(3,5) – Т2М(2,5)", BetType.T2_3M, BetType.T2_35L, BetType.T2_25L));
        add(new Formula(102, "Т2Б(4) – Т2М(4,5) – Т2М(3,5)", BetType.T2_4M, BetType.T2_45L, BetType.T2_35L));
        add(new Formula(103, "Т2Б(5) – Т2М(5,5) – Т2М(4,5)", BetType.T2_5M, BetType.T2_55L, BetType.T2_45L));
        add(new Formula(104, "Т2Б(6) – Т2М(6,5) – Т2М(5,5)", BetType.T2_6M, BetType.T2_65L, BetType.T2_55L));
        add(new Formula(105, "ТМ(1) – ТБ(0,5) – ТБ(1,5)", BetType.T1L, BetType.T05M, BetType.T15M));
        add(new Formula(106, "ТМ(2) – ТБ(1,5) – ТБ(2,5)", BetType.T2L, BetType.T15M, BetType.T25M));
        add(new Formula(107, "ТМ(3) – ТБ(2,5) – ТБ(3,5)", BetType.T3L, BetType.T25M, BetType.T35M));
        add(new Formula(108, "ТМ(4) – ТБ(3,5) – ТБ(4,5)", BetType.T4L, BetType.T35M, BetType.T45M));
        add(new Formula(109, "ТМ(5) – ТБ(4,5) – ТБ(5,5)", BetType.T5L, BetType.T45M, BetType.T55M));
        add(new Formula(110, "ТМ(6) – ТБ(5,5) – ТБ(6,5)", BetType.T6L, BetType.T55M, BetType.T65M));
        add(new Formula(111, "Т1М(1) – Т1Б(0,5) – Т1Б(1,5)", BetType.T1_1L, BetType.T1_05M, BetType.T1_15M));
        add(new Formula(112, "Т1М(2) – Т1Б(1,5) – Т1Б(2,5)", BetType.T1_2L, BetType.T1_15M, BetType.T1_25M));
        add(new Formula(113, "Т1М(3) – Т1Б(2,5) – Т1Б(3,5)", BetType.T1_3L, BetType.T1_25M, BetType.T1_35M));
        add(new Formula(114, "Т1М(4) – Т1Б(3,5) – Т1Б(4,5)", BetType.T1_4L, BetType.T1_35M, BetType.T1_45M));
        add(new Formula(115, "Т1М(5) – Т1Б(4,5) – Т1Б(5,5)", BetType.T1_5L, BetType.T1_45M, BetType.T1_55M));
        add(new Formula(116, "Т1М(6) – Т1Б(5,5) – Т1Б(6,5)", BetType.T1_6L, BetType.T1_55M, BetType.T1_65M));
        add(new Formula(117, "Т2М(1) – Т2Б(0,5) – Т2Б(1,5)", BetType.T2_1L, BetType.T2_05M, BetType.T2_15M));
        add(new Formula(118, "Т2М(2) – Т2Б(1,5) – Т2Б(2,5)", BetType.T2_2L, BetType.T2_15M, BetType.T2_25M));
        add(new Formula(119, "Т2М(3) – Т2Б(2,5) – Т2Б(3,5)", BetType.T2_3L, BetType.T2_25M, BetType.T2_35M));
        add(new Formula(120, "Т2М(4) – Т2Б(3,5) – Т2Б(4,5)", BetType.T2_4L, BetType.T2_35M, BetType.T2_45M));
        add(new Formula(121, "Т2М(5) – Т2Б(4,5) – Т2Б(5,5)", BetType.T2_5L, BetType.T2_45M, BetType.T2_55M));
        add(new Formula(122, "Т2М(6) – Т2Б(5,5) – Т2Б(6,5)", BetType.T2_6L, BetType.T2_55M, BetType.T2_65M));
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
                sum.profit = C2*K2 + C1;
            } else if (sum.bet == f.var3) {
                sum.sum = C3;
                sum.profit = C3*K3 + K2*C2;
            }
            betsResult.add(new EventSum(sum));
        }
        return betsResult;
    }

    protected BranchResult calculateUniform(List<EventSum> bets, Formula f, Double totalSum) {
        BranchResult branch = new BranchResult();
        branch.isProfit = true;
        branch.totalSum = totalSum;
        //Х1 = 1 / К1 + 1 / (К1 * К3) + (К1 – 1) / (К1 * К2)
        KKK k = getK(bets, f);
        Double K1 = k.K1;
        Double K2 = k.K2;
        Double K3 = k.K3;

        Double X1 = 1.0/K1 + 1.0/(K1*K3) + ((K1 - 1.0)/(K1*K2));
        Double C1 = totalSum/(X1*K1);
        Double C2 = (totalSum*(K1 - 1))/(X1*K1*K2);
        Double C3 = totalSum/(X1*K3*K1);
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
            return K1 - 1 - (1.0/K3);
        } else if (type == ProfitType.UNIFORM_1_2) {
            return (K1 - 1) / K2;
        } else if (type == ProfitType.UNIFORM_1_3) {
            return K1 / (K2 + K3*(K2 - 1.0));
        } else if (type == ProfitType.UNIQUE_1) {
            return 1.0 / (K3*(K2 - 1));
        } else if (type == ProfitType.UNIQUE_2) {
            return (K1*K3 - K1 - K3)/(K3 - K2);
        } else if (type == ProfitType.UNIQUE_3) {
            return (K1 - 1)/K2;
        } else {
            return 1.0; //TODO
        }
    }

    private Double getUniformSeparateX3(Double K1, Double K2, Double K3, Double X2, ProfitType type) {
        if (type == ProfitType.UNIFORM_2_3) {
            return 1.0/K3;
        } else if (type == ProfitType.UNIFORM_1_2) {
            return (1 + (1 - K2)*X2)/(K3 - 1);
        } else if (type == ProfitType.UNIFORM_1_3) {
            return (K2 - 1) * X2;
        } else if (type == ProfitType.UNIQUE_1) {
            return 1.0 / K3;
        } else if (type == ProfitType.UNIQUE_2) {
            return K1 - 1 - X2;
        } else if (type == ProfitType.UNIQUE_3) {
            return (K2 - 1)*(K1 - 1)/K2;
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
