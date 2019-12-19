
# [getNodeInfo](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/IotaAPICore.java#L153)
 [GetNodeInfoResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetNodeInfoResponse.java) getNodeInfo()

Returns information about this node.
> **Important note:** This API is currently in Beta and is subject to change. Use of these APIs in production applications is not supported.

    
## Output
[GetNodeInfoResponse](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/dto/response/GetNodeInfoResponse.java), which contains the following fields:
| Return type | Description |
|--|--|
| Long duration | Gets the duration. |
| String appName | Name of the IOTA software you're currently using. (IRI stands for IOTA Reference Implementation) |
| String appVersion | The version of the IOTA software this node is running. |
| int jreAvailableProcessors | Available cores for JRE on this node. |
| long jreFreeMemory | The amount of free memory in the Java Virtual Machine. |
| long jreMaxMemory | The maximum amount of memory that the Java virtual machine will attempt to use. |
| long jreTotalMemory | The total amount of memory in the Java virtual machine. |
| String jreVersion | The JRE version this node runs on |
| String latestMilestone | The hash of the latest transaction that was signed off by the coordinator. |
| int latestMilestoneIndex | Index of the The hash of the latest transaction that was signed off by the coordinator. |
| String latestSolidSubtangleMilestone | The hash of the latest transaction which is solid and is used for sending transactions.  For a milestone to become solid, your local node must approve the subtangle of coordinator-approved transactions,   and have a consistent view of all referenced transactions. |
| int latestSolidSubtangleMilestoneIndex | Index of the The hash of the latest transaction which is solid and is used for sending transactions.  For a milestone to become solid, your local node must approve the subtangle of coordinator-approved transactions,   and have a consistent view of all referenced transactions. |
| int milestoneStartIndex | The start index of the milestones.  This index is encoded in each milestone transaction by the coordinator |
| int neighbors | Number of neighbors this node is directly connected with. |
| int packetsQueueSize | The amount of transaction packets which are currently waiting to be broadcast. |
| long time | The difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC |
| int tips | Number of tips in the network. |
| int transactionsToRequest | When a node receives a transaction from one of its neighbors,  this transaction is referencing two other transactions t1 and t2 (trunk and branch transaction).  If either t1 or t2 (or both) is not in the node's local database,  then the transaction hash of t1 (or t2 or both) is added to the queue of the "transactions to request". At some point, the node will process this queue and ask for details about transactions in the  "transaction to request" queue from one of its neighbors.  This number represents the amount of "transaction to request" |
| String[] features | Every node can have features enabled or disabled.  This list will contain all the names of the features of a node as specified in . |
| String coordinatorAddress | The address of the Coordinator being followed by this node. |

## Exceptions
| Exceptions     | Description |
|:---------------|:--------|
| [ArgumentException](https://github.com/iotaledger/iota-java/blob/master/jota/src/main/java/org/iota/jota/error/ArgumentException.java) | The request was considered wrong in any way by the node |


 ## Example
 
 ```Java
 IotaAPI iotaAPI = new IotaAPI.Builder().build();

try { 
    GetNodeInfoResponse response = iotaAPI.getNodeInfo();
} catch (ArgumentException e) { 
    // Handle error
    e.printStackTrace(); 
}
 ```
