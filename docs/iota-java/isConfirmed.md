
# [isConfirmed](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L1097)
 [GetInclusionStateResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetInclusionStateResponse.java) isConfirmed(String[] hashes)

 Get the inclusion states of a set of transactions. This is for determining if a transaction was accepted and confirmed by the network or not. 
  This API call returns a list of boolean values in the same order as the submitted transactions. Boolean values will be `true` for confirmed transactions, otherwise `false`. 
  This is command does the same as [getInclusionStates(String...)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L376) but a copy exists  for readability.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| hashes | String[] | Required | The transaction hashes to check for |
    
## Output
[GetInclusionStateResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetInclusionStateResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| boolean[] states | Gets the states. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when one of the hashes is invalid |

## Related APIs (link to other product documentation)
| API     | Description |
|:---------------|:--------|
| [getInclusionStates(String...)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L376) |  Get the inclusion states of a set of transactions. This is for determining if a transaction was accepted and confirmed by the network or not. 
  This API call returns a list of boolean values in the same order as the submitted transactions. Boolean values will be `true` for confirmed transactions, otherwise `false`. 
 |

 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetInclusionStateResponse response = iotaAPI.isConfirmed(new String[]{"BUYLPZYTHMUECDBEIKGGJTY9CCMUUQRBMDODYCPTQBJECIPSCCOIUOFMMMIBKYEJZBTDFBVYTWTWCWS9T", "TYPRACCNQKOCGIYKYABGN9TLKALAVHN9VJDJFJZLFVGMF9EGBXDEFOPKGWMALCONLFX9DT9XLVLQCYOBX"});
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
