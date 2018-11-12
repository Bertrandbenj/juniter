package juniter.service.bma.model;

import juniter.core.model.net.Peer;

import java.io.Serializable;

public class LeafDTO implements Serializable {
    private static final long serialVersionUID = -4464547074954356696L;

    private String hash ;

    private Peer value;

    public LeafDTO(String hash, Peer value){
        setHash(hash);
        setValue(value);
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Peer getValue() {
        return value;
    }

    public void setValue(Peer value) {
        this.value = value;
    }


}
