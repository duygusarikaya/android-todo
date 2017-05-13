package ds.todoapp.services;

import android.content.Context;

import ds.todoapp.clients.TodosRESTClient;

/**
 * Created by Duygu on 12/05/2017.
 */

public class TodosService extends BaseService {

    private static final String TAG = TodosService.class.getSimpleName();
    private TodosRESTClient mTodosRESTClient;

    public TodosService(Context context) {
        super(context);
        mBus.register(this);
        mTodosRESTClient = new TodosRESTClient(baseBackendUrl, context);
    }
}
