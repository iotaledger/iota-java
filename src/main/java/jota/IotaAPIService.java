package jota;

import jota.dto.request.*;
import jota.dto.response.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * IOTA API Proxy Service definition using Retrofit2
 * 
 * @author davassi
 */
public interface IotaAPIService {

    public static final String CONTENT_TYPE_HEADER = "Content-Type: application/json";
    public static final String USER_AGENT_HEADER = "User-Agent: JOTA-API wrapper";

    /**
     * Returns information about your node.
     * 
     * curl http://localhost:14265 \ -X POST \ -H 'Content-Type:
     * application/json' \ -d '{"command": "getNodeInfo"}'
     * 
     * @return a {@code NodeInfoResponse} object, if succesfull.
     */
    @Headers({ CONTENT_TYPE_HEADER, USER_AGENT_HEADER })
    @POST("./")
    Call<GetNodeInfoResponse> getNodeInfo(@Body IotaCommandRequest request);

    /**
     * Returns a milestone from a given index.
     * 
     * curl http://localhost:14265 \ -X POST \ -H 'Content-Type:
     * application/json' \ -d '{"command": "getMilestone", "index": 8059}'
     * 
     * @return
     */
    @Headers({ CONTENT_TYPE_HEADER, USER_AGENT_HEADER })
    @POST("./")
    Call<GetMilestoneResponse> getMilestone(@Body IotaGetMilestoneRequest request);

    /**
     * Get the list of latest tips (unconfirmed transactions).
     * 
     * curl http://localhost:14265 -X POST -H 'Content-Type: application/json'
     * -d '{"command": "getNeighbors"}'
     */
    @Headers({ CONTENT_TYPE_HEADER, USER_AGENT_HEADER })
    @POST("./")
    Call<GetNeighborsResponse> getNeighbors(@Body IotaCommandRequest request);

    /**
     * Get the list of latest tips (unconfirmed transactions).
     *
     * curl http://localhost:14265 \ -X POST \ -H 'Content-Type:
     * application/json' \ -d '{"command": "getTips"}'
     */
    @Headers({ CONTENT_TYPE_HEADER, USER_AGENT_HEADER })
    @POST("./")
    Call<GetTipsResponse> getTips(@Body IotaCommandRequest request);

    /**
     * Get the list of transfers from a specified seed (account).
     *
     * curl http://localhost:14265 -X POST -H 'Content-Type: application/json'
     * -d '{"command": "getTransfers", "seed": "AAA999999999999999999999999999999999999999999999999999999999999999999999999999999", "securityLevel": 1}'
     */
    @Headers({ CONTENT_TYPE_HEADER, USER_AGENT_HEADER })
    @POST("./")
    Call<GetTransfersResponse> getTransfers(@Body IotaGetTransferRequest request);


    /**
     * Find the transactions which match the specified input and return
     * 
     * curl http://localhost:14265 \ -X POST \ -H 'Content-Type: application/json' \ 
     * -d '{"command": "findTransactions", "addresses": ["RVORZ9SIIP9RCYMREUIXXVPQIPHVCNPQ9HZWYKFWYWZRE9JQKG9REPKIASHUUECPSQO9JT9XNMVKWYGVAZETAIRPTM"]}'
     */
    @Headers({ CONTENT_TYPE_HEADER, USER_AGENT_HEADER })
    @POST("./")
    Call<FindTransactionResponse> findTransactions(@Body IotaFindTransactionsRequest request);


    /**
     * Get the inclusion states of a set of transactions. This is for determining if a transaction was accepted and confirmed by the network or not. You can search for multiple tips (and thus, milestones) to get past inclusion states of transactions.
     *
     * curl http://localhost:14265   -X POST   -H 'Content-Type: application/json'
     * -d '{"command": "getInclusionStates", "transactions"Q9HZWYKFWYWZRE9JQKG9REPKIASHUUECPSQO9JT9XNMVKWYGVAZETAIRPTM"], "tips" : []}'
     */
    @Headers({ CONTENT_TYPE_HEADER, USER_AGENT_HEADER })
    @POST("./")
    Call<GetInclusionStateResponse> getInclusionStates(@Body IotaGetInclusionStateRequest request);

