
# [getTips](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L224)
 [GetTipsResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetTipsResponse.java) getTips()

Returns all tips currently known by this node.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

    
## Output
[GetTipsResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetTipsResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| String[] hashes | Gets the hashes. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | The request was considered wrong in any way by the node |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetTipsResponse response = iotaAPI.getTips();
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
