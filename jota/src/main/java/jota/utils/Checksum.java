package jota.utils;

import jota.error.ArgumentException;
import jota.pow.ICurl;
import jota.pow.JCurl;
import jota.pow.SpongeFactory;

import static jota.utils.Constants.INVALID_ADDRESSES_INPUT_ERROR;

/**
 * This class defines utility methods to add/remove the checksum to/from an address.
 *
 * @author pinpong
 */
public class Checksum {

    /**
     * Adds the checksum to the specified address.
     *
     * @param address The address without checksum.
     * @return The address with the appended checksum.
     * @throws ArgumentException is thrown when the specified address is not an valid address.
     **/
    public static String addChecksum(String address) throws ArgumentException {
        InputValidator.checkAddress(address);
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
        return InputValidator.checkAddress(address) && address.length() == Constants.ADDRESS_LENGTH_WITH_CHECKSUM;
    }

    /**
     * Check if specified address is an address without checksum.
     *
     * @param address The address to check.
     * @return <code>true</code> if the specified address is without checksum ; otherwise, <code>false</code>.
     * @throws ArgumentException is thrown when the specified address is not an valid address.
     **/
    public static boolean isAddressWithoutChecksum(String address) throws ArgumentException {
        return InputValidator.checkAddress(address) && address.length() == Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM;
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
