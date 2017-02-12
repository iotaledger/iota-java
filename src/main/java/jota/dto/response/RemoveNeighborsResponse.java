package jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaNeighborsRequest}
 **/
public class RemoveNeighborsResponse extends AbstractResponse {

    private int removedNeighbors;

    public int getRemovedNeighbors() {
        return removedNeighbors;
    }
}
