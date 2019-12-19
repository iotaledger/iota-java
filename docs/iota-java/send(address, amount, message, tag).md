
# [send](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAccount.java#L382)
 Future<[Bundle](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Bundle.java)> send(String address , long amount , Optional<String> message , Optional<String> tag)

Sends a transfer using the accounts balance to the provided address. You must call `.get()` on the future in order to start this transfer.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| address | String | Required | The receiver of this transfer |
| amount | long | Required | The amount we are sending to the address |
| message | Optional<String> | Optional | An optional message for this transfer |
| tag | Optional<String> | Optional | An optional tag for this transfer |
    
## Output
| Return type | Description |
|--|--|
| Future<[Bundle](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Bundle.java)>  | The bundle we sent |



 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    Future<Bundle> response = iotaAPI.send("WCNGLG9XTVGGREAJMEVNTNGAJOOXONDPBEOWNJPOYQAZIWXTSXBVKOAFFUQCHMMFMEJZWLHDXQVLWILAV", 472, Optional.of("BSBPKIWVIXKNVWUBHUYOAJ9RVBUUOKYFUACLKUMRWQSER9MDJGDHNMFVBJVSBU9QCFZPQUGKDQIV9AKZZ", Optional.of("TAG9MMCOBJKWNHOBSMPDUWXZTUX");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
