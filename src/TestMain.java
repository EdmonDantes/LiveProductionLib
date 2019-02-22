/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

import ru.liveproduction.livelib.math.ExpressionObject;

public class TestMain {
    public static void main(String[] args) throws ExpressionObject.WrongArgumentsCountOperationException, ExpressionObject.OperationManager.WrongOperationPriority {
        var test = ExpressionObject.calcDouble("1 + 2 + 3 × (1 + 5 + 6 × ( 12 − 56) ÷ 85) ÷ 2 + 58");
        System.out.println(test);
        System.out.println("---------------");
    }
}
