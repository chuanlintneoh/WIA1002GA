import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Notification {
    private final int from;
    private final int to;
    private final String description;
    private final String datetime;
    private final boolean seen;
    public Notification(int from, int to, String description){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.now();
        this.from = from;
        this.to = to;
        this.datetime = formatter.format(dateTime);
        this.description = description;
        this.seen = false;
    }// create notification
    public Notification(int from, int to, String description, String datetime, boolean seen){
        this.from = from;
        this.to = to;
        this.description = description;
        this.datetime = datetime;
        this.seen = seen;
    }// get notification
    public int getFrom(){
        return from;
    }
    public int getTo(){
        return to;
    }
    public String getDescription(){
        return description;
    }
    public String getDate(){
        return datetime;
    }
    public boolean isSeen(){
        return seen;
    }
}