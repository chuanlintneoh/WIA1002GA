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
        if (!pageStack.isEmpty()) {
            Page previousPage = pageStack.pop(); // Pop the current page from the stack
            if (!pageStack.isEmpty()) {
                Page previousPageData = pageStack.peek(); // Get the previous page object
                // Retrieve the class name
                Class<?> pageClass = previousPage.getClass();
                String className = pageClass.getName();
                // Retrieve the arguments
                // Assuming the previous page class has a constructor with arguments
                Constructor<?> constructor = pageClass.getConstructors()[0];
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] arguments = new Object[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    // Assuming the arguments are stored as instance variables in the previous page object
                    try {
                        Field field = pageClass.getDeclaredField("arg" + i); // Assuming the argument variables are named as "arg0", "arg1", etc.
                        field.setAccessible(true);
                        arguments[i] = field.get(previousPageData);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                // Recreate the previous page object
                try {
                    Constructor<?> newConstructor = pageClass.getConstructor(parameterTypes);
                    newConstructor.newInstance(arguments);
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                         InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }
    public void clear(){
        pageStack.clear();
    }
}
