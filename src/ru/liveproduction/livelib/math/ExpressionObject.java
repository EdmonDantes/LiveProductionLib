/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

package ru.liveproduction.livelib.math;

import ru.liveproduction.livelib.utils.GenericUtils;
import ru.liveproduction.livelib.utils.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ExpressionObject<T> {

    public interface OperationMethod<K> {
        K execute(K[] args);
    }

    public static class UserMethodErrorException extends Exception {
        protected String operationTag;
        protected int countArgs;
        protected Exception exception;

        public UserMethodErrorException(String operationTag, int countArgs, Exception exception) {
            this.operationTag = operationTag;
            this.countArgs = countArgs;
            this.exception = exception;
        }

        public int getCountArgs() {
            return countArgs;
        }

        public String getOperationTag() {
            return operationTag;
        }

        public Exception getException() {
            return exception;
        }
    }

    public static class WrongArgumentsCountOperationException extends Exception {
        protected int need_count;
        protected int real_count;

        public WrongArgumentsCountOperationException(int need_count, int real_count) {
            this.need_count = need_count;
            this.real_count = real_count;
        }

        public int getRealArgumentsCount() {
            return real_count;
        }

        public int getNeedArgumentsCount() {
            return need_count;
        }
    }

    public static class Operation<K> {
        protected String operationTag;
        protected List<String> operationsString;
        protected int countArgs;
        protected boolean suffix;
        protected OperationMethod<K> method;

        private Operation() {
        }

        public Operation(String operationTag, String[] operationsString, int countArgs, OperationMethod<K> method) {
            this.operationTag = operationTag;
            this.countArgs = countArgs;
            this.method = method;
            this.operationsString = Arrays.asList(operationsString);
            suffix = false;
        }

        public Operation(String operationTag, List<String> operationsString, int countArgs, OperationMethod<K> method) {
            this.operationTag = operationTag;
            this.countArgs = countArgs;
            this.method = method;
            this.operationsString = operationsString;
            suffix = false;
        }

        public Operation(String operationTag, String[] operationsString, int countArgs, boolean suffix, OperationMethod<K> method) {
            this.operationTag = operationTag;
            this.countArgs = countArgs;
            this.suffix = suffix;
            this.method = method;
            this.operationsString = Arrays.asList(operationsString);
        }

        public Operation(String operationTag, List<String> operationsString, int countArgs, boolean suffix, OperationMethod<K> method) {
            this.operationTag = operationTag;
            this.countArgs = countArgs;
            this.suffix = suffix;
            this.method = method;
            this.operationsString = operationsString;
        }

        public int getCountArgs() {
            return countArgs;
        }

        public String getOperationTag() {
            return operationTag;
        }

        public void setOperationTag(String operationTag) {
            this.operationTag = operationTag;
        }

        public K execute(K[] args) throws WrongArgumentsCountOperationException, UserMethodErrorException {
            if (args.length == this.countArgs) {
                try {
                    return method.execute(args);
                }catch (Exception e) {
                    throw new UserMethodErrorException(operationTag, countArgs, e);
                }
            } else throw new WrongArgumentsCountOperationException(this.countArgs, args.length);
        }
    }

    public static class OperationManager<K> {

        public static class WrongOperationPriority extends Exception {
            protected int maxPriority;
            protected int realPriority;

            public WrongOperationPriority(int maxPriority, int realPriority) {
                this.maxPriority = maxPriority;
                this.realPriority = realPriority;
            }

            public int getMaxPriority() {
                return maxPriority;
            }

            public int getRealPriority() {
                return realPriority;
            }
        }

        protected Operation<K>[] operations;

        private OperationManager() {
        }

        public OperationManager(Operation<K>[] operations) {
            this.operations = operations;
        }

        public OperationManager(List<Operation<K>> operations) {
            if (operations.size() > 0)
                this.operations = operations.toArray((Operation<K>[]) Array.newInstance(operations.get(0).getClass(), operations.size()));
            else
                this.operations = new Operation[0];
        }

        protected Operation<K> getOperationFromPriority(int operationPriority) throws WrongOperationPriority {
            if (operationPriority < operations.length) return operations[operationPriority];
            else throw new WrongOperationPriority(operations.length - 1, operationPriority);
        }

        public int getCountArgs(int operationPriority) throws WrongOperationPriority {
            return getOperationFromPriority(operationPriority).countArgs;
        }

        public int getCountOperations() {
            return operations.length;
        }

        public K executeOperation(int operationPriority, K[] args) throws WrongOperationPriority, WrongArgumentsCountOperationException, UserMethodErrorException {
            return getOperationFromPriority(operationPriority).execute(args);
        }

        public boolean isThisOperation(int operationPriority, String operationString) throws WrongOperationPriority {
            return getOperationFromPriority(operationPriority).operationsString.contains(operationString);
        }

        public boolean isSuffix(int operationPriority) throws WrongOperationPriority {
            return getOperationFromPriority(operationPriority).suffix;
        }
    }

    protected OperationManager<T> operationsManager;
    protected Class<T> _class;
    protected List<String> allOperationsString;

    private static final String startVariableString = "ǄǼ";
    private static final String endVariableString = "Ǆ";
    private static final String nullValue = "0";

    protected static String getStartVariableString() {
        return startVariableString;
    }

    protected static String getEndVariableString() {
        return endVariableString;
    }

    protected static String getNullValueString() {
        return nullValue;
    }

    public ExpressionObject(Class<T> _class, Operation<T>[] operations) {
        this._class = _class;
        this.operationsManager = new OperationManager<T>(operations);
        this.allOperationsString = new LinkedList<>();
        for (var operation : operations) {
            allOperationsString.addAll(operation.operationsString);
        }
    }

    public ExpressionObject(Class<T> _class, List<Operation<T>> operations) {
        this._class = _class;
        this.operationsManager = new OperationManager<T>(operations);
        this.allOperationsString = new LinkedList<>();
        for (var operation : operations) {
            allOperationsString.addAll(operation.operationsString);
        }
    }

    public ExpressionObject(Class<T> _class, OperationManager<T> operationsManager) {
        this._class = _class;
        this.operationsManager = operationsManager;
        this.allOperationsString = new LinkedList<>();
        for (var operation : operationsManager.operations) {
            allOperationsString.addAll(operation.operationsString);
        }
    }

    protected T calculatePrimitiveExpression(String expression, List<T> executingValues) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, WrongArgumentsCountOperationException, OperationManager.WrongOperationPriority, UserMethodErrorException {
        if (expression.startsWith(getStartVariableString()) && expression.endsWith(getEndVariableString()))
            return executingValues.get(Integer.valueOf(expression.substring(2, expression.length() - 1)));

        List<Object> expressionParts = new ArrayList<>(StringUtils.splitWithSave(expression, this.allOperationsString));

        for (int i = 0; i < this.operationsManager.getCountOperations() && expressionParts.size() > 1; i++) {
            for (int j = 0; j < expressionParts.size(); j++) {
                if (expressionParts.get(j) instanceof String) {
                    if (this.operationsManager.isThisOperation(i, (String) expressionParts.get(j))) {
                        Object first = j > 0 && !this.allOperationsString.contains(expressionParts.get(j - 1)) ? expressionParts.get(j - 1) : null;
                        Object second = j + 1 < expressionParts.size() && !this.allOperationsString.contains(expressionParts.get(j + 1)) ? expressionParts.get(j + 1) : null;

                        if (this.operationsManager.getCountArgs(i) == 1 && (first == null || second == null)) {
                            T newValue;
                            if (this.operationsManager.isSuffix(i)) {
                                newValue = first instanceof String ? this.operationsManager.executeOperation(i, GenericUtils.createGenericArrayWithValues(_class, GenericUtils.createGenericFrom(_class, first))) : this.operationsManager.executeOperation(i, GenericUtils.createGenericArrayWithValues(_class, (T) first));
                                expressionParts.set(j, newValue);
                                expressionParts.remove(j - 1);
                            } else {
                                newValue = second instanceof String ? this.operationsManager.executeOperation(i, GenericUtils.createGenericArrayWithValues(_class, GenericUtils.createGenericFrom(_class, second))) : this.operationsManager.executeOperation(i, GenericUtils.createGenericArrayWithValues(_class, (T) second));
                                expressionParts.remove(j);
                                expressionParts.set(j, newValue);
                            }
                        } else if (this.operationsManager.getCountArgs(i) == 2 && first != null && second != null) {
                            T firstT = first instanceof String ? this._class.getConstructor(String.class).newInstance(first) : (T) first;
                            T secondT = second instanceof String ? this._class.getConstructor(String.class).newInstance(second) : (T) second;
                            expressionParts.remove(j);
                            expressionParts.set(j, this.operationsManager.executeOperation(i, GenericUtils.createGenericArrayWithValues(_class, firstT, secondT)));
                            expressionParts.remove(j - 1);
                            j--;
                        } else if (i == this.operationsManager.getCountOperations() - 1 && expressionParts.size() > 1) {
                            if (first != null) {
                                expressionParts.remove(j - 1);
                                if (second != null) expressionParts.remove(j - 1);
                                expressionParts.set(j - 1, getNullValueString());
                            } else if (second != null) {
                                expressionParts.remove(j + 1);
                                expressionParts.set(j, getNullValueString());
                            } else {
                                expressionParts.set(j, getNullValueString());
                            }
                            i = 0;
                        }
                    } else {
                        if (((String) expressionParts.get(j)).length() < 1) expressionParts.remove(j);
                        if (((String) expressionParts.get(j)).startsWith(getStartVariableString()) && ((String) expressionParts.get(j)).endsWith(getEndVariableString()))
                            expressionParts.set(j, executingValues.get(Integer.valueOf(((String) expressionParts.get(j)).substring(2, ((String) expressionParts.get(j)).length() - 1))));
                    }
                }
            }
        }
        return (T) expressionParts.get(0);
    }

    protected T calculateFullExpression(String expression, List<T> executingValue) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, WrongArgumentsCountOperationException, OperationManager.WrongOperationPriority, UserMethodErrorException {
        StringBuilder stringBuilder = new StringBuilder();
        int prev_end_index = 0;
        int start_index = expression.indexOf("(");
        int end_index = expression.indexOf(")");
        if (start_index == end_index) {
            return calculatePrimitiveExpression(expression, executingValue);
        } else {
            while (start_index != end_index) {
                int nextIndex = expression.indexOf("(", start_index + 1);
                while (nextIndex > -1 && nextIndex < end_index) {
                    end_index = expression.indexOf(")", end_index + 1);
                    nextIndex = expression.indexOf("(", nextIndex + 1);
                }
                T tmpValue = calculateFullExpression(expression.substring(start_index + 1, end_index), executingValue);
                stringBuilder.append(expression, prev_end_index, start_index).append(getStartVariableString()).append(executingValue.size()).append(getEndVariableString()).append(expression, end_index + 1, nextIndex > -1 ? nextIndex : expression.length());
                executingValue.add(tmpValue);
                start_index = nextIndex;
                prev_end_index = end_index + 1;
                end_index = start_index > -1 ? expression.indexOf(")", start_index + 1) : -1;
            }
            return calculatePrimitiveExpression(stringBuilder.toString(), executingValue);
        }
    }

    public T calc(String expression) throws WrongArgumentsCountOperationException, OperationManager.WrongOperationPriority, UserMethodErrorException {
        try {
            return calculateFullExpression(expression.replace(" ", ""), new ArrayList<>());
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (OperationManager.WrongOperationPriority | WrongArgumentsCountOperationException | UserMethodErrorException e) {
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

    public static Integer calcInteger(String expression) {
        try {
            return integerExpressionObject.calc(expression);
        } catch (WrongArgumentsCountOperationException e) {
            System.err.println("Please contact with developers. Special Error #1");
            e.printStackTrace();
        } catch (OperationManager.WrongOperationPriority wrongOperationPriority) {
            System.err.println("Please contact with developers. Special Error #2");
            wrongOperationPriority.printStackTrace();
        } catch (UserMethodErrorException e) {
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

    public static Double calcDouble(String expression) {
        try {
            return doubleExpressionObject.calc(expression.replace(",", "."));
        } catch (WrongArgumentsCountOperationException e) {
            System.err.println("Please contact with developers. Special Error #1");
            e.printStackTrace();
        } catch (OperationManager.WrongOperationPriority wrongOperationPriority) {
            System.err.println("Please contact with developers. Special Error #2");
            wrongOperationPriority.printStackTrace();
        } catch (UserMethodErrorException e) {
            System.err.println("Please contact with developers. Special Error #3");
            e.printStackTrace();
        }
        return null;
    }
}
