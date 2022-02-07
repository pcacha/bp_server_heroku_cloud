package cz.zcu.students.cacha.bp_server.view_models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * view model for updating an exhibit
 */
@Data
@NoArgsConstructor
public class UpdateExhibitVM {

    /**
     * exhibit name
     */
    @NotNull(message = "Name can not be blank")
    @Size(min = 1, max = 100, message = "Name must be between 1 to 100 letters long")
    private String name;

    /**
     * info label text
     */
    @NotNull(message = "Info label text can not be blank")
    @Size(max = 25000, message = "The text of an information label must be maximally 25000 letters long")
    private String infoLabelText;

    /**
     * building where exhibit is located
     */
    @NotNull(message = "Building can not be blank")
    @Size(max = 50, message = "Building must be maximally 50 letters long")
    private String building;

    /**
     * room where exhibit is located
     */
    @NotNull(message = "Room can not be blank")
    @Size(max = 50, message = "Room must be maximally 50 letters long")
    private String room;

    /**
     * show-case where exhibit is located
     */
    @NotNull(message = "Show-case can not be blank")
    @Size(max = 50, message = "Show-case must be maximally 50 letters long")
    private String showcase;
}
