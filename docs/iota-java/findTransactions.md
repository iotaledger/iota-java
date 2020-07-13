
# [findTransactions](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L245)
 [FindTransactionResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/FindTransactionResponse.java) findTransactions(String[] addresses , String[] tags , String[] approvees , String[] bundles)

 Find the transactions which match the specified input and return. All input values are lists, for which a list of return values (transaction hashes), in the same order, is returned for all individual elements. The input fields can either be `bundles`, `addresses`, `tags` or `approvees`. 
  Using multiple of these input fields returns the intersection of the values. Can error if the node found more transactions than the max transactions send amount
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| addresses | String[] | Required | Array of hashes from addresses, must contain checksums |
| tags | String[] | Required | Array of tags |
| approvees | String[] | Required | Array of transaction hashes |
| bundles | String[] | Required | Array of bundle hashes |
    
## Output
[FindTransactionResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/FindTransactionResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| String[] hashes | Gets the hashes. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | The request was considered wrong in any way by the node |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | If any of the parameters are not empty but have invalid values |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    FindTransactionResponse response = iotaAPI.findTransactions(new String[]{"HYDQKLICCRNJIGUWADSCXKPGQDBGM9NAYFTXTWACGCCRFFNTSPCORYDTFL9HSPAFXDXVZQAFUHDPCVEZS", "RVPEUHEKB9KVINKVTPMCYZLAFCHEGLDQC9Y9ZHISAGKT9ECGCNELELJEQB9CGDCJASOW9KXHDS9BOUPTX"}, new String[]{"TAG9MTJDPURKDGT9AXPVQZBILBQ", "TAG9LOKRKWXSSHOJKA9WTNLOTZA"}, new String[]{"WFXPHUDKZSSMOCIZUKAWBWBZVQPSZFSHADAJQZYQVIKYZABDSRNLAMERJKTSY9N9LYRUQJHKFCDFWQY9E", "STAQJZMMTSPMUKUL9KPFFKHTZFGLFTFSIWLGXPQ9IOIHMJGJBQNWGCLQUTBLDQRLGVPXHOKSTTPFECGNK"}, new String[]{"ACPVHRXNKXQBTDUCGPEXAVOCJRFNRKFWNBZJFCQDORADTELDYXXBBNUDEYQXQRLIASHSNFMBIUCEXRCCB", "UEQGRYMVAIJJYCHSEDBJOQFVAXRPZZYDAQBURXBBUQNJCGWUKWFZOOLKENMA9GEGPCLROYJJVCDBVFKSA"});
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
