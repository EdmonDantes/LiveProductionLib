/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

package ru.liveproduction.livelib.math.expression2;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class PolishNotation {

    protected static PolishNotation INSTANSE = null;

    protected PolishNotation(){}

    public <T> LinkedList<Object> createPolishNotation(String string, OperationManager<T> operationManager, Object ignore) throws WrongExpressionException, WrongCountOperationArgumentsException {

        String expression = string.replace(" ", "");

        /**
         *  Stack for operations
         *  1 arg in trio - Operator or Function
         *  2 arg in trio - Operator`s args` count on this moment
         *  3 arg in trio - Equals string in expression
         */
        Stack<Trio<Operation<T>, Integer, String>> operationStack = new Stack<>();

        /**
         * Contanstains Trio and String
         */
        LinkedList<Object> result = new LinkedList<>();

        /**
         * String builder for T in string
         */
        StringBuilder tmpSavePartOfValues = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            char tmpCharacter = expression.charAt(i);

            if (tmpCharacter == '(') {
                if (tmpSavePartOfValues.length() > 0) {
                    throw new WrongExpressionException(string);
                }
                if (operationStack.empty() || !operationStack.peek().getFirst().isFunction())
                    operationStack.push(new Trio<>(null, 0, "("));
            } else if (tmpCharacter == ')') {
                boolean leftWord = false;
                if (tmpSavePartOfValues.length() > 0) {
                    result.addLast(tmpSavePartOfValues.toString());
                    tmpSavePartOfValues.setLength(0);
                    leftWord = true;
                }

                if (!operationStack.empty() && leftWord){
                    operationStack.peek().setSecond(operationStack.peek().getSecond() + 1);
                }

                while (!operationStack.empty()
                        && operationStack.peek().getThird() != "(" && !operationStack.peek().getFirst().isFunction())
                    result.addLast(operationStack.pop());


                if (!operationStack.empty()){
                    if (operationStack.peek().getThird().equals("("))
                        operationStack.pop();
                    else if (operationStack.peek().getFirst().isFunction())
                        if (operationStack.peek().getFirst().getCountOperationArgs() >= operationStack.peek().getSecond())
                            result.addLast(operationStack.pop());
                        else throw new WrongCountOperationArgumentsException(operationStack.peek().getFirst().getCountOperationArgs(), operationStack.peek().getSecond());

                }
            } else {
                /**
                 * 1 arg - Operator or Function
                 * 2 arg - Priority
                 * 3 arg - End of string`s synonym
                 */
                List<Trio<Operation<T>, Integer, Integer>> operationOrFunctionSearch = operationManager.getOperationPriorityAndLastIndexFromString(expression, i);
                if (operationOrFunctionSearch.size() > 0) {
                    boolean leftWord = false;
                    if (tmpSavePartOfValues.length() > 0) {
                        result.addLast(tmpSavePartOfValues.toString());
                        tmpSavePartOfValues.setLength(0);
                        leftWord = true;
                    }

                    Trio<Operation<T>, Integer, Integer> operation = null;
                    for (Trio<Operation<T>, Integer, Integer> applicant : operationOrFunctionSearch) {
                        boolean rigthWord = applicant.getThird() < expression.length() - 1;
                        boolean haveBrace = rigthWord && expression.charAt(applicant.getThird() + 1) == '(';

                        if (leftWord) {
                            if (applicant.getFirst().isFunction() || !applicant.getFirst().haveSuffixForm()) continue;
                        } else {
                            if (haveBrace) {
                                if (!applicant.getFirst().isFunction()) continue;
                            } else {
                                if (applicant.getFirst().isFunction()) continue;
                            }

                            if (applicant.getFirst().haveSuffixForm()) continue;
                        }
                        operation = applicant;
                        break;
                    }

                    if (operation == null) {
                        operation = operationOrFunctionSearch.get(0);
                    }

                    while (!operationStack.empty() && !operationStack.peek().getThird().equals("(")
                            && operationStack.peek().getFirst().getCountOperationArgs() <= operationStack.peek().getSecond()
                            && operationManager.getOperationFromTag(operationStack.peek().getFirst().getOperationTag()).getSecond() <= operation.getSecond())
                        result.addLast(operationStack.pop());

                    operationStack.push(new Trio<>(operation.getFirst(), operation.getFirst().isFunction() ? 0 : ((leftWord ? 1 : 0) + (i < expression.length() - 1 ? 1 : 0)), expression.substring(i, operation.getThird())));

                    i = operation.getThird() - 1;
                } else {
                    int separatorSearch = operationManager.isArgumentsSeparator(expression, i);

                    if (separatorSearch > -1) {
                        if (tmpSavePartOfValues.length() > 0) {
                            result.addLast(tmpSavePartOfValues.toString());
                            tmpSavePartOfValues.setLength(0);
                        }

                        while (!operationStack.empty() && !operationStack.peek().getThird().equals("(")
                                && operationStack.peek().getFirst().getCountOperationArgs() <= operationStack.peek().getSecond()) {
                            result.addLast(operationStack.pop());
                        }

                        if (!operationStack.empty() && operationStack.peek().getFirst().getCountOperationArgs() > operationStack.peek().getSecond()
                                && (operationStack.peek().getFirst().isFunction() && operationStack.peek().getFirst().getArgumentSeparator(operationStack.peek().getSecond()).equals(expression.substring(i, separatorSearch)) ||
                                !operationStack.peek().getFirst().isFunction() && operationStack.peek().getFirst().getArgumentSeparator(operationStack.peek().getSecond() - 1).equals(expression.substring(i, separatorSearch))))
                            operationStack.peek().setSecond(operationStack.peek().getSecond() + 1);
                        else
                            throw new WrongExpressionException(string);
                        i = separatorSearch - 1;
                    } else {
                        tmpSavePartOfValues.append(tmpCharacter);
                    }
                }
            }
        }

        if (tmpSavePartOfValues.length() > 0) result.addLast(tmpSavePartOfValues.toString());

        while (!operationStack.empty()) {
            if (!operationStack.peek().getThird().equals("("))
                if (operationStack.peek().getFirst().getCountOperationArgs() >= operationStack.peek().getSecond())
                    result.addLast(operationStack.pop());
                else throw new WrongCountOperationArgumentsException(operationStack.peek().getFirst().getCountOperationArgs(), operationStack.peek().getSecond());
        }

        return result;
    }

