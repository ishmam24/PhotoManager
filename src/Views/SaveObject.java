package Views;

import photomanager.Controllers.ImageController;
import photomanager.Controllers.TagController;
import photomanager.Entry;
import photomanager.MasterLog;
import photomanager.Models.ImageModel;
import photomanager.Models.TagModel;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * https://stackoverflow.com/questions/41343295/how-create-multiple-serialization
 * Class to serialize the images
 */

class SaveObject implements Serializable  {

    /**
     * Method that initializes the ArrayLists that store ImageModels and TagModels
     */

    void run(){
        SaveObject obj = new SaveObject();
        List<ImageModel> saveImages = new ArrayList<>(ImageController.getImageCollection());
        List<TagModel> saveTags = new ArrayList<>(TagController.getTagCollection());
        List<ImageModel> saveTrash = new ArrayList<>(ImageController.getItemsInTrash());
        List<Entry> saveMasterLog = new ArrayList<>(MasterLog.getMasterLog());
        obj.serializeImages(saveImages, saveTags, saveTrash, saveMasterLog);
    }

    /**
     * Serializes the Images and its tags
     * @param images: ArrayList of ImageModels
     * @param tags: ArrayList of TagModels
     */

    private void serializeImages(List<ImageModel> images, List<TagModel> tags, List<ImageModel> trashItems, List<Entry> masterLog) {

        FileOutputStream fOut = null;
        ObjectOutputStream oos = null;
        ArrayList<Object> woi = new ArrayList<>();

        try {
            final String dir = System.getProperty("user.dir");
            fOut = new FileOutputStream(dir+"//imagesAndTags.ser");
            oos = new ObjectOutputStream(fOut);
            woi.add(images);
            woi.add(tags);
            woi.add(trashItems);
            woi.add(masterLog);
            oos.writeObject(woi);

        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {

            if (fOut != null) {
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
