/*
Copyright © 2019 Ilya Loginov. All rights reserved. 
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository

Created dantes on 31.03.19 17:40
*/

package ru.liveproduction.livelib.math.expression;

import ru.liveproduction.livelib.math.expression2.UserMethodException;
import ru.liveproduction.livelib.math.expression2.WrongCountOperationArgumentsException;

import java.util.*;

public class Operator<T> {
    protected String tag;
    protected Set<String> stringSynonyms;
    protected int maxLengthOfStringSynonyms;
    protected int countArgs;
    protected String[] argumentsSeparator;
    protected String defaultArgumentSeparator;
    protected Algorithm<T> algorithm;
    protected boolean haveSuffixForm;

    public Operator(String tag, Set<String> operationStringSynonyms, int countArgs, Algorithm<T> algorithm, String[] argumentSeparator, String defaultArgumentSeparator, boolean haveSuffixForm) {
        this.tag = tag;
        this.stringSynonyms = operationStringSynonyms;
        this.maxLengthOfStringSynonyms = 0;
        for (String synonym : operationStringSynonyms) {
            if (synonym.length() > this.maxLengthOfStringSynonyms) this.maxLengthOfStringSynonyms = synonym.length();
        }
        this.countArgs = countArgs;
        this.algorithm = algorithm;
        this.argumentsSeparator = argumentSeparator;
        this.defaultArgumentSeparator = defaultArgumentSeparator;
        this.haveSuffixForm = haveSuffixForm;
    }

    public Operator(String tag, List<String> operationStringSynonyms, int countArgs, Algorithm<T> algorithm, String[] argumentSeparator, String defaultArgumentSeparator, boolean haveSuffixForm) {
        this(tag, new TreeSet<String>(operationStringSynonyms), countArgs, algorithm, argumentSeparator, defaultArgumentSeparator, haveSuffixForm);
    }

    public Operator(String tag, String[] operationStringSynonyms, int countArgs, Algorithm<T> algorithm, String[] argumentSeparator, String defaultArgumentSeparator, boolean haveSuffixForm) {
        this(tag, Arrays.asList(operationStringSynonyms), countArgs, algorithm, argumentSeparator, defaultArgumentSeparator, haveSuffixForm);
    }

    public Operator(String tag, String operationStringSynonyms, int countArgs, Algorithm<T> algorithm, String[] argumentSeparator, String defaultArgumentSeparator, boolean haveSuffixForm) {
        this(tag, Collections.singleton(operationStringSynonyms), countArgs, algorithm, argumentSeparator, defaultArgumentSeparator, haveSuffixForm);
    }

    public Operator(String tag, Set<String> operationStringSynonyms, int countArgs, Algorithm<T> algorithm, String[] argumentSeparator, String defaultArgumentSeparator) {
        this(tag, operationStringSynonyms, countArgs, algorithm, argumentSeparator, defaultArgumentSeparator, true);
    }

    public Operator(String tag, List<String> operationStringSynonyms, int countArgs, Algorithm<T> algorithm, String[] argumentSeparator, String defaultArgumentSeparator) {
        this(tag, operationStringSynonyms, countArgs, algorithm, argumentSeparator, defaultArgumentSeparator, true);
    }

    public Operator(String tag, String[] operationStringSynonyms, int countArgs, Algorithm<T> algorithm, String[] argumentSeparator, String defaultArgumentSeparator) {
        this(tag, operationStringSynonyms, countArgs, algorithm, argumentSeparator, defaultArgumentSeparator, true);
    }

    public Operator(String tag, String operationStringSynonyms, int countArgs, Algorithm<T> algorithm, String[] argumentSeparator, String defaultArgumentSeparator) {
        this(tag, operationStringSynonyms, countArgs, algorithm, argumentSeparator, defaultArgumentSeparator, true);
    }

    public Operator(String tag, Set<String> operationStringSynonyms, int countArgs, Algorithm<T> algorithm, String defaultArgumentSeparator) {
        this(tag, operationStringSynonyms, countArgs, algorithm, new String[0], defaultArgumentSeparator, true);
    }

    public Operator(String tag, List<String> operationStringSynonyms, int countArgs, Algorithm<T> algorithm, String defaultArgumentSeparator) {
        this(tag, operationStringSynonyms, countArgs, algorithm, new String[0], defaultArgumentSeparator, true);
    }

    public Operator(String tag, String[] operationStringSynonyms, int countArgs, Algorithm<T> algorithm, String defaultArgumentSeparator) {
        this(tag, operationStringSynonyms, countArgs, algorithm, new String[0], defaultArgumentSeparator, true);
    }

