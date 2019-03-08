package ru.liveproduction.livelib.math.expression;

import java.util.*;

/**
 *
 * @param <K> Your class
 */
public class Operation<K> {
    protected String operationTag;

    protected Set<String> operationStringSynonyms;
    protected int maxLengthOfStringSynonyms;

    protected int countOperationArgs;
    protected String argumentSeparator;

    protected OperationFunction<K> operationFunction;

    protected boolean suffixForm; // Only for one-arg operation like "2!"

    /**
     * Don`t will override in children
     */
    private Operation() {}

    public Operation(String operationTag, Set<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, String argumentSeparator, boolean suffixForm) {
        this.operationTag = operationTag;
        this.operationStringSynonyms = operationStringSynonyms;
        this.maxLengthOfStringSynonyms = 0;
        for (String synonym : operationStringSynonyms) {
            if (synonym.length() > this.maxLengthOfStringSynonyms) this.maxLengthOfStringSynonyms = synonym.length();
        }
        this.countOperationArgs = countOperationArgs;
        this.operationFunction = operationFunction;
        this.argumentSeparator = argumentSeparator;
        this.suffixForm = suffixForm;
    }

    public Operation(String operationTag, List<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, String argumentSeparator, boolean suffixForm) {
        this(operationTag, new TreeSet<String>(operationStringSynonyms), countOperationArgs, operationFunction, argumentSeparator, suffixForm);
    }

    public Operation(String operationTag, String[] operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, String argumentSeparator, boolean suffixForm) {
        this(operationTag, Arrays.asList(operationStringSynonyms), countOperationArgs, operationFunction, argumentSeparator, suffixForm);
    }

    public Operation(String operationTag, Set<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, boolean suffixForm){
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, ";", suffixForm);
    }

    public Operation(String operationTag, List<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, boolean suffixForm){
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, ";", suffixForm);
    }

    public Operation(String operationTag, String[] operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, boolean suffixForm){
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, ";", suffixForm);
    }

    public Operation(String operationTag, Set<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction){
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, ";", false);
    }

    public Operation(String operationTag, List<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction){
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, ";", false);
    }

    public Operation(String operationTag, String[] operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction) {
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, ";", false);
    }
    
    public Operation(String operationTag, List<String> operationStringSynonyms, OperationFunction<K> operationFunction) {
        this(operationTag, operationStringSynonyms, 2, operationFunction, ";", false);
    }

    public Operation(String operationTag, Set<String> operationStringSynonyms, OperationFunction<K> operationFunction) {
        this(operationTag, operationStringSynonyms, 2, operationFunction, ";", false);
    }

    public Operation(String operationTag, String[] operationStringSynonyms, OperationFunction<K> operationFunction) {
        this(operationTag, operationStringSynonyms, 2, operationFunction, ";", false);
    }

    public Operation(String operationTag, String operationStringSynonym, OperationFunction<K> operationFunction) {
        this(operationTag, Collections.singleton(operationStringSynonym), 2, operationFunction, ";", false);
    }

    public String getOperationTag() {
        return operationTag;
    }

    public boolean haveStringSynonym(String stringSynonym) {
        return operationStringSynonyms.contains(stringSynonym);
    }

    public int indexOfStringSynonymInString(String str, int startIndex, int lastIndex) {
        for (int i = startIndex + 1; i <= lastIndex && i <= str.length() && i - startIndex <= maxLengthOfStringSynonyms; i++) {
            if (haveStringSynonym(str.substring(startIndex, i))) return i;
        }

        return -1;
    }

    public int indexOfStringSynonymInString(String str, int startIndex) {
        return indexOfStringSynonymInString(str, startIndex, str.length());
    }

    public int indexOfStringSynonymInString(String str){
        return indexOfStringSynonymInString(str, 0);
    }

    public boolean haveStringSynonymInString(String str, int startIndex, int lastIndex) {
        return indexOfStringSynonymInString(str, startIndex, lastIndex) > -1;
    }

    public boolean haveStringSynonymInString(String str, int startIndex) {
        return haveStringSynonymInString(str, startIndex, str.length());
    }

    public boolean haveStringSynonymInString(String str) {
        return haveStringSynonymInString(str, 0);
    }

    public int getMaxLengthOfStringSynonyms() {
        return maxLengthOfStringSynonyms;
    }

    public int getCountOperationArgs() {
        return countOperationArgs;
    }

    public String getArgumentSeparator() {
        return argumentSeparator;
    }

    /**
     * Execute operation
     * @param args Operation`s arguments
     * @return New value
     * @throws WrongCountOperationArgumentsException
     * @throws UserMethodException
     */
    public K execute(K[] args) throws WrongCountOperationArgumentsException, UserMethodException {
        if (args.length == this.countOperationArgs) {
            try {
                return operationFunction.execute(args);
            } catch (Throwable e) {
                throw new UserMethodException(operationTag, countOperationArgs, e);
            }
        } else throw new WrongCountOperationArgumentsException(this.countOperationArgs, args.length);
    }

    public boolean haveSuffixForm() {
        return suffixForm;
    }
}
