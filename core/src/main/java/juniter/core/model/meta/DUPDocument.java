package juniter.core.model.meta;


public interface DUPDocument extends DUPComponent {

    default Short getVersion(){
        return 10;
    }

    String getType();

    default String getCurrency() {
        return "g1";
    }

    String toDUPdoc(boolean signed);


}
