/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

package ru.liveproduction.livelib.math.expression2;


public interface OperationFunction<K> {

    /**
     * Action when calculate operation
     * @param args Operator arguments
     * @return New object with class K
     */
    K execute(K[] args);
}
