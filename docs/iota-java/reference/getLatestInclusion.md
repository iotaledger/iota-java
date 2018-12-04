
# [getLatestInclusion](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L909)
 [GetInclusionStateResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetInclusionStateResponse.java) getLatestInclusion(String[] hashes)

Wrapper function for getNodeInfo and getInclusionStates  Uses the latest milestone as tip
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| hashes | String[] | Required | The hashes. |
    
## Output
| Return type | Description |
|--|--|
| [GetInclusionStateResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetInclusionStateResponse.java)  | Inclusion state. |



 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetInclusionStateResponse response = iotaAPI.getLatestInclusion(["RCQJENJNZHQYRBCQJGXN9XXQIGSQ99ROHJJZVETXAUZXWWPMUJBQ9BPQOA9YCDHYIXZCHBQMSGQAWPYYP", "DSTAVMKCIYCEUGMEDZRECLJEPVAAWXISKEVTMVXRLXQCBBNEGSYHBJREEORWRIPUUIIVGXYAIBLYLRUQQ"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
