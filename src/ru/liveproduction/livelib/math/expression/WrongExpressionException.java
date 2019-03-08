/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

package ru.liveproduction.livelib.math.expression;

public class WrongExpressionException extends Exception {
    protected String expression;

    public WrongExpressionException(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }
}
