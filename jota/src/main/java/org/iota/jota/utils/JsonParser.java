package org.iota.jota.utils;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonParser {

    private static ObjectMapper toObjectMapper;
    private static ObjectMapper fromObjectMapper;

    private static JsonParser parser;
    
    private JsonParser() {
        toObjectMapper = new ObjectMapper();
        toObjectMapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
        toObjectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        toObjectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        toObjectMapper.setPropertyNamingStrategy(new RevertSnakeStrategy());
    }
    
    public static JsonParser get() {
        if (parser == null) {
            parser = new JsonParser();
        }
        return parser;
    }
    
    public <T> T parsJson(String data) throws JsonParseException, JsonMappingException, IOException {
        return toObjectMapper.readValue(data, new TypeReference<T>(){});
    }
    
    public Object parsJson(String data, JavaType type) throws JsonParseException, JsonMappingException, IOException {
        return toObjectMapper.readValue(data, type);
    }
    
    public <T> String toJson(T object) throws JsonProcessingException {
        return toObjectMapper.writeValueAsString(object);
    }
    
    public Object convert(Map<String, Object> map, JavaType type) {
        return toObjectMapper.convertValue(map, type);
    }

    public TypeFactory getTypeFactory() {
        return toObjectMapper.getTypeFactory();
    }
    
    public <T> T parseBackFromSnake(String snakeJson, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
        return fromObjectMapper.readValue(snakeJson, clazz);
    }
    
    //Jackson only uses the propertyNamingStrategy SNAKE for serializing, but that doesnt work in deserialising
    @SuppressWarnings("serial")
    private class RevertSnakeStrategy extends PropertyNamingStrategy {
        @Override
        public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
            return convert(defaultName);

        }

        @Override
        public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
            return convert(defaultName);
        }

        @Override
        public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
            String a = convert(defaultName);
            return a;
        }

        public String convert(String defaultName) {
            return defaultName.replaceAll("([^_A-Z])([A-Z])", "$1_$2").toLowerCase();
        }

    }

    public ObjectMapper getObjectMapper() {
        return toObjectMapper;
    }
}
