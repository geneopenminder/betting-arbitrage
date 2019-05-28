package com.isfootball.processing;

import com.google.gson.Gson;
import com.isfootball.jdbc.BetDao;
import com.isfootball.jdbc.SiteUpdates;
import com.rabbitmq.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Evgeniy Pshenitsin on 03.10.2015.
 */
public class SiteUpdateProcessor {

    final static Gson gson = new Gson();

    public final static String SITES_QUEUE_NAME = "site_updates";

    public final static int PREFETCH_COUNT = 1;

    private static final Logger logger = LogManager.getLogger("server");

    private static volatile SiteUpdateProcessor instance;

    private ArrayBlockingQueue<SiteUpdates> updatesToSend;

    public static class SiteUpdateProducer {

        private final Channel channel;
        private final Connection connection;

        public SiteUpdateProducer() {
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost(RabbitMQStatic.RABBITMQ_HOST);
                connection = factory.newConnection();
                channel = connection.createChannel();
                channel.queueDeclare(SITES_QUEUE_NAME, false, false, false, null);
                channel.basicQos(PREFETCH_COUNT);
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

        public void produce(SiteUpdates update) {
            try {
                final String message = gson.toJson(update);
                channel.basicPublish("", SITES_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                logger.info("SiteUpdateProducer [x] Sent '" + message + "'");
            } catch (Exception e) {
                logger.error("SiteUpdateProducer error", e);
            }
        }
    };

    public static class SiteUpdateConsumer {

        private final Channel channel;
        private final Connection connection;

        private SiteUpdateConsumer() {
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost(RabbitMQStatic.RABBITMQ_HOST);
                connection = factory.newConnection();
                channel = connection.createChannel();
                channel.queueDeclare(SITES_QUEUE_NAME, false, false, false, null);
                channel.basicQos(EventSender.PREFETCH_COUNT);

                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                            throws IOException {
                        String message = new String(body, "UTF-8");
                        try {
                            SiteUpdates update = gson.fromJson(message, SiteUpdates.class);
                            logger.info("SiteUpdateConsumer [x] Received '" + message + "'");
                            BetDao.saveSiteUpdate(update);
                        } catch (Exception e) {
                            logger.error("SiteUpdateConsumer handleDelivery site update", e);
                        } finally {
                            channel.basicAck(envelope.getDeliveryTag(), false);
                        }
                    }
                };
                channel.basicConsume(SITES_QUEUE_NAME, false, consumer);
            } catch (Exception e) {
                logger.fatal("SiteUpdateConsumer can't initialize rabbitmq consumer", e);
                throw new RuntimeException();
            }
        }

    };

    private SiteUpdateProducer producer;
    private SiteUpdateConsumer consumer;

    public static void initialize() {
        SiteUpdateProcessor localInstance = instance;
        if (localInstance == null) {
            synchronized (SiteUpdateProcessor.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SiteUpdateProcessor(new SiteUpdateProducer(), new SiteUpdateConsumer());
                }
            }
        }
    }

    public static void initializeProducer() {
        SiteUpdateProcessor localInstance = instance;
        if (localInstance == null) {
            synchronized (SiteUpdateProcessor.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SiteUpdateProcessor(new SiteUpdateProducer(), null);
                }
            }
        }
    }

    public static void initializeConsumer() {
        SiteUpdateProcessor localInstance = instance;
        if (localInstance == null) {
            synchronized (SiteUpdateProcessor.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SiteUpdateProcessor(null, new SiteUpdateConsumer());
                }
            }
        }
    }

    public static SiteUpdateProcessor getInstance() {
        return instance;
    }

    private SiteUpdateProcessor(SiteUpdateProducer producer, SiteUpdateConsumer consumer) {

        if (producer != null) {
            this.updatesToSend = new ArrayBlockingQueue<SiteUpdates>(1000, true);
            this.producer = producer;
            Thread thread = new Thread(new Runnable(
            ) {
                @Override
                public void run() {
                    logger.info("SiteUpdateProcessor thread started");
                    while (true) {
                        try {
                            SiteUpdates update = updatesToSend.poll(1000, TimeUnit.MINUTES);
                            logger.info("SiteUpdateProcessor eventQueue size: " + updatesToSend.size());
                            if (update != null) {
                                producer.produce(update);
                            }
                        } catch (Exception e) {
                            logger.error("SiteUpdateProcessor error", e);
                        }
                    }
                }
            });
            thread.start();
        }
        if (consumer != null) {
            this.consumer = consumer;
        }
    }

    public static void sendUpdate(SiteUpdates update) {
        try {
            getInstance().updatesToSend.offer(update, 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.fatal("SiteUpdateProcessor send", e);
        }
    }


}
