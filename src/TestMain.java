/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

import ru.liveproduction.livelib.math.EquationObject;
import ru.liveproduction.livelib.math.ExpressionObject;
import ru.liveproduction.livelib.net.HTTPServer;

public class TestMain {
    public static void main(String[] args) throws ExpressionObject.UserOperationException {
        new HTTPServer(8080).start();
        System.out.println("---------------");
    }
}
