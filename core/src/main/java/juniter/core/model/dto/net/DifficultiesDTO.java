package juniter.core.model.dto.net;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class DifficultiesDTO {

    Integer block;
    List<Difficulty> levels;

}
