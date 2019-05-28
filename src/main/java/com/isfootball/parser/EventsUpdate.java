package com.isfootball.parser;

import com.isfootball.model.BasicEvent;

import java.util.Date;
import java.util.List;

/**
 * Created by Evgeniy Pshenitsin on 12.07.2015.
 */
public class EventsUpdate {

    public List<BasicEvent> events;

    public Date lastUpdate;

    public EventsUpdate(List<BasicEvent> events, Date lastUpdate) {
        this.events = events;
        this.lastUpdate = lastUpdate;
    }

}
