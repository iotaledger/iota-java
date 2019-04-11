
# [wereAddressesSpentFrom](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPICore.java#L392)
 [WereAddressesSpentFromResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/WereAddressesSpentFromResponse.java) wereAddressesSpentFrom(String[] addresses)

Check if a list of addresses was ever spent from, in the current epoch, or in previous epochs.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| addresses | String[] | Required | List of addresses to check if they were ever spent from. |
    
## Output
| Return type | Description |
|--|--|
| [WereAddressesSpentFromResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/WereAddressesSpentFromResponse.java)  | The state of each address (true/false) |



 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    WereAddressesSpentFromResponse response = iotaAPI.wereAddressesSpentFrom(["XILMNGIWLJXJRVXYWBDVCCROURRE9VCVXYUQOJHNAUJQMFEHXGHZMATABNFXPOOGQUQKCMBEFFLOEZEVVGNWIUHVSDJP9JRJQFODSTRNIVUYJKQUNSFYQNZYQMISOUMVZ9BCDVHWUMNV9QLZE9U9TZXRO9W9QCTWYY", "PDXBJCDCIWDMGXEQFODYPQPKOXGBTFRNQMNLEXLQLMCLBDZLOOKFXRKHVJXAADHSOUWXPPIVQJKJCHA9QNVVUZFCFJZGMHSRE9BDMMY9NLGLUDSQPCYUYUESCVAJDYNIZN9EFGLVUP9HMPFPWCDQH9FOWPIISUJOBR"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
