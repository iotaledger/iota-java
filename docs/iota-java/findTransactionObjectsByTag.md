
# [findTransactionObjectsByTag](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L418)
 List<[Transaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java)> findTransactionObjectsByTag(String[] tags)

Wrapper function: Finds transactions, gets trytes and turns it into [Transaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java) objects.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| tags | String[] | Required | The tags the transactions we search for have |
    
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
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | if any of the tags are not a valid |

## Related APIs (link to other product documentation)
| API     | Description |
|:---------------|:--------|
| [findTransactionsByTags(String...)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L360) | Find the transactions by tags |
| [findTransactionsObjectsByHashes](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L372) | Wrapper function: get trytes and turns into [Transaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java) objects. Gets the trytes and transaction object from a list of transaction hashes. |

 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    List<Transaction> response = iotaAPI.findTransactionObjectsByTag(new String[]{"TAG9DKZQQPWFFFFLKPTHDMWZYTT", "TAG9Y9RDVC99ZLXVPIBY9WMTUCZ"});
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
