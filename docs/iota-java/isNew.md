
# [isNew](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/Account.java#L100)
 boolean isNew()

Checks whether the state of the account is new.  An account is considered new if no pending transfers or CDAs are stored.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

    
## Output
| Return type | Description |
|--|--|
| boolean  | `true` if it is new, otherwise `false` |



 ## Example
 
 ```Java
 Account account = new IotaAccount.Builder().build();
 boolean isNewAccount = account.isNew();
 // Now we know!
 ```
