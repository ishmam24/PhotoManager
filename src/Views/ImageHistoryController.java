package Views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import photomanager.Controllers.ImageController;
import photomanager.Models.ImageModel;
import java.io.File;

/**
 * Controller for ImageHistory.fxml.
 */
public class ImageHistoryController {

    /**
     * file object File whose data we are interested in
     */

    public File file;

    /**
     * FXML button to return to previous scene
     */

    @FXML
    public Button backButton;

    /**
     * TableView containing objects of class NameHistory
     */

    @FXML
    private TableView<NameHistory> tableView;

    /**
     * TableColumn containing String oldName from objects of NameHistory
     */

    @FXML
    private TableColumn<NameHistory, String> oldName;

    /**
     * TableColumn containing String newName from objects of NameHistory
     */

    @FXML
    private TableColumn<NameHistory, String> newName;

    /**
     * TableColumn containing String timeStamp from objects of NameHistory
     */

    @FXML
    private TableColumn<NameHistory, String> timeStamp;


    /**
     * Initializes the file's data and populates the table by individually populating each column
     * @param file: The file whose data we want
     */
    void initData(File file) {
        this.file = file;
        oldName.setCellValueFactory(new PropertyValueFactory<>("oldName"));
        newName.setCellValueFactory(new PropertyValueFactory<> ("newName"));
        timeStamp.setCellValueFactory(new PropertyValueFactory<>("timeStamp"));
        newName.setCellFactory(col -> {
            TableCell<NameHistory, String> cell = new TableCell<NameHistory, String>() {

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(null);
                        TextArea textArea = new TextArea();
                        textArea.setPrefColumnCount(4);
                        textArea.setPrefRowCount(1);
                        textArea.setText(item);
                        setGraphic(textArea);
                    }

                }

            };
            return cell;
        });
        oldName.setCellFactory(col -> {
            TableCell<NameHistory, String> cell = new TableCell<NameHistory, String>() {

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(null);
                        TextArea textArea = new TextArea();
                        textArea.setPrefColumnCount(4);
                        textArea.setPrefRowCount(1);
                        textArea.setText(item);
                        setGraphic(textArea);
                    }

                }

            };
            return cell;
        });
        ObservableList<NameHistory> lst = FXCollections.observableArrayList();
        for (ImageModel item: ImageController.getImageCollection()){
            if (item.getImageName().equals(file.getName())) {
                for (String[] imageHistory : item.getNameHistory ()){
                    lst.addAll(FXCollections.observableArrayList(new NameHistory(imageHistory[0], imageHistory[1], imageHistory[2])));
                }

            }
        }

        tableView.setItems(lst);
    }


    /**
     *  Connects to the back button in the scene to take the user back to the previous scene.
     *  @param event: The event which activates the call for this method
     */
    @FXML
    public void returnToBackScreen(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader ( getClass ().getResource ( "Image.fxml" ) );
            Parent root1 = fxmlLoader.load ();
            Stage stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
            stage.setScene ( new Scene ( root1 ) );
            stage.show ();

            ImageWindowController controller = fxmlLoader.getController ();
            controller.initData ( file );

        } catch (Exception e) {
            System.out.println ( "Cannot open window" );
        }
    }

}


