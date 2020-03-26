package ru.testfield.rt.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.testfield.rt.es.ElasticsearchManager;

import java.io.IOException;

public class NewsConsumer extends DefaultConsumer {

    private Logger logger = LoggerFactory.getLogger(NewsConsumer.class);

    private final ElasticsearchManager esManager;

    public NewsConsumer(Channel channel, ElasticsearchManager esManager) {
        super(channel);
        this.esManager = esManager;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
            throws IOException {
        String message = new String(body, "UTF-8");
        logger.info("Received from {}: {}",envelope.getRoutingKey(),message);
        String index = esManager.index("postbox", message);
        logger.info("Indexed as: {}", index);
        try {
            Thread.sleep(0);
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
