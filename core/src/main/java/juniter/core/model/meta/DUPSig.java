package juniter.core.model.meta;

public interface DUPSig extends DUPOutCondition {

    String getPubkey();

    @Override
    default String toDUP() {
        return "SIG(" + getPubkey() +")";
    }

}