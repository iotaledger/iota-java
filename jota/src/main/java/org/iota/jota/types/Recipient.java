package org.iota.jota.types;

import java.util.Arrays;

public class Recipient {
    
    private long value;
    private String message;
    private String tag;
    private String[] addresses;
    
    public Recipient(long value, String message, String tag, String... addresses) {
        this.value = value;
        this.message = message;
        this.tag = tag;
        this.addresses = addresses;
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

    public String[] getAddresses() {
        return addresses;
    }

    @Override
    public String toString() {
        return "Recipient [value=" + value + ", message=" + message + ", tag=" + tag + ", address="
                + Arrays.toString(addresses) + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(addresses);
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((tag == null) ? 0 : tag.hashCode());
        result = prime * result + (int) (value ^ (value >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        Recipient other = (Recipient) obj;
        if (!Arrays.equals(addresses, other.addresses)) {
            return false;
        }
        
        if ((message == null && other.message != null) || !message.equals(other.message)) {
            return false;
        }
        
        if ((tag == null && other.tag != null) || !tag.equals(other.tag)) {
            return false;
        }
        
        return value == other.value;
    }
    
    
}
