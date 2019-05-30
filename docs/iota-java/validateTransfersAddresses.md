
# [validateTransfersAddresses](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L1529)
 void validateTransfersAddresses(String seed , int security , List<String> trytes)

 Validates the supplied transactions with seed and security. This will check for correct input/output and key reuse 
  In order to do this we will generate all addresses for this seed which are currently in use. Address checksums will be regenerated and these addresses will be looked up, making this an expensive method call. 
 If no error is thrown, the transaction trytes are using correct addresses.  This will not validate transaction fields.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| seed | String | Required | The tryte-encoded seed. It should be noted that this seed is not transferred. |
| security | int | Required | Security level to be used for the private key / address. Can be 1, 2 or 3. |
| trytes | List<String> | Required | List of transaction trytes. |
    
## Output
| Return type | Description |
|--|--|

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when we are sending to our own input addresses |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when we try to remove funds from an address that is not an input |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when we are sending from an address we have already used for sending |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    void response = iotaAPI.validateTransfersAddresses("VJDQCQPMOUNKJMRFMTBKISRXRSJQBUOEIJYZSIUQWGE9JLGULSRSAJLSWKYHFGORYODQYZRFIUFQSHPNT", "83", ["LMZAJWWDVXZYSPPFKTJTNCDTZ ... QVSUHJLYVPTMI9UFRXUBB9YGW", "SGQNSV9IFPSXHDOHJTXQBDVDE ... KZKWT9DIVHJYCOYFHJEPDSQCT"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
