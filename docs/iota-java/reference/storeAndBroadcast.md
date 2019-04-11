
# [storeAndBroadcast](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L318)
 [BroadcastTransactionsResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/BroadcastTransactionsResponse.java) storeAndBroadcast(String[] trytes)

Wrapper function that stores and broadcasts the specified trytes.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| trytes | String[] | Required | The trytes. |
    
## Output
| Return type | Description |
|--|--|
| [BroadcastTransactionsResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/BroadcastTransactionsResponse.java)  | A BroadcastTransactionsResponse. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) | is thrown when the specified input is not valid. |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    BroadcastTransactionsResponse response = iotaAPI.storeAndBroadcast(["FAMASKNFZWAIMOVCJBAJBMRCXHWVGMFLGPEONWC9MFSMEXFNQCRRCNVWOJVXMEWAAALLXNLEZERUZDGGCCNTEPSIEGKXDUSISW9VETHENKZBETSUABAWTPGBUIMJTCWWZNRWL9KOVJOEJKWJFYLDGYPBTJIBTZAXER", "BHBXFCB9EETR9DDEWJBRALAIH9VDSAOLAZR9NIDFWGIH9PEQOTIFUCTJCYJRMTZTELC9THTGOAPYQGLHZKOOBYJDYVK9OGLVPXZBOJIPMGRHJGOYCKTXSBRMTLYYOZLAJYKH9P9BHTHBPONTP9JNLUIOLFCPHFHGCQ"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
