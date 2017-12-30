package Views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import photomanager.Controllers.ImageController;
import photomanager.Entry;
import photomanager.MasterLog;
import photomanager.Models.ImageModel;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class TrashWindowController implements Initializable{

    @FXML
    private ListView<String> listView;

    private ListView<String> selected = new ListView<>();

    // Refreshes the content in listViewer each time accessing this Window
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.getSelectionModel().selectedItemProperty().addListener((obs,ov,nv)->
                selected.setItems(listView.getSelectionModel().getSelectedItems()));
        refresh();
    }

    /**
     * Go back to Home Screen
     * @param event ActionEvent
     */
    @FXML
    public void goBack(ActionEvent event){
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
     * Reverts Items in the main Screen
     */
    @FXML
    public void revertItem(){
        for(String item: selected.getItems()){
            for(ImageModel image: ImageController.getItemsInTrash()){
                if(image.getImageLocation().equals(item)){
                    ImageController.getImageCollection().add(image);
                }
            }
            ImageController.getItemsInTrash().removeIf(imageModel -> imageModel.getImageLocation().equals(item));
            String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date());
            MasterLog.add(new Entry("Recovered " + item + " from trash", timeStamp));
        }
        refresh();
    }

    /**
     * Permanently delete selected items from the listViewer
     */
    @FXML
    public void deleteItem(){

        for(String item: selected.getItems()){
            ImageController.getItemsInTrash().removeIf(imageModel -> imageModel.getImageLocation().equals(item));
            String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date());
            MasterLog.add(new Entry("Deleted " + item + " from trash", timeStamp));
        }
        refresh();
    }

    /**
     * Empties entire items in trash
     */
    @FXML
    public void emptyTrash(){
        ImageController myController = new ImageController();
        myController.emptyTrash();
        refresh();

    }

    /**
     * Refreshes the window and data.
     */
    private void refresh(){
        try{
            listView.getItems().clear();
            //Repopulate ListView
            for(ImageModel  item: ImageController.getItemsInTrash()){
                listView.getItems().add(item.getImageLocation());
            }
        }catch (Exception e){
            listView.getItems().clear();
            //Repopulate ListView
            for(ImageModel  item: ImageController.getItemsInTrash()){
                listView.getItems().add(item.getImageLocation());
            }
        }
        if(listView.getItems().isEmpty()){
            listView.getItems().add("List is Empty");
        }
    }

}
