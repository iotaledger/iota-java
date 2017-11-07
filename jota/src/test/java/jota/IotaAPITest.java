package jota;

import com.google.gson.Gson;
import jota.dto.response.*;
import jota.error.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Let's do some integration test coverage against a default local real node.
 *
 * @author davassi
 */
public class IotaAPITest {


    private static final String TEST_SEED1 = "IHDEENZYITYVYSPKAURUZAQKGVJEREFDJMYTANNXXGPZ9GJWTEOJJ9IPMXOGZNQLSNMFDSQOTZAEETUEA";
    private static final String TEST_SEED2 = "IHDEENZYITYVYSPKAURUZAQKGVJEREFDJMYTANNXXGPZ9GJWTEOJJ9IPMXOGZNQLSNMFDSQOTZAEETUEA";
    private static final String TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_1 = "MALAZGDVZIAQQRTNYJDSZMY9VE9LAHQKTVCUOAGZUCX9IBUMODFFTMGUIUAXGLWZQ9CYRSLYBM9QBIBYA";
    private static final String TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2 = "LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZC";
    private static final String TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2_2 = "RXTLHYQWBSJUZQXUS9LMLBE9RLAQFNDWBMZUGYJRJRHYRQQKVXBXJKEZOJDCVKFXM9GXYNMKTESEEILAYFCTLW9DQD";
    private static final String TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_3 = "ASCZZOBQDMNHLELQKWJBMRETMHBTF9V9TNKYDIFW9PDXPUHPVVGHMSWPVMNJHSJF99QFCMNTPCPGS9DT9";
    private static final String TEST_HASH = "EVCNKLXUTEKHSQKYKBSBACP9SCPTCBLLYLB9IDBMNQ9HAYANSUBZOCAAIPSGIMDAYLICTGWJAFFAA9999";
    private static final String TEST_INVALID_TRYTES = "BYSWEAUTWXHXZ9YBZISEK9LUHWGMHXCGEVNZHRLUWQFCUSDXZHOFHWHL9MQPVJXXZLIXPXPXF9KYEREFSKCPKYIIKPZVLHUTDFQKKVVBBN9ATTLPCNPJDWDEVIYYLGPZGCWXOBDXMLJC9VO9QXTTBLAXTTBFUAROYEGQIVB9MJWJKXJMCUPTWAUGFZBTZCSJVRBGMYXTVBDDS9MYUJCPZ9YDWWQNIPUAIJXXSNLKUBSCOIJPCLEFPOXFJREXQCUVUMKSDOVQGGHRNILCO9GNCLWFM9APMNMWYASHXQAYBEXF9QRIHIBHYEJOYHRQJAOKAQ9AJJFQ9WEIWIJOTZATIBOXQLBMIJU9PCGBLVDDVFP9CFFSXTDUXMEGOOFXWRTLFGV9XXMYWEMGQEEEDBTIJ9OJOXFAPFQXCDAXOUDMLVYRMRLUDBETOLRJQAEDDLNVIRQJUBZBO9CCFDHIX9MSQCWYAXJVWHCUPTRSXJDESISQPRKZAFKFRULCGVRSBLVFOPEYLEE99JD9SEBALQINPDAZHFAB9RNBH9AZWIJOTLBZVIEJIAYGMC9AZGNFWGRSWAXTYSXVROVNKCOQQIWGPNQZKHUNODGYADPYLZZZUQRTJRTODOUKAOITNOMWNGHJBBA99QUMBHRENGBHTH9KHUAOXBVIVDVYYZMSEYSJWIOGGXZVRGN999EEGQMCOYVJQRIRROMPCQBLDYIGQO9AMORPYFSSUGACOJXGAQSPDY9YWRRPESNXXBDQ9OZOXVIOMLGTSWAMKMTDRSPGJKGBXQIVNRJRFRYEZ9VJDLHIKPSKMYC9YEGHFDS9SGVDHRIXBEMLFIINOHVPXIFAZCJKBHVMQZEVWCOSNWQRDYWVAIBLSCBGESJUIBWZECPUCAYAWMTQKRMCHONIPKJYYTEGZCJYCT9ABRWTJLRQXKMWY9GWZMHYZNWPXULNZAPVQLPMYQZCYNEPOCGOHBJUZLZDPIXVHLDMQYJUUBEDXXPXFLNRGIPWBRNQQZJSGSJTTYHIGGFAWJVXWL9THTPWOOHTNQWCNYOYZXALHAZXVMIZE9WMQUDCHDJMIBWKTYH9AC9AFOT9DPCADCV9ZWUTE9QNOMSZPTZDJLJZCJGHXUNBJFUBJWQUEZDMHXGBPTNSPZBR9TGSKVOHMOQSWPGFLSWNESFKSAZY9HHERAXALZCABFYPOVLAHMIHVDBGKUMDXC9WHHTIRYHZVWNXSVQUWCR9M9RAGMFEZZKZ9XEOQGOSLFQCHHOKLDSA9QCMDGCGMRYJZLBVIFOLBIJPROKMHOYTBTJIWUZWJMCTKCJKKTR9LCVYPVJI9AHGI9JOWMIWZAGMLDFJA9WU9QAMEFGABIBEZNNAL9OXSBFLOEHKDGHWFQSHMPLYFCNXAAZYJLMQDEYRGL9QKCEUEJ9LLVUOINVSZZQHCIKPAGMT9CAYIIMTTBCPKWTYHOJIIY9GYNPAJNUJ9BKYYXSV9JSPEXYMCFAIKTGNRSQGUNIYZCRT9FOWENSZQPD9ALUPYYAVICHVYELYFPUYDTWUSWNIYFXPX9MICCCOOZIWRNJIDALWGWRATGLJXNAYTNIZWQ9YTVDBOFZRKO9CFWRPAQQRXTPACOWCPRLYRYSJARRKSQPR9TCFXDVIXLP9XVL99ERRDSOHBFJDJQQGGGCZNDQ9NYCTQJWVZIAELCRBJJFDMCNZU9FIZRPGNURTXOCDSQGXTQHKHUECGWFUUYS9J9NYQ9U9P9UUP9YMZHWWWCIASCFLCMSKTELZWUGCDE9YOKVOVKTAYPHDF9ZCCQAYPJIJNGSHUIHHCOSSOOBUDOKE9CJZGYSSGNCQJVBEFTZFJ9SQUHOASKRRGBSHWKBCBWBTJHOGQ9WOMQFHWJVEG9NYX9KWBTCAIXNXHEBDIOFO9ALYMFGRICLCKKLG9FOBOX9PDWNQRGHBKHGKKRLWTBEQMCWQRLHAVYYZDIIPKVQTHYTWQMTOACXZOQCDTJTBAAUWXSGJF9PNQIJ9AJRUMUVCPWYVYVARKR9RKGOUHHNKNVGGPDDLGKPQNOYHNKAVVKCXWXOQPZNSLATUJT9AUWRMPPSWHSTTYDFAQDXOCYTZHOYYGAIM9CELMZ9AZPWB9MJXGHOKDNNSZVUDAGXTJJSSZCPZVPZBYNNTUQABSXQWZCHDQSLGK9UOHCFKBIBNETK999999999999999999999999999999999999999999999999999999999999999999999999999999999NOXDXXKUDWLOFJLIPQIBRBMGDYCPGDNLQOLQS99EQYKBIU9VHCJVIPFUYCQDNY9APGEVYLCENJIOBLWNB999999999XKBRHUD99C99999999NKZKEKWLDKMJCI9N9XQOLWEPAYWSH9999999999999999999999999KDDTGZLIPBNZKMLTOLOXQVNGLASESDQVPTXALEKRMIOHQLUHD9ELQDBQETS9QFGTYOYWLNTSKKMVJAUXSIROUICDOXKSYZTDPEDKOQENTJOWJONDEWROCEJIEWFWLUAACVSJFTMCHHXJBJRKAAPUDXXVXFWP9X9999IROUICDOXKSYZTDPEDKOQENTJOWJONDEWROCEJIEWFWLUAACVSJFTMCHHXJBJRKAAPUDXXVXFWP9X9999";
    private static final String TEST_TRYTES = "9RYSLXACOTYQZAQIAMCULCVKMBIPMKINPFJCZFIZOOZMTCTKIXQHQTUTVGZMVHGKLRZGWOT9UGYQFNCGNRJWDETRKWUFIAFA9M9ZPZKVJWKOJENHFWMFBDVOLEVCGQTKCTOPNGRYQURVQPFJAZFUKCXAOORIYCVDKDINDXRFAFRSFVRXCTPETGIJULBYGXUHQMBGHAKGJLMNZUQE9EVOGTFVJ9EVSDNUYPGQLZBAJNWG9XXXDHETVT9XBBCQZEWJXFLVHEXHEQARCOSRKQQVEQKK9APNBRBJHHUWRGAWUIMLY9AFCULX9ITRJBYU9BNQPZBJKJDNHDAKIGDMNUOFCWAEDSBDRUTORPZQCHCSQRVIERZNGPYBPEOQXGFAKANGXPE9MA9ZZCLKDQANOKIAV9QDDUZILVMEIHXDSHDISEJMLQGVEUJWMCKCMIQTRRGJKJFSVMZWYNQJCFCPOEGOTJULGPFTM9GQGEONOXSYXYPQZHVFYHKYREHQMYWHYUTZE999HRWIQTOENSRFPB9NXDVFSUA9NLAKOELYAAHTFDDAYBHPKNAXXTJIPYOKXTEMYUIOKXORYUFEIIAZCFGCCINBPTXFNTGPKFOQNGEODMSCVOZKRJYLECZB9VDZJCLPMTY9UPLTLH9GVDBALFNBLQXI9ADJJXCSPQJXHNNZUOJCMFJKZZ9ZTJRQMNLDSTZYTFNXQPZKMQJLZBHF9URFHNX9MQVMTIHEMROFCCPDLUOTRCAURYL9IJ9IGTRFVIJXSFLYBJOQOASBNVRFQGVIGKA9UJUDHWUSBFFKDHDVRGLOZQYADXGS9IOGCPCVCFPNUDFTUKXCOEEUMNDZCZGUXIECIYCKL9XCPEIQMYV9VCP9JJMSREJFQB9IVXUAXWCURUADEPBIVIGERXIMFZAGQPRC9KSYYLHBZVZGQJVTZVBGMRBWYJCYRHMJWPIKJYAYTALAAZROZYCMQKWBSFUGHFSOGGTCQDHWJU9VJGCAHJLHSORFKVJKV9RSZHLJZTDJJGOXTRLKNJORWYPW9UKRZROJONZQWJNUEPBZZJYBJJNCAQOEYTHFWVUBACCFTREABOZKQEKSMDAFECVGMGELNZUFZJHWHNXLBOSHLNBPGSQDVLZLBUXWINABWYBDMZNIYNVPYLRKUULTMNNKZUFNQOQKROJSXWYTBRDSJQKTOUXLMXVCVIXEYPZWBSMEXMBGUIVACRTGKDEIYZKP9KQCQXPWRXNLGQOATRHCXJFQINXFYQIDTPUJVXKUYVRYHWDHWSNLWUFPNNJZVNMRFWPZBJCRRSHMHUG9NKKH9SOXTUJUAXBF9MHYWHJ9ZTJRUQFKRLHMNVPWX9XFXLMVJAGASMWMIFYUZFAUCEIOOYMEYWOIZTNEWFVZKOQFECWEPSMOYFSJJKEJQMPSXGE9WTYRQJVMHUQZFD9MJUFFCNSGAZCTXYPIJFNSXAUCYPGZMNWMQWSWCKAQYKXJTWINSGPPZG9HLDLEAWUWEVCTVRCBDFOXKUROXH9HXXAXVPEJFRSLOGRVGYZASTEBAQNXJJROCYRTDPYFUIQJVDHAKEG9YACV9HCPJUEUKOYFNWDXCCJBIFQKYOXGRDHVTHEQUMHO999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999DNVEZXD99A99999999A99999999BULMKQXROXEBHPBYGKQINYJTHPTL9OGKUFBFAVRHPZSNXBUDMNRJFYIIGWOFVCXSVFQRZMPGCZMZUNKCDDRWDLKTKCZXZFMIZ9CZBWNOJGSXHBKEXCFQPBNFAE9QAVRX9SWGEZQRSYAYNW9TPWVWCJXAUPQHYZ9999NBDPASDXMXDCHLMZJIBHAZREUBOTZZAYGKCWTBSBKKETULAPABDTY9AMPBSXO9KJOLCWJMJSZFKU999999999999999999999999999999999999999999999999999999999999CWJQRPBIJGTWQTCMDUVOCBUAMH";
    private static final String TEST_MESSAGE = "JUSTANOTHERJOTATEST";
    private static final String TEST_TAG = "JOTASPAM9999999999999999999";
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

