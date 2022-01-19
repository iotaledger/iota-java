<h1 align="center">
  <br>
  <a href="https://docs.iota.org/docs/client-libraries/0.1/getting-started/java-quickstart"><img src="iota-java.png"></a>
</h1>

<h2 align="center">The official Java client library for interacting with the legacy Tangle (deprecated since Chrysalis)</h2>

<p align="center">
    <a href="https://docs.iota.org/docs/client-libraries/0.1/getting-started/java-quickstart" style="text-decoration:none;">
    <img src="https://img.shields.io/badge/Documentation%20portal-blue.svg?style=for-the-badge"
         alt="Developer documentation portal">
      </p>
<p align="center">
  <a href="https://iotaledger.github.io/iota-java/javadoc/" style="text-decoration:none;"><img src="https://img.shields.io/badge/javadoc-reference-informational.svg" alt="Auto-generated docs"></a>
  <a href="https://discord.iota.org/" style="text-decoration:none;"><img src="https://img.shields.io/badge/Discord-9cf.svg?logo=discord" alt="Discord"></a>
    <a href="https://iota.stackexchange.com/" style="text-decoration:none;"><img src="https://img.shields.io/badge/StackExchange-9cf.svg?logo=stackexchange" alt="StackExchange"></a>
    <a href="https://github.com/iotaledger/iota-java/blob/dev/LICENSE" style="text-decoration:none;"><img src="https://img.shields.io/github/license/iotaledger/iota-java.svg" alt="Apache 2.0 license"></a>
    <a href="https://docs.iota.org/docs/node-software/0.1/iri/references/api-reference" style="text-decoration:none;"><img src="https://img.shields.io/badge/Node%20API%20coverage-18/18%20commands-green.svg" alt="Supported IRI API endpoints"></a>
    <a href="https://www.codacy.com/app/kwek20/iota-java?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=iotaledger/iota-java&amp;utm_campaign=Badge_Grade" style="text-decoration:none;"><img src="https://api.codacy.com/project/badge/Grade/92feea51a15c4e589386c269475b8761" alt="Code quality"></a>
  <a href="https://jitpack.io/#iotaledger/iota-java" style="text-decoration:none;"><img src="https://jitpack.io/v/iotaledger/iota-java.svg" alt="Latest release"></a>
    <a href="https://travis-ci.org/iotaledger/iota-java" style="text-decoration:none;"><img src="https://travis-ci.org/iotaledger/iota-java.svg?branch=dev" alt="Build status"></a>
  <a href='https://sourcespy.com/github/iotaledgeriotajava'  style="text-decoration:none;" title='SourceSpy Dashboard'><img src='https://img.shields.io/badge/SourceSpy-dashboard-165cc5'/></a>
</p>
      
<p align="center">
  <a href="#about">About</a> ◈
  <a href="#prerequisites">Prerequisites</a> ◈
  <a href="#installation">Installation</a> ◈
  <a href="#getting-started">Getting started</a> ◈
  <a href="#api-reference">API reference</a> ◈
  <a href="#examples">Examples</a> ◈
  <a href="#change-logs">Change logs</a> ◈
  <a href="#supporting-the-project">Supporting the project</a> ◈
  <a href="#joining-the-discussion">Joining the discussion</a> 
</p>

---

## Deprecation warning

