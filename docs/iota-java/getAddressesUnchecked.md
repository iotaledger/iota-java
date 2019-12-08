
# [getAddressesUnchecked](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L158)
 [GetNewAddressResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetNewAddressResponse.java) getAddressesUnchecked([AddressRequest](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/builder/AddressRequest.java) addressRequest)

Generates `amount` of addresses, starting from `index` This does not mean that these addresses are safe to use (unspent)
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| addressRequest | [AddressRequest](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/builder/AddressRequest.java) | Required | [AddressRequest](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/builder/AddressRequest.java) |
    
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
    GetNewAddressResponse response = iotaAPI.getAddressesUnchecked(new AddressRequest.Builder().amount(5).checksum(true).build());
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
