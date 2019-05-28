package com.isfootball.parser;

import com.isfootball.branching.*;
import com.isfootball.jdbc.Bet;
import com.isfootball.jdbc.Match;
import com.isfootball.model.BetKey;
import com.isfootball.model.EventKey;
import com.isfootball.model.JSONEvent;
import com.isfootball.model.JSONResult;
import com.isfootball.parser.source.RuUnibetParser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Evgeniy Pshenitsin on 14.03.2015.
 */
public class StaticCache {

    public static String htmlBranches = "NO DATA";

    public static String htmlEvents = "NO DATA";

    public static String branches = "NO DATA";

    public static String tunnelBranches = "NO DATA";

    public static String htmlTable = "NO DATA";

    public static String allLine = "NO DATA";

    //public static List<BasicEvent> events = new ArrayList<BasicEvent>();

    public static volatile ConcurrentMap<BetSite, EventsUpdate> currentEventsUpdate = new ConcurrentHashMap<BetSite, EventsUpdate>();

    public static volatile List<Match> matches = null;

    public static volatile String lastFullBranchesNew = null;

    public static volatile String lastFullBranches = null;

    public static List<BaseScheme> shemes = new ArrayList<BaseScheme>() {{
        add(new Scheme1());
        add(new Scheme2());
        add(new Scheme3());
        add(new Scheme4());
        add(new Scheme5());
        add(new Scheme6());
        add(new Scheme7());
        add(new Scheme8());
        add(new Scheme9());
        add(new Scheme10());
        add(new Scheme11());
        add(new Scheme12());
        add(new Scheme13());
        add(new Scheme14());
        add(new Scheme15());
        add(new Scheme16());
        add(new Scheme17());
        add(new Scheme18());
        add(new Scheme19());
        add(new Scheme20());
        add(new Scheme21());
    }};

    public static List<BaseScheme> tunnels = new ArrayList<BaseScheme>() {{
        add(new SchemeTunnel1());
        add(new SchemeTunnel2());
        add(new SchemeTunnel3());
        add(new SchemeTunnel4());
    }};

    public static List<BaseScheme> allSchemes = new ArrayList<BaseScheme>() {{
        add(new Scheme1());
        add(new Scheme2());
        add(new Scheme3());
        add(new Scheme4());
        add(new Scheme5());
        add(new Scheme6());
        add(new Scheme7());
        add(new Scheme8());
        add(new Scheme9());
        add(new Scheme10());
        add(new Scheme11());
        add(new Scheme12());
        add(new Scheme13());
        add(new Scheme14());
        add(new Scheme15());
        add(new Scheme16());
        add(new Scheme17());
        add(new Scheme18());
        add(new Scheme19());
        add(new Scheme20());
        add(new Scheme21());

        add(new SchemeTunnel1());
        add(new SchemeTunnel2());
        add(new SchemeTunnel3());
        add(new SchemeTunnel4());
    }};
}