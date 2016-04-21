package in.nemi.ncontrol;

/**
 * Created by shouryas on 4/22/2016.
 */
public class Users {

    private int _id;
    private String _username;
    private String _password;
    private String _role;

    public Users() {

    }

    public Users(String role, String username, String password) {
        this._username = username;
        this._password = password;
        this._role = role;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public void set_role(String _role) {
        this._role = _role;
    }

    public int get_id() {
        return _id;
    }

    public String get_username() {
        return _username;
    }

    public String get_password() {
        return _password;
    }

    public String get_role() {
        return _role;
    }
}