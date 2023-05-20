public class Hobby {
    private int hobbyId;
    private String hobbyName;
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
}
