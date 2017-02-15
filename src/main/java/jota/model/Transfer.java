package jota.model;

import com.google.gson.Gson;

/**
 * This class represents a Transfer.
 *
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

    /**
     * Initializes a new instance of the Transfer class.
     */
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

    /**
     * Initializes a new instance of the Transfer class.
     */
    public Transfer(String address, long value, String message, String tag) {
        this.address = address;
        this.value = value;
        this.message = message;
        this.tag = tag;
    }

    /**
     * Returns a Json Object that represents this object.
     *
     * @return Returns a string representation of this object.
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
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
     * Set the address.
     *
     * @param address The address.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get the hash.
     *
     * @return The hash.
     */
    public String getHash() {
        return hash;
    }

    /**
     * Set the hash.
     *
     * @param hash The hash.
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Get the persistence.
     *
     * @return The persistence.
     */
    public Boolean getPersistence() {
        return persistence;
    }

    /**
     * Set the persistence.
     *
     * @param persistence The persistence.
     */
    public void setPersistence(Boolean persistence) {
        this.persistence = persistence;
    }

    /**
     * Get the timestamp.
     *
     * @return The timestamp.
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Set the timestamp.
     *
     * @param timestamp The timestamp.
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Get the value.
     *
     * @return The value.
     */
    public long getValue() {
        return value;
    }

    /**
     * Set the value.
     *
     * @param value The value.
     */
    public void setValue(long value) {
        this.value = value;
    }

    /**
     * Get the message.
     *
     * @return The message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message.
     *
     * @param message The message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get the tag.
     *
     * @return The tag.
     */
    public String getTag() {
        return tag;
    }

    /**
     * Set the tag.
     *
     * @param tag The tag.
     */
    public void setTag(String tag) {
        this.tag = tag;
    }
}