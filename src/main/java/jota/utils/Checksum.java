package jota.utils;

import jota.error.InvalidAddressException;
import jota.pow.JCurl;

/**
 * This class defines utility methods to add/remove the checksum to/from an address
 * @author pinpong
 */
public class Checksum {

    /**
     * adds the checksum to the specified address
     *
     * @param address address without checksum
     * @return the address with the appended checksum
     **/
    public static String addChecksum(String address) throws InvalidAddressException {
        InputValidator.checkAddress(address);
        String addressWithChecksum = address;
        addressWithChecksum += calculateChecksum(address);
        return addressWithChecksum;
    }

    /**
     * remove the checksum to the specified address
     *
     * @param address address with checksum
     * @return the address without checksum
     **/
    public static String removeChecksum(String address) throws InvalidAddressException {
        if (isAddressWithChecksum(address)) {
            return getAddress(address);
        }
        throw new InvalidAddressException();
    }

    /**
     * remove checksum length
     *
     * @param addressWithChecksum address with checksum
     * @return address without checksum
     **/
    private static String getAddress(String addressWithChecksum) {
        return addressWithChecksum.substring(0, Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM);
    }

    /**
     * check if checksum is valid
     *
     * @param addressWithChecksum address
     * @return boolean
     **/
    public static boolean isValidChecksum(String addressWithChecksum) throws InvalidAddressException {
        String addressWithoutChecksum = removeChecksum(addressWithChecksum);
        String addressWithRecalculateChecksum = addressWithChecksum += calculateChecksum(addressWithoutChecksum);
        return addressWithRecalculateChecksum.equals(addressWithChecksum);
    }


    /**
     * check if specified address is a address with checksum
     *
     * @param address address
     * @return boolean
     **/
    public static boolean isAddressWithChecksum(String address) throws InvalidAddressException {
        return InputValidator.checkAddress(address) && address.length() == Constants.ADDRESS_LENGTH_WITH_CHECKSUM;
    }


    /**
     * check if specified address is a address
     *
     * @param address address
     * @return boolean
     **/
    public static boolean isAddressWithoutChecksum(String address) throws InvalidAddressException {
        return InputValidator.checkAddress(address) && address.length() == Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM;
    }

    /**
     * calculate the checksum to the specified address
     *
     * @param address address
     * @return the checksum
     **/
    private static String calculateChecksum(String address) {
        JCurl curl = new JCurl();
        curl.reset();
        curl.setState(Converter.copyTrits(address, curl.getState()));
        curl.transform();
        return Converter.trytes(curl.getState()).substring(0, 9);
    }
}
