
# [traverseBundle](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L1152)
 [Bundle](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Bundle.java) traverseBundle(String trunkTx , String bundleHash , [Bundle](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Bundle.java) bundle)

Traverses the Bundle by going down the trunkTransactions until the bundle hash of the transaction changes.  In case the input transaction hash is not a tail, we return an error.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| trunkTx | String | Required | Hash of a trunk or a tail transaction of a bundle. |
| bundleHash | String | Required | The bundle hash. Should be `null`, and will use the transactions bundle hash |
| bundle | [Bundle](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Bundle.java) | Required | [Bundle](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Bundle.java) to be populated by traversing. |
    
## Output
| Return type | Description |
|--|--|
| [Bundle](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Bundle.java)  | Transaction objects. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when `trunkTx` is invalid, or has no transactions |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when a transaction in the bundle has no reference to the bundle |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when the first transaction in the bundle is not a tail |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    Bundle response = iotaAPI.traverseBundle("9KMVSCSOAGIDMS9SCMNDCTRHW9OFTMUAZHGPRTOWUAEETKRPKTG99TYBZ9XBM9THNWJYTEOZTAGWTZPRJ", "9MYMRXQTTKSIGPVUUQYFMJOHPGHAHSSYAENVJWJNIHRKSUWAKJZBUQIJEOMISUPDRESZIVYSRPAG9OD9K", bundle);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
