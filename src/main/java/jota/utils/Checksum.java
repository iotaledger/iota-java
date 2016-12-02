package jota.utils;

import org.apache.commons.lang3.NotImplementedException;

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
        throw new RuntimeException("Invalid address: " + addressWithChecksum);
    }

    private static String getAddress(String addressWithChecksum) {
        return addressWithChecksum.substring(0, Constants.addressLengthWithoutChecksum);
    }

    public static boolean isValidChecksum(String addressWithChecksum) {
        String addressWithoutChecksum = removeChecksum(addressWithChecksum);
        String adressWithRecalculateChecksum = calculateChecksum(addressWithoutChecksum);

        return adressWithRecalculateChecksum.equals(addressWithChecksum);
    }

    private static boolean isAddressWithChecksum(String addressWithChecksum) {
        return InputValidator.checkAddress(addressWithChecksum) && addressWithChecksum.length() == Constants.addressLengthWithChecksum;
    }

    private static String calculateChecksum(String address) {
        Curl curl = new Curl();
        curl.reset();
        curl.setState(Converter.copyTrits(address, curl.getState()));
        curl.transform();
        String checksum = Converter.trytes(curl.getState()).substring(0, 9);
        return checksum;
    }
}
