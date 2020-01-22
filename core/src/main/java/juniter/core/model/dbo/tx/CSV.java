package juniter.core.model.dbo.tx;

import juniter.core.model.meta.DUPCsv;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Embeddable;

@AllArgsConstructor
@Data
@Embeddable
public class CSV implements DUPCsv {

    Long timeToWait;
}
