package org.iota.jota.builder;

import org.iota.jota.utils.InputValidator;

/**
 * This class is used to obtain a new address from the network.
 * 
 * To get next available address simple create an instance like this
 * <blockquote><pre>
 * AddressRequest addressRequest = new AddressRequest.Builder(seed, security).build();
 * </pre></blockquote>
 */
public class AddressRequest {

    private final String seed;
    private final int securityLevel;
    private final int index;
    private final boolean checksum;
    private final int amount;
    private final boolean addSpendAddresses;

    private AddressRequest(Builder builder) {
        this.seed = builder.seed;
        this.securityLevel = builder.securityLevel;
        this.amount = builder.amount;
        this.index = builder.index;
        this.checksum = builder.checksum;
        this.addSpendAddresses = builder.addSpendAddresses;
    }

    public static class Builder {
        private final String seed;
        private final int securityLevel;
        private int amount = 0;
        private int index = 0;
        private boolean checksum = false;
        private boolean addSpendAddresses = false;

        /**
         * Constructor to instantiate a {@link AddressRequest} instance.
         *
         * @param seed          Tryte-encoded seed. It should be noted that this seed is not transferred.
         * @param securityLevel Security level to be used for the private key / address. Can be 1, 2 or 3.
         */
        public Builder(String seed, int securityLevel) {
            this.seed = InputValidator.requireValidSeed(seed);
            this.securityLevel = InputValidator.requireValidSecurityLevel(securityLevel);
        }

        /**
         * Total number of addresses to generate.
         * If this is set to 0, we will generate until the first unspent address is found, and stop.
         */
        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        /**
         * Key index to start search from.
         */
        public Builder index(int index) {
            this.index = index;
            return this;
        }

        /**
         * Adds 9-tryte address checksum. The checksum is required for all an API call.
         */
        public Builder checksum(boolean checksum) {
            this.checksum = checksum;
            return this;
        }

        /**
         * If <code>true</code>, it returns all addresses, even those who were determined to be spent from
         */
        public Builder addSpendAddresses(boolean addSpendAddresses) {
            this.addSpendAddresses = addSpendAddresses;
            return this;
        }

        public AddressRequest build() {
            return new AddressRequest(this);
        }
    }

    public String getSeed() {
        return seed;
    }

    public int getSecurityLevel() {
        return securityLevel;
    }

    public int getIndex() {
        return index;
    }

    public boolean isChecksum() {
        return checksum;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isAddSpendAddresses() {
        return addSpendAddresses;
    }

    @Override
    public String toString() {
        return "AddressRequest{" +
                "seed='" + seed + '\'' +
                ", securityLevel=" + securityLevel +
                ", index=" + index +
                ", checksum=" + checksum +
                ", amount=" + amount +
                ", addSpendAddresses=" + addSpendAddresses +
                '}';
    }
}
