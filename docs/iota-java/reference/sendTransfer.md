
# [sendTransfer](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L934)
 [SendTransferResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/SendTransferResponse.java) sendTransfer(String seed, int security, int depth, int minWeightMagnitude, List<[Transfer](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transfer.java)> transfers, List<[Input](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Input.java)> inputs, String remainderAddress, boolean validateInputs, boolean validateInputAddresses, List<[Transaction](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transaction.java)> tips)

Wrapper function that basically does prepareTransfers, as well as attachToTangle and finally, it broadcasts and stores the transactions locally.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| seed | String | Required | Tryte-encoded seed |
| security | int | Required | The security level of private key / seed. |
| depth | int | Required | The depth. |
| minWeightMagnitude | int | Required | The minimum weight magnitude. |
| transfers | List<[Transfer](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transfer.java)> | Required | Array of transfer objects. |
| inputs | List<[Input](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Input.java)> | Required | List of inputs used for funding the transfer. |
| remainderAddress | String | Required | If defined, this remainderAddress will be used for sending the remainder value (of the inputs) to. |
| validateInputs | boolean | Required | Whether or not to validate the balances of the provided inputs. |
| validateInputAddresses | boolean | Required | Whether or not to validate if the destination address is already used, if a key reuse is detect ot it's send to inputs. |
| tips | List<[Transaction](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transaction.java)> | Required | The starting points we walk back from to find the balance of the addresses |
    
## Output
| Return type | Description |
|--|--|
| [SendTransferResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/SendTransferResponse.java)  | Array of valid Transaction objects. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) | is thrown when the specified input is not valid. |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    SendTransferResponse response = iotaAPI.sendTransfer("USPZBWOEKMMLCZRZGQHDDNVTEWXADQNHEZXIXJJRJQ9TLUK9ECEWJKHXVSSCYVWNDPMFL9QS9JOAEKHAJ", "858", "15", "18", ["transfers", "transfers"], ["inputs", "inputs"], "QHEZDWCPWKDRSNXOYITAEJPUZJDKBA9ZLQGINEOORYAC9ZUZJJRQJB9CKFQJPPQJDPZFSWWNESEFWXLTQVIKYOAUVTPFYVBTBGVHDIXEZQ9RIMULXJBEVBVYAJLDCZYHKQSNGNWCGWCNRFXMMPCFINWFRAAIPQRDUS", "true", "ZHCRXSPGVSQHZQOFZVBIZFHISDRVQOIKXIO9EZYXHQKLLWTSGAF9YHSRQZBGAGIUWUOMZURPRACPUNQDBNUYFMGBZPAVVIEBFVKORTQASC9JAEATNTMZPNFRDOEKLROSMGZTXX9F9ZDE9PPXBCIKDEYMUYQQXXASRX", ["tips", "tips"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
