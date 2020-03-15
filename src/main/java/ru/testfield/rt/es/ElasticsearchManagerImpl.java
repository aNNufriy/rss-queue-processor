package ru.testfield.rt.es;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import java.io.IOException;
import java.util.Collection;

public class ElasticsearchManagerImpl implements ElasticsearchManager {
    private static final Logger logger = LogManager.getLogger(ElasticsearchManagerImpl.class.getName());

    private RestHighLevelClient restHighLevelClient;

    private final ElasticsearchMapper mapper;

    public ElasticsearchManagerImpl(RestHighLevelClient restHighLevelClient, ElasticsearchMapper mapper) {
        this.restHighLevelClient = restHighLevelClient;
        this.mapper = mapper;
    }

    @Override
    public <T> T queryForObject(GetRequestBuilder requestBuilder, Class<T> clazz) {
        logger.debug(requestBuilder.toString());
        GetResponse response = requestBuilder.execute().actionGet();
        return mapper.mapResult(response, clazz);
    }

    public HighlightBuilder createHighlighter(String field){
        return new HighlightBuilder().field(field)
                .numOfFragments(1)
                .fragmentSize(Integer.MAX_VALUE).highlighterType("plain");

    }

    @Override
    public <T> Collection<T> queryForCollection(SearchRequestBuilder requestBuilder, Class<T> clazz) {
        logger.debug(requestBuilder.toString());
        SearchResponse response = requestBuilder.execute().actionGet();
        return mapper.mapResults(response.getHits(), clazz);
    }

    @Override
    public <T> Collection<T> queryForIds(Class<T> clazz, String index, String ... ids) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.idsQuery().addIds(ids));
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return mapper.mapResults(searchResponse.getHits(), clazz);
    }

    @Override
    public String index(IndexRequest request) throws IOException {
        logger.debug(request.toString());
        return restHighLevelClient.index(request,RequestOptions.DEFAULT).getId();
    }

    @Override
    public String update(UpdateRequest request) throws IOException {
        logger.debug(request.toString());
        return restHighLevelClient.update(request,RequestOptions.DEFAULT).getId();
    }

    @Override
    public String delete(DeleteRequest request) throws IOException {
        logger.debug(request.toString());
        return restHighLevelClient.delete(request, RequestOptions.DEFAULT).getId();
    }
}