This java client is no longer usable in the new Chrysalis network.
Please use the Java bindings generated for iota.rs.
They can be found [here](https://github.com/iotaledger/iota.rs/tree/dev/bindings/java)

## About

This was the **official** Java client library, which allows you to do the following:

* Create transactions
* Read transactions
* Sign transactions
* Generate addresses

This is beta software, so there may be performance and stability issues.
Please report any issues in our [issue tracker](https://github.com/iotaledger/iota-java/issues/new/choose).

## Prerequisites

To use the IOTA Java client library, your computer must have the following minimum requirement:

* Java 6 (or higher)

## Installation

The IOTA Java client library is available on [**jitpack.io**][jitpack].

To install the IOTA Java client library and its dependencies, you can use one of the following options:

* Download the library with Gradle
* Download the library with Maven
* Download the library manually

### Gradle

 1. Add the following repository to your root `build.gradle` file (**not** your module `build.gradle` file):

    ```gradle
    allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
        }
    }
    ```

2. Add the following dependency to your module `build.gradle` file:

    ```gradle
    dependencies {
        compile 'com.github.iotaledger:iota-java:1.0.0-beta8'
    }
    ```
    
### Maven

#### Through Maven central
1. Add the following repository to your root `pom.xml` file:
    ```xml
    <dependency>
        <groupId>org.iota</groupId>
        <artifactId>jota</artifactId>
        <classifier>jar-with-dependencies</classifier>
        <version>1.0.0-beta9</version>
    </dependency>
    ```

#### Through Jitpack

1. Add the following repository to your root `pom.xml` file:
    ```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
    ```

2. Add the following dependency to your module `pom.xml` file:
    ```xml
    <dependency>
        <groupId>com.github.iotaledger.iota-java</groupId>
        <artifactId>jota</artifactId>
        <classifier>jar-with-dependencies</classifier>
        <version>[VERSION_INFORMATION]</version>
    </dependency>
    ```
    
3. Change the value of the `<version>` tag to either a release number or the first 10 characters of a Git commit hash:
`<version>b703ef3c05f05</version>` or `<version>1.0.0-beta9</version>`

**Note:** Find the latest version on the [Jitpack](https://jitpack.io/#iotaledger/iota-java) page.

### Manual installation

1. Clone or download the [GitHub repository](https://github.com/iotaledger/iota-java).

    Inside the project, you'll have the following directories:
    * jota
    * jota-parent

2. Import and reference the jota directory in your project
For example in Eclipse: `right mouse on your project -> Properties -> Java Build Path -> Projects -> Add 'jota'`

3. In the jota directory, run the following command:

    `mvn clean install`

You'll have a .jar file called `jota-[VERSION]-jar-with-dependencies.jar`, depending on your version of the library.

## Getting Started

After you've [installed the library](#installation), you can connect to an IRI node to send transactions to it and interact with the Tangle.
An extended guide can be found on our [documentation portal](https://docs.iota.org/docs/client-libraries/0.1/getting-started/java-quickstart), we strongly recommend you to go here for starting off. A quick start tutorial is shown below.

To connect to a local IRI node on port 14265, do the following:

```java
IotaAPI api = new IotaAPI.Builder().build();
GetNodeInfoResponse response = api.getNodeInfo();
```

To connect to a remote IRI node on port 14265, do the following:

```java
IotaAPI api = new IotaAPI.Builder()
        .protocol("http")
        .host("URL OF THE REMOTE IRI NODE")
        .port(14265)
        .build();
GetNodeInfoResponse response = api.getNodeInfo();
```

**Note:** To separate your IRI node configuration from the implementation, you can also specify your IRI node configuration in a [**Java `.properties` file**](https://en.wikipedia.org/wiki/.properties) or as command line flags. These options are useful if you develop an open-source app which is deployed on a CI and don't want contributors to see the internal IRI node configuration.
To make the API read from this file, add the configuration to the builder like so: `.config(new FileConfig("node_config.properties"))`

**Example .properties files**

```java
iota.node.protocol=http
iota.node.host=127.0.0.1
iota.node.port=14265
```

Most API calls are **synchronous**. Therefore, we recommend that you call the API from a background thread or a worker thread to stop the API from blocking other threads such as the UI or the main thread.

## API Reference

For a full list of API commands for the IOTA Java client library, go to the [GitHub page](https://iotaledger.github.io/iota-java/javadoc/jota/IotaAPICommands.html#enum.constant.summary).

Here are some of the most commonly used API functions:

- [`getTransactionsObjects`](https://iotaledger.github.io/iota-java/javadoc/jota/IotaAPI.html#getTransactionsObjects-java.lang.String:A-)
- [`findTransactionObjects`](https://iotaledger.github.io/iota-java/javadoc/jota/IotaAPI.html#findTransactionObjects-java.lang.String:A-)
- [`getTransactionsObjects`](https://iotaledger.github.io/iota-java/javadoc/jota/IotaAPI.html#getTransactionsObjects-java.lang.String:A-)
- [`getLatestInclusion`](https://iotaledger.github.io/iota-java/javadoc/jota/IotaAPI.html#getLatestInclusion-java.lang.String:A-)
- [`broadcastAndStore`](https://iotaledger.github.io/iota-java/javadoc/jota/IotaAPI.html#broadcastAndStore-java.lang.String...-)
- [`getNewAddress`](https://iotaledger.github.io/iota-java/javadoc/jota/IotaAPI.html#getNewAddress-java.lang.String-int-int-boolean-int-boolean-)
- [`getInputs`](https://iotaledger.github.io/iota-java/javadoc/jota/IotaAPI.html#getInputs-java.lang.String-int-int-int-long-)
- [`prepareTransfers`](https://iotaledger.github.io/iota-java/javadoc/jota/IotaAPI.html#prepareTransfers-java.lang.String-int-java.util.List-java.lang.String-java.util.List-boolean-)
- [`sendTrytes`](https://iotaledger.github.io/iota-java/javadoc/jota/IotaAPI.html#sendTrytes-java.lang.String:A-int-int-)
- [`sendTransfer`](https://iotaledger.github.io/iota-java/javadoc/jota/IotaAPI.html#sendTransfer-java.lang.String-int-int-int-java.util.List-jota.model.Input:A-java.lang.String-)
- [`replayBundle`](https://iotaledger.github.io/iota-java/javadoc/jota/IotaAPI.html#replayBundle-java.lang.String-int-int-)
- [`getBundle`](https://iotaledger.github.io/iota-java/javadoc/jota/IotaAPI.html#getBundle-java.lang.String-)
- [`getTransfers`](https://iotaledger.github.io/iota-java/javadoc/jota/IotaAPI.html#getTransfers-java.lang.String-int-java.lang.Integer-java.lang.Integer-java.lang.Boolean-)
- [`initiateTransfer`](https://iotaledger.github.io/iota-java/javadoc/jota/IotaAPI.html#initiateTransfer-int-java.lang.String-java.lang.String-java.util.List-boolean-)
- [`getAccountData`](https://iotaledger.github.io/iota-java/javadoc/jota/IotaAPI.html#getAccountData-java.lang.String-int-int-boolean-int-boolean-int-int-boolean-long-)

## Examples

We have a list of test cases in the [`src/test/java` directory][tests] that you can use as a reference when developing apps with IOTA.
A good starter is the [`IotaAPITest` case](https://github.com/iotaledger/iota-java/blob/master/jota/src/test/java/org/iota/jota/IotaAPITest.java).

Here's how you could send a zero-value transaction, using the library. For the guide, see the [documentation portal](https://docs.iota.org/docs/client-libraries/0.1/how-to-guides/java/send-your-first-bundle).

```java
class SendData {
    public static void main(String[] args) throws ArgumentException {

        // Connect to a node
        IotaAPI api = new IotaAPI.Builder()
            .protocol("https")
            .host("nodes.devnet.thetangle.org")
            .port(443)
            .build();

        int depth = 3;
        int minimumWeightMagnitude = 9;

        // Even though a seed is not necessary because zero value transactions are not signed,
        // the library requires a seed to send a transaction.
        // This seed can be any random string of 81 trytes
        String myRandomSeed = SeedRandomGenerator.generateNewSeed();

        // Define any security level (like the seed, this is not used)
        int securityLevel = 2;

        // Define an address.
        // This does not need to belong to anyone or have IOTA tokens.
        // It must only contain a maximum of 81 trytes
        // or 90 trytes with a valid checksum
        String address = "ZLGVEQ9JUZZWCZXLWVNTHBDX9G9KZTJP9VEERIIFHY9SIQKYBVAHIMLHXPQVE9IXFDDXNHQINXJDRPFDXNYVAPLZAW";
        // This is a zero-value transaction
        int value = 0;
        // Define a message to send.
        // This message must include only ASCII characters.
        String message = TrytesConverter.asciiToTrytes("Hello world");
        String tag = "HELLOWORLD";

        Transfer zeroValueTransaction = new Transfer(address, value, message, tag);
        
        ArrayList<Transfer> transfers = new ArrayList<Transfer>();

        transfers.add(zeroValueTransaction);
        
        // Create a bundle from the transfers list
        // and send the transaction to the node
        try { 
            // Since we don't send any value, we can skip validation of inputs
            SendTransferResponse response = api.sendTransfer(myRandomSeed, securityLevel, depth, minimumWeightMagnitude, transfers, null, null, false, false, null);
            System.out.println(response.getTransactions());
        } catch (ArgumentException e) { 
            // Handle error
            e.printStackTrace(); 
         }
    }
}
```

## Change logs

- Changes in [**1.0.0-beta9**](https://github.com/iotaledger/iota-java/compare/1.0.0-beta8...1.0.0-beta9)
- Changes in [**1.0.0-beta8**](https://github.com/iotaledger/iota-java/compare/1.0.0-beta7...1.0.0-beta8)
- Changes in [**1.0.0-beta7**](https://github.com/iotaledger/iota-java/compare/1.0.0-beta6...1.0.0-beta7)
- Changes in [**1.0.0-beta6**](https://github.com/iotaledger/iota-java/compare/1.0.0-beta5...1.0.0-beta6)
- Changes in [**1.0.0-beta5**](https://github.com/iotaledger/iota-java/compare/1.0.0-beta4...1.0.0-beta5)
- Changes in [**1.0.0-beta4**](https://github.com/iotaledger/iota-java/compare/1.0.0-beta3...1.0.0-beta4)
- Changes in [**1.0.0-beta3**](https://github.com/iotaledger/iota-java/compare/1.0.0-beta2...1.0.0-beta3)
- Changes in [**1.0.0-beta2**](https://github.com/iotaledger/iota-java/compare/1.0.0-beta1...1.0.0-beta2)
- Changes in [**1.0.0-beta1**](https://github.com/iotaledger/iota-java/compare/0.9.10...1.0.0-beta1)
- Changes in [**v0.9.10**](https://github.com/iotaledger/iota-java/compare/v0.9.6...0.9.10)
- Changes in [**v0.9.6**](https://github.com/iotaledger/iota-java/compare/v0.9.5...v0.9.6)
- Changes in [**v0.9.5**](https://github.com/iotaledger/iota-java/compare/v0.9.4...v0.9.5)
- Changes in [**v0.9.4**](https://github.com/iotaledger/iota-java/compare/v0.9.3...v0.9.4)
- Changes in [**v0.9.3**](https://github.com/iotaledger/iota-java/compare/v0.9.2...v0.9.3)
- Changes in [**v0.9.2**](https://github.com/iotaledger/iota-java/compare/v0.9.1...v0.9.2)
- Changes in [**v0.9.1**](https://github.com/iotaledger/iota-java/commits/v0.9.1)

## Supporting the project

If the IOTA Java client library has been useful to you and you feel like contributing, consider posting a [bug report](https://github.com/iotaledger/iota-java/issues/new-issue), feature request or a [pull request](https://github.com/iotaledger/iota-java/pulls/).  

We have some [basic contribution guidelines](.github/CONTRIBUTING.md) to keep our code base stable and consistent.

## Joining the discussion

If you want to get involved in the community, need help with getting setup, have any issues related with the library or just want to discuss blockchain, distributed ledgers, and IoT with other people, feel free to join our [Discord](https://discord.iota.org/).  


[issues]: https://github.com/iotaledger/iota-java/issues
[new-issue]: https://github.com/iotaledger/iota-java/issues/new
[pull-requests]: https://github.com/iotaledger/iota-java/pulls
[new-pull-request]: https://github.com/iotaledger/iota-java/compare
[contribution-guidelines]: https://github.com/iotaledger/iota-java/blob/master/CONTRIBUTING.md
[tests]: https://github.com/iotaledger/iota-java/tree/master/jota/src/test/java/org/iota/jota
[iota-discord]: https://discord.iota.org/
[iota-forum]: https://forum.iota.org/
