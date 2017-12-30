package Views;

import javafx.beans.property.SimpleStringProperty;


/**
 * Helper class implemented in ImageHistoryController and used in viewing data in the Table
 */

public class NameHistory {

    /**
     * Contains the old name of the Image
     */

    private final SimpleStringProperty oldName;

    /**
     * Contains the new name of the Image
     */

    private final SimpleStringProperty newName;

    /**
     * Contains the timeStamp (when the image's name was changed from oldName to newName)
     */

    private final SimpleStringProperty timeStamp;


    /**
     * Initializes the oldName, newName and the timeStamp of the image
     * @param oldName: Old name of the image
     * @param newName: New name of the image
     * @param timeStamp: Time at which name was changed from oldName to newName
     */

    NameHistory(String oldName, String newName, String timeStamp) {
        super();
        this.oldName = new SimpleStringProperty(oldName);
        this.newName = new SimpleStringProperty(newName);
        this.timeStamp = new SimpleStringProperty(timeStamp);
    }

    /**
     * Gets the oldName of the image
     * @return: old name of the image
     */

    public String getOldName() {
        return oldName.get();
    }

    /**
     * Gets the newName of the image
     * @return: new name of the image
     */

    public String getNewName() {
        return newName.get();
    }

    /**
     * Gets the timeStamp of the image
     * @return: time stamp of the image
     */

    public String getTimeStamp() {
        return timeStamp.get();
    }
}
