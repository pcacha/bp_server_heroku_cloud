package cz.zcu.students.cacha.bp_server.controllers;

import cz.zcu.students.cacha.bp_server.domain.Exhibit;
import cz.zcu.students.cacha.bp_server.domain.User;
import cz.zcu.students.cacha.bp_server.responses.GenericResponse;
import cz.zcu.students.cacha.bp_server.services.ExhibitService;
import cz.zcu.students.cacha.bp_server.shared.CurrentUser;
import cz.zcu.students.cacha.bp_server.view_models.ExhibitVM;
import cz.zcu.students.cacha.bp_server.view_models.ExhibitsLanguagesVM;
import cz.zcu.students.cacha.bp_server.view_models.ImageVM;
import cz.zcu.students.cacha.bp_server.view_models.UpdateExhibitVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Class represents rest controller which is responsible for exhibits operations
 */
@RestController
@RequestMapping("/exhibits")
public class ExhibitController {

    @Autowired
    private ExhibitService exhibitService;

    /**
     * Gets all exhibits of given institution
     * @param institutionId institution id
     * @return all exhibits of given institution
     */
    @GetMapping("/all/{institutionId}")
    public List<ExhibitVM> getAllExhibitsOfInstitution(@PathVariable Long institutionId) {
        List<ExhibitVM> exhibits = exhibitService.getExhibitsOfInstitution(institutionId);
        return exhibits;
    }

    /**
     * Gets all exhibits of logged in user's institution
     * @param user logged in user
     * @return all exhibits of logged in user's institution
     */
    @GetMapping("/all")
    public List<ExhibitVM> getAllExhibitsOfUsersInstitution(@CurrentUser User user) {
        List<ExhibitVM> exhibits = exhibitService.getAllExhibitsOfUsersInstitution(user);
        return exhibits;
    }

    /**
     * Gets exhibit based on its id
     * @param exhibitId exhibit id
     * @param user logged in usr
     * @return exhibit based on its id
     */
    @GetMapping("/{exhibitId}")
    public ExhibitVM getExhibit(@PathVariable Long exhibitId, @CurrentUser User user) {
        ExhibitVM exhibit = exhibitService.getExhibit(exhibitId, user);
        return exhibit;
    }

    /**
     * Gets base64 encoded QR code for given exhibit
     * @param exhibitId exhibit id
     * @param user logged in user
     * @return base64 encoded QR code
     */
    @GetMapping("/{exhibitId}/qrcode")
    public String getExhibitQRCode(@PathVariable Long exhibitId, @CurrentUser User user) {
        String qrCodeBase64 = exhibitService.getExhibitQRCode(exhibitId, user);
        return qrCodeBase64;
    }

    /**
     * Deletes an exhibit based on its id
     * @param exhibitId exhibit id
     * @param user logged in user
     * @return message containing whether operation was processed
     */
    @DeleteMapping("/{exhibitId}")
    public GenericResponse deleteExhibit(@PathVariable Long exhibitId, @CurrentUser User user) {
        exhibitService.deleteExhibit(exhibitId, user);
        return new GenericResponse("Exhibit deleted");
    }

    /**
     * Saves new exhibit to logged in user's institution
     * @param exhibit new exhibit
     * @param user logged in user
     * @return message containing whether operation was processed
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponse saveExhibit(@Valid @RequestBody Exhibit exhibit, @CurrentUser User user) {
        exhibitService.saveExhibit(exhibit, user);
        return new GenericResponse("Exhibit saved");
    }

    /**
     * Saves new exhibit to an institution defined by its id
     * @param institutionId id of an institution managing the exhibit
     * @param exhibit new exhibit
     * @return message containing whether operation was processed
     */
    @PostMapping("/{institutionId}")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponse saveExhibit(@PathVariable Long institutionId, @Valid @RequestBody Exhibit exhibit) {
        exhibitService.saveExhibit(exhibit, institutionId);
        return new GenericResponse("Exhibit saved");
    }

    /**
     * Updates the image of given exhibit and returns its new name
     * @param exhibitId id of updated exhibit
     * @param imageVM encoded image
     * @param user logged in user
     * @return new image name
     */
    @PutMapping("/{exhibitId}/updateImage")
    public GenericResponse updateExhibitImage(@PathVariable Long exhibitId, @Valid @RequestBody ImageVM imageVM, @CurrentUser User user) {
        String imageName = exhibitService.updateExhibitImage(exhibitId, imageVM, user);
        return new GenericResponse(imageName);
    }

    /**
     * Updates the info label image of given exhibit and returns its new name
     * @param exhibitId id of updated exhibit
     * @param imageVM encoded info label
     * @param user logged in user
     * @return new info label name
     */
    @PutMapping("/{exhibitId}/updateInfoLabel")
    public GenericResponse updateExhibitInfoLabel(@PathVariable Long exhibitId, @Valid @RequestBody ImageVM imageVM, @CurrentUser User user) {
        String imageName = exhibitService.updateExhibitInfoLabel(exhibitId, imageVM, user);
        return new GenericResponse(imageName);
    }

    /**
     * Updates information of given exhibit
     * @param exhibitId id of updated exhibit
     * @param updateExhibitVM updated exhibit information
     * @param user logged in user
     * @return message containing whether operation was processed
     */
    @PutMapping("/{exhibitId}")
    public GenericResponse updateExhibit(@PathVariable Long exhibitId, @Valid @RequestBody UpdateExhibitVM updateExhibitVM, @CurrentUser User user) {
        exhibitService.updateExhibit(exhibitId, updateExhibitVM, user);
        return new GenericResponse("Exhibit updated");
    }

    /**
     * Gets exhibits and allowed languages of logged in user insitution
     * @param user logged in user
     * @return exhibits and allowed language
     */
    @GetMapping("/approveTranslations")
    public ExhibitsLanguagesVM getExhibitsApproveTranslations(@CurrentUser User user) {
        ExhibitsLanguagesVM exhibitsLanguagesVM = exhibitService.getExhibitsApproveTranslations(user);
        return exhibitsLanguagesVM;
    }

    /**
     * Gets exhibits and allowed languages of an institution defined by its id
     * @param institutionId institution id
     * @return exhibits and allowed language
     */
    @GetMapping("/translate/{institutionId}")
    public ExhibitsLanguagesVM getExhibitsTranslate(@PathVariable Long institutionId) {
        ExhibitsLanguagesVM exhibitsLanguagesVM = exhibitService.getExhibitsTranslate(institutionId);
        return exhibitsLanguagesVM;
    }
}
