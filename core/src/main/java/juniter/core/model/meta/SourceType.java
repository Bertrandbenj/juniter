package juniter.core.model.meta;

public enum SourceType {
    D("D"),
    T("T"),
    ;

    private final String SOURCE_TYPE;

    SourceType(String transactionType) {
        this.SOURCE_TYPE = transactionType;
    }


}
