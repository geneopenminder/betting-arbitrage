package com.isfootball.utils;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by Evgeniy Pshenitsin on 07.01.2015.
 */
public class HttpClientInitializer {

    private static final Logger logger = LogManager.getLogger("Server");

    private static MultiThreadedHttpConnectionManager cm;

    public static void setupConnection() {
        try {
            cm = new MultiThreadedHttpConnectionManager();
            cm.setMaxConnectionsPerHost(50);
            cm.setMaxTotalConnections(200);
        } catch (Exception e) {
            logger.error( "createConnection ex - ", e);
        }
    }

    public static MultiThreadedHttpConnectionManager getConnectionManager() {
        return cm;
    }

}
