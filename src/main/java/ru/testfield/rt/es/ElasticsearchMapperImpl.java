package ru.testfield.rt.es;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class ElasticsearchMapperImpl implements ElasticsearchMapper {
    private final static Logger logger = LoggerFactory.getLogger(ElasticsearchMapper.class);

    private final ObjectMapper mapper;

    public ElasticsearchMapperImpl() {
        this.mapper = getObjectMapper();
    }

    @Override
    public <T> T mapResult(GetResponse response, Class<T> clazz) {
        T entity = mapEntity(response.getSourceAsString(), clazz);
        if (entity != null) {
            setId(entity, response.getId());
        }
        return entity;
    }

    @Override
    public <T> T mapResult(SearchHit response, Class<T> clazz) {
        T entity = mapEntity(response.getSourceAsString(), clazz);
        if (entity != null) {
            setId(entity, response.getId());
        }

        setHighlightFields(response, clazz, entity);
        return entity;
    }

    @Override
    public <T> Collection<T> mapResults(SearchHits hits, Class<T> clazz) {
        Collection<T> list = new ArrayList<>();
        hits.forEach(hit -> list.add(mapResult(hit, clazz)));
        return list;
    }

    private <T> T mapEntity(String source, Class<T> clazz) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        T result = null;
        try {
            result = mapper.readValue(source, clazz);
        } catch (IOException e) {
            logger.error("Failed parse source: " + source, e);
        }
        return result;
    }

    private <T> void setId(T entity, String id) {
        try {
            Field idField = entity.getClass().getDeclaredField("id");
            if (idField.getType().isAssignableFrom(String.class)) {
                idField.setAccessible(true);
                idField.set(entity, id);
            }
        } catch (NoSuchFieldException e) {
            logger.error("Failed find id field: ", e);
        } catch (IllegalAccessException e) {
            logger.error("Failed set id field: ", e);
        }
    }

    private <T> void setHighlightFields(SearchHit response, Class<T> clazz, T entity) {
        Map<String, HighlightField> fieldMap = response.getHighlightFields();
        if (fieldMap == null || fieldMap.isEmpty()) {
            return;
        }

        fieldMap.forEach((name, highlightField) -> {
            Field searchableField = null;
            try {
                searchableField = clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                logger.debug("Class {} does not contain field {}",clazz.getName(),name);
            }

            if (searchableField == null) {
                searchableField = Arrays.stream(clazz.getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(JsonProperty.class)
                                && field.getDeclaredAnnotation(JsonProperty.class).value().equals(name))
                        .findAny().orElse(null);
            }

            if (searchableField != null) {
                searchableField.setAccessible(true);
                String text = Arrays.stream(highlightField.fragments())
                        .map(Text::toString)
                        .collect(Collectors.joining());

                try {
                    searchableField.set(entity, text);
                } catch (IllegalAccessException e) {
                    logger.error("Failed set highligght field: ", e);
                }
            }
        });
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
}
