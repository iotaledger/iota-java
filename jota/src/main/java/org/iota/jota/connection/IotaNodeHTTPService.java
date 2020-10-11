package org.iota.jota.connection;

import org.iota.jota.dto.request.IotaAttachToTangleRequest;
import org.iota.jota.dto.request.IotaBroadcastTransactionRequest;
import org.iota.jota.dto.request.IotaCommandRequest;
import org.iota.jota.dto.request.IotaCustomRequest;
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
import org.iota.jota.dto.response.FindTransactionResponse;
import org.iota.jota.dto.response.GetAttachToTangleResponse;
import org.iota.jota.dto.response.GetBalancesResponse;
import org.iota.jota.dto.response.GetInclusionStateResponse;
import org.iota.jota.dto.response.GetNeighborsResponse;
import org.iota.jota.dto.response.GetNodeAPIConfigurationResponse;
import org.iota.jota.dto.response.GetNodeInfoResponse;
import org.iota.jota.dto.response.GetTipsResponse;
import org.iota.jota.dto.response.GetTransactionsToApproveResponse;
import org.iota.jota.dto.response.GetTrytesResponse;
import org.iota.jota.dto.response.InterruptAttachingToTangleResponse;
import org.iota.jota.dto.response.IotaCustomResponse;
import org.iota.jota.dto.response.RemoveNeighborsResponse;
import org.iota.jota.dto.response.StoreTransactionsResponse;
import org.iota.jota.dto.response.WereAddressesSpentFromResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * IOTA API Proxy Service definition using Retrofit2
 */
public interface IotaNodeHTTPService {

    String CONTENT_TYPE_HEADER = "Content-Type: application/json";
    String USER_AGENT_HEADER = "User-Agent: JOTA-API wrapper";

    /**
     * Returns information about the node.
     * <p>
     * {@code curl http://localhost:14265 -X POST -H 'X-IOTA-API-Version: 1.4.1' -H 'Content-Type: application/json'}
     * {@code -d '{"command": "getNodeInfo"}'}
     */
    @Headers({CONTENT_TYPE_HEADER, USER_AGENT_HEADER})
    @POST("./")
    Call<GetNodeInfoResponse> getNodeInfo(@Body IotaCommandRequest request);


    /**
     * Returns information about the node API Configuration.
     * <p>g
     * {@code curl http://localhost:14265 -X POST -H 'X-IOTA-API-Version: 1.4.1' -H 'Content-Type: application/json'}
     * {@code -d '{"command": "GetNodeAPIConfiguration"}'}
     */
    @Headers({CONTENT_TYPE_HEADER, USER_AGENT_HEADER})
    @POST("./")
    Call<GetNodeAPIConfigurationResponse> getNodeAPIConfiguration(@Body IotaCommandRequest request);

    /**
     * Get the list of neighbors from the node.
     * <p>
     * {@code curl http://localhost:14265 -X POST -H 'X-IOTA-API-Version: 1.4.1' -H 'Content-Type: application/json'}
     * {@code -d '{"command": "getNeighbors"}'}
     */
    @Headers({CONTENT_TYPE_HEADER, USER_AGENT_HEADER})
    @POST("./")
    Call<GetNeighborsResponse> getNeighbors(@Body IotaCommandRequest request);

    /**
     * Add a list of neighbors to the node.
     * <p>
     * {@code curl http://localhost:14265 -X POST -H 'X-IOTA-API-Version: 1.4.1' -H 'Content-Type: application/json'}
     * {@code -d '{"command": "addNeighbors", "uris": ["udp://8.8.8.8:14265", "udp://8.8.8.5:14265"]}'}
     */
    @Headers({CONTENT_TYPE_HEADER, USER_AGENT_HEADER})
    @POST("./")
    Call<AddNeighborsResponse> addNeighbors(@Body IotaNeighborsRequest request);

    /**
     * Removes a list of neighbors from the node.
     * <p>
     * {@code curl http://localhost:14265 -X POST -H 'X-IOTA-API-Version: 1.4.1' -H 'Content-Type: application/json'}
     * {@code -d '{"command": "removeNeighbors", "uris": ["udp://8.8.8.8:14265", "udp://8.8.8.5:14265"]}'}
     */
    @Headers({CONTENT_TYPE_HEADER, USER_AGENT_HEADER})
    @POST("./")
    Call<RemoveNeighborsResponse> removeNeighbors(@Body IotaNeighborsRequest request);

