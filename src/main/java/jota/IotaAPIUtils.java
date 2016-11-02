package jota;

import jota.dto.response.GetBundleResponse;
import jota.dto.response.GetNewAddressResponse;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client Side computation service
 *
 * @author davassi
 */
public class IotaAPIUtils {

    private static final Logger log = LoggerFactory.getLogger(IotaAPIUtils.class);

    public static GetNewAddressResponse getNewAddress(final String seed, final Integer securityLevel) {
        throw new NotImplementedException("Not yet implemented");
    }

    public static GetBundleResponse getBundle(final String transaction) {
        throw new NotImplementedException("Not yet implemented");
    }
}
