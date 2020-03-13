package ru.testfield.rt.rabbit;

import com.rabbitmq.client.*;
import ru.testfield.rt.config.Properties;
import ru.testfield.rt.es.ElasticAgent;

public class RabbitMQAgent {

    ElasticAgent elasticAgent;

    private boolean connected;

    private ConnectionFactory factory;
    private Consumer consumer;
    private Channel channel;
    private String rabbitQueueName;

    private String rabbitUser;
    private String rabbitPassword;

    private final String rabbitHost;
    private final int rabbitPort;

    public RabbitMQAgent(Properties properties, ElasticAgent elasticAgent) {
        this.factory = new ConnectionFactory();
        this.rabbitQueueName = properties.getRabbitQueueName();
        this.rabbitUser = properties.getRabbitUser();
        this.rabbitPassword = properties.getRabbitPassword();

        this.rabbitHost = properties.getRabbitHost();
        this.rabbitPort = properties.getRabbitPort();
        this.elasticAgent = elasticAgent;
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
            consumer = new NewsConsumer(channel, elasticAgent);
            channel.basicConsume(rabbitQueueName, true, consumer);
        }catch(Exception e){
            return false;
        }
        return true;
    }
}
