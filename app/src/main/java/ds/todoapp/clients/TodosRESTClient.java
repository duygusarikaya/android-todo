package ds.todoapp.clients;

import android.content.Context;
import android.util.Log;

import ds.todoapp.api.TodosAPIService;
import ds.todoapp.events.BaseEvent;
import ds.todoapp.events.CreateTaskResponseEvent;
import ds.todoapp.events.DeleteTaskResponseEvent;
import ds.todoapp.events.GetTasksResponseEvent;
import ds.todoapp.models.Todo;
import ds.todoapp.models.Todos;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Duygu on 12/05/2017.
 */

public class TodosRESTClient extends BaseRESTClient {

    private static final String TAG = TodosRESTClient.class.getSimpleName();
    private TodosAPIService service;

    public TodosRESTClient(String baseBackendUrl, Context mContext) {
        super(baseBackendUrl, mContext);
        service = retrofit.create(TodosAPIService.class);
    }
     public void getTasks(String apiKey, String userId) {
         Call<Todos> call = service.getTasks(apiKey, userId);
         call.enqueue(new Callback<Todos>() {
             @Override
             public void onResponse(Call<Todos> call, Response<Todos> response) {
                 Log.d(TAG, "GET TASKS RECEIVED SUCCESSFULLY");
                 mBus.post(new GetTasksResponseEvent(BaseEvent.Type.SUCCESS, response));
             }

             @Override
             public void onFailure(Call<Todos> call, Throwable t) {
                 Log.d(TAG, "GET TASKS RECEIVE FAIL");
                 mBus.post(new GetTasksResponseEvent(BaseEvent.Type.FAIL, null));
             }
         });
     }

     public void createTask(String apiKey, String userId, Todo todo) {
         Call<Todo> call = service.add(apiKey, userId, todo);
         call.enqueue(new Callback<Todo>() {
             @Override
             public void onResponse(Call<Todo> call, Response<Todo> response) {
                 if(response != null) {
                     Log.d(TAG, "CREATE TASK RECEIVED SUCCESSFULLY");
                     mBus.post(new CreateTaskResponseEvent(BaseEvent.Type.SUCCESS, response));
                 } else {
                     mBus.post(new CreateTaskResponseEvent(BaseEvent.Type.FAIL, null));
                 }
             }

             @Override
             public void onFailure(Call<Todo> call, Throwable t) {
                 Log.d(TAG, "CREATE TASK RECEIVE FAIL");
                 mBus.post(new CreateTaskResponseEvent(BaseEvent.Type.FAIL, null));
             }
         });
     }

    public void updateTask(String apiKey, String todoId, Todo todo) {
        Call<Todo> call = service.update(apiKey, todoId, todo);
        call.enqueue(new Callback<Todo>() {
            @Override
            public void onResponse(Call<Todo> call, Response<Todo> response) {
                Log.d(TAG, "UPDATE TASK RECEIVED SUCCESSFULLY");
                mBus.post(new CreateTaskResponseEvent(BaseEvent.Type.SUCCESS, response));
            }

            @Override
            public void onFailure(Call<Todo> call, Throwable t) {
                Log.d(TAG, "UPDATE TASK RECEIVE FAIL");
                mBus.post(new CreateTaskResponseEvent(BaseEvent.Type.FAIL, null));
            }
        });
    }

    public void deleteTask(String apiKey, String todoId) {
        Call<Todo> call = service.delete(apiKey, todoId);
        call.enqueue(new Callback<Todo>() {
            @Override
            public void onResponse(Call<Todo> call, Response<Todo> response) {
                Log.d(TAG, "DELETE TASK RECEIVED SUCCESSFULLY");
                mBus.post(new DeleteTaskResponseEvent(BaseEvent.Type.SUCCESS, response));
            }

            @Override
            public void onFailure(Call<Todo> call, Throwable t) {
                Log.d(TAG, "DELETE TASK RECEIVE FAIL");
                mBus.post(new DeleteTaskResponseEvent(BaseEvent.Type.FAIL, null));
            }
        });
    }
}
