package jota.dto.request;

import jota.IotaAPICommands;

public class IotaFindTransactionsRequest extends IotaCommandRequest {

	private IotaFindTransactionsRequest() {
		super(IotaAPICommands.FIND_TRANSACTIONS);
	}

	private String[] bundles; // List of bundle hashes. The hashes need to be extended to 81chars by padding the hash with 9's.
	private String[] addresses;
    private String[] tags;
    private String[] approvees;

    public static IotaFindTransactionsRequest createFindTransactionRequest() {
        return new IotaFindTransactionsRequest();
    }

    public IotaFindTransactionsRequest byBundles(String ... bundles) {
        this.bundles = bundles;
        return this;
    }

    public IotaFindTransactionsRequest byAddresses(String ... addresses) {
        this.addresses = addresses;
        return this;
    }

    public IotaFindTransactionsRequest byTags(String ... tags) {
        this.tags = tags;
        return this;
    }

    public IotaFindTransactionsRequest byApprovees(String ... approvees) {
        this.approvees = approvees;
        return this;
    }
}
