import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
public class Message {
    private final int from;
    private final int to;
    private final String text;
    private final String timeStamp;
    public Message(int from, int to, String text){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate date = LocalDate.now();
        this.from = from;
        this.to = to;
        this.text = text;
        this.timeStamp = formatter.format(date);
    }// send
    public Message(int from, int to, String text, String timeStamp){
        this.from = from;
        this.to = to;
        this.text = text;
        this.timeStamp = timeStamp;
    }// retrieve
    public int getFrom() {
        return from;
    }
    public int getTo() {
        return to;
    }
    public String getText() {
        return text;
    }
    public String getTimeStamp() {
        return timeStamp;
    }
}