    private IotaAPI iotaClient;

    @Before
    public void createApiClientInstance() {
        iotaClient = new IotaAPI.Builder().build();
    }

    @Test
    public void shouldCreateIotaApiProxyInstanceWithDefaultValues() {
        IotaAPI proxy = new IotaAPI.Builder().build();
        assertThat(proxy, IsNull.notNullValue());
        assertThat(proxy.getHost(), Is.is("localhost"));
        assertThat(proxy.getPort(), Is.is("14265"));
        assertThat(proxy.getProtocol(), Is.is("http"));
    }

    @Test
    public void shouldRetainValuesFromBuilder() {
        IotaAPI proxy = new IotaAPI.Builder().host("somewhere_over_the_rainbow").build();
        assertThat(proxy.getHost(), Is.is("somewhere_over_the_rainbow"));

        proxy = new IotaAPI.Builder().port("15515").build();
        assertThat(proxy.getPort(), Is.is("15515"));

        proxy = new IotaAPI.Builder().protocol("https").build();
        assertThat(proxy.getProtocol(), Is.is("https"));
    }

    @Test
    public void shouldGetValuesFromProperties() {
        Properties properties = new Properties();
        properties.put("iota.node.host", "somewhere_over_the_rainbow");
        IotaAPI proxy = new IotaAPI.Builder().config(properties).build();
        assertThat(proxy.getHost(), Is.is("somewhere_over_the_rainbow"));

        properties = new Properties();
        properties.put("iota.node.port", "15515");
        proxy = new IotaAPI.Builder().config(properties).build();
        assertThat(proxy.getPort(), Is.is("15515"));

        properties = new Properties();
        properties.put("iota.node.protocol", "https");
        proxy = new IotaAPI.Builder().config(properties).build();
        assertThat(proxy.getProtocol(), Is.is("https"));
    }

