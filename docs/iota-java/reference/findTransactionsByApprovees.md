
# [findTransactionsByApprovees](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPICore.java#L255)
 [FindTransactionResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/FindTransactionResponse.java) findTransactionsByApprovees(String[] approvees)

Find the transactions by approvees
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| approvees | String[] | Required | A List of approvess. |
    
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
    FindTransactionResponse response = iotaAPI.findTransactionsByApprovees(["RUVCZBTTWXQZMSYKTSRSMIZUQYONLXSTKIVUIHZYLEMQLLDUNFONACBCMORMW9EOWYFDCHY9ZSETBZDBR", "EMBPJBNZDETSFBBOIMBKJSZ9R9SQZRKDMNG9JTQJYDCPQXQMPGACYSYOC99ZZCW9UKVSFIBDWHMIAHSNY"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
