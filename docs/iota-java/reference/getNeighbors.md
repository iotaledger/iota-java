
# [getNeighbors](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPICore.java#L155)
 [GetNeighborsResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetNeighborsResponse.java) getNeighbors()

Get the list of neighbors from the node.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

    
## Output
| Return type | Description |
|--|--|
| [GetNeighborsResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetNeighborsResponse.java)  | The set of neighbors the node is connected with. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) |  |


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
