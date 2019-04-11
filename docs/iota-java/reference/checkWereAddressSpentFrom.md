
# [checkWereAddressSpentFrom](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L823)
 boolean[] checkWereAddressSpentFrom(String[] addresses)

Check if a list of addresses was ever spent from, in the current epoch, or in previous epochs.  If the address has a checksum, it is removed
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| addresses | String[] | Required | the addresses to check |
    
## Output
| Return type | Description |
|--|--|
| boolean[]  | list of address boolean checks |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) |  |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    boolean response = iotaAPI.checkWereAddressSpentFrom(["QBMKILBOYESLUCXMNIQIBBOICSRWMO9PMY99YHPACWLFPXLCVAELQZNRFLJPGDHWZHGIQYDG9JLREW99WOEWUXYVLVRXPXJJWSY9LCGSRYKLPZRSEHKKYNBSD9ESNDN9YUPBOGBXOBPOKASLNUEHANHCNWD99GLLMF", "RUKJQZIQHBSPRVRFOQMPIZOTP9FGQBVREIVLYL9HBSMTJCKBNLARQNLVWMEFAJQSEHVZPI9XRPJAKDAHOVRBMRUKREZWXVJEZGXIPREGDPWNFZDWIEPNDLNAN9CGEKEGUPIQNVC9KZGLUISHLZMOQGIHVJOZRRULGG"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
