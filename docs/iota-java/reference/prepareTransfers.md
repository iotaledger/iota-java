
# [prepareTransfers](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L474)
 List<String> prepareTransfers(String seed, int security, List<[Transfer](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transfer.java)> transfers, String remainder, List<[Input](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Input.java)> inputs, List<[Transaction](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transaction.java)> tips, boolean validateInputs)

Prepares transfer by generating bundle, finding and signing inputs.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| seed | String | Required | Tryte-encoded private key / seed. |
| security | int | Required | The security level of private key / seed. |
| transfers | List<[Transfer](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transfer.java)> | Required | Array of transfer objects. |
| remainder | String | Required | If defined, this address will be used for sending the remainder value (of the inputs) to. |
| inputs | List<[Input](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Input.java)> | Required | The inputs. |
| tips | List<[Transaction](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transaction.java)> | Required | The starting points we walk back from to find the balance of the addresses |
| validateInputs | boolean | Required | whether or not to validate the balances of the provided inputs |
    
## Output
| Return type | Description |
|--|--|
| List<String>  | Returns bundle trytes. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) | is thrown when the specified input is not valid. |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    List response = iotaAPI.prepareTransfers("UNMSIOVJKWBHSJMII9GVPJ9BYVDOGNIGPWNQYCEGVOIQPRTYBCYZIKFYRMKPGCDYPCEYYIMMVATIGAIYD", "22", ["transfers", "transfers"], "WOBWGPKLDHLSSEQTFEFWIFYXNOCLUQWHQSGGNTIMRTFKXFUYTZE9BNOIXFVXDXNHLQSCFCRSQYBYNPVRG", ["inputs", "inputs"], ["tips", "tips"], "false");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
