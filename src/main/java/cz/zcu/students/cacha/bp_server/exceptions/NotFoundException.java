package cz.zcu.students.cacha.bp_server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when element is not found
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFoundException extends RuntimeException {
    /**
     * Creates new instance with given message
     * @param message exception message
     */
    public NotFoundException(String message) {
        super(message);
    }
}
