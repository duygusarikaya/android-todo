package ds.todoapp.events;

import retrofit2.Response;

/**
 * Created by Duygu on 14/05/2017.
 */

public class CreateTaskResponseEvent extends BaseEvent {
    private Response response;
    public CreateTaskResponseEvent(Type type, Response response) {
        super(type);
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
