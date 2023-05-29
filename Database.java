import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.*;
public class Database {
    private Connection connection;
    private Statement statement;
    private final String url = "jdbc:mysql://localhost:3306/thefacebook";
    private final String username = "root";
    private final String password = "wia1002fb";
    public Database(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//Register the driver class
            connection = DriverManager.getConnection(url,username,password);//Create connection
            statement = connection.createStatement();//Create statement
            System.out.println("Database Connection Successful!");
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    // executeQuery -> retrieve data
    // executeUpdate -> modify database
    public void registerUser(User user){
        try {
            String query =
                    String.format("INSERT INTO users (name, username, password, email_address, contact_no, birthDate, gender) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%c')",
                            user.getName(),user.getUsername(),user.getPassword(),user.getEmailAddress(),user.getContactNo(),user.getBirthDate(),user.getGender());
            statement.executeUpdate(query);// Execute the query
            System.out.println("User registered successfully!");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public int authenticateUser(String username,String hashedPassword){
        String query =
                "SELECT user_id FROM users WHERE (username = ? OR email_address = ? OR contact_no = ?) AND password = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,username);
            statement.setString(2,username);
            statement.setString(3,username);
            statement.setString(4,hashedPassword);
            ResultSet resultSet = statement.executeQuery();
            // If a row is returned, the user exists and the credentials are valid
            if (resultSet.next()){
                return resultSet.getInt("user_id");
            }
            else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public int getUserId(String username){
        String query =
                "SELECT user_id FROM users WHERE username = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt("user_id");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
    public String get(String column,int userId){
        //column = name/username/password/email_address/contact_no/birthdate/gender/address
        String query =
                String.format("SELECT %s FROM users WHERE user_id = ?",column);
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return resultSet.getString(column);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public void set(String column,String data,int userId){
        //column = name/username/password/email_address/contact_no/birthdate/gender/address
        String query =
                String.format("UPDATE users SET %s = ? WHERE user_id = ?",column);
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,data);
            statement.setInt(2,userId);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public byte[] getProfilePicture(int userId){
        String query =
                "SELECT profile_picture FROM users WHERE user_id = ?";
        byte[] picture = null;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                Blob profilePictureBlob = resultSet.getBlob("profile_picture");
                if (profilePictureBlob != null){
                    picture = profilePictureBlob.getBytes(1,(int)profilePictureBlob.length());
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return picture;
    }
    public void deleteProfilePicture(int userId){
        String query =
                "UPDATE users SET profile_picture = NULL WHERE user_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userId);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void setProfilePicture(byte[] picture,int userId){
        deleteProfilePicture(userId);
        String query =
                "UPDATE users SET profile_picture = ? WHERE user_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(picture);
            statement.setBinaryStream(1,inputStream);
            statement.setInt(2,userId);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    //status & user_friends
    public String getStatus(int statusId){
        String query =
                "SELECT status FROM status WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,statusId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return resultSet.getString("status");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return "Undefined Status";
    }// view available status

    //Admin special features
    public boolean verifyAdmin(int userId, String hashedPassword){
        String query =
                "UPDATE users SET admin = 1 WHERE user_id = ?";
        if (hashedPassword.equals("c93750932def20e4e4fec0ba7b338a3c")){//admin password = WIA1002fb
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1,userId);
                boolean success = statement.executeUpdate() > 0;
                if (success){
                    System.out.println("Admin verified for User ID: " + userId);
                }
                else {
                    System.out.println("An error occurred.");
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
            return true;
        }
        System.out.println("Incorrect password!");
        return false;
    }
    public boolean isAdmin(int userId){
        String query =
                "SELECT admin FROM users WHERE user_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt("admin") == 1;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public List<User> viewAllUsers(){
        List<User> allUsers = new ArrayList<>();
        String query =
                "SELECT user_id, name, username, email_address, contact_no, birthdate, gender, address FROM users";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int userId = resultSet.getInt("user_id");
                String name = resultSet.getString("name");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email_address");
                String contactNo = resultSet.getString("contact_no");
                String birthdate = resultSet.getString("birthdate");
                char gender = resultSet.getString("gender").charAt(0);
                String address = resultSet.getString("address");
                allUsers.add(new User(userId,name,username,email,contactNo,birthdate,gender,address));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return allUsers;
    }
    public void deleteUser(int userId){
        String deleteUserQuery =
                "DELETE FROM users WHERE user_id = ?";
        String deleteFriendsQuery =
                "DELETE FROM user_friends WHERE user_id = ? OR friend_id = ?";
        String deleteHobbiesQuery =
                "DELETE FROM user_hobbies WHERE user_id = ?";
        String deleteJobsQuery =
                "DELETE FROM user_jobs WHERE user_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(deleteUserQuery);
            statement.setInt(1,userId);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        try {
            PreparedStatement statement = connection.prepareStatement(deleteFriendsQuery);
            statement.setInt(1,userId);
            statement.setInt(2,userId);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        try {
            PreparedStatement statement = connection.prepareStatement(deleteHobbiesQuery);
            statement.setInt(1,userId);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        try {
            PreparedStatement statement = connection.prepareStatement(deleteJobsQuery);
            statement.setInt(1,userId);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void close(){
        try {
            statement.close();
            connection.close();
            System.out.println("Database connection closed.");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}