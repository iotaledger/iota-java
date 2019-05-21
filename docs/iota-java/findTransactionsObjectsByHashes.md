
# [findTransactionsObjectsByHashes](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L454)
 List<[Transaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java)> findTransactionsObjectsByHashes(String[] hashes)

Wrapper function: get trytes and turns into [Transaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java) objects. Gets the trytes and transaction object from a list of transaction hashes.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| hashes | String[] | Required | The hashes of the transactions we want to get the transactions from |
    
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
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | if hashes is not a valid array of hashes |

## Related APIs (link to other product documentation)
| API     | Description |
|:---------------|:--------|
| [getTrytes(String...)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L378) | Returns the raw transaction data (trytes) of a specific transaction. These trytes can then be easily converted into the actual transaction object. You can use [Transaction(String)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java#L145) for conversion to an object. |

 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    List<Transaction> response = iotaAPI.findTransactionsObjectsByHashes(["HBYVBSPIIUITVMY9SEYFMBKVKJRRGKPATGJOPGHVNRMLUBKZIIRVVAPWPEC9OUU9MALDJSCVKSZOSJCUR", "RXVD9RRJEMXTYUUBEDOPRIQVYDVPET9YSS9ETDSUSRWYOXNDKHBEDXHRSCZOS9VBIQVRIRLOGDGW9SEXC"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
