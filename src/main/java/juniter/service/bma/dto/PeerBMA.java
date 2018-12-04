package juniter.service.bma.dto;

import juniter.core.model.net.Peer;

import java.io.Serializable;

public class PeerBMA implements Serializable {
    private static final long serialVersionUID = -6400215527778830671L;

    public String peer ;

    public PeerBMA (Peer peer){
        this.peer = peer.toDUP(true);
    }

    public PeerBMA(){
    }

    public String getPeer() {
        return peer;
    }

    public void setPeer(String peer) {
        this.peer = peer;
    }
}