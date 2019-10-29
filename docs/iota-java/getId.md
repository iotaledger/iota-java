
# [getId](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/Account.java#L23)
 String getId()

Returns the account’s unique identifier which is a sha256 hash of the account’s seed.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

    
## Output
| Return type | Description |
|--|--|
| String  | the account ID |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [AccountError](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/errors/AccountError.java) | When we failed to generate the ID |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();
IotaAccount account = new IotaAccount.Builder("MY9SEED9..").api(iotaAPI).build()
try { 
    String response = account.getId();
} catch (AccountError e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
