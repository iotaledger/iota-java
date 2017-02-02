package jota.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by Adrian on 09.12.2016.
 */
public class Input {
    private String address;
    private long balance;
    private int keyIndex;
    private int security;


    public Input(String address, long balance, int keyIndex, int security) {
        this.address = address;
        this.balance = balance;
        this.keyIndex = keyIndex;
        this.security = security;

    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public int getKeyIndex() {
        return keyIndex;
    }

    public void setKeyIndex(int keyIndex) {
        this.keyIndex = keyIndex;
    }

    public int getSecurity() {
        return security;
    }

    public void setSecurity(int security) {
        this.security = security;
    }
}