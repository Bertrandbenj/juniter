package juniter.core.model.dto.raw;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class WrapperCertification implements Wrapper{

    String certification;

    public WrapperCertification(String rawDoc) {
        this.certification = rawDoc;
    }
}