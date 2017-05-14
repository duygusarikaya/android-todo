package ds.todoapp.events;

import java.util.ArrayList;

import ds.todoapp.models.Todo;

/**
 * Created by Duygu on 13/05/2017.
 */

public class GetTasksResultEvent extends BaseEvent {
    private ArrayList<Todo> tasks;
    public GetTasksResultEvent(Type type, ArrayList<Todo> tasks) {
        super(type);
        this.tasks = tasks;
    }

    public ArrayList<Todo> getTasks() {
        return tasks;
    }
}
