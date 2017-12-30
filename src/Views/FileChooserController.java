package Views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javafx.event.ActionEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.Stage;
import photomanager.Controllers.ImageController;
import photomanager.Entry;
import photomanager.MasterLog;

/**
 * Controller for the file chooser window
 * Source: https://www.youtube.com/watch?v=hNz8Xf4tMI4
 */
public class FileChooserController {

    @FXML
    private ListView<String> listView;

    private ImageController myImageController = new ImageController();

    /**
     * Connects to the back button in the scene to take the user back to the previous scene
     * @param event: The event which activates the call for this method
     *
     */
    @FXML
    private void backButton(ActionEvent event){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root1));
            stage.show();

            HomeWindowController controller = fxmlLoader.getController();
            controller.refresh();

        }catch(Exception e){
            System.out.println("Cannot open window");
        }
    }

    /**
     * Opens the JavaFX file chooser window and adding the files the user selects to the PhotoManager.
     * This method also calls ImageController.initializeImage() to make the selected files ImageModels
     * and add them to ImageController.imageCollection.
     *
     */
    @FXML
    private void ImageButtonAction(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.bmp", "*.jpeg",
                        "*.tiff", "*.gif", "*.png", "*.JPG", "*.BMP","*.JPEG", "*.TIFF", "*.GIF", "*.PNG"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(listView.getScene().getWindow());

        if (selectedFiles != null){
            for (File selectedFile : selectedFiles) {
                boolean isFound = myImageController.initializeImage(selectedFile);
                if (isFound) {

                    listView.getItems().add("You have already added " + selectedFile.getName() + " to the" +
                            " Photo Manager.");
                } else {
                    listView.getItems().add("Successfully added " + selectedFile.getName() + " to the " +
                            "Photo Manager.");
                    String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date());
                    MasterLog.add(new Entry("Added " + selectedFile.getName() + " to the Photo Manager",
                            timeStamp));

                }
            }

        }

    }

    /**
     * Opens the JavaFX directory chooser window and adding the files the user selects to the photomanager.
     * This method also calls ImageController.initializeImage() to make the selected files
     * ImageModels and add them to ImageController.imageCollection.
     *
     */
    @FXML
    private void DirectoryButtonAction(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(listView.getScene().getWindow());
        if (selectedDirectory != null){
            ArrayList<File> imageFiles = getImages(selectedDirectory, new ArrayList<>());
            for (File selectedFile : imageFiles) {
                boolean isFound = myImageController.initializeImage(selectedFile);
                if (isFound) {

                    listView.getItems().add("You have already added " + selectedFile.getName() + " to the" +
                            " Photo Manager.");
                } else {
                    listView.getItems().add("Successfully added " + selectedFile.getName() + " to the " +
                            "Photo Manager.");
                    String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date());
                    MasterLog.add(new Entry("Added " + selectedFile.getName() + " to the Photo Manager",
                            timeStamp));

                }
            }

        }
    }

    /**
     * Returns all image files in the directory and its sub-directories
     * Source: https://stackoverflow.com/questions/14676407/list-all-files-in-the-folder-and-also-sub-folders
     * @param directory A directory
     * @param imageFiles An ArrayList of all imageFiles found in the directory and it's sub-directories
     * @return An ArrayList of all image files in directory and all sub-directories
     *
     */
    private ArrayList<File> getImages(File directory, ArrayList<File> imageFiles){
        File[] fileList = directory.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                if (isImage(file)) {
                    imageFiles.add(file);
                } else if (file.isDirectory()) {
                    getImages(file, imageFiles);
                }
            }
        }
        return imageFiles;
    }

    /**
     * Checks if file is an Image
     * @param file A File
     * @return true if the file is an Image, false otherwise
     *
     */
    private boolean isImage(File file){
        String name = file.getName().toLowerCase();
        return name.endsWith("jpg") || name.endsWith("jpeg") || name.endsWith("bmp") || name.endsWith("tiff") || //
                name.endsWith("gif") || name.endsWith("png");
    }


}
