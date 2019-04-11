
# [checkConsistency](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPICore.java#L408)
 [CheckConsistencyResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/CheckConsistencyResponse.java) checkConsistency(String[] tails)

Checks the consistency of the subtangle formed by the provided tails.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| tails | String[] | Required | The tails describing the subtangle. |
    
## Output
| Return type | Description |
|--|--|
| [CheckConsistencyResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/CheckConsistencyResponse.java)  | The The the raw transaction data (trytes) of a specific transaction. |



 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    CheckConsistencyResponse response = iotaAPI.checkConsistency(["ITFEBTJVQVWWZPHIXXB9IZNMESZSS9CFU9GZXWJJEJYKCKDHM9CKXZUWZBKLNRUMRBIVUV9EJVRZWRRBL", "N9ZDAGEUASCFSZIIYRROFQRAXLFJPOLMKEEEVTKRRMQXAKGXRLFOTGFCBCKPQDHANGWDOSFZIOJTCOWSM"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
