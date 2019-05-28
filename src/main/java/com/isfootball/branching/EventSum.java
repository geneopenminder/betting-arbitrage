package com.isfootball.branching;

import com.isfootball.parser.BetSite;
import com.isfootball.parser.BetType;

/**
 * Created by Evgeniy Pshenitsin on 03.02.2015.
 */
public class EventSum {

    public BetSite site;

    public String link;

    public BetType bet;

    public Double kReal;

    public Double kFromSite;

    public Double sum;

    public Double profit;

    public boolean isMiddle;

    public EventSum() {
    }

    public EventSum(BetSite site, BetType bet, Double val) {
        this.site = site;
        this.bet = bet;
        this.kReal = val;
        this.kFromSite = val;
        this.link = site.getName();
        isMiddle = false;
    }

    public EventSum(EventSum e) {
        this.site = e.site;
        this.profit = e.profit;
        this.sum = e.sum;
        this.bet = e.bet;
        this.kReal = e.kReal;
        this.link = e.link;
        this.kFromSite = e.kFromSite;
        isMiddle = false;
    }

};

