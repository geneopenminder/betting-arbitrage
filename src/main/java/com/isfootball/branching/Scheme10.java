package com.isfootball.branching;

/**
 * Created by Evgeniy Pshenitsin on 11.02.2015.
 */

import com.isfootball.parser.BetType;

import java.util.ArrayList;
import java.util.List;

public class Scheme10 extends BaseSchemeNonUniform {

    //full
    static List<Formula> formulas = new ArrayList<Formula>() {{
        add(new Formula(547, "Ф1(-0,25) – 2х – Ф2(0)", BetType.F1minus025, BetType.P2X, BetType.F20));
        add(new Formula(548, "Ф2(-0,25) – 1х – Ф1(0)", BetType.F2minus025, BetType.P1X, BetType.F10));
        add(new Formula(549, "Ф1(-0,25) –Ф2(+0,5) – Ф2(0)", BetType.F1minus025, BetType.F2plus05, BetType.F20));
        add(new Formula(550, "Ф2(-0,25) – Ф1(+0,5) – Ф1(0)", BetType.F2minus025, BetType.F1plus05, BetType.F10));
        add(new Formula(551, "Ф1(-1,25) – Ф2(+1,5) – Ф2(+1)", BetType.F1minus125, BetType.F2plus15, BetType.F2plus1));
        add(new Formula(552, "Ф2(-1,25) – Ф1(+1,5) – Ф1(+1)", BetType.F2minus125, BetType.F1plus15, BetType.F1plus1));
        add(new Formula(553, "Ф1(-2,25) – Ф2(+2,5) – Ф2(+2)", BetType.F1minus225, BetType.F2plus25, BetType.F2plus2));
        add(new Formula(554, "Ф2(-2,25) – Ф1(+2,5) – Ф1(+2)", BetType.F2minus225, BetType.F1plus25, BetType.F1plus2));
        add(new Formula(555, "Ф1(-3,25) – Ф2(+3,5) – Ф2(+3)", BetType.F1minus325, BetType.F2plus35, BetType.F2plus3));
        add(new Formula(556, "Ф2(-3,25) – Ф1(+3,5) – Ф1(+3)", BetType.F2minus325, BetType.F1plus35, BetType.F1plus3));
        add(new Formula(557, "Ф1(-4,25) – Ф2(+4,5) – Ф2(+4)", BetType.F1minus425, BetType.F2plus45, BetType.F2plus4));
        add(new Formula(558, "Ф2(-4,25) – Ф1(+4,5) – Ф1(+4)", BetType.F2minus425, BetType.F1plus45, BetType.F1plus4));
        add(new Formula(559, "Ф1(-5,25) – Ф2(+5,5) – Ф2(+5)", BetType.F1minus525, BetType.F2plus55, BetType.F2plus5));
        add(new Formula(560, "Ф2(-5,25) – Ф1(+5,5) – Ф1(+5)", BetType.F2minus525, BetType.F1plus55, BetType.F1plus5));
        add(new Formula(561, "Ф1(-6,25) – Ф2(+6,5) – Ф2(+6)", BetType.F1minus625, BetType.F2plus65, BetType.F2plus6));
        add(new Formula(562, "Ф2(-6,25) – Ф1(+6,5) – Ф1(+6)", BetType.F2minus625, BetType.F1plus65, BetType.F1plus6));
        add(new Formula(563, "Ф1(+0,75) – П2 – Ф2(-1)", BetType.F1plus075, BetType.P2, BetType.F2minus1));
        add(new Formula(564, "Ф2(+0,75) – П1 – Ф1(-1)", BetType.F2plus075, BetType.P1, BetType.F1minus1));
        add(new Formula(565, "Ф1(+0,75) –Ф2(-0,5) – Ф2(-1)", BetType.F1plus075, BetType.F2minus05, BetType.F2minus1));
        add(new Formula(566, "Ф2(+0,75) – Ф1(-0,5) – Ф1(-1)", BetType.F2plus075, BetType.F1minus05, BetType.F1minus1));
        add(new Formula(567, "Ф1(+1,75) –Ф2(-1,5) – Ф2(-2)", BetType.F1plus175, BetType.F2minus15, BetType.F2minus2));
        add(new Formula(568, "Ф2(+1,75) – Ф1(-1,5) – Ф1(-2)", BetType.F2plus175, BetType.F1minus15, BetType.F1minus2));
        add(new Formula(569, "Ф1(+2,75) –Ф2(-2,5) – Ф2(-3)", BetType.F1plus275, BetType.F2minus25, BetType.F2minus3));
        add(new Formula(570, "Ф2(+2,75) – Ф1(-2,5) – Ф1(-3)", BetType.F2plus275, BetType.F1minus25, BetType.F1minus3));
        add(new Formula(571, "Ф1(+3,75) –Ф2(-3,5) – Ф2(-4)", BetType.F1plus375, BetType.F2minus35, BetType.F2minus4));
        add(new Formula(572, "Ф2(+3,75) – Ф1(-3,5) – Ф1(-4)", BetType.F2plus375, BetType.F1minus35, BetType.F1minus4));
        add(new Formula(573, "Ф1(+4,75) –Ф2(-4,5) – Ф2(-5)", BetType.F1plus475, BetType.F2minus45, BetType.F2minus5));
        add(new Formula(574, "Ф2(+4,75) – Ф1(-4,5) – Ф1(-5)", BetType.F2plus475, BetType.F1minus45, BetType.F1minus5));
        add(new Formula(575, "Ф1(+5,75) –Ф2(-5,5) – Ф2(-6)", BetType.F1plus575, BetType.F2minus55, BetType.F2minus6));
        add(new Formula(576, "Ф2(+5,75) – Ф1(-5,5) – Ф1(-6)", BetType.F2plus575, BetType.F1minus55, BetType.F1minus6));
        add(new Formula(577, "Ф1(-0,25) – Г2(+1) – Ф2(0)", BetType.F1minus025, BetType.G2plus1, BetType.F20));
        add(new Formula(578, "Ф2(-0,25) – Г1(+1) – Ф1(0)", BetType.F2minus025, BetType.G1plus1, BetType.F10));
        add(new Formula(579, "Ф1(-1,25) – Г2(+2) – Ф2(+1)", BetType.F1minus125, BetType.G2plus2, BetType.F2plus1));
        add(new Formula(580, "Ф2(-1,25) – Г1(+2) – Ф1(+1)", BetType.F2minus125, BetType.G1plus2, BetType.F1plus1));
        add(new Formula(581, "Ф1(-2,25) – Г2(+3) – Ф2(+2)", BetType.F1minus225, BetType.G2plus3, BetType.F2plus2));
        add(new Formula(582, "Ф2(-2,25) – Г1(+3) – Ф1(+2)", BetType.F2minus225, BetType.G1plus3, BetType.F1plus2));
        add(new Formula(583, "Ф1(-3,25) – Г2(+4) – Ф2(+3)", BetType.F1minus325, BetType.G2plus4, BetType.F2plus3));
        add(new Formula(584, "Ф2(-3,25) – Г1(+4) – Ф1(+3)", BetType.F2minus325, BetType.G1plus4, BetType.F1plus3));
        add(new Formula(585, "Ф1(+1,75) – Г2(-1) – Ф2(-2)", BetType.F1plus175, BetType.G2minus1, BetType.F2minus2));
        add(new Formula(586, "Ф2(+1,75) – Г1(-1) – Ф1(-2)", BetType.F2plus175, BetType.G1minus1, BetType.F1minus2));
        add(new Formula(587, "Ф1(+2,75) – Г2(-2) – Ф2(-3)", BetType.F1plus275, BetType.G2minus2, BetType.F2minus3));
        add(new Formula(588, "Ф2(+2,75) – Г1(-2) – Ф1(-3)", BetType.F2plus275, BetType.G1minus2, BetType.F1minus3));
        add(new Formula(589, "Ф1(+3,75) – Г2(-3) – Ф2(-4)", BetType.F1plus375, BetType.G2minus3, BetType.F2minus4));
        add(new Formula(590, "Ф2(+3,75) – Г1(-3) – Ф1(-4)", BetType.F2plus375, BetType.G1minus3, BetType.F1minus4));
        add(new Formula(591, "Ф1(+4,75) – Г2(-4) – Ф2(-5)", BetType.F1plus475, BetType.G2minus4, BetType.F2minus5));
        add(new Formula(592, "Ф2(+4,75) – Г1(-4) – Ф1(-5)", BetType.F2plus475, BetType.G1minus4, BetType.F1minus5));
        add(new Formula(593, "ТМ(0,75) – ТБ(0,5) – ТБ(1)", BetType.T075L, BetType.T05M, BetType.T1M));
        add(new Formula(594, "ТМ(1,75) – ТБ(1,5) – ТБ(2)", BetType.T175L, BetType.T15M, BetType.T2M));
        add(new Formula(595, "ТМ(2,75) – ТБ(2,5) – ТБ(3)", BetType.T275L, BetType.T25M, BetType.T3M));
        add(new Formula(596, "ТМ(3,75) – ТБ(3,5) – ТБ(4)", BetType.T375L, BetType.T35M, BetType.T4M));
        add(new Formula(597, "ТМ(4,75) – ТБ(4,5) – ТБ(5)", BetType.T475L, BetType.T45M, BetType.T5M));
        add(new Formula(598, "ТМ(5,75) – ТБ(5,5) – ТБ(6)", BetType.T575L, BetType.T55M, BetType.T6M));
        add(new Formula(599, "ТМ(6,75) – ТБ(6,5) – ТБ(7)", BetType.T675L, BetType.T65M, BetType.T7M));
        add(new Formula(600, "Т1М(0,75) – Т1Б(0,5) – Т1Б(1)", BetType.T1_075L, BetType.T1_05M, BetType.T1_1M));
        add(new Formula(601, "Т1М(1,75) – Т1Б(1,5) – Т1Б(2)", BetType.T1_175L, BetType.T1_15M, BetType.T1_2M));
        add(new Formula(602, "Т1М(2,75) – Т1Б(2,5) – Т1Б(3)", BetType.T1_275L, BetType.T1_25M, BetType.T1_3M));
        add(new Formula(603, "Т1М(3,75) – Т1Б(3,5) – Т1Б(4)", BetType.T1_375L, BetType.T1_35M, BetType.T1_4M));
        add(new Formula(604, "Т1М(4,75) – Т1Б(4,5) – Т1Б(5)", BetType.T1_475L, BetType.T1_45M, BetType.T1_5M));
        add(new Formula(605, "Т1М(5,75) – Т1Б(5,5) – Т1Б(6)", BetType.T1_575L, BetType.T1_55M, BetType.T1_6M));
        add(new Formula(606, "Т1М(6,75) – Т1Б(6,5) – Т1Б(7)", BetType.T1_675L, BetType.T1_65M, BetType.T1_7M));
        add(new Formula(607, "Т2М(0,75) – Т2Б(0,5) – Т2Б(1)", BetType.T2_075L, BetType.T2_05M, BetType.T2_1M));
        add(new Formula(608, "Т2М(1,75) – Т2Б(1,5) – Т2Б(2)", BetType.T2_175L, BetType.T2_15M, BetType.T2_2M));
        add(new Formula(609, "Т2М(2,75) – Т2Б(2,5) – Т2Б(3)", BetType.T2_275L, BetType.T2_25M, BetType.T2_3M));
        add(new Formula(610, "Т2М(3,75) – Т2Б(3,5) – Т2Б(4)", BetType.T2_375L, BetType.T2_35M, BetType.T2_4M));
        add(new Formula(611, "Т2М(4,75) – Т2Б(4,5) – Т2Б(5)", BetType.T2_475L, BetType.T2_45M, BetType.T2_5M));
        add(new Formula(612, "Т2М(5,75) – Т2Б(5,5) – Т2Б(6)", BetType.T2_575L, BetType.T2_55M, BetType.T2_6M));
        add(new Formula(613, "Т2М(6,75) – Т2Б(6,5) – Т2Б(7)", BetType.T2_675L, BetType.T2_65M, BetType.T2_7M));
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
                sum.profit = C2*K2 + C3 + 0.5*C1;
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
        //Х1 = 1 / К1 + 1 / (2 * К1 * (К3 – 1)) + 1 / К2 – 1 / (2 * К2 * К1) – 1 / (2 * (К3 – 1) * К2 * К1)
        //Х2 = (К1 – 1 / 2 – 1 / (2*(К3 – 1))) / К2

        KKK k = getK(bets, f);
        Double K1 = k.K1;
        Double K2 = k.K2;
        Double K3 = k.K3;

        Double X1 = 1 / K1 + 1 / (2 * K1 * (K3 - 1)) + 1 / K2 - 1 / (2 * K2 * K1) - 1 / (2 * (K3 - 1) * K2 * K1);
        Double X2 = (K1 - 0.5 - 1 / (2*(K3 - 1))) / K2;
        Double C1 = totalSum / (X1 * K1);
        Double C2 =  (totalSum * X2) / (X1 * K1);
        Double C3 = totalSum / (X1 * K1 * 2 * (K3 - 1));
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

        Double X2 = 0.0;
        Double X3 = 0.0;
        Double K1 = k.K1;
        Double K2 = k.K2;
        Double K3 = k.K3;
        if (type == ProfitType.UNIFORM_2_3) {
            //revers
            X3 = 1 / (2 * (K3 - 1));
            X2 = (K1 - 1 - X3);
        } else if (type == ProfitType.UNIFORM_1_2) {
            X2 = (1 + (1 - K3) * (K1 - 0.5)) / (2 * K2 - 1 - K2 * K3);
            X3 = (K1 - 0.5 - K2 * X2);
        } else if (type == ProfitType.UNIFORM_1_3) {
            X2 =  1 / (2 * (K2 - 1));
            X3 = (K1 - X2 * K2) / K3;
        } else if (type == ProfitType.UNIQUE_1) {
            X2 =  1 / (2 * (K2 - 1));
            X3 = 1 / (2 * (K3 - 1));
        } else if (type == ProfitType.UNIQUE_2) {
            X2 = (K1 + K3 - K1 * K3) / (K2 - K3);
            X3 = K1 - 1 - X2;
        } else if (type == ProfitType.UNIQUE_3) {
            X2 = 1 / (2 * (K2 - 1));
            X3 = K1 - 1 - X2;
        }

        Double C1 = getUniformSeparateC1(totalSum, X2, X3);
        Double C2 = getUniformSeparateC2(totalSum, X2, X3);
        Double C3 = getUniformSeparateC3(totalSum, X2, X3);
        branch.branches = fillProfit(bets, f, C1, C2, C3, k.K1, k.K2 ,k.K3);
        return branch;
    }

}
