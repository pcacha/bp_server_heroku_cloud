package cz.zcu.students.cacha.bp_server.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to check institution name uniqueness except the one that caused the check
 */
@Constraint(validatedBy = UniqueInstitutionNameExclUpdatedValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueInstitutionNameExclUpdated {
    String message() default "Institution name is already taken";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
