package juniter.core.model.meta;


public enum OutFunction {
    SIG("SIG"), // require signature of
    XHX("XHX"), // require password
    CLTV("CLTV"), // until
    CSV("CSV"); //

    private final String FCT_TYPE;

    OutFunction(String output) {
        FCT_TYPE = output;
    }

    @Override
    public String toString() {
        return FCT_TYPE;
    }

}