package juniter.core.model.dbo.tx;

import juniter.core.model.meta.DUPXhx;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Embeddable;

@AllArgsConstructor
@Data
@Embeddable
public class XHX implements DUPXhx {

    String password;
}
