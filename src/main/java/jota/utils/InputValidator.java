package jota.utils;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import jota.model.Transaction;
import jota.model.Transfer;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Created by pinpong on 02.12.16.
 */
public class InputValidator {

    public static boolean isAddress(String address) {
        return (address.length() == Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM ||
                address.length() == Constants.ADDRESS_LENGTH_WITH_CHECKSUM) && isTrytes(address, address.length());
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

    public static boolean isValue(final String value) {
        return NumberUtils.isNumber(value);
    }

    public static boolean isArrayOfHashes(String[] hashes) {
        if (hashes == null)
            return false;

        for (String hash : hashes) {
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

    /**
     * checks if input is correct hash collections
     *
     * @param {array} hash
     * @method isTransfersArray
     * @returns {boolean}
     **/
    public static boolean isTransfersCollectionCorrect(final List<Transfer> transfers) {

        for (final Transfer transfer : transfers) {
            if (!isTransfersArray(transfer)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isTransfersArray(final Transfer transfer) {

        if (!isAddress(transfer.getAddress())) {
            return false;
        }

        // Check if message is correct trytes of any length
        if (!isTrytes(transfer.getMessage(), 0)) {
            return false;
        }

        // Check if tag is correct trytes of {0,27} trytes
        if (!isTrytes(transfer.getTag(), 27)) {
            return false;
        }

        return true;
    }

    public static String validateSeed(String seed) {
        if (seed.length() > 81) return null;
        while (seed.length() < 81) seed += 9;
        return seed;
    }
}
