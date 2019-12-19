
# [checkConsistency](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L563)
 [CheckConsistencyResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/CheckConsistencyResponse.java) checkConsistency(String[] tails)

Checks the consistency of the subtangle formed by the provided tails.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| tails | String[] | Required | The tails describing the subtangle. |
    
## Output
[CheckConsistencyResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/CheckConsistencyResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| boolean state | Gets the state. |
| String info | If state is false, this provides information on the cause of the inconsistency. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when a tail hash is invalid |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    CheckConsistencyResponse response = iotaAPI.checkConsistency(new String[]{"ULIYGFYFAVGNOWKTSJDRBIWUXHJGZDXDVLFJWKNFMJGDSLKEOLSSSWG9PHRHSPHWVHFIHMAUVDOWWSMPN", "CKBRRWMWKWYMXKVPOOVACNHLTLR9EOGWBAIAMNGO9JZOJCLIRJBAUEXCZ9QHYYOXKDCSOJLSMXRYLSFXN"});
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
