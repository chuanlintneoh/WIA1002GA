import java.awt.Image;
public class User {
    private int userId;
    private final String username;
    private String password;
    private Image profilePic;
    private String name;
    private String emailAddress;
    private String contactNo;
    private String birthDate;
    private String address;
    private char gender;

    public User(String name, String username, String password, String emailAddress, String contactNo, String birthDate, char gender) {
        this.username = username;
        this.password = password;
        this.profilePic = null;
        this.name = name;
        this.emailAddress = emailAddress;
        this.contactNo = contactNo;
        this.birthDate = birthDate;
        this.address = null;
        this.gender = gender;
        Database database = new Database();
        database.registerUser(this);
        database.close();
    }

    public User(int userId, String name, String username, String email, String contactNo, String birthDate, char gender, String address) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.emailAddress = email;
        this.contactNo = contactNo;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Image getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Image profilePic) {
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return String.format("User ID: %d, Name: %s, Username: %s, Email Address: %s, Contact No.: %s", userId, name, username, emailAddress, contactNo);
    }

    public int getUserId() {
        return userId;
    }
}