package juniter.core.model.meta;

import java.util.List;

public interface MultiIssuer {

    List<String> signatures();

    List<String> issuers();
}
