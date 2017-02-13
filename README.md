[![Build Status](https://travis-ci.org/iotaledger/iota.lib.java.svg?branch=dev)](https://travis-ci.org/iotaledger/iota.lib.java)


##Introduction

The JOTA library is a simple Java wrapper around [[IOTA]](http://www.iotatoken.com/) Node's JSON-REST HTTP interface.

It allows to connect easily using java directly to a local or a remote [[IOTA node]](https://iota.readme.io/docs/syncing-to-the-network).

* **Latest release:** 0.9.0 RC1
* **Compatibility:** fully compatible with IOTA IRI v1.2.4
* **API coverage:** 14 of 14 commands fully implemented
* **License:** Apache License 2.0 
* **Readme updated:** 2016-01-19 21:05:02 (UTC)

A list of all *IOTA* JSON-REST API commands currently supported by jota wrapper can be found in the `Commands` enum (see [here](https://github.com/davassi/JOTA/blob/master/src/main/java/jota/IotaAPICommands.java) for more details).

JOTA java wrapper is being designed to be thread-safe and simplest as possible in order to be easily mantainable, accordingly with the ongoing natural evolution of IOTA'api.
All the boilerplate code for connecting to the node rest interface has been eliminated using Retrofit.

##Technologies & dependencies

The JOTA library has been designed to be used with Java6+, in order to promote compatibility with Android.

Core dependencies:
* Retrofit Client 2.1.0 [[link]](https://square.github.io/retrofit/)
* Gson JSON Processor : [[link]](https://github.com/google/gson)
* Lombok 1.16.2 [[link]](https://github.com/rzwitserloot/lombok)

Other dependencies:
* Simple Logging Facade for Java 1.7.21 [[link]](http://www.slf4j.org/)
* Apache Commons Lang 3.3.2 [[link]](http://commons.apache.org/proper/commons-lang/)

##Getting started <a name="getting-started"></a>

Connect to your local node with the default settings is quite straightforward: it requires only 2 lines of code. For example, in order to fetch the Node Info:

	IotaApi api = new IotaApi.Builder.build();
	GetNodeInfoResponse response = api.getNodeInfo();

of if you need to connect to a remote node:

	IotaApi api = new IotaApi.Builder 
		.protocol("http")
		.nodeAddress("somewhere_over_the_rainbow")
		.port(14265) 
		.build();
	
	GetNodeInfoResponse response = api.getNodeInfo();

In order to communicate with *IOTA node*, JOTA needs to be aware of your node's exact configuration. If you dont want to use the builder the easiest way of providing this information is via a `node_config.properties` file, for example:

    iota.node.protocol=http``****************``
    iota.node.host=127.0.0.1
    iota.node.port=14265

Jota is still *not* in the central maven repository. It will be available when it will cover 100% iota's rest interface.

##Warning
 -   This is pre-release software!
 -   There may be performance and stability issues.
 -   You may loose all your money :)
 -   Please report any issues using the <a href="https://github.com/iotaledger/iota.lib.java/issues">Issue Tracker</a>

That's it!
	
##Examples

There's an extensive list of test coverages on the src/test/java package of the project that can be used as reference.

##Supporting the project

If JOTA has been useful to you and you feel like contributing, consider posting a bug report or a pull request.
