package cfb.pearldiver;

import jota.IotaLocalPoW;
import jota.model.Transaction;
import jota.pow.ICurl;
import jota.pow.SpongeFactory;
import jota.utils.Converter;

import java.util.Arrays;

/**
 * Perform local PoW using Come-from-Beyond's PearlDiver implementation.
 */
public class PearlDiverLocalPoW implements IotaLocalPoW {

    PearlDiver pearlDiver = new PearlDiver();

    /**
     * Initializes a new instance of the Signature class.
     */
    public static void setTransactionProperties(Transaction txn, final String trytes) {
        int[] transactionTrits = Converter.trits(trytes);
        int[] hash = new int[243];

        ICurl curl = SpongeFactory.create(SpongeFactory.Mode.CURL);
        // generate the correct transaction hash
        curl.reset();
        curl.absorb(transactionTrits, 0, transactionTrits.length);
        curl.squeeze(hash, 0, hash.length);

        txn.setHash(Converter.trytes(hash));
        txn.setSignatureFragments(trytes.substring(0, 2187));
        txn.setAddress(trytes.substring(2187, 2268));
        txn.setValue(Converter.longValue(Arrays.copyOfRange(transactionTrits, 6804, 6837)));
        txn.setTag(trytes.substring(2295, 2322));
        txn.setTimestamp(Converter.longValue(Arrays.copyOfRange(transactionTrits, 6966, 6993)));
        txn.setCurrentIndex(Converter.longValue(Arrays.copyOfRange(transactionTrits, 6993, 7020)));
        txn.setLastIndex(Converter.longValue(Arrays.copyOfRange(transactionTrits, 7020, 7047)));
        txn.setBundle(trytes.substring(2349, 2430));
        txn.setTrunkTransaction(trytes.substring(2430, 2511));
        txn.setBranchTransaction(trytes.substring(2511, 2592));
        txn.setNonce(trytes.substring(2592, 2673));
    }
    @Override
    public String performPoW(Transaction transaction, int minWeightMagnitude) {
        int[] trits = Converter.trits(transaction.toTrytes());
        if (!pearlDiver.search(trits, minWeightMagnitude, 0))
            throw new IllegalStateException("PearlDiver search failed");
        setTransactionProperties(transaction, Converter.trytes(trits));
        return Converter.trytes(trits);
    }
    @Override
    public String performPoW(Transaction transaction) {
        int[] trits = Converter.trits(transaction.toTrytes());
        if (!pearlDiver.search(trits, 15, 0))
            throw new IllegalStateException("PearlDiver search failed");
        setTransactionProperties(transaction, Converter.trytes(trits));
        return Converter.trytes(trits);
    }
}
