
# [getBundle](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L760)
 [GetBundleResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetBundleResponse.java) getBundle(String transaction)

Gets the associated bundle transactions of a single transaction.  Does validation of signatures, total sum as well as bundle order.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| transaction | String | Required | The transaction encoded in trytes. |
    
## Output
| Return type | Description |
|--|--|
| [GetBundleResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetBundleResponse.java)  | an array of bundle, if there are multiple arrays it means that there are conflicting bundles. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) | is thrown when the specified input is not valid. |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetBundleResponse response = iotaAPI.getBundle("GNOBEVSBZGGKXTCAFGBBAJEDYVPETZDUDBFTLKJYXFJUVPYQLOYUV9PQBMPPIVPZNZTB9SCWCTSFMJFWX");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
