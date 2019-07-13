
# [getAddressesUnchecked](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L246)
 [GetNewAddressResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetNewAddressResponse.java) getAddressesUnchecked(String seed , int security , boolean checksum , int index , int amount)

Generates `amount` of addresses, starting from `index` This does not mean that these addresses are safe to use (unspent)
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| seed | String | Required | Tryte-encoded seed. It should be noted that this seed is not transferred. |
| security | int | Required | Security level to be used for the private key / address. Can be 1, 2 or 3. |
| checksum | boolean | Required | Adds 9-tryte address checksum. |
| index | int | Required | Key index to start search from. The generation of the address is not deterministic. |
| amount | int | Required | Total number of addresses to generate. |
    
## Output
[GetNewAddressResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetNewAddressResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| List<String> addresses | Gets the addresses. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | is thrown when the specified input is not valid. |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetNewAddressResponse response = iotaAPI.getAddressesUnchecked("WSYQ9ZEHHRJXOOWUAJTZGXZAFM9DXYIVZ9BXENBLNGNNYMXFDUJMYYQH9CVFBLQQVEGMBCPYFJKHJGIQL", "573", "false", "256", "139");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
