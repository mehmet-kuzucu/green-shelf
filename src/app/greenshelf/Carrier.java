package app.greenshelf;
public class Carrier extends User {
    private String profilePicture;
    private String address;

    public Carrier(String name, String surname, String password, String email, String phone, String username, String address, String profilePicture) {
        super(name, surname, password, email, phone, username, "carrier");
        setProfilePicture(profilePicture);
        setAddress(address);
    }
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getProfilePicture()
    {
        return this.profilePicture;
    }
     
}   
