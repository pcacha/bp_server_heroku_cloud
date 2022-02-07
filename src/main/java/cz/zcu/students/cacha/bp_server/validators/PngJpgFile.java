package cz.zcu.students.cacha.bp_server.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to check right attachment type
 */
@Constraint(validatedBy = PngJpgFileValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PngJpgFile {
    String message() default "Image must be of png or jpeg type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}