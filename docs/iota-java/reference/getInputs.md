
# [getInputs](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L651)
 [GetBalancesAndFormatResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetBalancesAndFormatResponse.java) getInputs(String seed, int security, int start, int end, long threshold, String[] tips)

Gets the inputs of a seed
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| seed | String | Required | Tryte-encoded seed. It should be noted that this seed is not transferred. |
| security | int | Required | The Security level of private key / seed. |
| start | int | Required | Starting key index. |
| end | int | Required | Ending key index. |
| threshold | long | Required | Min balance required. |
| tips | String[] | Required | The starting points we walk back from to find the balance of the addresses |
    
## Output
| Return type | Description |
|--|--|

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) | is thrown when the specified input is not valid. |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetBalancesAndFormatResponse response = iotaAPI.getInputs("XHVONDIEDTGSOLELBWKIHMASUWQKMXGWDFOAFKDRAVMJZAYXPPEVCRFTSOIHNLHAQMSATAOCUQUSKVMQ9", "951", "621", "558", "100", ["GSCXVYCSPPRGRF99KTOBSBXUCL9UXJJIYN9ZLI9WBXARZKUHUIVB9NYRASVMYEM9SWJQQNBQNKAI9VYCV", "9VGYM9XFY9SIIUBSAYAHOSTZVXQBKHCQQOFDRFLCXMZGCZQCWNKKHYGBAI9MMDNALGRINSPUMADSGXJQQ"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
