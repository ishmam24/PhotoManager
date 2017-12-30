package Views;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import photomanager.Controllers.ImageController;
import photomanager.Controllers.TagController;
import photomanager.Entry;
import photomanager.MasterLog;
import photomanager.Models.ImageModel;
import photomanager.Models.TagModel;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for the add tag window.
 */
public class AddTagsWindowController implements Initializable{

    private ImageController imageController = new ImageController();

    private TagController tagController = new TagController();

    @FXML
    private ImageView imageView = new ImageView();

    @FXML
    private Label imageName = new Label();

    @FXML
    private ComboBox<String> tags = new ComboBox<>();

    @FXML
    private Button back = new Button();


    @FXML
    private ListView<String> tagList = new ListView<>();

    @FXML
    private TextField newTag = new TextField();

    @FXML
    private Button addToListButton = new Button();

    /**
     * The ImageFile displayed
     *
     */
    private File file;

    /**
     * Initializes the scene.
     * @param location: the location of the URL
     * @param resources: resources used for initialization
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        tags.setEditable(true);
        tagList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ArrayList<TagModel> tagCollection = TagController.getTagCollection();

        for(TagModel t: tagCollection){
            //tags.getItems().add(t.getTagName());
            tagList.getItems().add(t.getTagName());
        }
    }

    /**
     * Initializes the file's data which allows the program to add tags.
     * @param file: The file whose data we want
     */
    void initData(File file){
        this.file = file;
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        imageName.setText(file.getAbsolutePath());
        back.setVisible(false);
    }

    /**
     * Cancels the call for add functions; called when the user clicks the cancel button.
     * @param event: Event at which the method is called
     * @throws Exception: Handles the IOException thrown while loading the fxmlLoader.load()
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

    /**
     * Adds the tags to the images; called when the user clicks the add button
     * @param event: Event at which the method is called
     * @throws Exception: Handles the IOEception thrown while loading the fxmlLoader.load()
     */
    @FXML
    public void addButtonAction(ActionEvent event) throws Exception{
        ObservableList<String> tagsToAdd = tagList.getSelectionModel().getSelectedItems();

        if(!tagsToAdd.isEmpty()){
            ImageModel img = imageController.findImage(file.getAbsolutePath());
            for(String tagString: tagsToAdd){
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date());
                MasterLog.add(new Entry("Added tag named " + tagString + " to " + img.getImageLocation(),
                        timeStamp));
                tagController.addTag(new TagModel(tagString), img);

            }
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
     * Adds the list of tags to the ImageModel. Allows the user to add more than 1 tag.
     */
    @FXML
    public void addtoListAction(){
        if(newTag.getText().trim().length() > 0 && !tagList.getItems().contains(newTag.getText().trim())){
            String tag = newTag.getText();
            tagController.getTagCollection().add(new TagModel(tag));
            tagList.getItems().add(tag);
            newTag.clear();
        }else if(newTag.getText().trim().length() <= 0){
            errorFinding("CANNOT ADD EMPTY TAG");
        }else if(tagList.getItems().contains(newTag.getText().trim())){
            errorFinding("TAG ALREADY EXISTS");
        }

    }

    /**
     * Displays an error when a type of searched item is not found
     * @param displayError String
     */
    private void errorFinding(String displayError){
        newTag.setStyle("-fx-text-inner-color: red;");
        newTag.setText(displayError);
        newTag.setEditable(false);
        addToListButton.setDisable(true);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(3),
                ae -> {
                    newTag.setText("");
                    newTag.setEditable(true);
                    addToListButton.setDisable(false);
                    newTag.setStyle("-fx-text-inner-color: black;");
                })
        );
        timeline.play();
    }
}
