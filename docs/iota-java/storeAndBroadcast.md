
# [storeAndBroadcast](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L313)
 [BroadcastTransactionsResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/BroadcastTransactionsResponse.java) storeAndBroadcast(String[] trytes)

Wrapper method: stores and broadcasts the specified trytes.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| trytes | String[] | Required | The trytes. |
    
## Output
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | is thrown when the specified `trytes` is not valid. |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | If {@link #storeTransactions(String...)} fails |

## Related APIs (link to other product documentation)
| API     | Description |
|:---------------|:--------|
| [storeTransactions(String...)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L753) | Stores transactions in the local storage. The trytes to be used for this call should be valid, attached transaction trytes. These trytes are returned by `attachToTangle`, or by doing proof of work somewhere else. |
| [broadcastTransactions(String...)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L735) | Broadcast a list of transactions to all neighbors. The trytes to be used for this call should be valid, attached transaction trytes. These trytes are returned by `attachToTangle`, or by doing proof of work somewhere else. |

 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    BroadcastTransactionsResponse response = iotaAPI.storeAndBroadcast(new String[]{"PNZUWPGFCKB9ORDZNHRENUPEF ... CXLRFAEBVOFPOFSOAVYE9LHTM", "M99KAOTCNSJ9DRJGIQICCGWRH ... CNXTDNXMQEYMYJTVESRZQOUQS"});
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
