package juniter.core.model.dbo.tx;

import juniter.core.model.meta.DUPCltv;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Embeddable;

@AllArgsConstructor
@Data
@Embeddable
public class CLTV implements DUPCltv {

    Long deadline;
}
