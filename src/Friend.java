public class Friend {
    private final int userId;
    private final String status;
    private final String name;
    private final String username;
    public Friend(int userId,String status,String name,String username){
        this.userId = userId;
        this.status = status;
        this.name = name;
        this.username = username;
    }
    public int getUserId(){
        return userId;
    }
    public String getStatus(){
        return status;
    }
    public String getName(){
        return name;
    }
    public String getUsername() {
        return username;
    }
    @Override
    public String toString(){
        return String.format("User ID: %d, Name: %s, Username: %s, Status: %s",userId,name,username,status);
    }
}
