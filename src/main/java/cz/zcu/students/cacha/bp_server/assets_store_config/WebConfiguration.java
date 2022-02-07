package cz.zcu.students.cacha.bp_server.assets_store_config;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * Configuration of images serving
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    /**
     * folder of institutions images
     */
    public static final String INSTITUTIONS_IMAGES_FOLDER = "institutions_images";
    /**
     * folder of exhibits images
     */
    public static final String EXHIBITS_IMAGES_FOLDER = "exhibits_images";
    /**
     * folder of info label images
     */
    public static final String INFO_LABELS_IMAGES_FOLDER = "info_labels_images";

    /**
     * default institution image name
     */
    public static final String DEFAULT_INSTITUTION_IMAGE = "default_institution.png";
    /**
     * default exhibit image name
     */
    public static final String DEFAULT_EXHIBIT_IMAGE = "default_exhibit.png";
    /**
     * default blank image
     */
    public static final String DEFAULT_IMAGE = "default.png";

    /**
     * Maps http requests to folders
     * @param registry resource handler
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // map institutions images
        registry.addResourceHandler("/" + INSTITUTIONS_IMAGES_FOLDER + "/**")
                .addResourceLocations("file:" + INSTITUTIONS_IMAGES_FOLDER + "/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));

        // map exhibits images
        registry.addResourceHandler("/" + EXHIBITS_IMAGES_FOLDER + "/**")
                .addResourceLocations("file:" + EXHIBITS_IMAGES_FOLDER + "/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));

        // map info label images
        registry.addResourceHandler("/" + INFO_LABELS_IMAGES_FOLDER + "/**")
                .addResourceLocations("file:" + INFO_LABELS_IMAGES_FOLDER + "/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
    }

    /**
     * Creates images folders and inserts default images
     * @return runner
     */
    @Bean
    CommandLineRunner initImagesFolders() {
        return (args) -> {
            // create folders
            createNonExistingFolder(INSTITUTIONS_IMAGES_FOLDER);
            createNonExistingFolder(EXHIBITS_IMAGES_FOLDER);
            createNonExistingFolder(INFO_LABELS_IMAGES_FOLDER);

            // insert default images
            addNonExistingDefaultImage(INSTITUTIONS_IMAGES_FOLDER, DEFAULT_INSTITUTION_IMAGE);
            addNonExistingDefaultImage(EXHIBITS_IMAGES_FOLDER, DEFAULT_EXHIBIT_IMAGE);
        };
    }

    /**
     * Inserts default image to folder if there is no default image
     * @param dir directory name
     * @param defaultName default image name
     * @throws IOException exception
     */
    private void addNonExistingDefaultImage(String dir, String defaultName) throws IOException {
        String path = dir + "/" + defaultName;
        File image = new File(path);
        boolean imageExists = image.exists() && image.isFile();

        if(!imageExists) {
            // insert image only if there is no default already
            try(InputStream is =  getClass().getClassLoader().getResourceAsStream("static/" + DEFAULT_IMAGE)) {
                try(OutputStream os = new FileOutputStream(image)){
                    // copy image from static resources
                    IOUtils.copy(is, os);
                }
            }
        }
    }

    /**
     * Creates non existing folder
     * @param path path
     */
    private void createNonExistingFolder(String path) {
        File folder = new File(path);
        boolean folderExists = folder.exists() && folder.isDirectory();
        // create folder only if it not already exists
        if(!folderExists) {
            folder.mkdir();
        }
    }
}