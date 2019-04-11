
# [broadcastTransactions](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPICore.java#L480)
 [BroadcastTransactionsResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/BroadcastTransactionsResponse.java) broadcastTransactions(String[] trytes)

Broadcast a list of transactions to all neighbors. The input trytes for this call are provided by attachToTangle.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| trytes | String[] | Required | The list of raw data of transactions to be rebroadcast. |
    
## Output
| Return type | Description |
|--|--|



 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    BroadcastTransactionsResponse response = iotaAPI.broadcastTransactions(["LFXSPUUZXQDWFX9SKVPGVPCJY9XCVQMNTPXKNEFORU9ULZZLPDKJT9JEZKJTNEKEZPZHGPJDXOGXMHUNYIABJHFZXIZWWWYYOHMCQSRROZAKYVBJ9MYAMVWLNZQLLRNGWHFVRYTUCMRFQEBQ9ZFPFCN9XQDNPWSKOW", "YSDONKTDLFWBURVTDBNDCPCNDWZNSUIUR9EMSUOLRVGFDZUNOLJTEQLJURY99BWOABZYTITQH9NBUJVLMBCWQB9AGWJEUR9A9CSVVKQTFRWOQQY9RUBUQQHOZVBLS9BCCOZEIRSAIEUCKEQKFGBQMLXHTQZTSJA9WM"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
