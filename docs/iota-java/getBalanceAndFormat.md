
# [getBalanceAndFormat](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPI.java#L892)
 [GetBalancesAndFormatResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetBalancesAndFormatResponse.java) getBalanceAndFormat(List<String> addresses , List<String> tips , long threshold , int start , [StopWatch](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/utils/StopWatch.java) stopWatch , int security)

Gets the balances and formats the output.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| addresses | List<String> | Required | The addresses. |
| tips | List<String> | Required | The starting points we walk back from to find the balance of the addresses, can be `null` |
| threshold | long | Required | Min balance required. |
| start | int | Required | Starting key index. |
| stopWatch | [StopWatch](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/utils/StopWatch.java) | Required | the stopwatch. If you pass `null`, a new one is created. |
| security | int | Required | Security level to be used for the private key / address. Can be 1, 2 or 3. |
    
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
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | is thrown when the specified security level is not valid. |
| IllegalStateException | when there is not enough balance on the addresses |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetBalancesAndFormatResponse response = iotaAPI.getBalanceAndFormat(["KPDMESXOHHAFXMFFXSMDSNEVDRVFLWOLCSRVGDUOXMCVZJLRVRBYFMU9HBLSIXSUXBAUVRZHDACNMZQUM", "HSXWTIUFLQBLXRZANMACHEURAPXRQFXCWCXSFOLBSOZBJFXRCBSLSZOREDUIQPRKHAOXMQHCXGPHWUBFU"], ["KJPIXGHCCYD9TQNYFEOENPHFFSNNJRMFACMHKONVTVRTYHXSERLNLKTRGRMZKYQBJHIPJUGEKWG9Q9TKG", "LCBRFWVVCNFHGPKFWHEEDFDIPAVMWOKYN9TAEUEZGIMAMQDVSSOAANDUBAARXVZJYPGSDCVQFCP9UWMTO"], "100", "725", "stopwatch", "627");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
