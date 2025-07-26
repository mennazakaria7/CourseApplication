
public class User {
    protected String usertype;
    protected String ID;
    protected String username;
    protected String password;

    public User(String usertype, String ID, String username, String password) {
        this.usertype = usertype;
        this.ID = ID;
        this.username = username;
        this.password = password;
    }

    public User(String ID, String username, String password) {
        this.ID = ID;
        this.username = username;
        this.password = password;
    }

    public User() {

    }

    public String getUsertype() { return usertype; }
    public String getID() { return ID; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
