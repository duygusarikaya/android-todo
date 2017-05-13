package ds.todoapp.util;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import ds.todoapp.models.User;

/**
 * Created by Duygu on 13/05/2017.
 */

public class PrefsUtil {
    private static final String TAG = PrefsUtil.class.getSimpleName();
    private static final String USER = "user";

    public static void saveUser(SharedPreferences mSharedPreferences, User user) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(USER, json);
        editor.commit();
    }

    public static User getUser(SharedPreferences mSharedPreferences) {
        Gson gson = new Gson();
        String json = mSharedPreferences.getString(USER, null);
        if(json == null) return null;

        User user = gson.fromJson(json, User.class);
        return user;
    }

    public static String getUserId(SharedPreferences mSharedPreferences) {
        User user = getUser(mSharedPreferences);
        return (user == null) ? null : user.get_id();
    }

    public static void deleteUser(SharedPreferences mSharedPreferences) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USER, null);
        editor.commit();
    }
}
