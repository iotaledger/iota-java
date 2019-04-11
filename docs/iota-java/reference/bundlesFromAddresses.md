
# [bundlesFromAddresses](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L235)
 [Bundle[]](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Bundle.java) bundlesFromAddresses(String[] addresses, Boolean inclusionStates)

Internal function to get the formatted bundles of a list of addresses.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| addresses | String[] | Required | List of addresses. |
| inclusionStates | Boolean | Required | If `true`, it gets the inclusion states of the transfers. |
    
## Output
| Return type | Description |
|--|--|
| [Bundle[]](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Bundle.java)  | A Transaction objects. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) | is thrown when the specified input is not valid. |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    Bundle response = iotaAPI.bundlesFromAddresses(["QDMOERAXIV9JRI9XRZRAXBFOHPKNOZ9GLHQYAAMLTTUMSQBKTULNJKFGEGFBSKXMYNRURORWFYBXPDRHVPLNWXVUFYTAFVRZDHGOWOGINSLJOANIMKIOFGRASDNTKBGHZPW9GKNZLHAXAFRZDUYPZTXSLIKVZZKXBY", "WGZXFIBKKBAUJAMCPVDGXHBAHPJDWQSHJCBYJSCERWQHNDHUKFVVMYXCPXSSMIDOIAVRWZZOOODE9CWTDOGFWTWVNZUBEPMICJXWXPUZADBWCPKDGMYLRVFTXTDVKOZEIBPMUQSHVNMASDRC9TNLVTWWHXXTVEYAPO"], "false");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
