package juniter.core.validation;

import juniter.core.model.dbo.DBBlock;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BlockValidator implements ConstraintValidator<BlockConstraint, DBBlock>, BlockLocalValid {
    @Override
    public void initialize(BlockConstraint block) {
    }
    @Override
    public boolean isValid(DBBlock block, ConstraintValidatorContext context) {
        LOG.warn(" =========   " + block);
        if (block == null) {
            return true;
        }

        if (!(block instanceof DBBlock)) {
            throw new IllegalArgumentException("Illegal method signature, "
                    + "expected parameter of type DBBlock.");
        }
//
////        if (reservation.getBegin() == null
////                || reservation.getEnd() == null
////                || reservation.getCustomer() == null) {
////            return false;
////        }
////
////        return (reservation.getBegin().isAfter(LocalDate.now())
////                && reservation.getBegin().isBefore(reservation.getEnd())
////                && reservation.getRoom() > 0);
//        return false ;
        return silentCheck(block);
    }
}