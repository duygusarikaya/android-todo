package ds.todoapp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Duygu on 13/05/2017.
 */

public class ValidateUtil {

    public static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    public static boolean password(String password) {
        return !(isBlank(password));
    }

    public static boolean email(String email) {
        Matcher matcher = EMAIL_REGEX .matcher(email);
        return matcher.find();
    }

    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        if (str.trim().length() == 0) {
            return true;
        }
        if (str.equals("")) {
            return true;
        }
        if (str.equals("null") || str.equals("NULL")) {
            return true;
        }
        return false;
    }

    public static boolean name(String name) {
        return !(isBlank(name));
    }
}
