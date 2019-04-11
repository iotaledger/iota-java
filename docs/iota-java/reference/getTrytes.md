
# [getTrytes](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPICore.java#L305)
 [GetTrytesResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetTrytesResponse.java) getTrytes(String[] hashes)

Returns the raw trytes data of a transaction.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| hashes | String[] | Required | The of transaction hashes of which you want to get trytes from. |
    
## Output
| Return type | Description |
|--|--|
| [GetTrytesResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetTrytesResponse.java)  | The the raw transaction data (trytes) of a specific transaction. |



 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetTrytesResponse response = iotaAPI.getTrytes(["SOS9CRGTDKQAE9ESFJKMXBADHSMFXDCUWJNGAVLIXL9SSHTWIKDXQRNUBRIZWWAAQZTVXZUWCATTJP9KT", "JZXCNFGWCJAPTCNFITQRXKXCZPKZSWJCSPZUETTDKCSQZATYFZUTJYXUUVWABQFUZPMVERHUWDACPOUEX"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
