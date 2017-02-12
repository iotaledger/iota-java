package jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaNeighborsRequest}
 **/
public class AddNeighborsResponse extends AbstractResponse {

    private int addedNeighbors;

    public int getAddedNeighbors() {
        return addedNeighbors;
    }
}
