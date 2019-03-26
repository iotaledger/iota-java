package org.iota.jota.utils;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonParser {

    private static ObjectMapper objectMapper;

    private JsonParser() {
        objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    }
    
    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }
    
    public <T> T parsJson(String data) throws JsonParseException, JsonMappingException, IOException {
        return objectMapper.readValue(data, new TypeReference<T>(){});
    }
    
    public Object parsJson(String data, JavaType type) throws JsonParseException, JsonMappingException, IOException {
        return objectMapper.readValue(data, type);
    }
    
    public <T> String toJson(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
    
    public Object convert(Map<String, Object> map, JavaType type) {
        return objectMapper.convertValue(map, type);
    }

    public TypeFactory getTypeFactory() {
        return objectMapper.getTypeFactory();
    }
}
