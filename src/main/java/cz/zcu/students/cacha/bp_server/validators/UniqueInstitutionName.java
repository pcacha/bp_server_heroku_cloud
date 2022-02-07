package cz.zcu.students.cacha.bp_server.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to check uniqueness of institution name
 */
@Constraint(validatedBy = UniqueInstitutionNameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueInstitutionName {
    String message() default "Name is already taken";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}