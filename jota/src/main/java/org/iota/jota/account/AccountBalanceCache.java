package org.iota.jota.account;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.iota.jota.IotaAPI;
import org.iota.jota.account.addressgenerator.AddressGeneratorService;
import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.account.deposits.StoredDepositRequest;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Input;
import org.iota.jota.types.Address;
import org.iota.jota.types.Hash;

public class AccountBalanceCache {
    
    private Map<Input, DepositRequest> cachedIndexMap;
    
    private AddressGeneratorService addressGenerator;
    private AccountState state;

    private long totalBalance;
    
    public AccountBalanceCache( AddressGeneratorService addressGenerator, AccountState state, IotaAPI api) {
        this.addressGenerator = addressGenerator;
        this.state = state;
        
        totalBalance = 0;

        recalcluate(api);
    }
    
    /**
     * Calculates the balance currently available using the stored deposits.
     * Any added Inputs using the {@link #addBalance(Input, DepositRequest)}, but not stored will be discarded.
     * 
     * @param api
     */
    public void recalcluate(IotaAPI api) {
        cachedIndexMap = new ConcurrentHashMap<>();
        
        for (Entry<Integer, StoredDepositRequest> entry : state.getDepositRequests().entrySet()) {
            try {
                calculateBalance(api, 
                        entry.getKey(), 
                        entry.getValue().getRequest(), 
                        entry.getValue().getSecurityLevel());
            } catch (ArgumentException e) {
                e.printStackTrace();
                System.out.println("Failed to find balance for index " + entry.getKey() + ", ignoring..");
            }
        }
    }

    private void calculateBalance(IotaAPI api, int index, DepositRequest request, int security) {
        Address address = addressGenerator.get(index);
        
        long balance;
        if (request.hasTimeOut()) {
            // Not a remainder address, check balance
            balance = api.getBalance(100, address.getAddress().getHashCheckSum());
        } else {
            balance = request.getExpectedAmount();
        }
        
        Input input = new Input(
            address.getAddress().getHashCheckSum(), balance, index, security
        );
        addInput(input, request);
    }
    
    private void addInput(Input input, DepositRequest balance) {
        cachedIndexMap.put(input, balance);
        totalBalance += input.getBalance();
    }

    public Entry<Input, DepositRequest> getByAddress(Address address){
        return getByHash(address.getAddress());
    }
    
    public Entry<Input, DepositRequest> getByHash(Hash hash){
        return getByHash(hash.getHash());
    }
    
    public Entry<Input, DepositRequest> getByHash(String hash){
        synchronized(cachedIndexMap) {
            for (Entry<Input, DepositRequest> entry : cachedIndexMap.entrySet()) {
                if (entry.getKey().getAddress().equals(hash)) {
                    return entry;
                }
            }
        }   
        
        return null;
    }
    
    public Entry<Input, DepositRequest> getByIndex(int index){
        synchronized(cachedIndexMap) {
            for (Entry<Input, DepositRequest> entry : cachedIndexMap.entrySet()) {
                if (entry.getKey().getKeyIndex() == index) {
                    return entry;
                }
            }
        }
        return null;
    }

    public Input first() {
        return cachedIndexMap.entrySet().toArray(new Input[] {})[0];
    }
    
    public Stream<Entry<Input, DepositRequest>> getStream(){
        return cachedIndexMap.entrySet().stream();
    }

    public void addBalance(Input input, DepositRequest depositRequest) {
        synchronized(cachedIndexMap) {
            addInput(input, depositRequest);
        }
    }
    
    /**
     * Based on CDA amount defined
     * TODO: if CDA amount = 0, add input amount
     * @return
     */
    public long getTotalBalance() {
        return totalBalance;
    }

    public void removeInput(Input input) {
        synchronized(cachedIndexMap) {
            cachedIndexMap.remove(input);
        }
        totalBalance -= input.getBalance();
    }
}
