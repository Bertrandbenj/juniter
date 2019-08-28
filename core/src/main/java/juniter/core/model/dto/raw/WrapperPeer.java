package juniter.core.model.dto.raw;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class WrapperPeer implements Wrapper{

    String peer;

    public WrapperPeer(String rawDoc) {
        this.peer = rawDoc;
    }
}