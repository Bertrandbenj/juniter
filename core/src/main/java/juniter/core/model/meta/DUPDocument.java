package juniter.core.model.meta;


public interface DUPDocument extends DUPComponent {

    default Short getVersion(){
        return 10;
    }

    void setVersion(Short v);

    String type();

    default String getCurrency() {
        return "g1";
    }

    default void setCurrency(String ccy) {

    }

    String toDUPdoc(boolean signed);

    default boolean isValid(){
        return false;
    }

}
