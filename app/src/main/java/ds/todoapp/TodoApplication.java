package ds.todoapp;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import com.squareup.otto.Bus;

import ds.todoapp.services.TodosService;
import ds.todoapp.services.UserService;

/**
 * Created by Duygu on 12/05/2017.
 */

public class TodoApplication extends Application {

    private static final String BASE_BACKEND_URL = "https://agile-ocean-55991.herokuapp.com/api/";
    private static TodoApplication application;
    private Context mContext;
    private Bus mBus = BusProvider.getInstance();

    public static final String PREFS_NAME = "TodoAppPrefsFile";

    private TodosService mTodosService;
    private UserService mUserService;

    public static TodoApplication getApplication(final Context context) {
        return (TodoApplication) context.getApplicationContext();
    }

    public String getBaseURLBackend() {
        return BASE_BACKEND_URL;
    }

    public TodosService getTodosService() {
        return mTodosService;
    }
    public UserService getUserService() {
        return mUserService;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        mContext = this;
        mTodosService = new TodosService(this);
        mUserService = new UserService(this);
    }

}