    @Test
    public void shouldGetInputs() throws InvalidSecurityLevelException, InvalidAddressException {
        GetBalancesAndFormatResponse res = iotaClient.getInputs(TEST_SEED1, 2, 0, 0, 0);
        System.out.println(res);
        assertThat(res, IsNull.notNullValue());
        assertThat(res.getTotalBalance(), IsNull.notNullValue());
        assertThat(res.getInput(), IsNull.notNullValue());

    }

    @Test
    public void shouldCreateANewAddressWithoutChecksum() throws InvalidSecurityLevelException, InvalidAddressException {
        final GetNewAddressResponse res1 = iotaClient.getNewAddress(TEST_SEED1, 1, 0, false, 5, false);
        assertThat(res1.getAddresses().get(0), Is.is(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_1));

        final GetNewAddressResponse res2 = iotaClient.getNewAddress(TEST_SEED1, 2, 0, false, 5, false);
        assertThat(res2.getAddresses().get(0), Is.is(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2));

        final GetNewAddressResponse res3 = iotaClient.getNewAddress(TEST_SEED1, 3, 0, false, 5, false);
        assertThat(res3.getAddresses().get(0), Is.is(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_3));
    }

    @Test
    public void shouldCreate100Addresses() throws InvalidSecurityLevelException, InvalidAddressException {
        GetNewAddressResponse res = iotaClient.getNewAddress(TEST_SEED1, 2, 0, false, 100, false);
        assertEquals(res.getAddresses().size(), 100);
    }

