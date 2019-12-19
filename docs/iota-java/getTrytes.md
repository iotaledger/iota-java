
# [getTrytes](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L409)
 [GetTrytesResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetTrytesResponse.java) getTrytes(String[] hashes)

Returns the raw transaction data (trytes) of a specific transaction. These trytes can then be easily converted into the actual transaction object. You can use [Transaction(String)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java#L145) for conversion to an object.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| hashes | String[] | Required | The transaction hashes you want to get trytes from. |
    
## Output
[GetTrytesResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetTrytesResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| String[] trytes | Gets the trytes. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | The request was considered wrong in any way by the node |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when a transaction hash is invalid |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetTrytesResponse response = iotaAPI.getTrytes(new String[]{"FXZODREIAJEUTMA9YAJB9XNW9K9RBNRFJBWUCUSFGG9TNZTUCHHUIFFIAURNRRXYMHLXFXVDQPDOKHHZC", "YPKYGMRAIFFEUSPZZQMGQFRHRTVTDFKMCSXLHQLPPFQSZIVJHREYDAILPJIHDDQDYXLIOF9TCMBQHSWGN"});
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
