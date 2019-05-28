package com.isfootball.processing;

import com.google.gson.Gson;
import com.isfootball.model.BasicEvent;
import com.rabbitmq.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Evgeniy Pshenitsin on 03.10.2015.
 */
public class EventProducer {

    private static final Logger logger = LogManager.getLogger("server");

    private final Channel channel;
    private final Connection connection;

    final static Gson gson = new Gson();

    public EventProducer() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(RabbitMQStatic.RABBITMQ_HOST);
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(EventSender.EVENT_QUEUE_NAME, false, false, false, null);
            channel.basicQos(EventSender.PREFETCH_COUNT);
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

    public void produce(BasicEvent event) {
        try {
            final String message = gson.toJson(event);
            channel.basicPublish("", EventSender.EVENT_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            logger.info(" [x] Sent '" + message + "'");
        } catch (Exception e) {
            logger.error("produce error", e);
        }
    }

}
