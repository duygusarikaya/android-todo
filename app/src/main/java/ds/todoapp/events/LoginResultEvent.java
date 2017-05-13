package ds.todoapp.events;

import ds.todoapp.models.User;

/**
 * Created by Duygu on 13/05/2017.
 */

public class LoginResultEvent extends  BaseEvent{
    private User user;
    public LoginResultEvent(Type type, User user) {
        super(type);
    }

    public User getUser() {
        return user;
    }
}
