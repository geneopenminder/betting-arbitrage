package com.isfootball.jdbc;

import com.isfootball.branching.BetLine;
import com.isfootball.branching.Formula;
import com.isfootball.jdbc.rep.db.BetsDB;
import com.isfootball.jdbc.rep.db.MatchesDB;
import com.isfootball.jdbc.rep.db.SiteUpdatesDB;
import com.isfootball.model.BasicEvent;
import com.isfootball.model.BetKey;
import com.isfootball.parser.BetSite;
import com.isfootball.parser.BetType;
import com.isfootball.parser.source.ExpektParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Created by Evgeniy Pshenitsin on 14.09.2015.
 */
public class BetDao {

    private static final Logger logger = LogManager.getLogger("parser");

    private DataSource dataSource;

    private static volatile BetDao instance;

    private SimpleJdbcInsert jdbcTemplateSaveBet;
    private SimpleJdbcInsert jdbcTemplateSaveMatch;
    private SimpleJdbcInsert jdbcTemplateSaveSiteUpdate;
    private JdbcTemplate jdbcTemplateObject;
    private DataSourceTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;

    //sql queries

    final static String getBets  = "select b.* from (\n" +
            "select  bet, site, max(updated) as _updated from bets where match_id = ?\n" +
            " group by bet, site\n" +
            ") as x inner join bets as b on b.bet = x.bet and b.site = x.site and b.updated = _updated\n" +
            "where b.match_id = ? and b.updated > (now() - interval '20 minutes')\n";


    final static String getMatches = "select * from matches where closed = false and match_day >= (current_date - interval '1 day')";

    final static String getMatch = "select * from matches where id = ?";

