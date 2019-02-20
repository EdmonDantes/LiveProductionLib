import ru.liveproduction.livelib.math.ExpressionObject;

public class TestMain {
    public static void main(String[] args) throws ExpressionObject.UserOperationException {
        String str = "1 + 2 + 32 + (25 - (12 + 3) * 52) / 2 + 2".replace(" ", "");

        System.out.println(ExpressionObject.execInteger(str));
        System.out.println("---------------");
    }
}
