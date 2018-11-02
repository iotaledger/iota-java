package jota;

import com.google.gson.Gson;
import jota.category.IntegrationTest;
import jota.dto.response.*;
import jota.error.ArgumentException;
import jota.model.Bundle;
import jota.model.Input;
import jota.model.Transaction;
import jota.model.Transfer;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Let's do some integration test coverage against a default local real node.
 *
 */
public class IotaAPITest {

    private static final String TEST_SEED1 = "IHDEENZYITYVYSPKAURUZAQKGVJEREFDJMYTANNXXGPZ9GJWTEOJJ9IPMXOGZNQLSNMFDSQOTZAEETUEA";
    private static final String TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_1 = "MALAZGDVZIAQQRTNYJDSZMY9VE9LAHQKTVCUOAGZUCX9IBUMODFFTMGUIUAXGLWZQ9CYRSLYBM9QBIBYAEIAOPKXEA";
    private static final String TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2 = "LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZCCOZVXMTXC";
    private static final String TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_3 = "ASCZZOBQDMNHLELQKWJBMRETMHBTF9V9TNKYDIFW9PDXPUHPVVGHMSWPVMNJHSJF99QFCMNTPCPGS9DT9XAFKJVO9X";
    private static final String TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_1 = "MALAZGDVZIAQQRTNYJDSZMY9VE9LAHQKTVCUOAGZUCX9IBUMODFFTMGUIUAXGLWZQ9CYRSLYBM9QBIBYA";
    private static final String TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2 = "LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZC";
    private static final String TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_3 = "ASCZZOBQDMNHLELQKWJBMRETMHBTF9V9TNKYDIFW9PDXPUHPVVGHMSWPVMNJHSJF99QFCMNTPCPGS9DT9";
    private static final String TEST_HASH = "AKK9DUFNOAAWCERFSUQTV9EOGFTBTSYYRQHEBQWRMDDBPYOS9UIEMQZEEVEQJZIWHKLRKCUJPOW9AC999";
    private static final String TEST_INVALID_TRYTES = "BYSWEAUTWXHXZ9YBZISEK9LUHWGMHXCGEVNZHRLUWQFCUSDXZHOFHWHL9MQPVJXXZLIXPXPXF9KYEREFSKCPKYIIKPZVLHUTDFQKKVVBBN9ATTLPCNPJDWDEVIYYLGPZGCWXOBDXMLJC9VO9QXTTBLAXTTBFUAROYEGQIVB9MJWJKXJMCUPTWAUGFZBTZCSJVRBGMYXTVBDDS9MYUJCPZ9YDWWQNIPUAIJXXSNLKUBSCOIJPCLEFPOXFJREXQCUVUMKSDOVQGGHRNILCO9GNCLWFM9APMNMWYASHXQAYBEXF9QRIHIBHYEJOYHRQJAOKAQ9AJJFQ9WEIWIJOTZATIBOXQLBMIJU9PCGBLVDDVFP9CFFSXTDUXMEGOOFXWRTLFGV9XXMYWEMGQEEEDBTIJ9OJOXFAPFQXCDAXOUDMLVYRMRLUDBETOLRJQAEDDLNVIRQJUBZBO9CCFDHIX9MSQCWYAXJVWHCUPTRSXJDESISQPRKZAFKFRULCGVRSBLVFOPEYLEE99JD9SEBALQINPDAZHFAB9RNBH9AZWIJOTLBZVIEJIAYGMC9AZGNFWGRSWAXTYSXVROVNKCOQQIWGPNQZKHUNODGYADPYLZZZUQRTJRTODOUKAOITNOMWNGHJBBA99QUMBHRENGBHTH9KHUAOXBVIVDVYYZMSEYSJWIOGGXZVRGN999EEGQMCOYVJQRIRROMPCQBLDYIGQO9AMORPYFSSUGACOJXGAQSPDY9YWRRPESNXXBDQ9OZOXVIOMLGTSWAMKMTDRSPGJKGBXQIVNRJRFRYEZ9VJDLHIKPSKMYC9YEGHFDS9SGVDHRIXBEMLFIINOHVPXIFAZCJKBHVMQZEVWCOSNWQRDYWVAIBLSCBGESJUIBWZECPUCAYAWMTQKRMCHONIPKJYYTEGZCJYCT9ABRWTJLRQXKMWY9GWZMHYZNWPXULNZAPVQLPMYQZCYNEPOCGOHBJUZLZDPIXVHLDMQYJUUBEDXXPXFLNRGIPWBRNQQZJSGSJTTYHIGGFAWJVXWL9THTPWOOHTNQWCNYOYZXALHAZXVMIZE9WMQUDCHDJMIBWKTYH9AC9AFOT9DPCADCV9ZWUTE9QNOMSZPTZDJLJZCJGHXUNBJFUBJWQUEZDMHXGBPTNSPZBR9TGSKVOHMOQSWPGFLSWNESFKSAZY9HHERAXALZCABFYPOVLAHMIHVDBGKUMDXC9WHHTIRYHZVWNXSVQUWCR9M9RAGMFEZZKZ9XEOQGOSLFQCHHOKLDSA9QCMDGCGMRYJZLBVIFOLBIJPROKMHOYTBTJIWUZWJMCTKCJKKTR9LCVYPVJI9AHGI9JOWMIWZAGMLDFJA9WU9QAMEFGABIBEZNNAL9OXSBFLOEHKDGHWFQSHMPLYFCNXAAZYJLMQDEYRGL9QKCEUEJ9LLVUOINVSZZQHCIKPAGMT9CAYIIMTTBCPKWTYHOJIIY9GYNPAJNUJ9BKYYXSV9JSPEXYMCFAIKTGNRSQGUNIYZCRT9FOWENSZQPD9ALUPYYAVICHVYELYFPUYDTWUSWNIYFXPX9MICCCOOZIWRNJIDALWGWRATGLJXNAYTNIZWQ9YTVDBOFZRKO9CFWRPAQQRXTPACOWCPRLYRYSJARRKSQPR9TCFXDVIXLP9XVL99ERRDSOHBFJDJQQGGGCZNDQ9NYCTQJWVZIAELCRBJJFDMCNZU9FIZRPGNURTXOCDSQGXTQHKHUECGWFUUYS9J9NYQ9U9P9UUP9YMZHWWWCIASCFLCMSKTELZWUGCDE9YOKVOVKTAYPHDF9ZCCQAYPJIJNGSHUIHHCOSSOOBUDOKE9CJZGYSSGNCQJVBEFTZFJ9SQUHOASKRRGBSHWKBCBWBTJHOGQ9WOMQFHWJVEG9NYX9KWBTCAIXNXHEBDIOFO9ALYMFGRICLCKKLG9FOBOX9PDWNQRGHBKHGKKRLWTBEQMCWQRLHAVYYZDIIPKVQTHYTWQMTOACXZOQCDTJTBAAUWXSGJF9PNQIJ9AJRUMUVCPWYVYVARKR9RKGOUHHNKNVGGPDDLGKPQNOYHNKAVVKCXWXOQPZNSLATUJT9AUWRMPPSWHSTTYDFAQDXOCYTZHOYYGAIM9CELMZ9AZPWB9MJXGHOKDNNSZVUDAGXTJJSSZCPZVPZBYNNTUQABSXQWZCHDQSLGK9UOHCFKBIBNETK999999999999999999999999999999999999999999999999999999999999999999999999999999999NOXDXXKUDWLOFJLIPQIBRBMGDYCPGDNLQOLQS99EQYKBIU9VHCJVIPFUYCQDNY9APGEVYLCENJIOBLWNB999999999XKBRHUD99C99999999NKZKEKWLDKMJCI9N9XQOLWEPAYWSH9999999999999999999999999KDDTGZLIPBNZKMLTOLOXQVNGLASESDQVPTXALEKRMIOHQLUHD9ELQDBQETS9QFGTYOYWLNTSKKMVJAUXSIROUICDOXKSYZTDPEDKOQENTJOWJONDEWROCEJIEWFWLUAACVSJFTMCHHXJBJRKAAPUDXXVXFWP9X9999IROUICDOXKSYZTDPEDKOQENTJOWJONDEWROCEJIEWFWLUAACVSJFTMCHHXJBJRKAAPUDXXVXFWP9X9999";
    private static final String TEST_TRYTES = "CCWCXCGDEAXCGDEAPCEAHDTCGDHDRAADTCGDGDPCVCTC9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999WITPXKWONPNUPBESJTXQTZTFXFSTDUWVGYHW9VRDULWGBKDVAAJLOSAEPUSCMSOIYLKMIZLPEKAOYAXMWFLDKQJI99999999999999999999UUC9999999999999999FUCK9YOUIVBUGXD99999999999B99999999IDPWGXASJFLLGCDGPQVXYGSNUESCZQCEKVREGLZX9FCYQVUWESEKWSMHZTGMJLGBOLKU9GILFITSJLZBWEHH9RSRNBPSKKUZBBJDSYHYWYHLJUOAFCKMXGTRFTZHKKWSVKGRHHJECSILLLZXKYJAYAQYEOINSZ9999OCYREQNINOB9XMLJOXMDFJDTXYO9PANNXQSW9HPFAAHEPTSPHTNEBOFFNRPKSUQNTKSACROOJPXF99999UUC9999999999999999FUCK9YOUPNMHCEJIE999999999L99999999IGMVBPROUATGVGQTHYIYFVQETRW";
    private static final String TEST_MESSAGE = "JUSTANOTHERIOTATEST";
    private static final String TEST_TAG = "IOTAJAVASPAM999999999999999";
    private static final int MIN_WEIGHT_MAGNITUDE = 14;
    private static final int DEPTH = 9;

