
# [validateTransfersAddresses](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPI.java#L1245)
 void validateTransfersAddresses(String seed, int security, List<String> trytes)


> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| seed | String | Required | Tryte-encoded seed |
| security | int | Required | The security level of private key / seed. |
| trytes | List<String> | Required | The trytes. |
    
## Output
| Return type | Description |
|--|--|

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) | is thrown when the specified input is not valid. |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    void response = iotaAPI.validateTransfersAddresses("OAHWIVEZSYLA9KSL9CEQUW9TUPJLRVVUAGFCLQFR9BEHZGLSRJONKPXVB9O9IVHTUQA9EHPFQYDTJUZGI", "706", ["GIOZUNMUSTHLXRVCZUQNPUVFBVCTYBMLKKGKBCTVFY9KBSEOGVRPEIPERVPDMRNRDXWWKDHXZDRCZEIQBMRAXVAWKOYPJJJLHROBCFUPMZFGWTLNCQYGE9NOSFOVEDELGDMNAHHCPEF9VXETVLBXBVSCAOCPXPRWNU", "RLEUEUSFWDMOHPXXDRHRVHWPZDRZBQNFKQVXFOGHHEHWPXWVJKZMCUUZ9YAHVPPRAPYSNEUXFYBWJCNOGQOKQKAKZIQHYMUIOKFLUDFUVDRDSVZZFPCFOWYDROXCKKOPQIGURPYAECBBCBAETKSWEXHSWKDMNJLR9U"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
