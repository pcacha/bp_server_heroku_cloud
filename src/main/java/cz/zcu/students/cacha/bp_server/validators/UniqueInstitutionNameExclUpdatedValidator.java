package cz.zcu.students.cacha.bp_server.validators;

import cz.zcu.students.cacha.bp_server.domain.Institution;
import cz.zcu.students.cacha.bp_server.domain.User;
import cz.zcu.students.cacha.bp_server.repositories.InstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

/**
 * Class used to check institution name uniqueness except the one that caused the check
 */
public class UniqueInstitutionNameExclUpdatedValidator implements ConstraintValidator<UniqueInstitutionNameExclUpdated, String> {

    @Autowired
    private InstitutionRepository institutionRepository;

    /**
     * Initializes validator
     * @param constraintAnnotation annotation
     */
    @Override
    public void initialize(UniqueInstitutionNameExclUpdated constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Gets if name is unique except the that caused the check
     * @param value institution name
     * @param constraintValidatorContext validator context
     * @return if name is unique except the that caused the check
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Institution> institutionOptional = institutionRepository.findByName(value);

        // if no institution with the name found it is ok
        if(institutionOptional.isEmpty()) {
            return true;
        }

        // if institution found, but it is the same as caused the check it is ok
        if(auth.getInstitution() != null && auth.getInstitution().getId().equals(institutionOptional.get().getId())) {
            return true;
        }
        return false;
    }
}
