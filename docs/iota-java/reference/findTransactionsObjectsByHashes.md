
# [findTransactionsObjectsByHashes](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L371)
 List<[Transaction](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transaction.java)> findTransactionsObjectsByHashes(String[] hashes)

Wrapper function for getTrytes and transactionObjects.  Gets the trytes and transaction object from a list of transaction hashes.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| hashes | String[] | Required | The hashes |
    
## Output
| Return type | Description |
|--|--|
| List<[Transaction](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transaction.java)>  | Transaction objects. |



 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    List response = iotaAPI.findTransactionsObjectsByHashes(["NZKPKSSFNDJIJGOCXGHVXODLVUKULSVPWNJVVRBKRPQPLCNTFXRKDRLUDGMGIJIOVBZLUJVJXQAGTQ9WT", "HPAIWSLHLSHCBRWBSTLRBETGQLGMHMGDTFKRLDOBXI9WVMTAMKTPDZGPIBHUECAJSGSOUAEZMOFVY9IIP"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
