/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

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
