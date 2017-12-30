package photomanager.Controllers;

import photomanager.Entry;
import photomanager.MasterLog;
import photomanager.Models.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A class that contains methods for working with ImageModels.
 *
 */
public class ImageController implements Serializable {

    /*
     ArrayList to store all images that have been created by the user.
     */

    private static ArrayList<ImageModel> imageCollection = new ArrayList<>();

    /*
    ArrayList that stores Images deleted by the user.
     */
    private static ArrayList<ImageModel> itemsInTrash = new ArrayList<>();

    private TagController tagController = new TagController();

    /**
     * Converts a file to an instance of ImageModel.
     * @param file The file to be converted to an ImageModel.
     * @return A boolean to indicate if ImageModel has already been initialized in the past.
     *
     */
    public boolean initializeImage(File file){
        boolean found = false;
        for(ImageModel item: imageCollection){
            if(item.getImageLocation().equals(file.getAbsolutePath())){
                found = true;
            }
        }
        if(!found){
            ImageModel img = new ImageModel(file.getName(), file.getAbsolutePath());
            imageCollection.add(img);
            ArrayList<TagModel> tags = parseTags(img);
            for (TagModel t : tags) {
                if (!tagController.containsInTagCollection(t)) {
                    TagController.getTagCollection().add(t); //Add tags already in image.
                }
            }
        }

        return found;
    }

    /**
     * Returns an instance of ImageModel with the given location.
     * @param location The location of the ImageModel the user wishes to search for.
     * @return The instance of ImageModel with the given location.
     *
     */
    public ImageModel findImage(String location){
        ImageModel image = new ImageModel("","");
        for(ImageModel img: imageCollection){
            if(img.getImageLocation().equals(location)){
                image = img;
            }
        }
        return image;
    }

    /**
     * Moves an ImageModel to a new location on user's OS.
     * @param image The image the user wishes to move.
     * @param newLocation The new location the user wishes to move the ImageModel to.
     *
     * Source:
     * https://stackoverflow.com/questions/4645242/how-to-move-file-from-one-location-to-another-location-in-java
     */
    public void move(ImageModel image, String newLocation){
        String newPath = newLocation + "/" + image.getImageName();
        image.setImageLocation(newPath);
        boolean success = image.getImageFile().renameTo(new File(newPath));
        if(success){
            image.setImageFile(new File(image.getImageLocation()));
        }
    }

    /**
     * Opens the ImageModel in user's OS directory.
     *
     * @param image The ImageModel the user wishes to open.
     *
     * Sources: https://stackoverflow.com/questions/12339922/opening-finder-explorer-using-java-swing
     *          http://www.mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/
     *          https://askubuntu.com/questions/17062/how-to-open-a-directory-folder-and-a-url-through-terminal
     *
     */
    public void openInDirectory(ImageModel image){
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0 ){
            try {
                URL urlToFile = image.getImageFile().toURI().toURL(); //Converts the path to a linux path format
                Runtime.getRuntime().exec("nautilus " + urlToFile.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                Desktop.getDesktop().open(new File(image.getImageLocation().replace("/" + image.getImageName(),"")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Parses out all tags from an ImageModel.
     *
     * @param image ImageModel user wishes to parse tags from.
     * @return ArrayList<TagModel> Collection of all tags in ImageModel.
     *
     */
    public static ArrayList<TagModel> parseTags(ImageModel image){
        ArrayList<TagModel> tags = new ArrayList<>();
        String[] split = image.getImageName().split(" @");
        for (int i = 0; i < split.length; i++){
            if(i == split.length - 1){ //Remove the extension from the last item
                split[i] = split[i].substring(0, split[i].lastIndexOf(image.getExtension()));
            }
            if (i != 0){
                tags.add(new TagModel(split[i]));
            }
        }
        return tags;
    }

    /**
     * Returns the collection of all images that have been created.
     *
     * @return ArrayList<ImageModel> Collection of all images that have been created
     */
    public static ArrayList<ImageModel> getImageCollection() {
        return imageCollection;
    }

    /**
     * Returns the collection of all items in the trash.
     * @return ArrayList<ImageModel>
     */
    public static ArrayList<ImageModel> getItemsInTrash(){return itemsInTrash;}

    public void emptyTrash(){
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date());
        ImageController.getItemsInTrash().clear();
        MasterLog.add(new Entry("Emptied the trash", timeStamp));
    }

}

