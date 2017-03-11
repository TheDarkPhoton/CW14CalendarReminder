package backend;

import backend.entries.CalendarEntry;
import backend.entries.EntryType;
import backend.entries.ReminderEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dovydas on 11/03/17.
 */
public class Processor implements MainController {
    private static String match_1st = "((([2-9](?<!1)1)|(?<!\\d)1)st)";
    private static String match_2nd = "((([2-9](?<!1)2)|(?<!\\d)2)nd)";
    private static String match_3rd = "((([2-9](?<!1)3)|(?<!\\d)3)rd)";
    private static String match_xth = "(\\d?((?<=1)\\d|[04-9])th)";
    private static String match_weekday = "(mon|tues|wednes|thurs|fri|satur|sun)day";
    private static String match_day = "(" + match_1st + "|" + match_2nd + "|" + match_3rd + "|" + match_xth + ")";
    private static String match_month = "(jan(uary)?|feb(ruary)?|mar(ch)?|apr(il)?|may|jun(e)?|jul(y)?|aug(ust)?|sep(tember)?|oct(ober)?|(nov|dec)(ember)?)";

    private static Pattern pattern_weekday = Pattern.compile(match_weekday);
    private static Pattern pattern_day = Pattern.compile("\\d\\d?");
    private static Pattern pattern_month = Pattern.compile(match_month);

    private static String match_date1 = "(" + match_weekday + " " + match_day + " " + match_month + ")";
    private static String match_date2 = "((([0-3](((?<!0)0)|((?<=[0-2])[1-9])|((?<=3)1)))|[1-9]) ?/ ?(([01](((?<!0)0)|((?<=0)[1-9])|((?<=1)[12])))|[1-9]) ?/ ?([2-9]\\d{3}))";
    private static String match_date3 = "(on " + match_weekday + ")";
    private static String match_date4 = "(next " + match_weekday + ")";

    //matches weekday day month format
    private static Pattern pattern_date1 = Pattern.compile(match_date1);
    //matches 01/02/2000, 1/2/2000 or any variant
    private static Pattern pattern_date2 = Pattern.compile(match_date2);
    //matches on weekday
    private static Pattern pattern_date3 = Pattern.compile(match_date3);
    //matches next weekday
    private static Pattern pattern_date4 = Pattern.compile(match_date4);

    private static String match_time1 = "([0-2]((?<!2)\\d|(?<=2)[0-3]) ?: ?[0-5]\\d)";
    private static String match_time2 = "(([01]((?<=0)\\d|[0-2])|(?<![01])\\d)(am|pm))";
    private static String match_time3 = "(evening)";
    private static String match_time4 = "(morning)";

    private static Pattern pattern_time1 = Pattern.compile(match_time1);
    private static Pattern pattern_time2 = Pattern.compile(match_time2);
    private static Pattern pattern_time3 = Pattern.compile(match_time3);
    private static Pattern pattern_time4 = Pattern.compile(match_time4);

    private static Pattern pattern_location = Pattern.compile("at \\w+");

    private MainView _view;

    private static final Map<String, Integer> month;
    static
    {
        month = new HashMap<>();
        month.put("january", 0);
        month.put("february", 1);
        month.put("march", 2);
        month.put("april", 3);
        month.put("may", 4);
        month.put("june", 5);
        month.put("july", 6);
        month.put("august", 7);
        month.put("september", 8);
        month.put("october", 9);
        month.put("november", 10);
        month.put("december", 11);

        month.put("jan", 0);
        month.put("feb", 1);
        month.put("mar", 2);
        month.put("apr", 3);
        month.put("may", 4);
        month.put("jun", 5);
        month.put("jul", 6);
        month.put("aug", 7);
        month.put("sep", 8);
        month.put("oct", 9);
        month.put("nov", 10);
        month.put("dec", 11);
    }

    private static final Map<String, Integer> weekday;
    static
    {
        weekday = new HashMap<>();
        weekday.put("saturday", 0);
        weekday.put("sunday", 1);
        weekday.put("monday", 2);
        weekday.put("tuesday", 3);
        weekday.put("wednesday", 4);
        weekday.put("thursday", 5);
        weekday.put("friday", 6);
    }

    private static String[] suffixes = {
            "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
            "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
            "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
            "th", "st",
    };

