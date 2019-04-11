
# [generateNewAddresses](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L142)
 [GetNewAddressResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetNewAddressResponse.java) generateNewAddresses(String seed, int security, boolean checksum, int index, int amount, boolean addSpendAddresses)

Generates new addresses, meaning addresses which were not spend from, according to the connected node.  Stops when `amount` of unspent addresses are found,starting from `index`
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| seed | String | Required | Tryte-encoded seed. It should be noted that this seed is not transferred. |
| security | int | Required | Security level to be used for the private key / address. Can be 1, 2 or 3. |
| checksum | boolean | Required | Adds 9-tryte address checksum. |
| index | int | Required | Key index to start search from. |
| amount | int | Required | Total number of addresses to generate. |
| addSpendAddresses | boolean | Required | If `true`, it returns all addresses, even those who were determined to be spent from |
    
## Output
| Return type | Description |
|--|--|
| [GetNewAddressResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetNewAddressResponse.java)  | GetNewAddressResponse containing an array of strings with the specified number of addresses. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) | is thrown when the specified input is not valid. |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetNewAddressResponse response = iotaAPI.generateNewAddresses("TMXCFZCTCPSAJYDCNFVCRRAUZUCUXFQVHFXPMLYGUDDMFOZSWXOGPZAPWOSWTCUVLLCRXEKZSENQIIHRA", "743", "true", "143", "783", "VRQEMOFMORWETQBXJWDM9IOAIKXCLQMBBVXQHMEMSWL9GFPQIUSZJRXTRHAWRWOORUMNWYZZRWARUWMT9KANVPJV9LORJNOAKGRWGOXKPNYXSW9QDQJT9RQWEWCUUIUOVRA9B9BS9YHCVPZZXIGH9GRJRJBZIDMMWQ");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
