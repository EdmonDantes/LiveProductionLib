/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

package ru.liveproduction.livelib.math;

import ru.liveproduction.livelib.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class ExpressionObject<T> {

    public static final List<String> operationPriority = Arrays.asList("NOT", "LOG", "SQRT", "POW", "MULT", "DIV", "SUB", "SUM","LOGIC_NOT", "LOGIC_AND", "LOGIC_OR");
    public static final Map<String, List<String>> operationsString = new HashMap<>();
    public static final Map<String, Operation<Integer>> integersOperations = new HashMap<>();
    public static final Map<String, Operation<Double>> doubleOperations = new HashMap<>();
    public static final Map<String, Operation<BigInteger>> bigIntegersOperations = new HashMap<>();
    public static final Map<String, Operation<BigDecimal>> bigDecimalsOperations = new HashMap<>();

    static {
        operationsString.put("SUM", Collections.singletonList("+"));
        operationsString.put("SUB", Arrays.asList("-", "-"));
        operationsString.put("MULT", Collections.singletonList("*"));
        operationsString.put("DIV", Arrays.asList("/", ":"));
        operationsString.put("LOG", Arrays.asList("log", "lg", "l"));
        operationsString.put("POW", Collections.singletonList("^"));
        operationsString.put("SQRT", Collections.singletonList("@"));
        operationsString.put("LOGIC_NOT", Arrays.asList("~", "!"));
        operationsString.put("LOGIC_AND", Arrays.asList("&&", "&"));
        operationsString.put("LOGIC_OR", Arrays.asList("||", "|"));
        operationsString.put("NOT", Collections.singletonList("`"));

        integersOperations.put("SUM", (a, b) -> a + b);
        integersOperations.put("SUB", (a, b) -> a - b);
        integersOperations.put("MULT", (a, b) -> a * b);
        integersOperations.put("DIV", (a, b) -> a / b);
        integersOperations.put("LOG", (a, b) -> (int) Math.round(Math.log(a) / Math.log(b)));
        integersOperations.put("POW", (a, b) -> (int) Math.round(Math.pow(a, b)));
        integersOperations.put("SQRT", (a, b) -> (int) Math.round(Math.pow(a, 1.0 / b)));
        integersOperations.put("LOGIC_AND", (a, b) -> a & b);
        integersOperations.put("LOGIC_OR", (a, b) -> a | b);
        integersOperations.put("LOGIC_NOT", (a, b) -> ~b);
        integersOperations.put("NOT", (a, b) -> -b);

        bigIntegersOperations.put("SUM", BigInteger::add);
        bigIntegersOperations.put("SUB", BigInteger::subtract);
        bigIntegersOperations.put("MULT", BigInteger::multiply);
        bigIntegersOperations.put("DIV", BigInteger::divide);
        bigIntegersOperations.put("LOG", (a, b) -> {throw new Exception("This operation not support for this class");});
        bigIntegersOperations.put("POW", (a, b) -> a.pow(b.intValue()));
        bigIntegersOperations.put("SQRT", (a, b) -> {throw new Exception("This operation not support for this class");});
        bigIntegersOperations.put("LOGIC_AND", BigInteger::and);
        bigIntegersOperations.put("LOGIC_OR", BigInteger::or);
        bigIntegersOperations.put("LOGIC_NOT", (a, b) -> b.not());
        bigIntegersOperations.put("NOT", (a, b) -> b.negate());

        bigDecimalsOperations.put("SUM", BigDecimal::add);
        bigDecimalsOperations.put("SUB", BigDecimal::subtract);
        bigDecimalsOperations.put("MULT", BigDecimal::multiply);
        bigDecimalsOperations.put("DIV", BigDecimal::divide);
        bigDecimalsOperations.put("LOG", (a, b) -> {throw new Exception("This operation not support for this class");});
        bigDecimalsOperations.put("POW", (a, b) -> a.pow(b.intValue()));
        bigDecimalsOperations.put("SQRT", (a, b) -> {throw new Exception("This operation not support for this class");});
        bigDecimalsOperations.put("LOGIC_AND", (a, b) -> new BigDecimal(a.toBigInteger().and(b.toBigInteger())));
        bigDecimalsOperations.put("LOGIC_OR", (a, b) -> new BigDecimal(a.toBigInteger().or(b.toBigInteger())));
        bigDecimalsOperations.put("LOGIC_NOT", (a, b) -> new BigDecimal(b.toBigInteger().not()));
        bigDecimalsOperations.put("NOT", (a, b) -> b.negate());

        doubleOperations.put("SUM", (a, b) -> a + b);
        doubleOperations.put("SUB", (a, b) -> a - b);
        doubleOperations.put("MULT", (a, b) -> a * b);
        doubleOperations.put("DIV", (a, b) -> a / b);
        doubleOperations.put("LOG", (a, b) -> Math.log(a) / Math.log(b));
        doubleOperations.put("POW", Math::pow);
        doubleOperations.put("SQRT", (a, b) -> Math.pow(a, 1.0 / b));
        doubleOperations.put("LOGIC_AND", (a, b) -> (double) (Math.round(a) & Math.round(b)));
        doubleOperations.put("LOGIC_OR", (a, b) -> (double) (Math.round(a) | Math.round(b)));
        doubleOperations.put("LOGIC_NOT", (a, b) -> (double) (~Math.round(b)));
        doubleOperations.put("NOT", (a, b) -> -b);
    }

    private Class<T> clazz;
    private ExpressionObject(Class<T> clazz){
        this.clazz = clazz;
    }

    public static class UserOperationException extends Exception {
        private String operationTag;
        private String allExpression = null;
        private Exception exception;

        public UserOperationException(String operationTag, Exception exception){
            super();
            this.operationTag = operationTag;
            this.exception = exception;
        }

        public String getOperationTag() {
            return operationTag;
        }

        public String getAllExpression() {
            return allExpression;
        }

        public void setAllExpression(String allExpression) {
            this.allExpression = allExpression;
        }

        public void setOperationTag(String operationTag) {
            this.operationTag = operationTag;
        }

        @Override
        public String toString() {
            return "UserOperationException:\nOperation Tag: " + operationTag + "\nString Expression: " + allExpression + "\nException: \n" + exception.toString();
        }
    }


    private T executePrimitiveExpression(String expression, T nullValue, List<String> operationsPriority, Map<String, List<String>> operationsStrings, Map<String, Operation<T>> operations, List<T> executingValues, int operationsOffset) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, UserOperationException {
        if (expression.startsWith("${") && expression.endsWith("}")) return executingValues.get(Integer.valueOf(expression.substring(2, expression.length() - 1)));

        if (operationsOffset > operationsPriority.size() - 1 || expression.length() < 1) return clazz.getConstructor(String.class).newInstance(expression);

        String operationTag = operationsPriority.get(operationsPriority.size() - 1 - operationsOffset);

        String[] highPriorityExpressionsArray = StringUtils.split(expression, operationsStrings.get(operationTag));
        Operation<T> executingOperation = operations.get(operationTag);

        T result = highPriorityExpressionsArray[0].length() > 0 ? executePrimitiveExpression(highPriorityExpressionsArray[0], nullValue, operationsPriority, operationsStrings, operations, executingValues, operationsOffset + 1) : nullValue;
        for (int i = 1; i < highPriorityExpressionsArray.length; i++) {
            try {
                result = executingOperation.execute(result, highPriorityExpressionsArray[i].length() > 0 ? executePrimitiveExpression(highPriorityExpressionsArray[i], nullValue, operationsPriority, operationsStrings, operations, executingValues, operationsOffset + 1) : nullValue);
            } catch (Exception e) {
                throw new UserOperationException(operationTag, e);
            }
        }
        return result;
    }

    private T executeFullExpression(String expression, T nullValue, List<String> operationPriority, Map<String, List<String>> operationsString, Map<String, Operation<T>> operations, List<T> executingValue) throws InvocationTargetException, NoSuchMethodException, UserOperationException, InstantiationException, IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder();
        int prev_end_index = 0;
        int start_index = expression.indexOf("(");
        int end_index = expression.indexOf(")");
        if (start_index == end_index) {
            return executePrimitiveExpression(expression, nullValue, operationPriority, operationsString, operations, executingValue, 0);
        }else {
            while (start_index != end_index) {
                int nextIndex = expression.indexOf("(", start_index + 1);
                while (nextIndex > -1 && nextIndex < end_index) {
                    end_index = expression.indexOf(")", end_index + 1);
                    nextIndex = expression.indexOf("(", nextIndex + 1);
                }
                T tmpValue = executeFullExpression(expression.substring(start_index + 1, end_index), nullValue, operationPriority, operationsString, operations, executingValue);
                stringBuilder.append(expression, prev_end_index, start_index).append("${").append(executingValue.size()).append("}").append(expression, end_index + 1, nextIndex > -1 ? nextIndex : expression.length());
                executingValue.add(tmpValue);
                start_index = nextIndex;
                prev_end_index = end_index + 1;
                end_index = start_index > -1 ? expression.indexOf(")", start_index + 1) : -1;
            }
            return executePrimitiveExpression(stringBuilder.toString(), nullValue, operationPriority, operationsString, operations, executingValue, 0);
        }
    }

    /**
     * Interface for operation with your custom objects
     * @param <K> - Your custom class
     */
    public interface Operation<K> {

        /**
         *
         * @param a First your custom object
         * @param b Second your custom object
         * @return You custom object
         * @throws Exception
         */
        K execute(K a, K b) throws Exception;
    }

    /**
     * Create ExpressionObject witch can work this your custom object
     *
     * Your custom class must have constructor with String class
     * @param clazz Reference from your custom object or class
     * @param <K> Your custom class
     * @return ExpressionObject/<K/>
     */
    public static <K> ExpressionObject<K> createExpression(Class<K> clazz){
        return new ExpressionObject<K>(clazz);
    }

    /**
     * Create ExpressionObject witch can work this your custom object.
     *
     * Your custom class must have constructor with String class
     * @param obj Your custom object
     * @param <K> Your custom class
     * @return ExpressionObject/<K/>
     */
    public static <K> ExpressionObject<K> createExpression(K obj){
        return new ExpressionObject<K>((Class<K>) obj.getClass());
    }

    /**
     * Execute your custom expression with your custom setting with your custom object
     * @param expression String expression
     * @param nullValue Value equals 0 in integer
     * @param operationsPriority List of operations tags order by priority. 0 - max priority. Last - min priority.
     * @param operationsStrings Map of (Operations tags) => (List operations in string expression)
     * @param operations Map of (Operations tags) => (Operations interface)
     * @return Value of your custom class
     * @throws Exception
     */
    public T executeCustomFullExpression(String expression, T nullValue, List<String> operationsPriority, Map<String, List<String>> operationsStrings, Map<String, Operation<T>> operations) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, UserOperationException {
        try {
            return executeFullExpression(expression.replace(" ", ""), nullValue, operationsPriority, operationsStrings, operations, new ArrayList<>());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            System.err.println("Please create constructor with String argument in your custom class");
            throw e;
        } catch (UserOperationException e) {
            e.setAllExpression(expression);
            throw e;
        }
    }

    /**
     * Execute expression with Integer class
     * @param expression String expression
     * @return Integer value
     */
    public static Integer execInteger(String expression) throws UserOperationException {
        try {
            return createExpression(Integer.class).executeCustomFullExpression(expression, 0, operationPriority, operationsString, integersOperations);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            System.err.println("Please write developers on email dantes2104@gmail.com");
        } catch (UserOperationException e) {
            throw e;
        }
        return 0;
    }

    /**
     * Execute expression with BigInteger class
     * @param expression String expression
     * @return BigInteger value
     */
    public static BigInteger execBigInteger(String expression) throws UserOperationException {
        try {
            return createExpression(BigInteger.class).executeCustomFullExpression(expression, new BigInteger("0"), operationPriority, operationsString, bigIntegersOperations);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            System.err.println("Please write developers on email dantes2104@gmail.com");
        } catch (UserOperationException e) {
            throw e;
        }
        return new BigInteger("0");
    }

    /**
     * Execute expression with Double class
     * @param expression String expression
     * @return Double value
     */
    public static Double execDouble(String expression) throws UserOperationException {
        try {
            return createExpression(Double.class).executeCustomFullExpression(expression, new Double("0"), operationPriority, operationsString, doubleOperations);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            System.err.println("Please write developers on email dantes2104@gmail.com");
        } catch (UserOperationException e) {
            throw e;
        }

        return 0.0;
    }

    /**
     * Execute expression with BigDecimal class
     * @param expression String expression
     * @return BigDecimal value
     */
    public static BigDecimal execBigDecimal(String expression) throws UserOperationException, InvocationTargetException {
        try {
            return createExpression(BigDecimal.class).executeCustomFullExpression(expression, new BigDecimal("0"), operationPriority, operationsString, bigDecimalsOperations);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            System.err.println("Please write developers on email dantes2104@gmail.com");
        } catch (UserOperationException e) {
            throw e;
        }

        return new BigDecimal("0");
    }
}

