package ds.todoapp.events;

import retrofit2.Response;

/**
 * Created by Duygu on 14/05/2017.
 */

public class DeleteTaskResponseEvent extends BaseEvent {
    private Response response;
    public DeleteTaskResponseEvent(Type type, Response response) {
        super(type);
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
