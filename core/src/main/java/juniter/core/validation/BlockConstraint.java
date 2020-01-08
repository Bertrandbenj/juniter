package juniter.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = BlockValidator.class)
@Target({ METHOD, CONSTRUCTOR, PARAMETER, FIELD })
@Retention(RUNTIME)
@Documented
public @interface BlockConstraint {
    String message() default "Block must respect DUP Protocol";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}