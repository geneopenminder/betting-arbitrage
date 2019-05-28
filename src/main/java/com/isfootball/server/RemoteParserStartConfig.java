package com.isfootball.server;

import com.isfootball.parser.RemoteParser2;

/**
 * Created by Evgeniy Pshenitsin on 12.07.2015.
 */
public class RemoteParserStartConfig {

    public RemoteParser2 parser;

    public int interval;

    public int startDelay;

    public RemoteParserStartConfig(RemoteParser2 parser, int startDelay, int interval) {
        this.parser = parser;
        this.interval = interval;
        this.startDelay = startDelay;
    }

}
