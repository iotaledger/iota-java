package org.iota.jota.store;

import java.io.File;
import java.util.Optional;

public class IotaFileStore extends IotaClientStore {

    private static final String DEFAULT_STORE = ".." + File.separator + "client.store";
    
    public IotaFileStore() {
        super(new FlatFileStore(DEFAULT_STORE));
    }
    
    public IotaFileStore(String location) {
        super(new FlatFileStore(null != location ? location : DEFAULT_STORE));
    }
    
    public IotaFileStore(Optional<String> location) {
        super(new FlatFileStore(location.isPresent() ? location.get() : DEFAULT_STORE));
    }
}
