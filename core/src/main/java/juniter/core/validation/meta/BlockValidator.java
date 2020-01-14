package juniter.core.validation.meta;

import juniter.core.model.dbo.DBBlock;
import juniter.core.validation.BlockLocalValid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

//@SupportedValidationTarget({ValidationTarget.PARAMETERS })
public class BlockValidator implements ConstraintValidator<BlockConstraint, DBBlock>, BlockLocalValid {
    private static final Logger LOG = LogManager.getLogger(BlockValidator.class);

    private boolean checkPowAndSignature = true;


    @Override
    public void initialize(BlockConstraint constraintAnnotation) {
        checkPowAndSignature = constraintAnnotation.checkPowAndSignature();
    }

    @Override
    public boolean isValid(DBBlock block, ConstraintValidatorContext context) throws AssertionError {
        LOG.warn(" ===========   isValid(" + block + ")  " + context.getDefaultConstraintMessageTemplate());
        if (block == null) {
            return true;
        }

        try {

            assertBlockHash(block.signedPartSigned(), block.getHash());

            assertSignature(block.signedPart(), block.getSignature(), block.getIssuer());

            block.getTransactions().forEach(tx -> {
                checkTxAmounts(tx);
                checkTxSources(tx);
                checkTxIssuers(tx);
                checkTxRecipients(tx);
                if (checkPowAndSignature)
                    checkTxSignature(tx);
                checkTxVersion(tx);
            });

            checkParameters(block);

            checkUniversalDividend(block);

            if (checkPowAndSignature)
                checkProofOfWork(block);

            if (checkPowAndSignature)
                checkInnerHash(block);

            checkPreviousHash(block);

            checkPreviousIssuer(block);

            checkUnitBase(block);

            if (checkPowAndSignature)
                checkBlockSignature(block);

            checkBlockTimes(block);

            if (checkPowAndSignature)
                checkIdentitiesSignature(block);

            checkIdentitiesUserIDConflict(block);

            checkIdentitiesPubkeyConflict(block);

            //checkIdentitiesMatchJoin(block);

            checkMembershipUnicity(block);

            checkRevokedUnicity(block);

            checkRevokedAreExcluded(block);

            if (checkPowAndSignature)
                checkMembershipsSignature(block);

            checkCertificationOneByIssuer(block);

            checkCertificationUnicity(block);

            //checkCertificationIsntForLeaverOrExcluded(block);

            checkMaxTransactionChainingDepth(block);
        }catch(AssertionError ae ){
            context.buildConstraintViolationWithTemplate(ae.getMessage())
                    .addConstraintViolation();
            throw  ae;
        }

        LOG.warn(" finished isValid "  );

        return true;
    }
}