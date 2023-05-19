import java.sql.*;
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
    public boolean authenticateUser(String username,String password){
        try {
            String query =
                    String.format("SELECT * FROM users WHERE (username='%s' OR email_address='%s' OR contact_no='%s') AND password='%s'",username,username,username,password);
            ResultSet resultSet = statement.executeQuery(query);// Execute the query
            // If a row is returned, the user exists and the credentials are valid
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
