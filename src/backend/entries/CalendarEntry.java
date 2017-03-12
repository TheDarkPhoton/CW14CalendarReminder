package backend.entries;

import backend.Entry;

/**
 * Created by dovydas on 11/03/17.
 */
public class CalendarEntry extends Entry {

    @Override
    public String toString() {
        return "Event: " + _details + " | Date: " + _date + " | Time: " + _time + " | Location: " + _location;
    }
}
