package com.isfootball.branching;

import com.isfootball.parser.BetSite;
import com.isfootball.parser.BetType;

import java.util.Date;

/**
 * Created by Evgeniy Pshenitsin on 26.09.2015.
 */
public class BetLine {

    public Date added;

    public Double var1;

    public Double var2;

    public Double var3;

    public BetSite site1;

    public BetSite site2;

    public BetSite site3;

    public BetLine(Date added, Double var1, Double var2, Double var3,
                   BetSite site1, BetSite site2, BetSite site3) {
        this.added = added;
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
        this.site1 = site1;
        this.site2 = site2;
        this.site3 = site3;
    }

}
