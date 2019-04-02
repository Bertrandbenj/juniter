package juniter.core.model.dto.naughtylookup;

import juniter.core.model.dbo.BStamp;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class MetaLookup implements Serializable {
    private BStamp timestamp;
}
