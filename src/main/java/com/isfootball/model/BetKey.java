package com.isfootball.model;

import com.isfootball.parser.BetSite;

/**
 * Created by Evgeniy Pshenitsin on 04.02.2015.
 */
public class BetKey {

    public BetSite site;

    public Double kReal;

    public Double kFromSite;

    public String link;

    public BetKey() {

    }

    public BetKey(BetSite site, Double kReal, Double kFromSite, String link) {
        this.site = site;
        this.kReal = kReal;
        this.link = link;
        this.kFromSite = kFromSite;
    }

    public BetKey(BetSite site, Double kReal, String link) {
        this.site = site;
        this.kReal = kReal;
        this.link = link;
        this.kFromSite = kReal;
    }

}
