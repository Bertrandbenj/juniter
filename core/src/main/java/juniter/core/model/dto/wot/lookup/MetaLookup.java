package juniter.core.model.dto.wot.lookup;

import juniter.core.model.dbo.BStamp;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetaLookup  {
    private BStamp timestamp;
}
