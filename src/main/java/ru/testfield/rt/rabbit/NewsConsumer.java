package ru.testfield.rt.rabbit;

import com.rabbitmq.client.*;
import ru.testfield.rt.es.ElasticAgent;

import java.io.IOException;

public class NewsConsumer extends DefaultConsumer {

    private ElasticAgent elasticAgent;

    public NewsConsumer(Channel channel, ElasticAgent elasticAgent) {
        super(channel);
        this.elasticAgent = elasticAgent;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
            throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(" Received " + envelope.getRoutingKey() + ": '" + message + "'");
        elasticAgent.put(message);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
