/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

package ru.liveproduction.livelib.math.expression;

/**
 * Throw this exception when you send wrong argument to your OperationMethod
 */
public class WrongCountOperationArgumentsException extends Exception {
    protected int needCountOperationArgs;
    protected int realCountOperationArgs;

    public WrongCountOperationArgumentsException(int needCountOperationArgs, int realCountOperationArgs) {
        this.needCountOperationArgs = needCountOperationArgs;
        this.realCountOperationArgs = realCountOperationArgs;
    }

    /**
     * @return How many operations arguments you send to method
     */
    public int getRealCountOperationArgs() {
        return realCountOperationArgs;
    }

    /**
     * @return How many operations arguments need this method
     */
    public int getNeedCountOperationArgs() {
        return needCountOperationArgs;
    }
}
