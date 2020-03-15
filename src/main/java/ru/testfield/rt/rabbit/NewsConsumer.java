package ru.testfield.rt.rabbit;

import com.rabbitmq.client.*;

import java.io.IOException;

public class NewsConsumer extends DefaultConsumer {

    public NewsConsumer(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
            throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(" Received from '" + envelope.getRoutingKey() + "': '" + message + "'");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
