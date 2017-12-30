package photomanager.Controllers;

import photomanager.Models.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class that contains methods for working with TagModels .
 */
public class TagController implements Serializable{

    /*
    ArrayList to store all tags that have been created by the user.
     */
    private static ArrayList<TagModel> tagCollection = new ArrayList<>();

    /**
     * Adds a tag to an ImageModel.
     *
     * @param tag: The tag the user wishes to add.
     * @param image: The image the user wishes to add the tag to.
     *
     */
    public void addTag(TagModel tag, ImageModel image){
        ArrayList<TagModel> tags = ImageController.parseTags(image);

        for (TagModel t : tags) {
            if (!this.containsInTagCollection(t)) {
                tagCollection.add(t); //Add tags already in image.
            }
        }

        if(image.getTagHistory().size() == 0 || !image.getTagHistory().get(image.getTagHistory().size() - 1).contains(tag)){
            String newName = image.getAbsoluteName() + tag.toString() + image.getExtension(); //Add new tag before ext.
            image.setImageName(newName);
            ArrayList<TagModel> newSet = ImageController.parseTags(image);
            image.addToTagHistory(newSet); // Add the new set of tags to the image's tagHistory

        }

        if(!this.containsInTagCollection(tag)){
            tagCollection.add(tag); //Add to tagCollection if tag was not already there

        }
    }

    /**
     * Returns true iff TagModel tag has already been created before. ThTagModelis checks if tag already exists.
     *
     * @param tag The tag the user wishes to check if it already exists.
     * @return True iff the tag has already been created before.
     *
     */
    boolean containsInTagCollection(TagModel tag){
        boolean contains = false;

        for(TagModel t: tagCollection){
            if(t.getTagName().equals(tag.getTagName())){
                contains = true;
            }
        }
        return contains;
    }

    /**
     * Deletes a tag from an ImageModel iff ImageModel contains the tag.
     * @param tag: The tag the user wishes to remove.
     * @param image: The ImageModel the user wishes to remove tag from.
     *
     */
    public void deleteTag(TagModel tag, ImageModel image){
        String tagName = tag.getTagName();
        String imageName = image.getImageName();
        if(imageName.contains(tagName)){
            String newName = imageName.replace(" @" + tagName, "");
            image.setImageName(newName);
        }
    }

    /**
     *  Reverts an ImageModel's tags back to a specific set of tags that was once associated with the ImageModel
     * @param image The imageModel the user wishes to revert.
     * @param tagList A collection of tags the user wishes to revert to.
     *
     */
    public void revertTag(ImageModel image, ArrayList<TagModel> tagList){
        StringBuilder tagString = new StringBuilder("");
        for(TagModel tag: tagList){
            tagString.append(tag.toString());
        }

        String combinedTagString = tagString.toString();
        image.setImageName(image.getNameBeforeTags() + combinedTagString + image.getExtension());

    }

    /**
     * Returns the collection of all tags that have been created in the program.
     * @return The collection of all tags that have been created.
     *
     */
    public static ArrayList<TagModel> getTagCollection(){
        return TagController.tagCollection;
    }

    public void removeTagFromCollection(TagModel tag){
        tagCollection.removeIf(tagModel -> tagModel.getTagName().equals(tag.getTagName()));
    }

}