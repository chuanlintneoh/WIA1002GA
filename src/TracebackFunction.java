import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
    public void clear(){
        pageStack.clear();
    }
}
