package photomanager.Models;


import photomanager.Controllers.TagController;
import java.io.Serializable;

/**
 * A representation of a tag as a TagModel.
 *
 */
public class TagModel implements Serializable{

    private String tagName;


    /**
     * Creates a Tag with timestamp of current time.
     * @param tag Name of this tag.
     *
     */
    public TagModel(String tag){
        this.tagName = tag;
    }


    /**
     * Returns this TagModel's name.
     * @return This Tag's name.
     *
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * Returns true iff name of tags is equal.
     * @param obj TagModel
     * @return Boolean
     *
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof TagModel){
            TagModel otherTag = (TagModel) obj;
            return otherTag.getTagName().equals(this.tagName);
        }
        else{
            return false;
        }
    }

    /**
     * Returns a String representation of tag.
     * @return String representation of tag.
     *
     */
    @Override
    public String toString() {
        return " @" + this.tagName;
    }
}
