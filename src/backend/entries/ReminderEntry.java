package backend.entries;

import backend.Entry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dovydas on 11/03/17.
 */
public class ReminderEntry extends Entry {
    private static Pattern pattern_reminder = Pattern.compile("[Rr]emind [Mm]e [Tt]o");

    @Override
    public void setDetails(String details) {
        Matcher match = pattern_reminder.matcher(details);
        while (match.find()) {
            String replace = match.group();
            details = details.replace(replace, "").trim();
        }

        _details = details;
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
