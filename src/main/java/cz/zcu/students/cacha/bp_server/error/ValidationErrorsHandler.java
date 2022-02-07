package cz.zcu.students.cacha.bp_server.error;

import cz.zcu.students.cacha.bp_server.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for handling errors
 */
@RestControllerAdvice
public class ValidationErrorsHandler {

    /**
     * Gets the response for MethodArgumentNotValidException
     * @param exception MethodArgumentNotValidException
     * @param request http request
     * @return api error response
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        // set api error params
        ApiError apiError = new ApiError(400, "Validation error", request.getServletPath());
        BindingResult result = exception.getBindingResult();
        Map<String, String> validationErrors = new HashMap<>();

        // transform validation errors to hash map
        for(FieldError fieldError: result.getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        // set validation errors
        apiError.setValidationErrors(validationErrors);
        return apiError;
    }

    /**
     * Gets the response for ValidationErrorException custom exception
     * @param exception ValidationErrorException
     * @param request http request
     * @return api error response
     */
    @ExceptionHandler({ValidationErrorException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleCustomValidationException(ValidationErrorException exception, HttpServletRequest request) {
        // set params and validation errors
        ApiError apiError = new ApiError(400, "Validation error", request.getServletPath());
        apiError.setValidationErrors(exception.getErrors());
        return apiError;
    }

    /**
     * Handles named custom exception
     * @param exception Exception
     * @param request http request
     * @return api error response
     */
    @ExceptionHandler({CannotPerformActionException.class, CannotSaveImageException.class, NotFoundException.class, UnauthorizedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleException(Exception exception, HttpServletRequest request) {
        return new ApiError(400, exception.getMessage(), request.getServletPath());
    }
}
