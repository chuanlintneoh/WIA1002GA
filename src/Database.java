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
    public List<Friend> viewFriendsRequests(int userId){
        List<Friend> friendsRequests = new ArrayList<>();
        String query =
                "SELECT uf.user_id, uf.friend_id, s.status, u.name AS friend_name, u.username AS friend_username " +
                "FROM user_friends uf " +
                "JOIN status s ON uf.status = s.id " +
                "JOIN users u ON uf.friend_id = u.user_id " +
                "WHERE uf.user_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int friendId = resultSet.getInt("friend_id");
                String status = resultSet.getString("status");
                String friendName = resultSet.getString("friend_name");
                String friendUsername = resultSet.getString("friend_username");
                Friend friend = new Friend(friendId,status,friendName,friendUsername);
                friendsRequests.add(friend);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return friendsRequests;
    }// view current friends, friend requests (received,sent)
    public void insertStatus(int userId,int friendId,int statusId){
        //***Note: always do twice, for send and receive
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
    }// send request, receive request
    public void updateStatus(int userId, int friendId,int statusId){// update 2 rows in database
        //***Note: do once is enough
        //statusId = 3
        String query =
                "UPDATE user_friends SET status = ? WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";
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
        //***Note: do once is enough
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
    //user_hobbies
    public List<String> viewUserHobbies(int userId){
        List<String> userHobbies = new ArrayList<>();
        String query =
                "SELECT hobby FROM user_hobbies WHERE user_id = ?";
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
    public void editUserHobbies(int userId, List<String> editedHobbies){
        clearUserHobbies(userId);
        String query =
                "INSERT INTO user_hobbies (user_id, hobby) VALUES (?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for (String hobby: editedHobbies){
                statement.setInt(1,userId);
                statement.setString(2,hobby);
                statement.executeUpdate();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }// clear user's hobbies list then update with new list
    //jobs & user_jobs
    public List<Job> getJobs(){
        List<Job> jobs = new ArrayList<>();
        String query =
                "SELECT id, job FROM jobs";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int jobId = resultSet.getInt("id");
                String jobName = resultSet.getString("job");
                jobs.add(new Job(jobId,jobName));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return jobs;
    }// view ALL available jobs
    public int getJobId(String jobName){
        String query =
                "SELECT id FROM jobs WHERE job = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,jobName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt("id");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
    public String getJob(int jobId){
        String query =
                "SELECT job FROM jobs WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,jobId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return resultSet.getString("job");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return "Undefined Job";
    }
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
    public Stack<Job> viewUserJobs(int userId){
        Stack<Job> userJobs = new Stack<>();
        String query =
                "SELECT uj.job_id, j.job, uj.start_date, uj.end_date " +
                "FROM user_jobs uj " +
                "JOIN jobs j ON uj.job_id = j.id " +
                "WHERE uj.user_id = ?";
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
                userJobs.push(job);
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
    public void editUserJobs(int userId, Stack<Job> editedJobs){
        Stack<Job> reversedEditedJobs = new Stack<>();
        while (!editedJobs.isEmpty()){
            Job job = editedJobs.pop();
            reversedEditedJobs.push(job);
        }
        clearUserJobs(userId);
        String query =
                "INSERT INTO user_jobs (user_id, job_id, start_date, end_date) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            while (!reversedEditedJobs.isEmpty()){
                Job job = reversedEditedJobs.pop();
                statement.setInt(1, userId);
                statement.setInt(2, job.getJobId());
                statement.setString(3, job.getStartDate());
                statement.setString(4, job.getEndDate());
                statement.executeUpdate();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }// clear user's jobs list then update with new list
    public void pushUserJob(int userId, Job o){
        String query =
                "INSERT INTO user_jobs (user_id, job_id, start_date, end_date) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, o.getJobId());
            statement.setString(3,o.getStartDate());
            statement.setString(4,o.getEndDate());
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void popUserJob(int userId){
        Stack<Job> userJobs = viewUserJobs(userId);
        if (!userJobs.isEmpty()){
            userJobs.pop();
            if (!userJobs.isEmpty()){
                editUserJobs(userId,userJobs);
            }
            else {
                clearUserJobs(userId);
            }
        }
    }
    //Search users
    public List<Friend> searchUser(int userId, String keyword){
        List<Friend> searchResult = new ArrayList<>();
        String query =
                "SELECT u.user_id, u.name, u.username, s.status " +
                "FROM users u " +
                "LEFT JOIN user_friends uf ON u.user_id = uf.friend_id AND uf.user_id = ? " +
                "LEFT JOIN status s ON uf.status = s.id " +
                "WHERE u.name LIKE ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userId);
            statement.setString(2,"%"+keyword+"%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int user_id = resultSet.getInt("user_id");
                if(user_id != userId){// excepting user himself in the search list
                    String name = resultSet.getString("name");
                    String username = resultSet.getString("username");
                    String status = resultSet.getString("status");
                    searchResult.add(new Friend(user_id,status,name,username));
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return searchResult;
    }
    //Find mutual friends
    public List<Friend> findMutualFriends(int userId){
        List<Friend> mutualFriends = new ArrayList<>();
        String query =
                "SELECT u.user_id, u.name, u.username, s.status " +
                "FROM user_friends uf1 " +
                "JOIN user_friends uf2 ON uf1.friend_id = uf2.user_id " +
                "JOIN users u ON uf2.friend_id = u.user_id " +
                "JOIN status s ON uf2.status = s.id " +
                "WHERE uf1.user_id = ? AND uf1.status = 3 AND uf2.status = 3";
        //uf1 = user's friends, uf2 = user's mutual friends
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int user_id = resultSet.getInt("user_id");
                if (user_id != userId){// excepting user himself as a mutual friend
                    String name = resultSet.getString("name");
                    String username = resultSet.getString("username");
                    String status = "Mutual friend";
                    mutualFriends.add(new Friend(user_id,status,name,username));
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return mutualFriends;
    }
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
                "SELECT user_id, name, username, email_address, contact_no FROM users";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int userId = resultSet.getInt("user_id");
                String name = resultSet.getString("name");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email_address");
                String contactNo = resultSet.getString("contact_no");
                allUsers.add(new User(userId,name,username,email,contactNo));
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
