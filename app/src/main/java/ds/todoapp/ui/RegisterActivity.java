package ds.todoapp.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import ds.todoapp.events.RegisterResultEvent;
import ds.todoapp.services.UserService;
import ds.todoapp.util.ValidateUtil;

public class RegisterActivity extends FragmentActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText nameBox;
    private EditText emailBox;
    private EditText passwordBox;
    private CircularProgressButton registerBtn;

    private UserService mUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setContentView(R.layout.activity_register);

        BusProvider.getInstance().register(this);
        mUserService = TodoApplication.getApplication(this).getUserService();

        nameBox = (EditText) findViewById(R.id.name_edit_text);
        emailBox = (EditText) findViewById(R.id.email_edit_text);
        passwordBox = (EditText) findViewById(R.id.password_edit_text);
        registerBtn = (CircularProgressButton) findViewById(R.id.register_button);
        registerBtn.setIndeterminateProgressMode(true);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameBox.getText().toString();
                String email = emailBox.getText().toString();
                String password = passwordBox.getText().toString();
                if(!ValidateUtil.name(name)){
                    Toast.makeText(getApplicationContext(), getString(R.string.register_name_invalid), Toast.LENGTH_SHORT).show();
                }else if(!ValidateUtil.email(email)){
                    Toast.makeText(getApplicationContext(), getString(R.string.login_email_invalid), Toast.LENGTH_SHORT).show();
                }else if(!ValidateUtil.password(password)){
                    Toast.makeText(getApplicationContext(), getString(R.string.login_password_invalid), Toast.LENGTH_SHORT).show();
                } else if(ValidateUtil.name(name) && ValidateUtil.email(email) && ValidateUtil.password(password)) {
                    registerBtn.setClickable(false);
                    registerBtn.setProgress(50);
                    mUserService.register(name, email, password);
                }
            }
        });

    }

    @Subscribe
    public void onResisterResult(RegisterResultEvent result) {
        if(result.getType() == BaseEvent.Type.SUCCESS) {
            Log.d(TAG, "REGISTER SUCCESS");
            registerBtn.setProgress(100);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);
                        mRegisterBtnSuccessHandler.sendEmptyMessage(0);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } else if (result.getType() == BaseEvent.Type.FAIL) {
            Log.d(TAG, "REGISTER FAIL");
            registerBtn.setProgress(-1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        mRegisterBtnFailHandler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private Handler mRegisterBtnFailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            registerBtn.setProgress(0);
            registerBtn.setClickable(true);
        }
    };
    private Handler mRegisterBtnSuccessHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            finish(); //returns to login
        }
    };

}
