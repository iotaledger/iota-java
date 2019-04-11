
# [traverseBundle](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L970)
 [Bundle](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Bundle.java) traverseBundle(String trunkTx, String bundleHash, [Bundle](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Bundle.java) bundle)

Basically traverse the Bundle by going down the trunkTransactions until  the bundle hash of the transaction is no longer the same. In case the input  transaction hash is not a tail, we return an error.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| trunkTx | String | Required | Hash of a trunk or a tail transaction of a bundle. |
| bundleHash | String | Required | The bundle hash. |
| bundle | [Bundle](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Bundle.java) | Required | bundle to be populated. |
    
## Output
| Return type | Description |
|--|--|
| [Bundle](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Bundle.java)  | Transaction objects. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) | is thrown when an invalid input is provided. |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    Bundle response = iotaAPI.traverseBundle("UOBCKZTSWCMJFW9FVQLR9DTCZ9IAXWJZJN9PS9W9FZNHUTTGXCTFEYWWZNPSZYJTVCRARTLZOTTZRCCFH", "9SCAGKUUQBUFLFDSUWWYIFVCLRNQYHWHVMFBVDXOIFCZ9PZIGRDMVEN9ZIABGIQDZTKSPDAFHCHZWSB9P", "bundle");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
