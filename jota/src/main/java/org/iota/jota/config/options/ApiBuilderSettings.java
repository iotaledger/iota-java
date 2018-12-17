package org.iota.jota.config.options;

import org.iota.jota.IotaLocalPoW;
import org.iota.jota.pow.ICurl;

public interface ApiBuilderSettings {
    
    ICurl getCustomCurl();
    
    IotaLocalPoW getLocalPoW();
}
