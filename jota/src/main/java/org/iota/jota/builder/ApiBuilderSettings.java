package org.iota.jota.builder;

import org.iota.jota.IotaPoW;
import org.iota.jota.pow.ICurl;

public interface ApiBuilderSettings {
    
    ICurl getCustomCurl();
    
    IotaPoW getLocalPoW();
}
