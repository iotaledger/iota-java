
# [totalBalance](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/Account.java#L91)
 long totalBalance()

Uses all stored CDAs to determine the current total balance.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

    
## Output
| Return type | Description |
|--|--|
| long  | The total balance of this account |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [AccountError](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/errors/AccountError.java) | When we failed to get the balance |


 ## Example
 
 ```Java
 Account account = new IotaAccount.Builder().build();

try { 
    long totalBalance = account.totalBalance();
    System.out.println("Look how rich I am: " + totalBalance)
} catch (AccountError e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
