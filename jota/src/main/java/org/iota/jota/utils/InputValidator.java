package org.iota.jota.utils;

import static org.iota.jota.utils.Constants.INVALID_ADDRESSES_INPUT_ERROR;
import static org.iota.jota.utils.Constants.INVALID_TRANSFERS_INPUT_ERROR;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.math.NumberUtils;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Input;
import org.iota.jota.model.Transfer;

/**
 * 
 * This class provides methods to validate the parameters of different iota API methods
 */
public class InputValidator {

    /**
     * Determines whether the specified string is an address. 
     * Address must contain a checksum to be valid
     *
     * @param address The address to validate.
     * @return <code>true</code> if the specified string is an address; otherwise, <code>false</code>.
     **/
    public static boolean isAddress(String address) {
        return address.length() == Constants.ADDRESS_LENGTH_WITH_CHECKSUM && isTrytes(address);
    }
    
    /**
     * Determines whether the specified string is an address without checksum. 
     *
     * @param address The address to validate.
     * @return <code>true</code> if the specified string is an address; otherwise, <code>false</code>.
     **/
    public static boolean isAddressWithoutChecksum(String address) {
        return isTrytes(address, Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM);
    }

    /**
     * According to the following issue:
     * https://github.com/iotaledger/trinity-wallet/issues/866
     * 
     * This is because Kerl addresses always are with a 0 trit on the end.
     * So we validate if we actually send to a proper address, to prevent having to double spent
     * 
     * @param trytes The trytes to check
     * @return <code>true</code> if the specified trytes end with 0, otherwise <code>false</code>.
     */
    public static boolean hasTrailingZeroTrit(String trytes) {
       int[] trits = Converter.trits(trytes);
       return trits[trits.length - 1] == 0;
    }

    /**
     * Determines whether the specified addresses are valid.
     * Addresses must contain a checksum to be valid.
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
     * Addresses must contain a checksum to be valid.
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
     * Addresses must contain a checksum to be valid.
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
     * Checks whether the specified address is an address without checksum 
     * and throws and exception if the address is invalid.
     * 
     * @param address The address to validate.
     * @return <code>true</code> if the specified string is an address; otherwise, <code>false</code>.
     * @throws ArgumentException when the specified input is not valid.
     **/
    public static boolean checkAddressWithoutChecksum(String address) throws ArgumentException {
        if (!isTrytes(address, Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM)) {
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
    public static boolean isTrytesOfExactLength(String trytes, int length) {
        return trytes.matches("^[A-Z9]{" + (length == 0 ? "0," : length + ",") + "}$");
    }

    /**
     * Determines whether the specified string contains only characters from the trytes alphabet (see <see cref="Constants.TryteAlphabet"/>).
     * Alias of {@link #isTrytesOfExactLength(String, int)}
     *
     * @param trytes The trytes to validate.
     * @param length The length.
     * @return <code>true</code> if the specified trytes are trytes otherwise, <code>false</code>.
     **/
    public static boolean isTrytes(String trytes, int length) {
        return isTrytesOfExactLength(trytes, length);
    }

    /**
     * Determines whether the specified string consist only of '9'.
     *
     * @param trytes The trytes to validate.
     * @param length The length.
     * @return <code>true</code> if the specified string consist only of '9'; otherwise, <code>false</code>.
     **/
    public static boolean isNinesTrytes(String trytes, int length) {
        return trytes.matches("^[9]{" + (length == 0 ? "0," : length + ",") + "}$");
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
     * Determines whether the specified string array contains only trytes
     *
     * @param trytes The trytes array to validate.
     * @return <code>true</code> if the specified array contains only valid trytes otherwise, <code>false</code>.
     **/
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
        for (String tr : trytes) {
            // This part of the value trits exceed iota max supply when used
            if (!InputValidator.isNinesTrytes(tr.substring(2279, 2295), 16)) {
                return false;
            }
        }
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
     * Checks if the tag is valid.
     * Alias of {@link #isValidTag(String)}
     * 
     * @param tag The tag to validate.
     * @return <code>true</code> if the specified tag is valid; otherwise, <code>false</code>.
     */
    public static boolean isTag(String tag) {
        return isValidTag(tag);
    }
    
    /**
     * Checks if the tag is valid. The string must not be empty and must contain trytes.
     * 
     * @param tag The tag to validate.
     * @return <code>true</code> if the specified tag is valid; otherwise, <code>false</code>.
     * @see #isTrytes(String)
     */
    public static boolean isValidTag(String tag) {
        return tag != null && tag.length() <= Constants.TAG_LENGTH && isTrytes(tag);
    }

    /**
     * Checks if the tags are valid.
     *
     * @param tags The tags to validate.
     * @return <code>true</code> if all the tags are valid; otherwise, <code>false</code>.
     */
    public static boolean areValidTags(String... tags) {
        for (String tag : tags){
            if (!isValidTag(tag)){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the inputs are valid. 
     *
     * @param inputs The inputs to validate.
     * @return <code>true</code> if the specified inputs are valid; otherwise, <code>false</code>.
     **/
    public static boolean areValidInputsList(List<Input> inputs){
        return areValidInputs(inputs.toArray(new Input[inputs.size()]));
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
     * @param seed The seed to validate.
     * @return <code>true</code> if the specified seed is valid; otherwise, <code>false</code>.
     **/
    public static boolean isValidSeed(String seed) {
        return seed.length() <= Constants.SEED_LENGTH_MAX && isTrytes(seed);
    }

    /**
     * Checks that the specified seed reference is a valid seed.
     *
     * @param seed The seed to validate.
     * @return {@code seed} if valid seed
     * @throws IllegalArgumentException if {@code seed} is invalid
     * @see #isValidSeed(String)
     */
    public static String requireValidSeed(String seed) {
        if (isValidSeed(seed)) {
            return seed;
        }
        throw new IllegalArgumentException("Provided seed is invalid.");
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
     * Determines whether the specified array contains only valid hashes.
     *
     * @param hashes The hashes list to validate.
     * @return <code>true</code> if the specified hashes are valid; otherwise, <code>false</code>.
     **/
    public static boolean isHashes(List<String> hashes) {
        if (null == hashes) {
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
        if (hash.length() == Constants.ADDRESS_LENGTH_WITH_CHECKSUM) {
            if (!isTrytes(hash, Constants.ADDRESS_LENGTH_WITH_CHECKSUM)) {
                return false;
            }
        } else {
            if (!isTrytes(hash, Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM)) {
                return false;
            }
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

    /**
     * Checks that the specified security level reference is a valid security level.
     *
     * @param securityLevel The security level to validate.
     * @return {@code securityLevel} if valid security level
     * @throws IllegalArgumentException if {@code securityLevel} is invalid
     * @see #isValidSecurityLevel(int)
     */
    public static int requireValidSecurityLevel(int securityLevel) {
        if (isValidSecurityLevel(securityLevel)) {
            return securityLevel;
        }
        throw new IllegalArgumentException("Value is not within the specified security level range");
    }

    public static boolean isTrits(int[] trits) {
        Range<Integer> myRange = Range.between(-1, 1);
        IntPredicate contains = myRange::contains;
        
        long min = Arrays.stream(trits)
                .filter(contains.negate())
                .count(); 

        return min == 0;
    }
    
    public static boolean isTrits(List<Integer> trits) {
        Range<Integer> myRange = Range.between(-1, 1);
        IntPredicate contains = myRange::contains;
        
        long min = trits.stream()
                .filter(integer -> contains.negate().test(integer))
                .count(); 

        return min == 0;
    }
}
