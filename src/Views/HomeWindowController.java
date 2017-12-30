package Views;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import photomanager.Controllers.ImageController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import photomanager.Entry;
import photomanager.MasterLog;
import photomanager.Models.ImageModel;
import photomanager.Models.TagModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for the home window.
 */
public class HomeWindowController implements Initializable {

    /**
     * FXML searchText that allows the user to search the images and tags from the repositories or images added to the
     * PhotoManager.
     */
    @FXML
    private TextField searchText;

    /**
     * FXML list that contains the images added by the user.
     */
    @FXML
    private ListView<String> listViewer;

    /**
     * FXML Toolbar that allows the user to navigate the PhotoManager.
     */
    @FXML
    private ToolBar toolBar;

    /**
     * FXML search button that runs the search bar.
     */
    @FXML
    private Button searchButton;

    /**
     * FXML ListView that contains the list of selected images.
     */
    private ListView<String> selected = new ListView<>();

    /**
     * FXML ToggleSwitch that allows the user to switch between searching tags and searching images.
     */
    private ToggleSwitch toggleSwitch = new ToggleSwitch();

    /**
     * Initializes the scene.
     * @param location: the location of the URL
     * @param resources: resources used for initialization
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        toolBar.getItems().add(2,toggleSwitch);
        listViewer.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listViewer.getSelectionModel().selectedItemProperty().addListener((obs,ov,nv)->
                selected.setItems(listViewer.getSelectionModel().getSelectedItems()));
        refresh();
    }

    /**
     * Searches the home window using the search bar. Can be used to search for Images and Tags, implemented by the
     * toggle button.
     */
    @FXML
    public void searchImage() {
        boolean imgToggled = toggleSwitch.switchOnProperty().get();
        boolean found = false;
        if(imgToggled){
            listViewer.getItems().clear();
            for(ImageModel image: ImageController.getImageCollection()){
                if(image.getImageName().contains(searchText.getText())){
                    found = true;
                    if(!listViewer.getItems().contains(image.getImageLocation())){
                        listViewer.getItems().add(image.getImageLocation());
                    }
                }
            }
            if(!found){
                listViewer.getItems().clear();
                errorFinding("IMAGE NOT FOUND");
            }
        }
        else{
            for(ImageModel image: ImageController.getImageCollection()){
                ArrayList<TagModel> imageTags = ImageController.parseTags(image);
                for(TagModel tag: imageTags){
                    if (tag.getTagName().contains(searchText.getText())) {
                        found = true;
                        if(!listViewer.getItems().contains(image.getImageLocation())){
                            listViewer.getItems().add(image.getImageLocation());
                        }
                    }
                }
            }
            if (!found) {
                listViewer.getItems().clear();
                errorFinding("TAG NOT FOUND");
            }
        }
    }

    /**
     * Displays error when a type of searched item is not found.
     * @param displayError String
     */
    private void errorFinding(String displayError){
        searchText.setStyle("-fx-text-inner-color: red;");
        searchText.setText(displayError);
        searchText.setEditable(false);
        searchButton.setDisable(true);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(3),
                ae -> {
                    searchText.setText("");
                    searchText.setEditable(true);
                    searchButton.setDisable(false);
                    searchText.setStyle("-fx-text-inner-color: black;");
                })
        );
        timeline.play();
    }

    /**
     * Imports images by switching to FileChooserController.
     * @throws Exception Exception
     */
    @FXML
    public void importImages() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("JavaFileChooser.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = (Stage) searchText.getScene().getWindow();
        stage.setScene(new Scene(root1));
        stage.show();
    }

    /**
     * Refreshes the window and data when the user presses the refresh button.
     */
    public void refresh(){
        searchText.setText("");
        try{

            for(ImageModel  item: ImageController.getImageCollection()){
                if(!listViewer.getItems().contains(item.getImageLocation())){
                    listViewer.getItems().add(item.getImageLocation());
                }
            }
        }catch (Exception e){

            for(ImageModel  item: ImageController.getImageCollection()){
                if(!listViewer.getItems().contains(item.getImageLocation())){
                    listViewer.getItems().add(item.getImageLocation());
                }
            }
        }
    }

    /**
     * Implements the help button
     * @throws Exception: Handles the exception thrown while loading the fxmlLoader.load()
     */
    public void helpButton() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Help.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(new Scene(root1));
        stage.show();
    }
    /**
     * Closes the application when the user presses the close button.
     */
    public void close(){
        Stage stage = (Stage) listViewer.getScene().getWindow();
        stage.close();
    }

    /**
     * Opens an image by directing to ImageWindowController
     * @param event MouseEvent
     * @throws Exception Exception
     */
    public void openImage(MouseEvent event) throws Exception{
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Image.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root1));
            stage.show();

            String imageLocation = listViewer.getSelectionModel().getSelectedItem();
            File file = new File("");

            for(ImageModel img: ImageController.getImageCollection()){
                if(imageLocation.equals(img.getImageLocation())){
                    file = new File(img.getImageLocation());
                }
            }
            ImageWindowController controller = fxmlLoader.getController();
            controller.initData(file);
        }
    }

    /**
     * Empties all items in trash
     */
    @FXML
    public void emptyTrash(){
        ImageController myController = new ImageController();
        myController.emptyTrash();
        refresh();

    }

    /**
     * Deletes an item from the ImageController when the user clicks the delete button.
     */
    public void deleteItem(){

        for(String item: selected.getItems()){
            for(ImageModel image: ImageController.getImageCollection()){
                if(image.getImageLocation().equals(item)){
                    ImageController.getItemsInTrash().add(image);
                    String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date());
                    MasterLog.add(new Entry("Moved " + item + " to trash", timeStamp));
                }
            }
        }

        for(ImageModel image: ImageController.getItemsInTrash()){
            ImageController.getImageCollection().remove(image);
        }
        listViewer.getItems().clear();
        refresh();
    }
    /**
     * Views the trash folder which contains all recently deleted files to the user.
     */
    public void viewTrash(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Trash.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = (Stage) searchText.getScene().getWindow();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Switches scene from the HomeWindow to the CreateTag window when the user clicks the create tag button
     * @throws Exception: This handles the IOException thrown while loading fxmlLoader.load()
     */
    @FXML
    public void createTag() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateTag.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root1));
        stage.setTitle("Create Tags");
        stage.show();
    }

    /**
     * Deletes tags from the ImageModel
     * @throws Exception: This handles the IOException thrown while loading the fxmlLoader.load()
     */
    @FXML
    public void deleteTag() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DeleteMasterTags.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root1));
        stage.setTitle("Delete Tags");
        stage.show();
    }

    /**
     * Creates a masterLog of all tags created and switches the scene from the Home to the MasterLog scene.
     */
    @FXML
    public void masterLog(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MasterLog.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = (Stage) searchText.getScene().getWindow();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void viewAllTags() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AllTags.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root1));
        stage.show();
    }

}

