package com.isfootball.jdbc;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.isfootball.model.BasicEvent;
import com.isfootball.model.EventKey;
import com.isfootball.parser.*;
import com.isfootball.processing.BetUpdateConsumer;
import com.isfootball.processing.BetUpdateProducer;
import com.isfootball.server.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Evgeniy Pshenitsin on 15.09.2015.
 */
public class EventsUpdateProcessor implements Runnable {

    private static final Logger logger = LogManager.getLogger("server");

    private final static ArrayBlockingQueue<BasicEvent> eventQueue = new ArrayBlockingQueue<BasicEvent>(100000, true);

    public static volatile Map<EventKey, Match> events = new HashMap<EventKey, Match>();

    final ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("UpdateHandler-%d")
            .setDaemon(true)
            .build();

    final BlockingQueue<Runnable> queue =
            new ArrayBlockingQueue<Runnable>(100000);

    final ExecutorService executorService = new ThreadPoolExecutor(8, 8,
            0L, TimeUnit.MILLISECONDS,
            queue,
            threadFactory);

    class UpdateHandler implements Runnable {
        private final BasicEvent event;
        private final Match exist;

        UpdateHandler(BasicEvent event, Match exist) {
            this.event = event;
            this.exist = exist;
        }

        public void run() {
            logger.info("UpdateHandler: update event " + event.team1 + " - " + event.team2 + "; bets size - " + event.bets.size());
            logger.info("UpdateHandler workers queue size: " + queue.size());
            final BetSite site = event.site;
            List<Bet> betsForBatchUpdate = new ArrayList<>();

            for (BetType betType: event.bets.keySet()) {
                try {
                    Double kVal = Double.parseDouble(event.bets.get(betType));
                    BigDecimal bd = new BigDecimal(kVal);
                    bd = bd.setScale(8, RoundingMode.HALF_UP);

                    Bet bet = new Bet();
                    bet.setSite(site.toString());
                    bet.setBetSite(site);
                    bet.setBetVal(bd.doubleValue());
                    bet.setBet(betType.toString());
                    bet.setMatchId(exist.getId());
                    bet.setUpdated(new Date());

                    Bet betOld = exist.getBets().get(site).putIfAbsent(betType, bet);
                    if (betOld == null) {
                        BetDao.saveBet(bet);

                        BetUpdateConsumer.BetUpdate upd = new BetUpdateConsumer.BetUpdate();
                        upd.bet = betType;
                        upd.matchId = exist.getId();
                        upd.site = site;
                        upd.val = bet.getBetVal();
                        upd.isSame = false;
                        BetUpdateProducer.sendUpdate(upd);
                        logger.info("save new bet: " + betType + "; site - " + site + "; val - " + bet.getBetVal());
                    } else {
                        if (bet.getBetVal().equals(betOld.getBetVal())) {
                            betOld.setUpdated(new Date());
                            BetUpdateConsumer.BetUpdate upd = new BetUpdateConsumer.BetUpdate();
                            upd.bet = betType;
                            upd.matchId = exist.getId();
                            upd.site = site;
                            upd.val = betOld.getBetVal();
                            upd.isSame = true;
                            BetUpdateProducer.sendUpdate(upd);
                            betsForBatchUpdate.add(betOld);
                            //BetDao.updateBet(betOld);
                        } else {
                            BetDao.saveBet(bet);
                            exist.getBets().get(site).put(betType, bet);
                            BetUpdateConsumer.BetUpdate upd = new BetUpdateConsumer.BetUpdate();
                            upd.bet = betType;
                            upd.matchId = exist.getId();
                            upd.site = site;
                            upd.val = bet.getBetVal();
                            upd.isSame = false;
                            BetUpdateProducer.sendUpdate(upd);
                        }
                    }
                } catch (Exception d) {
                    logger.error("UpdateHandler save bet exception", d);
                }
            }
            try {
                long start = System.currentTimeMillis();
                BetDao.updateBetsBatch(betsForBatchUpdate);
                logger.info("updateBetsBatch batch time: " + (System.currentTimeMillis() - start)  + "; size - " + betsForBatchUpdate.size());
            } catch (Exception e) {
                logger.error("updateBetsBatch batch error", e);

            }
        }
    }

    public static void putEvent(BasicEvent event) {
        if (Server.isMaster) {
            try {
                eventQueue.offer(event, 10, TimeUnit.SECONDS);
            } catch (Exception e) {
                logger.fatal("EventsUpdateProcessor putEvent fatal", e);
            }
        }
    }

    public void initEvents() {
        logger.info("EventsUpdateProcessor init events");
        for (Match m: BetDao.getMatches()) {
            EventKey key = new EventKey(m);
            events.put(key, m);
        }
    }

    /*public void updateEvents() {
        Map<EventKey, Match> newEvents = new HashMap<EventKey, Match>();
        logger.info("EventsUpdateProcessor update events");
        long start = System.currentTimeMillis();

        StaticCache.matches = BetDao.getMatches();
        logger.info("EventsUpdateProcessor getMatches time - " + (System.currentTimeMillis() - start) / 1000);
        for (Match m: StaticCache.matches) {
            EventKey key = new EventKey(m);
            newEvents.put(key, m);
            logger.info("EventsUpdateProcessor: process event " + m.getTeam1() + " - " + m.getTeam2() +
                    " ;day - " + m.getMatchDay());
        }
        events = newEvents;

        StaticCache.lastFullBranchesNew = ParserCore.processGetBetsCustomNew();
        StaticCache.lastFullBranches = ParserCore.processGetBetsCustom();
    }*/

    @Override
    public void run() {
        if (Server.isMaster) {
            logger.info("EventsUpdateProcessor started");
            while (true) {
                try {
                    BasicEvent event = eventQueue.poll(1000, TimeUnit.MINUTES);
                    logger.info("eventQueue size: " + eventQueue.size());
                    if (event != null) {
                        processEvent(event);
                    }
                } catch (Exception e) {
                    logger.error("EventsUpdateProcessor error", e);
                }
            }
        }
    }

    private void processEvent(BasicEvent e) {
        logger.info("EventsUpdateProcessor: process event " + e.team1 + " - " + e.team2 +
        " ; site - " + e.site + " ; day - " + e.day + " ; bets size - " + e.bets.size());
        try {
            //save bets and events
            EventKey key = new EventKey(e);
            Match exist = events.get(key);
            if (exist == null) {
                Match match = new Match(e);
                events.put(key, match);
                //first save
                BetDao.saveMatch(match); //with bets

                //send update bets

                match.getBets().forEach((site, val) -> {
                    val.forEach((betType, betVal) -> {
                        BetUpdateConsumer.BetUpdate upd = new BetUpdateConsumer.BetUpdate();
                        upd.bet = betType;
                        upd.matchId = match.getId();
                        upd.site = site;
                        upd.val = betVal.getBetVal();
                        BetUpdateProducer.sendUpdate(upd);
                    });
                });

                logger.info("EventsUpdateProcessor: store new match " + e.team1 + " - " + e.team2 +
                        " ; site - " + e.site + " ; day - " + e.day);
            } else {
                executorService.submit(new UpdateHandler(e, exist));
            }
        } catch (Exception ex) {
            logger.error("EventsUpdateProcessor processEvent error", ex);
        }
    }
}
