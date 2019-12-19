
# [addNeighbors](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L195)
 [AddNeighborsResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/AddNeighborsResponse.java) addNeighbors(String[] uris)

Temporarily add a list of neighbors to your node. The added neighbors will not be available after restart. Add the neighbors to your config file  or supply them in the `-n` command line option if you want to add them permanently. The URI (Unique Resource Identification) for adding neighbors is: **tcp://IPADDRESS:PORT**
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| uris | String[] | Required | list of neighbors to add |
    
## Output
[AddNeighborsResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/AddNeighborsResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| int addedNeighbors | Gets the number of added neighbors. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | The request was considered wrong in any way by the node |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | When an URI is invalid |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    AddNeighborsResponse response = iotaAPI.addNeighbors(new String[]{"tcp://8.8.8.8:14265", "tcp://8.8.8.8:14265"});
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
