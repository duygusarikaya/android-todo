package ds.todoapp.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.squareup.otto.Subscribe;

import ds.todoapp.BusProvider;
import ds.todoapp.R;
import ds.todoapp.TodoApplication;
import ds.todoapp.events.BaseEvent;
import ds.todoapp.events.LoginResponseEvent;
import ds.todoapp.events.LoginResultEvent;
import ds.todoapp.services.UserService;
import ds.todoapp.util.ValidateUtil;

public class LoginActivity extends FragmentActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText emailBox;
    private EditText passwordBox;
    private Button registerBtn;
    private CircularProgressButton loginBtn;

    private UserService mUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setContentView(R.layout.activity_login);

        BusProvider.getInstance().register(this);
        mUserService = TodoApplication.getApplication(this).getUserService();

        emailBox = (EditText) findViewById(R.id.email_edit_text);
        passwordBox = (EditText) findViewById(R.id.password_edit_text);
        loginBtn = (CircularProgressButton) findViewById(R.id.login_button);
        loginBtn.setIndeterminateProgressMode(true);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailBox.getText().toString();
                String password = passwordBox.getText().toString();
                if(!ValidateUtil.email(email)){
                    Toast.makeText(getApplicationContext(), getString(R.string.login_email_invalid), Toast.LENGTH_SHORT).show();
                }else if(!ValidateUtil.password(password)){
                    Toast.makeText(getApplicationContext(), getString(R.string.login_password_invalid), Toast.LENGTH_SHORT).show();
                } else if(ValidateUtil.email(email) && ValidateUtil.password(password)) {
                    loginBtn.setClickable(false);
                    loginBtn.setProgress(50);
                    mUserService.login(email, password);
                }
            }
        });

        registerBtn = (Button) findViewById(R.id.register_button);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(loginIntent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,TodoappActivity.class);
        intent.putExtra("EXIT", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Subscribe
    public void onLoginResult(LoginResultEvent result) {
        if(result.getType() == BaseEvent.Type.SUCCESS) {
            Log.d(TAG, "LOGIN SUCCESS");
            loginBtn.setProgress(100);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);
                        mLoginBtnSuccessHandler.sendEmptyMessage(0);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } else if (result.getType() == BaseEvent.Type.FAIL) {
            Log.d(TAG, "LOGIN FAIL");
            loginBtn.setProgress(-1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        mLoginBtnFailHandler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private Handler mLoginBtnFailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            loginBtn.setProgress(0);
            loginBtn.setClickable(true);
        }
    };
    private Handler mLoginBtnSuccessHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            finish();
        }
    };

}
