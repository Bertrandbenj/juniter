package juniter.core.model.dbo.tx;

import juniter.core.model.meta.DUPSig;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Embeddable;

@AllArgsConstructor
@Data
@Embeddable
public class SIG implements DUPSig {

    private String pubkey;
}
