package cz.zcu.students.cacha.bp_server.validators;

import cz.zcu.students.cacha.bp_server.domain.User;
import cz.zcu.students.cacha.bp_server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

/**
 * Class used to check username uniqueness
 */
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    private UserRepository userRepository;

    /**
     * Initializes validator
     * @param constraintAnnotation annotation
     */
    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Gets if username is unique
     * @param value username
     * @param constraintValidatorContext validator context
     * @return if username is unique
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> inDBOptional = userRepository.findByUsername(value);
        if(inDBOptional.isEmpty()) {
            return true;
        }
        return false;
    }
}
