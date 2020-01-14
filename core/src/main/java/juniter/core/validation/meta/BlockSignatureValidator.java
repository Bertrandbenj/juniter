package juniter.core.validation.meta;

import juniter.core.crypto.Crypto;
import juniter.core.model.dbo.DBBlock;
import juniter.core.validation.BlockLocalValid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BlockSignatureValidator implements ConstraintValidator<SignatureConstraint, DBBlock>, BlockLocalValid {

    private static final  Logger LOG = LogManager.getLogger(BlockSignatureValidator.class);

    @Override
    public boolean isValid(DBBlock block, ConstraintValidatorContext context) {
        LOG.warn(" ===========  signature. isValid(" + block + ") ==========  " + context);
        return Crypto.verify(block.signedPart(), block.getSignature(), block.getIssuer());
    }
}
