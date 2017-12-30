package Views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import photomanager.Controllers.TagController;
import photomanager.Models.TagModel;

import java.net.URL;
import java.util.ResourceBundle;

public class AllTagsWindowController implements Initializable {

    /**
     * ListView containing all Tags
     */
    @FXML
    private ListView<String> tagList = new ListView<>();

    /**
     * Initializes the scene
     * @param location: the location of the URL
     * @param resources: resources to be used for initialization
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(TagModel t: TagController.getTagCollection()){
            tagList.getItems().add(t.getTagName());
        }
    }
}
