
# [getTransfers](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L274)
 [GetTransferResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetTransferResponse.java) getTransfers(String seed , int security , Integer start , Integer end , Boolean inclusionStates)

Finds all the bundles for all the addresses based on this seed and security.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| seed | String | Required | Tryte-encoded seed. It should be noted that this seed is not transferred. |
| security | int | Required | Security level to be used for the private key / address. Can be 1, 2 or 3. |
| start | Integer | Required | Starting key index, must be at least 0. |
| end | Integer | Required | Ending key index, must be bigger then `start` |
| inclusionStates | Boolean | Required | If `true`, it also gets the inclusion states of the transfers. |
    
## Output
[GetTransferResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetTransferResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| [Bundle[]](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Bundle.java) transfers | Gets the transfer bundle. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when <tt>start</tt> and <tt>end</tt> are more then 500 apart |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | Invalid security index |
| IllegalStateException | When the seed is invalid |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetTransferResponse response = iotaAPI.getTransfers("NIJTKADLAFUTBSHXPCUXGKGAVQAGBYZQLA9MLKAIEU9PHUMLWTWIBHQEFMLCTULZCMJAFBMEKHAOCBTGB", "562", "714", "954", "true");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
