/*
Copyright © 2019 Ilya Loginov. All rights reserved. 
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository

Created dantes on 31.03.19 17:48
*/

package ru.liveproduction.livelib.math.expression;

/**
 * This interface set action on calculate operation
 * @param <T> Your class
 */
public interface Algorithm<T> {
    /**
     * Action when calculate operation
     * @param args Operator arguments
     * @return New object with class T
     */
    T execute(T[] args);
}
