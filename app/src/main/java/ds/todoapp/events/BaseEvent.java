package ds.todoapp.events;

/**
 * Created by Duygu on 13/05/2017.
 */

public abstract class BaseEvent {
    public enum Type
    {
        SUCCESS,
        FAIL
    }

    public Type type;

    protected BaseEvent(Type type) {
        this.type = type;
    }

    public Type getType()  {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
