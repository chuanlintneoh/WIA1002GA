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
    public List<Friend> viewFriends(int userId){
        List<Friend> friendsRequests = new ArrayList<>();
        String query =
                "SELECT friend_id, status FROM user_friends WHERE user_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int friendId = resultSet.getInt("friend_id");
                int statusId = resultSet.getInt("status");
                String status = getStatus(statusId);
                Friend friend = new Friend(friendId,status);
                friendsRequests.add(friend);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return friendsRequests;
    }// view current friends, friend requests (received,sent)
    public void insertStatus(int userId,int friendId,int statusId){// add 2 rows in database
        //statusId = 1,2
        String query =
                "INSERT INTO user_friends (user_id,friend_id,status) VALUES (?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userId);
            statement.setInt(2,friendId);
            statement.setInt(3,statusId);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }// send request
    public void updateStatus(int userId, int friendId,int statusId){// update 2 rows in database
        //statusId = 3
        String query =
                "UPDATE user_friends SET status_id = ? WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,statusId);
            statement.setInt(2,userId);
            statement.setInt(3,friendId);
            statement.setInt(4,friendId);
            statement.setInt(5,userId);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }// accept request
    public void removeStatus(int userId,int friendId){// remove 2 rows in database
        String query =
                "DELETE FROM user_friends WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userId);
            statement.setInt(2,friendId);
            statement.setInt(3,friendId);
            statement.setInt(4,userId);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }// delete request, unfriend
    //hobbies & user_hobbies
    public List<String> getHobbies(){
        List<String> hobbies = new ArrayList<>();
        String query =
                "SELECT hobby FROM hobbies";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String hobby = resultSet.getString("hobby");
                hobbies.add(hobby);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return hobbies;
    }// view ALL available hobbies
    public void insertHobby(String hobby){
        String query =
                "INSERT INTO hobbies (hobby) VALUES (?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,hobby);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }// insert a new hobby into database
    public List<String> viewUserHobbies(int userId){
        List<String> userHobbies = new ArrayList<>();
        String query =
                "SELECT h.hobby FROM user_hobbies uh JOIN hobbies h ON uh.hobby_id = h.id WHERE uh.user_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String hobby = resultSet.getString("hobby");
                userHobbies.add(hobby);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return userHobbies;
    }// return user's hobbies list
    public void clearUserHobbies(int userId){
        String query =
                "DELETE FROM user_hobbies WHERE user_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userId);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }// clear user's hobbies list
    public void editUserHobbies(int userId, List<Integer> editedHobbies){
        clearUserHobbies(userId);
        String query =
                "INSERT INTO user_hobbies (user_id, hobby_id) VALUES (?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for (int hobbyId: editedHobbies){
                statement.setInt(1,userId);
                statement.setInt(2,hobbyId);
                statement.executeUpdate();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }// clear user's hobbies list then update with new list
    //jobs & user_jobs
    public List<String> getJobs(){
        List<String> jobs = new ArrayList<>();
        String query =
                "SELECT job FROM jobs";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String job = resultSet.getString("job");
                jobs.add(job);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return jobs;
    }// view ALL available jobs
    public void insertJob(String job){
        String query =
                "INSERT INTO jobs (job) VALUES (?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,job);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }// insert a new job into database
    public List<Job> viewUserJobs(int userId){
        List<Job> userJobs = new ArrayList<>();
        String query =
                "SELECT (uj.job_id, j.job, uj.start_date, uj.end_date) FROM user_jobs uj JOIN jobs j ON uj.job_id WHERE uj.user_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int jobId = resultSet.getInt("job_id");
                String jobName = resultSet.getString("job");
                String startDate = resultSet.getString("start_date");
                String endDate = resultSet.getString("end_date");
                Job job = new Job(jobId,jobName,startDate,endDate);
                userJobs.add(job);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return userJobs;
    }// return user's jobs list
    public void clearUserJobs(int userId){
        String query =
                "DELETE FROM user_jobs WHERE user_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userId);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }// clear user's jobs list
    public void editUserJobs(int userId, List<Job> editedJobs){
        clearUserJobs(userId);
        String query =
                "INSERT INTO user_jobs (user_id, job_id, start_date, end_date) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for (Job job: editedJobs){
                statement.setInt(1,userId);
                statement.setInt(2,job.getJobId());
                statement.setString(3, job.getStartDate());
                statement.setString(4, job.getEndDate());
                statement.executeUpdate();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }// clear user's jobs list then update with new list
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
