package ds.todoapp.clients;

import android.content.Context;
import android.util.Log;

import ds.todoapp.api.UserAPIService;
import ds.todoapp.events.BaseEvent;
import ds.todoapp.events.LoginResponseEvent;
import ds.todoapp.events.RegisterResponseEvent;
import ds.todoapp.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Duygu on 12/05/2017.
 */

public class UserRESTClient extends BaseRESTClient {
    private static final String TAG = UserRESTClient.class.getSimpleName();
    private UserAPIService service;

    public UserRESTClient(String baseBackendUrl, Context mContext) {
        super(baseBackendUrl, mContext);
        service = retrofit.create(UserAPIService.class);
    }

    public void register(User user) {
        Call<User> call = service.register(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "REGISTER RECEIVED SUCCESSFULLY");
                mBus.post(new RegisterResponseEvent(BaseEvent.Type.SUCCESS, response, response.body()));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "REGISTER RECEIVE FAILED");
                mBus.post(new RegisterResponseEvent(BaseEvent.Type.FAIL, null, null));
            }
        });
    }

    public void login(User user) {
        Call<User> call = service.login(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "LOGIN RECEIVED SUCCESSFULLY");
                mBus.post(new LoginResponseEvent(BaseEvent.Type.SUCCESS, response, response.body()));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "LOGIN RECEIVE FAILED");
                mBus.post(new LoginResponseEvent(BaseEvent.Type.FAIL, null, null));
            }
        });
    }

}
