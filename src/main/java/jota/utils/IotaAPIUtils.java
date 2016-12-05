package jota.utils;

import jota.IotaAPIProxy;
import jota.dto.response.FindTransactionResponse;
import jota.dto.response.GetBundleResponse;
import jota.dto.response.GetNewAddressResponse;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Client Side computation service
 *
 * @author davassi
 */
public class IotaAPIUtils {

    private static final Logger log = LoggerFactory.getLogger(IotaAPIUtils.class);

    /**
     * Generates a new address from a seed and returns the remainderAddress.
     * This is either done deterministically, or by providing the index of the new remainderAddress
     *
     * @param seed      Tryte-encoded seed. It should be noted that this seed is not transferred
     * @param index     Optional (default null). Key index to start search from. If the index is provided, the generation of the address is not deterministic.
     * @param checksum  Optional (default false). Adds 9-tryte address checksum
     * @param total     Optional (default 1)Total number of addresses to generate
     * @param returnAll If true, it returns all addresses which were deterministically generated (until findTransactions returns null)
     * @return an array of strings with the specifed number of addresses
     */

    public static GetNewAddressResponse getNewAddress(final String seed, final int index, final boolean checksum, final int total, final boolean returnAll) {

        final List<String> allAddresses = new ArrayList<>();
        // Case 1: total
        //
        // If total number of addresses to generate is supplied, simply generate
        // and return the list of all addresses
        //
        //
        if (total != 0) {
            // Increase index with each iteration
            for (int i = index; i < index + total; i++) {
                allAddresses.add(newAddress(seed, i, checksum));
            }
        }
        //  Case 2: no total provided
        //
        //  Continue calling findTransactions to see if address was already created
        //  if null, return list of addresses
        //
        else {

            // TODO init with params
            IotaAPIProxy proxy = new IotaAPIProxy.Builder().build();

            for (int i = index; ; i++) {
                String newAddress = newAddress(seed, i, checksum);

                FindTransactionResponse response = proxy.findTransactions(null, new String[]{newAddress}, null, null);

                // If returnAll, return list of allAddresses
                // else return only the last address that was generated
                
                if (!returnAll) {
                	allAddresses.clear();
                }
                
                allAddresses.add(newAddress);
                
                if (response.getHashes().length == 0) {
                    break;
                }
            }
        }

        return GetNewAddressResponse.create(allAddresses);
    }

    /**
     * Generates a new address
     *
     * @param seed
     * @param index
     * @param checksum
     * @return an String with address
     */
    private static String newAddress(String seed, int index, boolean checksum) {

        final int[] key = Signing.key(Converter.trits(seed), index, 2);
        log.debug("key Length = {}", key.length );
        
        final int[] digests = Signing.digests(key);
        log.debug("digests Length = {}", digests.length );
        
        final int[] addressTrits = Signing.address(digests);
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

