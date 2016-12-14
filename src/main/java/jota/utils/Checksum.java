package jota.utils;

import org.apache.commons.lang3.StringUtils;

import jota.pow.Curl;

/**
 * Created by pinpong on 02.12.16.
 */
public class Checksum {

    public static String addChecksum(String address) {
        InputValidator.checkAddress(address);
        String addressWithChecksum = address;
        addressWithChecksum += calculateChecksum(address);
        return addressWithChecksum;
    }

    public static String removeChecksum(String addressWithChecksum) {
        if (isAddressWithChecksum(addressWithChecksum)) {
            return getAddress(addressWithChecksum);
        }
        return StringUtils.EMPTY;
    }

    private static String getAddress(String addressWithChecksum) {
        return addressWithChecksum.substring(0, Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM);
    }

    public static boolean isValidChecksum(String addressWithChecksum) {
        String addressWithoutChecksum = removeChecksum(addressWithChecksum);
        String addressWithRecalculateChecksum = addressWithChecksum += calculateChecksum(addressWithoutChecksum);
        return addressWithRecalculateChecksum.equals(addressWithChecksum);
    }

    private static boolean isAddressWithChecksum(String addressWithChecksum) {
        return InputValidator.checkAddress(addressWithChecksum) && addressWithChecksum.length() == Constants.ADDRESS_LENGTH_WITH_CHECKSUM;
    }

    public static String calculateChecksum(String address) {
        Curl curl = new Curl();
        curl.reset();
        curl.setState(Converter.copyTrits(address, curl.getState()));
        curl.transform();
        return Converter.trytes(curl.getState()).substring(0, 9);
    }
}
