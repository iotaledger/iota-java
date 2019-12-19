
# [getInclusionStates](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L384)
 [GetInclusionStateResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetInclusionStateResponse.java) getInclusionStates(String[] transactions , String[] tips)

 Get the inclusion states of a set of transactions. This is for determining if a transaction was accepted and confirmed by the network or not. You can search for multiple tips (and thus, milestones) to get past inclusion states of transactions. 
  This API call returns a list of boolean values in the same order as the submitted transactions. Boolean values will be `true` for confirmed transactions, otherwise `false`. 

> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| transactions | String[] | Required | Array of transactions you want to get the inclusion state for. |
| tips | String[] | Required | Array of tips (including milestones) you want to search for the inclusion state. |
    
## Output
[GetInclusionStateResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetInclusionStateResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| boolean[] states | Gets the states. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | The request was considered wrong in any way by the node |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when a transaction hash is invalid |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when a tip is invalid |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetInclusionStateResponse response = iotaAPI.getInclusionStates(new String[]{"HUQLCQUAJDRTOOJMNVRLDFEDESKDKQDVXJNXQJNGGXAIRIQFWOJPTWTR9ALIFIZYUSBXFUUWBOMEELWBI", "IVVFEVFYVRWWANURCHQKMRSSGYIYFDQFL9LPLPCYXAUCGFPTHQNWE9DWHXDUSHGMFTQBJSYVYITMB9NBM"}, new String[]{"AQPJPOQRUZTZQZFWQIW9OGRCBIKDBPEGKYWICUSGNV9WXUCRPMJQBFFCJSXFATYXJMJCOBGZOOTIWHRPV", "QIMCPH9EFIYPFFEQUY9WNVYVZNOMZAJDXONKPKAZRTRBCWDDPAPEHJVPGHECHAFZIHCJOQ9RUGYBYOAVD"});
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
