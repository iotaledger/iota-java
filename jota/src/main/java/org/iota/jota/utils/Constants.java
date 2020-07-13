package org.iota.jota.utils;

/**
 * This class defines the global constants.
 *
 */
public class Constants {

    /**
     * Empty hash, same as TVM.NULL_HASH in IRI
     */
    public static final String NULL_HASH = "999999999999999999999999999999999999999999999999999999999999999999999999999999999";
    
    /**
     * This String contains all possible characters of the tryte alphabet
     */
    public static final String TRYTE_ALPHABET = "9ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * The length of an IOTA seed
     */
    public static final int SEED_LENGTH_MAX = 81;

    /**
     * The length of an address without checksum
     */
    public static final int ADDRESS_LENGTH_WITHOUT_CHECKSUM = 81;
    
    /**
     * The length of a hash in trits
     */
    public static final int HASH_LENGTH_TRITS = 243;

    /**
     * The length of an address with checksum
     */
    public static final int ADDRESS_LENGTH_WITH_CHECKSUM = 90;

    /**
     * The length of an message
     */
    public static final int MESSAGE_LENGTH = 2187;
    
    /**
     * The length of a transaction
     */
    public static final int TRANSACTION_LENGTH = 2673;
    
    /**
     * Maximum number represented in 27 trits
     */
    public static final long TRANSACTION_UPPER_BOUND_MAX = 3_812_798_742_493L;

    /**
     * The length of an tag in trytes
     */
    public static final int TAG_LENGTH = 27;    
    
    /**
     * Maximum security level of an address
     */
    public static final int MIN_SECURITY_LEVEL = 1;
    
    /**
     * Minimum security level of an address
     */
    public static final int MAX_SECURITY_LEVEL = 3;
    
    /**
     * Length of a single level of security for the trits of a signing key
     */
    public final static int KEY_LENGTH = 6561;
    
    
    public static final String ARRAY_NULL_OR_EMPTY = "Array cannot be null or empty";

    @Deprecated
    public static final String INVALID_THRESHOLD_ERROR = "Invalid threshold provided. (Between 0 and 100 incl.)";

    public static final String INVALID_APPROVE_DEPTH_ERROR = "Invalid depth provided. (Between 0 and 15, soft upper bound)";
    public static final String INVALID_TAG_INPUT_ERROR = "Invalid tag provided.";
    public static final String INVALID_TRYTES_INPUT_ERROR = "Invalid trytes provided.";
    public static final String INVALID_TRITS_INPUT_ERROR = "Invalid trits provided.";
    public static final String INVALID_HASH_INPUT_ERROR = "Invalid hash provided.";
    public static final String INVALID_HASHES_INPUT_ERROR = "Invalid hashes provided.";
    public static final String INVALID_TAIL_HASH_INPUT_ERROR = "Invalid tail hash provided.";
    public static final String INVALID_SEED_INPUT_ERROR = "Invalid seed provided.";
    public static final String INVALID_SECURITY_LEVEL_INPUT_ERROR = "Invalid security level provided.";
    public static final String INVALID_ATTACHED_TRYTES_INPUT_ERROR = "Invalid attached trytes provided.";
    public static final String INVALID_TRANSFERS_INPUT_ERROR = "Invalid transfers provided.";
    public static final String INVALID_ADDRESS_INPUT_ERROR = "Invalid address provided.";
    public static final String INVALID_ADDRESSES_INPUT_ERROR = "Invalid addresses provided.";
    public static final String INVALID_INPUT_ERROR = "Invalid input provided.";
    public static final String INVALID_INDEX_INPUT_ERROR = "Invalid index provided.";

    public static final String INVALID_BUNDLE_ERROR = "Invalid bundle.";
    public static final String INVALID_BUNDLE_SUM_ERROR = "Invalid bundle sum.";
    public static final String INVALID_BUNDLE_HASH_ERROR = "Invalid bundle hash.";
    public static final String INVALID_SIGNATURES_ERROR = "Invalid signatures.";
    public static final String INVALID_VALUE_TRANSFER_ERROR = "Invalid value transfer: the transfer does not require a signature.";

    public static final String NOT_ENOUGH_BALANCE_ERROR = "Not enough balance.";
    public static final String NO_REMAINDER_ADDRESS_ERROR = "No remainder address defined.";
    
    public static final String TRANSACTION_NOT_FOUND = "Transaction was not found on the node";

    public static final String GET_TRYTES_RESPONSE_ERROR = "Get trytes response was null.";
    public static final String GET_BUNDLE_RESPONSE_ERROR = "Get bundle response was null.";
    public static final String GET_INCLUSION_STATE_RESPONSE_ERROR = "Get inclusion state response was null.";

    public static final String SENDING_TO_USED_ADDRESS_ERROR = "Sending to a used address.";
    public static final String PRIVATE_KEY_REUSE_ERROR = "Private key reuse detect!";
    public static final String SEND_TO_INPUTS_ERROR = "Send to inputs!";

    public static final String ACCOUNT_MESSAGE = "IOTA Accounts Transfer";
}

