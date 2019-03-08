package ru.liveproduction.livelib.math.expression;

/**
 * This interface set action on execute operation
 * @param <K> Your class
 */
public interface OperationFunction<K> {

    /**
     * Action when execute operation
     * @param args Operation arguments
     * @return New object with class K
     */
    K execute(K[] args);
}
