
# [replayBundle](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L868)
 [ReplayBundleResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/ReplayBundleResponse.java) replayBundle(String tailTransactionHash, int depth, int minWeightMagnitude, String reference)

Replays a transfer by doing Proof of Work again.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| tailTransactionHash | String | Required | The hash of tail transaction. |
| depth | int | Required | The depth. |
| minWeightMagnitude | int | Required | The minimum weight magnitude. |
| reference | String | Required | Hash of transaction to start random-walk from, used to make sure the tips returned reference a given transaction in their past. |
    
## Output
| Return type | Description |
|--|--|
| [ReplayBundleResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/ReplayBundleResponse.java)  | Analyzed Transaction objects. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) | is thrown when the specified input is not valid. |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    ReplayBundleResponse response = iotaAPI.replayBundle("WRWKSEYN9ODIWDDEWSRF9UFSWMKYIQAVX9SLRJIXJWFLPE9JDHP9GOAZXJM9ODRVOIGMZMFDWHKNFTORD", "15", "18", "FKBFUEZVKOXEUMUMR9PWYCLLPHA9KYTKJLG9TXYGWQPWGYCHDRMLENNQFBZKL9UHSN999ILDQA9NPDTXT");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
