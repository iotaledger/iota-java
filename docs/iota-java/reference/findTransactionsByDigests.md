
# [findTransactionsByDigests](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPICore.java#L268)
 [FindTransactionResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/FindTransactionResponse.java) findTransactionsByDigests(String[] digests)

Find the transactions by digests
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| digests | String[] | Required | A List of digests. |
    
## Output
| Return type | Description |
|--|--|
| [FindTransactionResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/FindTransactionResponse.java)  | The transaction hashes which are returned depend on the input. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) |  |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    FindTransactionResponse response = iotaAPI.findTransactionsByDigests(["ITVMSGHCOUASPAVEPUCMOOYFJXOKRSRCW9DLWLNYBF9PRXAARGJAFMDYP9XGMNAIGQMVPGCLHJQRVOYHQ", "AYVWTXIGSKNNZIFEBDAKJJPLCEWXYKEHS9QJVDCIZQUS9JPIXLVIHUYXFUZCB9EXZRUEHGQTMIWRKROKB"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
