
public class Admin extends User {
    
    // Constructor
    public Admin(String name, String surname, String password, String email, String phone,  String username, String userType, String userID) {
        super(name, surname, password, email, phone, username, "Admin");
    }
}
