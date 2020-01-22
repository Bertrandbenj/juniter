package juniter.core.model.technical;


import juniter.core.model.meta.DUPBlock;
import juniter.core.validation.meta.BlockConstraint;
import lombok.Data;

import javax.validation.Valid;
@Valid
@Data
public class TestBean {
    @Valid
    @BlockConstraint
    private DUPBlock bl;

    @Valid
    @BlockConstraint
    public TestBean(@Valid @BlockConstraint DUPBlock b) {
        this.bl = b;

    }

}
