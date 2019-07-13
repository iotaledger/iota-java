
# [sendTransfer](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L1183)
 [SendTransferResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/SendTransferResponse.java) sendTransfer(String seed , int security , int depth , int minWeightMagnitude , List<[Transfer](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transfer.java)> transfers , List<[Input](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Input.java)> inputs , String remainderAddress , boolean validateInputs , boolean validateInputAddresses , List<[Transaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java)> tips)

Wrapper function: Runs prepareTransfers, as well as attachToTangle. We then broadcasts this and and store the transactions on the node.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| seed | String | Required | The tryte-encoded seed. It should be noted that this seed is not transferred. |
| security | int | Required | Security level to be used for the private key / address. Can be 1, 2 or 3. |
| depth | int | Required | The depth for getting transactions to approve |
| minWeightMagnitude | int | Required | The minimum weight magnitude for doing proof of work |
| transfers | List<[Transfer](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transfer.java)> | Required | List of [Transfer](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transfer.java) objects. |
| inputs | List<[Input](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Input.java)> | Required | List of [Input](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Input.java) used for funding the transfer. |
| remainderAddress | String | Required | If defined, this remainderAddress will be used for sending the remainder value (of the inputs) to.   When this is not defined, but a remaining exists, the next free address is used. |
| validateInputs | boolean | Required | Whether or not to validate the balances of the provided inputs. |
| validateInputAddresses | boolean | Required | Whether or not to validate if the destination address is already use. If a key reuse is detect or it's send to inputs. |
| tips | List<[Transaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java)> | Required | The starting points we walk back from to find the balance of the addresses   If multiple tips are supplied, only the first tip is used for [getTransactionsToApprove(Integer, String)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L403) |
    
## Output
[SendTransferResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/SendTransferResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| List<[Transaction](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Transaction.java)> transactions | Gets the transactions. |
| Boolean[] successfully | Gets the successfully. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | If the seed is invalid |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | If the security level is wrong. |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | When <tt>validateInputAddresses</tt> is <tt>true</tt>, if validateTransfersAddresses has an error. |
| IllegalStateException | If the transfers are not all valid |
| IllegalStateException | If there is not enough balance in the inputs to supply to the transfers |

## Related APIs (link to other product documentation)
| API     | Description |
|:---------------|:--------|
| [prepareTransfers(String, int, List, String, List, List, boolean)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L565) | Prepares transfer by generating bundle, finding and signing inputs. |
| [sendTrytes(String[], int, int, String)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L425) | Wrapper method: Gets transactions to approve, attaches to Tangle, broadcasts and stores. |
| [validateTransfersAddresses(String, int, List)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L1529) |  Validates the supplied transactions with seed and security. This will check for correct input/output and key reuse 
  In order to do this we will generate all addresses for this seed which are currently in use. Address checksums will be regenerated and these addresses will be looked up, making this an expensive method call. 
 If no error is thrown, the transaction trytes are using correct addresses.  This will not validate transaction fields. |

 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    SendTransferResponse response = iotaAPI.sendTransfer("MBYGDSPVZUPKNNUDZRLPB9OIEYJMY9R9MQLGQVFSZZHOKQDTYLKGFEBLFRKZ9FVTE9IM9MUDMIMLIRONP", "58", "15", "18", ["transfers", "transfers"], ["inputs", "inputs"], "DNTEIZUEJRZL9CEHLXCZQMNYHBJDZLISIEDWKFDIKGHZXGTTJTPHYECEGP9WFOFPDBVVFQGARQSNCMQJD", "true", "ESEGXONKTNVHXL9DNFW9ALOGPZFOLQXWNRZFVGSFW9UOYHTZLZRLXSGKH9EHOJXJQSDCCVJAXASKAYPOS", ["tips", "tips"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
