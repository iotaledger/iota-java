package org.iota.jota;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.iota.jota.utils.Constants.INVALID_TAG_INPUT_ERROR;
import static org.iota.jota.utils.Constants.INVALID_ADDRESS_INPUT_ERROR;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.iota.jota.account.AccountState;
import org.iota.jota.account.AccountStateManager;
import org.iota.jota.account.condition.ExpireCondition;
import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.account.errors.AccountLoadError;
import org.iota.jota.account.errors.AddressGenerationError;
import org.iota.jota.account.event.EventManager;
import org.iota.jota.account.event.EventTaskService;
import org.iota.jota.account.event.events.SendTransferEvent;
import org.iota.jota.account.event.impl.EventManagerImpl;
import org.iota.jota.account.promoter.PromoterReattacherImpl;
import org.iota.jota.account.services.AddressGeneratorService;
import org.iota.jota.account.transferchecker.IncomingTransferCheckerImpl;
import org.iota.jota.account.transferchecker.OutgoingTransferCheckerImpl;
import org.iota.jota.config.AccountConfig;
import org.iota.jota.config.FileConfig;
import org.iota.jota.config.options.AccountBuilderSettings;
import org.iota.jota.config.options.AccountOptions;
import org.iota.jota.dto.response.SendTransferResponse;
import org.iota.jota.store.PersistenceAdapter;
import org.iota.jota.types.Trytes;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Transaction;
import org.iota.jota.model.Transfer;
import org.iota.jota.utils.AbstractBuilder;
import org.iota.jota.utils.Checksum;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.InputValidator;
import org.iota.jota.utils.TrytesConverter;


public class IotaAccount {
    
    private static final Logger log = LoggerFactory.getLogger(IotaAccount.class);
    
    private AccountOptions options;
    
    private EventManager eventManager;
    
    List<EventTaskService> tasks = new ArrayList<>();

    private AccountStateManager accountManager;
    
    boolean loaded = false;
    
    /**
     * 
     * @param builder
     */
    protected IotaAccount(AccountOptions options) {
        this.options = options;
        this.eventManager = new EventManagerImpl();
        
        if (load()) {
            loaded = true;
        } else {
            // Loading went wrong! Log...
        }
    }
    
    protected IotaAccount(Builder builder) {
        this(new AccountOptions(builder));
    }
    
    /**
     * Constructs a IotaAccount with a config based on environment variables or default values.
     * If no environment variable is defined, will use {@value org.iota.jota.config.FileConfig#DEFAULT_CONFIG_NAME}
     * The default storage will be at {@value jota.config.IotaFileStore#DEFAULT_STORE}
     * 
     * @param seed
     * @throws Exception If the config did not load for whatever reason
     */
    public IotaAccount(String seed) throws Exception {
        this(new Builder(seed).generate());
    }
    
    /**
     * Constructs a IotaAccount with a config based on environment variables or default values.
     * If no environment variable is defined, will use {@value org.iota.jota.config.FileConfig#DEFAULT_CONFIG_NAME}
     * 
     * @param seed
     * @param store The method we use for storing key/value data
     * @throws Exception If the config did not load for whatever reason
     */
    public IotaAccount(String seed, PersistenceAdapter store) throws Exception {
        this(new Builder(seed).store(store).generate());
    }
    
    /**
     * Constructs a IotaAccount with a config from String
     * 
     * @param seed
     * @param store The method we use for storing key/value data
     * @param config The location of the config
     * @throws Exception If the config did not load for whatever reason
     */
    public IotaAccount(String seed, PersistenceAdapter store, String config) throws Exception {
        this(new Builder(seed).store(store).config(new FileConfig(config)).generate());
    }

    /**
     * Constructs a IotaAccount with config
     * 
     * @param seed
     * @param store The method we use for storing key/value data
     * @param iotaConfig The config we load nodes from
     * @throws Exception If the config did not load for whatever reason
     */
    public IotaAccount(String seed, PersistenceAdapter store, AccountConfig iotaConfig) throws Exception {
        this(new Builder(seed).store(store).config(iotaConfig).generate());
    }
    
