
# [getNeighbors](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L176)
 [GetNeighborsResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetNeighborsResponse.java) getNeighbors()

Returns the set of neighbors you are connected with, as well as their activity statistics (or counters). The activity counters are reset after restarting IRI.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

    
## Output
[GetNeighborsResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetNeighborsResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| List<[Neighbor](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Neighbor.java)> neighbors | Gets the neighbors. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | The request was considered wrong in any way by the node |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetNeighborsResponse response = iotaAPI.getNeighbors();
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
