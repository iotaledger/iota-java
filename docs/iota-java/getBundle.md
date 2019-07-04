
# [getBundle](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L954)
 [GetBundleResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetBundleResponse.java) getBundle(String transaction)

Gets the associated bundle transactions of a single transaction. Does validation of signatures, total sum as well as bundle order.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| transaction | String | Required | The transaction hash |
    
## Output
[GetBundleResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetBundleResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| List<[Transaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java)> transactions | Gets the transactions. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | if the transaction hash is invalid |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | if the bundle is invalid or not found |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetBundleResponse response = iotaAPI.getBundle("SSNDAZXFTOOKBDFXNANGLJSKIO9UHUK9OPYLFQOHOLIJDOYVIGDJEYVLIPWDQWOLCJIFLNGHKACDWUUSZ");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
