package ds.todoapp.events;

import ds.todoapp.models.User;
import retrofit2.Response;

/**
 * Created by Duygu on 13/05/2017.
 */

public class RegisterResponseEvent extends BaseEvent {
    private User result;
    private Response response;


    public RegisterResponseEvent(Type type, Response response, User result) {
        super(type);
        this.response = response;
        this.result = result;
    }

    public User getResult() {
        return result;
    }

    public Response getResponse() {
        return response;
    }
}
