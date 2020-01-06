package juniter.core.model.technical;

public interface DUPDocument extends DUPComponent {

    String toDUPdoc(boolean signed);

    default String toDUPDoc() {
        return toDUP();
    }

}
