
# [findTransactionsByAddresses](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPICore.java#L224)
 [FindTransactionResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/FindTransactionResponse.java) findTransactionsByAddresses(String[] addresses)

Find the transactions by addresses
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| addresses | String[] | Required | A List of addresses. |
    
## Output
| Return type | Description |
|--|--|
| [FindTransactionResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/FindTransactionResponse.java)  | The transaction hashes which are returned depend on the input. |



 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    FindTransactionResponse response = iotaAPI.findTransactionsByAddresses(["ASLPXGMMOVHXSWVFQTLKLCHYPKH9YJCZMIDWMWRUOZEHOXHIIVLHUEKPWGVZTLBJKIHVBHOPAPU9MHJYDWTYUQRUVOW9JSPOFIYOKKPAUDUXFOCAYVF9FOPZYKZECYLHXJNLOJAM9CMSDQHUDJTLFOCKFTFXPETNVU", "DEQJNGQZFFZUZP9OVJOHELGZDPVXGZXPXZGHYYSIG9STGATWPKW9GURTETHYFOJEBCJCYUIHDHFLOVLQJT9DVZHQVEAQFOXGDZWGRQFZXYO9IAWAYPPJNSDKCCZAYUBLCGNHFJECESM9XOVPAKAMTROTFNMKVZKYKM"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
