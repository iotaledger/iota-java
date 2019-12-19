
# [wereAddressesSpentFrom](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L547)
 [WereAddressesSpentFromResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/WereAddressesSpentFromResponse.java) wereAddressesSpentFrom(String[] addresses)

Check if a list of addresses was ever spent from, in the current epoch, or in previous epochs.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| addresses | String[] | Required | List of addresses to check if they were ever spent from. Must contain the checksum. |
    
## Output
[WereAddressesSpentFromResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/WereAddressesSpentFromResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| boolean[] states | Gets the states. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when an address is invalid |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    WereAddressesSpentFromResponse response = iotaAPI.wereAddressesSpentFrom(new String[]{"PFDSTQIZRRPNZVBNQTLRIXSIGDLLR9TZGVSXNREDAUMCYUAS9NZZZXCGY9UZEMMZCMBXLKIKNXHZRUEBG", "VIELNLTLAEKCHJTKNGHSGAKCFFLRVW9JFHZKPXRVKNWQDIWGIOWHFZZNTBNFMOXDMFFYMPWGHVVZITBJD"});
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
