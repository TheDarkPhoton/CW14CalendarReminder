package backend;

import backend.entries.Calendar;
import backend.entries.EntryType;
import backend.entries.Reminder;

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

    private static String match_date1 = "(" + match_weekday + " " + match_day + " " + match_month + ")";
    private static String match_date2 = "((([0-3](((?<!0)0)|((?<=[0-2])[1-9])|((?<=3)1)))|[1-9]) ?/ ?(([01](((?<!0)0)|((?<=0)[1-9])|((?<=1)[12])))|[1-9]) ?/ ?([2-9]\\d{3}))";
    private static String match_date3 = "(on " + match_weekday + ")";
    private static String match_date4 = "(next " + match_weekday + ")";

    private static Pattern pattern_dates = Pattern.compile(match_date1 + "|" + match_date2 + "|" + match_date3 + "|" + match_date4);
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

    @Override
    public Entry makeEntry(String input, EntryType type) {
        Entry entry = null;
        switch (type){
            case CALENDAR:
                entry = new Calendar();
                break;
            case REMINDER:
                entry = new Reminder();
                break;
        }

        entry.addObserver(_view.getObserver());

        Matcher m = pattern_location.matcher(input);
        while (m.find()) {
            String match = m.group();
            input = input.replace(match, "");
        }

        return entry;
    }

    @Override
    public void setMainView(MainView view) {
        _view = view;
    }
}
