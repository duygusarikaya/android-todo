package ds.todoapp.clients;

import android.content.Context;

import ds.todoapp.api.TodosAPIService;

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
}
