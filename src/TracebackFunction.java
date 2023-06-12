import java.util.Stack;
public class TracebackFunction {
    private Stack<Page> pageStack;
    public TracebackFunction(){
        pageStack = new Stack<>();
    }
    public void pushPage(Page page){
        pageStack.push(page);
    }
    public void popPeek() {
        if (!pageStack.isEmpty()){
            pageStack.pop();
            if (!pageStack.isEmpty()){
                pageStack.peek().showPage();
            }
        }
    }
    public void peek(){
        if(!pageStack.isEmpty()){
            pageStack.peek().showPage();;
        }
    }//refresh page
    public void clear(){
        pageStack.clear();
    }
}