    @Test
    public void shouldPrepareTransfer() throws InvalidSecurityLevelException, NotEnoughBalanceException, InvalidAddressException, InvalidTransferException {
        List<Input> inputlist = new ArrayList<>();
        List<Transfer> transfers = new ArrayList<>();

        GetBalancesAndFormatResponse rsp = iotaClient.getInputs(TEST_SEED1, 2, 0, 0, 100);

        for (Input input : rsp.getInput()) {
            inputlist.add(input);
        }
        transfers.add(new jota.model.Transfer(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2, 0, TEST_MESSAGE, TEST_TAG));
        transfers.add(new jota.model.Transfer(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2, 0, TEST_MESSAGE, TEST_TAG));
        List<String> trytes1 = iotaClient.prepareTransfers(TEST_SEED1, 2, transfers, null, null, false);
        List<String> trytes2 = iotaClient.prepareTransfers(TEST_SEED1, 2, transfers, null, inputlist, true);

        Assert.assertNotNull(trytes1);
        assertThat(trytes1.isEmpty(), Is.is(false));
        Assert.assertNotNull(trytes2);
        assertThat(trytes2.isEmpty(), Is.is(false));
    }

    @Test
    public void shouldGetLastInclusionState() throws NoNodeInfoException {
        GetInclusionStateResponse res = iotaClient.getLatestInclusion(new String[]{TEST_HASH});
        assertThat(res.getStates(), IsNull.notNullValue());
    }

