package ds.todoapp.services;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import ds.todoapp.events.BaseEvent;
import ds.todoapp.events.LoginResponseEvent;
import ds.todoapp.events.LoginResultEvent;
import ds.todoapp.events.RegisterResponseEvent;
import ds.todoapp.events.RegisterResultEvent;
import ds.todoapp.util.PrefsUtil;
import ds.todoapp.clients.UserRESTClient;
import ds.todoapp.models.User;

/**
 * Created by Duygu on 12/05/2017.
 */

public class UserService extends BaseService {

    private static final String TAG = UserService.class.getSimpleName();
    private UserRESTClient mUserRESTClient;

    public UserService(Context context) {
        super(context);
        mBus.register(this);
        mUserRESTClient = new UserRESTClient(baseBackendUrl, context);
    }

    public void register(String name, String email, String pass) {
        User user = new User(name, email, pass);
        mUserRESTClient.register(user);
    }

    @Subscribe
    public void onRegisterResult(RegisterResponseEvent result) {
        if(result.getType() == BaseEvent.Type.SUCCESS) {
            Log.d(TAG, "REGISTER RESPONSE SUCCESS");
            User user = result.getResult();
            Log.d(TAG, (new Gson()).toJson(user));
            if (user == null) {
                mBus.post(new RegisterResultEvent(BaseEvent.Type.FAIL, null));
                Log.d(TAG, "REGISTER FAIL");
            } else {
                PrefsUtil.saveUser(mSharedPreferences, user);
                mBus.post(new RegisterResultEvent(BaseEvent.Type.SUCCESS, user));
                Log.d(TAG, "REGISTER SUCCESS");
            }
        } else if (result.getType() == BaseEvent.Type.FAIL) {
            mBus.post(new RegisterResultEvent(result.getType(), null));
            Log.d(TAG, "REGISTER FAIL");
        }
    }

    public void login(String email, String pass) {
        User user = new User(null, email, pass);
        mUserRESTClient.login(user);
    }

    @Subscribe
    public void onLoginResult(LoginResponseEvent result) {
        if(result.getType() == BaseEvent.Type.SUCCESS) {
            Log.d(TAG, "LOGIN RESPONSE SUCCESS");
            User user = result.getResult();
            Log.d(TAG, (new Gson()).toJson(user));
            if (user == null) {
                mBus.post(new LoginResultEvent(BaseEvent.Type.FAIL, null));
                Log.d(TAG, "LOGIN FAIL");
            } else {
                PrefsUtil.saveUser(mSharedPreferences, user);
                mBus.post(new LoginResultEvent(BaseEvent.Type.SUCCESS, user));
                Log.d(TAG, "LOGIN SUCCESS");
            }
        } else if (result.getType() == BaseEvent.Type.FAIL) {
            mBus.post(new LoginResultEvent(result.getType(), null));
            Log.d(TAG, "LOGIN FAIL");
        }
    }

    public void logout() {
        PrefsUtil.deleteUser(mSharedPreferences);
    }

    public boolean isLoggedIn() {
        return (getUser() == null) ? false : true;
    }

    public User getUser() {
        return PrefsUtil.getUser(mSharedPreferences);
    }
}
