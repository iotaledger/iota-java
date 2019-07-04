
# [start](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/Account.java#L39)
 boolean start()

Starts the account event loop
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

    
## Output
| Return type | Description |
|--|--|
| boolean  | missing description |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [AccountError](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/errors/AccountError.java) | When an error occurred during start-up |


 ## Example
 
 ```Java
 Account account = new IotaAccount.Builder().build();

try { 
    boolean started = iotaAPI.start();
    if (started){
        // All went okay, and the accounts can be used
    }
} catch (AccountError e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
