package org.iota.jota.utils;

import static org.iota.jota.utils.Constants.INVALID_ADDRESSES_INPUT_ERROR;

import org.iota.jota.error.ArgumentException;
import org.iota.jota.pow.ICurl;
import org.iota.jota.pow.JCurl;
import org.iota.jota.pow.SpongeFactory;

/**
 * This class defines utility methods to add/remove the checksum to/from an address.
 */
public class Checksum {

    /**
     * Adds the checksum to the specified address.
     *
     * @param address The address without checksum.
     * @return The address with the appended checksum.
     * @throws ArgumentException is thrown when the specified address is not a valid address, or already has a checksum.
     **/
    public static String addChecksum(String address) throws ArgumentException {
        InputValidator.checkAddressWithoutChecksum(address);
        String addressWithChecksum = address;
        addressWithChecksum += calculateChecksum(address);
        return addressWithChecksum;
    }

    /**
     * Remove the checksum to the specified address.
     *
     * @param address The address with checksum.
     * @return The address without checksum.
     * @throws ArgumentException is thrown when the specified address is not an address with checksum.
     **/
    public static String removeChecksum(String address) throws ArgumentException {
        if (isAddressWithChecksum(address)) {
            return removeChecksumFromAddress(address);
        } else if (isAddressWithoutChecksum(address)) {
            return address;
        }
        throw new ArgumentException(INVALID_ADDRESSES_INPUT_ERROR);
    }

    private static String removeChecksumFromAddress(String addressWithChecksum) {
        return addressWithChecksum.substring(0, Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM);
    }

    /**
     * Determines whether the specified address with checksum has a valid checksum.
     *
     * @param addressWithChecksum The address with checksum.
     * @return <code>true</code> if the specified address with checksum has a valid checksum [the specified address with checksum]; otherwise, <code>false</code>.
     * @throws ArgumentException is thrown when the specified address is not an valid address.
     **/
    public static boolean isValidChecksum(String addressWithChecksum) throws ArgumentException {
        String addressWithoutChecksum = removeChecksum(addressWithChecksum);
        String addressWithRecalculateChecksum = addressWithoutChecksum += calculateChecksum(addressWithoutChecksum);
        return addressWithRecalculateChecksum.equals(addressWithChecksum);
    }

    /**
     * Check if specified address is an address with checksum.
     *
     * @param address The address to check.
     * @return <code>true</code> if the specified address is with checksum ; otherwise, <code>false</code>.
     * @throws ArgumentException is thrown when the specified address is not an valid address.
     **/
    public static boolean isAddressWithChecksum(String address) throws ArgumentException {
        return InputValidator.checkAddress(address);
    }

    /**
     * Check if specified address is an address without checksum.
     *
     * @param address The address to check.
     * @return <code>true</code> if the specified address is without checksum ; otherwise, <code>false</code>.
     * @throws ArgumentException is thrown when the specified address is not an valid address.
     **/
    public static boolean isAddressWithoutChecksum(String address) throws ArgumentException {
        return InputValidator.checkAddressWithoutChecksum(address);
    }

    private static String calculateChecksum(String address) {
        ICurl curl = SpongeFactory.create(SpongeFactory.Mode.KERL);
        curl.reset();
        curl.absorb(Converter.trits(address));
        int[] checksumTrits = new int[JCurl.HASH_LENGTH];
        curl.squeeze(checksumTrits);
        String checksum = Converter.trytes(checksumTrits);
        return checksum.substring(72, 81);
    }
}
