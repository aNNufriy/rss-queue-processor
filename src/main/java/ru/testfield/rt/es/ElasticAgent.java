package ru.testfield.rt.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import ru.testfield.rt.config.Properties;
import ru.testfield.rt.model.Post;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class ElasticAgent {

    public RestHighLevelClient restClient;

    public ElasticAgent(Properties properties) {
        RestClientBuilder builder = RestClient.builder(new HttpHost(properties.getElasticHost(),
                properties.getElasticPort(), "http"));

        restClient = new RestHighLevelClient(builder);
    }

    public String put() {
        try {
            Post post = new Post();
            post.setBody("post body");
            post.setDateAdded(LocalDateTime.now());
            post.setId(UUID.randomUUID().toString());
            String json = new ObjectMapper().writeValueAsString(post);
            System.out.println("JSON: "+json);

            IndexRequest request = new IndexRequest("postbox");
            request.id(post.getId());

            request.id(post.getId());
            request.source(json, XContentType.JSON);
            IndexResponse indexResponse = restClient.index(request, RequestOptions.DEFAULT);
            System.out.println("response id: " + indexResponse.getId());
            return indexResponse.getResult().name();
        }catch(IOException e){
            System.out.println("Error");
            return "Error";
        }
    }

    public String put(String body) {
        try {
            IndexRequest request = new IndexRequest("postbox");
            request.source(body, XContentType.JSON);
            IndexResponse indexResponse = restClient.index(request, RequestOptions.DEFAULT);
            return indexResponse.getResult().name();
        }catch(IOException e){
            System.out.println("Error");
            return "Error";
        }
    }
}
