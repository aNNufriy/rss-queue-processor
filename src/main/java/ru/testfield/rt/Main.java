package ru.testfield.rt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import ru.testfield.rt.config.ApplicationPropertiesProvider;
import ru.testfield.rt.config.Properties;
import ru.testfield.rt.es.*;
import ru.testfield.rt.model.Post;

import java.io.IOException;

public class Main {

  public static void main(String[] argv) {

    Properties properties = ApplicationPropertiesProvider.getInstance("properties.yml");

    ElasticsearchMapper esMapper = new ElasticsearchMapperImpl(getObjectMapper());
    ElasticsearchManager esManager = new ElasticsearchManagerImpl(getRestClient(properties),esMapper);

    try {
      esManager.queryForIds(Post.class,"postbox","BJ8h1HABrGv6483HXLMo")
              .forEach(post -> System.out.println(post));
    } catch (IOException e) {
      e.printStackTrace();
    }

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

  private static ObjectMapper getObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
    mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    return mapper;
  }
  private static RestHighLevelClient getRestClient(Properties properties){
    RestClientBuilder builder = RestClient.builder(
            new HttpHost(properties.getElasticHost(),
            properties.getElasticPort(), "http")
    );

    return new RestHighLevelClient(builder);
  }
}