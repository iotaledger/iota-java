package jota.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * This class represents an Input.
 *
 * @author Adrian
 **/
public class Input {

    private String address;
    private long balance;
    private int keyIndex;
    private int security;

    /**
     * Initializes a new instance of the Input class.
     */
    public Input(String address, long balance, int keyIndex, int security) {
        this.address = address;
        this.balance = balance;
        this.keyIndex = keyIndex;
        this.security = security;

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

    /**
     * Get the address.
     *
     * @return The address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Get the address.
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
}