    public Operator(String tag, String operationStringSynonyms, int countArgs, Algorithm<T> algorithm, String defaultArgumentSeparator) {
        this(tag, operationStringSynonyms, countArgs, algorithm, new String[0], defaultArgumentSeparator, true);
    }

    public Operator(String tag, Set<String> operationStringSynonyms, int countArgs, Algorithm<T> algorithm, String defaultArgumentSeparator, boolean haveSuffixForm) {
        this(tag, operationStringSynonyms, countArgs, algorithm, new String[0], defaultArgumentSeparator, haveSuffixForm);
    }

    public Operator(String tag, List<String> operationStringSynonyms, int countArgs, Algorithm<T> algorithm, String defaultArgumentSeparator, boolean haveSuffixForm) {
        this(tag, new TreeSet<String>(operationStringSynonyms), countArgs, algorithm, new String[0], defaultArgumentSeparator, haveSuffixForm);
    }

    public Operator(String tag, String[] operationStringSynonyms, int countArgs, Algorithm<T> algorithm, String defaultArgumentSeparator, boolean haveSuffixForm) {
        this(tag, Arrays.asList(operationStringSynonyms), countArgs, algorithm, new String[0], defaultArgumentSeparator, haveSuffixForm);
    }

    public Operator(String tag, String operationStringSynonyms, int countArgs, Algorithm<T> algorithm, String defaultArgumentSeparator, boolean haveSuffixForm) {
        this(tag, Collections.singleton(operationStringSynonyms), countArgs, algorithm, new String[0], defaultArgumentSeparator, haveSuffixForm);
    }

    public Operator(String tag, Set<String> operationStringSynonyms, Algorithm<T> algorithm, boolean haveSuffixForm) {
        this(tag, operationStringSynonyms, 2, algorithm, new String[0], ";", haveSuffixForm);
    }

    public Operator(String tag, List<String> operationStringSynonyms, Algorithm<T> algorithm, boolean haveSuffixForm) {
        this(tag, operationStringSynonyms, 2, algorithm, new String[0], ";", haveSuffixForm);
    }

    public Operator(String tag, String[] operationStringSynonyms, Algorithm<T> algorithm, boolean haveSuffixForm) {
        this(tag, operationStringSynonyms, 2, algorithm, new String[0], ";", haveSuffixForm);
    }

    public Operator(String tag, String operationStringSynonyms, Algorithm<T> algorithm, boolean haveSuffixForm) {
        this(tag, operationStringSynonyms, 2, algorithm, new String[0], ";", haveSuffixForm);
    }

    public Operator(String tag, Set<String> operationStringSynonyms, Algorithm<T> algorithm) {
        this(tag, operationStringSynonyms, 2, algorithm, new String[0], ";", true);
    }

    public Operator(String tag, List<String> operationStringSynonyms, Algorithm<T> algorithm) {
        this(tag, operationStringSynonyms, 2, algorithm, new String[0], ";", true);
    }

    public Operator(String tag, String[] operationStringSynonyms, Algorithm<T> algorithm) {
        this(tag, operationStringSynonyms, 2, algorithm, new String[0], ";", true);
    }

    public Operator(String tag, String operationStringSynonyms, Algorithm<T> algorithm) {
        this(tag, operationStringSynonyms, 2, algorithm, new String[0], ";", true);
    }

    /**
     * @return Operator tag
     */
    public String getTag() {
        return tag;
    }


    /**
     * Check string in array of string synonyms
     * @param stringSynonym Your string synonym
     * @return True if this operation have this stringSynonym, else False
     */
    public boolean haveStringSynonym(String stringSynonym) {
        return stringSynonyms.contains(stringSynonym);
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
    public int getCountArgs() {
        return countArgs;
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
    public T execute(T[] args) throws WrongCountOperationArgumentsException, UserMethodException {
        if (args.length == this.countArgs) {
            try {
                return algorithm.execute(args);
            } catch (Throwable e) {
                throw new UserMethodException(tag, countArgs, e);
            }
        } else throw new WrongCountOperationArgumentsException(this.countArgs, args.length);
    }

    public boolean haveSuffixForm() {
        return haveSuffixForm;
    }

    public boolean haveArgumentSeparator() {
        return argumentsSeparator != null && argumentsSeparator.length > 0 || defaultArgumentSeparator != null && defaultArgumentSeparator.length() > 0;
    }

    public boolean isOperator() {
        return true;
    }

    public boolean isFunction() {
        return false;
    }
}
