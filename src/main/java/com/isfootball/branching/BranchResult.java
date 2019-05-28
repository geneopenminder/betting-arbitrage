package com.isfootball.branching;

import com.isfootball.model.JSONOutcomeProfit;
import com.isfootball.model.JSONRate;
import com.isfootball.parser.BetSite;

import java.util.List;

/**
 * Created by Evgeniy Pshenitsin on 03.02.2015.
 */
public class BranchResult {

    public boolean isProfit;

    public Double totalSum;

    public List<EventSum> branches;

    public JSONOutcomeProfit getJSONOutcomeProfit(BaseScheme.ProfitType profitType) {
        JSONOutcomeProfit jP = new JSONOutcomeProfit();
        //if (profitType == BaseScheme.ProfitType.TUNNEL) {
        //    return null;
        //}
        if (profitType == BaseScheme.ProfitType.UNIFORM) {
            jP.profitRate = branches.get(0).profit / 10000.0;
            jP.factor1 = getRate(branches.get(0), profitType);
            jP.factor2 = getRate(branches.get(1), profitType);
            if (branches.size() > 2) {
                jP.factor3 = getRate(branches.get(2), profitType);
            }
        } else {
            EventSum s = branches.get(0);
            if (s != null) {
                jP.profitRate1 = s.profit / 10000.0;
                jP.factor1 = getRate(s, profitType);
            }
            s = branches.get(1);
            if (s != null) {
                jP.profitRate2 = s.profit / 10000.0;
                jP.factor2 = getRate(s, profitType);
            }
            s = branches.get(2);
            if (s != null) {
                jP.profitRate3 = s.profit / 10000.0;
                jP.factor3 = getRate(s, profitType);
            }
        }
        return jP;
    }

    private JSONRate getRate(EventSum sum, BaseScheme.ProfitType profitType) {
        if (sum.isMiddle) {
            return null;
        }
        /*if (sum.site == BetSite.NONE && profitType == BaseScheme.ProfitType.TUNNEL &&
                sum.link.equalsIgnoreCase("TUNNEL_DELETE")) {
            //TODO for tunnel delete
            return null;
        }*/
        JSONRate rate = new JSONRate();
        rate.bet = sum.bet.getRusName();
        rate.site = sum.site;
        rate.val = sum.kFromSite;
        rate.link = sum.link;
        rate.k = sum.sum / 10000.0;
        if (!sum.kReal.equals(sum.kFromSite)) {
            rate.valReal = sum.kReal;
        }
        return rate;
    }

}
