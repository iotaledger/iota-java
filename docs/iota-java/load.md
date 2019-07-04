
# [load](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/Account.java#L31)
 void load()

Loads the account’s inner event loop to accept commands and provided plugins using current the account settings
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

    
## Output
| Return type | Description |
|--|--|

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [AccountError](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/errors/AccountError.java) | When loading the account failed |


 ## Example
 
 ```Java
 Account account = new IotaAccount.Builder().build();

try { 
    account.load();
    // Now the account is loaded and can be started using start()
} catch (AccountError e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