    /**
     * Get the list of latest tips (unconfirmed transactions).
     * <p>
     * {@code curl http://localhost:14265 -X POST -H 'X-IOTA-API-Version: 1.4.1' -H 'Content-Type: application/json'}
     * {@code -d '{"command": "getTips"}'}
     */
    @Headers({CONTENT_TYPE_HEADER, USER_AGENT_HEADER})
    @POST("./")
    Call<GetTipsResponse> getTips(@Body IotaCommandRequest request);

    /**
     * Find the transactions which match the specified input and return.
     * <p>
     * {@code curl http://localhost:14265 -X POST -H 'X-IOTA-API-Version: 1.4.1' -H 'Content-Type: application/json'}
     * {@code -d '{"command": "findTransactions", "addresses": ["RVORZ9SIIP9RCYMREUIXXVPQIPHVCNPQ9HZWYKFWYWZRE9JQKG9REPKIASHUUECPSQO9JT9XNMVKWYGVAZETAIRPTM"]}'}
     */
    @Headers({CONTENT_TYPE_HEADER, USER_AGENT_HEADER})
    @POST("./")
    Call<FindTransactionResponse> findTransactions(@Body IotaFindTransactionsRequest request);


    /**
     * Get the inclusion states of a set of transactions. This is for determining if a transaction was accepted and confirmed by the network or not.
     * You can search for multiple tips (and thus, milestones) to get past inclusion states of transactions.
     * <p>
     * {@code curl http://localhost:14265 -X POST -H 'X-IOTA-API-Version: 1.4.1' -H 'Content-Type: application/json'}
     * {@code -d '{"command": "getInclusionStates", "transactions"Q9HZWYKFWYWZRE9JQKG9REPKIASHUUECPSQO9JT9XNMVKWYGVAZETAIRPTM"], "tips" : [ZIJGAJ9AADLRPWNCYNNHUHRRAC9QOUDATEDQUMTNOTABUVRPTSTFQDGZKFYUUIE9ZEBIVCCXXXLKX9999]}'}
     */
    @Headers({CONTENT_TYPE_HEADER, USER_AGENT_HEADER})
    @POST("./")
    Call<GetInclusionStateResponse> getInclusionStates(@Body IotaGetInclusionStateRequest request);

    /**
     * Returns the raw trytes data of a transaction.
     * <p>
     * {@code curl http://localhost:14265 -X POST -H 'X-IOTA-API-Version: 1.4.1' -H 'Content-Type: application/json'}
     * {@code -d '{"command": "getTrytes", "hashes": ["OAATQS9VQLSXCLDJVJJVYUGONXAXOFMJOZNSYWRZSWECMXAQQURHQBJNLD9IOFEPGZEPEMPXCIVRX9999"]}'}
     */
    @Headers({CONTENT_TYPE_HEADER, USER_AGENT_HEADER})
    @POST("./")
    Call<GetTrytesResponse> getTrytes(@Body IotaGetTrytesRequest request);

    /**
     * Tip selection which returns trunkTransaction and branchTransaction.
     * The input value is the latest coordinator milestone, as provided through the getNodeInfo API call.
     * <p>
     * {@code curl http://localhost:14265 -X POST -H 'X-IOTA-API-Version: 1.4.1' -H 'Content-Type: application/json'}
     * {@code -d '{"command": "getTransactionsToApprove", "depth": 27}'}
     */
    @Headers({CONTENT_TYPE_HEADER, USER_AGENT_HEADER})
    @POST("./")
    Call<GetTransactionsToApproveResponse> getTransactionsToApprove(@Body IotaGetTransactionsToApproveRequest request);

    /**
     * It returns the confirmed balance which a list of addresses have at the latest confirmed milestone.
     * <p>
     * {@code curl http://localhost:14265 -X POST -H 'X-IOTA-API-Version: 1.4.1' -H 'Content-Type: application/json'}
     * {@code -d '{"command": "getBalances", "addresses": ["HBBYKAKTILIPVUKFOTSLHGENPTXYBNKXZFQFR9VQFWNBMTQNRVOUKPVPRNBSZVVILMAFBKOTBLGLWLOHQ"]}
     */
    @Headers({CONTENT_TYPE_HEADER, USER_AGENT_HEADER})
    @POST("./")
    Call<GetBalancesResponse> getBalances(@Body IotaGetBalancesRequest request);

