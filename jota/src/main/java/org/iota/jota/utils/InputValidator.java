package org.iota.jota.utils;

import org.apache.commons.lang3.math.NumberUtils;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Transfer;

import static org.iota.jota.utils.Constants.INVALID_ADDRESSES_INPUT_ERROR;
import static org.iota.jota.utils.Constants.INVALID_TRANSFERS_INPUT_ERROR;

import java.util.List;

/**
 * 
 * This class provides methods to validate the parameters of different iota API methods
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
     * Determines whether the specified addresses are valid.
     *
     * @param addresses The address list to validate.
     * @return <code>true</code> if the specified addresses are valid; otherwise, <code>false</code>.
     **/
    public static boolean isAddressesCollectionValid(List<String> addresses) throws ArgumentException {
        for (String address : addresses) {
            if (!checkAddress(address)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Determines whether the specified addresses are valid.
     *
     * @param addresses The address array to validate.
     * @return <code>true</code> if the specified addresses are valid; otherwise, <code>false</code>.
     **/
    public static boolean isAddressesArrayValid(String[] addresses) throws ArgumentException {
        for (String address : addresses) {
            if (!checkAddress(address)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the specified address is an address and throws and exception if the address is invalid.
     *
     * @param address The address to validate.
     * @return <code>true</code> if the specified string is an address; otherwise, <code>false</code>.
     * @throws ArgumentException is thrown when the specified input is not valid.
     **/
    public static boolean checkAddress(String address) throws ArgumentException {
        if (!isAddress(address)) {
            throw new ArgumentException(INVALID_ADDRESSES_INPUT_ERROR);
        }
        return true;
    }

    /**
     * Determines whether the specified string contains only characters from the trytes alphabet (see <see cref="Constants.TryteAlphabet"/>).
     *
     * @param trytes The trytes to validate.
     * @return <code>true</code> if the specified trytes are trytes otherwise, <code>false</code>.
     **/
    public static boolean isTrytes(String trytes) {
        return isTrytes(trytes, trytes.length());
    }
    
    /**
     * Determines whether the specified string contains only characters from the trytes alphabet (see <see cref="Constants.TryteAlphabet"/>).
     *
     * @param trytes The trytes to validate.
     * @param length The length.
     * @return <code>true</code> if the specified trytes are trytes otherwise, <code>false</code>.
     **/
    public static boolean isTrytes(String trytes, int length) {
        return trytes.matches("^[A-Z9]{" + (length == 0 ? "0," : length) + "}$");
    }

    /**
     * Determines whether the specified string consist only of '9'.
     *
     * @param trytes The trytes to validate.
     * @param length The length.
     * @return <code>true</code> if the specified string consist only of '9'; otherwise, <code>false</code>.
     **/
    public static boolean isNinesTrytes(String trytes, int length) {
        return trytes.matches("^[9]{" + (length == 0 ? "0," : length) + "}$");
    }

    /**
     * Determines whether the specified string represents a signed integer.
     *
     * @param value The value to validate.
     * @return <code>true</code> the specified string represents an integer value; otherwise, <code>false</code>.
     **/
    public static boolean isValue(String value) {
        return NumberUtils.isCreatable(value);
    }

    /**
     * Determines whether the specified string array contains only trytes, and 
     * Deprecated - Use isArrayOfRawTransactionTrytes
     * @param trytes The trytes array to validate.
     * @return <code>true</code> if the specified array contains only valid trytes otherwise, <code>false</code>.
     **/
    @Deprecated
    public static boolean isArrayOfTrytes(String[] trytes) {
        return isArrayOfTrytes(trytes, Constants.TRANSACTION_LENGTH);
    }
    
    /**
     * Determines whether the specified string array contains only trytes, and 
     *
     * @param trytes The trytes array to validate.
     * @return <code>true</code> if the specified array contains only valid trytes otherwise, <code>false</code>.
     **/
    public static boolean isArrayOfRawTransactionTrytes(String[] trytes) {
        return isArrayOfTrytes(trytes, Constants.TRANSACTION_LENGTH);
    }
    
    /**
     * Determines whether the specified string array contains only trytes.
     *
     * @param trytes The trytes array to validate.
     * @param length The length each String should be
     * @return <code>true</code> if the specified array contains only valid trytes otherwise, <code>false</code>.
     **/
    public static boolean isArrayOfTrytes(String[] trytes, int length) {
        for (String tryte : trytes) {
            if (!isTrytes(tryte, length)) {
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
            if (hash.length() == Constants.ADDRESS_LENGTH_WITH_CHECKSUM) {
                if (!isTrytes(hash, Constants.ADDRESS_LENGTH_WITH_CHECKSUM)) {
                    return false;
                }
            } else {
                if (!isTrytes(hash, Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Checks if the array is not null and not empty
     * @param data The array with data
     * @return <code>true</code> if the array is valid; otherwise, <code>false</code>.
     */
    public static boolean isStringArrayValid(String[] data) {
        return null != data && 0 != data.length;
    }

    /**
     * Determines whether the specified transfers are valid.
     *
     * @param transfers The transfers list to validate.
     * @return <code>true</code> if the specified transfers are valid; otherwise, <code>false</code>.
     **/
    public static boolean isTransfersCollectionValid(List<Transfer> transfers) throws ArgumentException {

        // Input validation of transfers object
        if (transfers == null || transfers.isEmpty()) {
            throw new ArgumentException(INVALID_TRANSFERS_INPUT_ERROR);
        }

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
    public static boolean isValidTransfer(Transfer transfer) {

        if (transfer == null) {
            return false;
        }

        if (!isAddress(transfer.getAddress())) {
            return false;
        }

        // Check if message is correct trytes encoded of any length
        if (transfer.getMessage() == null || !isTrytes(transfer.getMessage(), transfer.getMessage().length())) {
            return false;
        }

        // Check if tag is correct trytes encoded and not longer than 27 trytes
        if (!isTag(transfer.getTag())) {
            return false;
        }

        return true;
    }
    
    /**
     * Checks if the seed is valid.
     * 
     * @param tag The tag to validate.
     * @return <code>true</code> if the specified tag is valid; otherwise, <code>false</code>.
     */
    public static boolean isTag(String tag) {
        return tag != null & isTrytes(tag, tag.length()) && tag.length() <= Constants.TAG_LENGTH;
    }

    /**
     * Checks if the seed is valid.
     *
     * @param seed The seed to validate.
     * @return <code>true</code> if the specified seed is valid; otherwise, <code>false</code>.
     **/
    public static boolean isValidSeed(String seed) {
        return isTrytes(seed, Constants.SEED_LENGTH_MAX);
    }

    /**
     * Checks if input is correct hashes.
     *
     * @param hashes The hashes list to validate.
     * @return <code>true</code> if the specified hashes are valid; otherwise, <code>false</code>.
     **/
    public static boolean isHashes(List<String> hashes) {
        for (String hash : hashes) {
            if (!isHash(hash)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if input is correct hash.
     *
     * @param hash The hash to validate.
     * @return <code>true</code> if the specified hash are valid; otherwise, <code>false</code>.
     **/
    public static boolean isHash(String hash) {
        if (!isTrytes(hash, Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM)) {
            return false;
        }
        return true;
    }
    
    /**
     * Checks if attached trytes if last 241 trytes are non-zero
     * the last 243 trytes basically consist of the: trunkTransaction + branchTransaction + nonce
     * @param trytes The trytes.
     * @return <code>true</code> if the specified trytes are valid/non 9s; otherwise, <code>false</code>.
     **/
    public static boolean isArrayOfAttachedTrytes(String[] trytes) {
        for (String tryteValue : trytes) {

            // Check if correct 2673 trytes
            if (!isTrytes(tryteValue, Constants.TRANSACTION_LENGTH)) {
                return false;
            }

            String lastTrytes = tryteValue.substring(
                    Constants.TRANSACTION_LENGTH - (
                            3 * Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM)
                    );

            if (isNinesTrytes(lastTrytes, lastTrytes.length())) {
                return false;
            }
        }

        return true;
    }
    
    /**
     * Checks if the security level is valid
     * @param level the level
     * @return <code>true</code> if the level is between 1 and 3(inclusive); otherwise, <code>false</code>.
     */
    public static boolean isValidSecurityLevel(int level) {
       return level >= Constants.MIN_SECURITY_LEVEL && level <= Constants.MAX_SECURITY_LEVEL;
    }
}
