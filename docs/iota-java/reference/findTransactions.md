
# [findTransactions](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/IotaAPICore.java#L204)
 [FindTransactionResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/FindTransactionResponse.java) findTransactions(String[] addresses, String[] tags, String[] approvees, String[] bundles)

Find the transactions which match the specified input
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

## Input
| Parameter       | Type | Required or Optional | Description |
|:---------------|:--------|:--------| :--------|
| addresses | String[] | Required | missing description |
| tags | String[] | Required | missing description |
| approvees | String[] | Required | missing description |
| bundles | String[] | Required | missing description |
    
## Output
| Return type | Description |
|--|--|
| [FindTransactionResponse](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/dto/response/FindTransactionResponse.java)  | The transaction hashes which are returned depend on the input. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/dev/jota/src/main/java/jota/error/ArgumentException.java) |  |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    FindTransactionResponse response = iotaAPI.findTransactions(["SVJZZUDACFVCEQPRQEDBTUJFJFNINPPSF9GMZAPNCJ9E9BINNNQRFVH9FGCQHYXCIWEZMSTCSUCF9HIDLZXBGOZBHOMBGCPZCLHDKWKPGCORTRGOBQFJBLO9RHQKSWPEQACHTPIPQIAMVLAULX9ROVZIEYHLPUADSN", "YQG9IFRVSTTDTZQNREDIJO9TANXGIVAJ9XVPDRYMRPOYGDNXFZOVJGAF9ZBGJGLVA9QJHQGQI9AAOONMYGSTDKWQQOYPMILCKCEA9QSDUHUZILNCAQCZPMQPHC9HHAAOYKPKARKLLON9YRAG9KMXDEHMRSIOQJITSM"], ["RALWTJIIBGMGUFQUUUBV9KCIGDRPWXVFGYQYHVGKBZAJAO9TYDMTRZOLKWXCCMS9GHIOUVSBMRFUPYARE", "UGVLSAQUPTGODBNLDNXKYTNANWBAATBEMJWGNARUTRJ9PVOIODAOQXBXDFTGUIKXVKSTFVAVKG9SCDZYO"], ["QTOETRISICLRIEBTWWRJRPIBYBTMXWFBITRDUXDNNEAYFF9CWFMBVQBWA9GMXRKTCENZRQUCDPBXBKGHP", "XHCYZCBKATOEW9PYZD9OKAPVHXCWQOGGXKFHIFFFKMVELLFKQPCFVBHGQGQGHUBTVIYVHIIPTIPTVNELL"], ["QIUPFX9TZFORXTELFIIRDIIMOMQIUSRHPFDCPOHWALXQWSYLWXZVOXCDOGXCGZMWJUZTYRQCUBYPY9XDN", "XRMCFRCPNADMJKOK9FDZAEZMHOWQTMFXWEXR9NC9QYKKKXBFDQ9SLTAYJ9UGSMXJFRTARH9LTWZW9OPXH"]);
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
