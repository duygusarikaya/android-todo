package ds.todoapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ds.todoapp.BusProvider;
import ds.todoapp.R;
import ds.todoapp.TodoApplication;
import ds.todoapp.events.RefreshEvent;
import ds.todoapp.services.UserService;

public class TodoappActivity extends AppCompatActivity {

    private static final String TAG = TodoappActivity.class.getName();
    private FloatingActionButton fab;

    private UserService mUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todoapp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BusProvider.getInstance().register(this);

        mUserService = TodoApplication.getApplication(this).getUserService();
        if(!mUserService.isLoggedIn()) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(getApplicationContext(),TodoDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("userId", mUserService.getUser().get_id());
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra("EXIT")) {
            Log.d(TAG, "onNewIntent EXIT");
            setIntent(intent);
        } else if (intent.hasExtra("refresh")) {
            Log.d(TAG, "onNewIntent refresh");
            setIntent(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (getIntent() != null) {
            Log.d(TAG, "onResume intent");
            if (getIntent().getBooleanExtra("EXIT", false) == true) {
                Log.d(TAG, "onResume EXIT true");
                finish();
            } else if(getIntent().getBooleanExtra("refresh", false) == true) {
                BusProvider.getInstance().post(new RefreshEvent());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todoapp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout) {
            mUserService.logout();
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
        return super.onOptionsItemSelected(item);
    }


}
