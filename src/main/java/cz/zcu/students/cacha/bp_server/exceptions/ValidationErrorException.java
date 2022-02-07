package cz.zcu.students.cacha.bp_server.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;

/**
 * Exception thrown when input params are not valid
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
@Data
public class ValidationErrorException extends RuntimeException {
    /**
     * input errors
     */
    private HashMap<String, String> errors;

    /**
     * Creates new instance with given validation errors hash map
     * @param errors errors map
     */
    public ValidationErrorException(HashMap<String, String> errors) {
        this.errors = errors;
    }
}

