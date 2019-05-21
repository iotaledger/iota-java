
# [send](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/account/Account.java#L57)
 Future<[Bundle](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Bundle.java)> send([Recipient](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/types/Recipient.java) recipient)

Sends the specified amounts to the given recipient.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| recipient | [Recipient](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/types/Recipient.java) | Required | Information about the receiving end of the transactions |
    
## Output
| Return type | Description |
|--|--|
| Future<[Bundle](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/model/Bundle.java)>  | The bundle we sent |



 ## Example
 
 ```Java
 Account account = new IotaAccount.Builder().build();

try { 
    Recipient recipient = new Recipient(1, "Message", "Tag", "ADDRESS999");
    Future<Bundle> response = account.send(recipient);
    Bundle bundle = response.get();
} catch (AccountError e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
