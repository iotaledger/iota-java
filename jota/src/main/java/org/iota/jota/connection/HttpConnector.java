package org.iota.jota.connection;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.iota.jota.config.IotaDefaultConfig;
import org.iota.jota.dto.request.IotaAttachToTangleRequest;
import org.iota.jota.dto.request.IotaBroadcastTransactionRequest;
import org.iota.jota.dto.request.IotaCheckConsistencyRequest;
import org.iota.jota.dto.request.IotaCommandRequest;
import org.iota.jota.dto.request.IotaFindTransactionsRequest;
import org.iota.jota.dto.request.IotaGetBalancesRequest;
import org.iota.jota.dto.request.IotaGetInclusionStateRequest;
import org.iota.jota.dto.request.IotaGetTransactionsToApproveRequest;
import org.iota.jota.dto.request.IotaGetTrytesRequest;
import org.iota.jota.dto.request.IotaNeighborsRequest;
import org.iota.jota.dto.request.IotaStoreTransactionsRequest;
import org.iota.jota.dto.request.IotaWereAddressesSpentFromRequest;
import org.iota.jota.dto.response.AddNeighborsResponse;
import org.iota.jota.dto.response.BroadcastTransactionsResponse;
import org.iota.jota.dto.response.CheckConsistencyResponse;
import org.iota.jota.dto.response.FindTransactionResponse;
import org.iota.jota.dto.response.GetAttachToTangleResponse;
import org.iota.jota.dto.response.GetBalancesResponse;
import org.iota.jota.dto.response.GetInclusionStateResponse;
import org.iota.jota.dto.response.GetNeighborsResponse;
import org.iota.jota.dto.response.GetNodeInfoResponse;
import org.iota.jota.dto.response.GetTipsResponse;
import org.iota.jota.dto.response.GetTransactionsToApproveResponse;
import org.iota.jota.dto.response.GetTrytesResponse;
import org.iota.jota.dto.response.InterruptAttachingToTangleResponse;
import org.iota.jota.dto.response.RemoveNeighborsResponse;
import org.iota.jota.dto.response.StoreTransactionsResponse;
import org.iota.jota.dto.response.WereAddressesSpentFromResponse;
import org.iota.jota.error.ArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpConnector implements Connection {
    private String protocol;
    private String host;
    private int port;
    
    //Default to 20 if someone passes a null client
    private int timeout = 20;
    
    private IotaNodeHTTPService service;
    private OkHttpClient client;
    
    private static final Logger log = LoggerFactory.getLogger(HttpConnector.class);

    public HttpConnector(String protocol, String host, int port) {
        this(protocol, host, port, IotaDefaultConfig.Defaults.CONNECTION_TIMEOUT);
    }
    
    public HttpConnector(String protocol, String host, int port, int timeout) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        
        this.timeout = timeout;
    }
    
    public HttpConnector(OkHttpClient client, String protocol, String host, int port) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        
        this.client = client;
    }
    
    @Override
    public int port() {
        return port;
    }
    
    @Override
    public String url() {
        return protocol + "://" + host;
    }
    
    public boolean start() {
        final String nodeUrl = protocol + "://" + host + ":" + port;

        // Create OkHttpBuilder
        OkHttpClient client = this.client;
        if (null == client){
            client = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request newRequest;

                        newRequest = request.newBuilder()
                                .addHeader(X_IOTA_API_VERSION_HEADER_NAME, X_IOTA_API_VERSION_HEADER_VALUE)
                                .build();

                        return chain.proceed(newRequest);
                    }
                })
                .build();
        }
        
        // use client to create Retrofit service
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(nodeUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        
        service = retrofit.create(IotaNodeHTTPService.class);
        return true;
    }
    
    public void stop() {
        //does nothing
    }
    
    protected static <T> Response<T> wrapCheckedException(final Call<T> call) throws ArgumentException, IllegalStateException, IllegalAccessError {
        try {
            final Response<T> res = call.execute();

            String error = null;

            if (res.errorBody() != null) {
                error = res.errorBody().string();
            }

            if (res.code() == 400) {
                throw new ArgumentException(error);

            } else if (res.code() == 401) {
                throw new IllegalAccessError("401 " + error);
            } else if (res.code() == 500) {
                throw new IllegalAccessError("500 " + error);
            } else if (error != null || res.body() == null) {
                //Unknown error, could be node timeout before our timeout or similar errors
                throw new IllegalAccessError(res.code() + " " + res.message());
            }

            return res;
        } catch (IOException e) {
            log.error("Execution of the API call raised exception. IOTA Node not reachable?", e);
            throw new IllegalStateException(e.getMessage());
        }
    }
    
    @Override
    public String toString() {
        return protocol + "://" + host + ":" + port;
    }

    @Override
    public GetNodeInfoResponse getNodeInfo(IotaCommandRequest request) throws ArgumentException {
        final Call<GetNodeInfoResponse> res = service.getNodeInfo(IotaCommandRequest.createNodeInfoRequest());
        return wrapCheckedException(res).body();
    }

    @Override
    public GetNeighborsResponse getNeighbors(IotaCommandRequest request) throws ArgumentException {
        final Call<GetNeighborsResponse> res = service.getNeighbors(request);
        return wrapCheckedException(res).body();
    }

    @Override
    public AddNeighborsResponse addNeighbors(IotaNeighborsRequest request) throws ArgumentException {
        final Call<AddNeighborsResponse> res = service.addNeighbors(request);
        return wrapCheckedException(res).body();
    }

    @Override
    public RemoveNeighborsResponse removeNeighbors(IotaNeighborsRequest request) throws ArgumentException {
        final Call<RemoveNeighborsResponse> res = service.removeNeighbors(request);
        return wrapCheckedException(res).body();
    }

    @Override
    public GetTipsResponse getTips(IotaCommandRequest request) throws ArgumentException {
        final Call<GetTipsResponse> res = service.getTips(request);
        return wrapCheckedException(res).body();
    }

    @Override
    public FindTransactionResponse findTransactions(IotaFindTransactionsRequest request) throws ArgumentException {
        final Call<FindTransactionResponse> res = service.findTransactions(request);
        return wrapCheckedException(res).body();
    }

    @Override
    public GetInclusionStateResponse getInclusionStates(IotaGetInclusionStateRequest request) throws ArgumentException {
        final Call<GetInclusionStateResponse> res = service.getInclusionStates(request);
        return wrapCheckedException(res).body();
    }

    @Override
    public GetTrytesResponse getTrytes(IotaGetTrytesRequest request) throws ArgumentException {
        final Call<GetTrytesResponse> res = service.getTrytes(request);
        return wrapCheckedException(res).body();
    }

    @Override
    public GetTransactionsToApproveResponse getTransactionsToApprove(IotaGetTransactionsToApproveRequest request) throws ArgumentException {
        final Call<GetTransactionsToApproveResponse> res = service.getTransactionsToApprove(request);
        return wrapCheckedException(res).body();
    }

    @Override
    public GetBalancesResponse getBalances(IotaGetBalancesRequest request) throws ArgumentException {
        final Call<GetBalancesResponse> res = service.getBalances(request);
        return wrapCheckedException(res).body();
    }

    @Override
    public GetAttachToTangleResponse attachToTangle(IotaAttachToTangleRequest request) throws ArgumentException {
        final Call<GetAttachToTangleResponse> res = service.attachToTangle(request);
        return wrapCheckedException(res).body();
    }

    @Override
    public InterruptAttachingToTangleResponse interruptAttachingToTangle(IotaCommandRequest request) throws ArgumentException {
        final Call<InterruptAttachingToTangleResponse> res = service.interruptAttachingToTangle(request);
        return wrapCheckedException(res).body();
    }

    @Override
    public BroadcastTransactionsResponse broadcastTransactions(IotaBroadcastTransactionRequest request) throws ArgumentException {
        final Call<BroadcastTransactionsResponse> res = service.broadcastTransactions(request);
        return wrapCheckedException(res).body();
    }

    @Override
    public StoreTransactionsResponse storeTransactions(IotaStoreTransactionsRequest request) throws ArgumentException {
        final Call<StoreTransactionsResponse> res = service.storeTransactions(request);
        return wrapCheckedException(res).body();
    }

    @Override
    public CheckConsistencyResponse checkConsistency(IotaCheckConsistencyRequest request) throws ArgumentException {
        final Call<CheckConsistencyResponse> res = service.checkConsistency(request);
        return wrapCheckedException(res).body();
    }

    @Override
    public WereAddressesSpentFromResponse wereAddressesSpentFrom(IotaWereAddressesSpentFromRequest request) throws ArgumentException {
        final Call<WereAddressesSpentFromResponse> res = service.wereAddressesSpentFrom(request);
        return wrapCheckedException(res).body();
    }

}
