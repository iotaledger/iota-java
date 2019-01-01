package org.iota.jota.account.services;

import org.iota.jota.account.errors.AddressGenerationError;
import org.iota.jota.config.options.AccountOptions;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.utils.IotaAPIUtils;

public class AddressGeneratorService {

    private AccountOptions options;

    public AddressGeneratorService(AccountOptions options) {
        this.options = options;
    }
    
    public String get(Integer index) throws AddressGenerationError {
        try {
            return IotaAPIUtils.newAddress(options.getSeed(), 
                                           options.getSecurityLevel(), 
                                           index, false,
                                           options.getApi().getCurl());
        } catch (ArgumentException e) {
            throw new AddressGenerationError(e);
        }
    }
}