//    public <K> LinkedList<Object> createPolishNotation(String string, OperationManager<K> operationManager, Object ignore) throws WrongExpressionException {
//        String expression = string.replace(" ", "");
//
//        Stack<Trio<Operator<K>, Integer, String>> operationStack = new Stack<>(); // 1 - Operator, 2 - count args, 3 - string`s synonym in expression
//
//        LinkedList<Object> result = new LinkedList<>();
//
//        StringBuilder tmpSavePartOfValues = new StringBuilder();
//        for (int i = 0; i < expression.length(); i++) {
//            char tmpCharacter = expression.charAt(i);
//
//            if (tmpCharacter == '(') {
//                if (tmpSavePartOfValues.length() > 0) {
//                    throw new WrongExpressionException(string);
//                }
//                operationStack.push(new Trio<>(null, 0, "("));
//            } else if (tmpCharacter == ')') {
//                if (tmpSavePartOfValues.length() > 0) {
//                    result.addLast(tmpSavePartOfValues.toString());
//                    tmpSavePartOfValues.setLength(0);
//                }
//
//                while (!operationStack.empty() && operationStack.peek().getThird() != "(")
//                    result.addLast(operationStack.pop());
//
//                if (!operationStack.empty()) operationStack.pop();
//                else throw new WrongExpressionException(string);
//
//                if (!operationStack.empty() && operationStack.peek().getFirst().isFunction()) {
//                    result.addLast(operationStack.pop());
//                }
//            } else {
//                List<Trio<Operator<K>, Integer, Integer>> operationSearch = operationManager.getOperationPriorityAndLastIndexFromString(expression, i);
//                if (operationSearch.size() > 0) {
//                    boolean leftWord = false;
//                    if (tmpSavePartOfValues.length() > 0) {
//                        result.addLast(tmpSavePartOfValues.toString());
//                        tmpSavePartOfValues.setLength(0);
//                        leftWord = true;
//                    }
//
//                    Trio<Operator<K>, Integer, Integer> operation = null;
//                    for (Trio<Operator<K>, Integer, Integer> applicant : operationSearch) {
//                        if (leftWord) {
//                            if (applicant.getFirst().getCountOperationArgs() == 1 && applicant.getFirst().haveSuffixForm()) {
//                                operation = applicant;
//                                break;
//                            }
//                        } else {
//                            if (!applicant.getFirst().haveSuffixForm() && applicant.getFirst().getCountOperationArgs() == 1) {
//                                operation = applicant;
//                                break;
//                            }
//                        }
//                    }
//                    if (operation == null) {
//                        for (Trio<Operator<K>, Integer, Integer> applicant : operationSearch) {
//                            if (applicant.getFirst().getCountOperationArgs() > 1) {
//                                operation = applicant;
//                                break;
//                            }
//                        }
//                        if (operation == null) throw new WrongExpressionException(string);
//                    }
//
//                    while (!operationStack.empty() && !operationStack.peek().getThird().equals("(")
//                            && operationStack.peek().getFirst().getCountOperationArgs() <= operationStack.peek().getSecond()
//                            && operationManager.getOperationFromTag(operationStack.peek().getFirst().getOperationTag()).getSecond() <= operation.getSecond())
//                        result.addLast(operationStack.pop());
//
//                    if (leftWord) {
//                        operationStack.push(new Trio<>(operation.getFirst(), i < expression.length() - 1 ? 2 : 1, expression.substring(i, operation.getThird())));
//                    } else {
//                        operationStack.push(new Trio<>(operation.getFirst(), i < expression.length() - 1 ? 1 : 0, expression.substring(i, operation.getThird())));
//                    }
//
//                    i = operation.getThird() - 1;
//                } else {
//                    int separatorSearch = operationManager.isArgumentsSeparator(expression, i);
//
//                    if (separatorSearch > -1) {
//                        if (tmpSavePartOfValues.length() > 0) {
//                            result.addLast(tmpSavePartOfValues.toString());
//                            tmpSavePartOfValues.setLength(0);
//                        }
//
//                        while (!operationStack.empty() && operationStack.peek().getFirst().getCountOperationArgs() <= operationStack.peek().getSecond()
//                                && !operationStack.peek().getThird().equals("(")) {
//                            result.addLast(operationStack.pop());
//                        }
//
//                        if (!operationStack.empty() && operationStack.peek().getFirst().getCountOperationArgs() > operationStack.peek().getSecond())
//                            operationStack.peek().setSecond(operationStack.peek().getSecond() + 1);
//
//                        i = separatorSearch - 1;
//                    } else {
//                        tmpSavePartOfValues.append(tmpCharacter);
//                    }
//                }
//            }
//        }
//
//        if (tmpSavePartOfValues.length() > 0) result.addLast(tmpSavePartOfValues.toString());
//
//        while (!operationStack.empty()) {
//            if (operationStack.peek().getThird().equals("(")) throw new WrongExpressionException(string);
//            result.addLast(operationStack.pop());
//        }
//
//       return result;
//    }

    public <K> String createPolishNotation(String string, OperationManager<K> operationManager, boolean useTags, String separator) throws WrongExpressionException, WrongCountOperationArgumentsException {
        LinkedList<Object> result = createPolishNotation(string, operationManager, null);

        StringBuilder resultBuilder = new StringBuilder();
        while (result.size() > 0) {
            if (result.peekFirst() instanceof String) resultBuilder.append(result.pollFirst()).append(separator);
            else {
                Trio<Operation<K>, Integer, String> local = (Trio) result.pollFirst();
                if (useTags) resultBuilder.append(local.getFirst().getOperationTag());
                else resultBuilder.append(local.getThird());
                resultBuilder.append(separator);
            }
        }
        return resultBuilder.substring(0, resultBuilder.length() - 1).toString();
    }

    public <K> String createPolishNotation(String string, OperationManager<K> operationManager, boolean useTags) throws WrongExpressionException, WrongCountOperationArgumentsException {
        return createPolishNotation(string, operationManager, useTags, ",");
    }

    public <K> String createPolishNotation(String string, OperationManager<K> operationManager) throws WrongExpressionException, WrongCountOperationArgumentsException {
        return createPolishNotation(string, operationManager, false);
    }

    public static PolishNotation getInstance() {
        if (INSTANSE == null) INSTANSE = new PolishNotation();
        return INSTANSE;
    }
}
