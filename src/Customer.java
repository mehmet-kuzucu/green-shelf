public class Customer extends User {

    
    private String address;
        
    // Constructor
    public Customer(String name, String surname, String password, String email, String phone, String username, String address) {
        super(name, surname, password, email, phone, username);
        setAddress(address);
    }



    public void setAddress(String address) {
        this.address = address;
    }


    public String getAddress() {
        return this.address;
    }

}
