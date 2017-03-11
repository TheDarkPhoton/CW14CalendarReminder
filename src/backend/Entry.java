package backend;

import java.util.Observable;

/**
 * Created by dovydas on 11/03/17.
 */
public abstract class Entry extends Observable {

    private String _details = "";
    private String _date = "---";
    private String _time = "---";
    private String _location = "---";

    @Override
    public String toString() {
        return "Event: " + _details + " | Date: " + _date + " | Time: " + _time + " | Location: " + _location;
    }
}
