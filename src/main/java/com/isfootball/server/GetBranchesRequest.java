package com.isfootball.server;

import com.isfootball.parser.BetSite;

import java.util.List;

/**
 * Created by Evgeniy Pshenitsin on 27.06.2015.
 */
public class GetBranchesRequest {

    public boolean useAllSites;

    public static class SiteConfig {

        public BetSite site;

        public Double cms;

    };

    public List<SiteConfig> sites;

}
