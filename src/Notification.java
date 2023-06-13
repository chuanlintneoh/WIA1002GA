import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
public class Notification {
    private final int from;
    private final int to;
    private final String description;
    private final String date;
    private final boolean seen;
    public Notification(int from, int to, String description){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate date = LocalDate.now();
        this.from = from;
        this.to = to;
        this.description = description;
        this.date = formatter.format(date);
        this.seen = false;
    }// create notification
    public Notification(int from, int to, String description, String date, boolean seen){
        this.from = from;
        this.to = to;
        this.description = description;
        this.date = date;
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
        return date;
    }
    public boolean isSeen(){
        return seen;
    }
}
