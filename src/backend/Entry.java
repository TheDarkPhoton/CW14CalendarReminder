package backend;

import java.util.Observable;

/**
 * Created by dovydas on 11/03/17.
 */
public abstract class Entry extends Observable {

    protected String _details = "";
    protected String _date = "---";
    protected String _time = "---";
    protected String _location = "---";

    public void setDetails(String details) {
        _details = details;
    }

    public void setDate(String date) {
        if (_date.equals("---")) _date = date;
    }

    public void setTime(String time) {
        if (_time.equals("---")) _time = time;
    }

    public void setLocation(String location) {
        if (_location.equals("---")) _location = location;
    }
}
