package juniter.core.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnitDTO implements Serializable {
    private static final long serialVersionUID = -6400555666088830671L;

    private Integer size;
    private Integer free;


}