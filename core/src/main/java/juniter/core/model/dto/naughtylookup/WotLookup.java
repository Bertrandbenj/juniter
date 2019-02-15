package juniter.core.model.dto.naughtylookup;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class WotLookup implements Serializable {
    private boolean partial;
    private List<WotLookupResult> results;


}

