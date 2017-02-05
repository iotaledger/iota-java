package jota.utils;

import jota.error.InvalidAddressException;
import jota.model.Transfer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;

/**
 * Created by pinpong on 02.12.16.
 */
public class InputValidator {

    /**
     * validates an address
     *
     * @param address address to validate
     * @return boolean
     **/
    public static boolean isAddress(String address) {
        return (address.length() == Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM ||
                address.length() == Constants.ADDRESS_LENGTH_WITH_CHECKSUM) && isTrytes(address, address.length());
    }

    /**
     * checks whether the specified address is an address
     *
     * @param address address to validate
     * @return boolean
     **/
    public static boolean checkAddress(String address) throws InvalidAddressException {
        if (!isAddress(address)) {
            throw new InvalidAddressException();
        }
        return true;
    }

    /**
     * checks if input is correct trytes consisting of A-Z9 optionally validates length
     *
     * @param trytes the trytes
     * @param length the length
     * @return boolean
     **/
    public static boolean isTrytes(final String trytes, final int length) {
        return trytes.matches("^[A-Z9]{" + (length == 0 ? "0," : length) + "}$");
    }

    /**
     * checks if input is correct trytes consisting of 9 optionally validates length
     *
     * @param trytes the trytes
     * @param length the length
     * @return boolean
     **/
    public static boolean isNinesTrytes(final String trytes, final int length) {
        return trytes.matches("^[9]{" + (length == 0 ? "0," : length) + "}$");
    }

    /**
     * determines whether the specified string represents a signed integer
     *
     * @param value the value
     * @return boolean
     **/
    public static boolean isValue(final String value) {
        return NumberUtils.isNumber(value);
    }

    /**
     * determines whether the specified string represents a signed integer
     *
     * @param trytes the trytes
     * @return boolean
     **/
    public static boolean isArrayOfTrytes(String[] trytes){
        for (String tryte : trytes) {
            // Check if correct 2673 trytes
            if (!isTrytes(tryte, 2673)) {
                return false;
            }
        }
        return true;
    }

    /**
     * checks if input is array of hashes
     *
     * @param hashes
     * @return boolean
     **/
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
     * @param transfers
     * @return boolean
     **/
    public static boolean isTransfersCollectionCorrect(final List<Transfer> transfers) {

        for (final Transfer transfer : transfers) {
            if (!isTransfer(transfer)) {
                return false;
            }
        }
        return true;
    }

    /**
     * checks if input is correct transfer
     *
     * @param transfer
     * @return boolean
     **/
    public static boolean isTransfer(final Transfer transfer) {

        if (!isAddress(transfer.getAddress())) {
            return false;
        }

        // Check if message is correct trytes of any length
        if (!isTrytes(transfer.getMessage(), 0)) {
            return false;
        }

        // Check if tag is correct trytes of {0,27} trytes
        return isTrytes(transfer.getTag(), 27);
    }

    /**
     * validate the seed
     *
     * @param seed the seed
     * @return validated seed
     **/
    public static String validateSeed(String seed) {
        if (seed.length() > 81)
            return null;

        seed = StringUtils.rightPad(seed, 81, '9');

        return seed;
    }
}
