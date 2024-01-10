package app.greenshelf;
public class Customer extends User {

    
    private String address;
    private String profilePicture;
        
    public Customer(String name, String surname, String password, String email, String phone, String username, String address, String profilePicture) {
        super(name, surname, password, email, phone, username, "customer");
        setAddress(address);
        setProfilePicture(profilePicture);
    }



    public void setAddress(String address) {
        this.address = address;
    }
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }


    public String getAddress() {
        return this.address;
    }
    public String getProfilePicture() {
        return this.profilePicture;
    }

}
