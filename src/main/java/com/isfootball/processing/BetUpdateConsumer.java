package com.isfootball.processing;

import com.google.gson.Gson;
import com.isfootball.jdbc.EventsUpdateProcessor;
import com.isfootball.model.BasicEvent;
import com.isfootball.parser.BetSite;
import com.isfootball.parser.BetType;
import com.rabbitmq.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by Evgeniy Pshenitsin on 03.10.2015.
 */
public class BetUpdateConsumer {

    public final static String BET_UPD_XCH_NAME = "bet_updates";

    private static volatile BetUpdateConsumer instance;

    private static final Logger logger = LogManager.getLogger("server");

    private final Channel channel;
    private final Connection connection;

    final static Gson gson = new Gson();

    public static class BetUpdate {

        public BetSite site;

        public BetType bet;

        public Double val;

        public long matchId;

        public String matchLink;

        public boolean isSame = false;

    };

    public static void initialize() {
        BetUpdateConsumer localInstance = instance;
        if (localInstance == null) {
            synchronized (BetUpdateConsumer.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new BetUpdateConsumer();
                }
            }
        }
    }

    public static BetUpdateConsumer getInstance() {
        return instance;
    }

    private BetUpdateConsumer() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(RabbitMQStatic.RABBITMQ_HOST);
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(BET_UPD_XCH_NAME, "fanout");

            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, BET_UPD_XCH_NAME, "");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    try {
                        BetUpdate upd = gson.fromJson(message, BetUpdate.class);
                        ForksEngine.pushUpdate(upd);
                        logger.info("BetUpdateConsumer [x] Received '" + message + "'");
                    } catch (Exception e) {
                        logger.error(" BetUpdateConsumer handleDelivery", e);
                    } finally {
                        //channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            };
            channel.basicConsume(queueName, true, consumer);
        } catch (Exception e) {
            logger.fatal("BetUpdateConsumer can't initialize rabbitmq consumer", e);
            throw new RuntimeException();
        }
    }

}
