package com.isfootball.processing;

import com.isfootball.parser.BetSite;

import java.util.List;

/**
 * Created by Evgeniy Pshenitsin on 12.10.2015.
 */
public class BettingNotifyRule {

    public List<BetSite> sites;

    public String email;

    public Double limit;

    public BettingNotifyRule(List<BetSite> sites, String email, Double limit) {
        this.sites = sites;
        this.email = email;
        this.limit = limit;
    }

}
