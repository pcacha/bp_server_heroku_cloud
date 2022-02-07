package cz.zcu.students.cacha.bp_server.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

/**
 * Class that represents response if an error has occured during response processing
 */
@Data
@NoArgsConstructor
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class ApiError {
    /**
     * response timestamp
     */
    private long timestamp;
    /**
     * response status
     */
    private int status;
    /**
     * response message
     */
    private String message;
    /**
     * request url
     */
    private String url;
    /**
     * possible validation errors
     */
    private Map<String, String> validationErrors;

    /**
     * Creates new instance with given parameters
     * @param status response status
     * @param message response message
     * @param url request url
     */
    public ApiError(int status, String message, String url) {
        this.status = status;
        this.message = message;
        this.url = url;
        this.timestamp = new Date().getTime();
    }
}