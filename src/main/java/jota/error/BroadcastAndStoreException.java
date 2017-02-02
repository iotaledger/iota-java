package jota.error;

/**
 * Created by Adrian on 22.01.2017.
 */
public class BroadcastAndStoreException extends BaseException {

    public BroadcastAndStoreException() {
        super("Impossible to broadcastAndStore, aborting.");
    }
}