
# [promoteTransaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L1738)
 List<[Transaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java)> promoteTransaction(String tail , int depth , int minWeightMagnitude , [Bundle](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Bundle.java) bundle)

Attempts to promote a transaction using a provided bundle and, if successful, returns the promoting Transactions. This is done by creating another transaction which points to the tail. This will effectively double the chances of the transaction to be picked, and this approved.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| tail | String | Required | bundle tail to promote, cannot be `null` |
| depth | int | Required | depth for getTransactionsToApprove |
| minWeightMagnitude | int | Required | minWeightMagnitude to use for Proof-of-Work |
| bundle | [Bundle](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Bundle.java) | Required | the [Bundle](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Bundle.java) to attach for promotion |
    
## Output
List<[Transaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java)>, which contains the following fields:
| Return type | Description |
|--|--|
| long attachmentTimestampLowerBound |  |
| long attachmentTimestampUpperBound |  |
| String hash | Get the hash. |
| String signatureFragments | Get the signature fragments. |
| String address | Get the address. |
| long value | Get the value. |
| String tag | Get the tag. |
| long timestamp | Get the timestamp. |
| long currentIndex | Get the current index. |
| long lastIndex | Get the last index. |
| String bundle | Get the bundle. |
| String trunkTransaction | Get the trunk transaction. |
| String branchTransaction | Get the branch transaction. |
| String nonce | Get the nonce. |
| Boolean persistence | Get the persistence. |
| String obsoleteTag | Get the obsoleteTag. |
| long attachmentTimestamp | Get the attachmentTimestamp. |
| boolean tailTransaction | Checks if the current index is 0 |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | When the bundle has no transaction |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | When <tt>depth</tt> or <tt>minWeightMagnitude</tt> is lower than 0 |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | When the <tt>tail</tt> hash is invalid |
| [NotPromotableException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/errors/NotPromotableException.java) | When the transaction is not promotable |

## Related APIs (link to other product documentation)
| API     | Description |
|:---------------|:--------|
| [checkConsistency(String...)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L533) | Checks the consistency of the subtangle formed by the provided tails. |
| [getTransactionsToApprove(Integer, String)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L403) | Tip selection which returns `trunkTransaction` and `branchTransaction`. The input value `depth` determines how many milestones to go back for finding the transactions to approve. The higher your `depth` value, the more work you have to do as you are confirming more transactions. If the `depth` is too large (usually above 15, it depends on the node's configuration) an error will be returned. The `reference` is an optional hash of a transaction you want to approve. If it can't be found at the specified `depth` then an error will be returned. |
| [attachToTangle(String, String, Integer, String...)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L578) |  Prepares the specified transactions (trytes) for attachment to the Tangle by doing Proof of Work. You need to supply `branchTransaction` as well as `trunkTransaction`. These are the tips which you're going to validate and reference with this transaction.  These are obtainable by the `getTransactionsToApprove` API call. 
  The returned value is a different set of tryte values which you can input into  `broadcastTransactions` and `storeTransactions`. 
  The last 243 trytes of the return value consist of the following:  * `trunkTransaction`
 * `branchTransaction`
 * `nonce`
   These are valid trytes which are then accepted by the network. |
| [storeAndBroadcast(String...)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L395) | Wrapper method: stores and broadcasts the specified trytes. |

 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    List<Transaction> response = iotaAPI.promoteTransaction("SEKFEWFLXVPQQORWSJHYVQZGOTINOCNLRUEDQGSTBSBBXQGVJWPSFYZRHLJEBXTKEIKNEAOGIRS9NHCSI", "15", "18", "bundle");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
