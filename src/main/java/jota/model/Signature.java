package jota.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an Signature
 * @author Adrian
 **/
public class Signature {

    String address;
    List<String> signatureFragments;

    public Signature() {
        this.signatureFragments = new ArrayList<>();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getSignatureFragments() {
        return signatureFragments;
    }

    public void setSignatureFragments(List<String> signatureFragments) {
        this.signatureFragments = signatureFragments;
    }
}
