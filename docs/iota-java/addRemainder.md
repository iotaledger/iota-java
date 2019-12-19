
# [addRemainder](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L1528)
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
| remainderAddress | String | Required | The address used for sending the remainder value (of the last input).   If this is `null`, [generateNewAddresses(AddressRequest)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L86) is used. |
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
| IllegalStateException | When the inputs do not contain enough balance to reach `totalValue`. |

## Related APIs (link to other product documentation)
| API     | Description |
|:---------------|:--------|
| [signInputsAndReturn(String, List, Bundle, List, org.iota.jota.pow.ICurl)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/utils/IotaAPIUtils.java#L79) | Finalizes and signs the bundle transactions. Bundle and inputs are assumed correct. |
| [generateNewAddresses(AddressRequest)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L86) | Generates new addresses, meaning addresses which were not spend from, according to the connected node. Stops when [getAmount()](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/builder/AddressRequest.java#L104) of unspent addresses are found, starting from [getIndex()](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/builder/AddressRequest.java#L96) If [getAmount()](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/builder/AddressRequest.java#L104) is set to 0, we will generate until the first unspent address is found, and stop. |

 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    List<String> response = iotaAPI.addRemainder("YOUR9SECRET9SEED9999999...", 3, new List<Input>(new Input[]{inputs, inputs}), bundle, "TAG9ABDOZDTYHY9ALNTRW9WCINN", 515, "M9LNVCAWCXEYRAZQKONSQSWTCVZCMKEDDBUBEBRKLCETDNNNSYF9L9QEVSATNONEBRBQBN9EOUPOGGUTP", new List<String>(new String[]{"KULNMSVMUAJJISRYBTEMGETHR ... LXLRMKDLRTFHPKYINRXIUUVMU", "EKOFHDI9IRIAC9OHQWVSTXMRG ... SCFMG9SFYDDFMISXTRDBIODLX"}));
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
