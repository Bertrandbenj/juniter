package juniter.core.model.meta;

public interface DUPAnd extends DUPOutCondition {
    DUPOutCondition getLeft();
    DUPOutCondition getRight();

    @Override
    default String toDUP() {
        return "(" + getLeft() + " && " + getRight() + ")";
    }
}