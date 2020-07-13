
# [getBalances](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L532)
 [GetBalancesResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetBalancesResponse.java) getBalances(List<String> addresses , List<String> tips)

 Calculates the confirmed balance, as viewed by the specified `tips`. If you do not specify the referencing `tips`, the returned balance is based on the latest confirmed milestone. In addition to the balances, it also returns the referencing `tips` (or milestone), as well as the index with which the confirmed balance was determined. The balances are returned as a list in the same order as the addresses were provided as input. 

> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| addresses | List<String> | Required | The addresses where we will find the balance for. Must contain the checksum. |
| tips | List<String> | Required | The tips to find the balance through. Can be `null` |
    
## Output
[GetBalancesResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetBalancesResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| String[] references | Gets the references this balance was requested through |
| int milestoneIndex | Gets the milestone index. |
| String[] balances | Gets the balances. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | The request was considered wrong in any way by the node |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetBalancesResponse response = iotaAPI.getBalances(new List<String>(new String[]{"DPSLWRLVDSQBUKYYXVAQSHKNZHVFNZMG9UHXAPSFDKFWMIVJL9IQFKIGNRLABCVGTTDOCNYBKQHPT9HOI", "ZEDT9UOTUSQVZULPDLEJJKKDPKSUDNHIPMVSUNCEA9SRVOEJEGWVSXONYFJGRFCTR9PSVPNVDCMTOIA9I"}), new List<String>(new String[]{"B9UDXTHLUYNTGSBLUQPTFBWMJERNOV9QTCVRVOGVHAECFIEAGRARNRSPZUAUTAJRTVVWGVPZXVHLEOOXQ", "JAWMKNUVVVHYEHWJCNXSCRVGKMCHCYOLLJ9XLNZVCFUVATOTSNGFPQVLQICLXXVMKQOQDZMHQFSGS9MAP"}));
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
