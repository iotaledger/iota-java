package org.iota.jota;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.iota.jota.account.Account;
import org.iota.jota.account.AccountBalanceCache;
import org.iota.jota.account.AccountState;
import org.iota.jota.account.AccountStateManager;
import org.iota.jota.account.AccountStore;
import org.iota.jota.account.addressgenerator.AddressGeneratorServiceImpl;
import org.iota.jota.account.clock.Clock;
import org.iota.jota.account.clock.SystemClock;
import org.iota.jota.account.condition.ExpireCondition;
import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.account.errors.AccountError;
import org.iota.jota.account.errors.AccountLoadError;
import org.iota.jota.account.event.AccountEvent;
import org.iota.jota.account.event.EventListener;
import org.iota.jota.account.event.EventManager;
import org.iota.jota.account.event.Plugin;
import org.iota.jota.account.event.events.EventAccountError;
import org.iota.jota.account.event.events.EventSendingTransfer;
import org.iota.jota.account.event.impl.EventManagerImpl;
import org.iota.jota.account.inputselector.InputSelectionStrategy;
import org.iota.jota.account.inputselector.InputSelectionStrategyImpl;
import org.iota.jota.account.promoter.PromoterReattacherImpl;
import org.iota.jota.account.transferchecker.IncomingTransferCheckerImpl;
import org.iota.jota.account.transferchecker.OutgoingTransferCheckerImpl;
import org.iota.jota.config.AccountConfig;
import org.iota.jota.config.FileConfig;
import org.iota.jota.config.options.AccountBuilderSettings;
import org.iota.jota.config.options.AccountOptions;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Input;
import org.iota.jota.model.Transaction;
import org.iota.jota.model.Transfer;
import org.iota.jota.types.Address;
import org.iota.jota.types.Hash;
import org.iota.jota.types.Recipient;
import org.iota.jota.types.Trytes;
import org.iota.jota.utils.AbstractBuilder;
import org.iota.jota.utils.Checksum;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.Converter;
import org.iota.jota.utils.InputValidator;
import org.iota.jota.utils.IotaAPIUtils;
import org.iota.jota.utils.TrytesConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class IotaAccount implements Account, EventListener {
    
    private static final Logger log = LoggerFactory.getLogger(IotaAccount.class);
    
    private AccountOptions options;
    
    private EventManager eventManager;
    
    List<Plugin> tasks = new ArrayList<>();

    private AccountStateManager accountManager;
    
    boolean loaded = false;
    
    String accountId = null;
    
    /**
     * 
     * @param builder
     */
    protected IotaAccount(AccountOptions options) {
        this.options = options;
        this.eventManager = new EventManagerImpl();
        this.getEventManager().registerListener(this);

        load();
        
        if (loaded) {
            start();
        } else {
            throw new AccountLoadError("Failed to load accounts. Check the error log");
        }
    }
    
    private String buildAccountId() {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        byte[] hash = digest.digest(getSeed().getBytes(StandardCharsets.UTF_8));
        String sha256hex = new String(Hex.encode(hash));
        return sha256hex;
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
    public IotaAccount(String seed, AccountStore store) throws Exception {
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
    public IotaAccount(String seed, AccountStore store, String config) throws Exception {
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
    public IotaAccount(String seed, AccountStore store, AccountConfig iotaConfig) throws Exception {
        this(new Builder(seed).store(store).config(iotaConfig).generate());
    }
    
    @Override
    public void load() {
        String accountId = buildAccountId();
        load(accountId, getStore().loadAccount(accountId));
    }

    public void load(String accountId, AccountState state) {
        loaded = false;
        
        this.accountId = accountId;
        AddressGeneratorServiceImpl service = new AddressGeneratorServiceImpl(options);
        
        AccountBalanceCache cache = new AccountBalanceCache(service, state, getApi());
        
        InputSelectionStrategy strategy = new InputSelectionStrategyImpl(cache, options.getTime());
        
        
        accountManager = new AccountStateManager(cache, accountId, strategy, state, service, options, getStore());
        
        //All plugins do their startup tasks on load();
        addTask(new PromoterReattacherImpl(eventManager, getApi(), accountManager, options));
        addTask(new IncomingTransferCheckerImpl(eventManager, getApi(), accountManager));
        addTask(new OutgoingTransferCheckerImpl(eventManager, getApi(), accountManager));
        
        shutdownHook();
        
        try {
            // Call to nodeInfo to ensure were connected
            getApi().getNodeInfo();
        } catch (ArgumentException e) {
            throw new AccountLoadError(e);
        }
        
        loaded = true;
    }
    
    private void shutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down IOTA Accounts, please hold tight...");
            try {
                unload(true);
            } catch (Exception e) {
                log.error("Exception occurred shutting down accounts module: ", e);
            }
        }, "Shutdown Hook"));
    }
    
    /**
     * Unloads all registered tasks. 
     * Any tasks added during this method execution are ignored and cleared in the end.
     */
    private void unload(boolean clearTasks) {
        for (Plugin task : tasks) {
            getEventManager().unRegisterListener(task);
            task.shutdown();
        }
        
        if (clearTasks) {
            tasks.clear();
        }
    }

    private void addTask(Plugin task) {
        if (task != null) {
            task.load();
            getEventManager().registerListener(task);
            tasks.add(task);
            log.debug("Loaded plugin " + task.name());
        }
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public String getId() throws AccountError {
        return accountId;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean start() throws AccountError {
        // TODO: Make inner task loop to prevent async errors
        return true;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void shutdown() throws AccountError {
        shutdown(true);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void shutdown(boolean skipAwaitingPlugins) throws AccountError {
        // TODO: Clear inner task loop or swap shutdown and unload
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Future<Bundle> send(Recipient recipient) throws AccountError {
        if (recipient.getAddresses().length == 1) {
            return recipient.getValue() == 0 
                    ? sendZeroValue(recipient.getMessage(), recipient.getTag(), recipient.getAddresses()[0]) 
                    : send(recipient.getAddresses()[0], 
                            recipient.getValue(),  
                            Optional.of(recipient.getMessage()), 
                            Optional.of(recipient.getTag()));
        } else {
            return sendMulti(
                    recipient.getAddresses(), 
                    recipient.getValue(), 
                    Optional.of(recipient.getMessage()), 
                    Optional.of(recipient.getTag()));
        }
        
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public DepositRequest newDepositRequest(Address depositAddress, int amount, Date timeOut, 
            ExpireCondition... otherConditions) throws AccountError {
        
        return null;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public long usableBalance() throws AccountError {
        return accountManager.getUsableBalance();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public long totalBalance() throws AccountError {
        return accountManager.getTotalBalance();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean isNew() {
        return accountManager.isNew();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void updateSettings(AccountOptions newSettings) throws AccountError {
        shutdown();
        unload(true);
        options = newSettings;
        load();
        start();
    }
    
    public Future<Bundle> send(String address, long amount, Optional<String> message, 
                               Optional<String> tag) throws ArgumentException {
       
        if (!loaded) {
            return null;
        }
        
        String tryteTag = tag.orElse("");
        tryteTag = StringUtils.rightPad(tryteTag, Constants.TAG_LENGTH, '9');
        
        if (!InputValidator.isTag(tryteTag)) {
            throw new ArgumentException(Constants.INVALID_TAG_INPUT_ERROR);
        }
        
        String asciiMessage = message.orElse("");
        String tryteMsg = TrytesConverter.asciiToTrytes( asciiMessage);
        if (!InputValidator.isTrytes(tryteMsg, tryteMsg.length())) {
            throw new ArgumentException(Constants.INVALID_INPUT_ERROR);
        }
        
        try {
            boolean spent = getApi().checkWereAddressSpentFrom(address);
            if (spent) {
                throw new ArgumentException(Constants.INVALID_ADDRESS_INPUT_ERROR);
            }
            
            Transfer transfer = new Transfer(address, amount, tryteMsg, tryteTag);
            
            List<Input> inputs = accountManager.getInputAddresses(amount);
            
            AtomicLong totalValue = new AtomicLong(0);
            inputs.stream().forEach(input -> totalValue.addAndGet(input.getBalance()));
            
            Transfer remainder = null;
            if (totalValue.get() > amount) {
                Input input = accountManager.createRemainder(totalValue.get() - amount);
                
                remainder = new Transfer(input.getAddress(), input.getBalance(), "", tryteTag);
            }
        
            List<Trytes> trytes = prepareTransfers(transfer, inputs, remainder);
            List<Transaction> transferResponse = getApi().sendTrytes(
                    trytes.stream().map(Trytes::toString).toArray(String[]::new), 
                    options.getDept(), options.getMwm(), null
                );
            
            accountManager.addPendingTransfer(
                    new Hash(transferResponse.get(0).getHash()), 
                    transferResponse.stream()
                        .map(Transaction::toTrytes)
                        .map(Trytes::new)
                        .toArray(Trytes[]::new),
                    1
                );
            
            Bundle bundle = new Bundle(transferResponse, transferResponse.size());
            EventSendingTransfer event = new EventSendingTransfer(bundle);
            eventManager.emit(event);
            
            return new FutureTask<Bundle>(() -> bundle);
        } catch (ArgumentException | IllegalStateException e) {
            // TODO: Throw accounts error
            EventAccountError event = new EventAccountError(e);
            eventManager.emit(event);
            
            log.error(e.getMessage());
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
    public Future<Bundle> sendZeroValue(String message, String tag, String address) throws ArgumentException, SendException {
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
    public Future<Bundle> sendZeroValue(Optional<String> message, Optional<String> tag, Optional<String> address) throws ArgumentException, SendException {
        if (!loaded) {
            return null;
        }
        
        if (tag.isPresent() && !InputValidator.isTag(tag.get())) {
            throw new ArgumentException(Constants.INVALID_TAG_INPUT_ERROR);
        }
        
        String addressHash = Bundle.EMPTY_HASH;
        // remove the checksum of the address if provided
        if (address.isPresent()) {
            if (InputValidator.isAddress(address.get())) {
                addressHash = Checksum.removeChecksum(address.get());
            } else {
                throw new ArgumentException(Constants.INVALID_ADDRESS_INPUT_ERROR);
            }
        }

        String tryteTag = tag.orElse("");
        tryteTag = StringUtils.rightPad(tryteTag, Constants.TAG_LENGTH, '9');
        
        //Set a mesage, or use default. We use this to keep track which transfer is the outbound.
        String asciiMessage = message.orElse(Constants.ACCOUNT_MESSAGE);
        String tryteMsg = TrytesConverter.asciiToTrytes( asciiMessage);
        
        Transfer transfer = new Transfer(addressHash, 0, tryteMsg, tryteTag);
        List<Transfer> transfers = new LinkedList<>();
        transfers.add(transfer);
        
        try {
            //Trytes of one bundle
            List<String> trytes = prepareTransfers(transfers);
            List<Transaction> transferResponse = getApi().sendTrytes(
                    trytes.toArray(new String[trytes.size()]), options.getDept(), options.getMwm(), null);
            
            Bundle bundle = new Bundle(transferResponse, transferResponse.size());
            EventSendingTransfer event = new EventSendingTransfer(bundle);
            eventManager.emit(event);
            
            return new FutureTask<Bundle>(() -> bundle);
        } catch (ArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Future<Bundle> sendMulti(String[] addresses, long amount, Optional<String> message, Optional<String> tag) {
        if (!loaded) {
            return null;
        }
        
        return null;
    }
    
    public Future<DepositRequest> requestDeposit(String depositAddress, long amount, Date timeOut, ExpireCondition... otherConditions){
        if (!loaded) {
            return null;
        }
        
        
        
        return null;
    }
    
    /**
     * Translates input, remainder and transfer to a single list of transfers
     * 
     * @param transfer
     * @param inputs
     * @param remainder
     * @return
     * @see #prepareTransfers(List)
     */
    private List<Trytes> prepareTransfers(Transfer transfer, List<Input> inputs, Transfer remainder) {
        List<Transfer> transfers = new LinkedList<>();
        
        // Add the actual transfer
        transfers.add(transfer);
        
        // Add all inputs as spent
        inputs.stream().forEach(input -> {
            transfers.add(new Transfer(input.getAddress(), -input.getBalance(), "", transfer.getTag()));
            
            // For each security level, add a transfer
            for (int i = 1; i < input.getSecurity(); i++) {
                transfers.add(new Transfer(input.getAddress(), 0, "", transfer.getTag()));
            }
        });
        
        //Add the remainder
        if (remainder != null){
            transfers.add(remainder);
        }
        
        Bundle bundle = new Bundle();
        System.out.println("prepareBundle");
        List<String> signatureFragments = prepareBundle(bundle, transfers);
        
        try {
            System.out.println("sign");
            List<String> output = IotaAPIUtils.signInputsAndReturn(getSeed(), inputs, bundle, signatureFragments, getApi().getCurl());
            Collections.reverse(output);
            
            return output.stream().map(Trytes::new).collect(Collectors.toList());
        } catch (ArgumentException e) {
            // Seed is validated at creation, will not happen under normal circumstances
            return null; 
        }
    }
    
    private List<String> prepareTransfers(List<Transfer> transfers){
        List<String> bundleTrytes = new LinkedList<>();
        
        for (Transfer transfer : transfers) {
            if (transfer.getValue() > 0) {
                
            } if (!transfer.getMessage().equals("")) {
                // Transfer message
            }
        }
        
        //If there are a lot of transfers, async and add atomic is still faster
        AtomicLong totalValue = new AtomicLong(0);
        transfers.stream().forEach(transfer -> totalValue.addAndGet(transfer.getValue()));
        
        Bundle bundle = new Bundle();
        List<String> signatureFragments = prepareBundle(bundle, transfers);
        
        signatureFragments.stream().forEach(System.out::println);
        
        //Zero value!  simply finalize the bundle
        bundle.finalize(getApi().getCurl());
        bundle.addTrytes(signatureFragments);

        List<Transaction> trxb = bundle.getTransactions();

        for (Transaction trx : trxb) {
            bundleTrytes.add(trx.toTrytes());
        }
        Collections.reverse(bundleTrytes);
        return bundleTrytes;
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
            System.out.println("trasnfer add to bundle: " + signatureMessageLength);
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
            unload(true);
            load(accountId, state);
            
            this.accountManager.save();
        }
    }
    
    public String getSeed(){
        return options.getSeed();
    }
    
    public AccountStore getStore(){
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
    
    @AccountEvent
    private void onError(EventAccountError error) {
        log.error(error.getMessage(), error.getCause());
        
        error.getException().printStackTrace();
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
        
        private AccountStore store;
        private IotaAPI api;

        private String seed;
        
        private int mwm, depth, securityLevel;
        
        private Clock clock;
        
        public Builder(String seed) throws ArgumentException {
            super(log);
            
            if (!InputValidator.isValidSeed(seed)) {
                throw new ArgumentException(Constants.INVALID_SEED_INPUT_ERROR);
            }
            
            this.seed = seed;
        }
        
        public Builder mwm(int mwm) {
            if (mwm > 0) {
                this.mwm = mwm;
            } else {
                log.warn(Constants.INVALID_INPUT_ERROR);
            }
            return this;
        }
        
        public Builder depth(int depth) {
            if (depth > 0) {
                this.depth = depth;
            } else {
                log.warn(Constants.INVALID_INPUT_ERROR);
            }
            return this;
        }
        
        public Builder securityLevel(int securityLevel) {
            if (InputValidator.isValidSecurityLevel(securityLevel)) {
                this.securityLevel = securityLevel;
            } else {
                log.warn(Constants.INVALID_SECURITY_LEVEL_INPUT_ERROR);
            }
            
            return this;
        }
        
        public Builder store(AccountStore store) {
            this.store = store;
            return this;
        }
        
        public Builder api(IotaAPI api) {
            this.api = api;
            return this;
        }
        
        public Builder clock(Clock clock) {
            this.clock = clock;
            return this;
        }

        @Override
        protected Builder generate() throws Exception {
            //If a config is specified through ENV, that one will be in the stream, otherwise default config is used
            for (AccountConfig config : getConfigs()) {
                if (config != null) {
                    //calculate Account specific values
                    
                    if (0 == getMwm()) {
                        mwm(config.getMwm());
                    }
                    
                    if (0 == getDept()) {
                        depth(config.getDept());
                    }
                    
                    if (0 == getSecurityLevel()) {
                        securityLevel(config.getSecurityLevel());
                    }
                    
                    if (null == store) {
                        store(config.getStore());
                    }
                    
                    if (null == api) {
                        api(new IotaAPI.Builder().build());
                    }
                    
                    if (null == clock) {
                        clock(new SystemClock());
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
        public AccountStore getStore() {
            return store;
        }

        @Override
        public Clock getTime() {
            return clock;
        }
    }
}