    /**
     * Attaches the specified transactions (trytes) to the Tangle by doing Proof of Work.
     * <p>
     * {@code curl http://localhost:14265 -X POST -H 'X-IOTA-API-Version: 1.4.1' -H 'Content-Type: application/json'}
     * {@code -d '{"command": "attachToTangle", "trunkTransaction": "JVMTDGDPDFYHMZPMWEKKANBQSLSDTIIHAYQUMZOKHXXXGJHJDQPOMDOMNRDKYCZRUFZROZDADTHZC9999", "branchTransaction": "P9KFSJVGSPLXAEBJSHWFZLGP9GGJTIO9YITDEHATDTGAFLPLBZ9FOFWWTKMAZXZHFGQHUOXLXUALY9999", "minWeightMagnitude": 18, "trytes": ["TRYTVALUEHERE"]}'}
     */
    @Headers({CONTENT_TYPE_HEADER, USER_AGENT_HEADER})
    @POST("./")
    Call<GetAttachToTangleResponse> attachToTangle(@Body IotaAttachToTangleRequest request);

    /**
     * Interrupts and completely aborts the attachToTangle process.
     * <p>
     * {@code curl http://localhost:14265 -X POST -H 'X-IOTA-API-Version: 1.4.1' -H 'Content-Type: application/json'}
     * {@code -d '{"command": "interruptAttachingToTangle"}'}
     */
    @Headers({CONTENT_TYPE_HEADER, USER_AGENT_HEADER})
    @POST("./")
    Call<InterruptAttachingToTangleResponse> interruptAttachingToTangle(@Body IotaCommandRequest request);