    @Test
    public void shouldFindTransactionObjects() {
        List<Transaction> ftr = iotaClient.findTransactionObjects(TEST_ADDRESSES);
        System.out.println(ftr);
        assertThat(ftr, IsNull.notNullValue());
    }

    @Test
    public void shouldGetAccountData() throws NoInclusionStatesException, InvalidTrytesException, NoNodeInfoException, ArgumentException, InvalidBundleException, InvalidSecurityLevelException, InvalidAddressException, InvalidSignatureException {
        GetAccountDataResponse gad = iotaClient.getAccountData(TEST_SEED1, 2, 0, true, 0, true, 0, 0, true, 0);
        assertThat(gad, IsNull.notNullValue());
    }

    @Test(expected = IllegalAccessError.class)
    public void shouldNotGetBundle() throws InvalidBundleException, ArgumentException, InvalidSignatureException {
        GetBundleResponse gbr = iotaClient.getBundle("SADASD");
        assertThat(gbr, IsNull.notNullValue());
    }

    @Test
    public void shouldGetBundle() throws InvalidBundleException, ArgumentException, InvalidSignatureException {
        GetBundleResponse gbr = iotaClient.getBundle(TEST_HASH);
        System.out.println(gbr);
        assertThat(gbr, IsNull.notNullValue());
    }

