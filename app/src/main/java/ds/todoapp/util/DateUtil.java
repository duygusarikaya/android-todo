package ds.todoapp.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Duygu on 13/05/2017.
 */

public class DateUtil {

    private static final String TAG = DateUtil.class.getSimpleName();

    //"2017-05-13T16:54:52.946Z"
    public static String getNow() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String today = dateFormatter.format(date);
        return today;
    }

    public static Date getDate(String date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return dateFormatter.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, "Cannot parse date");
        }
        return null;
    }

}
