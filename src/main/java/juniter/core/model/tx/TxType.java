package juniter.core.model.tx;

public enum TxType {
	D("D"), // Dividend
	T("T"), // Transaction
;

	private final String TX_TYPE;

	TxType(String transactionType) {
		this.TX_TYPE = transactionType;
	}

	public String getEndPointType() {
		return this.TX_TYPE;
	}

}
