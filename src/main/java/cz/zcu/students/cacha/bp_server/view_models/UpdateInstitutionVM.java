package cz.zcu.students.cacha.bp_server.view_models;

import cz.zcu.students.cacha.bp_server.validators.UniqueInstitutionNameExclUpdated;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * view model for updating institution information
 */
@Data
@NoArgsConstructor
public class UpdateInstitutionVM {

    /**
     * institution name
     */
    @NotNull(message = "Name can not be blank")
    @Size(min = 3, max = 100, message = "Name must be between 3 to 100 letters long")
    @UniqueInstitutionNameExclUpdated
    private String name;

    /**
     * institution address
     */
    @NotNull(message = "Address can not be blank")
    @Size(min = 3, max = 100, message = "Address must be between 3 to 100 letters long")
    private String address;

    /**
     * string with latitude
     */
    @NotNull(message = "Latitude can not be empty")
    @Pattern(regexp="^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$", message="Latitude format is incorrect, example: +90.0")
    private String latitudeString;

    /**
     * string with longitude
     */
    @NotNull(message = "Longitude can not be empty")
    @Pattern(regexp="^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$", message="Longitude format is incorrect, example: -127.55")
    private String longitudeString;
}
