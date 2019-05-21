
# [getNextAvailableAddress](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L131)
 [GetNewAddressResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetNewAddressResponse.java) getNextAvailableAddress(String seed , int security , boolean checksum , int index)

Checks all addresses until the first unspent address is found.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| seed | String | Required | Tryte-encoded seed. It should be noted that this seed is not transferred. |
| security | int | Required | Security level to be used for the private key / address. Can be 1, 2 or 3. |
| checksum | boolean | Required | Adds 9-tryte address checksum. Checksums are required for all API calls. |
| index | int | Required | Key index to start search from. |
    
## Output
[GetNewAddressResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetNewAddressResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| List<String> addresses | Gets the addresses. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | When the seed is invalid |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | When the security level is wrong. |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetNewAddressResponse response = iotaAPI.getNextAvailableAddress("YMDJWPCOZIXVUDBWVQ9CUEFUHLTRAEPMKTIYPTHXUMIQBQAFFHTHDOXYTHNHFPMOMAFJAMIH9IQALORPK", "384", "true", "495");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
