/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

package ru.liveproduction.livelib.math;

import ru.liveproduction.livelib.utils.GenericUtils;
import ru.liveproduction.livelib.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ExpressionObject<T> {

    /**
     * This interface set action on execute operation
     * @param <K> Your class
     */
    public interface OperationMethod<K> {

        /**
         * Action when execute operation
         * @param args Operation arguments
         * @return New object with class K
         */
        K execute(K[] args);
    }

    /**
     * Throw this exception when in your OperationMethod throw any Throwable errors
     */
    public static class UserMethodException extends Exception {
        protected String operationTag;
        protected int countOperationArgs;
        protected Throwable userError;

        public UserMethodException(String operationTag, int countOperationArgs, Throwable userError) {
            this.operationTag = operationTag;
            this.countOperationArgs = countOperationArgs;
            this.userError = userError;
        }

        /**
         * @return How many operation`s args you send to method
         */
        public int getCountOperationArgs() {
            return countOperationArgs;
        }

        /**
         * @return What is the operation executed
         */
        public String getOperationTag() {
            return operationTag;
        }

        /**
         * @return Error throwed in user method
         */
        public Throwable getUserError() {
            return userError;
        }
    }

    /**
     * Throw this exception when you send wrong argument to your OperationMethod
     */
    public static class WrongCountOperationArgumentsException extends Exception {
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

    /**
     *
     * @param <K> Your class
     */
    public static class Operation<K> {
        protected String operationTag;
        protected List<String> operationStringSynonyms;
        protected int countOperationArgs;
        protected OperationMethod<K> operationMethod;

        protected boolean suffixForm;

        /**
         * Don`t will override in children
         */
        private Operation() {}

        public Operation(String operationTag, String[] operationStringSynonyms, int countOperationArgs, OperationMethod<K> operationMethod) {
            this.operationTag = operationTag;
            this.countOperationArgs = countOperationArgs;
            this.operationMethod = operationMethod;
            this.operationStringSynonyms = Arrays.asList(operationStringSynonyms);
            suffixForm = false;
        }

        public Operation(String operationTag, List<String> operationStringSynonyms, int countOperationArgs, OperationMethod<K> operationMethod) {
            this.operationTag = operationTag;
            this.countOperationArgs = countOperationArgs;
            this.operationMethod = operationMethod;
            this.operationStringSynonyms = operationStringSynonyms;
            suffixForm = false;
        }

        public Operation(String operationTag, String[] operationStringSynonyms, int countOperationArgs, boolean suffixForm, OperationMethod<K> operationMethod) {
            this.operationTag = operationTag;
            this.countOperationArgs = countOperationArgs;
            this.suffixForm = suffixForm;
            this.operationMethod = operationMethod;
            this.operationStringSynonyms = Arrays.asList(operationStringSynonyms);
        }

        public Operation(String operationTag, List<String> operationStringSynonyms, int countOperationArgs, boolean suffixForm, OperationMethod<K> operationMethod) {
            this.operationTag = operationTag;
            this.countOperationArgs = countOperationArgs;
            this.suffixForm = suffixForm;
            this.operationMethod = operationMethod;
            this.operationStringSynonyms = operationStringSynonyms;
        }

        /**
         * @return How many operation arguments need this operation
         */
        public int getCountOperationArgs() {
            return countOperationArgs;
        }

        /**
         * @return Name of this operation
         */
        public String getOperationTag() {
            return operationTag;
        }

        /**
         * Set name for this operation
         * @param operationTag Operation`s name
         */
        public void setOperationTag(String operationTag) {
            this.operationTag = operationTag;
        }

        /**
         * Only for operations, what need only 1 operation`s arguments!!!
         * @return String synonyms must place after argument
         */
        public boolean haveSuffixForm() {
            return suffixForm;
        }

        /**
         * @return Operation`s string`s synonyms
         */
        public List<String> getOperationStringSynonyms() {
            return operationStringSynonyms;
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
                    return operationMethod.execute(args);
                } catch (Throwable e) {
                    throw new UserMethodException(operationTag, countOperationArgs, e);
                }
            } else throw new WrongCountOperationArgumentsException(this.countOperationArgs, args.length);
        }
    }

    /**
     * Throw this expression if your send wrong operation priority
     */
    public static class WrongOperationPriority extends Exception {
        protected int maxOperationPriority;
        protected int realOperationPriority;

        public WrongOperationPriority(int maxOperationPriority, int realOperationPriority) {
            this.maxOperationPriority = maxOperationPriority;
            this.realOperationPriority = realOperationPriority;
        }

        /**
         * @return Max operation priority
         */
        public int getMaxOperationPriority() {
            return maxOperationPriority;
        }

        /**
         * @return What did you send
         */
        public int getRealOperationPriority() {
            return realOperationPriority;
        }
    }

    public static class OperationManager<K> {

        protected Operation<K>[] operationsArray;

        /**
         * Don`t will override in children
         */
        private OperationManager() {}

        public OperationManager(Operation<K>[] operationsArray) {
            this.operationsArray = operationsArray;
        }

        @SuppressWarnings("unchecked")
        public OperationManager(List<Operation<K>> operationsArray) {
            if (operationsArray.size() > 0)
                this.operationsArray = operationsArray.toArray(GenericUtils.createGenericArray(operationsArray.get(0).getClass(), 0));
            else
                this.operationsArray = GenericUtils.createGenericArray(Operation.class, 0);


        }

        protected Operation<K> getOperationFromPriority(int operationPriority) throws WrongOperationPriority {
            if (operationPriority < operationsArray.length) return operationsArray[operationPriority];
            else throw new WrongOperationPriority(operationsArray.length - 1, operationPriority);
        }

        /**
         * @param operationPriority
         * @return How many operation`s argument is needed this operation
         * @throws WrongOperationPriority
         */
        public int getCountOperationArgs(int operationPriority) throws WrongOperationPriority {
            return getOperationFromPriority(operationPriority).getCountOperationArgs();
        }

        /**
         * @return How many operations in this OperationManager
         */
        public int getCountOperations() {
            return operationsArray.length;
        }

        /**
         * Execute operation with special operation priority
         * @param operationPriority
         * @param args Operation`s arguments
         * @return New value
         * @throws WrongOperationPriority
         * @throws WrongCountOperationArgumentsException
         * @throws UserMethodException
         */
        public K executeOperation(int operationPriority, K[] args) throws WrongOperationPriority, WrongCountOperationArgumentsException, UserMethodException {
            return getOperationFromPriority(operationPriority).execute(args);
        }

        /**
         * @param operationPriority
         * @param operationStringSynonym
         * @return True, if this operation have this string synonyms
         * @throws WrongOperationPriority
         */
        public boolean checkOperationStringSynonym(int operationPriority, String operationStringSynonym) throws WrongOperationPriority {
            return getOperationFromPriority(operationPriority).getOperationStringSynonyms().contains(operationStringSynonym);
        }

        /**
         * @param operationPriority
         * @return True, if operation have suffix form
         * @throws WrongOperationPriority
         */
        public boolean operationHaveSuffixForm(int operationPriority) throws WrongOperationPriority {
            return getOperationFromPriority(operationPriority).haveSuffixForm();
        }
    }

    protected OperationManager<T> operationsManager;
    protected Class<T> _class;
    protected List<String> allOperationsStringSynonyms;

    private static final String startVariableStringSynonym = "ǄǼ";
    private static final String endVariableStringSynonym = "Ǆ";
    private static final String nullValueStringSynonym = "0";

    protected static String getStartVariableStringSynonym() {
        return startVariableStringSynonym;
    }

    protected static String getEndVariableStringSynonym() {
        return endVariableStringSynonym;
    }

    protected static String getNullValueStringSynonym() {
        return nullValueStringSynonym;
    }

    public ExpressionObject(Class<T> _class, Operation<T>[] operations) {
        this._class = _class;
        this.operationsManager = new OperationManager<T>(operations);
        this.allOperationsStringSynonyms = new LinkedList<>();
        for (var operation : operations) {
            allOperationsStringSynonyms.addAll(operation.operationStringSynonyms);
        }
    }

    public ExpressionObject(Class<T> _class, List<Operation<T>> operations) {
        this._class = _class;
        this.operationsManager = new OperationManager<T>(operations);
        this.allOperationsStringSynonyms = new LinkedList<>();
        for (var operation : operations) {
            allOperationsStringSynonyms.addAll(operation.operationStringSynonyms);
        }
    }

    public ExpressionObject(Class<T> _class, OperationManager<T> operationsManager) {
        this._class = _class;
        this.operationsManager = operationsManager;
        this.allOperationsStringSynonyms = new LinkedList<>();
        for (var operation : operationsManager.operationsArray) {
            allOperationsStringSynonyms.addAll(operation.operationStringSynonyms);
        }
    }

    protected T calculatePrimitiveExpression(String expression, List<T> executingValues) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, WrongCountOperationArgumentsException, WrongOperationPriority, UserMethodException {
        List<Object> expressionParts = new ArrayList<>(StringUtils.splitWithSave(expression, this.allOperationsStringSynonyms));

        for (int indexOperationPriority = 0; indexOperationPriority < this.operationsManager.getCountOperations() && expressionParts.size() > 1; indexOperationPriority++) {
            for (int indexExpressionPart = 0; indexExpressionPart < expressionParts.size(); indexExpressionPart++) {
                if (expressionParts.get(indexExpressionPart) instanceof String) {
                    if (this.operationsManager.checkOperationStringSynonym(indexOperationPriority, (String) expressionParts.get(indexExpressionPart))) {
                        Object firstArgObject = indexExpressionPart > 0 && !this.allOperationsStringSynonyms.contains(expressionParts.get(indexExpressionPart - 1)) ? expressionParts.get(indexExpressionPart - 1) : null;
                        Object secondArgsObject = indexExpressionPart + 1 < expressionParts.size() && !this.allOperationsStringSynonyms.contains(expressionParts.get(indexExpressionPart + 1)) ? expressionParts.get(indexExpressionPart + 1) : null;

                        if (this.operationsManager.getCountOperationArgs(indexOperationPriority) == 1 && (firstArgObject == null || secondArgsObject == null)) {
                            T newValue;
                            if (this.operationsManager.operationHaveSuffixForm(indexOperationPriority)) {
                                newValue = firstArgObject instanceof String ? this.operationsManager.executeOperation(indexOperationPriority, GenericUtils.createGenericArrayWithValues(_class, GenericUtils.createGenericFrom(_class, firstArgObject))) : this.operationsManager.executeOperation(indexOperationPriority, GenericUtils.createGenericArrayWithValues(_class, (T) firstArgObject));
                                expressionParts.set(indexExpressionPart, newValue);
                                expressionParts.remove(indexExpressionPart - 1);
                            } else {
                                newValue = secondArgsObject instanceof String ? this.operationsManager.executeOperation(indexOperationPriority, GenericUtils.createGenericArrayWithValues(_class, GenericUtils.createGenericFrom(_class, secondArgsObject))) : this.operationsManager.executeOperation(indexOperationPriority, GenericUtils.createGenericArrayWithValues(_class, (T) secondArgsObject));
                                expressionParts.remove(indexExpressionPart);
                                expressionParts.set(indexExpressionPart, newValue);
                            }
                        } else if (this.operationsManager.getCountOperationArgs(indexOperationPriority) == 2 && firstArgObject != null && secondArgsObject != null) {
                            T firstT = firstArgObject instanceof String ? GenericUtils.createGenericFrom(_class, firstArgObject) : (T) firstArgObject;
                            T secondT = secondArgsObject instanceof String ? GenericUtils.createGenericFrom(_class, secondArgsObject) : (T) secondArgsObject;
                            expressionParts.remove(indexExpressionPart);
                            expressionParts.set(indexExpressionPart, this.operationsManager.executeOperation(indexOperationPriority, GenericUtils.createGenericArrayWithValues(_class, firstT, secondT)));
                            expressionParts.remove(indexExpressionPart - 1);
                            indexExpressionPart--;
                        } else if (indexOperationPriority == this.operationsManager.getCountOperations() - 1 && expressionParts.size() > 1) {
                            if (firstArgObject != null) {
                                expressionParts.remove(indexExpressionPart - 1);
                                if (secondArgsObject != null) expressionParts.remove(indexExpressionPart - 1);
                                expressionParts.set(indexExpressionPart - 1, getNullValueStringSynonym());
                            } else if (secondArgsObject != null) {
                                expressionParts.remove(indexExpressionPart + 1);
                                expressionParts.set(indexExpressionPart, getNullValueStringSynonym());
                            } else {
                                expressionParts.set(indexExpressionPart, getNullValueStringSynonym());
                            }
                            indexOperationPriority = 0;
                        }
                    } else {
                        if (((String) expressionParts.get(indexExpressionPart)).length() < 1)
                            expressionParts.remove(indexExpressionPart);
                        if (((String) expressionParts.get(indexExpressionPart)).startsWith(getStartVariableStringSynonym()) && ((String) expressionParts.get(indexExpressionPart)).endsWith(getEndVariableStringSynonym()))
                            expressionParts.set(indexExpressionPart, executingValues.get(Integer.valueOf(((String) expressionParts.get(indexExpressionPart)).substring(2, ((String) expressionParts.get(indexExpressionPart)).length() - 1))));
                    }
                }
            }
        }
        return (T) expressionParts.get(0);
    }

    protected T calculateFullExpression(String expression, List<T> executingValue) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, WrongCountOperationArgumentsException, WrongOperationPriority, UserMethodException {
        StringBuilder highPriorityExpression = new StringBuilder();
        int prevEndIndex = 0;
        int startIndex = expression.indexOf("(");
        int endIndex = expression.indexOf(")");
        if (startIndex == endIndex) {
            return calculatePrimitiveExpression(expression, executingValue);
        } else {
            while (startIndex != endIndex) {
                int nextIndex = expression.indexOf("(", startIndex + 1);
                while (nextIndex > -1 && nextIndex < endIndex) {
                    endIndex = expression.indexOf(")", endIndex + 1);
                    nextIndex = expression.indexOf("(", nextIndex + 1);
                }
                T valueOfHighPriorityExpression = calculateFullExpression(expression.substring(startIndex + 1, endIndex), executingValue);
                highPriorityExpression.append(expression, prevEndIndex, startIndex).append(getStartVariableStringSynonym()).append(executingValue.size()).append(getEndVariableStringSynonym()).append(expression, endIndex + 1, nextIndex > -1 ? nextIndex : expression.length());
                executingValue.add(valueOfHighPriorityExpression);
                startIndex = nextIndex;
                prevEndIndex = endIndex + 1;
                endIndex = startIndex > -1 ? expression.indexOf(")", startIndex + 1) : -1;
            }
            return calculatePrimitiveExpression(highPriorityExpression.toString(), executingValue);
        }
    }

    /**
     * Calculate string expression
     * @param expression
     * @return New value with your class
     * @throws WrongCountOperationArgumentsException
     * @throws WrongOperationPriority
     * @throws UserMethodException
     */

    public T calc(String expression) throws WrongCountOperationArgumentsException, WrongOperationPriority, UserMethodException {
        try {
            return calculateFullExpression(expression.replace(" ", ""), new ArrayList<>());
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (WrongOperationPriority | WrongCountOperationArgumentsException | UserMethodException e) {
            throw e;
        }
        return null;
    }


    protected static final List<ExpressionObject.Operation<Integer>> integerOperations = new ArrayList<>();

    static {
        integerOperations.add(new Operation<Integer>("NOT", new String[]{"-", "−", "-"}, 1, new ExpressionObject.OperationMethod<Integer>() {
            @Override
            public Integer execute(Integer[] args) {
                return -args[0];
            }
        }));
        integerOperations.add(new Operation<Integer>("LOG", new String[]{"log", "lg", "l"}, 2, new OperationMethod<Integer>() {
            @Override
            public Integer execute(Integer[] args) {
                return (int) (Math.log(args[0]) / Math.log(args[1]));
            }
        }));
        integerOperations.add(new Operation<Integer>("SQRT", new String[]{"#"}, 2, new OperationMethod<Integer>() {
            @Override
            public Integer execute(Integer[] args) {
                return (int) Math.pow(args[0], 1.0 / args[1]);
            }
        }));
        integerOperations.add(new Operation<Integer>("POW", new String[]{"^"}, 2, new OperationMethod<Integer>() {
            @Override
            public Integer execute(Integer[] args) {
                return (int) Math.pow(args[0], args[1]);
            }
        }));
        integerOperations.add(new Operation<Integer>("MULT", new String[]{"*", "×"}, 2, new ExpressionObject.OperationMethod<Integer>() {
            @Override
            public Integer execute(Integer[] args) {
                return args[0] * args[1];
            }
        }));
        integerOperations.add(new Operation<Integer>("DIV", new String[]{"÷", "/", ":"}, 2, new ExpressionObject.OperationMethod<Integer>() {
            @Override
            public Integer execute(Integer[] args) {
                return args[0] / args[1];
            }
        }));
        integerOperations.add(new Operation<Integer>("SUB", new String[]{"-", "−", "-"}, 2, new ExpressionObject.OperationMethod<Integer>() {
            @Override
            public Integer execute(Integer[] args) {
                return args[0] - args[1];
            }
        }));
        integerOperations.add(new Operation<Integer>("SUM", new String[]{"+"}, 2, new ExpressionObject.OperationMethod<Integer>() {
            @Override
            public Integer execute(Integer[] args) {
                return args[0] + args[1];
            }
        }));

    }

    protected static final ExpressionObject<Integer> integerExpressionObject = new ExpressionObject<Integer>(Integer.class, integerOperations);

    /**
     * Calculate string expression with math expression
     * @param expression
     * @return Integer value or null, if it create expression
     */
    public static Integer calcInteger(String expression) {
        try {
            return integerExpressionObject.calc(expression);
        } catch (WrongCountOperationArgumentsException e) {
            System.err.println("Please contact with developers. Special Error #1");
            e.printStackTrace();
        } catch (WrongOperationPriority wrongOperationPriority) {
            System.err.println("Please contact with developers. Special Error #2");
            wrongOperationPriority.printStackTrace();
        } catch (UserMethodException e) {
            System.err.println("Please contact with developers. Special Error #3");
            e.printStackTrace();
        }
        return null;
    }


    protected static final List<ExpressionObject.Operation<Double>> doubleOperations = new ArrayList<>();

    static {
        doubleOperations.add(new Operation<Double>("NOT", new String[]{"-", "−", "-"}, 1, new ExpressionObject.OperationMethod<Double>() {
            @Override
            public Double execute(Double[] args) {
                return -args[0];
            }
        }));
        doubleOperations.add(new Operation<Double>("LOG", new String[]{"log", "lg", "l"}, 2, new OperationMethod<Double>() {
            @Override
            public Double execute(Double[] args) {
                return Math.log(args[0]) / Math.log(args[1]);
            }
        }));
        doubleOperations.add(new Operation<Double>("SQRT", new String[]{"#"}, 2, new OperationMethod<Double>() {
            @Override
            public Double execute(Double[] args) {
                return Math.pow(args[0], 1.0 / args[1]);
            }
        }));
        doubleOperations.add(new Operation<Double>("POW", new String[]{"^"}, 2, new OperationMethod<Double>() {
            @Override
            public Double execute(Double[] args) {
                return Math.pow(args[0], args[1]);
            }
        }));
        doubleOperations.add(new Operation<Double>("MULT", new String[]{"*", "×"}, 2, new ExpressionObject.OperationMethod<Double>() {
            @Override
            public Double execute(Double[] args) {
                return args[0] * args[1];
            }
        }));
        doubleOperations.add(new Operation<Double>("DIV", new String[]{"÷", "/", ":"}, 2, new ExpressionObject.OperationMethod<Double>() {
            @Override
            public Double execute(Double[] args) {
                return args[0] / args[1];
            }
        }));
        doubleOperations.add(new Operation<Double>("SUB", new String[]{"-", "−", "-"}, 2, new ExpressionObject.OperationMethod<Double>() {
            @Override
            public Double execute(Double[] args) {
                return args[0] - args[1];
            }
        }));
        doubleOperations.add(new Operation<Double>("SUM", new String[]{"+"}, 2, new ExpressionObject.OperationMethod<Double>() {
            @Override
            public Double execute(Double[] args) {
                return args[0] + args[1];
            }
        }));

    }

    protected static final ExpressionObject<Double> doubleExpressionObject = new ExpressionObject<Double>(Double.class, doubleOperations);

    /**
     * Calculate string expression with math expression
     * @param expression
     * @return Double value or null, if it create expression
     */
    public static Double calcDouble(String expression) {
        try {
            return doubleExpressionObject.calc(expression.replace(",", "."));
        } catch (WrongCountOperationArgumentsException e) {
            System.err.println("Please contact with developers. Special Error #1");
            e.printStackTrace();
        } catch (WrongOperationPriority wrongOperationPriority) {
            System.err.println("Please contact with developers. Special Error #2");
            wrongOperationPriority.printStackTrace();
        } catch (UserMethodException e) {
            System.err.println("Please contact with developers. Special Error #3");
            e.printStackTrace();
        }
        return null;
    }
}
