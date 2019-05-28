package com.isfootball.model;

import com.isfootball.branching.BaseScheme;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Evgeniy Pshenitsin on 17.03.2015.
 */
public class JSONProfit {

    public int formulaId;

    public Double profitRate;

    public long forkId;

    public int forkLifeTime; //seconds

    public Map<BaseScheme.ProfitType, JSONOutcomeProfit> profits = new HashMap<BaseScheme.ProfitType, JSONOutcomeProfit>();

}
