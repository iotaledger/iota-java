package org.iota.jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaNeighborsRequest}.
 **/
public class RemoveNeighborsResponse extends AbstractResponse {

    private int removedNeighbors;

    /**
     * Gets the number of removed neighbors.
     *
     * @return The number of removed neighbors.
     */
    public int getRemovedNeighbors() {
        return removedNeighbors;
    }
}
