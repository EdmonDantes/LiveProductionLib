package ru.liveproduction.livelib.math.expression;

/**
 * Throw this exception when in your OperationMethod throw any Throwable errors
 */
public class UserMethodException extends Exception {
    protected String operationTag;
    protected int countOperationArgs;
    protected Throwable userError;

    public UserMethodException(String operationTag, int countOperationArgs, Throwable userError) {
        this.operationTag = operationTag;
        this.countOperationArgs = countOperationArgs;
        this.userError = userError;
    }

    /**
     * @return How many operation`s args you send to method
     */
    public int getCountOperationArgs() {
        return countOperationArgs;
    }

    /**
     * @return What is the operation executed
     */
    public String getOperationTag() {
        return operationTag;
    }

    /**
     * @return Error throwed in user method
     */
    public Throwable getUserError() {
        return userError;
    }
}
