package cz.zcu.students.cacha.bp_server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when there is error when saving an image
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CannotSaveImageException extends RuntimeException{
    /**
     * Creates new instances with given message
     * @param message exception message
     */
    public CannotSaveImageException(String message) {
        super(message);
    }
}

