package jota.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian on 27.12.2016.
 */
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
