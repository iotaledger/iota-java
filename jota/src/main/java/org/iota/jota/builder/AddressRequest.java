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

    /**
     * Tryte-encoded seed. It should be noted that this seed is not transferred.
     */
    private final String seed;

    /**
     * Security level to be used for the private key / address. Can be 1, 2 or 3.
     */
    private final int securityLevel;

    /**
     * Key index to start search from.
     */
    private final int index;

    /**
     * Adds 9-tryte address checksum. The checksum is required for all an API call.
     */
    private final boolean checksum;

    /**
     * Total number of addresses to generate.
     * If this is set to 0, we will generate until the first unspent address is found, and stop.
     * If amount is negative, we count back from index.
     */
    private final int amount;

    /**
     * If <code>true</code>, it returns all addresses, even those who were determined to be spent from
     */
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
        private int amount = 1;
        private int index = 0;
        private boolean checksum = false;
        private boolean addSpendAddresses = false;

        public Builder(String seed, int securityLevel) {
            this.seed = InputValidator.requireValidSeed(seed);
            this.securityLevel = InputValidator.requireValidSecurityLevel(securityLevel);
        }

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder index(int index) {
            this.index = index;
            return this;
        }

        public Builder checksum(boolean checksum) {
            this.checksum = checksum;
            return this;
        }

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
