package ds.todoapp.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Duygu on 11/05/2017.
 */

public class Todo implements Serializable, Comparable<Todo>{


    public enum TodoState {
        OPEN, INPROGRESS, DONE
    }
    private String _id;
    private String userId;
    private String title;
    private String details;
    private String createdAt;
    private TodoState state;

    public Todo(String userId, String title, String details, String createdAt, TodoState state) {
        this.userId = userId;
        this.title = title;
        this.details = details;
        this.createdAt = createdAt;
        this.state = state;
    }

    public Todo(String _id, String userId, String title, String details, String createdAt, TodoState state) {
        this(userId, title, details, createdAt, state);
        this._id = _id;
    }

    @Override
    public int compareTo(@NonNull Todo another) {
        return another.getCreationDate().compareTo(this.getCreationDate());
    }

    private Date getCreationDate() {
        //TODO
        return null;
    }

    public String get_id() {
        return _id;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public TodoState getState() {
        return state;
    }
}
