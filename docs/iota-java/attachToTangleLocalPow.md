
# [attachToTangleLocalPow](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L668)
 [GetAttachToTangleResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetAttachToTangleResponse.java) attachToTangleLocalPow(String trunkTransaction , String branchTransaction , Integer minWeightMagnitude , [IotaPoW](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaPoW.java) pow , String[] trytes)

 Prepares the specified transactions (trytes) for attachment to the Tangle by doing Proof of Work. You need to supply `branchTransaction` as well as `trunkTransaction`. These are the tips which you're going to validate and reference with this transaction.  These are obtainable by the `getTransactionsToApprove` API call. 
  The returned value is a different set of tryte values which you can input into  `broadcastTransactions` and `storeTransactions`. 
  The last 243 trytes of the return value consist of the following:  * `trunkTransaction`
 * `branchTransaction`
 * `nonce`
   These are valid trytes which are then accepted by the network.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| trunkTransaction | String | Required | A reference to an external transaction (tip) used as trunk. The transaction with index 0 will have this tip in its trunk. All other transactions reference the previous transaction in the bundle (Their index-1). |
| branchTransaction | String | Required | A reference to an external transaction (tip) used as branch.  Each Transaction in the bundle will have this tip as their branch, except the last.  The last one will have the branch in its trunk. |
| minWeightMagnitude | Integer | Required | The amount of work we should do to confirm this transaction.Each 0-trit on the end of the transaction represents 1 magnitude.A 9-tryte represents 3 magnitudes, since a 9 is represented by 3 0-trits.   Transactions with a different minWeightMagnitude are compatible. |
| pow | [IotaPoW](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaPoW.java) | Required | Method of proof of work |
| trytes | String[] | Required | The list of trytes to prepare for network attachment, by doing proof of work. |
    
## Output
[GetAttachToTangleResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetAttachToTangleResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| String[] trytes | Gets the rytes. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when a trunk or branch hash is invalid |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when the provided transaction trytes are invalid |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetAttachToTangleResponse response = iotaAPI.attachToTangleLocalPow("DQSUOEQAV9OQXZCXIZEEXPUJFGMOENTTHWAWXJGWIHDAYFPQCCYE9EBBAAMQSPXHDKYEBDJWJUPSVXEVH", "SDIRWOWZUGGBOJXSGXZ9KYDRSFAFOVSDNZFRP9K9OJPRSYHDJYMKTBVDLEARMMGJROQCQWKC9JACWBJXH", 18, pow, new String[]{"GYEXOBZRRWAWYQMHADRMXBKYZ ... CAZZYKPSHWMMOUMFKTUVQNLMP", "PROJPEXWYESTHIUVTPREDFCQU ... TUOVFCABJPVCXRBFLLWOGYJQX"});
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
