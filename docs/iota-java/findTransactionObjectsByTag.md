
# [findTransactionObjectsByTag](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L497)
 List<[Transaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java)> findTransactionObjectsByTag(String[] tags)

Wrapper function: Finds transactions, gets trytes and turns it into [Transaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java) objects.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| tags | String[] | Required | The tags the transactions we search for have |
    
## Output
| Return type | Description |
|--|--|
| List<[Transaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java)>  | Transactions. |



 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    List<Transaction> response = iotaAPI.findTransactionObjectsByTag(["PWATR9HZOXASW9ELVFWVSFJ9XMBXFTNRJZYSTUXFQEYMMFKQOHRIZIHPCKIIQLBFFDCKFZVWCLVGHQMPA", "SATASKK9LXPIPMCGVIYZUY99YSXCKPLAPHLGPIVQVUZMLLXZCQDGNASBJNEMAIOAKPQCPXADLWQDYKLFU"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
