package cn.granitech.variantorm.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class DateHelper {
    private static final String dateFormatPattern = "yyyy-MM-dd'T'HH:mm:ss";

    public static Date parseDateTime(String dateTimeStr) {
        if (dateTimeStr.contains("00:00T"))
            dateTimeStr = dateTimeStr.replaceFirst("00:00T", "");
        try {
            if (isValidDateTimeString(dateTimeStr))
                return (new SimpleDateFormat(dateFormatPattern)).parse(dateTimeStr);
            if (isValidDateString(dateTimeStr))
                return (new SimpleDateFormat(dateFormatPattern)).parse(dateTimeStr.concat("T00:00:00"));
            return (new SimpleDateFormat(dateFormatPattern)).parse(dateTimeStr);
        } catch (ParseException a) {
            throw new IllegalArgumentException((new StringBuilder()).insert(0, "Invalid datetime format: ").append(dateTimeStr).toString(), a);
        }
    }

    public static boolean isValidDateTimeString(String dateTimeStr) {
        return (dateTimeStr != null && pattern.matcher(dateTimeStr).matches());
    }

    private static final Pattern validDateString = Pattern.compile("\\d\\d\\d\\d-\\d\\d?-\\d\\d?");


    private static final String datePattern = "yyyy-MM-dd";

    public static boolean isValidDateString(String dateStr) {
        return (dateStr != null && validDateString.matcher(dateStr).matches());
    }

    public static Date parseDate(String dateStr) {
        try {
            if (isValidDateString(dateStr))
                return (new SimpleDateFormat(datePattern)).parse(dateStr);
            if (dateStr.indexOf("T") > 0)
                return (new SimpleDateFormat(datePattern)).parse(dateStr.substring(0, dateStr.indexOf('T')));
            return (new SimpleDateFormat(datePattern)).parse(dateStr.substring(0, dateStr.indexOf(" ")));
        } catch (ParseException a) {
            throw new IllegalArgumentException((new StringBuilder()).insert(0, "Invalid date format: ").append(dateStr).toString(), a);
        }
    }

    private static final Pattern pattern = Pattern.compile("\\d\\d\\d\\d-\\d\\d?-\\d\\d?T\\d\\d?:\\d\\d?:\\d\\d?");
}
