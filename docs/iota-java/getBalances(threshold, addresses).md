
# [getBalances](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L535)
 [GetBalancesResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetBalancesResponse.java) getBalances(Integer threshold , List<String> addresses)

 Calculates the confirmed balance, as viewed by the latest solid milestone.  In addition to the balances, it also returns the referencing `milestone`,  and the index with which the confirmed balance was determined. The balances are returned as a list in the same order as the addresses were provided as input. 

> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| threshold | Integer | Required | The confirmation threshold, should be set to 100. |
| addresses | List<String> | Required | The list of addresses you want to get the confirmed balance from. Must contain the checksum. |
    
## Output
[GetBalancesResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetBalancesResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| String[] references | Gets the references this balance was requested through |
| int milestoneIndex | Gets the milestone index. |
| String[] balances | Gets the balances. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | The request was considered wrong in any way by the node |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetBalancesResponse response = iotaAPI.getBalances(100, new List<String>(new String[]{"GCRESZFGIT9PZSQSFFJAUVGYLNQNNZWMLOCPYMUTHPCLURBLHFIHKONJRDS9EBEAGJ9ZLDUILMMRBDNKC", "JWIICZDUPTZIZNBSDLTKOUCNLY9FMYOKFBKCEQA9AFUITHDDXSVNIXFRCIDANOLZWNZEGNPCR9DKZIRY9"}));
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
