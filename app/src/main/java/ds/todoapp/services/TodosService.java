package ds.todoapp.services;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;

import ds.todoapp.clients.TodosRESTClient;
import ds.todoapp.events.BaseEvent;
import ds.todoapp.events.CreateTaskResponseEvent;
import ds.todoapp.events.CreateTaskResultEvent;
import ds.todoapp.events.DeleteTaskResponseEvent;
import ds.todoapp.events.DeleteTaskResultEvent;
import ds.todoapp.events.GetTasksResponseEvent;
import ds.todoapp.events.GetTasksResultEvent;
import ds.todoapp.events.UpdateTaskResponseEvent;
import ds.todoapp.events.UpdateTaskResultEvent;
import ds.todoapp.models.Todo;
import ds.todoapp.models.Todos;
import ds.todoapp.models.User;
import ds.todoapp.util.BackendUtil;
import ds.todoapp.util.PrefsUtil;

/**
 * Created by Duygu on 12/05/2017.
 */

public class TodosService extends BaseService {

    private static final String TAG = TodosService.class.getSimpleName();
    private TodosRESTClient mTodosRESTClient;

    private String apiKey;
    private User user;

    public TodosService(Context context) {
        super(context);
        mBus.register(this);
        mTodosRESTClient = new TodosRESTClient(baseBackendUrl, context);
    }

    public void getTasks(User user) {
        String apiKey = BackendUtil.getAuthHeader(user.getEmail(), user.getPass());
        mTodosRESTClient.getTasks(apiKey, user.get_id());
    }

    @Subscribe
    public void onGetTasksResponseEvent(GetTasksResponseEvent event) {
        if(event.getType() == BaseEvent.Type.SUCCESS) {
            Log.d(TAG, "GET TASKS RESPONSE SUCCESS");
            Todos todos = (Todos) event.getResponse().body();
            ArrayList<Todo> tasks = todos.getTodos();
            Log.d(TAG, (new Gson()).toJson(event.getResponse().body()));
            if (tasks == null) {
                mBus.post(new GetTasksResultEvent(BaseEvent.Type.FAIL, null));
                Log.d(TAG, "GET TASKS FAIL");
            } else {
                mBus.post(new GetTasksResultEvent(BaseEvent.Type.SUCCESS, tasks));
                Log.d(TAG, "GET TASKS SUCCESS");
            }
        } else if (event.getType() == BaseEvent.Type.FAIL) {
            mBus.post(new GetTasksResultEvent(event.getType(), null));
            Log.d(TAG, "GET TASKS FAIL");
        }
    }

    public void add(Todo todo) {
        updateData();
        mTodosRESTClient.createTask(apiKey, user.get_id(), todo);
    }

    @Subscribe
    public void onCreateTaskResponseEvent(CreateTaskResponseEvent event) {
        if(event.getType() == BaseEvent.Type.SUCCESS) {
            Log.d(TAG, "CREATE TASK RESPONSE SUCCESS");
            Todo todo = (Todo) event.getResponse().body();
            Log.d(TAG, (new Gson()).toJson(event.getResponse().body()));
            if (todo == null) {
                mBus.post(new CreateTaskResultEvent(BaseEvent.Type.FAIL, null));
                Log.d(TAG, "GET TASKS FAIL");
            } else {
                mBus.post(new CreateTaskResultEvent(BaseEvent.Type.SUCCESS, todo));
                Log.d(TAG, "GET TASKS SUCCESS");
            }
        } else if (event.getType() == BaseEvent.Type.FAIL) {
            mBus.post(new CreateTaskResultEvent(event.getType(), null));
            Log.d(TAG, "GET TASKS FAIL");
        }
    }


    public void update(Todo todo) {
        updateData();
        mTodosRESTClient.updateTask(apiKey, todo.get_id(), todo);
    }


    public void delete(String todoId) {
        updateData();
        mTodosRESTClient.deleteTask(apiKey, todoId);
    }

    @Subscribe
    public void onDeleteTaskResponseEvent(DeleteTaskResponseEvent event) {
        if(event.getType() == BaseEvent.Type.SUCCESS) {
            Log.d(TAG, "DELETE TASK RESPONSE SUCCESS");
            Log.d(TAG, (new Gson()).toJson(event.getResponse().body()));
            if (event.getResponse().code() != 200) {
                mBus.post(new DeleteTaskResultEvent(BaseEvent.Type.FAIL));
                Log.d(TAG, "DELETE TASKS FAIL");
            } else {
                mBus.post(new DeleteTaskResultEvent(BaseEvent.Type.SUCCESS));
                Log.d(TAG, "DELETE TASKS SUCCESS");
            }
        } else if (event.getType() == BaseEvent.Type.FAIL) {
            mBus.post(new DeleteTaskResultEvent(event.getType()));
            Log.d(TAG, "DELETE TASKS FAIL");
        }
    }

    private void updateData() {
        user = PrefsUtil.getUser(mSharedPreferences);
        apiKey = BackendUtil.getAuthHeader(user.getEmail(), user.getPass());
    }
}
