package com.isfootball.notify;

import com.isfootball.branching.BaseScheme;
import com.isfootball.model.JSONOutcomeProfit;
import com.isfootball.processing.ForksEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Evgeniy Pshenitsin on 12.10.2015.
 */
public class EmailSender {

    private static final Logger logger = LogManager.getLogger("server");

    final static String senderEmail = "betting.robot@yandex.ru";
    final static String senderPass = "Fdf76_fdOvmkkfd5438754_";

    public static class Message {

        public String email;
        public ForksEngine.ForkHolder fork;

        public Message(String email, ForksEngine.ForkHolder fork) {
            this.email = email;
            this.fork = fork;
        }

    }

    private final static ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(100);

    public static void send(Message msg) {
        try {
            messages.offer(msg, 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.fatal("EmailSender push to queue", e);
        }
    }

    public static void init() {
        Thread thread = new Thread(new Runnable(
        ) {
            @Override
            public void run() {
                logger.info("EmailSender thread started");
                while (true) {
                    try {
                        Message msg = messages.poll(1000, TimeUnit.MINUTES);
                        sendEmail(msg);
                        logger.info("EmailSender eventQueue size: " + messages.size());
                    } catch (Exception e) {
                        logger.error("EmailSender error", e);
                    }
                }
            }
        });
        thread.start();

    }
    public static void sendEmail(Message msg) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.yandex.ru");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail,senderPass);
                    }
                });
        try {
            final BigDecimal percentage = new BigDecimal((msg.fork.jsonProfit.profitRate - 1.0) * 100.0).setScale(4, BigDecimal.ROUND_HALF_UP);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(javax.mail.Message.RecipientType.TO,
                    InternetAddress.parse(msg.email));
            message.setSubject(msg.fork.forkId + ": Уведомеление о новой вилке: " + percentage.toString() + " %", "UTF-8");

            final StringBuilder newMessage = new StringBuilder();

            BigDecimal sum = new BigDecimal("10000.00");

            /*newMessage.append("Дата матча: ").append(msg.fork.jsonEvent.date).append("\n");
            newMessage.append("Команды: ").append(msg.fork.jsonEvent.team1).append(" - ").append(msg.fork.jsonEvent.team2).append("\n");

            newMessage.append("Вилка: ").append(percentage).append("%").append("\n");
            newMessage.append("Выигрыш от 10.000: ");
            newMessage.append(new BigDecimal(10000.0 * msg.fork.jsonEvent.profits.get(0).profitRate).setScale(2, BigDecimal.ROUND_HALF_UP)).append(" %").append("\n");

            msg.fork.jsonEvent.profits.forEach(profit -> {
                JSONOutcomeProfit uniform = profit.profits.get(BaseScheme.ProfitType.UNIFORM);
                Arrays.asList(uniform.factor1, uniform.factor2, uniform.factor3).stream().filter( factor -> factor != null)
                        .forEach(factor -> {
                    newMessage.append(factor.bet).append(" - ").append(factor.site).append(" - ")
                            .append(factor.val)
                            .append("; ставка - ")
                            .append(sum.multiply(new BigDecimal(factor.k)).setScale(2, BigDecimal.ROUND_HALF_UP))
                            .append("\n");
                });
            });*/

            message.setText(newMessage.toString(), "UTF-8");

            logger.info("EmailSender message: " + newMessage.toString());
            //Transport.send(message);
        } catch (MessagingException e) {
            logger.error("EmailSender: error send email MessagingException - ", e);
        } catch (Exception e) {
            logger.error("EmailSender: error send email Exception - ", e);
        }
    }

}
