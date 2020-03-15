package ru.testfield.rt.es;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.Collection;

public interface ElasticsearchMapper {
    <T> T mapResult(GetResponse response, Class<T> clazz);
    <T> T mapResult(SearchHit response, Class<T> clazz);
    <T> Collection<T> mapResults(SearchHits hits, Class<T> clazz);
}
