package com.isfootball.model;

import com.isfootball.jdbc.Match;
import com.isfootball.parser.BetSite;
import com.isfootball.parser.Teams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Evgeniy Pshenitsin on 04.02.2015.
 */
public class EventKey {

    private static final Logger logger = LogManager.getLogger("branching");

    public Teams team1;

    public Teams team2;

    public Date date;

    public String link;

    public EventKey() {
    }

    public EventKey(BasicEvent e) {
        this.team1 = e.team1;
        this.team2 = e.team2;
        this.date = e.day;
        this.link = e.link;
    }

    public EventKey(Match m) {
        this.team1 = Teams.valueOf(m.getTeam1());
        this.team2 = Teams.valueOf(m.getTeam2());
        this.date = m.getMatchDay();
    }

    public void copyEvent(BasicEvent e) {
        this.team1 = e.team1;
        this.team2 = e.team2;
        this.date = e.day;
        this.link = e.link;
    }

    public JSONEvent getJSON() {
        JSONEvent e = new JSONEvent();
        e.date = date;
        e.team1 = team1.getRussian();
        e.team2 = team2.getRussian();
        return e;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EventKey)) {
            return false;
        }

        EventKey e = (EventKey) o;

        //boolean dateCompareRes = true;
        if (e.date == null) {
            logger.fatal("Null date for event! - " + e.link);
            return false;
        }
        if (date == null) {
            logger.fatal("Null date for event! - " + link);
            return false;
        }

        if (e.team1 != this.team1 || e.team2 != this.team2) {
            return false;
        }

        DateTime dateTime = new DateTime(date);
        DateTime eDateTime = new DateTime(e.date);

        Interval interval = new Interval(dateTime.minusDays(2).minusHours(23), dateTime.plusDays(2).plusHours(23));

        if (!interval.contains(eDateTime)) {
            return false;
        }
        return true;
        /*if (dateTime.getDayOfMonth() != eDateTime.getDayOfMonth() ||
                dateTime.getMonthOfYear() != eDateTime.getMonthOfYear() ||
                dateTime.getYear() != eDateTime.getYear()) {
            dateCompareRes = false;
        }*/
        /*if (date.getDay() != e.date.getDay() ||
                date.getMonth() != e.date.getMonth() ||
                date.getYear() != e.date.getYear()) {
            dateCompareRes = false;
        }*/
        /*if (!dateCompareRes) {
            if ((e.link != null && e.link.contains("sbobet")) ||
                    (this.link != null && this.link.contains("sbobet"))) {
                Date thisD = this.date;
                Date eD = e.date;
                if (this.link.contains("sbobet")) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(this.date);
                    c.add(Calendar.DATE, -1);
                    thisD = c.getTime();
                } else {
                    Calendar c = Calendar.getInstance();
                    c.setTime(e.date);
                    c.add(Calendar.DATE, -1);
                    eD = c.getTime();
                }
                if (thisD.getDay() != eD.getDay() ||
                        thisD.getMonth() != eD.getMonth() ||
                        thisD.getYear() != eD.getYear()) {
                    return false;
                }
            } else {
                return false;
            }
        }*/
    }

    public int hashCode(){
        return this.team1.hashCode();
        //return new HashCodeBuilder().append(this.team2.ordinal() + this.team1.ordinal()).hashCode();
        //return date.getDay() + date.getMonth()*50 + date.getYear()*1000;
        //return new HashCodeBuilder().append(this.team1).hashCode();
    }

}
