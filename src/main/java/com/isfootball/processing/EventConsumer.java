package com.isfootball.processing;

import com.google.gson.Gson;
import com.isfootball.jdbc.EventsUpdateProcessor;
import com.isfootball.model.BasicEvent;
import com.rabbitmq.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by Evgeniy Pshenitsin on 03.10.2015.
 */
public class EventConsumer {

    private static volatile EventConsumer instance;

    private static final Logger logger = LogManager.getLogger("server");

    private final Channel channel;
    private final Connection connection;

    final static Gson gson = new Gson();

    public static void initialize() {
        EventConsumer localInstance = instance;
        if (localInstance == null) {
            synchronized (EventConsumer.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new EventConsumer();
                }
            }
        }
    }

    public static EventConsumer getInstance() {
        return instance;
    }

    private EventConsumer() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(RabbitMQStatic.RABBITMQ_HOST);
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(EventSender.EVENT_QUEUE_NAME, false, false, false, null);
            channel.basicQos(EventSender.PREFETCH_COUNT);

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    try {
                        BasicEvent event = gson.fromJson(message, BasicEvent.class);
                        logger.info("EventConsumer [x] Received '" + message + "'");
                        EventsUpdateProcessor.putEvent(event);
                    } catch (Exception e) {
                        logger.error(" EventConsumer handleDelivery", e);
                    } finally {
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            };
            channel.basicConsume(EventSender.EVENT_QUEUE_NAME, false, consumer);
        } catch (Exception e) {
            logger.fatal("EventConsumer can't initialize rabbitmq consumer", e);
            throw new RuntimeException();
        }
    }

}
