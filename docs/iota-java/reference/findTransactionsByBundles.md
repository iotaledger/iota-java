
# [findTransactionsByBundles](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPICore.java#L243)
 [FindTransactionResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/FindTransactionResponse.java) findTransactionsByBundles(String[] bundles)

Find the transactions by bundles
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| bundles | String[] | Required | A List of bundles. |
    
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
    FindTransactionResponse response = iotaAPI.findTransactionsByBundles(["RZRRXFQQWKNLNUY9MNVLHUIMHIUBVJEJTSHHWHAFPVZYCRKUBXMPYNOLLEBEEYWADCOJSQUFXLZDBPVOA", "JQWWQFVIEVHWCCPHKLWOKCOULANXUXZZZKPOBKCZXZFR9FGTEBDKHKYSXDPYKHKEEGACBRZY9GY9OOQTM"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
