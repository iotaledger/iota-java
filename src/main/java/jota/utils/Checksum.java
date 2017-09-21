package jota.utils;

import jota.error.InvalidAddressException;
import jota.pow.ICurl;
import jota.pow.JCurl;
import jota.pow.SpongeFactory;

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
     * @throws InvalidAddressException is thrown when the specified address is not an valid address.
     **/
    public static String addChecksum(String address) throws InvalidAddressException {
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
     * @throws InvalidAddressException is thrown when the specified address is not an address with checksum.
     **/
    public static String removeChecksum(String address) throws InvalidAddressException {
        if (isAddressWithChecksum(address)) {
            return removeChecksumFromAddress(address);
        } else if (isAddressWithoutChecksum(address)) {
            return address;
        }
        throw new InvalidAddressException();
    }

    private static String removeChecksumFromAddress(String addressWithChecksum) {
        return addressWithChecksum.substring(0, Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM);
    }

    /**
     * Determines whether the specified address with checksum has a valid checksum.
     *
     * @param addressWithChecksum The address with checksum.
     * @return <code>true</code> if the specified address with checksum has a valid checksum [the specified address with checksum]; otherwise, <code>false</code>.
     * @throws InvalidAddressException is thrown when the specified address is not an valid address.
     **/
    public static boolean isValidChecksum(String addressWithChecksum) throws InvalidAddressException {
        String addressWithoutChecksum = removeChecksum(addressWithChecksum);
        String addressWithRecalculateChecksum = addressWithoutChecksum += calculateChecksum(addressWithoutChecksum);
        return addressWithRecalculateChecksum.equals(addressWithChecksum);
    }


    /**
     * Check if specified address is a address with checksum.
     *
     * @param address The address to check.
     * @return <code>true</code> if the specified address is with checksum ; otherwise, <code>false</code>.
     * @throws InvalidAddressException is thrown when the specified address is not an valid address
     **/
    public static boolean isAddressWithChecksum(String address) throws InvalidAddressException {
        return InputValidator.checkAddress(address) && address.length() == Constants.ADDRESS_LENGTH_WITH_CHECKSUM;
    }


    /**
     * check if specified address is a address
     *
     * @param address address
     * @return boolean
     * @throws InvalidAddressException is thrown when the specified address is not an valid address
     **/
    public static boolean isAddressWithoutChecksum(String address) throws InvalidAddressException {
        return InputValidator.checkAddress(address) && address.length() == Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM;
    }

    private static String calculateChecksum(String address) {
        ICurl curl = SpongeFactory.create(SpongeFactory.Mode.KERL);
        curl.reset();
        curl.absorb(Converter.trits(address));
        int[] checksumTrits = new int[JCurl.HASH_LENGTH];
        curl.squeeze(checksumTrits);
        String checksum = Converter.trytes(checksumTrits);
        String checksumPrt = checksum.substring(72, 81);
        return checksumPrt;
    }
}
