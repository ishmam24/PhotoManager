package Views;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import photomanager.Controllers.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import photomanager.Entry;
import photomanager.MasterLog;
import photomanager.Models.ImageModel;
import photomanager.Models.TagModel;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller for Tag History window.
 */

public class TagHistoryController implements Initializable{

    /**
     * file object File whose data we are interested in
     */

    private File file;

    /**
     * A TagController
     */

    private TagController tagController = new TagController();

    /**
     * An ArrayList containing tag names.
     */

    private ArrayList<String> tagNames = new ArrayList<>();

    /**
     * An ImageController
     */

    private ImageController imageController = new ImageController();

    /**
     * FXML button to revert changes
     */

    @FXML
    private Button revertButton;

    /**
     * FXML list that contains history of tags associated with the image
     */

    @FXML
    private ListView<String> listView;


    /**
     * Initializes the scene
     * @param location: the location of the URL
     * @param resources: resources used for initialization
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.getItems().add("Empty/ No Tags");
    }


    /**
     * Initializes the file's data and populates the ListView listView
     * @param file: The file whose data we want
     */

    void initData(File file){

        this.file = file;

        for(ImageModel item: ImageController.getImageCollection()){
            if(item.getImageName().equals(file.getName())){
                for(ArrayList<TagModel> history : item.getTagHistory()){
                    StringBuilder oneTagSnapShot = new StringBuilder("");
                    for(TagModel tag : history){
                        oneTagSnapShot.append(tag.toString());
                    }

                    if(! listView.getItems().contains(oneTagSnapShot.toString())){
                        if(!oneTagSnapShot.toString().equals("")){
                            listView.getItems().add(oneTagSnapShot.toString());
                        }
                    }
                    tagNames.add(oneTagSnapShot.toString());
                }
            }
        }
        revertButton.setDisable(true);
    }

    /**
     * Method for when the user clicks a set of tags.
     */
    @FXML
    public void tagClicked() {
        revertButton.setDisable(false);
    }

    /**
     * Reverts the tags of the image to a previous set of tags
     * @param event: The event which activates the call for this method
     */

    @FXML
    public void revertToTag(ActionEvent event) throws Exception{
        ImageModel img = imageController.findImage(file.getAbsolutePath());
        String tag = listView.getSelectionModel().getSelectedItem();
        ArrayList<TagModel> tags = new ArrayList<>();
        //Checks if user chose the same tag set as the current one
        System.out.println(img.getTagHistory().size());
        if (img.getTagHistory().size() != 0){
            if(!tags.equals(img.getTagHistory().get(img.getTagHistory().size() - 1))){
                if(tag.equals("Empty/ No Tags")){
                    tag = "";
                }
                tag = tag.replaceAll("\\s+","");
                Pattern pattern = Pattern.compile("@[^@]*");
                Matcher matcher = pattern.matcher(tag);

                while (matcher.find()) {
                    String group = matcher.group();
                    tags.add(new TagModel(group.substring(1, group.length())));
                }

                String tagString = "";
                for (TagModel t : tags){
                    tagString += t.toString(); //Create string containing the new set of tags
                }
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date());
                MasterLog.add(new Entry("Reverted " + img.getImageLocation() + " tag set to [" + tagString + "]",
                        timeStamp));

                tagController.revertTag(img, tags);


                ArrayList<TagModel> newSet = ImageController.parseTags(img);
                img.addToTagHistory(newSet);

                this.file = new File(img.getImageFile().getAbsolutePath());

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Image.fxml"));
                Parent root1 = fxmlLoader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root1));
                stage.show();

                ImageWindowController controller = fxmlLoader.getController();
                controller.initData(this.file);
            }
            else{
                System.out.println("Cant revert to same tagSet");
            }
        }
        else{
            System.out.println("Cant revert to same tagSet");
        }

        }

    /**
     * Connects to the back button in the scene to take the user back to the previous scene
     * @param event: The event which activates the call for this method
     */
    @FXML
    public void backButton(ActionEvent event) throws Exception{
        returnToBackScreen(event);
    }

    /**
     * Method used to return to the previous scene
     * @param event: The event which activates the call for this method
     */

    private void returnToBackScreen(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Image.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root1));
        stage.show();

        ImageWindowController controller = fxmlLoader.getController();
        controller.initData(this.file);
    }
}