    @Test
    public void shouldGetTransfers() throws InvalidBundleException, ArgumentException, InvalidSignatureException, NoInclusionStatesException, NoNodeInfoException, InvalidSecurityLevelException, InvalidAddressException {
        GetTransferResponse gtr = iotaClient.getTransfers(TEST_SEED1, 2, 0, 0, false);
        assertThat(gtr.getTransfers(), IsNull.notNullValue());
        System.out.println(gtr);

        for (Bundle test : gtr.getTransfers()) {
            for (Transaction trx : test.getTransactions()) {
                System.out.println(new Gson().toJson(trx));
            }
        }
    }

    @Ignore
    @Test
    public void shouldReplayBundle() throws InvalidTrytesException, InvalidBundleException, InvalidSignatureException, ArgumentException {
        ReplayBundleResponse rbr = iotaClient.replayBundle(TEST_HASH, DEPTH, MIN_WEIGHT_MAGNITUDE);
        assertThat(rbr, IsNull.notNullValue());
    }

    @Ignore
    @Test(expected = InvalidTrytesException.class)
    public void shouldNotSendTrytes() throws InvalidTrytesException {
        iotaClient.sendTrytes(new String[]{TEST_INVALID_TRYTES}, DEPTH, MIN_WEIGHT_MAGNITUDE);
    }

    @Test()
    public void shouldGetTrytes() throws InvalidTrytesException {
        System.out.println(iotaClient.getTrytes(TEST_HASH));
    }


    @Ignore
    @Test
    public void shouldSendTrytes() throws InvalidTrytesException {
        iotaClient.sendTrytes(new String[]{TEST_TRYTES}, DEPTH, MIN_WEIGHT_MAGNITUDE);
    }

    @Ignore
    @Test(expected = IllegalStateException.class)
    public void shouldNotSendTransfer() throws ArgumentException, InvalidSignatureException, InvalidBundleException, NotEnoughBalanceException, InvalidSecurityLevelException, InvalidTrytesException, InvalidAddressException, InvalidTransferException {
        List<Transfer> transfers = new ArrayList<>();
        transfers.add(new jota.model.Transfer(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2, 2, TEST_MESSAGE, TEST_TAG));
        SendTransferResponse str = iotaClient.sendTransfer(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2, 2, DEPTH, MIN_WEIGHT_MAGNITUDE, transfers, null, null, false);
        assertThat(str.getSuccessfully(), IsNull.notNullValue());
    }

    @Ignore
    @Test
    public void shouldSendTransferWithoutInputs() throws ArgumentException, InvalidSignatureException, InvalidBundleException, NotEnoughBalanceException, InvalidSecurityLevelException, InvalidTrytesException, InvalidAddressException, InvalidTransferException {
        List<Transfer> transfers = new ArrayList<>();
        transfers.add(new jota.model.Transfer(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2_2, 100, TEST_MESSAGE, TEST_TAG));
        SendTransferResponse str = iotaClient.sendTransfer(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2, 2, DEPTH, MIN_WEIGHT_MAGNITUDE, transfers, null, null, false);
        assertThat(str.getSuccessfully(), IsNull.notNullValue());
    }

    @Ignore
    @Test
    public void shouldSendTransferWithInputs() throws ArgumentException, InvalidSignatureException, InvalidBundleException, NotEnoughBalanceException, InvalidSecurityLevelException, InvalidTrytesException, InvalidAddressException, InvalidTransferException {
        List<Input> inputlist = new ArrayList<>();
        List<Transfer> transfers = new ArrayList<>();

        GetBalancesAndFormatResponse rsp = iotaClient.getInputs(TEST_SEED1, 2, 0, 0, 100);

        for (Input input : rsp.getInput()) {
            inputlist.add(input);
        }

        transfers.add(new jota.model.Transfer(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2_2, 100, TEST_MESSAGE, TEST_TAG));

        SendTransferResponse str = iotaClient.sendTransfer(TEST_SEED1, 2, DEPTH, MIN_WEIGHT_MAGNITUDE, transfers, inputlist, null, true);
        assertThat(str.getSuccessfully(), IsNull.notNullValue());
    }
}