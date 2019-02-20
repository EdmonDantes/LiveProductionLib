/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

import ru.liveproduction.livelib.math.EquationObject;
import ru.liveproduction.livelib.math.ExpressionObject;

public class TestMain {
    public static void main(String[] args) throws ExpressionObject.UserOperationException {
        String str = "1 + 2 + 32 + (25 - (12 + 3) * 52) / 2 + 2".replace(" ", "");

        System.out.println(ExpressionObject.execInteger("(-4)*8*10+4*16+4*8^3-18*2*8*10-27*100"));

        System.out.println(EquationObject.valueOf("x2 - 5x + 6").getX());
        System.out.println("---------------");
    }
}
