package com.isfootball.server;

import com.isfootball.parser.BaseParser;

/**
 * Created by Evgeniy Pshenitsin on 12.07.2015.
 */
public class ParserStartConfig {

    public BaseParser parser;

    public int interval;

    public int startDelay;

    public ParserStartConfig(BaseParser parser, int startDelay, int interval) {
        this.parser = parser;
        this.interval = interval;
        this.startDelay = startDelay;
    }

}
