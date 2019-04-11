
# [getTransfers](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L205)
 [GetTransferResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetTransferResponse.java) getTransfers(String seed, int security, Integer start, Integer end, Boolean inclusionStates)


> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| seed | String | Required | Tryte-encoded seed. It should be noted that this seed is not transferred. |
| security | int | Required | The security level of private key / seed. |
| start | Integer | Required | Starting key index. |
| end | Integer | Required | Ending key index. |
| inclusionStates | Boolean | Required | If `true`, it gets the inclusion states of the transfers. |
    
## Output
| Return type | Description |
|--|--|
| [GetTransferResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetTransferResponse.java)  | Bundle of transfers. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) | is thrown when the specified input is not valid. |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetTransferResponse response = iotaAPI.getTransfers("HLIOXDOWQNAPJSNMOYSTZDYJFIPURNNYTWFFGGIZSVQTIENEAUVETLNTVZSMHNGRZZEORALCITUCEEOZS", "120", "764", "567", "true");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
