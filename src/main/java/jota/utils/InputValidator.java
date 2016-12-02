package jota.utils;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import jota.model.Transfer;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Arrays;

/**
 * Created by pinpong on 02.12.16.
 */
public class InputValidator {

    public static boolean isAddress(String address) {
        // TODO: In the future check checksum
        // Check if address with checksum
        return (address.length() == Constants.addressLengthWithoutChecksum ||
                address.length() == Constants.addressLengthWithChecksum) && isTrytes(address, address.length());
    }


    public static boolean checkAddress(String address) {
        if (!isAddress(address))
            throw new RuntimeException("Invalid address: " + address);
        return true;
    }

    public static boolean isTrytes(String trytes, int length) {

        // If no length specified, just validate the trytes

        Regex regexTrytes = new Regex("^[9A-Z]{" + (length == 0 ? "0," : length) + "}$");
        return trytes.matches(regexTrytes.toString());
    }
}
