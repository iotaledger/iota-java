package jota.dto.response;

import jota.model.Neighbor;

import java.util.ArrayList;
import java.util.List;

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
