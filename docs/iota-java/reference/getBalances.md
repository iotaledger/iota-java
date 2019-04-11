
# [getBalances](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPICore.java#L349)
 [GetBalancesResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetBalancesResponse.java) getBalances(Integer threshold, String[] addresses, String[] tips)

Similar to getInclusionStates.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| threshold | Integer | Required | The confirmation threshold, should be set to 100. |
| addresses | String[] | Required | The array list of addresses you want to get the confirmed balance from. |
| tips | String[] | Required | The starting points we walk back from to find the balance of the addresses |
    
## Output
| Return type | Description |
|--|--|
| [GetBalancesResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetBalancesResponse.java)  | The confirmed balance which a list of addresses have at the latest confirmed milestone. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) |  |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetBalancesResponse response = iotaAPI.getBalances("100", ["FRSHLMJLMNCGKTDBNUSHCYZWTIEEWUAOTXZJWMOUUKNGIZAUBZBEZBTAFFFTSDLOIKUYDNOVO9WODJYOWUGLATZKQEAUQSBBHRIKXODDCDRXIAUQMIIGWEUTKQ9RBROFKJYNQINSAK9POJGDQWAXTLKKIAXDPBFFAF", "UOSNMFRVMBWNUWNIPRJUWVQLYYQBZPNOGFYWHBJVIVQVFTKIKJLMZXFDTOXHPQ9IVHJ9FVVBD9NDGQNZAAWTDSPMRCKPVRAYARMYXCDIYXQFAVNEFPOACTEBCBOBBGXFUQFYVZMXGSLESPPQQPBJYFRXOYHJGAJRVY"], ["GLJVMPNUATSFCQYMVHQWWFOKEDRRJTDERHEGUKKFDZNILGOMABZPJOHQ9ATPUSDAXZGCWCHBNIUMXXILN", "XOLQAMUWACIAKFS9JXOTTAZLBEYHCLCTPVRNKQMBPJQLLVNQOXOEBFTXGZGPSO9PGRVNUXORDENE9BMKC"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
