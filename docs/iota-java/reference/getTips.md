
# [getTips](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPICore.java#L191)
 [GetTipsResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetTipsResponse.java) getTips()

Get the list of latest tips (unconfirmed transactions).
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

    
## Output
| Return type | Description |
|--|--|
| [GetTipsResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetTipsResponse.java)  | The the list of tips. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) |  |


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
