
# [updateSettings](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/Account.java#L111)
 void updateSettings([AccountOptions](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/AccountOptions.java) newSettings)

Updates the settings of the account in a synchronized manner by shutting down all plugins,  stopping the event loop, applying the settings  and starting the newly defined plugins and kicking off the event loop again.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| newSettings | [AccountOptions](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/AccountOptions.java) | Required | The new settings for this account |
    
## Output
| Return type | Description |
|--|--|

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [AccountError](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/errors/AccountError.java) | When we failed to update the settings |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();
IotaAccount account = new IotaAccount.Builder("MY9SEED9..").api(iotaAPI).build()
try { 
    account.updateSettings(newsettings);
} catch (AccountError e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
