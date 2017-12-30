package Views;

import javafx.beans.property.SimpleStringProperty;
import photomanager.Entry;

/**
 * Converts Entry to an acceptable type for the tableView in MastLogWindowController.
 */
public class EntryToProperty {
    /**
     * The message property of this EntryToProperty object
     */
    private SimpleStringProperty message;

    /**
     * The timeStamp property of this EntryToProperty object
     */
    private SimpleStringProperty timeStamp;

    /**
     * Initializes the EntryToProperty
     * @param entry Entry to be converted to a suitable type for MasterLogWindowController
     */
    EntryToProperty(Entry entry) {
        this.message = new SimpleStringProperty(entry.getMessage());
        this.timeStamp = new SimpleStringProperty(entry.getTimeStamp());
    }


    /**
     * Gets the entry's message
     *
     * @return Entry's message
     */
    public String getMessage() {
        return message.get();
    }

    /**
     * Gets the entry's timeStamp
     *
     * @return: Entry's timeStamp
     */
    public String getTimeStamp() {
        return timeStamp.get();
    }

}
