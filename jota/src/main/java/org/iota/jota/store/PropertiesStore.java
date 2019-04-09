package org.iota.jota.store;

import java.util.Properties;
import java.util.stream.Collectors;

public class PropertiesStore extends MemoryStore {
    
    public PropertiesStore(Properties properties) {
        super(properties.entrySet().stream().collect(
            Collectors.toMap(
                    e -> e.getKey().toString(),
                    e -> e.getValue().toString()
               )
           )
        );
    }
}
