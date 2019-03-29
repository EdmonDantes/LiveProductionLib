/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

package ru.liveproduction.livelib.math.expression;

import java.util.*;

/**
 * Class for operation with your class
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

    public Operation(String operationTag, String operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, String argumentSeparator, boolean suffixForm) {
        this(operationTag, Collections.singleton(operationStringSynonyms), countOperationArgs, operationFunction, argumentSeparator, suffixForm);
    }

    public Operation(String operationTag, Set<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, String argumentSeparator){
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, argumentSeparator, false);
    }

    public Operation(String operationTag, List<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, String argumentSeparator) {
        this(operationTag, new TreeSet<String>(operationStringSynonyms), countOperationArgs, operationFunction, argumentSeparator, false);
    }

    public Operation(String operationTag, String[] operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, String argumentSeparator) {
        this(operationTag, Arrays.asList(operationStringSynonyms), countOperationArgs, operationFunction, argumentSeparator, false);
    }

    public Operation(String operationTag, String operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, String argumentSeparator) {
        this(operationTag, Collections.singleton(operationStringSynonyms), countOperationArgs, operationFunction, argumentSeparator, false);
    }

    public Operation(String operationTag, Set<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, boolean suffixForm){
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, null, suffixForm);
    }

    public Operation(String operationTag, List<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, boolean suffixForm){
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, null, suffixForm);
    }

    public Operation(String operationTag, String[] operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, boolean suffixForm){
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, null, suffixForm);
    }

    public Operation(String operationTag, String operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, boolean suffixForm){
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, null, suffixForm);
    }

    public Operation(String operationTag, Set<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction){
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, null, false);
    }

    public Operation(String operationTag, List<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction){
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, null, false);
    }

    public Operation(String operationTag, String[] operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction) {
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, null, false);
    }

    public Operation(String operationTag, String operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction) {
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, null, false);
    }

    public Operation(String operationTag, Set<String> operationStringSynonyms, OperationFunction<K> operationFunction) {
        this(operationTag, operationStringSynonyms, 2, operationFunction, null, false);
    }
    
    public Operation(String operationTag, List<String> operationStringSynonyms, OperationFunction<K> operationFunction) {
        this(operationTag, operationStringSynonyms, 2, operationFunction, null, false);
    }

    public Operation(String operationTag, String[] operationStringSynonyms, OperationFunction<K> operationFunction) {
        this(operationTag, operationStringSynonyms, 2, operationFunction, null, false);
    }

    public Operation(String operationTag, String operationStringSynonym, OperationFunction<K> operationFunction) {
        this(operationTag, Collections.singleton(operationStringSynonym), 2, operationFunction, null, false);
    }

    /**
     * @return Operation tag
     */
    public String getOperationTag() {
        return operationTag;
    }

    /**
     * Check string in array of string synonyms
     * @param stringSynonym Your string synonym
     * @return True if this operation have this stringSynonym, else False
     */
    public boolean haveStringSynonym(String stringSynonym) {
        return operationStringSynonyms.contains(stringSynonym);
    }

    /**
     * Check string synonyms on interval of string
     * @param str String
     * @param startIndex First index
     * @param lastIndex Last index. (Search will be while INDEX <= ${lastIndex}
     * @return -1 if interval hasn`t string synonym, else last index of string synonym in interval
     */
    public int indexOfStringSynonymInString(String str, int startIndex, int lastIndex) {
        for (int i = startIndex + 1; i <= lastIndex && i <= str.length() && i - startIndex <= maxLengthOfStringSynonyms; i++) {
            if (haveStringSynonym(str.substring(startIndex, i))) return i;
        }

        return -1;
    }

    /**
     * Check string synonyms on interval of string
     * @param str String
     * @param startIndex First index
     * @return -1 if interval hasn`t string synonym, else last index of string synonym in interval
     */
    public int indexOfStringSynonymInString(String str, int startIndex) {
        return indexOfStringSynonymInString(str, startIndex, str.length());
    }

    /**
     * Check string synonyms in start of string
     * @param str String
     * @return -1 if interval hasn`t string synonym, else last index of string synonym in interval
     */
    public int indexOfStringSynonymInString(String str){
        return indexOfStringSynonymInString(str, 0);
    }

    /**
     * Check string synonyms on interval of string
     * @param str String
     * @param startIndex First index
     * @param lastIndex Last index. (Search will be while INDEX <= ${lastIndex}
     * @return FALSE if interval hasn`t string synonym, else return TRUE
     */
    public boolean haveStringSynonymInString(String str, int startIndex, int lastIndex) {
        return indexOfStringSynonymInString(str, startIndex, lastIndex) > -1;
    }

    /**
     * Check string synonyms on interval of string
     * @param str String
     * @param startIndex First index
     * @return FALSE if interval hasn`t string synonym, else return TRUE
     */
    public boolean haveStringSynonymInString(String str, int startIndex) {
        return haveStringSynonymInString(str, startIndex, str.length());
    }

    /**
     * Check string synonyms in start of string
     * @param str String
     * @return FALSE if interval hasn`t string synonym, else return TRUE
     */
    public boolean haveStringSynonymInString(String str) {
        return haveStringSynonymInString(str, 0);
    }

    /**
     * @return Maximum length of all strings` synonyms
     */
    public int getMaxLengthOfStringSynonyms() {
        return maxLengthOfStringSynonyms;
    }

    /**
     * @return Get count operation`s arguments
     */
    public int getCountOperationArgs() {
        return countOperationArgs;
    }

    /**
     * ONLY FOR 3 or > -args operations
     * @return Separator for operation`s arguments
     */
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

    public boolean haveArgumentSeparator() {
        return argumentSeparator != null;
    }
}
