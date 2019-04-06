package juniter.core.model.dto.raw;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class WrapperTransaction implements Wrapper{

    String transaction;

    public WrapperTransaction(String rawDoc) {
        this.transaction = rawDoc;
    }
}