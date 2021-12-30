package app.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author dingl01
 */
public abstract class DateHandler {

    public static long findTimeDifference(Date d1, Date d2) {
        if ((d1 == null) || (d2 == null)) {
            return -1;
        }
        long differenceInTime = d2.getTime() - d1.getTime();
        return(differenceInTime / (1000 * 60)) % 60;
    }

    public static String formatDate(Date date) {
        Locale locale = Locale.getDefault();
        String sDate = "...";
        if (date != null) {
            SimpleDateFormat ldf = new SimpleDateFormat("d.M.yy HH:mm:ss", locale);
            sDate = ldf.format(date);
        }
        return sDate;
    }

    public static Date timeMillisToDate(Long timeMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        return calendar.getTime();
    }
}
