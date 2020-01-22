package juniter.core.model.meta;

public interface DUPXhx extends DUPOutCondition {
    String getPassword();

    @Override
    default String toDUP() {
        return "XHX(" + getPassword() + ")";
    }

}