package cz.zcu.students.cacha.bp_server.validators;

import cz.zcu.students.cacha.bp_server.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static cz.zcu.students.cacha.bp_server.services.FileService.separator;

/**
 * Class represents validator that checks if file is of type png or jpg
 */
public class PngJpgFileValidator implements ConstraintValidator<PngJpgFile, String> {

    @Autowired
    private FileService fileService;

    /**
     * Validates whether file is jpg or png
     * @param value base64 encoded file
     * @param context validator context
     * @return whether file is jpg or png
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            return true;
        }

        String fileType;
        try {
            // remove prefix
            if (value.contains(separator)) {
                value = value.split(separator)[1];
            }

            // decode bytes
            byte[] decodeBytes = Base64.getDecoder().decode(value.getBytes(StandardCharsets.UTF_8));
            // detect type
            fileType = fileService.detectType(decodeBytes);
        } catch (Exception e) {
            return false;
        }

        // return true only if image is jpg or png
        if(fileType.equalsIgnoreCase("image/png") || fileType.equalsIgnoreCase("image/jpeg")) {
            return true;
        }

        return false;
    }
}