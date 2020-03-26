package ru.testfield.rt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.testfield.rt.config.ApplicationPropertiesProvider;
import ru.testfield.rt.config.Properties;
import ru.testfield.rt.es.*;
import ru.testfield.rt.rabbit.RabbitMQAgent;

import java.io.IOException;

public class Main {

  public static void main(String[] argv) {

    Logger logger = LoggerFactory.getLogger(Main.class);

    logger.info("Starting...");

    Properties properties = ApplicationPropertiesProvider.getInstance("properties.yml");

    ElasticsearchManager esManager = new ElasticsearchManagerImpl(properties);

    RabbitMQAgent rabbitMQAgent = new RabbitMQAgent(properties);

    try{
      rabbitMQAgent.init(esManager);
    }catch (IOException e){
      System.out.println("Unable to initialize Rabbit agent");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}