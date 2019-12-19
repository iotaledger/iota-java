
# [bundlesFromAddresses](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L225)
 [Bundle[]](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Bundle.java) bundlesFromAddresses(Boolean inclusionStates , String[] addresses)

Internal function to get the formatted bundles of a list of addresses.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| inclusionStates | Boolean | Required | If `true`, it also gets the inclusion state of each bundle. |
| addresses | String[] | Required | Array of addresses. |
    
## Output
| Return type | Description |
|--|--|
| [Bundle[]](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Bundle.java)  | All the transaction bundles for the addresses, or `null` when our thread gets interrupted |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | When the addresses are invalid |
| IllegalStateException | When inclusion state/confirmed could not be determined (`null` returned) |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    Bundle[] response = iotaAPI.bundlesFromAddresses(true, new String[]{"QFMOVHMDGKAWVCNAVJOBAVMJQZRQSJITNEOHHORMTNKA9QTZVMLHKWZMTQXDSCOVZXQESHCGJ9ALATFLE", "RDSXBLCZYXCSBFVJKE9BIUKQZUOLMKXTW9TLHHZEAGFKFYZPDTA9IJHHPLQLEDXQHSXYAPXCMJATLZNZN"});
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
