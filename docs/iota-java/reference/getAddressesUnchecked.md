
# [getAddressesUnchecked](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L181)
 [GetNewAddressResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetNewAddressResponse.java) getAddressesUnchecked(String seed, int security, boolean checksum, int index, int amount)

Generates `amount` of addresses, starting from `index`  This does not mean that these addresses are safe to use (unspent)
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
| Return type | Description |
|--|--|
| [GetNewAddressResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetNewAddressResponse.java)  | GetNewAddressResponse containing an array of strings with the specified number of addresses. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) | is thrown when the specified input is not valid. |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetNewAddressResponse response = iotaAPI.getAddressesUnchecked("DRRLHEZQJOVIJVOBVHJUGUFTMLMNPIOOVFGDCTUZD9D9UUFCFFH9IFDNMOENPWYGTTEZHERDQGMCMNFNZ", "44", "false", "321", "920");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