    /**
     * Get the list of transactions which were bundled with the specified tail transaction. This call returns the full value of all individual transactions, not just the hashes.
     *
     *  curl http://localhost:14265 -X POST -H 'Content-Type: application/json'
     *  -d '{"command": "getBundle", "transaction": "ZJVYUGTDRPDYFGFXMKOTV9ZWSGFK9CFPXTITQLQNLPPG9YNAARMKNKYQO9GSCSBIOTGMLJUFLZWSY9999"}'
     *
     */
    @Headers({ CONTENT_TYPE_HEADER, USER_AGENT_HEADER })
    @POST("./")
    Call<GetBundleResponse> getBundle(@Body IotaGetBundleRequest request);

    /**
     * Returns the raw trytes data of a transaction.
     *
     * curl http://localhost:14265 -X POST -H 'Content-Type: application/json'
     * -d '{"command": "getTrytes", "hashes": ["OAATQS9VQLSXCLDJVJJVYUGONXAXOFMJOZNSYWRZSWECMXAQQURHQBJNLD9IOFEPGZEPEMPXCIVRX9999"]}'
     *
     */
    @Headers({ CONTENT_TYPE_HEADER, USER_AGENT_HEADER })
    @POST("./")
    Call<GetTrytesResponse> getTrytes(@Body IotaGetTrytesRequest request);

    /*
    @Headers({ CONTENT_TYPE_HEADER, USER_AGENT_HEADER })
    @POST("./")
    Call<AnalyzeTransactionResponse> analyzeTransactions(@Body IotaAnalyzeTransactionRequest request);

    @Headers({ CONTENT_TYPE_HEADER, USER_AGENT_HEADER })
    @POST("./")
    Call<GetNewAddressResponse> getNewAddress(@Body IotaGetNewAddressRequest request);

    @Headers({ CONTENT_TYPE_HEADER, USER_AGENT_HEADER })
    @POST("./")
    Call<PrepareTransfersResponse> prepareTransfers(@Body IotaPrepareTransfersRequest request);

    @Headers({ CONTENT_TYPE_HEADER, USER_AGENT_HEADER })
    @POST("./")
    Call<GetTransactionsToApproveResponse> getTransactionsToApprove(@Body IotaGetTransactionsToApproveRequest request);

    @Headers({ CONTENT_TYPE_HEADER, USER_AGENT_HEADER })
    @POST("./")
    Call<GetAttachToTangleResponse> attachToTangle(@Body IotaAttachToTangleRequest request);

    @Headers({ CONTENT_TYPE_HEADER, USER_AGENT_HEADER })
    @POST("./")
    Call<InterruptAttachingToTangleResponse> interruptAttachingToTangle(@Body IotaInterruptAttachingToTangleRequest request);

    @Headers({ CONTENT_TYPE_HEADER, USER_AGENT_HEADER })
    @POST("./")
    Call<PushTransactionsResponse> pushTransactions(@Body IotaPushTransactionsRequest request);

    @Headers({ CONTENT_TYPE_HEADER, USER_AGENT_HEADER })
    @POST("./")
    Call<StoreTransactionsResponse> storeTransactions(@Body IotaStoreTransactionsRequest request);

    @Headers({ CONTENT_TYPE_HEADER, USER_AGENT_HEADER })
    @POST("./")
    Call<TransferResponse> transfer(@Body IotaTransferRequest request);

    @Headers({ CONTENT_TYPE_HEADER, USER_AGENT_HEADER })
    @POST("./")
    Call<ReplayTransferResponse> replayTransfer(@Body IotaReplayTransferRequest request);

    @Headers({ CONTENT_TYPE_HEADER, USER_AGENT_HEADER })
    @POST("./")
    Call<PullTransactionsResponse> pullTransactions(@Body IotaPullTransactionsRequest request);

     * Get the list of transactions which were bundled with the specified tail transaction.
     * This call returns the full value of all individual transactions, not just the hashes.
     */


























}
