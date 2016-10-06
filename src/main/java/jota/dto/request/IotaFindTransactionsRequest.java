package jota.dto.request;

import jota.IotaAPICommands;

public class IotaFindTransactionsRequest extends IotaCommandRequest {

	private IotaFindTransactionsRequest() {
		super(IotaAPICommands.FIND_TRANSACTIONS);
	}

	private String[] bundles; // List of bundle hashes. The hashes need to be extended to 81chars by padding the hash with 9's.
	private String[] addresses;
	private String[] digests;
	private String[] approvees;

    public static IotaFindTransactionsRequest createFindTransactionRequest() {
        return new IotaFindTransactionsRequest();
    }

    public IotaFindTransactionsRequest byBundles(String [] bundles) {
        this.bundles = bundles;
        return this;
    }

    public IotaFindTransactionsRequest byAddresses(String [] addresses) {
        this.addresses = addresses;
        return this;
    }

    public IotaFindTransactionsRequest byDigests(String [] digests) {
        this.digests = digests;
        return this;
    }

    public IotaFindTransactionsRequest byApprovees(String [] approvees) {
        this.approvees = approvees;
        return this;
    }



}
