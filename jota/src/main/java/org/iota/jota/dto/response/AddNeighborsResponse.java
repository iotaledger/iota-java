package org.iota.jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaNeighborsRequest}.
 **/
public class AddNeighborsResponse extends AbstractResponse {

    private int addedNeighbors;

    /**
     * Gets the number of added neighbors.
     *
     * @return The number of added neighbors.
     */
    public int getAddedNeighbors() {
        return addedNeighbors;
    }
}
