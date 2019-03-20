package org.iota.jota.utils;

import java.io.IOException;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {

    private ObjectMapper objectMapper;

    public JsonParser() {
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    }
    
    public <T> T parsJson(String data) throws JsonParseException, JsonMappingException, IOException {
        return objectMapper.readValue(data, new TypeReference<T>(){});
    }
    
    public <T> String toJson(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
