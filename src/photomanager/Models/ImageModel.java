package photomanager.Models;

import photomanager.Entry;
import photomanager.MasterLog;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A representation of an image as an ImageModel.
 *
 */
public class ImageModel implements Serializable{

    /**
     * String storing the name of the ImageModel
     *
     */
    private String imageName;

    /**
     * String storing the location of the ImageModel
     *
     */
    private String imageLocation;

    /**
     * ArrayList of Array[3] of Strings storing all the previous names of the ImageModel
     * In the Array, index 0 is old name, index 1 is new name and index 2 is time stamp
     *
     */
    private ArrayList<String[]> nameHistory = new ArrayList<>();

    /**
     * ArrayList of ArrayLists of TagModels storing all the TagModels in an ImageModel at a particular time. So whenever
     * the user updates the tags of an image, a new ArrayList containing the current tags associated with the image can
     * be added to tagHistory
     *
     */
    private ArrayList<ArrayList<TagModel>> tagHistory = new ArrayList<>();

    /**
     * file object File whose data we are interested in
     *
     */
    private File imageFile;

    /**
     * Constructs a new ImageModel with name and location.
     * @param name The name of this image.
     * @param location The location of this image.
     *
     */
    public ImageModel(String name, String location){
        this.imageName = name;
        this.imageLocation = location;
        this.imageFile = new File(location);
    }

    /**
     * Returns the file associated with this image.
     * @return file associated with this image.
     *
     */
    public File getImageFile(){
        return this.imageFile;
    }

    /**
     * Sets the file associated with the image to newFile.
     *
     */
    public void setImageFile(File newFile){
        this.imageFile = newFile;
    }

    /**
     * Gets the name of this image.
     * @return This image's name.
     *
     */
    public String getImageName(){
        return this.imageName;
    }

    /**
     * Sets the name of this image to String name. This is used to add tags to the imageName.
     * @param name New name of this image.
     *
     */
    public void setImageName(String name){
        File oldFile = new File(imageFile.getAbsolutePath());
        String oldName = this.imageName;
        this.imageLocation = this.imageLocation.replace(this.imageName, name);
        this.imageName = name;
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date());
        String[] entry= {oldName, name, timeStamp};
        addToNameHistory(entry);
        MasterLog.add(new Entry("Renamed " + oldFile.getAbsolutePath() + " to " + imageLocation, timeStamp));



        String newFilePath = this.imageLocation;

        File newFile = new File(newFilePath);
        boolean isSuccess = oldFile.renameTo(newFile);
        if(isSuccess){
            this.imageFile = new File(newFilePath);
        }
    }

    /**
     * Sets the location of image to new location.
     * @param location New location of this image.
     *
     */
    public void setImageLocation(String location){
        this.imageLocation = location;
    }


    /**
     * Gets the location of this image.
     * @return String representation of the location of this image.
     *
     */
    public String getImageLocation(){
        return imageLocation;
    }


    /**
     * Gets the ArrayList tagHistory containing an ArrayList of TagModels.
     * @return ArrayList of
     */
    public ArrayList<ArrayList<TagModel>> getTagHistory(){
        return this.tagHistory;
    }


    /**
     * Adds an ArrayList containing the updated set of TagModels associated with the image to tagHistory every time a
     * new tag is added or deleted.
     * @param tags Tag revision to be added.
     *
     */
    public void addToTagHistory(ArrayList<TagModel> tags){
        this.tagHistory.add(tags);
    }


    /**
     * Adds an entry (Array[3] containing newName, oldName and timeStamp) to nameHistory every time a image is renamed.
     * @param name Entry of new image name.
     *
     */
    private void addToNameHistory(String[] name){
        this.nameHistory.add(name);
    }


    /**
     * Returns the history of all names this image has had (stored in ArrayList nameHistory).
     * @return List of all names.
     *
     */
    public ArrayList<String[]> getNameHistory(){
        return this.nameHistory;
    }


    /**
     * Returns string representations of the ImageModel
     * @return The String name of the ImageModel.
     */
    @Override
    public String toString() {
        return getImageName();
    }


    /**
     * Returns the name of this ImageModel without file extension.
     * @return Absolute name of image.
     *
     */
    public String getAbsoluteName(){
        String absoluteName = imageName;
        int extensionStart = imageName.lastIndexOf(".");
        if(extensionStart >=0 ){
            absoluteName = imageName.substring(0, extensionStart);
        }
        return absoluteName;
    }


    /**
     *
     * Precondition: The ImageModel has a tag.
     *
     * Returns the name of the ImageModel without tags and extension.
     * @return The name of this ImageModel without tags or extension
     *
     */
    public String getNameBeforeTags(){
        if(imageName.contains(" @")){
            int indexOfFirstTag = imageName.indexOf(" @");
            return imageName.substring(0, indexOfFirstTag);
        }
        return this.getAbsoluteName();
    }


    /**
     * Returns the extension of this ImageModel.
     * @return The extension on this ImageModel file.
     *
     */
    public String getExtension(){
        String extension = imageName;
        int extensionStart = imageName.lastIndexOf(".");
        if(extensionStart >= 0){
            extension = imageName.substring(extensionStart, imageName.length());
        }
        return extension;
    }

    @Override
    public boolean equals(Object obj)
    {
        return (obj instanceof ImageModel) && (this.getImageName().equals(((ImageModel) obj).getImageName()))
                && (this.getImageLocation().equals(((ImageModel) obj).getImageLocation()));
    }
}
