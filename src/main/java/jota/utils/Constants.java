package jota.utils;

/**
 * This class defines the global constants.
 *
 * @author pinpong
 */
public class Constants {

    /**
     * This String contains all possible characters of the tryte alphabet
     */
    public static final String TRYTE_ALPHABET = "9ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * The maximum seed length
     */
    public static final int SEED_LENGTH_MAX = 81;

    /**
     * The length of an address without checksum
     */
    public static int ADDRESS_LENGTH_WITHOUT_CHECKSUM = 81;

    /**
     * The length of an address with checksum
     */
    public static int ADDRESS_LENGTH_WITH_CHECKSUM = 90;
}