    private static final String[] TEST_ADDRESSES = new String[]{"LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZCCOZVXMTXC"
            , "P9UDUZMN9DEXCRQEKLJYSBSBZFCHOBPJSDKMLCCVJDOVOFDWMNBZRIRRZJGINOUMPJBMYYZEGRTIDUABD"
            , "MIMVJEYREIIZLXOXQROMPJFCIX9NFVXD9ZQMNZERPI9OJIJFUWQ9WCTMKXEEPHYPWEZPHLJBJOFH9YTRB"
            , "FOJHXRVJRFMJTFDUWJYYZXCZIJXKQALLXMLKHZFDMHWTIBBXUKSNSUYJLKYRQBNXKRSUXZHDTPWXYD9YF"
            , "B9YNPQO9EXID9RDEEGLCBJBYKBLWHTOQOZKTLJDFPJZOPKJJTNUYUVVTDJPBCBYIWGPSCMNRZFGFHFSXH"
            , "NQEFOAFIYKZOUXDFQ9X9PHCNSDETRTJZINZ9EYGKU99QJLDSTSC9VTBAA9FHLNLNYQXWLTNPRJDWCGIPP"
            , "CEGLBSXDJVXGKGOUHRGMAQDRVYXCQLXBKUDWKFFSIABCUYRATFPTEEDIFYGAASKFZYREHLBIXBTKP9KLC"
            , "QLOXU9GIQXPPE9UUT9DSIDSIESRIXMTGZJMKLSJTNBCRELAVLWVJLUOLKGFCWAEPEQWZWPBV9YZJJEHUS"
            , "XIRMYJSGQXMM9YPHJVVLAVGBBLEEMOOKHHBFWKEAXJFONZLNSLBCGPQEVDMMOGHFVRDSYTETIFOIVNCR9"
            , "PDVVBYBXMHZKADPAYOKQNDPHRSWTHAWQ9GRVIBOIMZQTYCWEPCDWDVRSOUNASVBDLBOAMVLYEVVCMAM9N"
            , "U9GAIAPUUQWJGISAZWPLHUELTZ9WSHWXS9JLPKOWHRRIVUKGWCTJMBULVMKTETTUNHZ9HWHBALUCJIROU"
            , "VFPMKZLLMDUOEKNBEKQZPTNZJZF9UHRWSTHXLWQQ9OAXTZQHTZPAWNJNXKAZFSDFWKFQEKZIGJTLWQFLO"
            , "IGHK9XIWOAYBZUEZHQLEXBPTXSWVANIOUZZCPNKUIJIJOJNAQCJWUJHYKCZOIKVAAHDGAWJZKLTPVQL9G"
            , "LXQPWMNXSUZTEYNC9ZBBFHY9YWCCOVKBNIIOUSVXZJZMJKJFDUWGUVXYCHGKUHEEIDHSGEWFAHVJPRIJT"
            , "AKFDX9PGGQLZUWRMZ9YBDF9CG9TWXCNALCSXSAWHFIMGXCSYCJLSWIQDGGVDRMNEKKECQEYAITGNLNJFQ"
            , "YX9QSPYMSFVOW9UVZRDVOCPYYMUTDHCCPKHMXQSJQJYIXVCHILKW9GBYJTYGLIKBTRQMDCYBMLLNGSSIK"
            , "DSYCJKNG9TAGJHSKZQ9XLKAKNSKJFZIPVEDGJFXRTFGENHZFQGXHWDBNXLLDABDMOYELPG9DIXSNJFWAR"
            , "9ANNACZYLDDPZILLQBQG9YMG9XJUMTAENDFQ9HMSSEFWYOAXPJTUXBFTSAXDJPAO9FKTWBBSCSFMOUR9I"
            , "WDTFFXHBHMFQQVXQLBFJFVVHVIIAVYM9PFAZCHMKET9ESMHIRHSMVDJBZTXPTAFVIASMSXRDCIYVWVQNO"
            , "XCCPS9GMTSUB9DXPVKLTBDHOFX9PJMBYZQYQEXMRQDPGQPLWRGZGXODYJKGVFOHHYUJRCSXAIDGYSAWRB"
            , "KVEBCGMEOPDPRCQBPIEMZTTXYBURGZVNH9PLHKPMM9D9FUKWIGLKZROGNSYIFHULLWQWXCNAW9HKKVIDC"};

