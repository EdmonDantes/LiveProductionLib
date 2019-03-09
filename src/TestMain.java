import ru.liveproduction.livelib.math.expression.*;

/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/
public class TestMain {

    public static void main(String[] args) throws Exception {
        String str = "-1 + 2 + 3 + (5 + 6 * (25 - 65 / (1 + 2) + 9) + 4) + 1 + 2 + 2 * (32 + 4)";

        // PolishNotation
        //  int  1,-,2,+,3,+,5,6,25,65,1,2,+,/,-,9,+,*,+,4,+,+,1,2,+,2,32,4,+,*,+,+
        //double 1,-,2,+,3,+,5,6,25,65,1,2,+,/,-,9,+,*,+,4,+,+,1,2,+,2,32,4,+,*,+,+

        System.out.println(ExpressionObject.calculateDouble(str));
        System.out.println(ExpressionObject.calculateInteger(str));
        for (var obj : EquationObject.valueOf("2x2 - 18").getX())
            System.out.println(obj);


        System.out.println("---------------");
    }
}
