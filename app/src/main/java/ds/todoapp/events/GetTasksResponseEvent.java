package ds.todoapp.events;

import retrofit2.Response;

/**
 * Created by Duygu on 13/05/2017.
 */

public class GetTasksResponseEvent extends BaseEvent {
    private Response response;
    public GetTasksResponseEvent(Type type, Response response) {
        super(type);
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
