
# [getInclusionStates](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPICore.java#L282)
 [GetInclusionStateResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetInclusionStateResponse.java) getInclusionStates(String[] transactions, String[] tips)

Get the inclusion states of a set of transactions. This is for determining if a transaction was accepted and confirmed by the network or not.  Search for multiple tips (and thus, milestones) to get past inclusion states of transactions.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| transactions | String[] | Required | The list of transactions you want to get the inclusion state for. |
| tips | String[] | Required | List of tips (including milestones) you want to search for the inclusion state. |
    
## Output
| Return type | Description |
|--|--|
| [GetInclusionStateResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetInclusionStateResponse.java)  | The inclusion states of a set of transactions. |



 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetInclusionStateResponse response = iotaAPI.getInclusionStates(["ALLS9D9RVMQYJFWNWNMUOVTQTZNFETOSTMMNAUYDRLJYZYSHTECYTTYZZ9ERKGIQBEWSWJCBPGVGWXEDC", "DCHGWSWFGZUNOKONW9J9AXCMOKMGHVJMY9HDFJQUCLAFGWDPDEZCSMRUTGZFSFSTWWGDJYLBTUWYIDOKZ"], ["XKMXR9HJBTTN9LLGAHB9UNXRQYJUMMQXHACPJUSTBYIFHRAVLLMAEOCJHMRNPHYCLUNUNNJBUAGIMIGXH", "AKC99MIUPLSUGLXQREUSDGIXGZVNIEMEIOLCJMAGDAMICA9LOOTHVAFIS9UKIPNOWXCYIPYVLTKBR9RRB"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
