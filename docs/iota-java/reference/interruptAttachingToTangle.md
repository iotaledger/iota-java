
# [interruptAttachingToTangle](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPICore.java#L469)
 [InterruptAttachingToTangleResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/InterruptAttachingToTangleResponse.java) interruptAttachingToTangle()

Interrupts and completely aborts the attachToTangle process.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

    
## Output
| Return type | Description |
|--|--|

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) |  |


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
