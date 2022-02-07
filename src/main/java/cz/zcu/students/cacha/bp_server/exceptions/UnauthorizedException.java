package cz.zcu.students.cacha.bp_server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when user tries to access an element that he does not own
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnauthorizedException extends RuntimeException {
    /**
     * Creates new instance with given message
     * @param message exception message
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}
