package cz.zcu.students.cacha.bp_server.validators;

import cz.zcu.students.cacha.bp_server.domain.User;
import cz.zcu.students.cacha.bp_server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

/**
 * Class used to check username uniqueness except the one that caused the check
 */
public class UniqueUsernameExclPrincipalValidator implements ConstraintValidator<UniqueUsernameExclPrincipal, String> {

    @Autowired
    private UserRepository userRepository;

    /**
     * Initializes validator
     * @param constraintAnnotation annotation
     */
    @Override
    public void initialize(UniqueUsernameExclPrincipal constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Gets if username is unique except the that caused the check
     * @param value username
     * @param constraintValidatorContext validator context
     * @return if username is unique except the that caused the check
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> inDBOptional = userRepository.findByUsername(value);

        // if user with given username not found it is ok
        if(inDBOptional.isEmpty()) {
            return true;
        }

        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(inDBOptional.get().getId().equals(auth.getId())) {
            // if the username belong to the user that caused the check it is ok
            return true;
        }
        return false;
    }
}
