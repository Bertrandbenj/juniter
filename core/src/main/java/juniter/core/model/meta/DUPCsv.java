package juniter.core.model.meta;

public interface DUPCsv extends DUPOutCondition {
    Long getTimeToWait();

    @Override
    default String toDUP() {
        return "CSV(" + getTimeToWait() + ")";
    }

}