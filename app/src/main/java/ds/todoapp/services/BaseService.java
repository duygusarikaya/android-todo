package ds.todoapp.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.squareup.otto.Bus;

import ds.todoapp.BusProvider;
import ds.todoapp.TodoApplication;

/**
 * Created by Duygu on 12/05/2017.
 */
public abstract class BaseService {

    protected Bus mBus;
    protected String baseBackendUrl;
    protected SharedPreferences mSharedPreferences;

    protected BaseService(Context context) {
        baseBackendUrl = TodoApplication.getApplication(context).getBaseURLBackend();
        mBus = BusProvider.getInstance();
        mSharedPreferences = context.getSharedPreferences(TodoApplication.PREFS_NAME, 0);
    }
}