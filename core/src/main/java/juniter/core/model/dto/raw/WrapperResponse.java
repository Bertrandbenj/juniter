package juniter.core.model.dto.raw;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WrapperResponse {

    Integer ucode;
    String message;

    @Override
    public String toString() {

        return "{ucode:"+ucode+", message:"+message+"}";
    }
}
