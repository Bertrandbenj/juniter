package juniter.model.net;

public enum EndPointType {
	BMAS("BMAS"), //
	BASIC_MERKLED_API("BASIC_MERKLED_API"), //
	WS2P("WS2P"), //
	WS2PS("WS2PS"), //
	WS2PTOR("WS2PTOR"), //
	MONIT_API("MONIT_API"), //
	BMATOR("BMATOR"), // 
	ES_CORE_API("ES_CORE_API"), // 
	DEFAULT_ENDPOINT("DEFAULT_ENDPOINT"), // 
	ES_USER_API("ES_USER_API"), //
	ES_SUBSCRIPTION_API("ES_SUBSCRIPTION_API"), //
	BMA("BMA");

	private final String ENDPOINT_TYPE;

	EndPointType(String ept) {
		this.ENDPOINT_TYPE = ept;
	}

	public String getEndPointType() {
		return this.ENDPOINT_TYPE;
	}

}
