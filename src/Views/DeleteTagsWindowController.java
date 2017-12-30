package Views;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import photomanager.Controllers.ImageController;
import photomanager.Controllers.TagController;
import photomanager.Entry;
import photomanager.MasterLog;
import photomanager.Models.ImageModel;
import photomanager.Models.TagModel;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Controllers for the Delete Tags window.
 *
 */
public class DeleteTagsWindowController{

    /**
     * An ImageController
     *
     */
    private ImageController imageController = new ImageController();

    /**
     * A TagController
     *
     */
    private TagController tagController = new TagController();

    /**
     * FXML list that contains history of tags associated with the image
     *
     */

    @FXML
    ListView<String> tags = new ListView<>();

    /**
     * FXML imageView that views the image
     *
     */

    @FXML
    ImageView imageView = new ImageView();

    /**
     * FXML Label imageName that labels the scene
     */

    @FXML
    Label imageName = new Label();

    /**
     * FXML imageView that views the image
     *
     */

    @FXML
    Button back = new Button();

    /**
     *  file object File whose data we are interested in
     */

    private File file;

    /**
     * Initializes the file's data which allows the program to delete tags.
     * @param file: The file whose data we want
     */
    void initData(File file){
        this.file = file;
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        imageName.setText(file.getAbsolutePath());

        tags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ImageModel img = imageController.findImage(file.getAbsolutePath());
        ArrayList<TagModel> imageTags = ImageController.parseTags(img);
        for(TagModel t: imageTags){
            tags.getItems().add(t.getTagName());
        }
        back.setVisible(false);
    }


    @FXML

    /**
     * Deletes a tag once activated by the user.
     *
     * @param event: The event that activates the call for this method
     */

    public void deleteTagButton(ActionEvent event) throws Exception{
        if (tags.getSelectionModel().getSelectedItems().get(0) != null) {
            ObservableList<String> tagStrings = tags.getSelectionModel().getSelectedItems();

            ImageModel img = imageController.findImage(file.getAbsolutePath());

            for(String tag: tagStrings){
                TagModel t = new TagModel(tag);
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date());
                MasterLog.add(new Entry("Deleted " + tag + " from " + img.getImageLocation(), timeStamp));
                tagController.deleteTag(t, img);
            }

            //TagModel t = new TagModel(tagStrings.get(0));
            //tagController.deleteTag(t, img);

            ArrayList<TagModel> newSet = ImageController.parseTags(img);
            img.addToTagHistory(newSet); // Add the new set of tags to the image's tagHistory
            this.file = new File(img.getImageFile().getAbsolutePath());

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Image.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root1));
            stage.show();

            ImageWindowController controller = fxmlLoader.getController();
            controller.initData(this.file);
        }
    }

    /**
     * Takes the user back to the previous screen.
     * @param event: The event which activates the call for this method.
     * @throws Exception: handles the IOException thrown while loading the Parent root
     */
    @FXML
    public void cancelButton(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Image.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root1));
        stage.show();

        ImageWindowController controller = fxmlLoader.getController();
        controller.initData(file);
    }
}
