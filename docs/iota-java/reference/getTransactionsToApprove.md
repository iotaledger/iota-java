
# [getTransactionsToApprove](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPICore.java#L335)
 [GetTransactionsToApproveResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetTransactionsToApproveResponse.java) getTransactionsToApprove(Integer depth)

[getTransactionsToApprove(Integer, String)](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPICore.java#L324)
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| depth | Integer | Required | missing description |
    
## Output
| Return type | Description |
|--|--|

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) |  |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetTransactionsToApproveResponse response = iotaAPI.getTransactionsToApprove("15");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
