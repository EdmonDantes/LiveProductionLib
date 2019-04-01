import ru.liveproduction.livelib.math.EquationObject;
import ru.liveproduction.livelib.math.expression2.*;

/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/
public class TestMain {

    public static void main(String[] args) throws Exception {
        String str = "-1 + 2 + 3 + (5 + 6 * (25 - 65 / (1 + 2) + 9) + 4) + 1 + 2 + 2 * (32 + 4)";
        String str2 = "div(25, 2)";

        // PolishNotation
        //  int  1,-,2,+,3,+,5,6,25,65,1,2,+,/,-,9,+,*,+,4,+,+,1,2,+,2,32,4,+,*,+,+
        //double 1,-,2,+,3,+,5,6,25,65,1,2,+,/,-,9,+,*,+,4,+,+,1,2,+,2,32,4,+,*,+,+

        System.out.println(PolishNotation.getInstance().createPolishNotation(str, ExpressionObject.doublesManager));
//        var start = System.nanoTime();
        double test = ExpressionObject.calculateDouble(str);
//        var end = System.nanoTime();
        System.out.println(test);
//
//        System.out.println(end - start);
//        System.out.println(ExpressionObject.calculateInteger(str));
//        for (var obj : EquationObject.valueOf("2x2 - 18").getX())
//            System.out.println(obj);

        OperationManager<Double> operationManager = new OperationManager<>(new Operation[][]{
                new Operation[]{
                        new Operation<Double>("DIVF", "div", 3, new OperationFunction<Double>() {
                            @Override
                            public Double execute(Double[] args) {
                                return args[0] / args[1];
                            }
                        }, new String[]{"$", "^"}, ";", false, true)
                },
                new Operation[]{
                        new Operation<Double>("DIV", "div", 1, new OperationFunction<Double>() {
                            @Override
                            public Double execute(Double[] args) {
                                return args[0] / 10;
                            }
                        }, null, ";", false, false)
                }
        });

        System.out.println(PolishNotation.getInstance().createPolishNotation(str2, operationManager, true));


        System.out.println("---------------");
    }
}
