package ru.testfield.rt.rabbit;

import com.rabbitmq.client.*;
import ru.testfield.rt.config.Properties;

public class RabbitMQAgent {

    private boolean connected;

    private ConnectionFactory factory;
    private Consumer consumer;
    private Channel channel;
    private String rabbitQueueName;

    private String rabbitUser;
    private String rabbitPassword;

    private final String rabbitHost;
    private final int rabbitPort;

    public RabbitMQAgent(Properties properties) {
        this.factory = new ConnectionFactory();
        this.rabbitQueueName = properties.getRabbitQueueName();
        this.rabbitUser = properties.getRabbitUser();
        this.rabbitPassword = properties.getRabbitPassword();

        this.rabbitHost = properties.getRabbitHost();
        this.rabbitPort = properties.getRabbitPort();
    }

    public void init() {
        this.initFactory();
        this.connect();
    }

    private void initFactory(){
        factory.setHost(rabbitHost);
        factory.setPort(rabbitPort);
        factory.setUsername(rabbitUser);
        factory.setPassword(rabbitPassword);
        factory.setAutomaticRecoveryEnabled(true);
    }

    private void connect() {
        try {
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
            channel.basicQos(1);
        }catch(Exception e){
            throw new RuntimeException();
        }
    }

    public boolean consume() {
        try {
            consumer = new NewsConsumer(channel);
            channel.basicConsume(rabbitQueueName, true, consumer);
        }catch(Exception e){
            return false;
        }
        return true;
    }
}
