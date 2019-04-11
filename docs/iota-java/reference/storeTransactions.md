
# [storeTransactions](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPICore.java#L497)
 [StoreTransactionsResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/StoreTransactionsResponse.java) storeTransactions(String[] trytes)

Store transactions into the local storage. The trytes to be used for this call are returned by attachToTangle.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| trytes | String[] | Required | The list of raw data of transactions to be rebroadcast. |
    
## Output
| Return type | Description |
|--|--|

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) |  |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    StoreTransactionsResponse response = iotaAPI.storeTransactions(["RUBMODXK9QSY9GMTKHHFIBYCJEJQNKDUFNXEQYSETLXVMTXLTIOADTSLX9BCIDUWWVYOQEERVLFWPZDFCWWXQI9LCPALBOOCPXXEQWRFNCAEGQBHVGNYANRXUQRQICIYD9VIGZUCBJGHHI9NIUZDPDQPNORVGXWABD", "FZYSGMTDJKXOG9FABPIVZA9ERDDYUPWRJFIAYSOBSALTWWYFXDMCCZ9OOKQG9MAZTXJCDBNIFYXMHMJLOCW9NPVIFAJVARKYPBIHHQK9SBKPN9JSBOCGD9SELOOXHNLSXJZEJIYV9VXYLVADFFHASWAVHBJ9GMKNVE"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