    /**
     * Broadcast a list of transactions to all neighbors. The input trytes for this call are provided by attachToTangle.
     * <p>
     * {@code curl http://localhost:14265 -X POST -H 'X-IOTA-API-Version: 1.4.1' -H 'Content-Type: application/json'}
     * {@code -d '{"command": "broadcastTransactions", "trytes": ["BYSWEAUTWXHXZ9YBZISEK9LUHWGMHXCGEVNZHRLUWQFCUSDXZHOFHWHL9MQPVJXXZLIXPXPXF9KYEREFSKCPKYIIKPZVLHUTDFQKKVVBBN9ATTLPCNPJDWDEVIYYLGPZGCWXOBDXMLJC9VO9QXTTBLAXTTBFUAROYEGQIVB9MJWJKXJMCUPTWAUGFZBTZCSJVRBGMYXTVBDDS9MYUJCPZ9YDWWQNIPUAIJXXSNLKUBSCOIJPCLEFPOXFJREXQCUVUMKSDOVQGGHRNILCO9GNCLWFM9APMNMWYASHXQAYBEXF9QRIHIBHYEJOYHRQJAOKAQ9AJJFQ9WEIWIJOTZATIBOXQLBMIJU9PCGBLVDDVFP9CFFSXTDUXMEGOOFXWRTLFGV9XXMYWEMGQEEEDBTIJ9OJOXFAPFQXCDAXOUDMLVYRMRLUDBETOLRJQAEDDLNVIRQJUBZBO9CCFDHIX9MSQCWYAXJVWHCUPTRSXJDESISQPRKZAFKFRULCGVRSBLVFOPEYLEE99JD9SEBALQINPDAZHFAB9RNBH9AZWIJOTLBZVIEJIAYGMC9AZGNFWGRSWAXTYSXVROVNKCOQQIWGPNQZKHUNODGYADPYLZZZUQRTJRTODOUKAOITNOMWNGHJBBA99QUMBHRENGBHTH9KHUAOXBVIVDVYYZMSEYSJWIOGGXZVRGN999EEGQMCOYVJQRIRROMPCQBLDYIGQO9AMORPYFSSUGACOJXGAQSPDY9YWRRPESNXXBDQ9OZOXVIOMLGTSWAMKMTDRSPGJKGBXQIVNRJRFRYEZ9VJDLHIKPSKMYC9YEGHFDS9SGVDHRIXBEMLFIINOHVPXIFAZCJKBHVMQZEVWCOSNWQRDYWVAIBLSCBGESJUIBWZECPUCAYAWMTQKRMCHONIPKJYYTEGZCJYCT9ABRWTJLRQXKMWY9GWZMHYZNWPXULNZAPVQLPMYQZCYNEPOCGOHBJUZLZDPIXVHLDMQYJUUBEDXXPXFLNRGIPWBRNQQZJSGSJTTYHIGGFAWJVXWL9THTPWOOHTNQWCNYOYZXALHAZXVMIZE9WMQUDCHDJMIBWKTYH9AC9AFOT9DPCADCV9ZWUTE9QNOMSZPTZDJLJZCJGHXUNBJFUBJWQUEZDMHXGBPTNSPZBR9TGSKVOHMOQSWPGFLSWNESFKSAZY9HHERAXALZCABFYPOVLAHMIHVDBGKUMDXC9WHHTIRYHZVWNXSVQUWCR9M9RAGMFEZZKZ9XEOQGOSLFQCHHOKLDSA9QCMDGCGMRYJZLBVIFOLBIJPROKMHOYTBTJIWUZWJMCTKCJKKTR9LCVYPVJI9AHGI9JOWMIWZAGMLDFJA9WU9QAMEFGABIBEZNNAL9OXSBFLOEHKDGHWFQSHMPLYFCNXAAZYJLMQDEYRGL9QKCEUEJ9LLVUOINVSZZQHCIKPAGMT9CAYIIMTTBCPKWTYHOJIIY9GYNPAJNUJ9BKYYXSV9JSPEXYMCFAIKTGNRSQGUNIYZCRT9FOWENSZQPD9ALUPYYAVICHVYELYFPUYDTWUSWNIYFXPX9MICCCOOZIWRNJIDALWGWRATGLJXNAYTNIZWQ9YTVDBOFZRKO9CFWRPAQQRXTPACOWCPRLYRYSJARRKSQPR9TCFXDVIXLP9XVL99ERRDSOHBFJDJQQGGGCZNDQ9NYCTQJWVZIAELCRBJJFDMCNZU9FIZRPGNURTXOCDSQGXTQHKHUECGWFUUYS9J9NYQ9U9P9UUP9YMZHWWWCIASCFLCMSKTELZWUGCDE9YOKVOVKTAYPHDF9ZCCQAYPJIJNGSHUIHHCOSSOOBUDOKE9CJZGYSSGNCQJVBEFTZFJ9SQUHOASKRRGBSHWKBCBWBTJHOGQ9WOMQFHWJVEG9NYX9KWBTCAIXNXHEBDIOFO9ALYMFGRICLCKKLG9FOBOX9PDWNQRGHBKHGKKRLWTBEQMCWQRLHAVYYZDIIPKVQTHYTWQMTOACXZOQCDTJTBAAUWXSGJF9PNQIJ9AJRUMUVCPWYVYVARKR9RKGOUHHNKNVGGPDDLGKPQNOYHNKAVVKCXWXOQPZNSLATUJT9AUWRMPPSWHSTTYDFAQDXOCYTZHOYYGAIM9CELMZ9AZPWB9MJXGHOKDNNSZVUDAGXTJJSSZCPZVPZBYNNTUQABSXQWZCHDQSLGK9UOHCFKBIBNETK999999999999999999999999999999999999999999999999999999999999999999999999999999999NOXDXXKUDWLOFJLIPQIBRBMGDYCPGDNLQOLQS99EQYKBIU9VHCJVIPFUYCQDNY9APGEVYLCENJIOBLWNB999999999XKBRHUD99C99999999NKZKEKWLDKMJCI9N9XQOLWEPAYWSH9999999999999999999999999KDDTGZLIPBNZKMLTOLOXQVNGLASESDQVPTXALEKRMIOHQLUHD9ELQDBQETS9QFGTYOYWLNTSKKMVJAUXSIROUICDOXKSYZTDPEDKOQENTJOWJONDEWROCEJIEWFWLUAACVSJFTMCHHXJBJRKAAPUDXXVXFWP9X9999IROUICDOXKSYZTDPEDKOQENTJOWJONDEWROCEJIEWFWLUAACVSJFTMCHHXJBJRKAAPUDXXVXFWP9X9999"]}'}
     */
    @Headers({CONTENT_TYPE_HEADER, USER_AGENT_HEADER})
    @POST("./")
    Call<BroadcastTransactionsResponse> broadcastTransactions(@Body IotaBroadcastTransactionRequest request);

