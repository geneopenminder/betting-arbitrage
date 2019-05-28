package com.isfootball.branching;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Evgeniy Pshenitsin on 19.02.2015.
 */
public abstract class BaseSchemeUniformOnly  extends BaseScheme {

    @Override
    public Map<ProfitType, BranchResult> calculateByScheme(List<EventSum> bets, Formula f, Double totalSum) {
        Map<ProfitType, BranchResult> result = new HashMap<ProfitType, BranchResult>();
        BranchResult uniform = calculateUniform(bets, f, totalSum);
        result.put(ProfitType.UNIFORM, uniform);
        return result;
    }

    protected abstract BranchResult calculateUniform(List<EventSum> bets, Formula f, Double totalSum);
}
