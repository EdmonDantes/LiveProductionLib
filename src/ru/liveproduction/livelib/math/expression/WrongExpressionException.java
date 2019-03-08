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
