package cz.zcu.students.cacha.bp_server.controllers;

import cz.zcu.students.cacha.bp_server.domain.Institution;
import cz.zcu.students.cacha.bp_server.domain.User;
import cz.zcu.students.cacha.bp_server.responses.GenericResponse;
import cz.zcu.students.cacha.bp_server.services.InstitutionService;
import cz.zcu.students.cacha.bp_server.shared.CurrentUser;
import cz.zcu.students.cacha.bp_server.view_models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Class represents rest controller which is responsible for institution operations
 */
@RestController
@RequestMapping("/institutions")
public class InstitutionController {

    @Autowired
    private InstitutionService institutionService;

    /**
     * Gets all registered institutions
     * @return all institutions
     */
    @GetMapping
    public List<InstitutionVM> getInstitutions() {
        List<InstitutionVM> institutions = institutionService.getInstitutions();
        return institutions;
    }

    /**
     * Gets all institutions ordered relative to given coordinates
     * @param coordinates coordinates
     * @return ordered institutions
     */
    @GetMapping("/ordered")
    public List<InstitutionVM> getInstitutionsOrdered(@Valid @RequestBody CoordinatesVM coordinates) {
        List<InstitutionVM> institutions = institutionService.getInstitutionsOrdered(coordinates);
        return institutions;
    }

    /**
     * Saves new institution with logged in user as a manager
     * @param institution new institution
     * @param user logged in user
     * @return message containing whether operation was processed
     */
    @PostMapping("/myInstitution")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponse saveInstitution(@Valid @RequestBody Institution institution, @CurrentUser User user) {
        institutionService.saveInstitution(institution, user);
        return new GenericResponse("Institution saved");
    }

    /**
     * Gets the chosen and possible languages of logged in user's institution
     * @param user logged in user
     * @return chosen and possible languages
     */
    @GetMapping("/myInstitution/languages")
    public AllowedLanguagesVM getAllowedLanguages(@CurrentUser User user) {
        AllowedLanguagesVM allowedLanguages = institutionService.getAllowedLanguages(user);
        return allowedLanguages;
    }

    /**
     * Adds language to logged in user's institution
     * @param languageId id of language to add
     * @param user logged in user
     * @return message containing whether operation was processed
     */
    @PostMapping("/myInstitution/languages/{languageId}")
    public GenericResponse addLanguage(@PathVariable Long languageId, @CurrentUser User user) {
        institutionService.addLanguage(languageId, user);
        return new GenericResponse("Language added");
    }

    /**
     * Updates institution image
     * @param imageVM Encoded image
     * @param user logged in user
     * @return new image name
     */
    @PutMapping("/myInstitution/updateImage")
    public GenericResponse updateImage(@Valid @RequestBody ImageVM imageVM, @CurrentUser User user) {
        String imageName = institutionService.updateImage(imageVM, user);
        return new GenericResponse(imageName);
    }

    /**
     * Updates institution information
     * @param updateInstitutionVM updated institution
     * @param user logged in user
     * @return message containing whether operation was processed
     */
    @PutMapping("/myInstitution")
    public GenericResponse updateInstitution(@Valid @RequestBody UpdateInstitutionVM updateInstitutionVM, @CurrentUser User user) {
        institutionService.updateInstitution(updateInstitutionVM, user);
        return new GenericResponse("Institution updated");
    }

    /**
     * Gets an institution of current user
     * @param user logged in user
     * @return user's institution
     */
    @GetMapping("/myInstitution")
    public InstitutionVM getMyInstitution(@CurrentUser User user) {
        InstitutionVM institution = institutionService.getMyInstitution(user);
        return institution;
    }

    /**
     * Adds new institution manager by sending credentials of a new manager account to given email
     * @param emailVM email of a new manager
     * @param user logged in user
     * @return message containing whether operation was processed
     */
    @PostMapping("/myInstitution/addManager")
    public GenericResponse addInstitutionManager(@Valid @RequestBody EmailVM emailVM, @CurrentUser User user) {
        institutionService.addInstitutionManager(emailVM, user);
        return new GenericResponse("Email with credentials to a new institution manager account sent");
    }

    /**
     * Deletes institution of logged in user
     * @param user logged in user
     * @return message containing whether operation was processed
     */
    @DeleteMapping("/myInstitution")
    public GenericResponse deleteMyInstitution(@CurrentUser User user) {
        institutionService.deleteMyInstitution(user);
        return new GenericResponse("Institution deleted");
    }
}
