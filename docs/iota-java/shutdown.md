
# [shutdown](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/Account.java#L48)
 void shutdown()

Shutdowns the account’s inner event loop and shutdowns all plugins.  Will gracefully shut down all plugins
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

    
## Output
| Return type | Description |
|--|--|

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [AccountError](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/errors/AccountError.java) |  |


 ## Example
 
 ```Java
 Account account = new IotaAccount.Builder().build();

try { 
    account.shutdown();
    // Account has been stopped
} catch (AccountError e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
