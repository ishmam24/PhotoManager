package Views;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import photomanager.Controllers.TagController;
import photomanager.Entry;
import photomanager.MasterLog;
import photomanager.Models.TagModel;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class DeleteMasterTagsController implements Initializable{

    private TagController tagController = new TagController();
    @FXML
    private ListView<String> tagList = new ListView<>();

    /**
     * Lists the tags
     * @param location URL
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tagList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        for(TagModel t: TagController.getTagCollection()){
            tagList.getItems().add(t.getTagName());
        }
    }

    /**
     * Exits the delete tag window
     */
    @FXML
    public void cancel(){
        Stage stage = (Stage) tagList.getScene().getWindow();
        stage.close();
    }

    /**
     * Deletes the tags selected from listView
     */
    @FXML
    public void deleteTags(){
        ObservableList<String> selectedTagList = tagList.getSelectionModel().getSelectedItems();

        for(String tag: selectedTagList){
            String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date());
            MasterLog.add(new Entry("Deleted tag @" + tag + "from tag collection.", timeStamp));
            TagModel t = new TagModel(tag);
            tagController.removeTagFromCollection(t);
        }

        Stage stage = (Stage) tagList.getScene().getWindow();
        stage.close();
    }
}
