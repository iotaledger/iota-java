package org.iota.jota.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * This class represents an Input.
 **/
public class Input {

    private String address;
    private long balance;
    private int keyIndex;
    private int security;

    /**
     * Initializes a new instance of the Input class.
     *
     * @param address  with checksum
     * @param balance
     * @param keyIndex
     * @param security
     */
    public Input(String address, long balance, int keyIndex, int security) {
        this.address = address;
        this.balance = balance;
        this.keyIndex = keyIndex;
        this.security = security;

    }

    /**
     * Get the address with checksum
     *
     * @return The address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the address with checksum.
     *
     * @param address The address.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get the balance.
     *
     * @return The balance.
     */
    public long getBalance() {
        return balance;
    }

    /**
     * Get the balance.
     *
     * @param balance The balance.
     */
    public void setBalance(long balance) {
        this.balance = balance;
    }

    /**
     * Get the keyIndex.
     *
     * @return The keyIndex.
     */
    public int getKeyIndex() {
        return keyIndex;
    }

    /**
     * Get the keyIndex.
     *
     * @param keyIndex The keyIndex.
     */
    public void setKeyIndex(int keyIndex) {
        this.keyIndex = keyIndex;
    }

    /**
     * Get the security.
     *
     * @return The security.
     */
    public int getSecurity() {
        return security;
    }

    /**
     * Get the security.
     *
     * @param security The security.
     */
    public void setSecurity(int security) {
        this.security = security;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + (int) (balance ^ (balance >>> 32));
        result = prime * result + keyIndex;
        result = prime * result + security;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Input other = (Input) obj;
        if (address == null) {
            if (other.address != null) {
                return false;
            }
        } else if (!address.equals(other.address)) {
            return false;
        }
        if (balance != other.balance) {
            return false;
        }
        if (keyIndex != other.keyIndex) {
            return false;
        }
        if (security != other.security) {
            return false;
        }
        return true;
    }

    /**
     * Returns a String that represents this object.
     *
     * @return Returns a string representation of this object.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
