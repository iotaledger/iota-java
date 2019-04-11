
# [promoteTransaction](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L1404)
 List<[Transaction](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transaction.java)> promoteTransaction(String tail, int depth, int minWeightMagnitude, [Bundle](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Bundle.java) bundle)

Attempts to promote a transaction using a provided bundle and, if successful, returns the promoting Transactions.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| tail | String | Required | bundle tail to promote |
| depth | int | Required | depth for getTransactionsToApprove |
| minWeightMagnitude | int | Required | minWeightMagnitude to use for Proof-of-Work |
| bundle | [Bundle](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Bundle.java) | Required | the bundle to attach for promotion |
    
## Output
| Return type | Description |
|--|--|
| List<[Transaction](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transaction.java)>  | attached bundle trytes |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) | invalid method arguments provided |
| [NotPromotableException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/NotPromotableException.java) | transaction is not promotable |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    List response = iotaAPI.promoteTransaction("GUBZQIIEYVUKROYKDT9OQUXLDIGMTPPJGGHBDKHTZRHYVJGK9NPHCDWNSNCLVSXYDHSZVBWXMAG99OUPR", "15", "18", "bundle");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
