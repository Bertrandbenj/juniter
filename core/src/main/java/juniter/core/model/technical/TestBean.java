package juniter.core.model.technical;


import juniter.core.model.dbo.DBBlock;
import juniter.core.validation.meta.BlockConstraint;
import lombok.Data;

import javax.validation.Valid;
@Valid
@Data
public class TestBean {
    @Valid
    @BlockConstraint
    private DBBlock bl;

    @Valid
    @BlockConstraint
    public TestBean(@Valid @BlockConstraint DBBlock language) {
        this.bl = language;

    }

}
