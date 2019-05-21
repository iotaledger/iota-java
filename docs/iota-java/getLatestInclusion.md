
# [getLatestInclusion](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L1147)
 [GetInclusionStateResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetInclusionStateResponse.java) getLatestInclusion(String[] hashes)

Wrapper function: runs getNodeInfo and getInclusionStates Uses the latest milestone as tip
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| hashes | String[] | Required | The hashes. |
    
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
| [getNodeInfo()](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L147) | Returns information about this node. |
| [getInclusionStates(String[], String[])](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L354) |  Get the inclusion states of a set of transactions. This is for determining if a transaction was accepted and confirmed by the network or not. You can search for multiple tips (and thus, milestones) to get past inclusion states of transactions. 
  This API call returns a list of boolean values in the same order as the submitted transactions. Boolean values will be `true` for confirmed transactions, otherwise `false`. 
 |

 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetInclusionStateResponse response = iotaAPI.getLatestInclusion(["QZYJSEILNQQLXGZKQLFVVFCOSBTVQJLJGMFQLAUKCQHCLBYCDKFJXFXHNEWYTQEPBKVRAPRESBPNF9MED", "HZMFPPVLTRQYTBZBSNNOZRTQKJRQGRTNMDER9JBAMYSGQNRHGHFSSFAPV9OHTYWOONDWRAORMQMCZKRZ9"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
