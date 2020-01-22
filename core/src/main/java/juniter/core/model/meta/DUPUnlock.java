package juniter.core.model.meta;

public interface DUPUnlock extends DUPComponent {



    @Override
    default String toDUP() {
        return getInputRef() + ":" + getFct() + "(" + getParam() + ")";
    }

    Integer getInputRef();

    LockType getFct();

    String getParam();

}
