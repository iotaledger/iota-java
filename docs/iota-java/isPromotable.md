
# [isPromotable](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L1671)
 boolean isPromotable([Transaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java) tail)

Checks if a transaction hash is promotable
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| tail | [Transaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java) | Required | the [Transaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java) we want to promote |
    
## Output
| Return type | Description |
|--|--|
| boolean  | `true` if it is, otherwise `false` |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when we can't get the consistency of this transaction |

## Related APIs (link to other product documentation)
| API     | Description |
|:---------------|:--------|
| [checkConsistency(String...)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L533) | Checks the consistency of the subtangle formed by the provided tails. |

 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    boolean response = iotaAPI.isPromotable("tail");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
