package jota.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by pinpong on 02.12.16.
 */
public class Transfer {

    private String timestamp;
    private String address;
    private String hash;
    private Integer persistence;
    private long value;

    public Transfer(String timestamp, String address, String hash, Integer persistence, long value) {

        this.timestamp = timestamp;
        this.address = address;
        this.hash = hash;
        this.persistence = persistence;
        this.value = value;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public String getAddress() {
        return address;
    }

    public String getHash() {
        return hash;
    }

    public Integer getPersistence() {
        return persistence;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public long getValue() {
        return value;
    }
}
