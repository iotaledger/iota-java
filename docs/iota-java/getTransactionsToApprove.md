
# [getTransactionsToApprove](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L435)
 [GetTransactionsToApproveResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetTransactionsToApproveResponse.java) getTransactionsToApprove(Integer depth , String reference)

Tip selection which returns `trunkTransaction` and `branchTransaction`. The input value `depth` determines how many milestones to go back for finding the transactions to approve. The higher your `depth` value, the more work you have to do as you are confirming more transactions. If the `depth` is too large (usually above 15, it depends on the node's configuration) an error will be returned. The `reference` is an optional hash of a transaction you want to approve. If it can't be found at the specified `depth` then an error will be returned.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| depth | Integer | Required | Number of bundles to go back to determine the transactions for approval. |
| reference | String | Required | Hash of transaction to start random-walk from.  This used to make sure the tips returned reference a given transaction in their past.  Can be `null`. |
    
## Output
[GetTransactionsToApproveResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetTransactionsToApproveResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| String trunkTransaction | Gets the trunk transaction. |
| String branchTransaction | Gets the branch transaction. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | The request was considered wrong in any way by the node |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | The depth is invalid |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetTransactionsToApproveResponse response = iotaAPI.getTransactionsToApprove(15, "BDOSJNKBHLCQINXWVENXMZTLLO9GRIFLRGSENIGWOMJZVLOZ9HWFWWMWCRP9FMSIETJKVAYQGNVJBRSFD");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
