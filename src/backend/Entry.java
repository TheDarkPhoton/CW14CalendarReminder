package backend;

import java.io.Serializable;
import java.util.Observable;

/**
 * Created by dovydas on 11/03/17.
 */
public abstract class Entry extends Observable implements Serializable {

    protected String _details = "";
    protected String _date = "---";
    protected String _time = "---";
    protected String _location = "---";

    public void setDetails(String details) {
        _details = details;
        setChanged();
    }

    public void setDate(String date) {
        _date = date;
        setChanged();
    }

    public void setTime(String time) {
        _time = time;
        setChanged();
    }

    public void setLocation(String location) {
        _location = location;
        setChanged();
    }
}
