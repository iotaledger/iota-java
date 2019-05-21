
# [getInputs](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L771)
 [GetBalancesAndFormatResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetBalancesAndFormatResponse.java) getInputs(String seed , int security , int start , int end , long threshold , String[] tips)

Gets the inputs of a seed If start, end and threshold are 0, checks everything until an address with nothing is found. Addresses are all with checksum appended
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| seed | String | Required | Tryte-encoded seed. It should be noted that this seed is not transferred. |
| security | int | Required | Security level to be used for the private key / address. Can be 1, 2 or 3. |
| start | int | Required | Starting key index, must be at least 0. |
| end | int | Required | Ending key index, must be bigger then `start`, and cant span more than 500 indexes |
| threshold | long | Required | Minimum balance required. |
| tips | String[] | Required | The starting points we walk back from to find the balance of the addresses, can be `null` |
    
## Output
[GetBalancesAndFormatResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetBalancesAndFormatResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| List<[Input](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Input.java)> inputs | Gets the input. |
| long totalBalance | Gets the total balance. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | If the seed is invalid |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | If the security level is wrong. |
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | when <tt>start</tt> and <tt>end</tt> are more then 500 apart |

## Related APIs (link to other product documentation)
| API     | Description |
|:---------------|:--------|
| [getBalanceAndFormat(List, List, long, int, StopWatch, int)](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L892) | Gets the balances and formats the output. |

 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetBalancesAndFormatResponse response = iotaAPI.getInputs("VOINACFSQEEGNPVHSWEVFOTCZVJMST9TFLJW9FWSGPMLZJOVFSQYFSGQYYZQLISTJHQYOOBRFGHUNXU9R", "491", "817", "132", "100", ["WODEXHYNOR9KGLGJ9UVHURLABC9CWRXDPEKGHYKOBMFGXYPOHLCJJTFAKPXVD99RINOWYBLVEAOZPKHRN", "GGLMERBGBQHFKTDGPUNMJNHPJCJYMVAYQNLZOQWLBMOXEXZAUCXGPWIKUJWHYGPHOJKFPPFW9YEUTHRDK"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
