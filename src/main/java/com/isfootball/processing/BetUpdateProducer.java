package com.isfootball.processing;

import com.google.gson.Gson;
import com.isfootball.model.BasicEvent;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Evgeniy Pshenitsin on 03.10.2015.
 */
public class BetUpdateProducer {

    private static final Logger logger = LogManager.getLogger("server");

    final static Gson gson = new Gson();

    public class Producer {

        private final Channel channel;
        private final Connection connection;

        public Producer() {
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost(RabbitMQStatic.RABBITMQ_HOST);
                connection = factory.newConnection();
                channel = connection.createChannel();
                channel.exchangeDeclare(BetUpdateConsumer.BET_UPD_XCH_NAME, "fanout");
            } catch (Exception e) {
                logger.fatal("can't initialize rabbitmq", e);
                throw new RuntimeException();
            }
        }

        private void close() {
            try {
                channel.close();
                connection.close();
            } catch (Exception e) {
                logger.fatal("can't close rabbitmq", e);
            }
        }

        public void produce(BetUpdateConsumer.BetUpdate update) {
            try {
                final String message = gson.toJson(update);
                channel.basicPublish(BetUpdateConsumer.BET_UPD_XCH_NAME, "", null, message.getBytes());
                logger.info(" [x] Sent '" + message + "'");
            } catch (Exception e) {
                logger.error("BetUpdateProducer produce error", e);
            }
        }

    }

    private static volatile BetUpdateProducer instance;

    private final static ArrayBlockingQueue<BetUpdateConsumer.BetUpdate> eventsToSend = new ArrayBlockingQueue<BetUpdateConsumer.BetUpdate>(10000, true);

    private final Producer producer;

    public static void initialize() {
        BetUpdateProducer localInstance = instance;
        if (localInstance == null) {
            synchronized (BetUpdateProducer.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new BetUpdateProducer();
                }
            }
        }
    }

    public static BetUpdateProducer getInstance() {
        return instance;
    }

    private BetUpdateProducer() {
        producer = new Producer();

        Thread thread = new Thread(new Runnable(
        ) {
            @Override
            public void run() {
                logger.info("BetUpdateProducer thread started");
                while (true) {
                    try {
                        BetUpdateConsumer.BetUpdate event = eventsToSend.poll(1000, TimeUnit.MINUTES);
                        logger.info("BetUpdateProducer eventQueue size: " + eventsToSend.size());
                        if (event != null ) { //&& event.validate()) {
                            producer.produce(event);
                        } else {
                            logger.fatal("not valid event", event == null ? "NULL" : event);
                        }
                    } catch (Exception e) {
                        logger.error("BetUpdateProducer error", e);
                    }
                }
            }
        });
        thread.start();
    }

    public static void sendUpdate(BetUpdateConsumer.BetUpdate event) {
        try {
            eventsToSend.offer(event, 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.fatal("BetUpdateProducer sendUpdate", e);
        }
    }
}