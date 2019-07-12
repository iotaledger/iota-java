
# [availableBalance](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/Account.java#L82)
 long availableBalance()

Runs the input selection with the CDAs in order to determine the usable balance for funding transfers.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

    
## Output
| Return type | Description |
|--|--|
| long  | The amount of balance for this account which you can immediately use |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [AccountError](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/errors/AccountError.java) | When we failed to get the balance |


 ## Example
 
 ```Java
 Account account = new IotaAccount.Builder().build();

try { 
    long balance = account.availableBalance();
    
    // Sweep your account balance to 1 address
    Future<ConditionalDepositAddress> response = account.newDepositAddress(nextHour, false, balance);
    ConditionalDepositAddress cda = response.get();

    account.send(new Recipient(balance, "My sweep message", "IOTA9SWEEP9", cda.getDepositAddress())
} catch (AccountError e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
