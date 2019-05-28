package com.isfootball.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;

/**
 * Created by Evgeniy Pshenitsin on 11.09.2015.
 */
public class DBConnection {

    private static final Logger logger = LogManager.getLogger("server");

    public static void createPool() {
        try {
            Class.forName("org.postgresql.Driver");

            ComboPooledDataSource cpds = new ComboPooledDataSource();
            cpds.setDriverClass("org.postgresql.Driver");
            //cpds.setJdbcUrl( "jdbc:postgresql://localhost/bets" );
            cpds.setJdbcUrl( "jdbc:postgresql://217.147.90.216/bets" );
            cpds.setUser("postgres");
            cpds.setPassword("betarbs");
            cpds.setMinPoolSize(8);
            cpds.setAcquireIncrement(4);
            cpds.setMaxPoolSize(8);

            /*PGPoolingDataSource source = new PGPoolingDataSource();
            source.setDataSourceName("A Data Source");
            source.setServerName("localhost");
            source.setDatabaseName("bets");
            source.setUser("postgres");
            source.setPassword("postgres");
            source.setMaxConnections(10);*/

            //JdbcTemplate jdbcTemplateObject = new JdbcTemplate(source);

            BetDao.initialize(cpds);

            //String SQL = "insert into site_updates (site, events_number, update_time) values ('TIPICO', 200, 300)";

            //jdbcTemplateObject.update(SQL);

        } catch (Exception e) {
            logger.fatal("DBConnection create pool", e);
        }

    }

}
