import java.security.SecureRandom;

public class User {
    private String UserID;
    private String name;
    private String surname;
    private String password;
    private String email;
    private String phone;
    private String username;



    // Constructor
    public User(String name, String surname, String password, String email, String phone, String username) {
        setName(name);
        setSurname(surname);
        setPassword(password);
        setEmail(email);
        setPhone(phone);
        setUsername(username);
        setUserID();
    }

    /* bu olmayacak, simdilik program calissin diye var */
    public User(String username, String password){
        setUsername(username);
        setPassword(password);
    }


    // Setters
    public void setUserID() {

        /* bu 16 karakterlik string olusturuyo onu da UserID'ye atiyo */

        if (this.UserID == null){
            
            String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            SecureRandom random = new SecureRandom();
            StringBuilder sb = new StringBuilder(16);

            for (int i = 0; i < 16; i++) {
                int randomIndex = random.nextInt(CHARACTERS.length());
                char randomChar = CHARACTERS.charAt(randomIndex);
                sb.append(randomChar);
            }

            this.UserID =  sb.toString();
        } else {
            System.out.println("User ID already exists");
        } 

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
    // Getters
    public String getUserID() {
        return this.UserID;
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

}
