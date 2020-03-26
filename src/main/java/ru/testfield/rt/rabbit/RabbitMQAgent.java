package ru.testfield.rt.rabbit;

import com.rabbitmq.client.*;
import ru.testfield.rt.config.Properties;
import ru.testfield.rt.es.ElasticsearchManager;

import java.io.IOException;

public class RabbitMQAgent implements AutoCloseable {

    private Channel channel;
    private String rabbitQueueName;

    private String rabbitUser;
    private String rabbitPassword;

    private final String rabbitHost;
    private final int rabbitPort;

    public RabbitMQAgent(Properties properties) {
        this.rabbitQueueName = properties.getRabbitQueueName();
        this.rabbitUser = properties.getRabbitUser();
        this.rabbitPassword = properties.getRabbitPassword();

        this.rabbitHost = properties.getRabbitHost();
        this.rabbitPort = properties.getRabbitPort();
    }

    public void init(ElasticsearchManager esManager) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitHost);
        factory.setPort(rabbitPort);
        factory.setUsername(rabbitUser);
        factory.setPassword(rabbitPassword);
        factory.setAutomaticRecoveryEnabled(true);
        try {
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
            channel.basicQos(1);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        Consumer consumer = new NewsConsumer(channel,esManager);
        channel.basicConsume(rabbitQueueName, true, consumer);
    }

    @Override
    public void close() throws Exception {
        this.channel.close();
    }
}
