
# [broadcastTransactions](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L735)
 [BroadcastTransactionsResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/BroadcastTransactionsResponse.java) broadcastTransactions(String[] trytes)

Broadcast a list of transactions to all neighbors. The trytes to be used for this call should be valid, attached transaction trytes. These trytes are returned by `attachToTangle`, or by doing proof of work somewhere else.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| trytes | String[] | Required | The list of transaction trytes to broadcast |
    
## Output
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when the provided transaction trytes are invalid |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    BroadcastTransactionsResponse response = iotaAPI.broadcastTransactions(new String[]{"DXELUOEVEGVLFNTMTJUYWCLEU ... ZOXAMDGJRTAMORTGGSXR9YGDK", "TJCDHGEIDGSUINOJKTFGDELIT ... KCSPTGGBKJONJUEBOHASNYOTE"});
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