    private IotaAPI iotaAPI;

    @Before
    public void createApiClientInstance() {
        iotaAPI = new IotaAPI.Builder().port("80").build();
    }

    @Test
    public void shouldCreateIotaApiProxyInstanceWithDefaultValues() {
        iotaAPI = new IotaAPI.Builder().build();
        assertThat(iotaAPI, IsNull.notNullValue());
        assertThat(iotaAPI.getHost(), Is.is("nodes.testnet.iota.org"));
        assertThat(iotaAPI.getPort(), Is.is("80"));
        assertThat(iotaAPI.getProtocol(), Is.is("http"));
    }

    @Test
    public void shouldRetainValuesFromBuilder() {
        iotaAPI = new IotaAPI.Builder().host("somewhere_over_the_rainbow").build();
        assertThat(iotaAPI.getHost(), Is.is("somewhere_over_the_rainbow"));

        iotaAPI = new IotaAPI.Builder().port("15515").build();
        assertThat(iotaAPI.getPort(), Is.is("15515"));

        iotaAPI = new IotaAPI.Builder().protocol("https").build();
        assertThat(iotaAPI.getProtocol(), Is.is("https"));
    }

    @Test
    public void shouldGetValuesFromProperties() {
        Properties properties = new Properties();

        properties.put("iota.node.host", "somewhere_over_the_rainbow");
        iotaAPI = new IotaAPI.Builder().config(properties).build();
        assertThat(iotaAPI.getHost(), Is.is("somewhere_over_the_rainbow"));

        properties = new Properties();
        properties.put("iota.node.port", "15515");
        iotaAPI = new IotaAPI.Builder().config(properties).build();
        assertThat(iotaAPI.getPort(), Is.is("15515"));

        properties = new Properties();
        properties.put("iota.node.protocol", "https");
        iotaAPI = new IotaAPI.Builder().config(properties).build();
        assertThat(iotaAPI.getProtocol(), Is.is("https"));
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldGetInputs() throws ArgumentException {
        GetBalancesAndFormatResponse res = iotaAPI.getInputs(TEST_SEED1, 2, 0, 0, 0);

        assertThat(res, IsNull.notNullValue());
        assertThat(res.getTotalBalance(), IsNull.notNullValue());
        assertThat(res.getInputs(), IsNull.notNullValue());
    }

    @Test
    public void shouldCreateANewAddressWithChecksum() throws ArgumentException {
        final GetNewAddressResponse res1 = iotaAPI.getAddressesUnchecked(TEST_SEED1, 1, true, 0, 5);
        assertThat(res1.getAddresses().get(0), Is.is(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_1));

        final GetNewAddressResponse res2 = iotaAPI.getAddressesUnchecked(TEST_SEED1, 2, true, 0, 5);
        assertThat(res2.getAddresses().get(0), Is.is(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2));

        final GetNewAddressResponse res3 = iotaAPI.getAddressesUnchecked(TEST_SEED1, 3, true, 0, 5);
        assertThat(res3.getAddresses().get(0), Is.is(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_3));
    }

    @Test
    public void shouldCreateANewAddressWithoutChecksum() throws ArgumentException {
        final GetNewAddressResponse res1 = iotaAPI.getAddressesUnchecked(TEST_SEED1, 1, false, 0, 5);
        assertThat(res1.getAddresses().get(0), Is.is(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_1));

        final GetNewAddressResponse res2 = iotaAPI.getAddressesUnchecked(TEST_SEED1, 2, false, 0, 5);
        assertThat(res2.getAddresses().get(0), Is.is(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2));

        final GetNewAddressResponse res3 = iotaAPI.getAddressesUnchecked(TEST_SEED1, 3, false, 0, 5);
        assertThat(res3.getAddresses().get(0), Is.is(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_3));
    }

    @Test
    public void shouldCreate100Addresses() throws ArgumentException {
        GetNewAddressResponse res = iotaAPI.getAddressesUnchecked(TEST_SEED1, 2, false, 0, 100);
        assertEquals(res.getAddresses().size(), 100);
    }

    //seed contains 0 balance
    @Test(expected = IllegalStateException.class)
    @Category(IntegrationTest.class)
    public void shouldPrepareTransfer() throws ArgumentException {
        List<Transfer> transfers = new ArrayList<>();

        transfers.add(new Transfer(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, 100, TEST_MESSAGE, TEST_TAG));
        List<String> trytes = iotaAPI.prepareTransfers(TEST_SEED1, 2, transfers, null, null, null, false);

        Assert.assertNotNull(trytes);
        assertThat(trytes.isEmpty(), Is.is(false));
    }

    //seed contains 0 balance
    @Test(expected = IllegalStateException.class)
    @Category(IntegrationTest.class)
    public void shouldPrepareTransferWithInputs() throws ArgumentException {
        List<Input> inputlist = new ArrayList<>();
        List<Transfer> transfers = new ArrayList<>();

        GetBalancesAndFormatResponse rsp = iotaAPI.getInputs(TEST_SEED1, 2, 0, 0, 0);

        inputlist.addAll(rsp.getInputs());

        transfers.add(new Transfer(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, 100, TEST_MESSAGE, TEST_TAG));
        List<String> trytes = iotaAPI.prepareTransfers(TEST_SEED1, 2, transfers, null, inputlist, null,true);

        Assert.assertNotNull(trytes);
        assertThat(trytes.isEmpty(), Is.is(false));
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldGetLastInclusionState() throws ArgumentException {
        GetInclusionStateResponse res = iotaAPI.getLatestInclusion(new String[]{TEST_HASH});
        assertThat(res.getStates(), IsNull.notNullValue());
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldFindTransactionObjects() throws ArgumentException {
        List<Transaction> ftr = iotaAPI.findTransactionObjectsByAddresses(TEST_ADDRESSES);
        assertThat(ftr, IsNull.notNullValue());
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldGetAccountData() throws ArgumentException {
        GetAccountDataResponse gad = iotaAPI.getAccountData(TEST_SEED1, 2, 0, true, 0, true, 0, 0, true, 0);
        assertThat(gad, IsNull.notNullValue());
    }

    @Test(expected = ArgumentException.class)
    public void shouldNotGetBundle() throws ArgumentException {
        iotaAPI.getBundle("SADASD");
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldGetBundle() throws ArgumentException {
        GetBundleResponse gbr = iotaAPI.getBundle(TEST_HASH);
        assertThat(gbr, IsNull.notNullValue());
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldCheckConsistency() throws ArgumentException {
        GetNodeInfoResponse gni = iotaAPI.getNodeInfo();
        CheckConsistencyResponse ccr = iotaAPI.checkConsistency(gni.getLatestSolidSubtangleMilestone());
        assertThat(ccr, IsNull.notNullValue());
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldGetTransfers() throws ArgumentException {
        GetTransferResponse gtr = iotaAPI.getTransfers(TEST_SEED1, 2, 0, 0, false);
        assertThat(gtr.getTransfers(), IsNull.notNullValue());

        for (Bundle test : gtr.getTransfers()) {
            for (Transaction trx : test.getTransactions()) {
                System.out.println(new Gson().toJson(trx));
            }
        }
    }

    @Ignore
    @Test
    public void shouldReplayBundle() throws ArgumentException {
        ReplayBundleResponse rbr = iotaAPI.replayBundle(TEST_HASH, DEPTH, MIN_WEIGHT_MAGNITUDE, null);
        assertThat(rbr, IsNull.notNullValue());
    }

    @Ignore
    @Test(expected = ArgumentException.class)
    public void shouldNotSendTrytes() throws ArgumentException {
        iotaAPI.sendTrytes(new String[]{TEST_INVALID_TRYTES}, DEPTH, MIN_WEIGHT_MAGNITUDE, null);
    }

    @Test()
    @Category(IntegrationTest.class)
    public void shouldGetTrytes() throws ArgumentException {
        iotaAPI.getTrytes(TEST_HASH);
    }

    @Test()
    @Category(IntegrationTest.class)
    public void shouldBroadcastAndStore() throws ArgumentException {
        //Manually generate a transaction?
    }
    
    @Test()
    @Category(IntegrationTest.class)
    public void shouldFailBeforeSnapshotTimeStamp() throws ArgumentException {
        try {
            iotaAPI.storeAndBroadcast(TEST_TRYTES);
            fail("Transaction did not fail on old timestamp value");
        } catch (ArgumentException e) {
            //TODO Check for specific error
        }
    }

    @Ignore
    @Test
    public void shouldSendTrytes() throws ArgumentException {
        iotaAPI.sendTrytes(new String[]{TEST_TRYTES}, DEPTH, MIN_WEIGHT_MAGNITUDE, null);
    }

    @Ignore
    @Test(expected = IllegalStateException.class)
    public void shouldNotSendTransfer() throws ArgumentException {
        List<Transfer> transfers = new ArrayList<>();
        transfers.add(new Transfer(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2, 2, TEST_MESSAGE, TEST_TAG));
        SendTransferResponse str = iotaAPI.sendTransfer(TEST_SEED1, 2, DEPTH, MIN_WEIGHT_MAGNITUDE, transfers, null, null, false, true, null);
        assertThat(str.getTransactions(), IsNull.notNullValue());
        assertThat(str.getSuccessfully(), IsNull.notNullValue());
    }

    @Ignore
    @Test
    public void shouldSendTransferWithoutInputs() throws ArgumentException {
        List<Transfer> transfers = new ArrayList<>();
        transfers.add(new Transfer(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2, 1, TEST_MESSAGE, TEST_TAG));
        SendTransferResponse str = iotaAPI.sendTransfer(TEST_SEED1, 2, DEPTH, MIN_WEIGHT_MAGNITUDE, transfers, null, null, false, true, null);
        assertThat(str.getTransactions(), IsNull.notNullValue());
        assertThat(str.getSuccessfully(), IsNull.notNullValue());
    }

    @Ignore
    @Test
    public void shouldSendTransferWithInputs() throws ArgumentException {
        List<Input> inputlist = new ArrayList<>();
        List<Transfer> transfers = new ArrayList<>();

        GetBalancesAndFormatResponse rsp = iotaAPI.getInputs(TEST_SEED1, 2, 0, 0, 1);

        inputlist.addAll(rsp.getInputs());

        transfers.add(new Transfer(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2, 1, TEST_MESSAGE, TEST_TAG));

        SendTransferResponse str = iotaAPI.sendTransfer(TEST_SEED1, 2, DEPTH, MIN_WEIGHT_MAGNITUDE, transfers, inputlist, null, true, true, null);
        assertThat(str.getTransactions(), IsNull.notNullValue());
        assertThat(str.getSuccessfully(), IsNull.notNullValue());
    }
}