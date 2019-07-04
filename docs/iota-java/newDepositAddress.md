
# [newDepositAddress](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/Account.java#L72)
 Future<[ConditionalDepositAddress](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/deposits/ConditionalDepositAddress.java)> newDepositAddress(Date timeOut , boolean multiUse , long expectedAmount , [ExpireCondition[]](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/condition/ExpireCondition.java) otherConditions)

Allocates a new CDA in the underlying store. Note: Specifying both multi-use and an expected amount is not allowed
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| timeOut | Date | Required | The date this address expires |
| multiUse | boolean | Required | If we expect multiple deposits |
| expectedAmount | long | Required | The final balance we expect to have in this address before we expire |
| otherConditions | [ExpireCondition[]](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/condition/ExpireCondition.java) | Required | Currently unused, used to specify custom conditions |
    
## Output
Future<[ConditionalDepositAddress](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/deposits/ConditionalDepositAddress.java)>, which contains the following fields:
| Return type | Description |
|--|--|
| [DepositRequest](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/deposits/DepositRequest.java) request | The information about this deposit |
| [Hash](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/types/Hash.java) depositAddress | The address this deposit is requesting to |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [AccountError](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/errors/AccountError.java) | When we were not able to generate a new address |
| [AccountError](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/errors/AccountError.java) | When invalid conditions were given |


 ## Example
 
 ```Java
 Account account = new IotaAccount.Builder().build();

try { 
    int expectedAMount = 10;
    Date nextHour = new Date(new Date().getTime() + 1000 * 60 * 60);
    Future<ConditionalDepositAddress> response = account.newDepositAddress(nextHour, false, expectedAmount);
    ConditionalDepositAddress cda = response.get();
} catch (AccountError e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
