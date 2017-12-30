package Views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.text.SimpleDateFormat;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import photomanager.Controllers.*;
import photomanager.Entry;
import photomanager.MasterLog;
import photomanager.Models.ImageModel;

/**
 * Controller for the image window.
 */
public class ImageWindowController{

    private ImageController imageController = new ImageController();

    /**
     * FXML imageView that views the image.
     *
     */
    @FXML
    ImageView imageView = new ImageView();

    /**
     * FXML button that shows the Image in its directory.
     *
     */
    @FXML
    Button openInDirectoryButton;

    /**
     * FXML Label imageName that labels the scene.
     */
    @FXML
    Label imageName = new Label();

    /**
     * FXML button that takes the user back to the previous scene.
     */
    @FXML
    Button back = new Button();

    /**
     * FXML button that allows the user to move the image from one directory to another.
     */
    @FXML
    Button moveImageButton = new Button();

    /**
     *  file object File whose data we are interested in
     */

    private File file;

    /**
     * Initializes the file's data which is used in subsequent parts of the scene.
     * @param file: The file whose data we want
     *
     */
    void initData(File file){
        this.file = file;
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        imageController.initializeImage(file);
        imageName.setText(file.getAbsolutePath());
    }

    /**
     * Allows the user to move an image from one directory to another, activated by the moveImageButton,
     *
     */
    @FXML
    public void moveImageAction(){
        ImageModel thisImage = imageController.findImage(this.file.getAbsolutePath());
        DirectoryChooser fc = new DirectoryChooser();
        File selectedDirectory = fc.showDialog(imageName.getScene().getWindow());
        if (selectedDirectory != null){
            String oldPath = thisImage.getImageLocation();
            String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date());
            MasterLog.add(new Entry("Moved " + oldPath + " to " + selectedDirectory.getAbsolutePath(),
                    timeStamp));
            imageController.move(thisImage, selectedDirectory.getAbsolutePath());
            this.file = new File(thisImage.getImageLocation());
            imageName.setText(thisImage.getImageLocation()); // Update the image label



        }
    }

    /**
     * Adds a tag to the ImageModel
     *
     * @param event: The event which activates the call for this method.
     * @throws Exception: handles the IOException thrown while loading the Parent root
     */
    @FXML
    public void addTag(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddTags.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root1));
        stage.show();

        AddTagsWindowController controller = fxmlLoader.getController();
        controller.initData(file);
    }

    /**
     * Deletes a tag from the ImageModel
     *
     * @param event: The event which activates the call for this method.
     * @throws Exception: handles the IOException thrown while loading the Parent root
     */
    @FXML
    public void deleteTag(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DeleteTags.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root1));
        stage.show();

        DeleteTagsWindowController controller = fxmlLoader.getController();
        controller.initData(file);
    }

    /**
     * Opens the ImageModel in its parent directory on the file explorer of the OS.
     *
     */
    @FXML
    public void openImageInDirectory(){
        for(ImageModel img: ImageController.getImageCollection()){
            if(img.getImageLocation().equals(file.getAbsolutePath())){
                imageController.openInDirectory(img);
            }
        }
    }

    /**
     *  Connects to the back button in the scene to take the user back to the previous scene.
     *  @param event: The event which activates the call for this method
     */
    @FXML
    public void backButton(ActionEvent event){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root1));
            stage.show();

            HomeWindowController controller = fxmlLoader.getController();
        }catch(Exception e){
            System.out.println("Cannot open window");
        }
    }

    /**
     * Activated by the tagHistoryButton, takes the user to a scene containing the history of tags associated with the
     * image.
     *
     * @param event: The event which activates the call for this method.
     * @throws Exception: handles the IOException thrown while loading the Parent root
     */
    @FXML
    public void tagHistoryButton(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TagHistory.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root1));
        stage.show();

        TagHistoryController controller = fxmlLoader.getController();
        controller.initData(file);
    }

    /**
     * Activated by the imageHistoryButton, takes the user to a scene containing the history of imageNames associated
     * with the image.
     * @param event: The event which activates the call for this method.
     * @throws Exception: handles the IOException thrown while loading the Parent root
     */
    @FXML
    public void imageHistoryButton(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ImageHistory.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root1));
        stage.show();

        ImageHistoryController controller = fxmlLoader.getController();
        controller.initData(file);
    }
}
