
# [getBalanceAndFormat](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L710)
 [GetBalancesAndFormatResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetBalancesAndFormatResponse.java) getBalanceAndFormat(List<String> addresses, List<String> tips, long threshold, int start, [StopWatch](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/utils/StopWatch.java) stopWatch, int security)

Gets the balances and formats the output.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| addresses | List<String> | Required | The addresses. |
| tips | List<String> | Required | The starting points we walk back from to find the balance of the addresses |
| threshold | long | Required | Min balance required. |
| start | int | Required | Starting key index. |
| stopWatch | [StopWatch](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/utils/StopWatch.java) | Required | the stopwatch. |
| security | int | Required | The security level of private key / seed. |
    
## Output
| Return type | Description |
|--|--|
| [GetBalancesAndFormatResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/GetBalancesAndFormatResponse.java)  | Inputs object. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) | is thrown when the specified security level is not valid. |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetBalancesAndFormatResponse response = iotaAPI.getBalanceAndFormat(["VYQURBXUOWEMWWXVIMZSTNTIVQANRZWHRX9MOYCWZN9QZNUTKBYGSWZORROBUZZTHHLVKC9USDGU9JN9QIYRYXVJVQIFMKGQEGXFYNYDNWZJKBUPPLLVAOSDWJOCGCABFC9FASFCVJPDWSUCCJCRDEKWQHTVSUQSSG", "TSVYIKXIIQBVVPJGBTFYIBZKIRTEUBPYPVVKXMTBBPCFNWZQEMIOIVQXHVR9TDTNJPYYTGOYPPXJABQUWYUCXZTPZANBFBZLETYNGRURLHRRLBYMAX9RRKNFJCQHV9SGDKSCKMVXVPUPZPJLUONMNKQXNNJHEPOB9X"], ["VTAZSTPAYWIRVBOORE9FZEJTTYBQKDTNWVHIZCQVEAZVUDOJOQSXZSXVVNLFGHN9MQCJSZNXXAO9IZBET", "OGURBELFXSBKNMANKBWPCDTMIMNOERUFHIXHHNVANHSZUPIEKJYCLKELKIXOG9BETKJWAPIUZTIESSYAI"], "100", "487", "stopwatch", "909");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
