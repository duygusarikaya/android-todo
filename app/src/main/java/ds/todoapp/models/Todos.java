package ds.todoapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Duygu on 14/05/2017.
 */

public class Todos implements Serializable{
    private ArrayList<Todo> todos;

    public Todos(ArrayList<Todo> todos) {
        this.todos = todos;
    }

    public ArrayList<Todo> getTodos() {
        return todos;
    }

}
