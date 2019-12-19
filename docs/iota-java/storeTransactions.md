
# [storeTransactions](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L753)
 [StoreTransactionsResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/StoreTransactionsResponse.java) storeTransactions(String[] trytes)

Stores transactions in the local storage. The trytes to be used for this call should be valid, attached transaction trytes. These trytes are returned by `attachToTangle`, or by doing proof of work somewhere else.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| trytes | String[] | Required | Transaction data to be stored. |
    
## Output
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when the provided transaction trytes are invalid |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    StoreTransactionsResponse response = iotaAPI.storeTransactions(new String[]{"FZE9UXESVWNRVHRPGEAGHYTUE ... DKSUNMZ9YMOYMPBKOPUXCEWJN", "IGTJJFUYMJDOICSZRXDGIG9ZX ... IIHMKR9VXH9ZTTPOQAULBDACL"});
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
