package juniter.core.model.dto.tx;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Wrapper {

    private String currency = "g1";
    private String pubkey;
    private List<Source> sources = new ArrayList<>();

    public Wrapper(String pubkey, List<Source> sources) {
        this.pubkey = pubkey;
        this.sources = sources;
    }

}
