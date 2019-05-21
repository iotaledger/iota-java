
# [replayBundle](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L1059)
 [ReplayBundleResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/ReplayBundleResponse.java) replayBundle(String tailTransactionHash , int depth , int minWeightMagnitude , String reference)

Replays a transfer by doing Proof of Work again. This will make a new, but identical transaction which now also can be approved. If any of the replayed transactions gets approved, the others stop getting approved.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| tailTransactionHash | String | Required | The hash of tail transaction. |
| depth | int | Required | The depth for getting transactions to approve |
| minWeightMagnitude | int | Required | The minimum weight magnitude for doing proof of work |
| reference | String | Required | Hash of transaction to start random-walk from.This is used to make sure the tips returned reference a given transaction in their past.Can be `null`, in that case the latest milestone is used as a reference. |
    
## Output
[ReplayBundleResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/ReplayBundleResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| Boolean[] successfully | Gets the successfully. |
| [Bundle](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Bundle.java) newBundle |  |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when the <tt>tailTransactionHash</tt> is invalid |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when the bundle is invalid or not found |

## Related APIs (link to other product documentation)
| API     | Description |
|:---------------|:--------|
| [sendTrytes(String[], int, int, String)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L425) | Wrapper method: Gets transactions to approve, attaches to Tangle, broadcasts and stores. |

 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    ReplayBundleResponse response = iotaAPI.replayBundle("LUKKXQVUTINZFCGWLQPVBYLRSAGABLTPLRUPRPGFHFSFABHGCNEEJDMYI9EFWZNMABYFDXXORDHEUVUMG", "15", "18", "IYEBCPYKEWXEFIBJCOWA9AWNLDRLIPOJAXJGMNXJDUBTYDMHWKMWDHDGRQLYRKTNIYNDSIULAVLNKIUKF");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
