package juniter.core.validation;

import juniter.core.model.dbo.DBBlock;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestBlockValidator {

    BlockValidator bValidator = new BlockValidator();

    @Test
    public void testIsNotValid(){
        assertFalse(bValidator.isValid(new DBBlock(), null));
    }


    @Test
    public void testIsValid(){
        assertTrue(bValidator.isValid(new DBBlock(), null));
    }

}