
# [findTransactionsByBundles](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L319)
 [FindTransactionResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/FindTransactionResponse.java) findTransactionsByBundles(String[] bundles)

Find the transactions by bundles
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| bundles | String[] | Required | An array of bundles. |
    
## Output
[FindTransactionResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/FindTransactionResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| String[] hashes | Gets the hashes. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | The request was considered wrong in any way by the node |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | If one of the bundle hashes is invalid |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    FindTransactionResponse response = iotaAPI.findTransactionsByBundles(new String[]{"MFXJXGOUOJCOWXABCVLFSMKRSNPQIYTCVIBH9MARPXJHNUAPVXITOQENNMJYAJVDUYPHLNJZFKGNPUZGH", "FHTFADVQFROCQHSNRFHPWIDPQFAAYJZIURVFXINMWJP9BYYT9UBDTZLPHHYQJSFYVTTNYGMNVNJWELXJX"});
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
