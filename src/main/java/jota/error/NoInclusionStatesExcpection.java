package jota.error;

/**
 * @author Adrian
 */
public class NoInclusionStatesExcpection extends BaseException {

    public NoInclusionStatesExcpection() {
        super("No inclusion states for the provided seed");
    }
}
