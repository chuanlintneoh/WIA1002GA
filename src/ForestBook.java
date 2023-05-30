public class ForestBook {
    private static TracebackFunction tracebackFunction;
    public static void main(String[] args) {
        tracebackFunction = new TracebackFunction();
        tracebackFunction.pushPage(new LoginPage(tracebackFunction));
    }
}
