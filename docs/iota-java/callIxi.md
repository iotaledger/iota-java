
# [callIxi](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L289)
 [IotaCustomResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/IotaCustomResponse.java) callIxi(String module , Map<String, Object> params)

Custom API call used to call IXI modules.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| module | String | Required | The module name, or the folder of the ixi on the node |
| params | Map<String, Object> | Required | Optional arguments for the IXI module, |
    
## Output
[IotaCustomResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/IotaCustomResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| Map<String, Object> ixi | Returns the IXI data in a map.  This is not a clone, so changes reflect the response field. |
| Object arg | Gets an argument from the IXI responseThe Object assigned to that name, or `null` if [hasArg(String)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/IotaCustomResponse.java#L28) returns `false`The name of the argument |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | If the ixi failed processing |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    Map<String, Object> params = new HashMap<>();
    params.put("customparam", "somevariable");
    IotaCustomResponse res = api.callIxi("customixi.customcall", params);
    System.out.println("Duration: " + res.getDuration());
    for (String key : res.getIxi().keySet()) {
        System.out.println(key + ": " + res.getArg(key));
    }
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
