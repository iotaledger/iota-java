package jota.utils;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jota.dto.response.GetBundleResponse;

/**
 * Client Side computation service
 *
 * @author davassi
 */
public class IotaAPIUtils {

    private static final Logger log = LoggerFactory.getLogger(IotaAPIUtils.class);

    /**
     * Generates a new address
     *
     * @param seed
     * @param index
     * @param checksum
     * @return an String with address
     */
    public static String newAddress(String seed, int index, boolean checksum) {

        final int[] key = Signing.key(Converter.trits(seed), index, 2);
        log.debug("key Length = {}", key.length );
        
        final int[] digests = Signing.digests(key);
        log.debug("digests Length = {}", digests.length );
        
        final int[] addressTrits = Signing.address(digests);
        log.debug("addressTrits Length = {}", addressTrits.length );
        
        String address = Converter.trytes(addressTrits);

        if (checksum) {
            address = Checksum.addChecksum(address);
        }
        return address;
    }

    public static GetBundleResponse getBundle(final String transaction) {
        throw new NotImplementedException("Not yet implemented");
    }
}

