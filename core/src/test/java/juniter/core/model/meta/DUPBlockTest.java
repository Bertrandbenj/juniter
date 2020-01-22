package juniter.core.model.meta;

import juniter.core.model.dbo.DBBlock;
import juniter.core.model.technical.TestBean;
import juniter.core.validation.meta.BlockConstraint;
import org.junit.Test;

import javax.validation.Valid;
import javax.validation.Validation;

public class DUPBlockTest {

    @Test
    public void testCheck() {
        new DBBlock().check(new DBBlock());
    }

    @Valid
    @BlockConstraint
    private DBBlock getABlock() {
        return new DBBlock();
    }

    @Valid
    @BlockConstraint
    private TestBean getATEstBlock() {
        return  new TestBean(getABlock());
    }


    @Test
    public void testMethod() {
        System.out.println(getATEstBlock());
    }


    @Test
    public void testCheck2() {
        new DBBlock().checked();
    }

    @Test
    public void usingBean() {
        new TestBean(new DBBlock());
    }

    @Test
    public void hardTest() {
        Validation
                .buildDefaultValidatorFactory()
                .getValidator()
                .validate(new DBBlock());
    }


}