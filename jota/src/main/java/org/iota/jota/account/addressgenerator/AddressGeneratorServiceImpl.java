package org.iota.jota.account.addressgenerator;

import org.iota.jota.account.errors.AddressGenerationError;
import org.iota.jota.config.options.AccountOptions;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.types.Address;
import org.iota.jota.types.Hash;
import org.iota.jota.utils.IotaAPIUtils;

public class AddressGeneratorServiceImpl implements AddressGeneratorService {
    
    private AccountOptions options;

    public AddressGeneratorServiceImpl(AccountOptions options) {
        this.options = options;
    }
    
    public Address get(int index) throws AddressGenerationError {
        try {
            return new Address(
                new Hash(IotaAPIUtils.newAddress(options.getSeed().getSeed().getTrytesString(), 
                                                 options.getSecurityLevel(), 
                                                 index, false,
                                                 options.getApi().getCurl())),
                index,
                options.getSecurityLevel()
            );
        } catch (ArgumentException e) {
            throw new AddressGenerationError(e);
        }
    }

    @Override
    public int getSecurityLevel() {
        return options.getSecurityLevel();
    }
}
