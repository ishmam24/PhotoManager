package Views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import photomanager.Entry;
import photomanager.MasterLog;

import java.net.URL;
import java.util.ResourceBundle;

public class MasterLogWindowController implements Initializable{
    /**
     * TableView containing objects of class Entry
     */

    @FXML
    private TableView<EntryToProperty> tableView;

    /**
     * TableColumn containing String message from objects of Entry
     */

    @FXML
    private TableColumn<EntryToProperty, String> message;

    /**
     * TableColumn containing String timeStamp from objects of Entry
     */

    @FXML
    private TableColumn<EntryToProperty, String> timeStamp;


    /**
     * Initializes the file's data and populates the table by individually populating each column
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        message.setCellValueFactory(new PropertyValueFactory<> ("message"));
        timeStamp.setCellValueFactory(new PropertyValueFactory<>("timeStamp"));
        ObservableList<EntryToProperty> lst = FXCollections.observableArrayList();
        message.setCellFactory(col -> {
            return new TableCell<EntryToProperty, String>() {

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
        });
        for (Entry entry : MasterLog.getMasterLog()){
            lst.addAll(FXCollections.observableArrayList(new EntryToProperty(entry)));
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
            FXMLLoader fxmlLoader = new FXMLLoader ( getClass ().getResource ( "Home.fxml" ) );
            Parent root1 = fxmlLoader.load ();
            Stage stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
            stage.setScene ( new Scene( root1 ) );
            stage.show ();
        } catch (Exception e) {
            System.out.println ( "Cannot open window" );
        }
    }
}
