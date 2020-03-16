package ru.testfield.rt;

import ru.testfield.rt.config.ApplicationPropertiesProvider;
import ru.testfield.rt.config.Properties;
import ru.testfield.rt.es.*;
import ru.testfield.rt.model.Post;

import java.io.IOException;

public class Main {

  public static void main(String[] argv) {

    Properties properties = ApplicationPropertiesProvider.getInstance("properties.yml");

    ElasticsearchManager esManager = new ElasticsearchManagerImpl(properties);

    try {
      esManager.queryForIds(Post.class,
              "postbox",
              "BJ8h1HABrGv6483HXLMo",
              "A58h1HABrGv6483HNLMm",
              "BZ8h1HABrGv6483Hg7Nq")
              .forEach(post -> System.out.println(post));
      esManager.delete("postbox","BZ8h1HABrGv6483Hg7Nq");
    } catch (IOException e) {
      e.printStackTrace();
    }
    esManager.close();

//    try {
//      Post post = new Post();
//      post.setBody("post body");
//      post.setDateAdded(LocalDateTime.now());
//      post.setId(UUID.randomUUID().toString());
//      String json = new ObjectMapper().writeValueAsString(post);
//      System.out.println("JSON: "+json);
//    }catch(Exception e){
//      System.out.println("Error");
//    }

//    RabbitMQAgent rabbitMQAgent = new RabbitMQAgent(properties);
//    rabbitMQAgent.init();
//    rabbitMQAgent.consume();
  }

}