package org.iota.jota;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.iota.jota.account.Account;
import org.iota.jota.account.AccountBalanceCache;
import org.iota.jota.account.AccountOptions;
import org.iota.jota.account.AccountState;
import org.iota.jota.account.AccountStateManager;
import org.iota.jota.account.AccountStore;
import org.iota.jota.account.addressgenerator.AddressGeneratorServiceImpl;
import org.iota.jota.account.condition.ExpireCondition;
import org.iota.jota.account.deposits.ConditionalDepositAddress;
import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.account.deposits.StoredDepositAddress;
import org.iota.jota.account.errors.AccountError;
import org.iota.jota.account.errors.AccountLoadError;
import org.iota.jota.account.errors.SendException;
import org.iota.jota.account.event.AccountEvent;
import org.iota.jota.account.event.EventListener;
import org.iota.jota.account.event.EventManager;
import org.iota.jota.account.event.events.EventAccountError;
import org.iota.jota.account.event.events.EventAttachingToTangle;
import org.iota.jota.account.event.events.EventDoingProofOfWork;
import org.iota.jota.account.event.events.EventNewInput;
import org.iota.jota.account.event.events.EventSentTransfer;
import org.iota.jota.account.event.events.EventShutdown;
import org.iota.jota.account.event.impl.EventManagerImpl;
import org.iota.jota.account.inputselector.InputSelectionStrategy;
import org.iota.jota.account.inputselector.InputSelectionStrategyImpl;
import org.iota.jota.account.plugins.Plugin;
import org.iota.jota.account.plugins.promoter.PromoterReattacherImpl;
import org.iota.jota.account.plugins.transferchecker.IncomingTransferCheckerImpl;
import org.iota.jota.account.plugins.transferchecker.OutgoingTransferCheckerImpl;
import org.iota.jota.account.seedprovider.SeedProvider;
import org.iota.jota.builder.AccountBuilder;
import org.iota.jota.config.options.AccountConfig;
import org.iota.jota.config.types.FileConfig;
import org.iota.jota.dto.response.GetAttachToTangleResponse;
import org.iota.jota.dto.response.GetTransactionsToApproveResponse;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Input;
import org.iota.jota.model.Transaction;
import org.iota.jota.model.Transfer;
import org.iota.jota.pow.SpongeFactory;
import org.iota.jota.types.Address;
import org.iota.jota.types.Hash;
import org.iota.jota.types.Recipient;
import org.iota.jota.types.Trytes;
import org.iota.jota.utils.Checksum;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.InputValidator;
import org.iota.jota.utils.IotaAPIUtils;
import org.iota.jota.utils.TrytesConverter;
import org.iota.jota.utils.thread.TaskService;
import org.iota.mddoclet.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class IotaAccount implements Account, EventListener {

    private static final String ACC_START_FAILED = "Failed to load accounts. Check the error log";
    
    private static final Logger log = LoggerFactory.getLogger(IotaAccount.class);
    
    private AccountOptions options;
    
    private EventManager eventManager;
    
    List<Plugin> tasks = new ArrayList<>();

    private AccountStateManager accountManager;
    
    boolean loaded = false;
    
    String accountId = null;

    private AddressGeneratorServiceImpl addressService;

    private AccountBalanceCache balanceCache;
    
    /**
     * 
     * @param options
     */
    public IotaAccount(AccountOptions options) {
        this.options = options;
        this.eventManager = new EventManagerImpl();
        this.getEventManager().registerListener(this);

        try {
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        if (loaded) {
            start();
        } else {
            throw new AccountLoadError(ACC_START_FAILED);
        }
    }
    
    protected IotaAccount(AccountBuilder builder) {
        this(new AccountOptions(builder));
    }
    
    /**
     * Constructs a IotaAccount with a config based on environment variables or default values.
     * 
     * @param seed
     * @throws Exception If the config did not load for whatever reason
     */
    public IotaAccount(String seed) throws Exception {
        this(new AccountBuilder(seed).generate());
    }
    
    /**
     * Constructs a IotaAccount with a config based on environment variables or default values.
     * 
     * @param seed
     * @param store The method we use for storing key/value data
     * @throws Exception If the config did not load for whatever reason
     */
    public IotaAccount(String seed, AccountStore store) throws Exception {
        this(new AccountBuilder(seed).store(store).generate());
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
        this(new AccountBuilder(seed).store(store).config(new FileConfig(config)).generate());
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
        this(new AccountBuilder(seed).store(store).config(iotaConfig).generate());
    }
    
    @Override
    public void load() {
        String accountId = buildAccountId();
        
        // TODO make this nicer
        if (options.getStore() instanceof TaskService) {
            try {
                ((TaskService)options.getStore()).load();
            } catch (Exception e) {
                throw new AccountError(e);
            }
        }
        
        load(accountId, getStore().loadAccount(accountId));
    }
    
    private String buildAccountId() {
        return IotaAPIUtils.newAddress(getSeed().getSeed().toString(), 2, 0, false, getApi().getCurl());
    }

    public void load(String accountId, AccountState state) throws AccountError {
        loaded = false;
        
        this.accountId = accountId;
        
        addressService = new AddressGeneratorServiceImpl(options);
        
        balanceCache = new AccountBalanceCache(addressService, state, getApi());
        
        InputSelectionStrategy strategy = new InputSelectionStrategyImpl(balanceCache, options.getTime());
        
        
        accountManager = new AccountStateManager(balanceCache, accountId, strategy, state, addressService, options, getStore());
        
        //All plugins do their startup tasks on load();
        addTask(new PromoterReattacherImpl(eventManager, getApi(), accountManager, options));
        addTask(new IncomingTransferCheckerImpl(eventManager, getApi(), accountManager, addressService, balanceCache, true));
        addTask(new OutgoingTransferCheckerImpl(eventManager, getApi(), accountManager));
        
        if (options.getPlugins() != null) {
            for (Plugin customPlugin : options.getPlugins()) {
                addTask(customPlugin);
            }
        }
        
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
                shutdown();
            } catch (Exception e) {
                log.error("Exception occurred shutting down accounts module: ", e);
            }
        }, "Shutdown Hook"));
    }
    
    /**
     * Unloads all registered tasks. 
     */
    private void unload(boolean clearTasks) {
        //TODO Improve
        if (options.getStore() instanceof TaskService) {
            ((TaskService)options.getStore()).shutdown();
        }
        
        synchronized (tasks) {
            for (Plugin task : tasks) {
                getEventManager().unRegisterListener(task);
                task.shutdown();
                task.setAccount(null);
            }
            
            if (clearTasks) {
                tasks.clear();
            }
        }
    }

    private void addTask(Plugin task) {
        synchronized (tasks) {
            if (task != null) {
                try {
                    task.setAccount(this);
                    task.load();
                    getEventManager().registerListener(task);
                    tasks.add(task);
                    log.debug("Loaded plugin " + task.name());
                } catch (Exception e) {
                    throw new AccountError(e);
                }
                
            }
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
        //TODO Improve
        if (options.getStore() instanceof TaskService) {
            if (!((TaskService)options.getStore()).start()) {
                throw new AccountError("Store failed to start");
            }
        }
        
        synchronized (tasks) {
            for (Plugin task : tasks) {
                if (!task.start()) {
                    // TODO log failed start
                }
            }
        }
        return true;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void shutdown() throws AccountError {
        Date now = options.getTime().time();
        unload(true);
        
        eventManager.emit(new EventShutdown(now));
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public long availableBalance() throws AccountError {
        return accountManager.getAvailableBalance();
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
        unload(false);
        options = newSettings;

        try {
            load();

            if (!start()) {
                throw new AccountError(ACC_START_FAILED);
            }
        } catch (AccountError e){
            EventAccountError event = new EventAccountError(e);
            eventManager.emit(event);
            throw e;
        }
    }
    
    public Future<Bundle> send(String address, long amount, String message, String tag){
        return send(address, amount, Optional.ofNullable(message), Optional.ofNullable(tag));
    }
    
    /**
     * Sends a transfer using the accounts balance to the provided address.
     * You must call <code>.get()</code> on the future in order to start this transfer.
     * 
     * @param address The receiver of this transfer
     * @param amount The amount we are sending to the address
     * @param message An optional message for this transfer
     * @param tag An optional tag for this transfer
     * @return The bundle we sent
     */
    @Document
    public Future<Bundle> send(String address, long amount, Optional<String> message, 
                               Optional<String> tag) {
        FutureTask<Bundle> task = new FutureTask<Bundle>(() -> {
            if (!loaded) {
                return null;
            }
            
            if (amount == 0) {
                return sendZeroValue(message, tag, Optional.ofNullable(address)).get();
            }
            
            String tryteTag = tag == null ? "" : tag.orElse("");
            tryteTag = StringUtils.rightPad(tryteTag, Constants.TAG_LENGTH, '9');
            
            if (!InputValidator.isTag(tryteTag)) {
                throw new ArgumentException(Constants.INVALID_TAG_INPUT_ERROR);
            }
            
            String asciiMessage = message == null ? "" : message.orElse("");
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
            
                List<String> trytes = prepareTransfers(transfer, inputs, remainder);
                
                //Reversed order, end is tail
                List<Transaction> transferResponse = sendTrytes(null, trytes.toArray(new String[0]));

                Trytes[] bundleTrytes = new Trytes[transferResponse.size()];
                for (int i=0; i<transferResponse.size(); i++){
                    bundleTrytes[i] = new Trytes(transferResponse.get(i).toTrytes());
                }

                accountManager.addPendingTransfer(
                        new Hash(transferResponse.get(0).getHash()),
                        bundleTrytes,1);
                
                Bundle bundle = new Bundle(transferResponse, transferResponse.size());
                EventSentTransfer event = new EventSentTransfer(bundle);
                eventManager.emit(event);
                
                return bundle;
            } catch (ArgumentException | IllegalStateException e) {
                // TODO: Throw accounts error
                EventAccountError event = new EventAccountError(e);
                eventManager.emit(event);
                return null;
            }
        });
        task.run();
        return task;
    }
    
    /**
     * Future always completed
     * 
     * {@inheritDoc}
     */
    @Override
    public Future<ConditionalDepositAddress> newDepositAddress(Date timeOut, boolean multiUse, long expectedAmount,
            ExpireCondition... otherConditions) throws AccountError {
        return newDepositRequest(new DepositRequest(timeOut, multiUse, expectedAmount), otherConditions);
    }
    
    public Future<ConditionalDepositAddress> newDepositRequest(DepositRequest request, ExpireCondition... otherConditions) throws AccountError {
        FutureTask<ConditionalDepositAddress> task = new FutureTask<ConditionalDepositAddress>(() -> {
            if (request.getMultiUse() && request.getExpectedAmount() != 0) {
                throw new AccountError("Cannot use multi-use and amount simultaneously");
            }
            
            Address address = accountManager.getNextAddress();
            StoredDepositAddress storedRequest = new StoredDepositAddress(request, options.getSecurityLevel());
            accountManager.addDepositRequest(address.getIndex(), storedRequest);
            balanceCache.addBalance(
                    new Input(address.getAddress().getHashCheckSum(), 0, address.getIndex(), options.getSecurityLevel()), 
                    request);
            
            EventNewInput event = new EventNewInput(address, request);
            eventManager.emit(event);
            return new ConditionalDepositAddress(request, address.getAddress());
        });
        task.run();
        return task;
    }
    
    /**
     * Future always completed
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
     * Future always completed
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
     * Future always completed
     * 
     * @param message the optional message to use
     * @param tag the optional tag to use
     * @return the sent bundle
     * @throws ArgumentException If an argument is wrong
     * @throws SendException If an internal error happened whilst sending
     */
    public Future<Bundle> sendZeroValue(Optional<String> message, Optional<String> tag, Optional<String> address) throws ArgumentException, SendException {
        FutureTask<Bundle> task = new FutureTask<Bundle>(() -> {
            if (!loaded) {
                return null;
            }
            
            if (tag.isPresent() && !InputValidator.isTag(tag.get())) {
                throw new ArgumentException(Constants.INVALID_TAG_INPUT_ERROR);
            }
            
            String addressHash = Constants.NULL_HASH;
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
            
            //Set a message, or use default. We use this to keep track which transfer is the outbound.
            String asciiMessage = message.orElse(Constants.ACCOUNT_MESSAGE);
            String tryteMsg = TrytesConverter.asciiToTrytes( asciiMessage);
            
            Transfer transfer = new Transfer(addressHash, 0, tryteMsg, tryteTag);
            List<Transfer> transfers = new LinkedList<>();
            transfers.add(transfer);
            
            try {
                //Trytes of one bundle
                List<String> trytes = prepareTransfers(transfers);
                List<Transaction> transferResponse = sendTrytes(null, trytes.toArray(new String[0]));
                
                Bundle bundle = new Bundle(transferResponse, transferResponse.size());
                EventSentTransfer event = new EventSentTransfer(bundle);
                eventManager.emit(event);
                
                return bundle;
            } catch (ArgumentException e) {
                throw new AccountError(e);
            }
        });
        task.run();
        return task;
    }

    /**
     * NOT YET IMPLEMENTED
     * 
     * @param addresses
     * @param amount
     * @param message
     * @param tag
     * @return
     */
    public Future<Bundle> sendMulti(String[] addresses, long amount, Optional<String> message, Optional<String> tag) {
        FutureTask<Bundle> task = new FutureTask<Bundle>(() -> {
            if (!loaded) {
        
                return null;
            }
        
            return null;
        });
        task.run();
        return task;
    }
    
    /**
     * Translates input, remainder and transfer to a single list of transfers
     * Used in sending value transactions
     * 
     * @param transfer
     * @param inputs
     * @param remainder
     * @return
     * @see #prepareTransfers(List)
     */
    private List<String> prepareTransfers(Transfer transfer, List<Input> inputs, Transfer remainder) {
        List<Transfer> transfers = new LinkedList<>();
        
        // Add the actual transfer
        transfers.add(transfer);
        
        // Add all inputs as spent
        inputs.stream().forEach(input -> {
            transfers.add(new Transfer(input.getAddress(), -input.getBalance(), "", transfer.getTag()));
            
            // For each security level, add a transfer
            for (int i = Constants.MIN_SECURITY_LEVEL; i < input.getSecurity(); i++) {
                transfers.add(new Transfer(input.getAddress(), 0, "", transfer.getTag()));
            }
        });
        
        //Add the remainder
        if (remainder != null){
            transfers.add(remainder);
        }
        
        Bundle bundle = new Bundle();
        List<String> signatureFragments = prepareBundle(bundle, transfers);
        try {
            List<String> output = IotaAPIUtils.signInputsAndReturn(
                    getSeed().getSeed().getTrytesString(), inputs, bundle, signatureFragments, getApi().getCurl());
            
            return output;
        } catch (ArgumentException e) {
            // Seed is validated at creation, will not happen under normal circumstances
            e.printStackTrace();
            return null; 
        }
    }
    
    /**
     * TODO Merge both prepareTransfers funcitons
     * 
     * @param transfers
     * @return
     */
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
            bundle.addEntry(signatureMessageLength, transfer.getAddress(), transfer.getValue(), transfer.getTag(), timestamp);
        }
        return signatureFragments;
    }
    
    private List<Transaction> sendTrytes(Hash reference, String... trytes) {
        GetTransactionsToApproveResponse txs = getApi().getTransactionsToApprove(options.getDepth(), 
                reference == null ? null : reference.getHash());

        EventAttachingToTangle attach = new EventAttachingToTangle(trytes);
        getEventManager().emit(attach);
        
        GetAttachToTangleResponse res;
        // attach to tangle - do pow
        if (getApi().getOptions().getLocalPoW() != null) {
            EventDoingProofOfWork eventPow = new EventDoingProofOfWork(trytes);
            getEventManager().emit(eventPow);
            
            res = getApi().attachToTangleLocalPow(txs.getTrunkTransaction(), txs.getBranchTransaction(), 
                    options.getMwm(), getApi().getOptions().getLocalPoW(), trytes);
        } else {
            res = getApi().attachToTangle(txs.getTrunkTransaction(), txs.getBranchTransaction(), 
                    options.getMwm(), trytes);
        }
        
        
        try {
            getApi().storeAndBroadcast(res.getTrytes());
        } catch (ArgumentException e) {
            throw new AccountError(e);
        }

        final List<Transaction> trx = new ArrayList<>();

        for (String tryte : res.getTrytes()) {
            trx.add(new Transaction(tryte, SpongeFactory.create(SpongeFactory.Mode.CURL_P81)));
        }
        
        return trx;
    }
    
    /**
     * Makes a copy of the current account state.
     * Modifications to the copy do not reflect in the original.
     * 
     * The IotaAccount is still using the original account state
     * @return a clone of the account state. or <code>null</code> if it failed
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
    
    public SeedProvider getSeed(){
        return options.getSeed();
    }
    
    private AccountStore getStore(){
        return options.getStore();
    }
    
    public IotaAPI getApi(){
        return options.getApi();
    }
    
    public EventManager getEventManager() {
        return eventManager;
    }
    
    public AccountStateManager getAccountManager() {
        return accountManager;
    }
    
    @AccountEvent
    private void onError(EventAccountError error) {
        if (error.shouldLog()) {
            log.error(error.getMessage(), error.getCause());
            error.getException().printStackTrace();
        }
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("----------------------");
        builder.append(System.getProperty("line.separator"));
        builder.append("iota-java accounts configured with the following: ");
        
        builder.append(System.getProperty("line.separator"));
        
        builder.append(options.toString());
        
        return builder.toString();
    }
    
    // Builder here for easy access
    public static class Builder extends AccountBuilder {
        
        public Builder(SeedProvider seed) throws ArgumentException {
            super(seed);
        }
        
        public Builder(String seed) throws ArgumentException {
            super(seed);
        }
    }
}
