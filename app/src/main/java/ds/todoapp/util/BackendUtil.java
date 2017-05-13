package ds.todoapp.util;

import android.util.Base64;

/**
 * Created by Duygu on 13/05/2017.
 */

public class BackendUtil {
    public static String getAuthHeader(String email, String password) {
        String credentials = email.trim() + ":" + password.trim();
        String authStr = "Basic "+ Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        return authStr;
    }
}
