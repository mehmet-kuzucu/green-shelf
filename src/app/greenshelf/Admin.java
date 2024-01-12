package app.greenshelf;

public class Admin extends User {
    
    public Admin(String name, String surname, String password, String email, String phone,  String username) {
        super(name, surname, password, email, phone, username, "Admin");
    }
}
