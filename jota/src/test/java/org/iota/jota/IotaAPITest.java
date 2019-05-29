package org.iota.jota;

import org.hamcrest.core.IsNull;
import org.iota.jota.category.IntegrationTest;
import org.iota.jota.config.types.FileConfig;
import org.iota.jota.config.types.IotaDefaultConfig;
import org.iota.jota.dto.response.*;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.error.InternalException;
import org.iota.jota.model.Input;
import org.iota.jota.model.Transaction;
import org.iota.jota.model.Transfer;
import org.iota.jota.pow.SpongeFactory;
import org.iota.jota.pow.pearldiver.PearlDiverLocalPoW;
import org.iota.jota.utils.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

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

    @Before
    public void createApiClientInstance() throws Exception {
        iotaAPI = new IotaAPI.Builder().config(new FileConfig()).build();
        assertNotNull("An API should have been created", iotaAPI);
    }

    @Test
    public void shouldCreateIotaApiProxyInstanceWithDefaultValues() {
        iotaAPI = new IotaAPI.Builder().build();
        assertNotNull(iotaAPI);
        assertEquals("Host should have been set to defaults", iotaAPI.getHost(), IotaDefaultConfig.Defaults.LEGACY_HOST);
        assertEquals("Port should have been set to defaults", iotaAPI.getPort(),IotaDefaultConfig.Defaults.LEGACY_PORT + "");
        assertEquals("Protocol should have been set to defaults", iotaAPI.getProtocol(), IotaDefaultConfig.Defaults.LEGACY_PROTOCOL);
    }

    @Test
    public void shouldRetainValuesFromBuilder() {
        iotaAPI = new IotaAPI.Builder().host("iota.org").build();
        assertEquals("Host should have been set to iota.org", iotaAPI.getHost(),"iota.org");

        iotaAPI = new IotaAPI.Builder().port(15515).build();
        assertEquals("Port should have been set to 15515", iotaAPI.getPort(),"15515");

        iotaAPI = new IotaAPI.Builder().protocol("https").build();
        assertEquals("Protocol should have been set to https", iotaAPI.getProtocol(),"https");
    }

    @Test
    public void shouldGetValuesFromProperties() {
        Properties properties = new Properties();

        properties.put("iota.node.host", "somewhere_over_the_rainbow");
        iotaAPI = new IotaAPI.Builder().config(properties).build();
        assertEquals("Host should have been set to somewhere_over_the_rainbow", iotaAPI.getHost(),"somewhere_over_the_rainbow");

        properties = new Properties();
        properties.put("iota.node.port", "15515");
        iotaAPI = new IotaAPI.Builder().config(properties).build();
        assertEquals("Port should have been set to 15515", iotaAPI.getPort(),"15515");

        properties = new Properties();
        properties.put("iota.node.protocol", "https");
        iotaAPI = new IotaAPI.Builder().config(properties).build();
        assertEquals("Protocol should be set to https", iotaAPI.getProtocol(),"https");
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldGetInputs() throws ArgumentException {
        // Address 0 should contain 1000
        GetBalancesAndFormatResponse res = iotaAPI.getInputs(TEST_SEED1, 2, 0, 10, 0);

        assertThat("Error on getInputs should have thrown", res, IsNull.notNullValue());
        assertTrue("Res should have a balance(1000)",res.getTotalBalance() > 0);
        assertThat("Error on getInputs should have thrown", res.getInputs(), IsNull.notNullValue());
    }

    @Test
    public void shouldCreateANewAddressWithChecksum() throws ArgumentException {
        final GetNewAddressResponse res1 = iotaAPI.getAddressesUnchecked(TEST_SEED1, 1, true, 0, 5);
        assertEquals("Generated address should match original", res1.getAddresses().get(0), TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_1);

        final GetNewAddressResponse res2 = iotaAPI.getAddressesUnchecked(TEST_SEED1, 2, true, 0, 5);
        assertEquals("Generated address should match original", res2.getAddresses().get(0), TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2);

        final GetNewAddressResponse res3 = iotaAPI.getAddressesUnchecked(TEST_SEED1, 3, true, 0, 5);
        assertEquals("Generated address should match original", res3.getAddresses().get(0), TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_3);
    }

    @Test
    public void shouldCreateANewAddressWithoutChecksum() throws ArgumentException {
        final GetNewAddressResponse res1 = iotaAPI.getAddressesUnchecked(TEST_SEED1, 1, false, 0, 5);
        assertEquals("Generated address should match original", res1.getAddresses().get(0), TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_1);

        final GetNewAddressResponse res2 = iotaAPI.getAddressesUnchecked(TEST_SEED1, 2, false, 0, 5);
        assertEquals("Generated address should match original", res2.getAddresses().get(0), TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2);

        final GetNewAddressResponse res3 = iotaAPI.getAddressesUnchecked(TEST_SEED1, 3, false, 0, 5);
        assertEquals("Generated address should match original", res3.getAddresses().get(0), TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_3);
    }

    @Test
    public void shouldCreate100Addresses() throws ArgumentException {
        GetNewAddressResponse res = iotaAPI.getAddressesUnchecked(TEST_SEED1, 2, false, 0, 100);
        assertEquals("Should have generated 100 addresses", res.getAddresses().size(), 100);
    }
    
    @Test
    @Category(IntegrationTest.class)
    public void shouldPrepareTransfer() {
        List<Transfer> transfers = new ArrayList<>();

        transfers.add(new Transfer(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, 100, TEST_MESSAGE, TEST_TAG));
        List<String> trytes = iotaAPI.prepareTransfers(TEST_SEED1, 2, transfers, null, null, null, false);

        assertNotNull("prepareTransfers should throw an error on failure", trytes);
        assertFalse("prepareTransfers should throw an error on failure", trytes.isEmpty());

        Transaction first = new Transaction(trytes.get(0));
        assertEquals("prepareTransfers should have reversed bundle order for attachToTangle", first.getLastIndex(), first.getCurrentIndex());
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldPrepareTransferWithInputs(){
        List<Input> inputlist = new ArrayList<>();
        List<Transfer> transfers = new ArrayList<>();

        GetBalancesAndFormatResponse rsp = iotaAPI.getInputs(TEST_SEED1, 2, 0, 10, 0);
        
        inputlist.addAll(rsp.getInputs());

        transfers.add(new Transfer(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, 1, TEST_MESSAGE, TEST_TAG));
        List<String> trytes = iotaAPI.prepareTransfers(TEST_SEED1, 2, transfers, null, inputlist, null,true);

        assertNotNull("prepareTransfers should throw an error on failure", trytes);
        assertFalse("prepareTransfers should throw an error on failure", trytes.isEmpty());

        Transaction first = new Transaction(trytes.get(0));
        assertEquals("prepareTransfers should have reversed bundle order for attachToTangle",first.getLastIndex(), first.getCurrentIndex());
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldFailTransfer() {
        try {
            List<Transfer> transfers = new ArrayList<>();

            transfers.add(new Transfer(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, 100, TEST_MESSAGE, TEST_TAG));
            iotaAPI.prepareTransfers(TEST_SEED2, 2, transfers, null, null, null, false);

            fail("prepareTransfers should have thrown an error due to lack of balance on the seed");
        } catch (IllegalStateException e){
            assertEquals("Message should say that there is not enough balance", Constants.NOT_ENOUGH_BALANCE_ERROR, e.getMessage());
        }
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldFailTransferWithInputs(){
        try {
            List<Input> inputlist = new ArrayList<>();
            List<Transfer> transfers = new ArrayList<>();
            transfers.add(new Transfer(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, 1, TEST_MESSAGE, TEST_TAG));
            iotaAPI.prepareTransfers(TEST_SEED2, 2, transfers, null, inputlist, null, true);

            fail("prepareTransfer should have thrown an error on wrong/lack of inputs");
        } catch (ArgumentException e){
            assertEquals("Message should say that the input is invalid", Constants.INVALID_ADDRESSES_INPUT_ERROR, e.getMessage());
        }
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldGetLastInclusionState(){
        GetInclusionStateResponse res = iotaAPI.getLatestInclusion(new String[]{TEST_HASH});
        assertThat("States should be an array of booleans", res.getStates(), IsNull.notNullValue());
        assertTrue("Hash should have been seen as confirmed", res.getStates()[0]);
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldFindTransactionObjects() {
        List<Transaction> ftr = iotaAPI.findTransactionObjectsByAddresses(TEST_ADDRESSES);
        assertThat("findTransactionObjectsByAddresses should not return null on failure", ftr, IsNull.notNullValue());

        assertTrue("findTransactionObjectsByAddresses should find multiple transactions", !ftr.isEmpty());

    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldGetAccountData(){
        GetAccountDataResponse gad = iotaAPI.getAccountData(TEST_SEED3, 2, 0, true, 0, true, 0, 10, true, 0);
        assertThat("GetAccountDataResponse should not return null on failure", gad, IsNull.notNullValue());
    }

    @Test(expected = ArgumentException.class)
    public void shouldNotGetBundle() throws ArgumentException {
        iotaAPI.getBundle("SADASD");
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldGetBundle(){
        GetBundleResponse gbr = iotaAPI.getBundle(TEST_HASH);
        assertThat("GetBundleResponse should not return null on failure", gbr, IsNull.notNullValue());
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldCheckConsistency(){
        GetNodeInfoResponse gni = iotaAPI.getNodeInfo();
        CheckConsistencyResponse ccr = iotaAPI.checkConsistency(gni.getLatestSolidSubtangleMilestone());
        assertThat("CheckConsistencyResponse should not return null on failure", ccr, IsNull.notNullValue());
        assertTrue("Latest milestone should always be consistent", ccr.getState());
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldGetTransfers(){
        GetTransferResponse gtr = iotaAPI.getTransfers(TEST_SEED3, 2, 0, 0, false);
        assertThat("GetTransfers should return GetTransferResponse object on success", gtr.getTransfers(), IsNull.notNullValue());
        assertTrue("GetTransfers should return more than 0 transfers", gtr.getTransfers().length > 0);
    }

    @Test
    public void shouldReplayBundle(){
        ReplayBundleResponse rbr = iotaAPI.replayBundle(TEST_HASH, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, null);
        assertThat("Bundle should be replayed", rbr, IsNull.notNullValue());
    }

    @Test
    public void shouldNotSendTrytes(){
        try {
            iotaAPI.sendTrytes(new String[]{TEST_INVALID_TRYTES}, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, null);
            fail("Should have thrown an exception on attempting to attach wrong trytes");
        } catch (ArgumentException | InternalException e) {
            // ArgumentException for local PoW
            // InternalException for remote PoW
            //TODO: Validate message/return
        }
    } 

    @Test()
    @Category(IntegrationTest.class)
    public void shouldGetTrytes(){
        GetTrytesResponse res = iotaAPI.getTrytes(TEST_HASH);
        assertEquals("getTrytes should send back 1 transaction trytes", 1, res.getTrytes().length);
    }

    @Test()
    @Category(IntegrationTest.class)
    public void shouldStoreAndBroadcast() throws ArgumentException {
        List<Transaction> response = iotaAPI.sendTrytes(new String[]{TEST_TRYTES}, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, null);

        BroadcastTransactionsResponse res = iotaAPI.storeAndBroadcast(response.get(0).toTrytes());
        assertNotNull("storeAndBroadcast should not return null on fail", res);
    }
    
    @Test()
    @Category(IntegrationTest.class)
    public void shouldFailBeforeSnapshotTimeStamp(){
        try {
            iotaAPI.storeAndBroadcast(TEST_TRYTES);
            fail("Transaction did not fail on old timestamp value");
        } catch (ArgumentException e) {
            assertTrue("Message should say that the input is invalid", e.getMessage().contains("Invalid trytes input"));
        }
    }

    @Test
    public void shouldSendTrytes(){
        List<Transaction> response = iotaAPI.sendTrytes(new String[]{TEST_TRYTES}, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, null);
        assertEquals("Sending 1 transaction received unexpected amount", 1, response.size());
    }

    @Test
    public void shouldNotSendTransfer(){
        try {
            List<Transfer> transfers = new ArrayList<>();
            // Adress is spent
            transfers.add(new Transfer(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, 2, TEST_MESSAGE, TEST_TAG));
            SendTransferResponse str = iotaAPI.sendTransfer(TEST_SEED1, 2, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, transfers, null, null, false, true, null);
            fail("Transaction did not fail on spent address");
        } catch (ArgumentException e){
            assertEquals("Message should say we try to use a used address", Constants.SENDING_TO_USED_ADDRESS_ERROR, e.getMessage());
        }
    }

    @Test
    public void shouldSendTransferWithoutInputs(){
        List<Transfer> transfers = new ArrayList<>();

        String address = iotaAPI.getNextAvailableAddress(TEST_SEED1, 2, true).first();
        transfers.add(new Transfer(address, 1, TEST_MESSAGE, TEST_TAG));

        SendTransferResponse str = iotaAPI.sendTransfer(TEST_SEED1, 2, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, transfers, null, null, false, false, null);
        assertThat("Sending transfer should have returned multiple transactions", str.getTransactions(), IsNull.notNullValue());
        assertThat("Sending transfer should contain success information", str.getSuccessfully(), IsNull.notNullValue());

        assertEquals("Returned transfers should have normal bundle order",0, str.getTransactions().get(0).getCurrentIndex());
    }

    @Test
    public void shouldSendTransferWithInputs(){
        List<Input> inputlist = new ArrayList<>();
        List<Transfer> transfers = new ArrayList<>();

        GetBalancesAndFormatResponse rsp = iotaAPI.getInputs(TEST_SEED3, 2, 0, 0, 1);

        inputlist.addAll(rsp.getInputs());

        String address = iotaAPI.getNextAvailableAddress(TEST_SEED3, 2, true).first();
        transfers.add(new Transfer(address, 1, TEST_MESSAGE, TEST_TAG));

        // validatInputs to true would mean we have to spent all balance in once. Now we double spent but its devnet
        SendTransferResponse str = iotaAPI.sendTransfer(TEST_SEED3, 2, DEPTH, MIN_WEIGHT_MAGNITUDE_DEV, transfers, inputlist, null, false, true, null);
        assertThat("Sending transfer should have returned multiple transactions", str.getTransactions(), IsNull.notNullValue());
        assertThat("Sending transfer should contain success information", str.getSuccessfully(), IsNull.notNullValue());
    }
}