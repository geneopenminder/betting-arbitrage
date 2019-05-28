package com.isfootball.processing;

import com.isfootball.model.BasicEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Evgeniy Pshenitsin on 03.10.2015.
 */
public class EventSender {

    public final static String EVENT_QUEUE_NAME = "basic_events";

    public final static int PREFETCH_COUNT = 1;

    private static final Logger logger = LogManager.getLogger("server");

    private static volatile EventSender instance;

    private final static ArrayBlockingQueue<BasicEvent> eventsToSend = new ArrayBlockingQueue<BasicEvent>(100000, true);

    private final EventProducer producer;

    public static void initialize() {
        EventSender localInstance = instance;
        if (localInstance == null) {
            synchronized (EventSender.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new EventSender();
                }
            }
        }
    }

    public static EventSender getInstance() {
        return instance;
    }

    private EventSender() {
        producer = new EventProducer();

        Thread thread = new Thread(new Runnable(
        ) {
            @Override
            public void run() {
                logger.info("EventSender thread started");
                while (true) {
                    try {
                        BasicEvent event = eventsToSend.poll(1000, TimeUnit.MINUTES);
                        logger.info("EventSender eventQueue size: " + eventsToSend.size());
                        if (event != null ) { //&& event.validate()) {
                            producer.produce(event);
                        } else {
                            logger.fatal("not valid event", event == null ? "NULL" : event);
                        }
                    } catch (Exception e) {
                        logger.error("EventSender error", e);
                    }
                }
            }
        });
        thread.start();
    }

    public static void sendEvent(BasicEvent event) {
        try {
            eventsToSend.offer(event, 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.fatal("EventSender sendEvent", e);
        }
    }

}
