
# [interruptAttachingToTangle](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L721)
 [InterruptAttachingToTangleResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/InterruptAttachingToTangleResponse.java) interruptAttachingToTangle()

Interrupts and completely aborts the `attachToTangle` process.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

    
## Output
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | If node connection fails |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    InterruptAttachingToTangleResponse response = iotaAPI.interruptAttachingToTangle();
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
