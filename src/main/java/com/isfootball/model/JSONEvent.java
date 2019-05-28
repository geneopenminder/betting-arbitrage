package com.isfootball.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Evgeniy Pshenitsin on 17.03.2015.
 */
public class JSONEvent {

    public String team1;

    public String team2;

    public Date date;

    public List<JSONProfit> profits = new ArrayList<JSONProfit>();

    public JSONEvent() {

    }

    public JSONEvent(JSONEvent event) {
        this.team1 = event.team1;
        this.team2 = event.team2;
        this.date = event.date;
    }

    public JSONEvent(FinalEvent event, String lang) {
        this.team1 = event.team1.getByLang(lang);
        this.team2 = event.team2.getByLang(lang);
        this.date = event.date;
        this.profits = event.profits;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EventKey)) {
            return false;
        }

        EventKey e = (EventKey) o;
        if (date.getDay() != e.date.getDay() ||
                date.getMonth() != e.date.getMonth() ||
                date.getYear() != e.date.getYear()) {
            return false;
        }
        if (team1.equals(team1) && team2.equals(this.team2)) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode(){
        return this.team1.hashCode();
        //return new HashCodeBuilder().append(this.team2.ordinal() + this.team1.ordinal()).hashCode();
        //return date.getDay() + date.getMonth()*50 + date.getYear()*1000;
        //return new HashCodeBuilder().append(this.team1).hashCode();
    }

}