    /**
     * Store transactions into the local storage. The trytes to be used for this call are returned by attachToTangle.
     * <p>
     * {@code curl http://localhost:14265 -X POST -H 'X-IOTA-API-Version: 1.4.1' -H 'Content-Type: application/json'}
     * {@code -d '{"command": "storeTransactions", "trytes": ["BYSWEAUTWXHXZ9YBZISEK9LUHWGMHXCGEVNZHRLUWQFCUSDXZHOFHWHL9MQPVJXXZLIXPXPXF9KYEREFSKCPKYIIKPZVLHUTDFQKKVVBBN9ATTLPCNPJDWDEVIYYLGPZGCWXOBDXMLJC9VO9QXTTBLAXTTBFUAROYEGQIVB9MJWJKXJMCUPTWAUGFZBTZCSJVRBGMYXTVBDDS9MYUJCPZ9YDWWQNIPUAIJXXSNLKUBSCOIJPCLEFPOXFJREXQCUVUMKSDOVQGGHRNILCO9GNCLWFM9APMNMWYASHXQAYBEXF9QRIHIBHYEJOYHRQJAOKAQ9AJJFQ9WEIWIJOTZATIBOXQLBMIJU9PCGBLVDDVFP9CFFSXTDUXMEGOOFXWRTLFGV9XXMYWEMGQEEEDBTIJ9OJOXFAPFQXCDAXOUDMLVYRMRLUDBETOLRJQAEDDLNVIRQJUBZBO9CCFDHIX9MSQCWYAXJVWHCUPTRSXJDESISQPRKZAFKFRULCGVRSBLVFOPEYLEE99JD9SEBALQINPDAZHFAB9RNBH9AZWIJOTLBZVIEJIAYGMC9AZGNFWGRSWAXTYSXVROVNKCOQQIWGPNQZKHUNODGYADPYLZZZUQRTJRTODOUKAOITNOMWNGHJBBA99QUMBHRENGBHTH9KHUAOXBVIVDVYYZMSEYSJWIOGGXZVRGN999EEGQMCOYVJQRIRROMPCQBLDYIGQO9AMORPYFSSUGACOJXGAQSPDY9YWRRPESNXXBDQ9OZOXVIOMLGTSWAMKMTDRSPGJKGBXQIVNRJRFRYEZ9VJDLHIKPSKMYC9YEGHFDS9SGVDHRIXBEMLFIINOHVPXIFAZCJKBHVMQZEVWCOSNWQRDYWVAIBLSCBGESJUIBWZECPUCAYAWMTQKRMCHONIPKJYYTEGZCJYCT9ABRWTJLRQXKMWY9GWZMHYZNWPXULNZAPVQLPMYQZCYNEPOCGOHBJUZLZDPIXVHLDMQYJUUBEDXXPXFLNRGIPWBRNQQZJSGSJTTYHIGGFAWJVXWL9THTPWOOHTNQWCNYOYZXALHAZXVMIZE9WMQUDCHDJMIBWKTYH9AC9AFOT9DPCADCV9ZWUTE9QNOMSZPTZDJLJZCJGHXUNBJFUBJWQUEZDMHXGBPTNSPZBR9TGSKVOHMOQSWPGFLSWNESFKSAZY9HHERAXALZCABFYPOVLAHMIHVDBGKUMDXC9WHHTIRYHZVWNXSVQUWCR9M9RAGMFEZZKZ9XEOQGOSLFQCHHOKLDSA9QCMDGCGMRYJZLBVIFOLBIJPROKMHOYTBTJIWUZWJMCTKCJKKTR9LCVYPVJI9AHGI9JOWMIWZAGMLDFJA9WU9QAMEFGABIBEZNNAL9OXSBFLOEHKDGHWFQSHMPLYFCNXAAZYJLMQDEYRGL9QKCEUEJ9LLVUOINVSZZQHCIKPAGMT9CAYIIMTTBCPKWTYHOJIIY9GYNPAJNUJ9BKYYXSV9JSPEXYMCFAIKTGNRSQGUNIYZCRT9FOWENSZQPD9ALUPYYAVICHVYELYFPUYDTWUSWNIYFXPX9MICCCOOZIWRNJIDALWGWRATGLJXNAYTNIZWQ9YTVDBOFZRKO9CFWRPAQQRXTPACOWCPRLYRYSJARRKSQPR9TCFXDVIXLP9XVL99ERRDSOHBFJDJQQGGGCZNDQ9NYCTQJWVZIAELCRBJJFDMCNZU9FIZRPGNURTXOCDSQGXTQHKHUECGWFUUYS9J9NYQ9U9P9UUP9YMZHWWWCIASCFLCMSKTELZWUGCDE9YOKVOVKTAYPHDF9ZCCQAYPJIJNGSHUIHHCOSSOOBUDOKE9CJZGYSSGNCQJVBEFTZFJ9SQUHOASKRRGBSHWKBCBWBTJHOGQ9WOMQFHWJVEG9NYX9KWBTCAIXNXHEBDIOFO9ALYMFGRICLCKKLG9FOBOX9PDWNQRGHBKHGKKRLWTBEQMCWQRLHAVYYZDIIPKVQTHYTWQMTOACXZOQCDTJTBAAUWXSGJF9PNQIJ9AJRUMUVCPWYVYVARKR9RKGOUHHNKNVGGPDDLGKPQNOYHNKAVVKCXWXOQPZNSLATUJT9AUWRMPPSWHSTTYDFAQDXOCYTZHOYYGAIM9CELMZ9AZPWB9MJXGHOKDNNSZVUDAGXTJJSSZCPZVPZBYNNTUQABSXQWZCHDQSLGK9UOHCFKBIBNETK999999999999999999999999999999999999999999999999999999999999999999999999999999999NOXDXXKUDWLOFJLIPQIBRBMGDYCPGDNLQOLQS99EQYKBIU9VHCJVIPFUYCQDNY9APGEVYLCENJIOBLWNB999999999XKBRHUD99C99999999NKZKEKWLDKMJCI9N9XQOLWEPAYWSH9999999999999999999999999KDDTGZLIPBNZKMLTOLOXQVNGLASESDQVPTXALEKRMIOHQLUHD9ELQDBQETS9QFGTYOYWLNTSKKMVJAUXSIROUICDOXKSYZTDPEDKOQENTJOWJONDEWROCEJIEWFWLUAACVSJFTMCHHXJBJRKAAPUDXXVXFWP9X9999IROUICDOXKSYZTDPEDKOQENTJOWJONDEWROCEJIEWFWLUAACVSJFTMCHHXJBJRKAAPUDXXVXFWP9X9999"]}'}
     */
    @Headers({CONTENT_TYPE_HEADER, USER_AGENT_HEADER})
    @POST("./")
    Call<StoreTransactionsResponse> storeTransactions(@Body IotaStoreTransactionsRequest request);

    /**
     * Check if a list of addresses was ever spent from, in the current epoch, or in previous epochs.
     * <p>
     * {@code curl http://localhost:14265 -X POST -H 'X-IOTA-API-Version: 1.4.1' -H 'Content-Type: application/json'}
     * {@code -d '{"command": "wereAddressesSpentFrom", "addresses": ["9LVHGEZGOGTXXEKX9QPKPOSHANFCAJOSMOGAHFGOVSDEIHQKIWGXOIGR9EHMDWRSLOCAUUYYSZNPBOJMX"]}'}
     */
    @Headers({CONTENT_TYPE_HEADER, USER_AGENT_HEADER})
    @POST("./")
    Call<WereAddressesSpentFromResponse> wereAddressesSpentFrom(@Body IotaWereAddressesSpentFromRequest request);

    @Headers({CONTENT_TYPE_HEADER, USER_AGENT_HEADER})
    @POST("./")
    Call<IotaCustomResponse> customRequest(@Body IotaCustomRequest customRequest);
}
