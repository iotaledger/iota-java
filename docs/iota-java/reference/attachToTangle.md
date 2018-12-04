
# [attachToTangle](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPICore.java#L427)
 [GetAttachToTangleResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetAttachToTangleResponse.java) attachToTangle(String trunkTransaction, String branchTransaction, Integer minWeightMagnitude, String[] trytes)

Attaches the specified transactions (trytes) to the Tangle by doing Proof of Work.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| trunkTransaction | String | Required | The trunk transaction to approve. |
| branchTransaction | String | Required | The branch transaction to approve. |
| minWeightMagnitude | Integer | Required | The Proof of Work intensity. |
| trytes | String[] | Required | A List of trytes (raw transaction data) to attach to the tangle. |
    
## Output
| Return type | Description |
|--|--|



 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetAttachToTangleResponse response = iotaAPI.attachToTangle("QTIGDMUKDLSVQEUISWEKRHKQMOEWTHOXMQP9WMPSFWOVAIW9BIIKYTOMEAXZWLAPILUQIJWXHWSDFQVCM", "9VFGKCQSSNCBQKNWESGSXFKCMDOJWBBYDPRKXZYBYGSDLRBZMFZWXKJGRQHXMZPXVXUPRTIBKERWBUEMV", "18", ["GTRSCGYWXGAOILFPWTMZUTFKGBDVFLHYRPOFFNX9DTZXENM9WXOKX9HCIYSEONOINLPLYECCGPQGXSPTKYFFTZNHAOVLWCRAMVNHRBTOLTLQFNFWNSW9OBP9QMLXAFEIYLGMGEUCZI9PZHVEQFDACEBPWOFDQZSCLH", "PETTNFNWUNEVFJYIMAQZACBEXCTARWHVVFULTECBCQNHTGFTML9FSGARJHHIPVFAONTCGYCCPAWQFPG9OXSYBDOHKIPPEYSIVSMPPGKWXCRTXURWCI9JNNVZUTGMTVEKEXJQXGCCJXK9QOXVOLIRWJUFOJYIGUOTYY"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
