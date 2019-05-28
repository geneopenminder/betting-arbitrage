package com.isfootball.branching;

import com.isfootball.parser.BetSite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Evgeniy Pshenitsin on 19.02.2015.
 */
public abstract class BaseSchemeNonUniform  extends BaseScheme {

    @Override
    public Map<ProfitType, BranchResult> calculateByScheme(List<EventSum> bets, Formula f, Double totalSum) {
        Map<ProfitType, BranchResult> result = new HashMap<ProfitType, BranchResult>();
        BranchResult uniform = calculateUniform(bets, f, totalSum);
        result.put(ProfitType.UNIFORM, uniform);
        if (!uniform.isProfit) {
            //non profit
            return result;
        } else {
            result.put(ProfitType.UNIFORM_1_2, calculateUniformSeparate(bets, f, totalSum, ProfitType.UNIFORM_1_2));
            result.put(ProfitType.UNIFORM_1_3, calculateUniformSeparate(bets, f, totalSum, ProfitType.UNIFORM_1_3));
            result.put(ProfitType.UNIFORM_2_3, calculateUniformSeparate(bets, f, totalSum, ProfitType.UNIFORM_2_3));
            result.put(ProfitType.UNIQUE_1, calculateUniformSeparate(bets, f, totalSum, ProfitType.UNIQUE_1));
            result.put(ProfitType.UNIQUE_2, calculateUniformSeparate(bets, f, totalSum, ProfitType.UNIQUE_2));
            result.put(ProfitType.UNIQUE_3, calculateUniformSeparate(bets, f, totalSum, ProfitType.UNIQUE_3));
        }
        return result;
    }

    protected abstract BranchResult calculateUniform(List<EventSum> bets, Formula f, Double totalSum);
    protected abstract BranchResult calculateUniformSeparate(List<EventSum> bets, Formula f, Double totalSum, ProfitType type);

    protected Double getUniformSeparateC1(Double totalSum, Double X2, Double X3) {
        return totalSum/(1 + X2 + X3);
    }

    protected Double getUniformSeparateC2(Double totalSum, Double X2, Double X3) {
        return (X2*totalSum)/(1 + X2 + X3);
    }

    protected Double getUniformSeparateC3(Double totalSum, Double X2, Double X3) {
        return (X3*totalSum)/(1 + X2 + X3);
    }

}
