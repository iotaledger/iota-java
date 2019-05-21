
# [generateNewAddresses](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L188)
 [GetNewAddressResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetNewAddressResponse.java) generateNewAddresses(String seed , int security , boolean checksum , int index , long amount , boolean addSpendAddresses)

Generates new addresses, meaning addresses which were not spend from, according to the connected node. Stops when `amount` of unspent addresses are found,starting from `index`
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| seed | String | Required | Tryte-encoded seed. It should be noted that this seed is not transferred. |
| security | int | Required | Security level to be used for the private key / address. Can be 1, 2 or 3. |
| checksum | boolean | Required | Adds 9-tryte address checksum. Checksums are required for all API calls. |
| index | int | Required | Key index to start search from. |
| amount | long | Required | Total number of addresses to generate.  If this is set to 0, we will generate until the first unspent address is found, and stop. |
| addSpendAddresses | boolean | Required | If `true`, it returns all addresses, even those who were determined to be spent from |
    
## Output
[GetNewAddressResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetNewAddressResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| List<String> addresses | Gets the addresses. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | When the seed is invalid |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | When the security level is wrong. |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | When index plus the amount are below 0 |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetNewAddressResponse response = iotaAPI.generateNewAddresses("LZXFD9PFC9GPSTMKHXOIPQ9BHCCLFAVXPZMPEHHADJEAOQCFZJMYSRADXWCGGHO9IHSLDAVG9CALDOSWC", "956", "true", "281", "amount", "UIGJSVU9PXUIWFKEPFKUW9OIRFCXLEBBHBRCRCOOYZCANZL9Q99UJPOHPEYTAU9JMDRQUPMKRSGEARRMC");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
