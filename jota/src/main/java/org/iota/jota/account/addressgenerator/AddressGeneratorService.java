package org.iota.jota.account.addressgenerator;

import org.iota.jota.account.errors.AddressGenerationError;
import org.iota.jota.types.Address;

public interface AddressGeneratorService {
    
    int getSecurityLevel();

    /**
     * 
     * @param index
     * @return
     * @throws AddressGenerationError
     */
    Address get(int index) throws AddressGenerationError;
    
    /**
     * 
     * @param index
     * @param securityLevel
     * @return
     * @throws AddressGenerationError
     */
    Address get(int index, int securityLevel) throws AddressGenerationError;
}
