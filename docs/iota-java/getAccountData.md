
# [getAccountData](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L993)
 [GetAccountDataResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetAccountDataResponse.java) getAccountData(String seed , int security , int index , boolean checksum , int total , boolean returnAll , int start , int end , boolean inclusionStates , long threshold)

Similar to getTransfers, just that it returns additional account data
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| seed | String | Required | Tryte-encoded seed. It should be noted that this seed is not transferred. |
| security | int | Required | Security level to be used for the private key / address. Can be 1, 2 or 3. |
| index | int | Required | Key index to start search from. If the index is provided, the generation of the address is not deterministic. |
| checksum | boolean | Required | Adds 9-tryte address checksum. Checksum is required for all API calls. |
| total | int | Required | Total number of addresses to generate. |
| returnAll | boolean | Required | If `true`, it returns all addresses which were deterministically generated (until findTransactions returns null). |
| start | int | Required | Starting key index, must be at least 0. |
| end | int | Required | Ending key index, must be bigger then `start` |
| inclusionStates | boolean | Required | If `true`, it gets the inclusion states of the transfers. |
| threshold | long | Required | Minimum balance required. |
    
## Output
[GetAccountDataResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetAccountDataResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| List<String> addresses | Gets the addresses. |
| [Bundle[]](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Bundle.java) transfers | Gets the transfers. |
| List<[Input](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Input.java)> input | Gets the inputs. |
| long balance | Gets the balance. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when the specified security level is not valid. |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when <tt>start</tt> and <tt>end</tt> are invalid |

## Related APIs (link to other product documentation)
| API     | Description |
|:---------------|:--------|
| [getTransfers(String, int, Integer, Integer, Boolean)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L274) | Finds all the bundles for all the addresses based on this seed and security. |

 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetAccountDataResponse response = iotaAPI.getAccountData("GMPXWHYRJRCEJQPOZFWEJJNRIPXXGEOZBRFGRILQJIOFVKURVAUHTVHGGXQWXJYPBSXGNZNUHYKSUEDJV", "85", "408", "false", "562", "false", "829", "579", "true", "100");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
