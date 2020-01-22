package juniter.core.model.meta;

public interface DUPCltv extends DUPOutCondition {
    Long getDeadline();

    @Override
    default String toDUP() {
        return "CLTV(" + getDeadline() + ")";
    }
}