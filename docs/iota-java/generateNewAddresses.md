
# [generateNewAddresses](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L86)
 [GetNewAddressResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetNewAddressResponse.java) generateNewAddresses([AddressRequest](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/builder/AddressRequest.java) addressRequest)

Generates new addresses, meaning addresses which were not spend from, according to the connected node. Stops when [getAmount()](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/builder/AddressRequest.java#L104) of unspent addresses are found, starting from [getIndex()](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/builder/AddressRequest.java#L96) If [getAmount()](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/builder/AddressRequest.java#L104) is set to 0, we will generate until the first unspent address is found, and stop.
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



 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetNewAddressResponse response = iotaAPI.generateNewAddresses(new AddressRequest.Builder().amount(5).checksum(true).build());
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
