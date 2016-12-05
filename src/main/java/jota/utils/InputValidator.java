package jota.utils;

/**
 * Created by pinpong on 02.12.16.
 */
public class InputValidator {

    public static boolean isAddress(String address) {
        return (address.length() == Constants.addressLengthWithoutChecksum ||
                address.length() == Constants.addressLengthWithChecksum) && isTrytes(address, address.length());
    }

    public static boolean checkAddress(String address) {
        if (!isAddress(address)) {
            throw new RuntimeException("Invalid address: " + address);
        }
        return true;
    }

    public static boolean isTrytes(String trytes, int length) {
        return trytes.matches("^[A-Z9]{" + (length == 0 ? "0," : length) + "}$");
    }
}
