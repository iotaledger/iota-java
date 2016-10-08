##Introduction

The JOTA library is a simple Java8 wrapper around [[IOTA]](http://www.iotatoken.com/) Node's JSON-REST HTTP interface.

It allows to connect easily using java directly to a local or a remote [[IOTA node]](https://iota.readme.io/docs/syncing-to-the-network).

* **Latest release:** 0.0.1 Snapshot
* **Compatibility:** IOTA IRI v1.0.6
* **API coverage:** 12 of 20 commands fully implemented
* **License:** Apache License 2.0 
* **Readme updated:** 2016-10-07 14:23:43 (UTC)

A list of all *IOTA* JSON-REST API commands currently supported by jota wrapper can be found in the `Commands` enum (see [here](https://github.com/davassi/JOTA/blob/master/src/main/java/jota/IotaAPICommands.java) for more details).

JOTA java wrapper is being designed to be thread-safe and simplest as possible in order to be easily mantainable, accordingly with the ongoing natural evolution of IOTA'api.
All the boilerplate code for connecting to the node rest interface has been eliminated using Retrofit.

##Technologies & dependencies

The JOTA library has been designed to be used exclusively with Java 8+.

Core dependencies:
* Retrofit Client 2.1.0 [[link]](https://square.github.io/retrofit/)
* Gson JSON Processor : [[link]](https://github.com/google/gson)
* Lombok 1.16.2 [[link]](https://github.com/rzwitserloot/lombok)

Other dependencies:
* Simple Logging Facade for Java 1.7.21 [[link]](http://www.slf4j.org/)
* Apache Commons Lang 3.3.2 [[link]](http://commons.apache.org/proper/commons-lang/)


##Getting started <a name="getting-started"></a>

Connect to your local node with the default settings is quite straitforward: it requires only 2 lines of code. For example, in order to fetch the Node Info:

	IotaApiProxy api = new IotaApiProxy.Builder.build();
	GetNodeInfoResponse response = api.getNodeInfo();

of if you need to connect to a remote node on https:

	IotaApiProxy api = new IotaApiProxy.Builder 
		.protocol("https")
		.nodeAddress("somewhere_over_the_rainbow")
		.port(54321) 
		.build();
	
	GetNodeInfoResponse response = api.getNodeInfo();

Jota is still *not* in the central maven repository. It will be available when it will cover 100% iota's rest interface.

In order to communicate with *IOTA node*, JOTA needs to be aware of your node's exact configuration. The easiest way of providing this information is via a `node_config.properties` file, for example:


    iota.node.protocol=http
    iota.node.host=127.0.0.1
    iota.node.port=14265

That's it!
	
##Examples

There's an extensive list of test coverages on the src/test/java package of the project that can be used as reference.

##Supporting the project

If JOTA has been useful to you and you feel like contributing, consider posting a bug report or a pull request. Alternatively, donations are very welcome too!

* Bitcoin: `3FGCHqhG1SUpgn2eS1Agq2KnxJemWnQFbB`


