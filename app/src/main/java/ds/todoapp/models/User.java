package ds.todoapp.models;

import java.io.Serializable;

/**
 * Created by Duygu on 11/05/2017.
 */

public class User implements Serializable {

    private String _id;
    private String name;
    private String email;
    private String pass;

    public User(String name, String email, String pass) {
        this.name = name;
        this.email = email;
        this.pass = pass;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

}
