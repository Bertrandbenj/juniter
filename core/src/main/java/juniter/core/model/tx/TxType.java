package juniter.core.model.tx;

public enum TxType {
    D("D"),
    T("T"),
    ;

    private final String TX_TYPE;

    TxType(String transactionType) {
        this.TX_TYPE = transactionType;
    }


}
