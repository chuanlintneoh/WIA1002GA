public class Hobby {
    private final int hobbyId;
    private final String hobbyName;
    public Hobby(int hobbyId){
        Database database = new Database();
        this.hobbyId = hobbyId;
        this.hobbyName = database.getHobby(hobbyId);
        database.close();
    }
    public Hobby(int hobbyId,String hobbyName){
        this.hobbyId = hobbyId;
        this.hobbyName = hobbyName;
    }
    public int getHobbyId(){
        return hobbyId;
    }
    public String getHobbyName(){
        return hobbyName;
    }
    @Override
    public String toString(){
        return String.format("Hobby ID: %d, Hobby Name: %s",hobbyId,hobbyName);
    }
}
