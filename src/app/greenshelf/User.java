package app.greenshelf;

public class User {
    private String name;
    private String surname;
    private String password;
    private String email;
    private String phone;
    private String username;
    private String userType;

    public User(String name, String surname, String password, String email, String phone, String username, String userType) {
        setName(name);
        setSurname(surname);
        setPassword(password);
        setEmail(email);
        setPhone(phone);
        setUsername(username);
        setUserType(userType);
    }

    public User(String name, String surname, String username, String password){
        setName(name);
        setSurname(surname);
        setUsername(username);
        setPassword(password);
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname= surname;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setEmail(String email) {
        this.email= email;
    }
    public void setPhone(String phone) {
        this.phone= phone;
    }
    public void setUsername(String username) {
        this.username= username;
    }
    public void setUserType(String userType) {
        this.userType= userType;
    }
    

    public String getName() {
        return this.name;
    }
    public String getSurname() {
        return this.surname;
    }
    public String getPassword() {
        return this.password;
    }
    public String getEmail() {
        return this.email;
    }
    public String getPhone() {
        return this.phone;
    }
    public String getUsername() {
        return this.username;
    }
    public String getUserType() {
        return this.userType;
    }
}
