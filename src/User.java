import java.util.ArrayList;
import java.awt.Image;
public class User {
    private final String username;
    private String password;
    private Image profilePic;
    private String name;
    private String emailAddress;
    private String contactNo;
    private String birthDate;
    private String address;
    private char gender;
    private ArrayList<User> friends;
    private ArrayList<String> hobbies;
    private String job;
    public User(String name,String username,String password,String emailAddress, String contactNo, String birthDate,char gender){
        this.username = username;
        this.password = password;
        this.profilePic = null;
        this.name = name;
        this.emailAddress = emailAddress;
        this.contactNo = contactNo;
        this.birthDate = birthDate;
        this.address = null;
        this.gender = gender;
        this.friends = new ArrayList<>();
        this.hobbies = new ArrayList<>();
        this.job = null;
        Database database = new Database();
        database.registerUser(this);
        database.close();
    }
    public User(String username,String email,String phone,String password){
        this(null,username,password,email,phone,null,'-');
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
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
    public String getJob() {
        return job;
    }
    public void setJob(String job) {
        this.job = job;
    }
}
