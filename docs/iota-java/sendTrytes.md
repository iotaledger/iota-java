
# [sendTrytes](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L425)
 List<[Transaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java)> sendTrytes(String[] trytes , int depth , int minWeightMagnitude , String reference)

Wrapper method: Gets transactions to approve, attaches to Tangle, broadcasts and stores.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| trytes | String[] | Required | The transaction trytes |
| depth | int | Required | The depth for getting transactions to approve |
| minWeightMagnitude | int | Required | The minimum weight magnitude for doing proof of work |
| reference | String | Required | Hash of transaction to start random-walk from   This is used to make sure the tips returned reference a given transaction in their past.   This can be `null`, in that case the latest milestone is used as a reference. |
    
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
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | is thrown when invalid trytes is provided. |

## Related APIs (link to other product documentation)
| API     | Description |
|:---------------|:--------|
| [broadcastTransactions(String...)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L701) | Broadcast a list of transactions to all neighbors. The trytes to be used for this call should be valid, attached transaction trytes. These trytes are returned by `attachToTangle`, or by doing proof of work somewhere else. |
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
    List<Transaction> response = iotaAPI.sendTrytes(["YPGANSULLVLKBEEUHXCYMBIIB ... TDSKWQ9F9BDKGZVXYM9YQZZAB", "PBF9CKGAVSTEIRXJTLGDKYVBC ... NKWCB9PHOERDCLCSUQJZAQVNU"], "15", "18", "TRJYVNUZNP9EPEMY9EBQMTWEJQBUQJUCGPKXOQKCMMJSXYOWFWNHHGHVRUXKCLHCWFIFMRZBQRPGJTMKU");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
