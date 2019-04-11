
# [findTransactionObjectsByBundle](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L451)
 List<[Transaction](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transaction.java)> findTransactionObjectsByBundle(String[] bundles)

Wrapper function for findTransactions, getTrytes and transactionObjects.  Returns the transactionObject of a transaction hash. The input can be a list of valid bundles.  findTransactions input
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| bundles | String[] | Required | The bundles. |
    
## Output
| Return type | Description |
|--|--|
| List<[Transaction](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transaction.java)>  | Transactions. |



 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    List response = iotaAPI.findTransactionObjectsByBundle(["XCXGSAADBJVBNKSHUQLCDSMJFFRADYYESEPKLWONABWOZBZNIYIUIPEHCSYDUZNIJBIDMERILGW9YC9OG", "BLUZKR9GWRHPNVQVFEVV99JAUIQHWTJWYKUKAVFIKNBWTNJAUQMWPZNUGRLAWMPKQOCXUUYAEMITPNKNE"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
