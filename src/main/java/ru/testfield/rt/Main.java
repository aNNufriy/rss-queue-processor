package ru.testfield.rt;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.testfield.rt.config.ApplicationPropertiesProvider;
import ru.testfield.rt.config.Properties;
import ru.testfield.rt.es.ElasticAgent;
import ru.testfield.rt.model.Post;
import ru.testfield.rt.rabbit.RabbitMQAgent;

import java.time.LocalDateTime;
import java.util.UUID;

public class Main {

  public static void main(String[] argv) {

    try {
      Post post = new Post();
      post.setBody("post body");
      post.setDateAdded(LocalDateTime.now());
      post.setId(UUID.randomUUID().toString());
      String json = new ObjectMapper().writeValueAsString(post);
      System.out.println("JSON: "+json);
    }catch(Exception e){
      System.out.println("Error");
    }

    Properties properties = ApplicationPropertiesProvider.getInstance("properties.yml");

    ElasticAgent elastiAgent = new ElasticAgent(properties);
    elastiAgent.put();
    RabbitMQAgent rabbitMQAgent = new RabbitMQAgent(properties,elastiAgent);
    rabbitMQAgent.init();
    rabbitMQAgent.consume();
  }
}