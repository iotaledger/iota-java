package jota.model;

import com.google.gson.Gson;

/**
 * This class represents a Transfer
 * @author pinpong
 */
public class Transfer {

    private String timestamp;
    private String address;
    private String hash;
    private Boolean persistence;
    private long value;
    private String message;
    private String tag;

    public Transfer(String timestamp, String address, String hash, Boolean persistence, long value, String message,
                    String tag) {
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

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Boolean getPersistence() {
        return persistence;
    }

    public void setPersistence(Boolean persistence) {
        this.persistence = persistence;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}