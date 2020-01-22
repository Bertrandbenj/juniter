package juniter.core.model.dbo.tx;

import juniter.core.model.meta.DUPAnd;
import juniter.core.model.meta.DUPOutCondition;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Embeddable;

@AllArgsConstructor
@Data
@Embeddable
public class AND implements DUPAnd {

    DUPOutCondition left, right;

}
