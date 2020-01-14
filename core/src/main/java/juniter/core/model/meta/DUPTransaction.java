package juniter.core.model.meta;

import java.util.List;

public interface DUPTransaction extends DUPDocument, MultiIssuer, HasWritten {

    List<String> getSignatures();

    List<String> getIssuers();

    @Override
    default List<String> issuers() {
        return getIssuers() ;
    }

    @Override
    default List<String> signatures() {
        return getSignatures();
    }

    @Override
    default String getType(){
        return "Block";
    }
}
