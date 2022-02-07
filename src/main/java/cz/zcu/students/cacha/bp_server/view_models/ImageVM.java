package cz.zcu.students.cacha.bp_server.view_models;

import cz.zcu.students.cacha.bp_server.validators.PngJpgFile;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * view model for base64 encoded image
 */
@Data
@NoArgsConstructor
public class ImageVM {

    /**
     * base64 encoded image
     */
    @PngJpgFile
    @NotNull(message = "Image can not be null")
    private String encodedImage;
}
