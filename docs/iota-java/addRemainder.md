
# [addRemainder](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L1598)
 List<String> addRemainder(String seed , int security , List<[Input](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Input.java)> inputs , [Bundle](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Bundle.java) bundle , String tag , long totalValue , String remainderAddress , List<String> signatureFragments)

Uses input, and adds to the bundle, untill `totalValue` is reached. If there is a remainder left on the last input, a remainder transfer is added.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| seed | String | Required | The tryte-encoded seed. It should be noted that this seed is not transferred. |
| security | int | Required | Security level to be used for the private key / address. Can be 1, 2 or 3. |
| inputs | List<[Input](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Input.java)> | Required | List of inputs used for funding the transfer. |
| bundle | [Bundle](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Bundle.java) | Required | The [Bundle](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Bundle.java) to be populated. |
| tag | String | Required | The tag to add to each bundle entry (input and remainder) |
| totalValue | long | Required | The total value of the desired transaction |
| remainderAddress | String | Required | The address used for sending the remainder value (of the last input).   If this is `null`, [getNextAvailableAddress(String, int, boolean)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L115) is used. |
| signatureFragments | List<String> | Required | The signature fragments (message), used for signing.Should be 2187 characters long, can be padded with 9s. |
    
## Output
| Return type | Description |
|--|--|
| List<String>  | A list of signed inputs to be used in a transaction |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | When the seed is invalid |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | When the security level is wrong. |
| IllegalStateException | When the inputs do not contain enough balance to reach <tt>totalValue</tt>. |

## Related APIs (link to other product documentation)
| API     | Description |
|:---------------|:--------|
| [signInputsAndReturn(String, List, Bundle, List, org.iota.jota.pow.ICurl)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/utils/IotaAPIUtils.java#L64) | Finalizes and signs the bundle transactions. Bundle and inputs are assumed correct. |
| [getNextAvailableAddress(String, int, boolean)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L115) | Checks all addresses until the first unspent address is found. Starts at index 0. |

 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    List<String> response = iotaAPI.addRemainder("GJRKVXECSQFPQOEMFNKWZLBKMYXUW9ZX9DCBBKFUFFICTCL9BXCWDEUQWHWRDYYSJEBYRRTJJZEVFXROV", "365", ["inputs", "inputs"], "bundle", "OVTBVZF9UHIMBKAE9NQZUNWZ9AUWGAMTKUPG9BOEOS9LFYYYXGPCOZWVXARKK9QXJJEQITVIODK9AZNOG", "totalvalue", "VIGLBXE9SWTYGJYDLSPNGVTRLIVRVGHXFMMZAIXNGFWZVQYXMQISB99MACJ9XUOVMSXALC99YPWAVCCJL", ["AFGGZR9I9GCPKYAE9PEWRIUDIUBQKQWUOR9BIL9QKUQOTAKTNKYLRTBQ9HL9NRULMPJIDEWIEAMWLLERI", "YHKJKHNRUFGRJ9CYZVFOJXLAWNWC9OEPXMTNPLNUBGKKFI9AOBWTNQZPARWILW9FHSUVRSLFNJNTLVI9K"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
