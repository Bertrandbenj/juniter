package juniter.core.validation.meta;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = BlockValidator.class)
@Target({METHOD, CONSTRUCTOR, PARAMETER, FIELD, TYPE, ANNOTATION_TYPE, TYPE_USE})
@Retention(RUNTIME)

@Repeatable(BlockConstraint.List.class)
@Documented
public @interface BlockConstraint {

    String message() default "Block is not locally valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean checkPow() default true;
    boolean checkSignature() default true;
    boolean checkHash() default true;
    // ConstraintTarget validationAppliesTo() default ConstraintTarget.RETURN_VALUE;

    @Target({METHOD, CONSTRUCTOR, PARAMETER, FIELD, TYPE, ANNOTATION_TYPE, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        BlockConstraint[] value();
    }
}