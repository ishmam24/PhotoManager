package Views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import photomanager.Controllers.*;
import photomanager.Entry;
import photomanager.MasterLog;
import photomanager.Models.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class Main extends Application {

    /**
     * Starts the PhotoManager
     *
     */
    public void start(Stage primaryStage) throws Exception{
        final String dir = System.getProperty("user.dir");
        File f = new File(dir+"//imagesAndTags.ser");
        if(f.exists() && !f.isDirectory()) {
            try {
                FileInputStream fis=new FileInputStream(dir+"//imagesAndTags.ser");
                ObjectInputStream ois=new ObjectInputStream(fis);

                @SuppressWarnings("unchecked")
                ArrayList<Object> woi= (ArrayList<Object>)ois.readObject();
                @SuppressWarnings("unchecked")
                ArrayList<ImageModel> savedImages = (ArrayList<ImageModel>) woi.get(0);
                ImageController.getImageCollection().addAll(savedImages);
                @SuppressWarnings("unchecked")
                ArrayList<TagModel> savedTags = (ArrayList<TagModel>) woi.get(1);
                TagController.getTagCollection().addAll(savedTags);
                @SuppressWarnings("unchecked")
                ArrayList<ImageModel> savedTrashItems = (ArrayList<ImageModel>) woi.get(2);
                ImageController.getItemsInTrash().addAll(savedTrashItems);
                @SuppressWarnings("unchecked")
                ArrayList<Entry> savedMasterLog = (ArrayList<Entry>) woi.get(3);
                MasterLog.getMasterLog().addAll(savedMasterLog);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        primaryStage.setTitle("Photo Manager");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    /**
     * Stops the PhotoManager
     */
    public void stop(){
        SaveObject saveFile = new SaveObject();
        saveFile.run();
    }

    /**
     * Launches the application
     * @param args: Arguments that launch the application
     */
    public static void main(String[] args) {
        launch(args);
    }


}
