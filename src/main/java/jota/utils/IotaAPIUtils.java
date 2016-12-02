package jota.utils;

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

    public static GetNewAddressResponse getNewAddress(final String seed, final int index) {

        final int[] key = Signing.key(Converter.trits(seed), index, 2);
        System.out.println("Length = "+ key.length );
        final int[] digests = Signing.digests(key);
        System.out.println("Length = "+ digests.length );

        final int[] addressTrits = Signing.address(digests);
        System.out.println("Length = "+ addressTrits.length );

        final String address = Converter.trytes(addressTrits);

        return GetNewAddressResponse.create(address);
    }

    public static GetBundleResponse getBundle(final String transaction) {
        throw new NotImplementedException("Not yet implemented");
    }
}
