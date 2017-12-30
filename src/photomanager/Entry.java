package photomanager;

import java.io.Serializable;

public class Entry implements Serializable {
    private String message;

    private String timeStamp;

    public Entry(String message, String timeStamp){
        this.message = message;
        this.timeStamp = timeStamp;
    }
    public String getMessage(){
        return this.message;
    }

    public String getTimeStamp(){
        return this.timeStamp;
    }

}
