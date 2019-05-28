package com.isfootball.branching;

/**
 * Created by Evgeniy Pshenitsin on 11.02.2015.
 */

import com.isfootball.parser.BetType;

import java.util.ArrayList;
import java.util.List;

public class Scheme13 extends BaseSchemeNonUniform {

    //full
    static List<Formula> formulas = new ArrayList<Formula>() {{
        add(new Formula(700, "Ф1(0) – Ф2(+0,25) – П2", BetType.F10, BetType.F2plus025, BetType.P2));
        add(new Formula(701, "Ф2(0) – Ф1(+0,25) – П1", BetType.F20, BetType.F1plus025, BetType.P1));
        add(new Formula(702, "Ф1(0) – Ф2(+0,25) – Ф2(-0,5)", BetType.F10, BetType.F2plus025, BetType.F2minus05));
        add(new Formula(703, "Ф2(0) – Ф1(+0,25) – Ф1(-0,5)", BetType.F20, BetType.F1plus025, BetType.F1minus05));
        add(new Formula(704, "Ф1(+1) – Ф2(-0,75) – Ф2(-1,5)", BetType.F1plus1, BetType.F2minus075, BetType.F2minus15));
        add(new Formula(705, "Ф2(+1) – Ф1(-0,75) – Ф1(-1,5)", BetType.F2plus1, BetType.F1minus075, BetType.F1minus15));
        add(new Formula(706, "Ф1(+2) – Ф2(-1,75) – Ф2(-2,5)", BetType.F1plus2, BetType.F2minus175, BetType.F2minus25));
        add(new Formula(707, "Ф2(+2) – Ф1(-1,75) – Ф1(-2,5)", BetType.F2plus2, BetType.F1minus175, BetType.F1minus25));
        add(new Formula(708, "Ф1(+3) – Ф2(-2,75) – Ф2(-3,5)", BetType.F1plus3, BetType.F2minus275, BetType.F2minus35));
        add(new Formula(709, "Ф2(+3) – Ф1(-2,75) – Ф1(-3,5)", BetType.F2plus3, BetType.F1minus275, BetType.F1minus35));
        add(new Formula(710, "Ф1(+4) – Ф2(-3,75) – Ф2(-4,5)", BetType.F1plus4, BetType.F2minus375, BetType.F2minus45));
        add(new Formula(711, "Ф2(+4) – Ф1(-3,75) – Ф1(-4,5)", BetType.F2plus4, BetType.F1minus375, BetType.F1minus45));
        add(new Formula(712, "Ф1(+5) – Ф2(-4,75) – Ф2(-5,5)", BetType.F1plus5, BetType.F2minus475, BetType.F2minus55));
        add(new Formula(713, "Ф2(+5) – Ф1(-4,75) – Ф1(-5,5)", BetType.F2plus5, BetType.F1minus475, BetType.F1minus55));
        add(new Formula(714, "Ф1(+6) – Ф2(-5,75) – Ф2(-6,5)", BetType.F1plus6, BetType.F2minus575, BetType.F2minus65));
        add(new Formula(715, "Ф2(+6) – Ф1(-5,75) – Ф1(-6,5)", BetType.F2plus6, BetType.F1minus575, BetType.F1minus65));
        add(new Formula(716, "Ф1(-1) – Ф2(+1,25) – Ф2(+0,5)", BetType.F1minus1, BetType.F2plus125, BetType.F2plus05));
        add(new Formula(717, "Ф2(-1) – Ф1(+1,25) – Ф1(+0,5)", BetType.F2minus1, BetType.F1plus125, BetType.F1plus05));
        add(new Formula(718, "Ф1(-2) – Ф2(+2,25) – Ф2(+1,5)", BetType.F1minus2, BetType.F2plus225, BetType.F2plus15));
        add(new Formula(719, "Ф2(-2) – Ф1(+2,25) – Ф1(+1,5)", BetType.F2minus2, BetType.F1plus225, BetType.F1plus15));
        add(new Formula(720, "Ф1(-3) – Ф2(+3,25) – Ф2(+2,5)", BetType.F1minus3, BetType.F2plus325, BetType.F2plus25));
        add(new Formula(721, "Ф2(-3) – Ф1(+3,25) – Ф1(+2,5)", BetType.F2minus3, BetType.F1plus325, BetType.F1plus25));
        add(new Formula(722, "Ф1(-4) – Ф2(+4,25) – Ф2(+3,5)", BetType.F1minus4, BetType.F2plus425, BetType.F2plus35));
        add(new Formula(723, "Ф2(-4) – Ф1(+4,25) – Ф1(+3,5)", BetType.F2minus4, BetType.F1plus425, BetType.F1plus35));
        add(new Formula(724, "Ф1(-5) – Ф2(+5,25) – Ф2(+4,5)", BetType.F1minus5, BetType.F2plus525, BetType.F2plus45));
        add(new Formula(725, "Ф2(-5) – Ф1(+5,25) – Ф1(+4,5)", BetType.F2minus5, BetType.F1plus525, BetType.F1plus45));
        add(new Formula(726, "Ф1(-6) – Ф2(+6,25) – Ф2(+5,5)", BetType.F1minus6, BetType.F2plus625, BetType.F2plus55));
        add(new Formula(727, "Ф2(-6) – Ф1(+6,25) – Ф1(+5,5)", BetType.F2minus6, BetType.F1plus625, BetType.F1plus55));
        add(new Formula(728, "Ф1(+1) – Ф2(-0,75) – Г2(-1)", BetType.F1plus1, BetType.F2minus075, BetType.G2minus1));
        add(new Formula(729, "Ф2(+1) – Ф1(-0,75) – Г1(-1)", BetType.F2plus1, BetType.F1minus075, BetType.G1minus1));
        add(new Formula(730, "Ф1(+2) – Ф2(-1,75) – Г2(-2)", BetType.F1plus2, BetType.F2minus175, BetType.G2minus2));
        add(new Formula(731, "Ф2(+2) – Ф1(-1,75) – Г1(-2)", BetType.F2plus2, BetType.F1minus175, BetType.G1minus2));
        add(new Formula(732, "Ф1(+3) – Ф2(-2,75) – Г2(-3)", BetType.F1plus3, BetType.F2minus275, BetType.G2minus3));
        add(new Formula(733, "Ф2(+3) – Ф1(-2,75) – Г1(-3)", BetType.F2plus3, BetType.F1minus275, BetType.G1minus3));
        add(new Formula(734, "Ф1(+4) – Ф2(-3,75) – Г2(-4)", BetType.F1plus4, BetType.F2minus375, BetType.G2minus4));
        add(new Formula(735, "Ф2(+4) – Ф1(-3,75) – Г1(-4)", BetType.F2plus4, BetType.F1minus375, BetType.G1minus4));
        add(new Formula(736, "Ф1(-1) – Ф2(+1,25) – Г2(+1)", BetType.F1minus1, BetType.F2plus125, BetType.G2plus1));
        add(new Formula(737, "Ф2(-1) – Ф1(+1,25) – Г1(+1)", BetType.F2minus1, BetType.F1plus125, BetType.G1plus1));
        add(new Formula(738, "Ф1(-2) – Ф2(+2,25) – Г2(+2)", BetType.F1minus2, BetType.F2plus225, BetType.G2plus2));
        add(new Formula(739, "Ф2(-2) – Ф1(+2,25) – Г1(+2)", BetType.F2minus2, BetType.F1plus225, BetType.G1plus2));
        add(new Formula(740, "Ф1(-3) – Ф2(+3,25) – Г2(+3)", BetType.F1minus3, BetType.F2plus325, BetType.G2plus3));
        add(new Formula(741, "Ф2(-3) – Ф1(+3,25) – Г1(+3)", BetType.F2minus3, BetType.F1plus325, BetType.G1plus3));
        add(new Formula(742, "Ф1(-4) – Ф2(+4,25) – Г2(+4)", BetType.F1minus4, BetType.F2plus425, BetType.G2plus4));
        add(new Formula(743, "Ф2(-4) – Ф1(+4,25) – Г1(+4)", BetType.F2minus4, BetType.F1plus425, BetType.G1plus4));
        add(new Formula(744, "ТБ(1) – ТМ(1,25) – ТМ(0,5)", BetType.T1M, BetType.T125L, BetType.T05L));
        add(new Formula(745, "ТБ(2) – ТМ(2,25) – ТМ(1,5)", BetType.T2M, BetType.T225L, BetType.T15L));
        add(new Formula(746, "ТБ(3) – ТМ(3,25) – ТМ(2,5)", BetType.T3M, BetType.T325L, BetType.T25L));
        add(new Formula(747, "ТБ(4) – ТМ(4,25) – ТМ(3,5)", BetType.T4M, BetType.T425L, BetType.T35L));
        add(new Formula(748, "ТБ(5) – ТМ(5,25) – ТМ(4,5)", BetType.T5M, BetType.T525L, BetType.T45L));
        add(new Formula(749, "ТБ(6) – ТМ(6,25) – ТМ(5,5)", BetType.T6M, BetType.T625L, BetType.T55L));
        add(new Formula(750, "Т1Б(1) – Т1М(1,25) – Т1М(0,5)", BetType.T1_1M, BetType.T1_125L, BetType.T1_05L));
        add(new Formula(751, "Т1Б(2) – Т1М(2,25) – Т1М(1,5)", BetType.T1_2M, BetType.T1_225L, BetType.T1_15L));
        add(new Formula(752, "Т1Б(3) – Т1М(3,25) – Т1М(2,5)", BetType.T1_3M, BetType.T1_325L, BetType.T1_25L));
        add(new Formula(753, "Т1Б(4) – Т1М(4,25) – Т1М(3,5)", BetType.T1_4M, BetType.T1_425L, BetType.T1_35L));
        add(new Formula(754, "Т1Б(5) – Т1М(5,25) – Т1М(4,5)", BetType.T1_5M, BetType.T1_525L, BetType.T1_45L));
        add(new Formula(755, "Т1Б(6) – Т1М(6,25) – Т1М(5,5)", BetType.T1_6M, BetType.T1_625L, BetType.T1_55L));
        add(new Formula(756, "Т2Б(1) – Т2М(1,25) – Т2М(0,5)", BetType.T2_1M, BetType.T2_125L, BetType.T2_05L));
        add(new Formula(757, "Т2Б(2) – Т2М(2,25) – Т2М(1,5)", BetType.T2_2M, BetType.T2_225L, BetType.T2_15L));
        add(new Formula(758, "Т2Б(3) – Т2М(3,25) – Т2М(2,5)", BetType.T2_3M, BetType.T2_325L, BetType.T2_25L));
        add(new Formula(759, "Т2Б(4) – Т2М(4,25) – Т2М(3,5)", BetType.T2_4M, BetType.T2_425L, BetType.T2_35L));
        add(new Formula(760, "Т2Б(5) – Т2М(5,25) – Т2М(4,5)", BetType.T2_5M, BetType.T2_525L, BetType.T2_45L));
        add(new Formula(761, "Т2Б(6) – Т2М(6,25) – Т2М(5,5)", BetType.T2_6M, BetType.T2_625L, BetType.T2_55L));
        add(new Formula(762, "ТМ(1) – ТБ(0,75) – ТБ(1,5)", BetType.T1L, BetType.T075M, BetType.T15M));
        add(new Formula(763, "ТМ(2) – ТБ(1,75) – ТБ(2,5)", BetType.T2L, BetType.T175M, BetType.T25M));
        add(new Formula(764, "ТМ(3) – ТБ(2,75) – ТБ(3,5)", BetType.T3L, BetType.T275M, BetType.T35M));
        add(new Formula(765, "ТМ(4) – ТБ(3,75) – ТБ(4,5)", BetType.T4L, BetType.T375M, BetType.T45M));
        add(new Formula(766, "ТМ(5) – ТБ(4,75) – ТБ(5,5)", BetType.T5L, BetType.T475M, BetType.T55M));
        add(new Formula(767, "ТМ(6) – ТБ(5,75) – ТБ(6,5)", BetType.T6L, BetType.T575M, BetType.T65M));
        add(new Formula(768, "Т1М(1) – Т1Б(0,75) – Т1Б(1,5)", BetType.T1_1L, BetType.T1_075M, BetType.T1_15M));
        add(new Formula(769, "Т1М(2) – Т1Б(1,75) – Т1Б(2,5)", BetType.T1_2L, BetType.T1_175M, BetType.T1_25M));
        add(new Formula(770, "Т1М(3) – Т1Б(2,75) – Т1Б(3,5)", BetType.T1_3L, BetType.T1_275M, BetType.T1_35M));
        add(new Formula(771, "Т1М(4) – Т1Б(3,75) – Т1Б(4,5)", BetType.T1_4L, BetType.T1_375M, BetType.T1_45M));
        add(new Formula(772, "Т1М(5) – Т1Б(4,75) – Т1Б(5,5)", BetType.T1_5L, BetType.T1_475M, BetType.T1_55M));
        add(new Formula(773, "Т1М(6) – Т1Б(5,75) – Т1Б(6,5)", BetType.T1_6L, BetType.T1_575M, BetType.T1_65M));
        add(new Formula(774, "Т2М(1) – Т2Б(0,75) – Т2Б(1,5)", BetType.T2_1L, BetType.T2_075M, BetType.T2_15M));
        add(new Formula(775, "Т2М(2) – Т2Б(1,75) – Т2Б(2,5)", BetType.T2_2L, BetType.T2_175M, BetType.T2_25M));
        add(new Formula(776, "Т2М(3) – Т2Б(2,75) – Т2Б(3,5)", BetType.T2_3L, BetType.T2_275M, BetType.T2_35M));
        add(new Formula(777, "Т2М(4) – Т2Б(3,75) – Т2Б(4,5)", BetType.T2_4L, BetType.T2_375M, BetType.T2_45M));
        add(new Formula(778, "Т2М(5) – Т2Б(4,75) – Т2Б(5,5)", BetType.T2_5L, BetType.T2_475M, BetType.T2_55M));
        add(new Formula(779, "Т2М(6) – Т2Б(5,75) – Т2Б(6,5)", BetType.T2_6L, BetType.T2_575M, BetType.T2_65M));
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
                sum.profit = C1 + C2/2 + K2*C2/2;
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
        //Х1 = 1 / К1 + 2 * (К1 – 1) / (К1 * (К2 + 1)) + 1 / К3 – 2 * К2 * (К1 – 1) / (К1 * К3 * (К2 + 1))
        KKK k = getK(bets, f);
        Double K1 = k.K1;
        Double K2 = k.K2;
        Double K3 = k.K3;

        Double X1 = 1 / K1 + 2 * (K1 - 1) / (K1 * (K2 + 1)) + 1 / K3 - 2 * K2 * (K1 - 1) / (K1 * K3 * (K2 + 1));
        Double C1 = totalSum / (X1 * K1);
        Double C2 = totalSum * 2 * (K1 - 1) / (X1 * K1 * (K2 +1));
        Double C3 = totalSum * (K2 + 1 + (K1 - 1) * (1 - K2)) / (X1 * K1 * (K2 + 1) * K3);
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
            return (K3 * (K1 - 1) - 1) / (K3 - (K2 - 1) / 2);
        } else if (type == ProfitType.UNIFORM_1_2) {
            return 2 * (K1 - 1) / (K2 + 1);
        } else if (type == ProfitType.UNIFORM_1_3) {
            return K1 / (K2 + K3 * (K2 - 1) / 2);
        } else if (type == ProfitType.UNIQUE_1) {
            return 1 / (K2 - 1 + (K3 - 1) * (K2 - 1) / 2);
        } else if (type == ProfitType.UNIQUE_2) {
            return (K1 + K3 - K1 * K3) / (K2 - K3);
        } else if (type == ProfitType.UNIQUE_3) {
            return 2 * (K1 - 1) / (K2 + 1);
        } else {
            return 1.0; //TODO
        }
    }

    private Double getUniformSeparateX3(Double K1, Double K2, Double K3, Double X2, ProfitType type) {
        if (type == ProfitType.UNIFORM_2_3) {
            return ((1 - (K1 - 1) * (K2 - 1) / 2)) / (K3 - (K2 - 1) / 2);
        } else if (type == ProfitType.UNIFORM_1_2) {
            return (K2 + 1 - 2 * (K2 - 1) * (K1 - 1)) / ((K3 - 1) * (K2 + 1));
        } else if (type == ProfitType.UNIFORM_1_3) {
            return K1 * (K2 - 1) / (2 * K2 + K3 * (K2 - 1));
        } else if (type == ProfitType.UNIQUE_1) {
            return (K2 - 1) / (2 * K2 - 2 + (K3 - 1) * (K2 - 1));
        } else if (type == ProfitType.UNIQUE_2) {
            return K1 - 1 - X2;
        } else if (type == ProfitType.UNIQUE_3) {
            return (K2 - 1) * (K1 - 1) / (K2 + 1);
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
