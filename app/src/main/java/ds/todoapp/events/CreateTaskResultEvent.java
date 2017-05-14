package ds.todoapp.events;

import ds.todoapp.models.Todo;

/**
 * Created by Duygu on 14/05/2017.
 */

public class CreateTaskResultEvent extends BaseEvent {
    private Todo todo;
    public CreateTaskResultEvent(Type type, Todo todo) {
        super(type);
        this.todo = todo;
    }

    public Todo getTodo() {
        return todo;
    }
}
