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

    public void setDetails(String details) {
        if (_details.equals("---")) _details = details;
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

    @Override
    public String toString() {
        return "Event: " + _details + " | Date: " + _date + " | Time: " + _time + " | Location: " + _location;
    }
}
