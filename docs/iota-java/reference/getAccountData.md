
# [getAccountData](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L796)
 [GetAccountDataResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetAccountDataResponse.java) getAccountData(String seed, int security, int index, boolean checksum, int total, boolean returnAll, int start, int end, boolean inclusionStates, long threshold)

Similar to getTransfers, just that it returns additional account data
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| seed | String | Required | Tryte-encoded seed. It should be noted that this seed is not transferred. |
| security | int | Required | The Security level of private key / seed. |
| index | int | Required | Key index to start search from. If the index is provided, the generation of the address is not deterministic. |
| checksum | boolean | Required | Adds 9-tryte address checksum. |
| total | int | Required | Total number of addresses to generate. |
| returnAll | boolean | Required | If `true`, it returns all addresses which were deterministically generated (until findTransactions returns null). |
| start | int | Required | Starting key index. |
| end | int | Required | Ending key index. |
| inclusionStates | boolean | Required | If `true`, it gets the inclusion states of the transfers. |
| threshold | long | Required | Min balance required. |
    
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
    GetAccountDataResponse response = iotaAPI.getAccountData("QQRDERCUPBCMCTBDYGYMYHIZRTQQKHGX9QSEWIBHEK9RKIB9MXBC9XEFYUZQSIBMUIENCWA9KEUIIOBTQ", "446", "763", "false", "302", "true", "921", "626", "false", "100");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
