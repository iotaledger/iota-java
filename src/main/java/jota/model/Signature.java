package jota.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an Signature.
 *
 * @author Adrian
 **/
public class Signature {

    private String address;
    private List<String> signatureFragments;

    /**
     * Initializes a new instance of the Signature class.
     */
    public Signature() {
        this.signatureFragments = new ArrayList<>();
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
     * Get the signatureFragments.
     *
     * @return The signatureFragments.
     */
    public List<String> getSignatureFragments() {
        return signatureFragments;
    }

    /**
     * Set the signatureFragments.
     *
     * @param signatureFragments The signatureFragments.
     */
    public void setSignatureFragments(List<String> signatureFragments) {
        this.signatureFragments = signatureFragments;
    }
}
