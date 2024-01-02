package app.greenshelf;
public class Carrier extends User {
    private String profilePicture;


    public Carrier(String name, String surname, String password, String email, String phone, String username, String address, String profilePicture) {
        super(name, surname, password, email, phone, username, "carrier");
        setProfilePicture(profilePicture);
    }
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }


    public String getProfilePicture()
    {
        return this.profilePicture;
    }
     
}   
