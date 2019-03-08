package ru.liveproduction.livelib.math.expression;

/**
 * This interface set action on calculate operation
 * @param <K> Your class
 */
public interface OperationFunction<K> {

    /**
     * Action when calculate operation
     * @param args Operation arguments
     * @return New object with class K
     */
    K execute(K[] args);
}
