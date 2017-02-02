package jota.error;

/**
 * Created by Adrian on 22.01.2017.
 */
public class NoInclusionStatesExcpection extends BaseException {

    public NoInclusionStatesExcpection() {
        super("No inclusion states for the provided seed");
    }
}
