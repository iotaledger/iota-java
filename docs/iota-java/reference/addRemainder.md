
# [addRemainder](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L1299)
 List<String> addRemainder(String seed, int security, List<[Input](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Input.java)> inputs, [Bundle](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Bundle.java) bundle, String tag, long totalValue, String remainderAddress, List<String> signatureFragments)


> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| seed | String | Required | Tryte-encoded seed. |
| security | int | Required | The security level of private key / seed. |
| inputs | List<[Input](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Input.java)> | Required | List of inputs used for funding the transfer. |
| bundle | [Bundle](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Bundle.java) | Required | To be populated. |
| tag | String | Required | The tag. |
| totalValue | long | Required | The total value. |
| remainderAddress | String | Required | If defined, this address will be used for sending the remainder value (of the inputs) to. |
| signatureFragments | List<String> | Required | The signature fragments. |
    
## Output
| Return type | Description |
|--|--|

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) | is thrown when an invalid argument is provided. |
| IllegalStateException | is thrown when a transfer fails because their is not enough balance to perform the transfer. |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    List response = iotaAPI.addRemainder("AYJZVRRTSOYEYTZFIYUHRRIZYLRPFANPSUVJLWWZEAPHGTYAWZUSGDQVMCGBNBRUADYKFAH9SQHZTWAVX", "971", ["inputs", "inputs"], "bundle", "9VTDKZZXPCVOXYRQXTBGHUIAOBX9BLCKKWYIUGMRRIHZWGOZHRFCFEPUVXJQAZPIKPILVHOLBAAKMNVFX", "totalvalue", "OXCBNAGRJGGNBQRVNOXWXBUSJVJFNLLAV9NGBIGVBKKIAFTNPLLWHOEIYHWTJRXNYAVWADOKCA9FCYVVDTGSJXZ9LNITEYOJKVYDICMYU9CXTMJNAZNREKV9TMTPRUSOIZV9NFVOYBRVLYYPUFXZASTMXRWXVTAFMW", ["TLRSOFSKXIDJCUESREXRVL9ZFUROUDFCTIUYXCQCUKLNRFQFHSFDXPEGNGPYFGYRNWHQAEHTGRMHVZKNI", "PLQUYNPORYJDZLOTEGWVOEXNEKRVCXBBLHNYUDLNUGPIXGUSKCKINXIWXOY9BS9QJOTOMRBIKPLQBPARK"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
