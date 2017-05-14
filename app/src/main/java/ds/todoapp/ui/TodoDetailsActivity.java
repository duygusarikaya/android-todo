package ds.todoapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.squareup.otto.Subscribe;

import ds.todoapp.BusProvider;
import ds.todoapp.R;
import ds.todoapp.TodoApplication;
import ds.todoapp.events.BaseEvent;
import ds.todoapp.events.CreateTaskResultEvent;
import ds.todoapp.models.Todo;
import ds.todoapp.services.TodosService;
import ds.todoapp.util.DateUtil;
import ds.todoapp.util.ValidateUtil;
import info.hoang8f.android.segmented.SegmentedGroup;

public class TodoDetailsActivity extends AppCompatActivity {

    private static final String TAG = TodoDetailsActivity.class.getSimpleName();
    private Todo todo;
    private String userId;
    private boolean isEditing = false;

    private EditText titleBox;
    private EditText detailsBox;
    private SegmentedGroup statusGroup;
    private CircularProgressButton saveBtn;

    private TodosService mTodoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BusProvider.getInstance().register(this);

        titleBox = (EditText) findViewById(R.id.title_edit_text);
        detailsBox = (EditText) findViewById(R.id.details_edit_text);
        statusGroup = (SegmentedGroup) findViewById(R.id.segmented_status);
        saveBtn = (CircularProgressButton) findViewById(R.id.save_button);
        saveBtn.setIndeterminateProgressMode(true);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        mTodoService = TodoApplication.getApplication(this).getTodosService();

        if (getIntent() != null) {
            Log.d(TAG, "intent");
            userId = getIntent().getStringExtra("userId");
            todo = (Todo) getIntent().getSerializableExtra("TASK");
            if (todo != null) {
                Log.d(TAG, "todo edit intent");
                userId = todo.getUserId();
                isEditing = true;
                fillEditTexts(todo);
            }

        }
    }

    private void fillEditTexts(Todo todo) {
        titleBox.setText(todo.getTitle());
        detailsBox.setText(todo.getDetails());
        setStatusButton(todo.getState());
    }

    private void setStatusButton(Todo.TodoState state) {
        if(state == Todo.TodoState.open) {
            statusGroup.check(R.id.button_open);
        } else if(state == Todo.TodoState.inprogress) {
            statusGroup.check(R.id.button_inprogress);
        } else if(state == Todo.TodoState.done) {
            statusGroup.check(R.id.button_done);
        }

    }

    private Todo.TodoState getStatusFromUI() {
        switch (statusGroup.getCheckedRadioButtonId()) {
            case R.id.button_open:
                return Todo.TodoState.open;
            case R.id.button_inprogress:
                return Todo.TodoState.inprogress;
            case R.id.button_done:
                return Todo.TodoState.done;
            default:
                return Todo.TodoState.open;
        }
    }

    private void send() {
        String title = titleBox.getText().toString();
        String details = detailsBox.getText().toString();
        Todo.TodoState state = getStatusFromUI();
        if(ValidateUtil.isBlank(title)){
            Toast.makeText(getApplicationContext(), getString(R.string.task_title_invalid), Toast.LENGTH_SHORT).show();
        } else {

            saveBtn.setClickable(false);
            saveBtn.setProgress(50);
            if (isEditing) {
                todo.setTitle(title);
                todo.setDetails(details);
                todo.setState(state);
                mTodoService.update(todo);
            } else {
                todo = new Todo(userId, title,details, DateUtil.getNow(), state);
                mTodoService.add(todo);
            }
        }

    }

    @Subscribe
    public void onCreateUpdateTaskResultEvent(CreateTaskResultEvent result) {
        if(result.getType() == BaseEvent.Type.SUCCESS) {
            Log.d(TAG, "SAVE SUCCESS");
            saveBtn.setProgress(100);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);
                        mSaveBtnSuccessHandler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } else if (result.getType() == BaseEvent.Type.FAIL) {
            Log.d(TAG, "SAVE FAIL");
            saveBtn.setProgress(-1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        mSaveBtnFailHandler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private Handler mSaveBtnFailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            saveBtn.setProgress(0);
            saveBtn.setClickable(true);
        }
    };
    private Handler mSaveBtnSuccessHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            returnWithRefreshInvoke();
        }
    };

    private void returnWithRefreshInvoke() {
        Intent intent = new Intent(this,TodoappActivity.class);
        intent.putExtra("refresh", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
