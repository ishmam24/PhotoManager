package photomanager;

import java.io.Serializable;
import java.util.ArrayList;

public class MasterLog implements Serializable{
    private static ArrayList<Entry> masterLogArray = new ArrayList<>();

    public static ArrayList<Entry> getMasterLog(){
        return masterLogArray;
    }

    public static void add(Entry entry){
        masterLogArray.add(entry);
    }

}
