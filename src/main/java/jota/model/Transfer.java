package jota.model;

import com.google.gson.Gson;
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
    private String message;
    private String tag;

    public Transfer(String timestamp, String address, String hash, Integer persistence, long value, String message, String tag) {

        this.timestamp = timestamp;
        this.address = address;
        this.hash = hash;
        this.persistence = persistence;
        this.value = value;
        this.message = message;
        this.tag = tag;

    }

    public Transfer(String address, long value, String message, String tag) {

        this.address = address;
        this.value = value;
        this.message = message;
        this.tag = tag;

    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
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

    public String getMessage() {
        return message;
    }

    public String getTag() {
        return tag;
    }

}