    private boolean load() {
        return load(new AccountState());
    }
    
    private boolean load(AccountState state) {
        try {
            AddressGeneratorService service = new AddressGeneratorService(options);
            state.load(service, getStore());
            accountManager = new AccountStateManager(state, service, options, getStore());
            
            addTask(new PromoterReattacherImpl(eventManager, getApi()));
            addTask(new IncomingTransferCheckerImpl(eventManager, getApi()));
            addTask(new OutgoingTransferCheckerImpl(eventManager, getApi()));
            
            shutdownHook();
            return true;
        } catch (AccountLoadError e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    private void shutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down IOTA Accounts, please hold tight...");
            try {
                unload();
            } catch (Exception e) {
                log.error("Exception occurred shutting down accounts module: ", e);
            }
        }, "Shutdown Hook"));
    }
    
    /**
     * Unloads all registered tasks. 
     * Any tasks added during this method execution are ignored and cleared in the end.
     */
    private void unload() {
        for (EventTaskService task : tasks.toArray(new EventTaskService[tasks.size()])) {
            getEventManager().unRegisterListener(task);
            task.shutdown();
        }
        tasks.clear();
    }

    private void addTask(EventTaskService task) {
        if (task != null) {
            task.load();
            getEventManager().registerListener(task);
            tasks.add(task);
        }
    }
    
    public Bundle send(String address, int amount, int securityLevel, String message, String tag) throws ArgumentException{
        if (!loaded) {
            return null;
        }
        
        int secLvl = securityLevel == 0 ? options.getSecurityLevel() : securityLevel;
        String tryteMsg = TrytesConverter.asciiToTrytes(message);
        
        if (tag != null && !InputValidator.isTag(tag)) {
            throw new ArgumentException(INVALID_TAG_INPUT_ERROR);
        }
        
        StringUtils.rightPad(tag, Constants.MESSAGE_LENGTH, '9');
        
        Transfer transfer = new Transfer(address, 0, tryteMsg, tag);
        List<Transfer> transfers = new LinkedList<>();
        transfers.add(transfer);
        
        try {
            SendTransferResponse transferResponse = getApi().sendTransfer(options.getSeed(), secLvl, 
                    options.getDept(), options.getMwm(), 
                    transfers, null, null, false, false, null);
            
            Bundle bundle = new Bundle(transferResponse.getTransactions(), transferResponse.getTransactions().size());
            SendTransferEvent event = new SendTransferEvent(bundle);
            eventManager.emit(event);
            return bundle;
        } catch (ArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 
     * @param message the optional message to use, can be null
     * @param tag the optional tag to use, can be null
     * @param address the optional address to use, can be null
     * @return the sent bundle
     * @throws ArgumentException If an argument is wrong
     * @throws SendException If an internal error happened whilst sending
     */
    public Bundle sendZeroValue(String message, String tag, String address) throws ArgumentException, SendException {
        return sendZeroValue(Optional.ofNullable(message), Optional.ofNullable(tag), Optional.ofNullable(address));
    }
    
    /**
     * 
     * @param message the optional message to use
     * @param tag the optional tag to use
     * @return the sent bundle
     * @throws ArgumentException If an argument is wrong
     * @throws SendException If an internal error happened whilst sending
     */
    public Bundle sendZeroValue(Optional<String> message, Optional<String> tag, Optional<String> address) throws ArgumentException, SendException {
        if (!loaded) {
            return null;
        }
        
        if (tag.isPresent() && !InputValidator.isTag(tag.get())) {
            throw new ArgumentException(INVALID_TAG_INPUT_ERROR);
        }
        
        String addressHash = Bundle.EMPTY_HASH;
        // remove the checksum of the address if provided
        if (address.isPresent()) {
            if (InputValidator.isAddress(address.get())) {
                addressHash = Checksum.removeChecksum(address.get());
            } else {
                throw new ArgumentException(INVALID_ADDRESS_INPUT_ERROR);
            }
        }

        String tryteTag = tag.orElse("");
        tryteTag = StringUtils.rightPad(tryteTag, Constants.TAG_LENGTH, '9');
        
        String tryteMessage = message.orElse("");
        String tryteMsg = TrytesConverter.asciiToTrytes( tryteMessage);
        
        Transfer transfer = new Transfer(addressHash, 0, tryteMsg, tryteTag);
        
        List<Transfer> transfers = new LinkedList<>();
        transfers.add(transfer);
        
        try {
            List<String> trytes = prepareTransfers(transfers);
            List<Transaction> transferResponse = getApi().sendTrytes(
                    trytes.toArray(new String[trytes.size()]), options.getDept(), options.getMwm(), null);
            
            Bundle bundle = new Bundle(transferResponse, transferResponse.size());
            SendTransferEvent event = new SendTransferEvent(bundle);
            eventManager.emit(event);
            return bundle;
        } catch (ArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Future<Bundle> sendMulti(String[] addresses, int[] amounts, int securityLevel, String[] messages, String tag) {
        if (!loaded) {
            return null;
        }
        
        return null;
    }
    
    public Future<DepositRequest> requestDeposit(String depositAddress, int amount, Date timeOut, ExpireCondition... otherConditions){
        if (!loaded) {
            return null;
        }
        
        return null;
    }
    
    private List<String> prepareTransfers(List<Transfer> transfers){
        List<String> bundleTrytes = new LinkedList<>();
        
        //If there are a lot of transfers, async and add atomic is still faster
        AtomicLong totalValue = new AtomicLong(0);
        transfers.stream().forEach(transfer -> totalValue.addAndGet(transfer.getValue()));
        
        Bundle bundle = new Bundle();
        List<String> signatureFragments = prepareBundle(bundle, transfers);
        
        if (totalValue.longValue() == 0) {
            //Zero value!  simply finalize the bundle
            bundle.finalize(getApi().getCurl());
            bundle.addTrytes(signatureFragments);

            List<Transaction> trxb = bundle.getTransactions();

            for (Transaction trx : trxb) {
                bundleTrytes.add(trx.toTrytes());
            }
            Collections.reverse(bundleTrytes);
            return bundleTrytes;
        } else  {
            // Oh oh! Were sending value!
        }
        
        return null;
    }
    
    private List<String> prepareBundle(Bundle bundle, List<Transfer> transfers){
        List<String> signatureFragments = new ArrayList<>();
        
        for (Transfer transfer : transfers) {
            int signatureMessageLength = 1;

            // If message longer than 2187 trytes, increase signatureMessageLength (add 2nd transaction)
            if (transfer.getMessage().length() > Constants.MESSAGE_LENGTH) {

                // Get total length, message / maxLength (2187 trytes)
                signatureMessageLength += Math.floor(transfer.getMessage().length() / Constants.MESSAGE_LENGTH);

                String msgCopy = transfer.getMessage();

                // While there is still a message, copy it
                while (!msgCopy.isEmpty()) {

                    String fragment = StringUtils.substring(msgCopy, 0, Constants.MESSAGE_LENGTH);
                    msgCopy = StringUtils.substring(msgCopy, Constants.MESSAGE_LENGTH, msgCopy.length());

                    // Pad remainder of fragment

                    fragment = StringUtils.rightPad(fragment, Constants.MESSAGE_LENGTH, '9');

                    signatureFragments .add(fragment);
                }
            } else {
                // Else, get single fragment with 2187 of 9's trytes
                String fragment = transfer.getMessage();

                if (transfer.getMessage().length() < Constants.MESSAGE_LENGTH) {
                    fragment = StringUtils.rightPad(fragment, Constants.MESSAGE_LENGTH, '9');
                }
                signatureFragments.add(fragment);
            }

            // get current timestamp in seconds
            long timestamp = (long) Math.floor(Calendar.getInstance().getTimeInMillis() / 1000);

            // Add first entry to the bundle
            bundle.addEntry(signatureMessageLength, transfer.getAddress(), transfer.getValue(), transfer.getTag(), timestamp);
        }
        return signatureFragments;
    }
    
    /**
     * Makes a copy of the current account state.
     * Modifications to the copy do not reflect in the original.
     * 
     * The IotaAccount is still using the original account state
     * @return a clone of the account state.
     */
    public AccountState exportAccount() {
        if (!loaded) {
            return null;
        }
        
        try {
            return getAccountManager().getAccountState().clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Saves the current account state, then replaces it with the one provided.
     * Provided state will also be saved to persist
     * @param state the new account state
     */
    public void importAccount(AccountState state) {
        synchronized (this) {
            if (this.accountManager != null) {
                this.accountManager.save();
            }
            unload();
            load(state);
            state.save(getStore());
        }
    }
    
    public String getSeed(){
        return options.getSeed();
    }
    
    public PersistenceAdapter getStore(){
        return options.getStore();
    }
    
    public IotaAPI getApi(){
        return options.getApi();
    }
    
    public EventManager getEventManager() {
        return eventManager;
    }
    
    private AccountStateManager getAccountManager() {
        return accountManager;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("----------------------");
        builder.append(System.getProperty("line.separator"));
        builder.append("iota-java accounts configured with the following: ");
        
        builder.append(System.getProperty("line.separator"));
        //builder.append("Seed: " + getSeed().substring(0, 10) + StringUtils.repeat('X', Constants.SEED_LENGTH_MAX - 10));
        
        builder.append(options.toString());
        
        return builder.toString();
    }
    
    public static class Builder extends AbstractBuilder<Builder, IotaAccount, AccountConfig> implements AccountConfig, AccountBuilderSettings {
        
        private static final Logger log = LoggerFactory.getLogger(IotaAccount.class);
        
        private PersistenceAdapter store;
        private IotaAPI api;

        private String seed;
        
        private int mwm, depth, securityLevel;
        
        public Builder(String seed) throws ArgumentException {
            super(log);
            
            if (!InputValidator.isValidSeed(seed)) {
                throw new ArgumentException(Constants.INVALID_SEED_INPUT_ERROR);
            }
            
            this.seed = seed;
        }
        
        public Builder store(PersistenceAdapter store) {
            this.store = store;
            return this;
        }
        
        public Builder api(IotaAPI api) {
            this.api = api;
            return this;
        }

        @Override
        protected Builder generate() throws Exception {
            //If a config is specified through ENV, that one will be in the stream, otherwise default config is used
            for (AccountConfig config : getConfigs()) {
                if (config != null) {
                    //calculate Account specific values
                    
                    if (0 == getMwm()) {
                        mwm = config.getMwm();
                    }
                    
                    if (0 == getDept()) {
                        depth = config.getDept();
                    }
                    
                    if (0 == getSecurityLevel()) {
                        securityLevel = config.getSecurityLevel();
                    }
                    
                    if (null == store) {
                        store = config.getStore();
                    }
                    
                    if (null == api) {
                        api = new IotaAPI.Builder().build();
                    }
                }
            }
            
            return this;
        }
        
        @Override
        protected IotaAccount compile(){
            return new IotaAccount(new AccountOptions(this));
        }
        
        @Override
        public String getSeed() {
            return seed;
        }

        @Override
        public IotaAPI getApi() {
            return api;
        }

        @Override
        public int getMwm() {
            return mwm;
        }

        @Override
        public int getDept() {
            return depth;
        }

        @Override
        public int getSecurityLevel() {
            return securityLevel;
        }

        @Override
        public PersistenceAdapter getStore() {
            return store;
        }
    }
}
