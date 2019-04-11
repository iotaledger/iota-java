package org.iota.jota.store;

import java.util.Optional;

public class IotaFileStore extends IotaClientStore {
    
    private static final String DEFAULT_STORE = "../client.store";
    
    public IotaFileStore() {
        super(new JsonFlatFileStore(DEFAULT_STORE));
    }
    
    public IotaFileStore(String location) {
        super(new JsonFlatFileStore(null != location ? location : DEFAULT_STORE));
    }
    
    public IotaFileStore(Optional<String> location) {
        super(new JsonFlatFileStore(location.isPresent() ? location.get() : DEFAULT_STORE));
    }
}
