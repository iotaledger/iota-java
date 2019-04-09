package org.iota.jota.dto.response;

import java.util.ArrayList;
import java.util.List;

import org.iota.jota.model.Neighbor;

/**
 * Response of {@link jota.dto.request.IotaCommandRequest}.
 **/
public class GetNeighborsResponse extends AbstractResponse {

    private List<Neighbor> neighbors = new ArrayList<>();

    /**
     * Gets the neighbors.
     *
     * @return The neighbors.
     */
    public List<Neighbor> getNeighbors() {
        return neighbors;
    }
}
