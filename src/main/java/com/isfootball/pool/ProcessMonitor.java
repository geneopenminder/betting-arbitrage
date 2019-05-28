package com.isfootball.pool;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

/**
 * Created by Evgeniy Pshenitsin on 10.09.2015.
 */
public class ProcessMonitor {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger("server");

    public static class ChromedriverProcess {

        public String id;
        public long startTime;

        @Override
        public int hashCode() {
            return Integer.parseInt(id);
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof ChromedriverProcess)) {
                return false;
            }

            ChromedriverProcess e = (ChromedriverProcess) o;
            if (e.id.equals(this.id)) {
                return true;
            }
            return false;
        }

    };

    static public ConcurrentMap<ChromedriverProcess, Long> processes = new ConcurrentHashMap<>();

    static ChromedriverProcess getLastRunningProcess() {
        logger.info("getLastRunningProcess: " + processes.size());

        try {
            Process p1 = Runtime.getRuntime().exec(new String[] { "ps", "-ef" });
            InputStream input = p1.getInputStream();
            Process p2 = Runtime.getRuntime().exec(new String[] { "grep", "chromedriver"});
            OutputStream output = p2.getOutputStream();
            IOUtils.copy(input, output);
            output.close(); // signals grep to finish
            List<String> result = IOUtils.readLines(p2.getInputStream());

            for (String process: result) {
                ChromedriverProcess ps = new ChromedriverProcess();
                //root     27332     2
                ps.id = process.substring(5, 15).trim();
                if (!processes.containsKey(ps)) {
                    logger.info("put process str: " + process);
                    ps.startTime = System.currentTimeMillis();
                    processes.put(ps, 0L);
                    logger.info("put process: " + ps.id);
                    return ps;
                }
            }

            /*String process;
            Process p = Runtime.getRuntime().exec("ps -few | grep chromedriver");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((process = input.readLine()) != null) {
                System.out.println(process); // <-- Print all Process here line
            }
            input.close();*/
        } catch (Exception e) {
            logger.error("getLastRunningProcess - exception", e);
        }
        return null;
    }

    public static void destroyInstance(ChromedriverProcess ps) {
        try {
            logger.info("destroyInstance; " + ps.id + " ; work time - " + (System.currentTimeMillis() - ps.startTime) / (1000 * 60) + " min.");
            Runtime.getRuntime().exec("pkill -TERM -P " + ps.id);
            Runtime.getRuntime().exec("kill -9 " + ps.id);
            processes.remove(ps);
        } catch (Exception e) {
            logger.error("destroyInstance - exception", e);
        }
    }

}
