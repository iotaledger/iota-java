
# [sendTrytes](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L343)
 List<[Transaction](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transaction.java)> sendTrytes(String[] trytes, int depth, int minWeightMagnitude, String reference)

Facade method: Gets transactions to approve, attaches to Tangle, broadcasts and stores.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| trytes | String[] | Required | The trytes. |
| depth | int | Required | The depth. |
| minWeightMagnitude | int | Required | The minimum weight magnitude. |
| reference | String | Required | Hash of transaction to start random-walk from, used to make sure the tips returned reference a given transaction in their past. |
    
## Output
| Return type | Description |
|--|--|
| List<[Transaction](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/model/Transaction.java)>  | Transactions objects. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) | is thrown when invalid trytes is provided. |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    List response = iotaAPI.sendTrytes(["OOJUEJNFLUQREQAHNHXCLWQVFDVQJMRFGNMTIUWLVDEGNSJTJHLAJURPYIYPJIMCNVTAAORTOHXIYBCAJMKYLUDMKGSG9LDZADRX9AOMWOQAYLGNBAECVBAZMSENBSR9UWSLFR9LJOKGGUYQH9JFDFE9VLXADGUMMZ", "MHYQDJCUGLUQYYCCTTBERMNFFJYYVU9LBM9KZDJHOIIGOQT9KTJPLRYPPSCJ9SENQFBQRAAUYIIHTZBBOVEWPCTKX9HMKNZIMOE9AYHZP9KFTHFWCOFFWWAKABMJEACGFXRNZNXSHLAMU9UKJEAZHIJBLDPWGNQJUZ"], "15", "18", "TMQVDKXZSUYNEILLBUSGPTYKXJWWP9SGCXDOZOZKKDXUDFKFM9PLHSADXBOHLFYTBUA9LZUXIRLKLYSMS");
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
