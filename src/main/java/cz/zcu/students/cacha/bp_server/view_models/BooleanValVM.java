package cz.zcu.students.cacha.bp_server.view_models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * View model with one boolean value
 */
@Data
@NoArgsConstructor
public class BooleanValVM {
    /**
     * True/false value
     */
    @NotNull(message = "Value can not be empty")
    private Boolean value;
}