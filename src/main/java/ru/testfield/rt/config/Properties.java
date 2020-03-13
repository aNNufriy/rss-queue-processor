package ru.testfield.rt.config;

import java.io.InputStream;

public class Properties {

    private String rabbitQueueName;
    private String rabbitHost;
    private int rabbitPort;

    private String rabbitUser;
    private String rabbitPassword;

    private String elasticHost;
    private int elasticPort;

    public String getRabbitQueueName() {
        return rabbitQueueName;
    }

    public void setRabbitQueueName(String rabbitQueueName) {
        this.rabbitQueueName = rabbitQueueName;
    }

    public String getRabbitHost() {
        return rabbitHost;
    }

    public void setRabbitHost(String rabbitHost) {
        this.rabbitHost = rabbitHost;
    }

    public int getRabbitPort() {
        return rabbitPort;
    }

    public void setRabbitPort(int rabbitPort) {
        this.rabbitPort = rabbitPort;
    }

    public String getRabbitUser() {
        return rabbitUser;
    }

    public void setRabbitUser(String rabbitUser) {
        this.rabbitUser = rabbitUser;
    }

    public String getRabbitPassword() {
        return rabbitPassword;
    }

    public void setRabbitPassword(String rabbitPassword) {
        this.rabbitPassword = rabbitPassword;
    }

    public String getElasticHost() {
        return elasticHost;
    }

    public void setElasticHost(String elasticHost) {
        this.elasticHost = elasticHost;
    }

    public int getElasticPort() {
        return elasticPort;
    }

    public void setElasticPort(int elasticPort) {
        this.elasticPort = elasticPort;
    }
}
