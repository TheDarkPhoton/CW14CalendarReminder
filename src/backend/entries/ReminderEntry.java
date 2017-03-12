package backend.entries;

import backend.Entry;

/**
 * Created by dovydas on 11/03/17.
 */
public class ReminderEntry extends Entry {

    @Override
    public void setDetails(String details) {
        _details = details.replace("remind me to", "").trim();
        setChanged();
    }

    @Override
    public String toString() {
        String result = _details;

        if (!_date.equals("---"))
            result += " | Date: " + _date;

        if (!_time.equals("---"))
            result += " | Time: " + _time;

        if (!_location.equals("---"))
            result += " | Location: " + _location;

        return result;
    }
}
