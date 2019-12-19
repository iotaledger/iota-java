
# [findTransactionsByAddresses](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L304)
 [FindTransactionResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/FindTransactionResponse.java) findTransactionsByAddresses(String[] addresses)

Find the transactions by addresses with checksum
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| addresses | String[] | Required | An array of addresses, must contain checksums |
    
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
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | If one of the addresses is invalid |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    FindTransactionResponse response = iotaAPI.findTransactionsByAddresses(new String[]{"YFKPGRTWIUGYNLJFKNYOBVMSAANNM9RGFWUPIQZFDTLTJOCYGRWRSOWZRDIQNRYSNOPOUDPZKTZLWZAWP", "CABNRPMYNEPXMGCXYC99CIQMWPW9PLVALMPFOKWXDRFIAMIFTGOSINIMDPALDFZUOFMMECAIJZMWRIGCC"});
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
