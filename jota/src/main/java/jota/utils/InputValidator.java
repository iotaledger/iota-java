package jota.utils;

import jota.error.ArgumentException;
import jota.model.Input;
import jota.model.Transfer;
import org.apache.commons.lang3.math.NumberUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static jota.utils.Constants.INVALID_ADDRESSES_INPUT_ERROR;
import static jota.utils.Constants.INVALID_TRANSFERS_INPUT_ERROR;

/**
 * This class provides methods to validate the parameters of different iota API methods.
 *
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
                address.length() == Constants.ADDRESS_LENGTH_WITH_CHECKSUM) && isTrytes(address);
    }

    /**
     * Determines whether the specified addresses are valid.
     *
     * @param addresses The address list to validate.
     * @return <code>true</code> if the specified addresses are valid; otherwise, <code>false</code>.
     **/
    public static boolean isAddressesCollectionValid(final List<String> addresses) throws ArgumentException {
        for (final String address : addresses) {
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
     * @throws ArgumentException when the specified input is not valid.
     **/
    public static boolean checkAddress(String address) throws ArgumentException {
        if (!isAddress(address)) {
            throw new ArgumentException(INVALID_ADDRESSES_INPUT_ERROR);
        }
        return true;
    }

    /**
     * Determines whether the specified string contains only characters from the trytes alphabet
     * @param trytes The trytes to validate.
     * @return <code>true</code> if the specified trytes are trytes, otherwise <code>false</code>.
     */
    public static boolean isTrytes(String trytes) {
        return isTrytesOfExactLength(trytes, 0);
    }
    
    /**
     * Determines whether the specified string contains only characters from the trytes alphabet.
     *
     * @param trytes The trytes to validate.
     * @param length The length.
     * @return <code>true</code> if the specified trytes are trytes and have the correct size, otherwise <code>false</code>.
     **/
    public static boolean isTrytesOfExactLength(String trytes, int length) {
        if (length < 0 ) {
            return false;
        }
        
        return trytes.matches("^[A-Z9]{" + (length == 0 ? "0," : length) + "}$");
    }
    
    /**
     * Determines whether the specified string contains only characters from the trytes alphabet 
     * and has a maximum (including) of the provided length
     * @param trytes The trytes to validate.
     * @param maxLength The length.
     * @return <code>true</code> if the specified trytes are trytes and have the correct size, otherwise <code>false</code>.
     */
    public static boolean isTrytesOfMaxLength(String trytes, int maxLength) {
        if (trytes.length() > maxLength) {
            return false;
        }
        
        return isTrytesOfExactLength(trytes, 0);
    }
    
    /**
     * Determines whether the specified string consist only of '9'.
     *
     * @param trytes The trytes to validate.
     * @return <code>true</code> if the specified string consist only of '9'; otherwise, <code>false</code>.
     **/
    public static boolean isEmptyTrytes(String trytes) {
        return isNinesTrytes(trytes, 0);
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
        return NumberUtils.isCreatable(value);
    }

    /**
     * Deprecated due to ambigue function name, please switch to {@link #areTransactionTrytes}
     * Determines whether the specified string array contains only trytes of a transaction length
     * @param trytes The trytes array to validate.
     * @return <code>true</code> if the specified array contains only valid trytes otherwise, <code>false</code>.
     **/
    @Deprecated
    public static boolean isArrayOfTrytes(String[] trytes) {
        for (String tryte : trytes) {
            // Check if correct 2673 trytes
            if (!isTrytesOfExactLength(tryte, Constants.TRANSACTION_SIZE)) {
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
        if (hashes == null) {
            return false;
        }

        for (String hash : hashes) {
            if (!isHash(hash)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines whether the specified transfers are valid.
     *
     * @param transfers The transfers list to validate.
     * @return <code>true</code> if the specified transfers are valid; otherwise, <code>false</code>.
     * @throws ArgumentException when the specified input is not valid.
     **/
    public static boolean isTransfersCollectionValid(final List<Transfer> transfers) throws ArgumentException {

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
     * @return <code>true</code> if the specified transfer is valid; otherwise, <code>false</code>.
     **/
    public static boolean isValidTransfer(final Transfer transfer) {

        if (transfer == null) {
            return false;
        }

        if (!isAddress(transfer.getAddress())) {
            return false;
        }

        // Check if message is correct trytes encoded of any length
        if (transfer.getMessage() == null || !isTrytesOfExactLength(transfer.getMessage(), transfer.getMessage().length())) {
            return false;
        }

        // Check if tag is correct trytes encoded and not longer than 27 trytes
        return isValidTag(transfer.getTag());
    }
    
    /**
     * Checks if the tags are valid. 
     *
     * @param tags The tags to validate.
     * @return <code>true</code> if the specified tags are valid; otherwise, <code>false</code>.
     **/
    public static boolean areValidTags(String... tags) {
     // Input validation of tags
        if (tags == null || tags.length == 0) {
            return false;
        }

        for (final String tag : tags) {
            if (!isValidTag(tag)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if the tag is valid. 
     *
     * @param tag The tag to validate.
     * @return <code>true</code> if the specified tag is valid; otherwise, <code>false</code>.
     **/
    public static boolean isValidTag(String tag) {
        return tag != null && tag.length() <= Constants.TAG_LENGTH && isTrytes(tag);
    }
    
    /**
     * Checks if the inputs are valid. 
     *
     * @param inputs The inputs to validate.
     * @return <code>true</code> if the specified inputs are valid; otherwise, <code>false</code>.
     **/
    public static boolean areValidInputs(Input... inputs){
        // Input validation of input objects
        if (inputs == null || inputs.length == 0) {
            return false;
        }

        for (final Input input : inputs) {
            if (!isValidInput(input)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if the input is valid. 
     *
     * @param input The input to validate.
     * @return <code>true</code> if the specified input is valid; otherwise, <code>false</code>.
     **/
    public static boolean isValidInput(Input input){
        if (input == null) {
            return false;
        }

        if (!isAddress(input.getAddress())) {
            return false;
        }

        if (input.getKeyIndex() < 0) {
            return false;
        }
        
        return isValidSecurityLevel(input.getSecurity());
    }

    /**
     * Checks if the seed is valid.
     *
     * @param seed The input to validate.
     * @return <code>true</code> if the specified input is valid; otherwise, <code>false</code>.
     **/
    public static boolean isValidSeed(String seed) {
        if (seed.length() > Constants.SEED_LENGTH_MAX) {
            return false;
        }
        
        return isTrytes(seed);
    }

    /**
     * Checks if input is correct hashes.
     *
     * @param hashes The hashes list to validate.
     * @return <code>true</code> if the specified hashes are valid; otherwise, <code>false</code>.
     **/
    public static boolean isHashes(List<String> hashes) {
        if (hashes == null || hashes.size() == 0) {
            return false;
        }
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
     // Check if address with checksum
        if (hash.length() == Constants.ADDRESS_LENGTH_WITH_CHECKSUM) {
            return isTrytesOfExactLength(hash, 0); //We already checked length
        } else {
            return isTrytesOfExactLength(hash, Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM);
        }
    }
    
    /**
     * Checks if the uris are valid. 
     *
     * @param uris The uris to validate.
     * @return <code>true</code> if the specified uris are valid; otherwise, <code>false</code>.
     **/
    public static boolean areValidUris(String... uris) {
        if (uris == null || uris.length == 0) {
            return false;
        }
        for (String uri : uris) {
            if (!isValidUri(uri)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if the uri is valid. 
     *
     * @param uri The uri to validate.
     * @return <code>true</code> if the specified uri is valid; otherwise, <code>false</code>.
     **/
    public static boolean isValidUri(String uri) {
        if (uri.length() < 7) {
            return false;
        }
        
        String protocol = uri.substring(0, 6);
        if (!"tcp://".equals(protocol) && !"udp://".equals(protocol)) {
            return false;
        }
        
        try {
            new URI(uri);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
    
    /**
      * Determines whether the specified string array contains only trytes of a transaction length
      * @param trytes The trytes array to validate.
      * @return <code>true</code> if the specified array contains only valid trytes otherwise, <code>false</code>.
      **/
    public static boolean areTransactionTrytes(String... trytes) {
        for (String tryteValue : trytes) {

            // Check if correct 2673 trytes
            if (!isTrytesOfExactLength(tryteValue, Constants.TRANSACTION_SIZE)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if attached trytes if last 241 trytes are non-zero
     *
     * @param trytes The trytes.
     * @return <code>true</code> if the specified trytes are valid; otherwise, <code>false</code>.
     **/
    public static boolean isArrayOfAttachedTrytes(String[] trytes) {

        for (String tryteValue : trytes) {

            // Check if correct 2673 trytes
            if (!isTrytesOfExactLength(tryteValue, Constants.TRANSACTION_SIZE)) {
                return false;
            }

            String lastTrytes = tryteValue.substring(Constants.TRANSACTION_SIZE - (3 * 81));

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
