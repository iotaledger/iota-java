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

    public static boolean isTrytes(final String trytes, final int length) {
        return trytes.matches("^[A-Z9]{" + (length == 0 ? "0," : length) + "}$");
    }

    public static boolean isArrayOfHashes(String[] hashes) {
        if (hashes == null) return false;

        for (int i = 0; i < hashes.length; i++) {
            String hash = hashes[i];

            // Check if address with checksum
            if (hash.length() == 90) {
                if (!isTrytes(hash, 90)) {
                    return false;
                }
            } else {
                if (!isTrytes(hash, 81)) {
                    return false;
                }
            }
        }
        return true;

    }
}