    private String getFormattedString(Calendar calendar){
        String result = new SimpleDateFormat("EEEE @ MMMM").format(calendar.getTime());
        int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
        result = result.replace("@", day_of_month + suffixes[day_of_month]);
        return result;
    }

    private void validateCalendar(Calendar c){
        try {
            c.getTime();
        } catch (IllegalArgumentException e) {
            int day = c.get(Calendar.DAY_OF_MONTH) - c.getMaximum(Calendar.DAY_OF_MONTH);
            if (day > 0) {
                c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
                c.set(Calendar.DAY_OF_MONTH, day);
            }

            int month = c.get(Calendar.MONTH) - c.getMaximum(Calendar.MONTH);
            if (month > 0) {
                c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
                c.set(Calendar.MONTH, month);
            }
        }
    }

    private String extractDate(Entry entry, String input){
        Matcher match_date1 = pattern_date1.matcher(input);
        while (match_date1.find()) {
            String match = match_date1.group();

            Calendar c = Calendar.getInstance();
            Matcher match_day = pattern_day.matcher(match);
            while (match_day.find()) {
                c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(match_day.group()));
            }

            Matcher match_month = pattern_month.matcher(match);
            while (match_month.find()) {
                c.set(Calendar.MONTH, month.get(match_month.group()));
            }

            input = input.replace(match, "").trim();
            entry.setDate(getFormattedString(c));
        }

        Matcher match_date2 = pattern_date2.matcher(input);
        while (match_date2.find()) {
            String match = match_date2.group();
            String[] parts = match.split(" ?/ ?");

            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[0]));
            c.set(Calendar.MONTH, Integer.parseInt(parts[1]));
            c.set(Calendar.YEAR, Integer.parseInt(parts[2]));

            input = input.replace(match, "").trim();
            entry.setDate(getFormattedString(c));
        }

        Matcher match_date3 = pattern_date3.matcher(input);
        while (match_date3.find()) {
            String match = match_date3.group();

            Calendar c = Calendar.getInstance();
            c.setLenient(false);
            Matcher match_weekday = pattern_weekday.matcher(match);
            while (match_weekday.find()) {
                int current_date = c.get(Calendar.DAY_OF_MONTH);
                c.set(Calendar.DAY_OF_WEEK, weekday.get(match_weekday.group()));
                if (c.get(Calendar.DAY_OF_MONTH) - current_date <= 0)
                    c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 7);

//                validateCalendar(c);

                System.out.println(getFormattedString(c));
            }

            input = input.replace(match, "").trim();
            entry.setDate(getFormattedString(c));
        }

        Matcher match_date4 = pattern_date4.matcher(input);
        while (match_date4.find()) {
            String match = match_date4.group();

            Calendar c = Calendar.getInstance();
            c.setLenient(false);
            Matcher match_weekday = pattern_weekday.matcher(match);
            while (match_weekday.find()) {
                int current_date = c.get(Calendar.DAY_OF_MONTH);
                c.set(Calendar.DAY_OF_WEEK, weekday.get(match_weekday.group()));
                c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 7);
                if (c.get(Calendar.DAY_OF_MONTH) - current_date <= 7)
                    c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 7);

                System.out.println(getFormattedString(c));
            }

            input = input.replace(match, "").trim();
            entry.setDate(getFormattedString(c));
        }

        return input;
    }

    private String extractTime(Entry entry, String input){
        Matcher m = pattern_time1.matcher(input);
        while (m.find()) {
            String match = m.group();
            input = input.replace(match, "").trim();
            entry.setTime(match);
        }

        return input;
    }

    private String extractLocation(Entry entry, String input){
        Matcher m = pattern_location.matcher(input);
        while (m.find()) {
            String match = m.group();
            input = input.replace(match, "").trim();
            entry.setLocation(match);
        }

        return input;
    }

    @Override
    public Entry makeEntry(String input, EntryType type) {
        Entry entry = null;
        switch (type){
            case CALENDAR:
                entry = new CalendarEntry();
                break;
            case REMINDER:
                entry = new ReminderEntry();
                break;
        }

        entry.addObserver(_view.getObserver());

        input = extractDate(entry, input);
        input = extractTime(entry, input);
        input = extractLocation(entry, input);
        entry.setDetails(input);

        return entry;
    }

    @Override
    public void setMainView(MainView view) {
        _view = view;
    }
}
