package jota.utils;

import jota.model.Transaction;
import jota.pow.ICurl;
import jota.pow.JCurl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created by Adrian on 15.01.2017.
 */
public class TransactionConverter {
    private static final Logger log = LoggerFactory.getLogger(TransactionConverter.class);
    private ICurl customCurl;

    public TransactionConverter(ICurl curl) {
        customCurl = curl;
    }

    public TransactionConverter() {
        customCurl = null;
    }

    public Transaction transactionObject(final String trytes) {

        if (StringUtils.isEmpty(trytes)) {
            log.warn("Warning: empty trytes in input for transactionObject");
            return null;
        }

        // validity check
        for (int i = 2279; i < 2295; i++) {
            if (trytes.charAt(i) != '9') {
                log.warn("Trytes {} does not seem a valid tryte", trytes);
                return null;
            }
        }

        int[] transactionTrits = Converter.trits(trytes);
        int[] hash = new int[243];

        final JCurl curl = new JCurl(); // we need a fluent JCurl.

        // generate the correct transaction hash
        curl.reset();
        curl.absorbb(transactionTrits, 0, transactionTrits.length);
        curl.squeezee(hash, 0, hash.length);

        Transaction trx = new Transaction();

        trx.setHash(Converter.trytes(hash));
        trx.setSignatureFragments(trytes.substring(0, 2187));
        trx.setAddress(trytes.substring(2187, 2268));
        trx.setValue("" + Converter.longValue(Arrays.copyOfRange(transactionTrits, 6804, 6837)));
        trx.setTag(trytes.substring(2295, 2322));
        trx.setTimestamp("" + Converter.longValue(Arrays.copyOfRange(transactionTrits, 6966, 6993)));
        trx.setCurrentIndex("" + Converter.longValue(Arrays.copyOfRange(transactionTrits, 6993, 7020)));
        trx.setLastIndex("" + Converter.longValue(Arrays.copyOfRange(transactionTrits, 7020, 7047)));
        trx.setBundle(trytes.substring(2349, 2430));
        trx.setTrunkTransaction(trytes.substring(2430, 2511));
        trx.setBranchTransaction(trytes.substring(2511, 2592));
        trx.setNonce(trytes.substring(2592, 2673));

        return trx;
    }
}
