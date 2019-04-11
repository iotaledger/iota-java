
# [findTransactionObjectsByAddresses](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L395)
 List<[Transaction](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transaction.java)> findTransactionObjectsByAddresses(String[] addresses)

Wrapper function for findTransactions, getTrytes and transactionObjects.  Returns the transactionObject of a transaction hash. The input can be a list of valid addresses.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| addresses | String[] | Required | The addresses. |
    
## Output
| Return type | Description |
|--|--|
| List<[Transaction](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transaction.java)>  | Transactions. |



 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    List response = iotaAPI.findTransactionObjectsByAddresses(["WOKMCCHEQBLAYGGDJZJKJSZSIPKNAKZNUB9JATOVQBEDERTSTPQQOJSPAXVVOXERFJDWEHWIEVLUIYSPQV9ZTGPMMWRMBIUQWX9CH9BHNYQUGEPK9BCXRUJO9VKLWBPJV9KOGHDTMWYJQDITSBLSIGDXHLFGNNHCWI", "TPZG9VORTYFHBYUAJQCZETCPW9THPGAGXELHGLULCTXKHNQJQSGOVUYNJZJOYVXNHREMRTSGMVCF9S9WGFFGIHZIHTCJGJ9EFTJGS9BVRMMOKQHLPWMQTRRPJGHRPG9YDPCXNGSJUBGDADJWGC9UNUYICNCTRKYNCV"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
