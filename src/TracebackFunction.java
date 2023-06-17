import java.util.LinkedList;
import java.util.Stack;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class TracebackFunction {
    private Stack<Page> pageStack;
    private LinkedList<String> pageHistory;
    public TracebackFunction(){
        pageStack = new Stack<>();
        pageHistory = new LinkedList<>();
    }
    private String getFormattedTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm:ss");
        return now.format(formatter);
    }
    public void pushPage(Page page){
        pageStack.push(page);
        pageHistory.add("[" + getFormattedTimestamp() + "]: " + page.getTitle());
    }
    public void popPeek() {
        if (!pageStack.isEmpty()){
            pageStack.pop();
            if (!pageStack.isEmpty()){
                pageStack.peek().showPage();
                pageHistory.add("[" + getFormattedTimestamp() + "]: " + pageStack.peek().getTitle());
            }
        }
    }
    public void peek() {
        if(!pageStack.isEmpty()){
            pageStack.peek().showPage();
        }
    }// refresh page
    public void addHistory(String activity){
        pageHistory.add("[" + getFormattedTimestamp() + "]: " + activity);
    }
    public LinkedList<String> getPageHistory(){
        return pageHistory;
    }
    public void clear(){
        pageStack.clear();
        pageHistory.clear();
    }
}