
# [findTransactionObjectsByApprovees](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L434)
 List<[Transaction](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transaction.java)> findTransactionObjectsByApprovees(String[] approvees)

Wrapper function for findTransactions, getTrytes and transactionObjects.  Returns the transactionObject of a transaction hash. The input can be a list of valid approvees.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| approvees | String[] | Required | The approvees. |
    
## Output
| Return type | Description |
|--|--|
| List<[Transaction](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transaction.java)>  | Transactions. |



 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    List response = iotaAPI.findTransactionObjectsByApprovees(["9IHYITNFEMXXGDSVAAAHM9DJUZNKMWWFRQ9EWHVSIPXJTFVNZLYUQHIRM9ZSVSGPYIZWKJHRSVWIG9OBY", "KGXOGGGLFYCSDURRYFYKDTWJFNGQBICPTKIF9RMA9QPOGRICKWZWRRVSWWRTSKCQCAWVAPBGRXTGLNOXH"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