    public static void initialize(DataSource dataSource) {
        BetDao localInstance = instance;
        if (localInstance == null) {
            synchronized (BetDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new BetDao(dataSource);
                }
            }
        }
    }

    private BetDao(DataSource dataSource) {
        this.dataSource = dataSource;
        try {
            jdbcTemplateSaveBet = new SimpleJdbcInsert(dataSource)
                    .withTableName(BetsDB.getTableName())
                    .usingGeneratedKeyColumns(BetsDB.COLUMNS.ID.getColumnName());

            jdbcTemplateSaveMatch = new SimpleJdbcInsert(dataSource)
                    .withTableName(MatchesDB.getTableName())
                    .usingGeneratedKeyColumns(MatchesDB.COLUMNS.ID.getColumnName());

            jdbcTemplateSaveSiteUpdate = new SimpleJdbcInsert(dataSource)
                    .withTableName(SiteUpdatesDB.getTableName())
                    .usingGeneratedKeyColumns(SiteUpdatesDB.COLUMNS.ID.getColumnName());

            jdbcTemplateObject = new JdbcTemplate(dataSource);
            transactionManager = new DataSourceTransactionManager(dataSource);
            transactionTemplate = new TransactionTemplate(transactionManager);

        } catch (Exception e) {
            logger.fatal("BetDao create error", e);
            throw new RuntimeException();
        }
    }

    public static BetDao getInstance() {
        return instance;
    }

    public static void fillBets(Match m) {
        List<Bet> bets = getInstance().jdbcTemplateObject.query(getBets, new Object[]{m.getId(), m.getId()}, BetsDB.ROW_MAPPER);
        for (Bet b: bets) {
            m.getBets().get(BetSite.valueOf(b.getSite())).put(BetType.valueOf(b.getBet()), b);
        }
    }

    public static List<Match> getMatchesWithoutBets() {
        return getInstance().jdbcTemplateObject.query(getMatches, MatchesDB.ROW_MAPPER);
    }

    public static Match getMatch(long id) {
        return getInstance().jdbcTemplateObject.queryForObject(getMatch, new Object[]{id}, MatchesDB.ROW_MAPPER);
    }

    public static Match getMatchWithBets(long id) {
        Match m =  getInstance().jdbcTemplateObject.queryForObject(getMatch, new Object[]{id}, MatchesDB.ROW_MAPPER);
        fillBets(m);
        return m;
    }

    public static List<Match> getMatches() {
        List<Match> matches = getInstance().jdbcTemplateObject.query(getMatches, MatchesDB.ROW_MAPPER);

        matches.parallelStream().forEach((Match m) -> fillBets(m));

        /*ForkJoinPool forkJoinPool = new ForkJoinPool(8);
        for (Match m: matches) {
            forkJoinPool.submit(new Runnable() {
                @Override
                public void run() {
                    List<Bet> bets = getInstance().jdbcTemplateObject.query(getBets, new Object[]{m.getId(), m.getId()}, BetsDB.ROW_MAPPER);
                    for (Bet b: bets) {
                        m.getBets().get(BetSite.valueOf(b.getSite())).put(BetType.valueOf(b.getBet()), b);
                    }
                }
            });
        }

        for (Match m: matches) {
            List<Bet> bets = getInstance().jdbcTemplateObject.query(getBets, new Object[]{m.getId(), m.getId()}, BetsDB.ROW_MAPPER);
            for (Bet b: bets) {
                m.getBets().get(BetSite.valueOf(b.getSite())).put(BetType.valueOf(b.getBet()), b);
            }
        }*/
        return  matches;
    }

    public static void saveBet(Bet bet) {
        Number id = getInstance().jdbcTemplateSaveBet.executeAndReturnKey(BetsDB.ROW_UNMAPPER.mapColumns(bet));
        bet.setId(id.longValue());
    }

    public static void updateBet(Bet bet) {
        getInstance().jdbcTemplateObject.update("update bets set updated = ? where id = ?",
                bet.getUpdated(), bet.getId());
    }

    public static void saveBetsBatch(List<Bet> bets) {
        getInstance().jdbcTemplateObject.batchUpdate("update bets set updated = ? where id = ?",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Bet bet = bets.get(i);
                        ps.setLong(1, bet.getId());
                        ps.setTimestamp(2, new Timestamp(bet.getUpdated().getTime()));
                    }

                    @Override
                    public int getBatchSize() {
                        return bets.size();
                    }
                });
    }

    public static void updateBetsBatch(List<Bet> bets) {
        getInstance().jdbcTemplateObject.batchUpdate("update bets set updated = ? where id = ?",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Bet bet = bets.get(i);
                        ps.setTimestamp(1, new Timestamp(bet.getUpdated().getTime()));
                        ps.setLong(2, bet.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return bets.size();
                    }
                });
    }

    public static void saveMatch(Match match) {
        getInstance().transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                Number id = getInstance().jdbcTemplateSaveMatch.executeAndReturnKey(MatchesDB.ROW_UNMAPPER.mapColumns(match));
                match.setId(id.longValue());
                match.setPersisted(true);
                for (BetSite site : match.getBets().keySet()) {
                    for (BetType betType : match.getBets().get(site).keySet()) {
                        Bet bet = match.getBets().get(site).get(betType);
                        bet.setMatchId(match.getId());
                        Number idBet = getInstance().jdbcTemplateSaveBet.executeAndReturnKey(BetsDB.ROW_UNMAPPER.mapColumns(bet));
                        bet.setId(idBet.longValue());
                    }
                }
                return null;
            }
        });

    }

    public static void saveSiteUpdate(SiteUpdates update) {
        Number id = getInstance().jdbcTemplateSaveSiteUpdate.executeAndReturnKey(SiteUpdatesDB.ROW_UNMAPPER.mapColumns(update));
        update.setId(id.longValue());
    }

    static AtomicLong count = new AtomicLong(0);

    public static Match getMaxBetsForMatch(long matchId, BetSite site) {
        final Match match = new Match();

        StringBuilder sb = new StringBuilder();
        sb.append("select bet, MAX(bet_val) as val from bets where  site = '")
                .append(site.toString())
                .append("' and match_id = ")
                .append(matchId)
                .append(" group by bet");

        List<Map<String, Object>> result = getInstance().jdbcTemplateObject.queryForList(sb.toString());
        result.parallelStream().forEach(obj -> {
            final BetType betType = BetType.valueOf((String) obj.get("bet"));
            final Double val = ((BigDecimal)obj.get("val")).doubleValue(); //((BigDecimal) obj.get(f.var3.toString().toLowerCase())).doubleValue()
            Bet bet = new Bet();
            bet.setBetSite(site);
            bet.setBet(betType.toString());
            bet.setBetVal(val);
            bet.setSite(site.toString());
            match.getBets().get(site).put(betType, bet);
        });

        return  match;
    }

    public static List<BetLine> getAllSitesBetsLine(final Formula f, Long matchId) {
        logger.info("count: " + count.incrementAndGet());
        final long start = System.currentTimeMillis();

        final boolean isTwoBetsF = f.var3 == null ? true : false;
        StringBuilder sb = new StringBuilder();
        sb.append("select bs.date_added,");
        //add bet1
        sb.append("(select b1.bet_val from bets b1 where b1.bet = '");
        sb.append(f.var1.toString());
        sb.append("' and b1.match_id = ");
        sb.append(matchId);
        sb.append(" and bs.date_added between b1.date_added and b1.updated order by b1.bet_val desc limit 1) ");
        sb.append(f.var1.toString());
        sb.append(",");

        sb.append("(select b1.site from bets b1 where b1.bet = '");
        sb.append(f.var1.toString());
        sb.append("' and b1.match_id = ");
        sb.append(matchId);
        sb.append(" and bs.date_added between b1.date_added and b1.updated order by b1.bet_val desc limit 1) ");
        sb.append("site1");
        sb.append(",");

        //add bet2
        sb.append("(select b1.bet_val from bets b1 where b1.bet = '");
        sb.append(f.var2.toString());
        sb.append("' and b1.match_id = ");
        sb.append(matchId);
        sb.append(" and bs.date_added between b1.date_added and b1.updated order by b1.bet_val desc limit 1) ");
        sb.append(f.var2.toString());
        sb.append(",");

        sb.append("(select b1.site from bets b1 where b1.bet = '");
        sb.append(f.var2.toString());
        sb.append("' and b1.match_id = ");
        sb.append(matchId);
        sb.append(" and bs.date_added between b1.date_added and b1.updated order by b1.bet_val desc limit 1) ");
        sb.append("site2");

        if (!isTwoBetsF) {
            sb.append(",");
            //add bet3
            sb.append("(select b1.bet_val from bets b1 where b1.bet = '");
            sb.append(f.var3.toString());
            sb.append("' and b1.match_id = ");
            sb.append(matchId);
            sb.append(" and bs.date_added between b1.date_added and b1.updated order by b1.bet_val desc limit 1) ");
            sb.append(f.var3.toString());

            sb.append(",");
            sb.append("(select b1.site from bets b1 where b1.bet = '");
            sb.append(f.var3.toString());
            sb.append("' and b1.match_id = ");
            sb.append(matchId);
            sb.append(" and bs.date_added between b1.date_added and b1.updated order by b1.bet_val desc limit 1) ");
            sb.append("site3");
        }

        sb.append(" from bets bs where bs.match_id = ");
        sb.append(matchId);
        sb.append(" and bs.bet in (");
        sb.append("'").append(f.var1).append("','").append(f.var2);
        if (!isTwoBetsF) {
            sb.append("','").append(f.var3).append("') ");
        } else {
            sb.append("')");
        }
        sb.append(" group by bs.date_added, ");
        sb.append(f.var1).append(",").append(f.var2);
        sb.append(",").append("site1").append(",").append("site2");
        if (!isTwoBetsF) {
            sb.append(",").append(f.var3).append(",").append("site3");
        }
        sb.append(" order by bs.date_added");

        List<Map<String, Object>> result = getInstance().jdbcTemplateObject.queryForList(sb.toString());
        List<BetLine> ret = new ArrayList<>();
        if (result != null && !result.isEmpty()) {
            ret = result.parallelStream()
                    .map(obj -> {
                        try {
                            return new BetLine((Date) obj.get("date_added"),
                                    ((BigDecimal) obj.get(f.var1.toString().toLowerCase())).doubleValue(),
                                    ((BigDecimal) obj.get(f.var2.toString().toLowerCase())).doubleValue(),
                                    isTwoBetsF ? null : ((BigDecimal) obj.get(f.var3.toString().toLowerCase())).doubleValue(),
                                    BetSite.valueOf((String) obj.get("site1")),
                                    BetSite.valueOf((String) obj.get("site2")),
                                    isTwoBetsF ? null : BetSite.valueOf((String) obj.get("site3")));
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .filter(res -> res != null)
                    .collect(Collectors.toList());

        } else {
            ret = new ArrayList<>();
        }
        logger.info("getAllSitesBetsLine() time: " + (System.currentTimeMillis() - start));
        return ret;
    }

}
