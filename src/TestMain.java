/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

import ru.liveproduction.livelib.math.ExpressionObject;

public class TestMain {
    public static void main(String[] args) throws ExpressionObject.UserOperationException {
        String str = "1 + 2 + 32 + (25 - (12 + 3) * 52) / 2 + 2".replace(" ", "");

        System.out.println(ExpressionObject.execInteger(str));
        System.out.println("---------------");
    }
}
