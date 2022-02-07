package cz.zcu.students.cacha.bp_server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when operation can not be performed
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CannotPerformActionException extends RuntimeException{
    /**
     * Creates new CannotPerformActionException with given message
     * @param message exception message
     */
    public CannotPerformActionException(String message) {
        super(message);
    }
}
