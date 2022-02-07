package cz.zcu.students.cacha.bp_server.services;

import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import static cz.zcu.students.cacha.bp_server.assets_store_config.WebConfiguration.*;

/**
 * Class represent service which is responsible for images operations
 */
@Service
public class FileService {

    @Autowired
    Tika tika;

    /**
     * Separator that can occur at the beginning of base64 string
     */
    public static final String separator = ",";

    /**
     * Detects the type of file
     * @param fileArr bytes of file
     * @return detected type
     */
    public String detectType(byte[] fileArr) {
        return tika.detect(fileArr);
    }

    /**
     * Saves given institution image and returns its name
     * @param encodedImage base64 encoded image
     * @return saved image name
     * @throws Exception
     */
    public String saveInstitutionImage(String encodedImage) throws Exception {
        return saveImage(INSTITUTIONS_IMAGES_FOLDER, encodedImage);
    }

    /**
     * Deletes institution image
     * @param image name
     */
    public void deleteInstitutionImage(String image) {
        deleteImage(INSTITUTIONS_IMAGES_FOLDER, image);
    }

    /**
     * Saves given exhibit image and returns its name
     * @param encodedImage base64 encoded image
     * @return saved image name
     * @throws Exception
     */
    public String saveExhibitImage(String encodedImage) throws Exception {
        return saveImage(EXHIBITS_IMAGES_FOLDER, encodedImage);
    }

    /**
     * Delete exhibit image
     * @param image name
     */
    public void deleteExhibitImage(String image) {
        deleteImage(EXHIBITS_IMAGES_FOLDER, image);
    }

    /**
     * Saves given info label image and returns its name
     * @param encodedInfoLabel base64 encoded image
     * @return saved image name
     * @throws Exception
     */
    public String saveInfoLabelImage(String encodedInfoLabel) throws Exception {
        return saveImage(INFO_LABELS_IMAGES_FOLDER, encodedInfoLabel);
    }

    /**
     * Delete info label image
     * @param image name
     */
    public void deleteInfoLabelImage(String image) {
        deleteImage(INFO_LABELS_IMAGES_FOLDER, image);
    }

    /**
     * Saves image in to the fs
     * @param directory directory to save image
     * @param encodedImage base64 encoded image
     * @return sved image name
     * @throws IOException
     */
    private String saveImage(String directory, String encodedImage) throws Exception {
        // generate unique name
        String imageName = getRandomName();
        // remove prefix
        if (encodedImage.contains(separator)) {
            encodedImage = encodedImage.split(separator)[1];
        }
        // decode image
        byte[] decodedBytes = Base64.getDecoder().decode(encodedImage.getBytes(StandardCharsets.UTF_8));
        // add file type extension
        String fileType = detectType(decodedBytes);
        if(fileType.equalsIgnoreCase("image/jpeg")) {
            imageName += ".jpg";
        }
        else if(fileType.equalsIgnoreCase("image/png")) {
            imageName += ".png";
        }
        // save image
        File target = new File(directory + "/" + imageName);
        FileUtils.writeByteArrayToFile(target, decodedBytes);
        return imageName;
    }

    /**
     * Deletes image from given directory
     * @param directory directory to delete from
     * @param name image name
     */
    private void deleteImage(String directory, String name) {
        try {
            Files.deleteIfExists(Paths.get(directory + "/" + name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets random name by generating UUID
     * @return unique name
     */
    private String getRandomName() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
