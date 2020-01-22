package juniter.core.model.dbo.tx;

import juniter.core.model.meta.DUPOr;
import juniter.core.model.meta.DUPOutCondition;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Embeddable;

@AllArgsConstructor
@Data
@Embeddable
public class OR implements DUPOr {

    DUPOutCondition left, right;
}
