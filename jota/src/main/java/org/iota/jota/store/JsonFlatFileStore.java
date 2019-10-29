package org.iota.jota.store;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.iota.jota.account.AccountState;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

public class JsonFlatFileStore extends FlatFileStore {
    
    private ObjectMapper objectMapper;
    
    public JsonFlatFileStore(File file) {
        super(file);
        loadJson();
    }

    public JsonFlatFileStore(InputStream inputStream, OutputStream outputStream) {
        super(inputStream, outputStream);
        loadJson();
    }

    public JsonFlatFileStore(String location) {
        super(location);
        loadJson();
    }
    
    private void loadJson() {
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    }

    @Override
    protected Map<String, Serializable> loadFromInputStream(InputStream stream){
        try {
            return Collections.unmodifiableMap(objectMapper.readValue(stream,
                    new TypeReference<Map<String, AccountState>>() {}));
        } catch (IOException e) {
            if (e.getClass().equals(MismatchedInputException.class)) {
                return new HashMap<>();
            } else {
                throw new RuntimeException(e);
            }
        }
    }
    
    @Override
    protected void writeToOutputStream(OutputStream stream, Map<String, Serializable> store) throws IOException {
        objectMapper.writeValue(stream, store);
    }
}
