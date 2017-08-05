package jota.utils;

import jota.error.InvalidAddressException;
import jota.model.Transfer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;

/**
 * This class provides methods to validate the parameters of different iota API methods.
 *
 * @author pinpong
 */
public class InputValidator {

    /**
     * Determines whether the specified string is an address.
     *
     * @param address The address to validate.
     * @return <code>true</code> if the specified string is an address; otherwise, <code>false</code>.
     **/
    public static boolean isAddress(String address) {
        return (address.length() == Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM ||
                address.length() == Constants.ADDRESS_LENGTH_WITH_CHECKSUM) && isTrytes(address, address.length());
    }

    /**
     * Checks whether the specified address is an address and throws and exception if the address is invalid.
     *
     * @param address The address to validate.
     * @return <code>true</code> if the specified string is an address; otherwise, <code>false</code>.
     * @throws InvalidAddressException is thrown when the specified address is not an valid address.
     **/
    public static boolean checkAddress(String address) throws InvalidAddressException {
        if (!isAddress(address)) {
            throw new InvalidAddressException();
        }
        return true;
    }

    /**
     * Determines whether the specified string contains only characters from the trytes alphabet (see <see cref="Constants.TryteAlphabet"/>).
     *
     * @param trytes The trytes to validate.
     * @param length The length.
     * @return <code>true</code> if the specified trytes are trytes otherwise, <code>false</code>.
     **/
    public static boolean isTrytes(final String trytes, final int length) {
        return trytes.matches("^[A-Z9]{" + (length == 0 ? "0," : length) + "}$");
    }

    /**
     * Determines whether the specified string consist only of '9'.
     *
     * @param trytes The trytes to validate.
     * @param length The length.
     * @return <code>true</code> if the specified string consist only of '9'; otherwise, <code>false</code>.
     **/
    public static boolean isNinesTrytes(final String trytes, final int length) {
        return trytes.matches("^[9]{" + (length == 0 ? "0," : length) + "}$");
    }

    /**
     * Determines whether the specified string represents a signed integer.
     *
     * @param value The value to validate.
     * @return <code>true</code> the specified string represents an integer value; otherwise, <code>false</code>.
     **/
    public static boolean isValue(final String value) {
        return NumberUtils.isNumber(value);
    }

    /**
     * Determines whether the specified string array contains only trytes.
     *
     * @param trytes The trytes array to validate.
     * @return <code>true</code> if the specified array contains only valid trytes otherwise, <code>false</code>.
     **/
    public static boolean isArrayOfTrytes(String[] trytes) {
        for (String tryte : trytes) {
            // Check if correct 2673 trytes
            if (!isTrytes(tryte, 2673)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines whether the specified array contains only valid hashes.
     *
     * @param hashes The hashes array to validate.
     * @return <code>true</code> the specified array contains only valid hashes; otherwise, <code>false</code>.
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
     * Determines whether the specified transfers are valid.
     *
     * @param transfers The transfers list to validate.
     * @return <code>true</code> if the specified transfers are valid; otherwise, <code>false</code>.
     **/
    public static boolean isTransfersCollectionValid(final List<Transfer> transfers) {

        for (final Transfer transfer : transfers) {
            if (!isValidTransfer(transfer)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines whether the specified transfer is valid.
     *
     * @param transfer The transfer to validate.
     * @return <code>true</code> if the specified transfer is valid; otherwise, <code>false</code>>.
     **/
    public static boolean isValidTransfer(final Transfer transfer) {

        if (!isAddress(transfer.getAddress())) {
            return false;
        }

        // Check if message is correct trytes of any length
        if (!isTrytes(transfer.getMessage(), 0)) {
            return false;
        }

        if (null == transfer.getTag() || transfer.getTag().isEmpty()) {
            return true;
        } else {
            // Check if tag is correct trytes of {0,27} trytes
            return isTrytes(transfer.getTag(), 27);
        }
    }

    /**
     * Checks if the seed is valid. If not, an exception is thrown.
     *
     * @param seed The seed to validate.
     * @return The validated seed.
     * @throws IllegalStateException Format not in trytes or Invalid Seed: Seed too long.
     **/
    public static String validateSeed(String seed) {
        if (seed.length() > 81)
            throw new IllegalStateException("Invalid Seed: Seed too long");

        if (!isTrytes(seed, seed.length()))
            throw new IllegalStateException("Invalid Seed: Format not in trytes");

        seed = StringUtils.rightPad(seed, 81, '9');

        return seed;
    }
}
