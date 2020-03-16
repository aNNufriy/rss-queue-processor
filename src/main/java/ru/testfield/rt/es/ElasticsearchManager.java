package ru.testfield.rt.es;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import java.io.IOException;
import java.util.Collection;


public interface ElasticsearchManager {

    <T> T queryForObject(GetRequestBuilder requestBuilder, Class<T> clazz);
    <T> Collection<T> queryForCollection(SearchRequestBuilder requestBuilder, Class<T> clazz);
    <T> Collection<T> queryForIds(Class<T> clazz, String index, String ... ids) throws IOException;

    HighlightBuilder createHighlighter(String field);

    String index(IndexRequest request) throws IOException;
    String update(UpdateRequest request) throws IOException;
    String delete(String index, String id) throws IOException;

    void close();
}

