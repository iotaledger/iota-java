
# [removeNeighbors](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPICore.java#L179)
 [RemoveNeighborsResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/RemoveNeighborsResponse.java) removeNeighbors(String[] uris)

Removes a list of neighbors from the node.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| uris | String[] | Required | The list of URI elements. |
    
## Output
| Return type | Description |
|--|--|

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) |  |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    RemoveNeighborsResponse response = iotaAPI.removeNeighbors(["udp://8.8.8.8:14265", "udp://8.8.8.8:14265"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
