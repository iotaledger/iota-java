
# [prepareTransfers](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L565)
 List<String> prepareTransfers(String seed , int security , List<[Transfer](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transfer.java)> transfers , String remainder , List<[Input](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Input.java)> inputs , List<[Transaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java)> tips , boolean validateInputs)

Prepares transfer by generating bundle, finding and signing inputs.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| seed | String | Required | The tryte-encoded seed. It should be noted that this seed is not transferred.   Only required when we are preparing a value transfer |
| security | int | Required | Security level to be used for the private key / address. Can be 1, 2 or 3. |
| transfers | List<[Transfer](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transfer.java)> | Required | List of transfer objects.   If the total value of the transfers is 0, no signing is performed. |
| remainder | String | Required | If defined, this address will be used for sending the remainder value (of the inputs) to.   Otherwise, then next available address is used (if the transfer value is over 0) |
| inputs | List<[Input](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Input.java)> | Required | The inputs used for this transfer |
| tips | List<[Transaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java)> | Required | The starting points we walk back from to find the balance of the addresses, can be `null` |
| validateInputs | boolean | Required | Whether or not to validate the balances of the provided inputs   If no validation is required |
    
## Output
| Return type | Description |
|--|--|
| List<String>  | Returns a list of the trytes of each bundle. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | If the seed is invalid |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | If the security level is wrong. |
| IllegalStateException | If the transfers are not all valid |
| IllegalStateException | If there is not enough balance in the inputs |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    List<String> response = iotaAPI.prepareTransfers("UUL9TJ9RJIIM9HUGBSASBNKZPXFUQA9HYUQSLFJQL9VSNAZWNUNLSXMH9UAGDEKYMGJPD9SYBJJD9JQMX", "47", ["transfers", "transfers"], "WMYIMQJLS9XXUEUTZUCVNFTBQSXAIDWEEUDFI9PULEBBJRW9GICUXQBKNVQSLUIZ9YOKDFCKBIJDH9EVP", ["inputs", "inputs"], ["tips", "tips"], "true");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
