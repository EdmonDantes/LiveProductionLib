package ru.liveproduction.livelib.math.expression;

public class WrongExpressionForPolishNotationException extends Exception {
    protected String expression;

    public WrongExpressionForPolishNotationException(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }
}
