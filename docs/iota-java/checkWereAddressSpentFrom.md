
# [checkWereAddressSpentFrom](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L1022)
 boolean[] checkWereAddressSpentFrom(String[] addresses)

Check if a list of addresses was ever spent from, in the current epoch, or in previous epochs. Addresses must have a checksum.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| addresses | String[] | Required | the addresses to check |
    
## Output
| Return type | Description |
|--|--|
| boolean[]  | list of address boolean checks |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when an address is invalid |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    boolean response = iotaAPI.checkWereAddressSpentFrom(["WXBYKDMRDYKCQBJWZZFPNHKZLYINE9TLYSETXLOFXQKRUKDVRGWFQHVPSNHJMNIHU9QVPLPQKSNXRBLAV", "JYWRGZDJJCRUOUVZJKNWGMEWOLJEEQLHAIVM9D9GWEZN9YJZWYLOBGUCBBWIMEUQDYZG9HPVAZZDSVLSP"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
