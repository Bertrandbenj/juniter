package juniter.service.bma.model;

import java.io.Serializable;

public class SandBoxesDTO implements Serializable {
    private static final long serialVersionUID = -6400559996088830671L;


    private UnitDTO identities ;
    private UnitDTO memberships ;
    private UnitDTO transactions;


    public UnitDTO getIdentities() {
        return identities;
    }

    public void setIdentities(UnitDTO identities) {
        this.identities = identities;
    }

    public UnitDTO getMemberships() {
        return memberships;
    }

    public void setMemberships(UnitDTO memberships) {
        this.memberships = memberships;
    }

    public UnitDTO getTransactions() {
        return transactions;
    }

    public void setTransactions(UnitDTO transactions) {
        this.transactions = transactions;
    }


}
