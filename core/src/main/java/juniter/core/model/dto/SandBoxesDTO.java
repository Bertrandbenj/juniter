package juniter.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SandBoxesDTO  {

    private static final long serialVersionUID = -6400559996088830671L;

    private UnitDTO identities;
    private UnitDTO memberships;
    private UnitDTO transactions;


}
