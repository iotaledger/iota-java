package jota.error;

/**
 * This exceptions occurs if its not possible to broadcast and store.
 *
 * @author Adrian
 */
public class BroadcastAndStoreException extends BaseException {

    /**
     * Initializes a new instance of the BroadcastAndStoreException.
     */
    public BroadcastAndStoreException() {
        super("Impossible to broadcastAndStore, aborting.");
    }
}