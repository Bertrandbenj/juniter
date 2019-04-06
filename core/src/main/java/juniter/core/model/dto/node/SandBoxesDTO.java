package juniter.core.model.dto.node;

import lombok.*;

@Data
@AllArgsConstructor
public class SandBoxesDTO  {

    private static final long serialVersionUID = -6400559996088830671L;

    private UnitDTO identities;
    private UnitDTO memberships;
    private UnitDTO transactions;


}
