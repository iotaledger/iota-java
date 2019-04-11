
# [findTransactionObjectsByTag](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L418)
 List<[Transaction](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transaction.java)> findTransactionObjectsByTag(String[] tags)

Wrapper function for findTransactions, getTrytes and transactionObjects.  Returns the transactionObject of a transaction hash. The input can be a list of valid tags.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| tags | String[] | Required | The tags. |
    
## Output
| Return type | Description |
|--|--|
| List<[Transaction](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transaction.java)>  | Transactions. |



 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    List response = iotaAPI.findTransactionObjectsByTag(["HEKUGTODBUPOETWARMMJ9PSAYJUIJJLHFVOAHPDWSSURULWAGDFCDUNGAUDQDWTIIFVDTDZGRZXARCVTQ", "A9CAJ9WUFQKYUQOMZHPOHMQ9NUDNHBYBGHYBNPVPRXNDAHCYNN9UAKPNUCJQISJYKVQEVMYXFNBJHACRD"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
