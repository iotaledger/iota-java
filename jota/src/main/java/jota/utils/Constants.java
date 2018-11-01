package jota.utils;

/**
 * This class defines the global constants.
 *
 */
public class Constants {
    
    /**
     * 
     */
    public final static int KEY_LENGTH = 6561;

    /**
     * This String contains all possible characters of the tryte alphabet
     */
    public static final String TRYTE_ALPHABET = "9ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * The maximum seed length
     */
    public static final int SEED_LENGTH_MAX = 81;
    
    /**
     * The length of a hash in trits
     */
    public static int HASH_LENGTH_TRITS = 243;

    /**
     * The length of an address without checksum in trytes
     */
    public static int ADDRESS_LENGTH_WITHOUT_CHECKSUM = 81;

    /**
     * The length of an address with checksum in trytes
     */
    public static int ADDRESS_LENGTH_WITH_CHECKSUM = 90;

    /**
     * The length of a message
     */
    public static int MESSAGE_LENGTH = 2187;
    
    /**
     * Size of a full transaction, whether it has been attached or not.
     */
    public static int TRANSACTION_SIZE = 2673;

    /**
     * The length of an tag
     */
    public static int TAG_LENGTH = 27;
    
    /**
     * Maximum security level of an address
     */
    public static int MIN_SECURITY_LEVEL = 1;
    
    /**
     * Minimum security level of an address
     */
    public static int MAX_SECURITY_LEVEL = 3;

    public static final String INVALID_TRYTES_INPUT_ERROR = "Invalid trytes provided.";
    public static final String INVALID_HASHES_INPUT_ERROR = "Invalid hashes provided.";
    public static final String INVALID_TAIL_HASH_INPUT_ERROR = "Invalid tail hash provided.";
    public static final String INVALID_SEED_INPUT_ERROR = "Invalid seed provided.";
    public static final String INVALID_SECURITY_LEVEL_INPUT_ERROR = "Invalid security level provided.";
    public static final String INVALID_INDEX_INPUT_ERROR = "Invalid index provided.";
    public static final String INVALID_ATTACHED_TRYTES_INPUT_ERROR = "Invalid attached trytes provided.";
    public static final String INVALID_TRANSFERS_INPUT_ERROR = "Invalid transfers provided.";
    public static final String INVALID_ADDRESSES_INPUT_ERROR = "Invalid addresses provided.";
    public static final String INVALID_INPUT_ERROR = "Invalid input provided.";
    public static final String INVALID_TAG_ERROR = "Invalid tag provided.";
    
    public static final String INVALID_BUNDLE_ERROR = "Invalid bundle.";
    public static final String INVALID_BUNDLE_SUM_ERROR = "Invalid bundle sum.";
    public static final String INVALID_BUNDLE_HASH_ERROR = "Invalid bundle hash.";
    public static final String INVALID_SIGNATURES_ERROR = "Invalid signatures.";
    public static final String INVALID_VALUE_TRANSFER_ERROR = "Invalid value transfer: the transfer does not require a signature.";

    public static final String TRANSACTION_NOT_FOUND = "Transaction was not found on the node";
    
    public static final String NOT_ENOUGH_BALANCE_ERROR = "Not enough balance.";
    public static final String NO_REMAINDER_ADDRESS_ERROR = "No remainder address defined.";

    public static final String GET_TRYTES_RESPONSE_ERROR = "Get trytes response was null.";
    public static final String GET_BUNDLE_RESPONSE_ERROR = "Get bundle response was null.";
    public static final String GET_INCLUSION_STATE_RESPONSE_ERROR = "Get inclusion state response was null.";

    public static final String SENDING_TO_USED_ADDRESS_ERROR = "Sending to a used address.";
    public static final String PRIVATE_KEY_REUSE_ERROR = "Private key reuse detect!";
    public static final String SEND_TO_INPUTS_ERROR = "Send to inputs!";
}

