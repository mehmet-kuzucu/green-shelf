package app.greenshelf;
public class Carrier {
    private String name;
    private String surname;
    private String password;
    private String email;
    private String phone;
    private String username;
    private String userType;
    private String carrierID;

    public String getCarrierID() {
        return carrierID;
    }

    public Carrier() {
    }

    public Carrier(String name, String surname, String password, String email, String phone, String username, String userType, String carrierID) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.userType = userType;
        this.carrierID = carrierID;
    }
}
