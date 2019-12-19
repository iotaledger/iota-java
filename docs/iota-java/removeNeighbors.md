
# [removeNeighbors](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L213)
 [RemoveNeighborsResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/RemoveNeighborsResponse.java) removeNeighbors(String[] uris)

Temporarily removes a list of neighbors from your node. The added neighbors will be added again after relaunching IRI. Remove the neighbors from your config file or make sure you don't supply them in the -n command line option if you want to keep them removed after restart. The URI (Unique Resource Identification) for removing neighbors is: **tcp://IPADDRESS:PORT**
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| uris | String[] | Required | The URIs of the neighbors we want to remove. |
    
## Output
[RemoveNeighborsResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/RemoveNeighborsResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| int removedNeighbors | Gets the number of removed neighbors. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | The request was considered wrong in any way by the node |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | When an URI is invalid |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    RemoveNeighborsResponse response = iotaAPI.removeNeighbors(new String[]{"tcp://8.8.8.8:14265", "tcp://8.8.8.8:14265"});
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
