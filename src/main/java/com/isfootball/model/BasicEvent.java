package com.isfootball.model;

import com.isfootball.parser.BetSite;
import com.isfootball.parser.BetType;
import com.isfootball.parser.Teams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Evgeniy Pshenitsin on 04.01.2015.
 */
public class BasicEvent {

    private static final Logger logger = LogManager.getLogger("server");

    public Teams team1;

    public Teams team2;

    public String id;

    public String date;

    public Date day;

    public Map<BetType, String> bets = new HashMap<BetType, String>();

    public BetSite site;

    public String link;

    public boolean validate() {
        if (day == null) {
            logger.fatal("day invalid" + site == null ? "site null" : site );
            return false;
        }
        if (date == null) {
            logger.fatal("date invalid" + site == null ? "site null" : site );
            return false;
        }
        if (site == null) {
            logger.fatal("date invalidsite null");
            return false;
        }
        if (team1 == null) {
            logger.fatal("team1 invalid" + site == null ? "site null" : site );
            return false;
        }
        if (team2 == null) {
            logger.fatal("team2 invalid" + site == null ? "site null" : site );
            return false;
        }
        if (bets.isEmpty()) {
            logger.fatal("bets empty invalid" + site == null ? "site null" : site );
            return false;
        }
        return true;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BasicEvent)) {
            return false;
        }

        BasicEvent e = (BasicEvent) o;

        if (e.day == null) {
            return false;
        }
        if (day == null) {
            return false;
        }

        if (e.team1 != this.team1 || e.team2 != this.team2) {
            return false;
        }

        DateTime dateTime = new DateTime(day);
        DateTime eDateTime = new DateTime(e.day);

        Interval interval = new Interval(dateTime.minusDays(2).minusHours(23), dateTime.plusDays(2).plusHours(23));

        if (!interval.contains(eDateTime)) {
            return false;
        }
        return true;
    }

    public int hashCode(){
        return this.team1.hashCode();
    }

}
