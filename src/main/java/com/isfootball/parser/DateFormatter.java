package com.isfootball.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Evgeniy Pshenitsin on 30.09.2015.
 */
public class DateFormatter {

    private static final Logger logger = LogManager.getLogger("parser");

    static Map<BetSite, DateTimeFormatter> formatters = new HashMap<BetSite, DateTimeFormatter>() {{
        put(BetSite.WINLINEBET, DateTimeFormatter.ofPattern("dd/MMyyyy"));
        put(BetSite.LIGASTAVOK, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        put(BetSite.BET_10, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        put(BetSite.SBOBET, DateTimeFormatter.ofPattern("MMM ddyyyy"));
        put(BetSite.EXPEKT, DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH));
        put(BetSite.BETFAIR, DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.GERMAN));
        put(BetSite.BALTBET, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        put(BetSite.BETCITY, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        put(BetSite.INTERWETTEN, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        put(BetSite.OLIMPIKZ, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        put(BetSite.PLANETWIN365, DateTimeFormatter.ofPattern("dd MM yyyy"));
        put(BetSite.SMARKETS, DateTimeFormatter.ofPattern("yyyy-MM-dd")); //2015-10-21
    }};

    public static Date format(String date, BetSite site) {
        try {
            return Date.from(
                    LocalDate.parse(date, formatters.get(site))
                            .atStartOfDay()
                            .atZone(ZoneId.systemDefault())
                            .toInstant());
        } catch (Exception e) {
            logger.fatal("DateFormatter error: " + date + ":" + site, e);
            return null;
        }
    }

    public static Date format(String date, DateTimeFormatter formatter) {
        try {
            return Date.from(
                    LocalDate.parse(date, formatter)
                            .atStartOfDay()
                            .atZone(ZoneId.systemDefault())
                            .toInstant());
        } catch (Exception e) {
            logger.fatal("DateFormatter error: " + date , e);
            return null;
        }
    }


}
