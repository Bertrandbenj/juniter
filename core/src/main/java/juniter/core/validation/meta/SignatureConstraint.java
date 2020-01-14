package juniter.core.validation.meta;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {BlockSignatureValidator.class, TxSignatureValidator.class})
@Target({METHOD, CONSTRUCTOR, PARAMETER, FIELD, TYPE, ANNOTATION_TYPE, TYPE_USE})
@Retention(RUNTIME)
@Documented
public @interface SignatureConstraint {
    String message() default "Signature must respect DUP Protocol";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        SignatureConstraint[] value();
    }
}