package com.isfootball.branching;

/**
 * Created by Evgeniy Pshenitsin on 11.02.2015.
 */

import com.isfootball.parser.BetType;

import java.util.ArrayList;
import java.util.List;

public class Scheme12 extends BaseSchemeNonUniform {

    //full
    static List<Formula> formulas = new ArrayList<Formula>() {{
        add(new Formula(636, "Ф1(-0,25) – 2х – Ф2(-0,25)", BetType.F1minus025, BetType.P2X, BetType.F2minus025));
        add(new Formula(637, "Ф2(-0,25) – 1х – Ф1(-0,25)", BetType.F2minus025, BetType.P1X, BetType.F1minus025));
        add(new Formula(638, "Ф1(-0,25) – Ф2(+0,5) – Ф2(-0,25)", BetType.F1minus025, BetType.F2plus05, BetType.F2minus025));
        add(new Formula(639, "Ф2(-0,25) – Ф1(+0,5) – Ф1(-0,25)", BetType.F2minus025, BetType.F1plus05, BetType.F1minus025));
        add(new Formula(640, "Ф1(-1,25) – Ф2(+1,5) – Ф2(+0,75)", BetType.F1minus125, BetType.F2plus15, BetType.F2plus075));
        add(new Formula(641, "Ф2(-1,25) – Ф1(+1,5) – Ф1(+0,75)", BetType.F2minus125, BetType.F1plus15, BetType.F1plus075));
        add(new Formula(642, "Ф1(-2,25) – Ф2(+2,5) – Ф2(+1,75)", BetType.F1minus225, BetType.F2plus25, BetType.F2plus175));
        add(new Formula(643, "Ф2(-2,25) – Ф1(+2,5) – Ф1(+1,75)", BetType.F2minus225, BetType.F1plus25, BetType.F1plus175));
        add(new Formula(644, "Ф1(-3,25) – Ф2(+3,5) – Ф2(+2,75)", BetType.F1minus325, BetType.F2plus35, BetType.F2plus275));
        add(new Formula(645, "Ф2(-3,25) – Ф1(+3,5) – Ф1(+2,75)", BetType.F2minus325, BetType.F1plus35, BetType.F1plus275));
        add(new Formula(646, "Ф1(-4,25) – Ф2(+4,5) – Ф2(+3,75)", BetType.F1minus425, BetType.F2plus45, BetType.F2plus375));
        add(new Formula(647, "Ф2(-4,25) – Ф1(+4,5) – Ф1(+3,75)", BetType.F2minus425, BetType.F1plus45, BetType.F1plus375));
        add(new Formula(648, "Ф1(-5,25) – Ф2(+5,5) – Ф2(+4,75)", BetType.F1minus525, BetType.F2plus55, BetType.F2plus475));
        add(new Formula(649, "Ф2(-5,25) – Ф1(+5,5) – Ф1(+4,75)", BetType.F2minus525, BetType.F1plus55, BetType.F1plus475));
        add(new Formula(650, "Ф1(-6,25) – Ф2(+6,5) – Ф2(+5,75)", BetType.F1minus625, BetType.F2plus65, BetType.F2plus575));
        add(new Formula(651, "Ф2(-6,25) – Ф1(+6,5) – Ф1(+5,75)", BetType.F2minus625, BetType.F1plus65, BetType.F1plus575));
        add(new Formula(652, "Ф1(+0,75) – П2 – Ф2(-1,25)", BetType.F1plus075, BetType.P2, BetType.F2minus125));
        add(new Formula(653, "Ф2(+0,75) – П1 – Ф1(-1,25)", BetType.F2plus075, BetType.P1, BetType.F1minus125));
        add(new Formula(654, "Ф1(+0,75) – Ф2(-0,5) – Ф2(-1,25)", BetType.F1plus075, BetType.F2minus05, BetType.F2minus125));
        add(new Formula(655, "Ф2(+0,75) – Ф1(-0,5) – Ф1(-1,25)", BetType.F2plus075, BetType.F1minus05, BetType.F1minus125));
        add(new Formula(656, "Ф1(+1,75) – Ф2(-1,5) – Ф2(-2,25)", BetType.F1plus175, BetType.F2minus15, BetType.F2minus225));
        add(new Formula(657, "Ф2(+1,75) – Ф1(-1,5) – Ф1(-2,25)", BetType.F2plus175, BetType.F1minus15, BetType.F1minus225));
        add(new Formula(658, "Ф1(+2,75) – Ф2(-2,5) – Ф2(-3,25)", BetType.F1plus275, BetType.F2minus25, BetType.F2minus325));
        add(new Formula(659, "Ф2(+2,75) – Ф1(-2,5) – Ф1(-3,25)", BetType.F2plus275, BetType.F1minus25, BetType.F1minus325));
        add(new Formula(660, "Ф1(+3,75) – Ф2(-3,5) – Ф2(-4,25)", BetType.F1plus375, BetType.F2minus35, BetType.F2minus425));
        add(new Formula(661, "Ф2(+3,75) – Ф1(-3,5) – Ф1(-4,25)", BetType.F2plus375, BetType.F1minus35, BetType.F1minus425));
        add(new Formula(662, "Ф1(+4,75) – Ф2(-4,5) – Ф2(-5,25)", BetType.F1plus475, BetType.F2minus45, BetType.F2minus525));
        add(new Formula(663, "Ф2(+4,75) – Ф1(-4,5) – Ф1(-5,25)", BetType.F2plus475, BetType.F1minus45, BetType.F1minus525));
        add(new Formula(664, "Ф1(+5,75) – Ф2(-5,5) – Ф2(-6,25)", BetType.F1plus575, BetType.F2minus55, BetType.F2minus625));
        add(new Formula(665, "Ф2(+5,75) – Ф1(-5,5) – Ф1(-6,25)", BetType.F2plus575, BetType.F1minus55, BetType.F1minus625));
        add(new Formula(666, "Ф1(-0,25) – Г2(+1) – Ф2(-0,25)", BetType.F1minus025, BetType.G2plus1, BetType.F2minus025));
        add(new Formula(667, "Ф2(-0,25) – Г1(+1) – Ф1(-0,25)", BetType.F2minus025, BetType.G1plus1, BetType.F1minus025));
        add(new Formula(668, "Ф1(-1,25) – Г2(+2) – Ф2(+0,75)", BetType.F1minus125, BetType.G2plus2, BetType.F2plus075));
        add(new Formula(669, "Ф2(-1,25) – Г1(+2) – Ф1(+0,75)", BetType.F2minus125, BetType.G1plus2, BetType.F1plus075));
        add(new Formula(670, "Ф1(-2,25) – Г2(+3) – Ф2(+1,75)", BetType.F1minus225, BetType.G2plus3, BetType.F2plus175));
        add(new Formula(671, "Ф2(-2,25) – Г1(+3) – Ф1(+1,75)", BetType.F2minus225, BetType.G1plus3, BetType.F1plus175));
        add(new Formula(672, "Ф1(-3,25) – Г2(+4) – Ф2(+2,75)", BetType.F1minus325, BetType.G2plus4, BetType.F2plus275));
        add(new Formula(673, "Ф2(-3,25) – Г1(+4) – Ф1(+2,75)", BetType.F2minus325, BetType.G1plus4, BetType.F1plus275));
        add(new Formula(674, "Ф1(+1,75) – Г2(-1) – Ф2(-2,25)", BetType.F1plus175, BetType.G2minus1, BetType.F2minus225));
        add(new Formula(675, "Ф2(+1,75) – Г1(-1) – Ф1(-2,25)", BetType.F2plus175, BetType.G1minus1, BetType.F1minus225));
        add(new Formula(676, "Ф1(+2,75) – Г2(-2) – Ф2(-3,25)", BetType.F1plus275, BetType.G2minus2, BetType.F2minus325));
        add(new Formula(677, "Ф2(+2,75) – Г1(-2) – Ф1(-3,25)", BetType.F2plus275, BetType.G1minus2, BetType.F1minus325));
        add(new Formula(678, "Ф1(+3,75) – Г2(-3) – Ф2(-4,25)", BetType.F1plus375, BetType.G2minus3, BetType.F2minus425));
        add(new Formula(679, "Ф2(+3,75) – Г1(-3) – Ф1(-4,25)", BetType.F2plus375, BetType.G1minus3, BetType.F1minus425));
        add(new Formula(680, "Ф1(+4,75) – Г2(-4) – Ф2(-5,25)", BetType.F1plus475, BetType.G2minus4, BetType.F2minus525));
        add(new Formula(681, "Ф2(+4,75) – Г1(-4) – Ф1(-5,25)", BetType.F2plus475, BetType.G1minus4, BetType.F1minus525));
        add(new Formula(682, "ТБ(1,25) – ТМ(1,5) – ТМ(0,75)", BetType.T125M, BetType.T15L, BetType.T075L));
        add(new Formula(683, "ТБ(2,25) – ТМ(2,5) – ТМ(1,75)", BetType.T225M, BetType.T25L, BetType.T175L));
        add(new Formula(684, "ТБ(3,25) – ТМ(3,5) – ТМ(2,75)", BetType.T325M, BetType.T35L, BetType.T275L));
        add(new Formula(685, "ТБ(4,25) – ТМ(4,5) – ТМ(3,75)", BetType.T425M, BetType.T45L, BetType.T375L));
        add(new Formula(686, "ТБ(5,25) – ТМ(5,5) – ТМ(4,75)", BetType.T525M, BetType.T55L, BetType.T475L));
        add(new Formula(687, "ТБ(6,25) – ТМ(6,5) – ТМ(5,75)", BetType.T625M, BetType.T65L, BetType.T575L));
        add(new Formula(688, "Т1Б(1,25) – Т1М(1,5) – Т1М(0,75)", BetType.T1_125M, BetType.T1_15L, BetType.T1_075L));
        add(new Formula(689, "Т1Б(2,25) – Т1М(2,5) – Т1М(1,75)", BetType.T1_225M, BetType.T1_25L, BetType.T1_175L));
        add(new Formula(690, "Т1Б(3,25) – Т1М(3,5) – Т1М(2,75)", BetType.T1_325M, BetType.T1_35L, BetType.T1_275L));
        add(new Formula(691, "Т1Б(4,25) – Т1М(4,5) – Т1М(3,75)", BetType.T1_425M, BetType.T1_45L, BetType.T1_375L));
        add(new Formula(692, "Т1Б(5,25) – Т1М(5,5) – Т1М(4,75)", BetType.T1_525M, BetType.T1_55L, BetType.T1_475L));
        add(new Formula(693, "Т1Б(6,25) – Т1М(6,5) – Т1М(5,75)", BetType.T1_625M, BetType.T1_65L, BetType.T1_575L));
        add(new Formula(694, "Т2Б(1,25) – Т2М(1,5) – Т2М(0,75)", BetType.T2_125M, BetType.T2_15L, BetType.T2_075L));
        add(new Formula(695, "Т2Б(2,25) – Т2М(2,5) – Т2М(1,75)", BetType.T2_225M, BetType.T2_25L, BetType.T2_175L));
        add(new Formula(696, "Т2Б(3,25) – Т2М(3,5) – Т2М(2,75)", BetType.T2_325M, BetType.T2_35L, BetType.T2_275L));
        add(new Formula(697, "Т2Б(4,25) – Т2М(4,5) – Т2М(3,75)", BetType.T2_425M, BetType.T2_45L, BetType.T2_375L));
        add(new Formula(698, "Т2Б(5,25) – Т2М(5,5) – Т2М(4,75)", BetType.T2_525M, BetType.T2_55L, BetType.T2_475L));
        add(new Formula(699, "Т2Б(6,25) – Т2М(6,5) – Т2М(5,75)", BetType.T2_625M, BetType.T2_65L, BetType.T2_575L));
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
                sum.profit = C2*K2 + 0.5*C3 + 0.5*C1;
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
        //Х1 = 1 / К1 + 1 / К2 + 1 / ((2 * К3 – 1) * К1) – 1 / (2 * К2 * К1) – 1 / (2 * (2 * К3 – 1) * К2 * К1)
        //Х2 =  (К1 – 1 / 2 – 1 / (2 * (2 * К3 – 1))) / К2

        KKK k = getK(bets, f);
        Double K1 = k.K1;
        Double K2 = k.K2;
        Double K3 = k.K3;

        Double X1 = 1 / K1 + 1 / K2 + 1 / ((2 * K3 - 1) * K1) - 1 / (2 * K2 * K1) - 1 / (2 * (2 * K3 - 1) * K2 * K1);
        Double X2 = (K1 - 0.5 - 1 / (2 * (2 * K3 - 1))) / K2;
        Double C1 = totalSum / (X1 * K1);
        Double C2 = (totalSum * X2) / (X1 * K1);
        Double C3 = totalSum / (X1 * K1*(2*K3 - 1));
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
        Double X4 = 0.0;
        Double K1 = k.K1;
        Double K2 = k.K2;
        Double K3 = k.K3;
        if (type == ProfitType.UNIFORM_2_3) {
            //revers
            X3 = 1 / (2 * (K3 - 0.5));
            X2 = K1 - 1 - X3;
        } else if (type == ProfitType.UNIFORM_1_2) {
            X4 = 1 / (2 * (K3 - 1));
            X2 = (K1 - 0.5 - X4) / (K2 + X4 * (1 - K2));
            X3 = 2 * (1 + X2 * (1 - K2)) * X4 ;
        } else if (type == ProfitType.UNIFORM_1_3) {
            X2 =  (K1 + K3) / (K2 + 2 * K3 * (K2 - 1));
            X3 = 2 * (K2 - 1) * X2 - 1;
        } else if (type == ProfitType.UNIQUE_1) {
            X2 =  K3 / (2 * K3 * K2 - 2 * K3 - K2 + 1);
            X3 = 2 * (K2 - 1) * X2 - 1;
        } else if (type == ProfitType.UNIQUE_2) {
            X2 = (K1 + K3 - K1 * K3) / (K2 - K3);
            X3 = K1 - 1 - X2;
        } else if (type == ProfitType.UNIQUE_3) {
            X2 = K1 / (2 * K2 - 1);
            X3 = 2 * (K2 - 1) * X2 - 1;
        }

        Double C1 = getUniformSeparateC1(totalSum, X2, X3);
        Double C2 = getUniformSeparateC2(totalSum, X2, X3);
        Double C3 = getUniformSeparateC3(totalSum, X2, X3);
        branch.branches = fillProfit(bets, f, C1, C2, C3, k.K1, k.K2 ,k.K3);
        return branch;
    }

}
