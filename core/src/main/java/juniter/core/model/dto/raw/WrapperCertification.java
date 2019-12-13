package juniter.core.model.dto.raw;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class WrapperCertification implements Wrapper{

    String cert;

    public WrapperCertification(String rawDoc) {
        this.cert = rawDoc;
    }
}