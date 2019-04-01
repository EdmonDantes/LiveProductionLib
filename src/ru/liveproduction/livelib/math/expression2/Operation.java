/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

package ru.liveproduction.livelib.math.expression2;

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

    protected String[] argumentsSeparator;
    protected String defaultArgumentSeparator;

    protected OperationFunction<K> operationFunction;

    protected boolean suffixForm; // Only for one-arg operation like "2!"

    protected boolean isFunction;

    /**
     * Don`t will override in children
     */
    private Operation() {}

    public Operation(String operationTag, Set<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, String[] argumentSeparator, String defaultArgumentSeparator, boolean suffixForm, boolean isFunction) {
        this.operationTag = operationTag;
        this.operationStringSynonyms = operationStringSynonyms;
        this.maxLengthOfStringSynonyms = 0;
        for (String synonym : operationStringSynonyms) {
            if (synonym.length() > this.maxLengthOfStringSynonyms) this.maxLengthOfStringSynonyms = synonym.length();
        }
        this.countOperationArgs = countOperationArgs;
        this.operationFunction = operationFunction;
        this.argumentsSeparator = argumentSeparator;
        this.defaultArgumentSeparator = defaultArgumentSeparator;
        this.suffixForm = suffixForm;
        this.isFunction = isFunction;
    }

    public Operation(String operationTag, List<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, String[] argumentSeparator, String defaultArgumentSeparator, boolean suffixForm, boolean isFunction) {
        this(operationTag, new TreeSet<String>(operationStringSynonyms), countOperationArgs, operationFunction, argumentSeparator, defaultArgumentSeparator, suffixForm, isFunction);
    }

    public Operation(String operationTag, String[] operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, String[] argumentSeparator, String defaultArgumentSeparator, boolean suffixForm, boolean isFunction) {
        this(operationTag, Arrays.asList(operationStringSynonyms), countOperationArgs, operationFunction, argumentSeparator, defaultArgumentSeparator, suffixForm, isFunction);
    }

    public Operation(String operationTag, String operationStringSynonym, int countOperationArgs, OperationFunction<K> operationFunction, String[] argumentSeparator, String defaultArgumentSeparator, boolean suffixForm, boolean isFunction) {
        this(operationTag, Collections.singleton(operationStringSynonym), countOperationArgs, operationFunction, argumentSeparator, defaultArgumentSeparator, suffixForm, isFunction);
    }

    public Operation(String operationTag, Set<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, String[] argumentSeparator, String defaultArgumentSeparator, boolean suffixForm) {
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, argumentSeparator, defaultArgumentSeparator, suffixForm, false);
    }

    public Operation(String operationTag, List<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, String[] argumentSeparator, String defaultArgumentSeparator, boolean suffixForm) {
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, argumentSeparator, defaultArgumentSeparator, suffixForm, false);
    }

    public Operation(String operationTag, String[] operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, String[] argumentSeparator, String defaultArgumentSeparator, boolean suffixForm) {
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, argumentSeparator, defaultArgumentSeparator, suffixForm, false);
    }

    public Operation(String operationTag, String operationStringSynonym, int countOperationArgs, OperationFunction<K> operationFunction, String[] argumentSeparator, String defaultArgumentSeparator, boolean suffixForm) {
        this(operationTag, operationStringSynonym, countOperationArgs, operationFunction, argumentSeparator, defaultArgumentSeparator, suffixForm, false);
    }

    public Operation(String operationTag, Set<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, String defaultArgumentSeparator, boolean suffixForm) {
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, null, defaultArgumentSeparator, suffixForm);
    }

    public Operation(String operationTag, List<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, String defaultArgumentSeparator, boolean suffixForm) {
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, null, defaultArgumentSeparator, suffixForm);
    }

    public Operation(String operationTag, String[] operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, String defaultArgumentSeparator, boolean suffixForm) {
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, null, defaultArgumentSeparator, suffixForm);
    }

    public Operation(String operationTag, String operationStringSynonym, int countOperationArgs, OperationFunction<K> operationFunction, String defaultArgumentSeparator, boolean suffixForm) {
        this(operationTag, operationStringSynonym, countOperationArgs, operationFunction, null, defaultArgumentSeparator, suffixForm);
    }

    public Operation(String operationTag, Set<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, boolean suffixForm) {
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, ";", suffixForm);
    }

    public Operation(String operationTag, List<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, boolean suffixForm) {
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, ";", suffixForm);
    }

    public Operation(String operationTag, String[] operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction, boolean suffixForm) {
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, ";", suffixForm);
    }

    public Operation(String operationTag, String operationStringSynonym, int countOperationArgs, OperationFunction<K> operationFunction, boolean suffixForm) {
        this(operationTag, operationStringSynonym, countOperationArgs, operationFunction, ";", suffixForm);
    }

    public Operation(String operationTag, Set<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction) {
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, true);
    }

    public Operation(String operationTag, List<String> operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction) {
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, true);
    }

    public Operation(String operationTag, String[] operationStringSynonyms, int countOperationArgs, OperationFunction<K> operationFunction) {
        this(operationTag, operationStringSynonyms, countOperationArgs, operationFunction, true);
    }

    public Operation(String operationTag, String operationStringSynonym, int countOperationArgs, OperationFunction<K> operationFunction) {
        this(operationTag, operationStringSynonym, countOperationArgs, operationFunction, true);
    }

    public Operation(String operationTag, Set<String> operationStringSynonyms, OperationFunction<K> operationFunction, boolean suffixForm) {
        this(operationTag, operationStringSynonyms, 2, operationFunction, ";", suffixForm);
    }

    public Operation(String operationTag, List<String> operationStringSynonyms, OperationFunction<K> operationFunction, boolean suffixForm) {
        this(operationTag, operationStringSynonyms, 2, operationFunction, ";", suffixForm);
    }

    public Operation(String operationTag, String[] operationStringSynonyms, OperationFunction<K> operationFunction, boolean suffixForm) {
        this(operationTag, operationStringSynonyms, 2, operationFunction, ";", suffixForm);
    }

    public Operation(String operationTag, String operationStringSynonym, OperationFunction<K> operationFunction, boolean suffixForm) {
        this(operationTag, operationStringSynonym, 2, operationFunction, ";", suffixForm);
    }

    public Operation(String operationTag, Set<String> operationStringSynonyms, OperationFunction<K> operationFunction) {
        this(operationTag, operationStringSynonyms, operationFunction, true);
    }

    public Operation(String operationTag, List<String> operationStringSynonyms, OperationFunction<K> operationFunction) {
        this(operationTag, operationStringSynonyms, operationFunction, true);
    }

    public Operation(String operationTag, String[] operationStringSynonyms, OperationFunction<K> operationFunction) {
        this(operationTag, operationStringSynonyms, operationFunction, true);
    }

    public Operation(String operationTag, String operationStringSynonym, OperationFunction<K> operationFunction) {
        this(operationTag, operationStringSynonym, operationFunction, true);
    }

    /**
     * @return Operator tag
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
     * ONLY FOR 2 or > args operations
     * @return Separator for operation`s arguments
     */
    public String getArgumentSeparator(int index) {
        if (argumentsSeparator != null && index < argumentsSeparator.length)
            return argumentsSeparator[index];
        else
            return defaultArgumentSeparator;
    }

    /**
     * Execute operation
     * @param args Operator`s arguments
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
        return argumentsSeparator != null && argumentsSeparator.length > 0 || defaultArgumentSeparator != null && defaultArgumentSeparator.length() > 0;
    }

    public boolean isFunction(){
        return isFunction;
    }
}
