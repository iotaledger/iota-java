package org.iota.jota;

import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hamcrest.core.IsNull;
import org.iota.jota.builder.AddressRequest;
import org.iota.jota.config.types.FileConfig;
import org.iota.jota.config.types.IotaDefaultConfig;
import org.iota.jota.dto.response.*;
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
import org.iota.jota.model.Input;
import org.iota.jota.model.Transaction;
import org.iota.jota.model.Transfer;

import org.iota.jota.pow.pearldiver.PearlDiverLocalPoW;

import org.iota.jota.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Let's do some integration test coverage against a devnet node
 * Careful with running these tests too fast, the balance might not work in that case
 */
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

    private static final int MIN_WEIGHT_MAGNITUDE = 14;
    private static final int MIN_WEIGHT_MAGNITUDE_DEV = 9;

    private static final int DEPTH = 9;

    private static final String[] TEST_ADDRESSES = new String[]{
            "LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZCCOZVXMTXC"
            , "P9UDUZMN9DEXCRQEKLJYSBSBZFCHOBPJSDKMLCCVJDOVOFDWMNBZRIRRZJGINOUMPJBMYYZEGRTIDUABDODCNSCYJD"
            , "MIMVJEYREIIZLXOXQROMPJFCIX9NFVXD9ZQMNZERPI9OJIJFUWQ9WCTMKXEEPHYPWEZPHLJBJOFH9YTRBGCCKGIWYW"
            , "FOJHXRVJRFMJTFDUWJYYZXCZIJXKQALLXMLKHZFDMHWTIBBXUKSNSUYJLKYRQBNXKRSUXZHDTPWXYD9YFHA9AWOMPX"
            , "B9YNPQO9EXID9RDEEGLCBJBYKBLWHTOQOZKTLJDFPJZOPKJJTNUYUVVTDJPBCBYIWGPSCMNRZFGFHFSXHTIYXWAKZ9"
            , "NQEFOAFIYKZOUXDFQ9X9PHCNSDETRTJZINZ9EYGKU99QJLDSTSC9VTBAA9FHLNLNYQXWLTNPRJDWCGIPPYIPAMUSVY"
            , "CEGLBSXDJVXGKGOUHRGMAQDRVYXCQLXBKUDWKFFSIABCUYRATFPTEEDIFYGAASKFZYREHLBIXBTKP9KLCRTXEGJXKX"
            , "QLOXU9GIQXPPE9UUT9DSIDSIESRIXMTGZJMKLSJTNBCRELAVLWVJLUOLKGFCWAEPEQWZWPBV9YZJJEHUSMBQHBROEZ"
            , "XIRMYJSGQXMM9YPHJVVLAVGBBLEEMOOKHHBFWKEAXJFONZLNSLBCGPQEVDMMOGHFVRDSYTETIFOIVNCR9IUZLVJVWX"
            , "PDVVBYBXMHZKADPAYOKQNDPHRSWTHAWQ9GRVIBOIMZQTYCWEPCDWDVRSOUNASVBDLBOAMVLYEVVCMAM9NPLXNSO9BD"
            , "U9GAIAPUUQWJGISAZWPLHUELTZ9WSHWXS9JLPKOWHRRIVUKGWCTJMBULVMKTETTUNHZ9HWHBALUCJIROUBRIIYCUHC"
            , "VFPMKZLLMDUOEKNBEKQZPTNZJZF9UHRWSTHXLWQQ9OAXTZQHTZPAWNJNXKAZFSDFWKFQEKZIGJTLWQFLOBXMPLSQNB"
            , "IGHK9XIWOAYBZUEZHQLEXBPTXSWVANIOUZZCPNKUIJIJOJNAQCJWUJHYKCZOIKVAAHDGAWJZKLTPVQL9GJSCYKNWTW"
            , "LXQPWMNXSUZTEYNC9ZBBFHY9YWCCOVKBNIIOUSVXZJZMJKJFDUWGUVXYCHGKUHEEIDHSGEWFAHVJPRIJTJCNU9GJAC"
            , "AKFDX9PGGQLZUWRMZ9YBDF9CG9TWXCNALCSXSAWHFIMGXCSYCJLSWIQDGGVDRMNEKKECQEYAITGNLNJFQCFALBLRZX"
            , "YX9QSPYMSFVOW9UVZRDVOCPYYMUTDHCCPKHMXQSJQJYIXVCHILKW9GBYJTYGLIKBTRQMDCYBMLLNGSSIKNQOHMSKTD"
            , "DSYCJKNG9TAGJHSKZQ9XLKAKNSKJFZIPVEDGJFXRTFGENHZFQGXHWDBNXLLDABDMOYELPG9DIXSNJFWARNURMPPVKC"
            , "9ANNACZYLDDPZILLQBQG9YMG9XJUMTAENDFQ9HMSSEFWYOAXPJTUXBFTSAXDJPAO9FKTWBBSCSFMOUR9IDQNHAFE9W"
            , "WDTFFXHBHMFQQVXQLBFJFVVHVIIAVYM9PFAZCHMKET9ESMHIRHSMVDJBZTXPTAFVIASMSXRDCIYVWVQNODEVYEGMVW"
            , "XCCPS9GMTSUB9DXPVKLTBDHOFX9PJMBYZQYQEXMRQDPGQPLWRGZGXODYJKGVFOHHYUJRCSXAIDGYSAWRBRXZJONAYW"
            , "KVEBCGMEOPDPRCQBPIEMZTTXYBURGZVNH9PLHKPMM9D9FUKWIGLKZROGNSYIFHULLWQWXCNAW9HKKVIDCTGKHAVBJZ"};

    private IotaAPI iotaAPI;

    @BeforeEach
    public void createApiClientInstance() throws Exception {
        iotaAPI = new IotaAPI.Builder().config(new FileConfig()).localPoW(new PearlDiverLocalPoW()).build();
        assertNotNull(iotaAPI, "An API should have been created");
    }

    @SuppressWarnings("deprecation")
    @Test
    public void shouldCreateIotaApiProxyInstanceWithDefaultValues() {
        iotaAPI = new IotaAPI.Builder().build();
        assertNotNull(iotaAPI);
        assertEquals(iotaAPI.getHost(), IotaDefaultConfig.Defaults.LEGACY_HOST, "Host should have been set to defaults");
        assertEquals(iotaAPI.getPort(),IotaDefaultConfig.Defaults.LEGACY_PORT + "", "Port should have been set to defaults");
        assertEquals(iotaAPI.getProtocol(), IotaDefaultConfig.Defaults.LEGACY_PROTOCOL, "Protocol should have been set to defaults");
    }

    @SuppressWarnings("deprecation")
    @Test
    public void shouldRetainValuesFromBuilder() {
        iotaAPI = new IotaAPI.Builder().host("iota.org").build();
        assertEquals(iotaAPI.getHost(),"iota.org", "Host should have been set to iota.org");

        iotaAPI = new IotaAPI.Builder().port(15515).build();
        assertEquals(iotaAPI.getPort(),"15515", "Port should have been set to 15515");

        iotaAPI = new IotaAPI.Builder().protocol("https").build();
        assertEquals(iotaAPI.getProtocol(),"https", "Protocol should have been set to https");
    }

    @SuppressWarnings("deprecation")
    @Test
    public void shouldGetValuesFromProperties() {
        Properties properties = new Properties();

        properties.put("iota.node.host", "somewhere_over_the_rainbow");
        iotaAPI = new IotaAPI.Builder().config(properties).build();
        assertEquals(iotaAPI.getHost(),"somewhere_over_the_rainbow", "Host should have been set to somewhere_over_the_rainbow");

        properties = new Properties();
        properties.put("iota.node.port", "15515");
        iotaAPI = new IotaAPI.Builder().config(properties).build();
        assertEquals(iotaAPI.getPort(),"15515", "Port should have been set to 15515");

        properties = new Properties();
        properties.put("iota.node.protocol", "https");
        iotaAPI = new IotaAPI.Builder().config(properties).build();
        assertEquals(iotaAPI.getProtocol(),"https", "Protocol should be set to https");
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldGetInputs() throws ArgumentException {
        // Address 0 should contain 1000
        GetBalancesAndFormatResponse res = iotaAPI.getInputs(TEST_SEED1, 2, 0, 10, 0);

        assertThat("Error on getInputs should have thrown", res, IsNull.notNullValue());
        assertTrue(res.getTotalBalance() > 0, "Res should have a balance(1000)");
        assertThat("Error on getInputs should have thrown", res.getInputs(), IsNull.notNullValue());
    }

    @Test
    public void shouldCreateANewAddressWithChecksum() throws ArgumentException {
        AddressRequest addressRequest = new AddressRequest.Builder(TEST_SEED1, 1).checksum(true).amount(5).build();
        final GetNewAddressResponse res1 = iotaAPI.getAddressesUnchecked(addressRequest);
        assertEquals(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_1, res1.getAddresses().get(0));

        AddressRequest secondAddressRequest = new AddressRequest.Builder(TEST_SEED1, 2).checksum(true).amount(5).build();
        final GetNewAddressResponse res2 = iotaAPI.getAddressesUnchecked(secondAddressRequest);
        assertEquals(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, res2.getAddresses().get(0));

        AddressRequest thirdAddressRequest = new AddressRequest.Builder(TEST_SEED1, 3).checksum(true).amount(5).build();
        final GetNewAddressResponse res3 = iotaAPI.getAddressesUnchecked(thirdAddressRequest);
        assertEquals(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_3, res3.getAddresses().get(0));
    }

    @Test
    public void shouldCreateANewAddressWithoutChecksum() throws ArgumentException {
        AddressRequest addressRequest = new AddressRequest.Builder(TEST_SEED1, 1).amount(5).build();
        final GetNewAddressResponse res1 = iotaAPI.getAddressesUnchecked(addressRequest);
        assertEquals(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_1, res1.getAddresses().get(0));

        AddressRequest secondAddressRequest = new AddressRequest.Builder(TEST_SEED1, 2).amount(5).build();
        final GetNewAddressResponse res2 = iotaAPI.getAddressesUnchecked(secondAddressRequest);
        assertEquals(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2, res2.getAddresses().get(0));

        AddressRequest thirdAddressRequest = new AddressRequest.Builder(TEST_SEED1, 3).amount(5).build();
        final GetNewAddressResponse res3 = iotaAPI.getAddressesUnchecked(thirdAddressRequest);
        assertEquals(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_3, res3.getAddresses().get(0));
    }

    @Test
    public void shouldCreate100Addresses() throws ArgumentException {
        AddressRequest addressRequest = new AddressRequest.Builder(TEST_SEED1, 2).amount(100).build();
        GetNewAddressResponse res = iotaAPI.getAddressesUnchecked(addressRequest);
        assertEquals(100, res.getAddresses().size());
    }

    @Test
    public void generateNewAddressesWithZeroIndexAndZeroAmountShouldGenerateOneAddresses() {
        AddressRequest addressRequest = new AddressRequest.Builder(TEST_SEED1, 2).amount(0).build();
        GetNewAddressResponse addressResponse = iotaAPI.generateNewAddresses(addressRequest);
        assertEquals(1, addressResponse.getAddresses().size());
    }

    @Test
    public void generateNewAddressesWithZeroAmountShouldGenerateOneAddresses() {
        AddressRequest addressRequest = new AddressRequest.Builder(TEST_SEED1, 2).amount(0).index(1).build();
        GetNewAddressResponse addressResponse = iotaAPI.generateNewAddresses(addressRequest);
        assertEquals(1, addressResponse.getAddresses().size());
    }

    @Test
    public void generateNewAddresses() {
        AddressRequest addressRequest = new AddressRequest.Builder(TEST_SEED1, 2).amount(1).build();
        GetNewAddressResponse firstAddressResponse = iotaAPI.generateNewAddresses(addressRequest);
        assertEquals(1, firstAddressResponse.getAddresses().size());
        assertNotNull(firstAddressResponse.getAddresses().get(0));
    }

    @Test
    public void generateNewAddressesWithSameIndexAndOneAmountShouldGenerateSameAddress() {
        AddressRequest addressRequest = new AddressRequest.Builder(TEST_SEED1, 2).amount(1).build();
        GetNewAddressResponse firstAddressResponse = iotaAPI.generateNewAddresses(addressRequest);
        GetNewAddressResponse secondAddressResponse = iotaAPI.generateNewAddresses(addressRequest);
        assertEquals(1, firstAddressResponse.getAddresses().size());
        assertEquals(1, secondAddressResponse.getAddresses().size());
        assertEquals(firstAddressResponse.getAddresses().get(0), secondAddressResponse.getAddresses().get(0));
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldPrepareTransfer() throws ArgumentException {
        List<Transfer> transfers = new ArrayList<>();

        transfers.add(new Transfer(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, 5, TEST_MESSAGE, TEST_TAG));
        List<String> trytes = iotaAPI.prepareTransfers(TEST_SEED1, 2, transfers, null, null, null, false);

        assertNotNull(trytes, "prepareTransfers should throw an error on failure");
        assertFalse(trytes.isEmpty(), "prepareTransfers should throw an error on failure");

        Transaction first = new Transaction(trytes.get(0));
        assertEquals(first.getLastIndex(), first.getCurrentIndex(), "prepareTransfers should have reversed bundle order for attachToTangle");
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldPrepareTransferWithInputs() throws ArgumentException {
        List<Input> inputList;
        List<Transfer> transfers = new ArrayList<>();

        GetBalancesAndFormatResponse rsp = iotaAPI.getInputs(TEST_SEED1, 2, 0, 10, 0);

        inputList  = new ArrayList<>(rsp.getInputs());

        transfers.add(new Transfer(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, 1, TEST_MESSAGE, TEST_TAG));
        List<String> trytes = iotaAPI.prepareTransfers(TEST_SEED1, 2, transfers, null, inputList, null, true);

        assertNotNull(trytes, "prepareTransfers should throw an error on failure");
        assertFalse(trytes.isEmpty(), "prepareTransfers should throw an error on failure");

        Transaction first = new Transaction(trytes.get(0));
        assertEquals(first.getLastIndex(), first.getCurrentIndex(), "prepareTransfers should have reversed bundle order for attachToTangle");
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldFailTransfer() {
        try {
            List<Transfer> transfers = new ArrayList<>();

            transfers.add(new Transfer(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, 100, TEST_MESSAGE, TEST_TAG));
            iotaAPI.prepareTransfers(TEST_SEED2, 2, transfers, null, null, null, false);

            fail("prepareTransfers should have thrown an error due to lack of balance on the seed");
        } catch (IllegalStateException e){
            assertEquals(Constants.NOT_ENOUGH_BALANCE_ERROR, e.getMessage(), "Message should say that there is not enough balance");
        }
    }

    //seed contains 0 balance -> wrong input fields as inputs arent valid
    @Test
    @Tag("IntegrationTest")
    public void shouldFailTransferWithInputs(){
        try {
            List<Input> inputlist = new ArrayList<>();
            List<Transfer> transfers = new ArrayList<>();
            transfers.add(new Transfer(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, 1, TEST_MESSAGE, TEST_TAG));
            iotaAPI.prepareTransfers(TEST_SEED2, 2, transfers, null, inputlist, null, true);

            fail("prepareTransfer should have thrown an error on wrong/lack of inputs");
        } catch (ArgumentException e){
            assertEquals(Constants.INVALID_ADDRESSES_INPUT_ERROR, e.getMessage(), "Message should say that the input is invalid");
        }
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldGetLastInclusionState() throws ArgumentException {
        GetInclusionStateResponse res = iotaAPI.getLatestInclusion(new String[]{TEST_HASH});
        assertThat("States should be an array of booleans", res.getStates(), IsNull.notNullValue());
        assertTrue(res.getStates()[0], "Hash should have been seen as confirmed");
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldFindTransactionObjects() throws ArgumentException {
        List<Transaction> ftr = iotaAPI.findTransactionObjectsByAddresses(TEST_ADDRESSES);
        assertThat("findTransactionObjectsByAddresses should not return null on failure", ftr, IsNull.notNullValue());

        assertFalse(ftr.isEmpty(), "findTransactionObjectsByAddresses should find multiple transactions");

    }

    @Test
    @Tag("IntegrationTest")
    public void shouldGetAccountData(){
        GetAccountDataResponse gad = iotaAPI.getAccountData(TEST_SEED3, 2, 0, true, 0, true, 0, 10, true, 0);
        assertThat("GetAccountDataResponse should not return null on failure", gad, IsNull.notNullValue());
    }

    @Test
    public void shouldNotGetBundle() throws ArgumentException {
        assertThrows(ArgumentException.class, () -> iotaAPI.getBundle("SADASD"));
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldGetBundle() throws ArgumentException {
        GetBundleResponse gbr = iotaAPI.getBundle(TEST_HASH);
        assertThat("GetBundleResponse should not return null on failure", gbr, IsNull.notNullValue());
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldCheckConsistency() throws ArgumentException {
        GetNodeInfoResponse gni = iotaAPI.getNodeInfo();
        CheckConsistencyResponse ccr = iotaAPI.checkConsistency(gni.getLatestSolidSubtangleMilestone());
        assertThat("CheckConsistencyResponse should not return null on failure", ccr, IsNull.notNullValue());
        assertTrue(ccr.getState(), "Latest milestone should always be consistent");
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldGetTransfers(){
        GetTransferResponse gtr = iotaAPI.getTransfers(TEST_SEED3, 2, 0, 10, false);
        assertThat("GetTransfers should return GetTransferResponse object on success", gtr.getTransfers(), IsNull.notNullValue());
        assertTrue(gtr.getTransfers().length > 0, "GetTransfers should return more than 0 transfers");
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldReplayBundle(){
        ReplayBundleResponse rbr = iotaAPI.replayBundle(TEST_HASH, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, null);
        assertThat("Bundle should be replayed", rbr, IsNull.notNullValue());
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldNotSendTrytes() throws ArgumentException {
        assertThrows(ArgumentException.class, () ->
                iotaAPI.sendTrytes(new String[]{TEST_INVALID_TRYTES}, DEPTH, MIN_WEIGHT_MAGNITUDE, null));
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldGetTrytes() throws ArgumentException {
        GetTrytesResponse trytes = iotaAPI.getTrytes(TEST_HASH);
        assertNotNull(trytes);
        assertEquals(1, trytes.getTrytes().length, "getTrytes should send back 1 transaction trytes");
    }

    @Disabled
    @Test
    @Tag("IntegrationTest")
    public void shouldBroadcastAndStore() throws ArgumentException {
        List<Transaction> response = iotaAPI.sendTrytes(new String[]{TEST_TRYTES}, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, null);

        BroadcastTransactionsResponse res = iotaAPI.storeAndBroadcast(response.get(0).toTrytes());
        assertNotNull(res, "storeAndBroadcast should not return null on fail");
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldFailBeforeSnapshotTimeStamp() throws ArgumentException {
        assertThrows(ArgumentException.class, () -> {
            iotaAPI.storeAndBroadcast(TEST_TRYTES);
        }, "Transaction did not fail on old timestamp value");
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldSendTrytes(){
        List<Transaction> response = iotaAPI.sendTrytes(new String[]{TEST_TRYTES}, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, null);
        assertEquals(1, response.size(), "Sending 1 transaction received unexpected amount");
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldNotSendTransfer(){
        try {
            List<Transfer> transfers = new ArrayList<>();
            // Adress is spent
            transfers.add(new Transfer(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, 2, TEST_MESSAGE, TEST_TAG));
            iotaAPI.sendTransfer(TEST_SEED1, 2, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, transfers, null, null, false, true, null);
            fail("Transaction did not fail on spent address");
        } catch (ArgumentException e){
            assertEquals(Constants.SENDING_TO_USED_ADDRESS_ERROR, e.getMessage(), "Message should say we try to use a used address");
        }
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldSendTransferWithoutInputs(){
        List<Transfer> transfers = new ArrayList<>();

        AddressRequest addressRequest = new AddressRequest.Builder(TEST_SEED1, 2).checksum(true).build();
        String address = iotaAPI.generateNewAddresses(addressRequest).first();
        transfers.add(new Transfer(address, 1, TEST_MESSAGE, TEST_TAG));

        SendTransferResponse str = iotaAPI.sendTransfer(TEST_SEED1, 2, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, transfers, null, null, false, true, null);
        assertThat("Sending transfer should have returned multiple transactions", str.getTransactions(), IsNull.notNullValue());
        assertThat("Sending transfer should contain success information", str.getSuccessfully(), IsNull.notNullValue());

        assertEquals(0, str.getTransactions().get(0).getCurrentIndex(), "Returned transfers should have normal bundle order");
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldSendTransferWithInputs(){
        List<Input> inputList = new ArrayList<>();
        List<Transfer> transfers = new ArrayList<>();

        GetBalancesAndFormatResponse rsp = iotaAPI.getInputs(TEST_SEED3, 2, 0, 0, 1);

        inputList.addAll(rsp.getInputs());

        AddressRequest addressRequest = new AddressRequest.Builder(TEST_SEED3, 2).checksum(true).build();
        String address = iotaAPI.generateNewAddresses(addressRequest).first();
        transfers.add(new Transfer(address, 1, TEST_MESSAGE, TEST_TAG));

        // validatInputs to true would mean we have to spent all balance in once. Now we double spent but its devnet
        SendTransferResponse str = iotaAPI.sendTransfer(TEST_SEED3, 2, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, transfers, inputList, null, false, true, null);
        assertThat("Sending transfer should have returned multiple transactions", str.getTransactions(), IsNull.notNullValue());
        assertThat("Sending transfer should contain success information", str.getSuccessfully(), IsNull.notNullValue());
    }
}