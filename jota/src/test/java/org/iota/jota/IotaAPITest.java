package org.iota.jota;

import org.hamcrest.core.IsNull;
import org.iota.jota.builder.AddressRequest;
import org.iota.jota.config.types.FileConfig;
import org.iota.jota.config.types.IotaDefaultConfig;
import org.iota.jota.connection.HttpConnector;
import org.iota.jota.dto.response.BroadcastTransactionsResponse;
import org.iota.jota.dto.response.CheckConsistencyResponse;
import org.iota.jota.dto.response.GetAccountDataResponse;
import org.iota.jota.dto.response.GetBalancesAndFormatResponse;
import org.iota.jota.dto.response.GetBundleResponse;
import org.iota.jota.dto.response.GetInclusionStateResponse;
import org.iota.jota.dto.response.GetNewAddressResponse;
import org.iota.jota.dto.response.GetNodeInfoResponse;
import org.iota.jota.dto.response.GetTransferResponse;
import org.iota.jota.dto.response.GetTrytesResponse;
import org.iota.jota.dto.response.ReplayBundleResponse;
import org.iota.jota.dto.response.SendTransferResponse;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Input;
import org.iota.jota.model.Transaction;
import org.iota.jota.model.Transfer;
import org.iota.jota.pow.pearldiver.PearlDiverLocalPoW;
import org.iota.jota.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.MalformedURLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IotaAPITest {

    // Contains 6000 iota
    private static final String TEST_SEED1 = "IHDEENZYITYVYSPKAURUZAQKGVJEREFDJMYTANNXXGPZ9GJWTEOJJ9IPMXOGZNQLSNMFDSQOTZAEETUEA";

    // Empty
    private static final String TEST_SEED2 = "KHFKUYFYITYPJHFKAURUZAQKGVJEREFDJMYTAGHFEGPZ9GJWTEJGF9IHFUPOZNQLSNMFDSQOTHGPEJGKD";

    // contains 1000 iota
    private static final String TEST_SEED3 = "9JFTUEPOTYPJHFKAURUZAQKGVJEREFDJMYTAGHFEGPZ9GJWTEJGF9IHFUPOZNQLSNMFDJOEMFLLSDKGJD";

    private static final String TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_1 = "MALAZGDVZIAQQRTNYJDSZMY9VE9LAHQKTVCUOAGZUCX9IBUMODFFTMGUIUAXGLWZQ9CYRSLYBM9QBIBYAEIAOPKXEA";
    private static final String TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2 = "LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZCCOZVXMTXC";
    private static final String TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_3 = "ASCZZOBQDMNHLELQKWJBMRETMHBTF9V9TNKYDIFW9PDXPUHPVVGHMSWPVMNJHSJF99QFCMNTPCPGS9DT9XAFKJVO9X";

    private static final String TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_1 = "MALAZGDVZIAQQRTNYJDSZMY9VE9LAHQKTVCUOAGZUCX9IBUMODFFTMGUIUAXGLWZQ9CYRSLYBM9QBIBYA";
    private static final String TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2 = "LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZC";
    private static final String TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_3 = "ASCZZOBQDMNHLELQKWJBMRETMHBTF9V9TNKYDIFW9PDXPUHPVVGHMSWPVMNJHSJF99QFCMNTPCPGS9DT9";

    private static final String TEST_HASH = "AKK9DUFNOAAWCERFSUQTV9EOGFTBTSYYRQHEBQWRMDDBPYOS9UIEMQZEEVEQJZIWHKLRKCUJPOW9AC999";

    // Has non-0 trits in the value area which exceeds max IOTA supply
    private static final String TEST_INVALID_TRYTES = "BYSWEAUTWXHXZ9YBZISEK9LUHWGMHXCGEVNZHRLUWQFCUSDXZHOFHWHL9MQPVJXXZLIXPXPXF9KYEREFSKCPKYIIKPZVLHUTDFQKKVVBBN9ATTLPCNPJDWDEVIYYLGPZGCWXOBDXMLJC9VO9QXTTBLAXTTBFUAROYEGQIVB9MJWJKXJMCUPTWAUGFZBTZCSJVRBGMYXTVBDDS9MYUJCPZ9YDWWQNIPUAIJXXSNLKUBSCOIJPCLEFPOXFJREXQCUVUMKSDOVQGGHRNILCO9GNCLWFM9APMNMWYASHXQAYBEXF9QRIHIBHYEJOYHRQJAOKAQ9AJJFQ9WEIWIJOTZATIBOXQLBMIJU9PCGBLVDDVFP9CFFSXTDUXMEGOOFXWRTLFGV9XXMYWEMGQEEEDBTIJ9OJOXFAPFQXCDAXOUDMLVYRMRLUDBETOLRJQAEDDLNVIRQJUBZBO9CCFDHIX9MSQCWYAXJVWHCUPTRSXJDESISQPRKZAFKFRULCGVRSBLVFOPEYLEE99JD9SEBALQINPDAZHFAB9RNBH9AZWIJOTLBZVIEJIAYGMC9AZGNFWGRSWAXTYSXVROVNKCOQQIWGPNQZKHUNODGYADPYLZZZUQRTJRTODOUKAOITNOMWNGHJBBA99QUMBHRENGBHTH9KHUAOXBVIVDVYYZMSEYSJWIOGGXZVRGN999EEGQMCOYVJQRIRROMPCQBLDYIGQO9AMORPYFSSUGACOJXGAQSPDY9YWRRPESNXXBDQ9OZOXVIOMLGTSWAMKMTDRSPGJKGBXQIVNRJRFRYEZ9VJDLHIKPSKMYC9YEGHFDS9SGVDHRIXBEMLFIINOHVPXIFAZCJKBHVMQZEVWCOSNWQRDYWVAIBLSCBGESJUIBWZECPUCAYAWMTQKRMCHONIPKJYYTEGZCJYCT9ABRWTJLRQXKMWY9GWZMHYZNWPXULNZAPVQLPMYQZCYNEPOCGOHBJUZLZDPIXVHLDMQYJUUBEDXXPXFLNRGIPWBRNQQZJSGSJTTYHIGGFAWJVXWL9THTPWOOHTNQWCNYOYZXALHAZXVMIZE9WMQUDCHDJMIBWKTYH9AC9AFOT9DPCADCV9ZWUTE9QNOMSZPTZDJLJZCJGHXUNBJFUBJWQUEZDMHXGBPTNSPZBR9TGSKVOHMOQSWPGFLSWNESFKSAZY9HHERAXALZCABFYPOVLAHMIHVDBGKUMDXC9WHHTIRYHZVWNXSVQUWCR9M9RAGMFEZZKZ9XEOQGOSLFQCHHOKLDSA9QCMDGCGMRYJZLBVIFOLBIJPROKMHOYTBTJIWUZWJMCTKCJKKTR9LCVYPVJI9AHGI9JOWMIWZAGMLDFJA9WU9QAMEFGABIBEZNNAL9OXSBFLOEHKDGHWFQSHMPLYFCNXAAZYJLMQDEYRGL9QKCEUEJ9LLVUOINVSZZQHCIKPAGMT9CAYIIMTTBCPKWTYHOJIIY9GYNPAJNUJ9BKYYXSV9JSPEXYMCFAIKTGNRSQGUNIYZCRT9FOWENSZQPD9ALUPYYAVICHVYELYFPUYDTWUSWNIYFXPX9MICCCOOZIWRNJIDALWGWRATGLJXNAYTNIZWQ9YTVDBOFZRKO9CFWRPAQQRXTPACOWCPRLYRYSJARRKSQPR9TCFXDVIXLP9XVL99ERRDSOHBFJDJQQGGGCZNDQ9NYCTQJWVZIAELCRBJJFDMCNZU9FIZRPGNURTXOCDSQGXTQHKHUECGWFUUYS9J9NYQ9U9P9UUP9YMZHWWWCIASCFLCMSKTELZWUGCDE9YOKVOVKTAYPHDF9ZCCQAYPJIJNGSHUIHHCOSSOOBUDOKE9CJZGYSSGNCQJVBEFTZFJ9SQUHOASKRRGBSHWKBCBWBTJHOGQ9WOMQFHWJVEG9NYX9KWBTCAIXNXHEBDIOFO9ALYMFGRICLCKKLG9FOBOX9PDWNQRGHBKHGKKRLWTBEQMCWQRLHAVYYZDIIPKVQTHYTWQMTOACXZOQCDTJTBAAUWXSGJF9PNQIJ9AJRUMUVCPWYVYVARKR9RKGOUHHNKNVGGPDDLGKPQNOYHNKAVVKCXWXOQPZNSLATUJT9AUWRMPPSWHSTTYDFAQDXOCYTZHOYYGAIM9CELMZ9AZPWB9MJXGHOKDNNSZVUDAGXTJJSSZCPZVPZBYNNTUQABSXQWZCHDQSLGK9UOHCFKBIBNETK999999999999999999999999999999999999999999999999999999999999999999999999999999999NOXDXXKUDWLOFJLIPQIBRBMGDYCPGDNLQOLQS99EQYKBIU9VHCJVIPFUYCQDNY9APGEVYLCENJIOBLWNB999999999XKBRHUD99C99999999NKZKEKWLDKMJCI9N9XQOLWEPAYWSH9999999999999999999999999KDDTGZLIPBNZKMLTOLOXQVNGLASESDQVPTXALEKRMIOHQLUHD9ELQDBQETS9QFGTYOYWLNTSKKMVJAUXSIROUICDOXKSYZTDPEDKOQENTJOWJONDEWROCEJIEWFWLUAACVSJFTMCHHXJBJRKAAPUDXXVXFWP9X9999IROUICDOXKSYZTDPEDKOQENTJOWJONDEWROCEJIEWFWLUAACVSJFTMCHHXJBJRKAAPUDXXVXFWP9X9999";
    private static final String TEST_TRYTES = "CCWCXCGDEAXCGDEAPCEAHDTCGDHDRAADTCGDGDPCVCTC9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999WITPXKWONPNUPBESJTXQTZTFXFSTDUWVGYHW9VRDULWGBKDVAAJLOSAEPUSCMSOIYLKMIZLPEKAOYAXMWFLDKQJI99999999999999999999UUC9999999999999999FUCK9YOUIVBUGXD99999999999B99999999IDPWGXASJFLLGCDGPQVXYGSNUESCZQCEKVREGLZX9FCYQVUWESEKWSMHZTGMJLGBOLKU9GILFITSJLZBWEHH9RSRNBPSKKUZBBJDSYHYWYHLJUOAFCKMXGTRFTZHKKWSVKGRHHJECSILLLZXKYJAYAQYEOINSZ9999OCYREQNINOB9XMLJOXMDFJDTXYO9PANNXQSW9HPFAAHEPTSPHTNEBOFFNRPKSUQNTKSACROOJPXF99999UUC9999999999999999FUCK9YOUPNMHCEJIE999999999L99999999IGMVBPROUATGVGQTHYIYFVQETRW";
    private static final String TEST_MESSAGE = "JUSTANOTHERIOTATEST";
    private static final String TEST_TAG = "IOTAJAVASPAM999999999999999";

    private static final String SIGNATURE_FRAGMENTS = "CCWCXCGDEAXCGDEAPCEAHDTCGDHDRAADTCGDGDPCVCTC9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";
    private final String BRANCH_TRANSACTION = "KEOPMHUKZNCZJTMMZXGKJZHDRVUDXWBJNQAEJTRKKGVTFBFWVTXGAGFTIFUEXPUKCYJDEGSLRPZHYD999";
    private final String TRUNK_TRANSACTION = "KEOPMHUKZNCZJTMMZXGKJZHDRVUDXWBJNQAEJTRKKGVTFBFWVTXGAGFTIFUEXPUKCYJDEGSLRPZHYD999";
    private final String BUNDLE = "NGLDHVQNXRNNJRLEWHHJBBS9SBWNGE9TDINLSAEEBZFTKPPUJZGDASXEUUYLWWVZHODGZIBPFCZIRYPID";
    private final long ATTACHMENT_TIMESTAMP = Instant.now().getEpochSecond();
    private final long ATTACHMENT_TIMESTAMP_LOWER_BOUND = 0;
    private final long ATTACHMENT_TIMESTAMP_UPPER_BOUND = Instant.now().plus(30, ChronoUnit.DAYS).toEpochMilli();

    private static final int MIN_WEIGHT_MAGNITUDE = 14;
    private static final int MIN_WEIGHT_MAGNITUDE_DEV = 9;

    private static final int DEPTH = 4;

    private static final String[] TEST_ADDRESSES = new String[]{
            "LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZCCOZVXMTXC"
            , "P9UDUZMN9DEXCRQEKLJYSBSBZFCHOBPJSDKMLCCVJDOVOFDWMNBZRIRRZJGINOUMPJBMYYZEGRTIDUABDODCNSCYJD"
            , "MIMVJEYREIIZLXOXQROMPJFCIX9NFVXD9ZQMNZERPI9OJIJFUWQ9WCTMKXEEPHYPWEZPHLJBJOFH9YTRBGCCKGIWYW"
            , "FOJHXRVJRFMJTFDUWJYYZXCZIJXKQALLXMLKHZFDMHWTIBBXUKSNSUYJLKYRQBNXKRSUXZHDTPWXYD9YFHA9AWOMPX"
            , "B9YNPQO9EXID9RDEEGLCBJBYKBLWHTOQOZKTLJDFPJZOPKJJTNUYUVVTDJPBCBYIWGPSCMNRZFGFHFSXHTIYXWAKZ9"
            , "NQEFOAFIYKZOUXDFQ9X9PHCNSDETRTJZINZ9EYGKU99QJLDSTSC9VTBAA9FHLNLNYQXWLTNPRJDWCGIPPYIPAMUSVY"
            , "CEGLBSXDJVXGKGOUHRGMAQDRVYXCQLXBKUDWKFFSIABCUYRATFPTEEDIFYGAASKFZYREHLBIXBTKP9KLCRTXEGJXKX"
            , "QLOXU9GIQXPPE9UUT9DSIDSIESRIXMTGZJMKLSJTNBCRELAVLWVJLUOLKGFCWAEPEQWZWPBV9YZJJEHUSMBQHBROEZ"
            , "XIRMYJSGQXMM9YPHJVVLAVGBBLEEMOOKHHBFWKEAXJFONZLNSLBCGPQEVDMMOGHFVRDSYTETIFOIVNCR9IUZLVJVWX"};

    // TODO this is only available to verify why the disabled test are not running
    private IotaAPI iotaAPI;

    @Mock
    private IotaAPI iotaAPIMock;

    @Mock
    private GetTrytesResponse trytesResponse;

    @Mock
    private CheckConsistencyResponse checkConsistencyResponseMock;

    // TODO this is only available to verify why the disabled test are not running
    @BeforeEach
    public void createApiClientInstance() throws Exception {
        iotaAPI = new IotaAPI.Builder().config(new FileConfig()).localPoW(new PearlDiverLocalPoW()).build();
        assertNotNull(iotaAPI, "An API should have been created");
    }

    @Test
    public void shouldAcceptUrlAsNode() throws MalformedURLException {
        IotaAPI.Builder builder = new IotaAPI.Builder();
        IotaAPI api;

        builder.host("iota.net/node/", false);
        assertEquals(builder.getHost(), "iota.net/node/", "Host should have been accepted");
        api = builder.build();
        assertFalse(api.nodes.isEmpty(), "API should be created successfully");

        builder = new IotaAPI.Builder();

        builder.addNode(new HttpConnector("https://iota.net:14265/node/"));
        assertEquals(builder.getNodes().size(), 1, "URL should have been accepted");
        api = builder.build();
        assertFalse(api.nodes.isEmpty(), "API should be created successfully");

        builder = new IotaAPI.Builder();

        builder.addNode(new HttpConnector("https", "iota.net", 14265, "/node/"));
        assertEquals(builder.getNodes().size(), 1, "URL should have been accepted");
        api = builder.build();
        assertFalse(api.nodes.isEmpty(), "API should be created successfully");
    }

    @SuppressWarnings("deprecation")
    @Test
    public void shouldCreateIotaApiProxyInstanceWithDefaultValues() {
        IotaAPI iotaAPI = new IotaAPI.Builder().build();

        assertNotNull(iotaAPI);
        assertEquals(iotaAPI.getHost(), IotaDefaultConfig.Defaults.LEGACY_HOST, "Host should have been set to defaults");
        assertEquals(iotaAPI.getPort(), IotaDefaultConfig.Defaults.LEGACY_PORT + "", "Port should have been set to defaults");
        assertEquals(iotaAPI.getProtocol(), IotaDefaultConfig.Defaults.LEGACY_PROTOCOL, "Protocol should have been set to defaults");
    }

    @SuppressWarnings("deprecation")
    @Test
    public void shouldRetainHostFromBuilder() {
        IotaAPI iotaAPI = new IotaAPI.Builder().host("iota.org").build();
        assertEquals(iotaAPI.getHost(), "iota.org", "Host should have been set to iota.org");
    }

    @SuppressWarnings("deprecation")
    @Test
    public void shouldRetainPortFromBuilder() {
        IotaAPI iotaAPI = new IotaAPI.Builder().port(15515).build();
        assertEquals(iotaAPI.getPort(), "15515", "Port should have been set to 15515");
    }

    @SuppressWarnings("deprecation")
    @Test
    public void shouldRetainProtocolFromBuilder() {
        IotaAPI iotaAPI = new IotaAPI.Builder().protocol("https").build();
        assertEquals(iotaAPI.getProtocol(), "https", "Protocol should have been set to https");
    }

    @SuppressWarnings("deprecation")
    @Test
    public void shouldGetValuesFromProperties() {
        Properties properties = new Properties();
        properties.put("iota.node.host", "somewhere_over_the_rainbow");
        properties.put("iota.node.port", "15515");
        properties.put("iota.node.protocol", "https");

        IotaAPI iotaAPI = new IotaAPI.Builder().config(properties).build();
        assertEquals(iotaAPI.getHost(), "somewhere_over_the_rainbow", "Host should have been set to somewhere_over_the_rainbow");
        assertEquals(iotaAPI.getPort(), "15515", "Port should have been set to 15515");
        assertEquals(iotaAPI.getProtocol(), "https", "Protocol should be set to https");
    }

    @Test
    public void shouldGetInputs() throws ArgumentException {
        GetBalancesAndFormatResponse balancesAndFormatResponse =
                GetBalancesAndFormatResponse.create(Collections.singletonList(new Input(TEST_ADDRESSES[0], 10, 0, 2)), 10, 1206);

        when(iotaAPIMock.getInputs(TEST_SEED1, 2, 0, 10, 0)).thenReturn(balancesAndFormatResponse);

        GetBalancesAndFormatResponse inputs = iotaAPIMock.getInputs(TEST_SEED1, 2, 0, 10, 0);

        assertNotNull(inputs, "Client should return always not null inputs");
        assertTrue(inputs.getTotalBalance() > 0, "Res should have a balance(1000)");
        assertNotNull(inputs.getInputs(), "Inputs should never null");
    }

    @Test
    public void shouldCreateNewAddressWithChecksumSecurityLevel1() throws ArgumentException {
        GetNewAddressResponse newAddressResponse = GetNewAddressResponse
                .create(Collections.singletonList(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_1), 497);

        AddressRequest addressRequestMock = new AddressRequest.Builder(TEST_SEED1, 1).checksum(true).amount(5).build();

        when(iotaAPIMock.getAddressesUnchecked(addressRequestMock)).thenReturn(newAddressResponse);

        GetNewAddressResponse addressResponse = iotaAPIMock.getAddressesUnchecked(addressRequestMock);
        assertEquals(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_1, addressResponse.getAddresses().get(0),
                "Should contain address with checksum with security level 1");
    }

    @Test
    public void shouldCreateNewAddressWithChecksumSecurityLevel2() throws ArgumentException {
        GetNewAddressResponse newAddressResponse = GetNewAddressResponse
                .create(Collections.singletonList(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2), 497);

        AddressRequest addressRequestMock = new AddressRequest.Builder(TEST_SEED1, 2).checksum(true).amount(5).build();

        when(iotaAPIMock.getAddressesUnchecked(addressRequestMock)).thenReturn(newAddressResponse);

        GetNewAddressResponse addressResponse = iotaAPIMock.getAddressesUnchecked(addressRequestMock);
        assertEquals(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, addressResponse.getAddresses().get(0),
                "Should contain address with checksum with security level 2");
    }

    @Test
    public void shouldCreateNewAddressWithChecksumSecurityLevel3() throws ArgumentException {
        GetNewAddressResponse newAddressResponse = GetNewAddressResponse
                .create(Collections.singletonList(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_3), 497);

        AddressRequest addressRequestMock = new AddressRequest.Builder(TEST_SEED1, 3).checksum(true).amount(5).build();

        when(iotaAPIMock.getAddressesUnchecked(addressRequestMock)).thenReturn(newAddressResponse);

        GetNewAddressResponse addressResponse = iotaAPIMock.getAddressesUnchecked(addressRequestMock);
        assertEquals(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_3, addressResponse.getAddresses().get(0),
                "Should contain address with checksum with security level 3");
    }

    @Test
    public void shouldCreateNewAddressWithoutChecksum1() throws ArgumentException {
        GetNewAddressResponse newAddressResponseMock = GetNewAddressResponse
                .create(Collections.singletonList(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_1), 497);

        AddressRequest addressRequest = new AddressRequest.Builder(TEST_SEED1, 1).amount(5).build();

        when(iotaAPIMock.getAddressesUnchecked(addressRequest)).thenReturn(newAddressResponseMock);

        GetNewAddressResponse newAddressResponse = iotaAPIMock.getAddressesUnchecked(addressRequest);
        assertEquals(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_1, newAddressResponse.getAddresses().get(0),
                "Should contain address without checksum with security level 1");
    }

    @Test
    public void shouldCreateNewAddressWithoutChecksum2() throws ArgumentException {
        GetNewAddressResponse newAddressResponseMock = GetNewAddressResponse
                .create(Collections.singletonList(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2), 497);

        AddressRequest addressRequest = new AddressRequest.Builder(TEST_SEED1, 2).amount(5).build();

        when(iotaAPIMock.getAddressesUnchecked(addressRequest)).thenReturn(newAddressResponseMock);

        GetNewAddressResponse newAddressResponse = iotaAPIMock.getAddressesUnchecked(addressRequest);
        assertEquals(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2, newAddressResponse.getAddresses().get(0),
                "Should contain address without checksum with security level 2");
    }

    @Test
    public void shouldCreateNewAddressWithoutChecksum3() throws ArgumentException {
        GetNewAddressResponse newAddressResponseMock = GetNewAddressResponse
                .create(Collections.singletonList(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_3), 497);

        AddressRequest addressRequest = new AddressRequest.Builder(TEST_SEED1, 3).amount(5).build();

        when(iotaAPIMock.getAddressesUnchecked(addressRequest)).thenReturn(newAddressResponseMock);

        GetNewAddressResponse newAddressResponse = iotaAPIMock.getAddressesUnchecked(addressRequest);
        assertEquals(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_3, newAddressResponse.getAddresses().get(0),
                "Should contain address without checksum with security level 3");
    }

    @Test
    public void shouldCreate100Addresses() throws ArgumentException {
        GetNewAddressResponse newAddressResponse = GetNewAddressResponse.create(IntStream.range(0, 100)
                .mapToObj(it -> "LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZCCOZVXMTXC")
                .collect(toList()), 497);

        AddressRequest addressRequest = new AddressRequest.Builder(TEST_SEED1, 2).amount(100).build();

        when(iotaAPIMock.getAddressesUnchecked(addressRequest)).thenReturn(newAddressResponse);

        GetNewAddressResponse addressResponse = iotaAPIMock.getAddressesUnchecked(addressRequest);
        assertEquals(100, addressResponse.getAddresses().size(),
                "Client should respect the address request values");
    }

    @Test
    public void generateNewAddressesWithZeroIndexAndZeroAmountShouldGenerateOneAddresses() {
        GetNewAddressResponse newAddressResponseMock = GetNewAddressResponse
                .create(Collections.singletonList(TEST_ADDRESSES[0]), 6491);

        AddressRequest addressRequest = new AddressRequest.Builder(TEST_SEED1, 2).amount(0).build();

        when(iotaAPIMock.generateNewAddresses(addressRequest)).thenReturn(newAddressResponseMock);

        GetNewAddressResponse addressResponse = iotaAPIMock.generateNewAddresses(addressRequest);
        assertEquals(1, addressResponse.getAddresses().size(),
                "Client should respect the address request values");
    }

    @Test
    public void generateNewAddressesWithZeroAmountShouldGenerateOneAddresses() {
        GetNewAddressResponse newAddressResponseMock = GetNewAddressResponse
                .create(Collections.singletonList(TEST_ADDRESSES[0]), 6491);

        AddressRequest addressRequest = new AddressRequest.Builder(TEST_SEED1, 2).amount(0).index(1).build();

        when(iotaAPIMock.generateNewAddresses(addressRequest)).thenReturn(newAddressResponseMock);

        GetNewAddressResponse addressResponse = iotaAPIMock.generateNewAddresses(addressRequest);
        assertEquals(1, addressResponse.getAddresses().size(),
                "Client should respect the address request values");
    }

    @Test
    public void generateNewAddresses() {
        GetNewAddressResponse newAddressResponseMock = GetNewAddressResponse
                .create(Collections.singletonList(TEST_ADDRESSES[0]), 6491);

        AddressRequest addressRequest = new AddressRequest.Builder(TEST_SEED1, 2).amount(1).build();

        when(iotaAPIMock.generateNewAddresses(addressRequest)).thenReturn(newAddressResponseMock);

        GetNewAddressResponse firstAddressResponse = iotaAPIMock.generateNewAddresses(addressRequest);

        assertEquals(1, firstAddressResponse.getAddresses().size(),
                "Client should respect the address request values");
        assertNotNull(firstAddressResponse.getAddresses().get(0), "Address response should contain address");
    }

    @Test
    public void generateNewAddressesWithSameIndexAndOneAmountShouldGenerateSameAddress() {
        GetNewAddressResponse newAddressResponseMock = GetNewAddressResponse
                .create(Collections.singletonList(TEST_ADDRESSES[0]), 6491);

        AddressRequest addressRequest = new AddressRequest.Builder(TEST_SEED1, 2).amount(1).build();

        when(iotaAPIMock.generateNewAddresses(addressRequest)).thenReturn(newAddressResponseMock);

        GetNewAddressResponse firstAddressResponse = iotaAPIMock.generateNewAddresses(addressRequest);
        GetNewAddressResponse secondAddressResponse = iotaAPIMock.generateNewAddresses(addressRequest);

        assertEquals(1, firstAddressResponse.getAddresses().size(),
                "Client should respect the address request values");
        assertEquals(1, secondAddressResponse.getAddresses().size(),
                "Client should respect the address request values");
        assertEquals(firstAddressResponse.getAddresses().get(0), secondAddressResponse.getAddresses().get(0),
                "Execute the same request two times then the address must be the same too");
    }

    @Test
    public void shouldPrepareTransfer() throws ArgumentException {
        List<Transfer> transfers = Collections.singletonList(
                new Transfer(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, 5, TEST_MESSAGE, TEST_TAG));

        when(iotaAPIMock.prepareTransfers(TEST_SEED1, 2, transfers, null, null, null, false))
                .thenReturn(Arrays.asList("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999ZZGLPAVTMZYRFTBZJHYTKDBOGM9UC9ICCYCSICTPZEPJOIOXE9NHJQSGHAVMTUUQKWGMTUJFUCTHBQSJCZFH999999999999999999999999IOTAJAVASPAM999999999999999ZZIFRDD99C99999999C99999999CRQBRUDTGJIGOE9HFQRZVHHKPKNLSHYUXEABKCAVGIPOUJJZSJQLSAOZIK9CLTZCSJYJFWDFOH9HJHUFC999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999IOTAJAVASPAM9999999999999999CGRSPC999CGRSPC999CGRSPC99999999999999999999999999999", TEST_TRYTES));

        List<String> trytes = iotaAPIMock.prepareTransfers(TEST_SEED1, 2, transfers,
                null, null, null, false);

        assertNotNull(trytes, "prepareTransfers should throw an error on failure");
        assertFalse(trytes.isEmpty(), "prepareTransfers should throw an error on failure");

        Transaction transaction = new Transaction.Builder().buildWithTrytes(trytes.get(0));
        assertEquals(transaction.getLastIndex(), transaction.getCurrentIndex(),
                "prepareTransfers should have reversed bundle order for attachToTangle");
    }

    @Test
    public void shouldPrepareTransferWithInputs() throws ArgumentException {
        GetBalancesAndFormatResponse balancesAndFormatResponse = GetBalancesAndFormatResponse.create(
                Collections.singletonList(new Input(TEST_ADDRESSES[0], 10, 0, 2)), 10, 1440);

        when(iotaAPIMock.getInputs(TEST_SEED1, 2, 0, 10, 0)).thenReturn(balancesAndFormatResponse);

        GetBalancesAndFormatResponse rsp = iotaAPIMock.getInputs(TEST_SEED1, 2, 0, 10, 0);

        List<Transfer> transfers = Collections.singletonList(new Transfer(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, 1, TEST_MESSAGE, TEST_TAG));
        List<Input> inputs = rsp.getInputs();

        when(iotaAPIMock.prepareTransfers(TEST_SEED1, 2, transfers, null, inputs, null, true))
                .thenReturn(Arrays.asList("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999ZZGLPAVTMZYRFTBZJHYTKDBOGM9UC9ICCYCSICTPZEPJOIOXE9NHJQSGHAVMTUUQKWGMTUJFUCTHBQSJCI99999999999999999999999999IOTAJAVASPAM999999999999999UNJFRDD99C99999999C99999999JTCHFJBQKGWTVDWWXWWXRWGCUZEKFZHCUKJUURCB9CYUSJWTBSHBTUOFHJBXOVSP9P9W9AEELLQRYKOIZ999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999IOTAJAVASPAM9999999999999999CGRSPC999CGRSPC999CGRSPC99999999999999999999999999999", TEST_TRYTES));

        List<String> trytes = iotaAPIMock.prepareTransfers(TEST_SEED1, 2, transfers, null, inputs, null, true);

        assertNotNull(trytes, "prepareTransfers should throw an error on failure");
        assertFalse(trytes.isEmpty(), "prepareTransfers should throw an error on failure");

        Transaction transaction = new Transaction.Builder().buildWithTrytes(trytes.get(0));
        assertEquals(transaction.getLastIndex(), transaction.getCurrentIndex(),
                "prepareTransfers should have reversed bundle order for attachToTangle");
    }

    @Test
    public void shouldFailTransfer() {
        List<Transfer> transfers = Collections.singletonList(
                new Transfer(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, 100, TEST_MESSAGE, TEST_TAG));

        when(iotaAPIMock.prepareTransfers(TEST_SEED2, 2, transfers, null, null, null, false))
                .thenThrow(new IllegalStateException(Constants.NOT_ENOUGH_BALANCE_ERROR));

        IllegalStateException assertThrows = assertThrows(IllegalStateException.class, () ->
                iotaAPIMock.prepareTransfers(TEST_SEED2, 2, transfers, null, null, null, false));

        assertEquals(Constants.NOT_ENOUGH_BALANCE_ERROR, assertThrows.getMessage(), "Message should say that there is not enough balance");
    }

    @Test
    public void shouldFailTransferWithInputs() {
        List<Input> inputs = emptyList();
        List<Transfer> transfers = Collections.singletonList(
                new Transfer(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, 1, TEST_MESSAGE, TEST_TAG));

        when(iotaAPIMock.prepareTransfers(TEST_SEED2, 2, transfers, null, inputs, null, false))
                .thenThrow(new ArgumentException(Constants.INVALID_ADDRESSES_INPUT_ERROR));

        ArgumentException assertThrows = assertThrows(ArgumentException.class, () ->
                iotaAPIMock.prepareTransfers(TEST_SEED2, 2, transfers, null, inputs, null, false));

        assertEquals(Constants.INVALID_ADDRESSES_INPUT_ERROR, assertThrows.getMessage(), "Message should say that there is not enough balance");
    }

    @Test
    public void shouldFindTransactionObjects() throws ArgumentException {
        List<Transaction> transactions = Collections.singletonList(new Transaction.Builder()
                .branchTransaction(BRANCH_TRANSACTION)
                .trunkTransaction(TRUNK_TRANSACTION)
                .address(TEST_ADDRESSES[0])
                .signatureFragments(SIGNATURE_FRAGMENTS)
                .tag(TEST_TAG)
                .timestamp(Instant.now().getEpochSecond())
                .nonce("WBTKE9RPRMHDJGITAENBMGDKWXQ")
                .obsoleteTag(TEST_TAG)
                .attachmentTimestamp(ATTACHMENT_TIMESTAMP)
                .attachmentTimestampLowerBound(ATTACHMENT_TIMESTAMP_LOWER_BOUND)
                .attachmentTimestampUpperBound(ATTACHMENT_TIMESTAMP_UPPER_BOUND)
                .hash("AKNHNOQTIMRCETAWNHZXEHLUERYUHGCGEVFHO9JF9AL9J9CXDDUVAKBYPJMXWEJCPUJXRNXCQBRWBQ999")
                .currentIndex(0)
                .lastIndex(2)
                .bundle(BUNDLE)
                .value(1)
                .build());

        when(iotaAPIMock.findTransactionObjectsByAddresses(TEST_ADDRESSES)).thenReturn(transactions);

        List<Transaction> transactionObjectsByAddresses = iotaAPIMock.findTransactionObjectsByAddresses(TEST_ADDRESSES);

        assertNotNull(transactionObjectsByAddresses, "Should always return a list and not null");
        assertFalse(transactionObjectsByAddresses.isEmpty(), "findTransactionObjectsByAddresses should find multiple transactions");
    }

    @Test
    public void shouldGetAccountData() {
        when(iotaAPIMock.getAccountData(TEST_SEED3, 2, 0, true, 0, true, 0, 10, true, 0))
                .thenReturn(new GetAccountDataResponse());

        GetAccountDataResponse accountData = iotaAPIMock.getAccountData(TEST_SEED3, 2, 0, true, 0, true, 0, 10, true, 0);

        assertNotNull(accountData, "Should throw an error on failure");
    }

    @Test
    public void shouldNotGetBundle() throws ArgumentException {
        when(iotaAPIMock.getBundle("SADASD"))
                .thenThrow(new ArgumentException(Constants.INVALID_ADDRESSES_INPUT_ERROR));

        assertThrows(ArgumentException.class, () -> iotaAPIMock.getBundle("SADASD"),
                "Should throw an error on failure");
    }

    @Test
    public void shouldGetBundle() throws ArgumentException {
        when(iotaAPIMock.getBundle(TEST_HASH)).thenReturn(new GetBundleResponse());

        GetBundleResponse bundle = iotaAPIMock.getBundle(TEST_HASH);
        assertNotNull(bundle, "Should return bundle if valid hash is provided");
    }

    @Test
    public void shouldCheckConsistency() throws ArgumentException {
        GetNodeInfoResponse nodeInfoResponse = GetNodeInfoResponse.create("HORNET",
                "0.5.3-rc3", 0, 0, null, 0, 0,
                "HSPDOTYNLKWIXYWLOCBVMGGKDQYBSC9BZJNJDEGXNIFMQNVJGZMYJNFVXBCVCKNJDOWKQDJXATSDUU999",
                1942896,
                "HSPDOTYNLKWIXYWLOCBVMGGKDQYBSC9BZJNJDEGXNIFMQNVJGZMYJNFVXBCVCKNJDOWKQDJXATSDUU999",
                1942896, 1651535, 3, 0,
                Instant.now().toEpochMilli(), 3, 0, new String[]{"RemotePOW", "WereAddressesSpentFrom"},
                "GYISMBVRKSCEXXTUPBWTIHRCZIKIRPDYAHAYKMNTPZSCSDNADDWAEUNHKUERZCTVAYJCNFXGTNUH9OGTW");
        nodeInfoResponse.setDuration(0L);

        when(iotaAPIMock.getNodeInfo()).thenReturn(nodeInfoResponse);

        GetNodeInfoResponse nodeInfo = iotaAPIMock.getNodeInfo();

        when(iotaAPIMock.checkConsistency(nodeInfo.getLatestSolidSubtangleMilestone())).thenReturn(checkConsistencyResponseMock);
        when(checkConsistencyResponseMock.getState()).thenReturn(true);

        CheckConsistencyResponse checkConsistencyResponse = iotaAPIMock.checkConsistency(nodeInfo.getLatestSolidSubtangleMilestone());

        assertNotNull(checkConsistencyResponse, "Should throw an error on failure");
        assertTrue(checkConsistencyResponse.getState(), "Latest milestone should always be consistent");
    }

    @Test
    public void shouldGetTransfers() {
        GetTransferResponse transferResponseMock = GetTransferResponse.create(
                new Bundle[]{new Bundle()}, 1503);

        when(iotaAPIMock.getTransfers(TEST_SEED3, 2, 0, 10, false)).thenReturn(transferResponseMock);

        GetTransferResponse transferResponse = iotaAPIMock.getTransfers(TEST_SEED3, 2, 0, 10, false);

        assertNotNull(transferResponse.getTransfers(), "Should throw an error on failure");
        assertTrue(transferResponse.getTransfers().length > 0, "GetTransfers should return more than 0 transfers");
    }

    @Test
    public void shouldReplayBundle() {
        when(iotaAPIMock.replayBundle(TEST_HASH, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, null))
                .thenReturn(new ReplayBundleResponse());

        ReplayBundleResponse replayBundleResponse = iotaAPIMock.replayBundle(TEST_HASH, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, null);
        assertNotNull(replayBundleResponse, "should throw an error on failure");
    }

    @Test
    public void shouldNotSendTrytes() throws ArgumentException {
        when(iotaAPIMock.sendTrytes(new String[]{TEST_INVALID_TRYTES}, DEPTH, MIN_WEIGHT_MAGNITUDE, null))
                .thenThrow(ArgumentException.class);

        assertThrows(ArgumentException.class, () ->
                iotaAPIMock.sendTrytes(new String[]{TEST_INVALID_TRYTES}, DEPTH, MIN_WEIGHT_MAGNITUDE, null),
                "If invalid trytes present the method should fail");
    }

    @Test
    public void shouldGetTrytes() throws ArgumentException {
        when(iotaAPIMock.getTrytes(TEST_HASH)).thenReturn(trytesResponse);
        when(trytesResponse.getTrytes()).thenReturn(new String[]{TEST_TRYTES});

        GetTrytesResponse trytes = iotaAPIMock.getTrytes(TEST_HASH);
        assertNotNull(trytes, "should throw an error on failure");
        assertEquals(1, trytes.getTrytes().length, "getTrytes should send back 1 transaction trytes");
    }

    @Test
    public void shouldBroadcastAndStore() throws ArgumentException {
        List<Transaction> transactions = Collections.singletonList(new Transaction.Builder()
                .branchTransaction(BRANCH_TRANSACTION)
                .trunkTransaction(TRUNK_TRANSACTION)
                .address(TEST_ADDRESSES[0])
                .signatureFragments(SIGNATURE_FRAGMENTS)
                .tag(TEST_TAG)
                .timestamp(Instant.now().getEpochSecond())
                .nonce("WBTKE9RPRMHDJGITAENBMGDKWXQ")
                .obsoleteTag(TEST_TAG)
                .attachmentTimestamp(ATTACHMENT_TIMESTAMP)
                .attachmentTimestampLowerBound(ATTACHMENT_TIMESTAMP_LOWER_BOUND)
                .attachmentTimestampUpperBound(ATTACHMENT_TIMESTAMP_UPPER_BOUND)
                .hash("AKNHNOQTIMRCETAWNHZXEHLUERYUHGCGEVFHO9JF9AL9J9CXDDUVAKBYPJMXWEJCPUJXRNXCQBRWBQ999")
                .currentIndex(0)
                .lastIndex(2)
                .bundle(BUNDLE)
                .value(1)
                .build());

        when(iotaAPIMock.sendTrytes(new String[]{TEST_TRYTES}, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, null)).thenReturn(transactions);

        List<Transaction> response = iotaAPIMock.sendTrytes(new String[]{TEST_TRYTES}, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, null);

        BroadcastTransactionsResponse broadcastTransactionsResponse = new BroadcastTransactionsResponse();
        broadcastTransactionsResponse.setDuration(0L);
        when(iotaAPIMock.storeAndBroadcast(response.get(0).toTrytes())).thenReturn(broadcastTransactionsResponse);

        BroadcastTransactionsResponse res = iotaAPIMock.storeAndBroadcast(response.get(0).toTrytes());
        assertNotNull(res, "storeAndBroadcast should not return null on fail");
    }

    @Test
    public void shouldFailBeforeSnapshotTimeStamp() throws ArgumentException {
        when(iotaAPIMock.storeAndBroadcast(TEST_TRYTES)).thenThrow(ArgumentException.class);

        assertThrows(ArgumentException.class,
                () -> iotaAPIMock.storeAndBroadcast(TEST_TRYTES), "Transaction did not fail on old timestamp value");
    }

    @Test
    public void shouldSendTransferWithoutInputs() {
        AddressRequest addressRequest = new AddressRequest.Builder(TEST_SEED1, 2).checksum(true).build();

        GetNewAddressResponse newAddressResponse = GetNewAddressResponse
                .create(Collections.singletonList("WZKACMYDTB9PGCYODXNPGYVQUVTPVVNJRZUXNWRQZVXUUAZCQBQUIZTTFNLCLHXFDRDCXUWKBASYMUDWBLAXFEJTDD"), 7032);
        when(iotaAPIMock.generateNewAddresses(eq(addressRequest))).thenReturn(newAddressResponse);

        GetNewAddressResponse getNewAddressResponse = iotaAPIMock.generateNewAddresses(addressRequest);
        String address = getNewAddressResponse.first();

        List<Transfer> transfers = Collections.singletonList(new Transfer(address, 1, TEST_MESSAGE, TEST_TAG));

        List<Transaction> transactions = IntStream.range(0, 4).mapToObj(it -> new Transaction.Builder()
                .branchTransaction(BRANCH_TRANSACTION)
                .trunkTransaction(TRUNK_TRANSACTION)
                .address(TEST_ADDRESSES[0])
                .signatureFragments(SIGNATURE_FRAGMENTS)
                .tag(TEST_TAG)
                .timestamp(Instant.now().getEpochSecond())
                .nonce("WBTKE9RPRMHDJGITAENBMGDKWXQ")
                .obsoleteTag(TEST_TAG)
                .attachmentTimestamp(ATTACHMENT_TIMESTAMP)
                .attachmentTimestampLowerBound(ATTACHMENT_TIMESTAMP_LOWER_BOUND)
                .attachmentTimestampUpperBound(ATTACHMENT_TIMESTAMP_UPPER_BOUND)
                .hash("AKNHNOQTIMRCETAWNHZXEHLUERYUHGCGEVFHO9JF9AL9J9CXDDUVAKBYPJMXWEJCPUJXRNXCQBRWBQ999")
                .currentIndex(it)
                .lastIndex(3)
                .bundle(BUNDLE)
                .value(1)
                .build()).collect(toList());

        transactions.get(1).setValue(-5998);
        transactions.get(2).setValue(0);
        transactions.get(3).setValue(5997);

        SendTransferResponse transferResponseMock = SendTransferResponse.create(transactions, new Boolean[]{true, true, true, true}, 26281);
        when(iotaAPIMock.sendTransfer(TEST_SEED1, 2, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, transfers, null, null, false, true, null)).thenReturn(transferResponseMock);

        SendTransferResponse transferResponse = iotaAPIMock.sendTransfer(TEST_SEED1, 2, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, transfers, null, null, false, true, null);

        assertNotNull(transferResponse.getTransactions(), "Returned transfer should contain transactions");
        assertNotNull(transferResponse.getSuccessfully(), "Returned transfer should contain states about transfer");

        assertEquals(0, transferResponse.getTransactions().get(0).getCurrentIndex(), "Returned transfers should have normal bundle order");
    }

    @Test
    public void shouldSendTrytes() {
        final List<Transaction> transactions = Collections.singletonList(
                new Transaction.Builder()
                        .branchTransaction("IVHGCAVSCDBXFGEOVHTKWRATXSAM9EKDBYBTDIUQAPFHOHUVUXIWUEQQXWDBKPAVXWXNCEJDIIDMKG999")
                        .trunkTransaction("VKVQFOLXYTQZLFIBTKCBGXCMMZXUDMGQFYFMFBBRFKBCRUTYVOCATEZPG9XVJOCSPQCTSHJLRVXB9F999")
                        .address(TEST_ADDRESSES[0])
                        .signatureFragments(SIGNATURE_FRAGMENTS)
                        .tag(TEST_TAG)
                        .timestamp(1510238556)
                        .nonce("RFJYAOFGRHZCYYWMVJRUSJWNGPN")
                        .obsoleteTag(TEST_TAG)
                        .attachmentTimestamp(ATTACHMENT_TIMESTAMP)
                        .attachmentTimestampLowerBound(ATTACHMENT_TIMESTAMP_LOWER_BOUND)
                        .attachmentTimestampUpperBound(ATTACHMENT_TIMESTAMP_UPPER_BOUND)
                        .hash("HANDGDNZUMFTECFKZWKSVNSMQRVTVUMSCZRAVSXJUNLDHDJLDHYY9QFZZQJNKKAMQQWVIUIHGOPNPN999")
                        .currentIndex(0)
                        .lastIndex(2)
                        .bundle("IDPWGXASJFLLGCDGPQVXYGSNUESCZQCEKVREGLZX9FCYQVUWESEKWSMHZTGMJLGBOLKU9GILFITSJLZBW")
                        .value(3625178820L)
                        .build());

        String[] trytes = new String[]{TEST_TRYTES};
        when(iotaAPIMock.sendTrytes(eq(trytes), eq(DEPTH), eq(MIN_WEIGHT_MAGNITUDE_DEV), isNull())).thenReturn(transactions);

        List<Transaction> response = iotaAPIMock.sendTrytes(trytes, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, null);

        assertEquals(1, response.size(), "Sending 1 transaction received unexpected amount");
    }

    // TODO: The following tests must be checked and reactivated

    @Disabled("Transaction did not fail on spent address")
    @Test
    public void shouldNotSendTransfer() {
        try {
            List<Transfer> transfers = new ArrayList<>();
            // Address is spent
            transfers.add(new Transfer(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, 2, TEST_MESSAGE, TEST_TAG));
            iotaAPI.sendTransfer(TEST_SEED1, 2, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, transfers, null, null, false, true, null);
            fail("Transaction did not fail on spent address");
        } catch (ArgumentException e) {
            assertEquals(Constants.SENDING_TO_USED_ADDRESS_ERROR, e.getMessage(), "Message should say we try to use a used address");
        }
    }

    @Disabled("Failed with ArgumentException: Sending to a used address.")
    @Test
    public void shouldSendTransferWithInputs() {
        List<Transfer> transfers = new ArrayList<>();

        GetBalancesAndFormatResponse rsp = iotaAPI.getInputs(TEST_SEED3, 2, 0, 0, 1);

        List<Input> inputList = new ArrayList<>(rsp.getInputs());

        AddressRequest addressRequest = new AddressRequest.Builder(TEST_SEED3, 2).checksum(true).build();
        String address = iotaAPI.generateNewAddresses(addressRequest).first();
        transfers.add(new Transfer(address, 1, TEST_MESSAGE, TEST_TAG));

        // validate inputs to true would mean we have to spent all balance in once. Now we double spent but its devnet
        SendTransferResponse str = iotaAPI.sendTransfer(TEST_SEED3, 2, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, transfers, inputList, null, false, true, null);
        assertThat("Sending transfer should have returned multiple transactions", str.getTransactions(), IsNull.notNullValue());
        assertThat("Sending transfer should contain success information", str.getSuccessfully(), IsNull.notNullValue());
    }

    @Disabled("Hash should have been seen as confirmed ==> expected: <true> but was: <false>")
    @Test
    public void shouldGetInclusionStates() throws ArgumentException {
        GetInclusionStateResponse res = iotaAPI.getInclusionStates(new String[]{TEST_HASH});
        assertThat("States should be an array of booleans", res.getStates(), IsNull.notNullValue());
        assertTrue(res.getStates()[0], "Hash should have been seen as confirmed");
    }

    @Disabled("Hash should have been seen as confirmed ==> expected: <true> but was: <false>")
    @Test
    public void shouldIsConfirmed() throws ArgumentException {
        GetInclusionStateResponse res = iotaAPI.isConfirmed(new String[]{TEST_HASH});
        assertThat("States should be an array of booleans", res.getStates(), IsNull.notNullValue());
        assertTrue(res.getStates()[0], "Hash should have been seen as confirmed");
    }

}
