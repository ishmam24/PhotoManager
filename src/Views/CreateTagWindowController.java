package Views;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import photomanager.Controllers.TagController;
import photomanager.Models.TagModel;

public class CreateTagWindowController {

    /**
     * TextField to create the Tag
     */
    @FXML
    private TextField tagTextField = new TextField();

    /**
     * Adds tags the directory
     */
    @FXML
    public void addTag(){
        String tag = tagTextField.getText();
        TagController.getTagCollection().add(new TagModel(tag));
        Stage stage = (Stage) tagTextField.getScene().getWindow();
        stage.close();
    }

    /**
     * Cancels the action and exits the scene
     */
    @FXML
    public void cancelButtonAction(){
        Stage stage = (Stage) tagTextField.getScene().getWindow();
        stage.close();
    }
}
