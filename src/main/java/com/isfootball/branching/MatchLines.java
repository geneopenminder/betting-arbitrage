package com.isfootball.branching;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Evgeniy Pshenitsin on 26.09.2015.
 */
public class MatchLines {

    public static DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.GERMAN);

    public static class FormulaLine {

        public FormulaLine(Formula f, Map<BaseScheme.ProfitType, BranchResult> eventProfit, Date date) {
            this.f = f;
            this.eventProfit = eventProfit;
            this.date = date;
        }

        public Date date;

        public Formula f;

        public Map<BaseScheme.ProfitType, BranchResult> eventProfit;

        public String getDay() {
            return format.format(date);
        }

        public Double getProfit() {
            return eventProfit.get(BaseScheme.ProfitType.UNIFORM)
                    .getJSONOutcomeProfit(BaseScheme.ProfitType.UNIFORM)
                    .profitRate;
        }
    };

    List<FormulaLine> fLines = new ArrayList<>();

}
