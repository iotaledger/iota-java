package jota.error;

/**
 * @author Adrian
 */
public class BroadcastAndStoreException extends BaseException {

    public BroadcastAndStoreException() {
        super("Impossible to broadcastAndStore, aborting.");
    }
}