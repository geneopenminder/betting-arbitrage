package com.isfootball.branching;

/**
 * Created by Evgeniy Pshenitsin on 11.02.2015.
 */

import com.isfootball.parser.BetType;

import java.util.ArrayList;
import java.util.List;

public class Scheme14 extends BaseSchemeNonUniform {

    //full
    static List<Formula> formulas = new ArrayList<Formula>() {{
        add(new Formula(780, "Ф1(0) – 2х – Ф2(-0,25)", BetType.F10, BetType.P2X, BetType.F2minus025));
        add(new Formula(781, "Ф2(0) - 1х - Ф1(-0,25)", BetType.F20, BetType.P1X, BetType.F1minus025));
        add(new Formula(782, "Ф1(0) - Ф2(+0,5) - Ф2(-0,25)", BetType.F10, BetType.F2plus05, BetType.F2minus025));
        add(new Formula(783, "Ф2(0) - Ф1(+0,5) - Ф1(-0,25)", BetType.F20, BetType.F1plus05, BetType.F1minus025));
        add(new Formula(784, "Ф1(-1) - Ф2(+1,5) - Ф2(+0,75)", BetType.F1minus1, BetType.F2plus15, BetType.F2plus075));
        add(new Formula(785, "Ф2(-1) - Ф1(+1,5) - Ф1(+0,75)", BetType.F2minus1, BetType.F1plus15, BetType.F1plus075));
        add(new Formula(786, "Ф1(-2) - Ф2(+2,5) - Ф2(+1,75)", BetType.F1minus2, BetType.F2plus25, BetType.F2plus175));
        add(new Formula(787, "Ф2(-2) - Ф1(+2,5) - Ф1(+1,75)", BetType.F2minus2, BetType.F1plus25, BetType.F1plus175));
        add(new Formula(788, "Ф1(-3) - Ф2(+3,5) - Ф2(+2,75)", BetType.F1minus3, BetType.F2plus35, BetType.F2plus275));
        add(new Formula(789, "Ф2(-3) - Ф1(+3,5) - Ф1(+2,75)", BetType.F2minus3, BetType.F1plus35, BetType.F1plus275));
        add(new Formula(790, "Ф1(-4) - Ф2(+4,5) - Ф2(+3,75)", BetType.F1minus4, BetType.F2plus45, BetType.F2plus375));
        add(new Formula(791, "Ф2(-4) - Ф1(+4,5) - Ф1(+3,75)", BetType.F2minus4, BetType.F1plus45, BetType.F1plus375));
        add(new Formula(792, "Ф1(-5) – Ф2(+5,5) – Ф2(+4,75)", BetType.F1minus5, BetType.F2plus55, BetType.F2plus475));
        add(new Formula(793, "Ф2(-5) – Ф1(+5,5) – Ф1(+4,75)", BetType.F2minus5, BetType.F1plus55, BetType.F1plus475));
        add(new Formula(794, "Ф1(-6) – Ф2(+6,5) – Ф2(+5,75)", BetType.F1minus6, BetType.F2plus65, BetType.F2plus575));
        add(new Formula(795, "Ф2(-6) – Ф1(+6,5) – Ф1(+5,75)", BetType.F2minus6, BetType.F1plus65, BetType.F1plus575));
        add(new Formula(796, "Ф1(+1) - П2 - Ф2(-1,25)", BetType.F1plus1, BetType.P2, BetType.F2minus125));
        add(new Formula(797, "Ф2(+1) - П1 - Ф1(-1,25)", BetType.F2plus1, BetType.P1, BetType.F1minus125));
        add(new Formula(798, "Ф1(+1) - Ф2(-0,5) - Ф2(-1,25)", BetType.F1plus1, BetType.F2minus05, BetType.F2minus125));
        add(new Formula(799, "Ф2(+1) - Ф1(-0,5) - Ф1(-1,25)", BetType.F2plus1, BetType.F1minus05, BetType.F1minus125));
        add(new Formula(800, "Ф1(+2) - Ф2(-1,5) - Ф2(-2,25)", BetType.F1plus2, BetType.F2minus15, BetType.F2minus225));
        add(new Formula(801, "Ф2(+2) - Ф1(-1,5) - Ф1(-2,25)", BetType.F2plus2, BetType.F1minus15, BetType.F1minus225));
        add(new Formula(802, "Ф1(+3) - Ф2(-2,5) - Ф2(-3,25)", BetType.F1plus3, BetType.F2minus25, BetType.F2minus325));
        add(new Formula(803, "Ф2(+3) - Ф1(-2,5) - Ф1(-3,25)", BetType.F2plus3, BetType.F1minus25, BetType.F1minus325));
        add(new Formula(804, "Ф1(+4) - Ф2(-3,5) - Ф2(-4,25)", BetType.F1plus4, BetType.F2minus35, BetType.F2minus425));
        add(new Formula(805, "Ф2(+4) - Ф1(-3,5) - Ф1(-4,25)", BetType.F2plus4, BetType.F1minus35, BetType.F1minus425));
        add(new Formula(806, "Ф1(+5) - Ф2(-4,5) - Ф2(-5,25)", BetType.F1plus5, BetType.F2minus45, BetType.F2minus525));
        add(new Formula(807, "Ф2(+5) - Ф1(-4,5) - Ф1(-5,25)", BetType.F2plus5, BetType.F1minus45, BetType.F1minus525));
        add(new Formula(808, "Ф1(+6) - Ф2(-5,5) - Ф2(-6,25)", BetType.F1plus6, BetType.F2minus55, BetType.F2minus625));
        add(new Formula(809, "Ф2(+6) - Ф1(-5,5) - Ф1(-6,25)", BetType.F2plus6, BetType.F1minus55, BetType.F1minus625));
        add(new Formula(810, "Ф1(0) - Г2(+1) - Ф2(-0,25)", BetType.F10, BetType.G2plus1, BetType.F2minus025));
        add(new Formula(811, "Ф2(0) - Г1(+1) - Ф1(-0,25)", BetType.F20, BetType.G1plus1, BetType.F1minus025));
        add(new Formula(812, "Ф1(-1) - Г2(+2) - Ф2(+0,75)", BetType.F1minus1, BetType.G2plus2, BetType.F2plus075));
        add(new Formula(813, "Ф2(-1) - Г1(+2) - Ф1(+0,75)", BetType.F2minus1, BetType.G1plus2, BetType.F1plus075));
        add(new Formula(814, "Ф1(-2) - Г2(+3) - Ф2(+1,75)", BetType.F1minus2, BetType.G2plus3, BetType.F2plus175));
        add(new Formula(815, "Ф2(-2) - Г1(+3) - Ф1(+1,75)", BetType.F2minus2, BetType.G1plus3, BetType.F1plus175));
        add(new Formula(816, "Ф1(-3) - Г2(+4) - Ф2(+2,75)", BetType.F1minus3, BetType.G2plus4, BetType.F2plus275));
        add(new Formula(817, "Ф2(-3) - Г1(+4) - Ф1(+2,75)", BetType.F2minus3, BetType.G1plus4, BetType.F1plus275));
        add(new Formula(818, "Ф1(+2) - Г2(-1) - Ф2(-2,25)", BetType.F1plus2, BetType.G2minus1, BetType.F2minus225));
        add(new Formula(819, "Ф2(+2) - Г1(-1) - Ф1(-2,25)", BetType.F2plus2, BetType.G1minus1, BetType.F1minus225));
        add(new Formula(820, "Ф1(+3) - Г2(-2) - Ф2(-3,25)", BetType.F1plus3, BetType.G2minus2, BetType.F2minus325));
        add(new Formula(821, "Ф2(+3) - Г1(-2) - Ф1(-3,25)", BetType.F2plus3, BetType.G1minus2, BetType.F1minus325));
        add(new Formula(822, "Ф1(+4) - Г2(-3) - Ф2(-4,25)", BetType.F1plus4, BetType.G2minus3, BetType.F2minus425));
        add(new Formula(823, "Ф2(+4) - Г1(-3) - Ф1(-4,25)", BetType.F2plus4, BetType.G1minus3, BetType.F1minus425));
        add(new Formula(824, "Ф1(+5) - Г2(-4) - Ф2(-5,25)", BetType.F1plus5, BetType.G2minus4, BetType.F2minus525));
        add(new Formula(825, "Ф2(+5) - Г1(-4) - Ф1(-5,25)", BetType.F2plus5, BetType.G1minus4, BetType.F1minus525));
        add(new Formula(826, "ТМ(1) - ТБ(0,5) - ТБ(1,25)", BetType.T1L, BetType.T05M, BetType.T125M));
        add(new Formula(827, "ТМ(2) - ТБ(1,5) - ТБ(2,25)", BetType.T2L, BetType.T15M, BetType.T225M));
        add(new Formula(828, "ТМ(3) - ТБ(2,5) - ТБ(3,25)", BetType.T3L, BetType.T25M, BetType.T325M));
        add(new Formula(829, "ТМ(4) - ТБ(3,5) - ТБ(4,25)", BetType.T4L, BetType.T35M, BetType.T425M));
        add(new Formula(830, "ТМ(5) - ТБ(4,5) - ТБ(5,25)", BetType.T5L, BetType.T45M, BetType.T525M));
        add(new Formula(831, "ТМ(6) - ТБ(5,5) - ТБ(6,25)", BetType.T6L, BetType.T55M, BetType.T625M));
        add(new Formula(832, "ТБ(1) - ТМ(1,5) - ТМ(0,75)", BetType.T1M, BetType.T15L, BetType.T075L));
        add(new Formula(833, "ТБ(2) - ТМ(2,5) - ТМ(1,75)", BetType.T1M, BetType.T25L, BetType.T175L));
        add(new Formula(834, "ТБ(3) - ТМ(3,5) - ТМ(2,75)", BetType.T1M, BetType.T35L, BetType.T275L));
        add(new Formula(835, "ТБ(4) - ТМ(4,5) - ТМ(3,75)", BetType.T1M, BetType.T45L, BetType.T375L));
        add(new Formula(836, "ТБ(5) - ТМ(5,5) - ТМ(4,75)", BetType.T1M, BetType.T55L, BetType.T475L));
        add(new Formula(837, "ТБ(6) - ТМ(6,5) - ТМ(5,75)", BetType.T1M, BetType.T65L, BetType.T575L));
        add(new Formula(838, "Т1М(1) - Т1Б(0,5) - Т1Б(1,25)", BetType.T1_1L, BetType.T1_05M, BetType.T1_125M));
        add(new Formula(839, "Т1М(2) - Т1Б(1,5) - Т1Б(2,25)", BetType.T1_2L, BetType.T1_15M, BetType.T1_225M));
        add(new Formula(840, "Т1М(3) - Т1Б(2,5) - Т1Б(3,25)", BetType.T1_3L, BetType.T1_25M, BetType.T1_325M));
        add(new Formula(841, "Т1М(4) - Т1Б(3,5) - Т1Б(4,25)", BetType.T1_4L, BetType.T1_35M, BetType.T1_425M));
        add(new Formula(842, "Т1М(5) - Т1Б(4,5) - Т1Б(5,25)", BetType.T1_5L, BetType.T1_45M, BetType.T1_525M));
        add(new Formula(843, "Т1М(6) - Т1Б(5,5) - Т1Б(6,25)", BetType.T1_6L, BetType.T1_55M, BetType.T1_625M));
        add(new Formula(844, "Т1Б(1) - Т1М(1,5) - Т1М(0,75)", BetType.T1_1M, BetType.T1_15L, BetType.T1_075L));
        add(new Formula(845, "Т1Б(2) - Т1М(2,5) - Т1М(1,75)", BetType.T1_2M, BetType.T1_25L, BetType.T1_175L));
        add(new Formula(846, "Т1Б(3) - Т1М(3,5) - Т1М(2,75)", BetType.T1_3M, BetType.T1_35L, BetType.T1_275L));
        add(new Formula(847, "Т1Б(4) - Т1М(4,5) - Т1М(3,75)", BetType.T1_4M, BetType.T1_45L, BetType.T1_375L));
        add(new Formula(848, "Т1Б(5) - Т1М(5,5) - Т1М(4,75)", BetType.T1_5M, BetType.T1_55L, BetType.T1_475L));
        add(new Formula(849, "Т1Б(6) - Т1М(6,5) - Т1М(5,75)", BetType.T1_6M, BetType.T1_65L, BetType.T1_575L));
        add(new Formula(850, "Т2М(1) - Т2Б(0,5) - Т2Б(1,25)", BetType.T2_1L, BetType.T2_05M, BetType.T2_125M));
        add(new Formula(851, "Т2М(2) - Т2Б(1,5) - Т2Б(2,25)", BetType.T2_2L, BetType.T2_15M, BetType.T2_225M));
        add(new Formula(852, "Т2М(3) - Т2Б(2,5) - Т2Б(3,25)", BetType.T2_3L, BetType.T2_25M, BetType.T2_325M));
        add(new Formula(853, "Т2М(4) - Т2Б(3,5) - Т2Б(4,25)", BetType.T2_4L, BetType.T2_35M, BetType.T2_425M));
        add(new Formula(854, "Т2М(5) - Т2Б(4,5) - Т2Б(5,25)", BetType.T2_5L, BetType.T2_45M, BetType.T2_525M));
        add(new Formula(855, "Т2М(6) - Т2Б(5,5) - Т2Б(6,25)", BetType.T2_6L, BetType.T2_55M, BetType.T2_625M));
        add(new Formula(856, "Т2Б(1) - Т2М(1,5) - Т2М(0,75)", BetType.T2_1M, BetType.T2_15L, BetType.T2_075L));
        add(new Formula(857, "Т2Б(2) - Т2М(2,5) - Т2М(1,75)", BetType.T2_2M, BetType.T2_25L, BetType.T2_175L));
        add(new Formula(858, "Т2Б(3) - Т2М(3,5) - Т2М(2,75)", BetType.T2_3M, BetType.T2_35L, BetType.T2_275L));
        add(new Formula(859, "Т2Б(4) - Т2М(4,5) - Т2М(3,75)", BetType.T2_4M, BetType.T2_45L, BetType.T2_375L));
        add(new Formula(860, "Т2Б(5) - Т2М(5,5) - Т2М(4,75)", BetType.T2_5M, BetType.T2_55L, BetType.T2_475L));
        add(new Formula(861, "Т2Б(6) - Т2М(6,5) - Т2М(5,75)", BetType.T2_6M, BetType.T2_65L, BetType.T2_575L));
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
                sum.profit = C1 + C2*K2 + 0.5*C3;
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
        //Х1 = 1 / К1 + 1 / К2 – 1 / (К1 * К2) – 1 / (2 * К1 * К2 * (К3 – 0,5)) + 1 / (К1 * (К3 – 0,5))
        //Х2 = (К1 – 1 – 1 / (2 * (К3 – 0,5))) / К2

        KKK k = getK(bets, f);
        Double K1 = k.K1;
        Double K2 = k.K2;
        Double K3 = k.K3;

        Double X1 = 1 / K1 + 1 / K2 - 1 / (K1 * K2) - 1 / (2 * K1 * K2 * (K3 - 0.5)) + 1 / (K1 * (K3 - 0.5));
        Double X2 = (K1 - 1 - 1 / (2 * (K3 - 0.5))) / K2;
        Double C1 = totalSum / (X1 * K1);
        Double C2 = (totalSum * X2) / (X1 * K1);
        Double C3 = totalSum / (X1 * K1 * (K3 - 0.5));
        branch.branches = fillProfit(bets, f, C1, C2, C3, K1, K2 ,K3);
        for (EventSum sum: branch.branches) {
            if (sum.profit <= totalSum) {
                branch.isProfit = false;
            }
        }
        return branch;
    }

    private Double getUniformSeparateX2(Double K1, Double K2, Double K3, Double X4, ProfitType type) {
        if (type == ProfitType.UNIFORM_2_3) {
            return K1 - 1 - 1.0 / (K3 - 0.5);
        } else if (type == ProfitType.UNIFORM_1_2) {
            return 1.0 / (K2 - 1 + (K3 - 1) * X4);
        } else if (type == ProfitType.UNIFORM_1_3) {
            return K1 / (K2 + 2 * K3 * (K2 - 1));
        } else if (type == ProfitType.UNIQUE_1) {
            return 1 / (K2 - 1 + 2 * (K3 - 1) * (K2 - 1));
        } else if (type == ProfitType.UNIQUE_2) {
            return (K1 + K3 - K1 * K3) / (K2 - K3);
        } else if (type == ProfitType.UNIQUE_3) {
            return (K1 - 1) / (1 + 2 * (K2 - 1));
        } else {
            return 1.0; //TODO
        }
    }

    private Double getUniformSeparateX3(Double K1, Double K2, Double K3, Double X2, Double X4, ProfitType type) {
        if (type == ProfitType.UNIFORM_2_3) {
            return 1 / (K3 - 0.5);
        } else if (type == ProfitType.UNIFORM_1_2) {
            return X4 / (K2 - 1 + (K3 - 1) * X4);
        } else if (type == ProfitType.UNIFORM_1_3) {
            return 2 * (K2 - 1) * K1 / (K2 + 2 * K3 * (K2 - 1));
        } else if (type == ProfitType.UNIQUE_1) {
            return 2 * (K2 - 1) / (K2 - 1 + 2 * (K3 - 1) * (K2 - 1)) ;
        } else if (type == ProfitType.UNIQUE_2) {
            return K1 - 1 - X2;
        } else if (type == ProfitType.UNIQUE_3) {
            return 2 * (K2 - 1) * (K1 - 1) / (1 + 2 * (K2 - 1));
        } else {
            return 1.0; //TODO
        }
    }
    private Double getUniformSeparateX4(Double K1, Double K2, Double K3, ProfitType type) {
        if (type == ProfitType.UNIFORM_1_2) {
            return (K2 - (K1 - 1) * (K2 - 1)) / ((K1 - 1) * (K3 - 1) - 0.5);
            //(K2 - (K1 - 1) * (K2 - 1)) / ((K1 - 1) * (K3 - 1) - 0,5)
        } else {
            return 1.0; //TODO
        }
    }

    protected BranchResult calculateUniformSeparate(List<EventSum> bets, Formula f, Double totalSum, ProfitType type) {
        BranchResult branch = new BranchResult();
        branch.isProfit = true;
        branch.totalSum = totalSum;
        KKK k = getK(bets, f);

        Double X4 = getUniformSeparateX4(k.K1, k.K2, k.K3, type);
        Double X2 = getUniformSeparateX2(k.K1, k.K2, k.K3, X4, type);
        Double X3 = getUniformSeparateX3(k.K1, k.K2, k.K3, X2, X4, type);

        Double C1 = getUniformSeparateC1(totalSum, X2, X3);
        Double C2 = getUniformSeparateC2(totalSum, X2, X3);
        Double C3 = getUniformSeparateC3(totalSum, X2, X3);
        branch.branches = fillProfit(bets, f, C1, C2, C3, k.K1, k.K2 ,k.K3);
        return branch;
    }

}
