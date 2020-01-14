package juniter.core.validation.meta;

import juniter.core.crypto.Crypto;
import juniter.core.model.meta.DUPTransaction;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TxSignatureValidator implements ConstraintValidator<SignatureConstraint, DUPTransaction> {
    @Override
    public boolean isValid(DUPTransaction value, ConstraintValidatorContext context) {

        for (int i = 0; i < value.issuers().size(); i++) {

            if(Crypto.verify(value.toDUPdoc(false),
                    value.signatures().get(i),
                    value.issuers().get(i))){
                return false;
            }
        }
        return true;//Crypto.verify(value.toDUPdoc(false), value.getSignature(), value.issuer());
    }
}
