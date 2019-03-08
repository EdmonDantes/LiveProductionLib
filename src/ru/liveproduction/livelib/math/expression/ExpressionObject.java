package ru.liveproduction.livelib.math.expression;

import ru.liveproduction.livelib.utils.GenericUtils;

import java.util.*;

/**
 * Class for calculate string to your class
 * @param <T> Your class
 */
public class ExpressionObject<T> {
    protected OperationManager<T> operationManager;
    protected Class<T> _class;

    public ExpressionObject(OperationManager<T> operationManager, Class<T> _class) {
        this.operationManager = operationManager;
        this._class = _class;
    }

    /**
     * Method for calculate
     * @param expression String expression
     * @return Value with your class
     * @throws WrongCountOperationArgumentsException Wrong count arguments for your operation
     * @throws UserMethodException What happened in your operationFunction object
     */
    public T calculate(String expression) throws WrongCountOperationArgumentsException, UserMethodException, WrongExpressionException {
        LinkedList<Object> partsOfPolishNotation = PolishNotation.getInstance().createPolishNotation(expression, this.operationManager, null);
        Stack<T> argumentsStack = new Stack<>();

        for (Object argumentsOrOperation : partsOfPolishNotation) {
            if (argumentsOrOperation instanceof String)
                argumentsStack.push(GenericUtils.createGenericFrom(this._class, (String) argumentsOrOperation));
            else {
                Trio<Operation<T>, Integer, Integer> operation = (Trio) argumentsOrOperation;
                T[] args = GenericUtils.createGenericArray(this._class, operation.getFirst().getCountOperationArgs());
                for (int i = args.length - 1; i > -1 && !argumentsStack.empty(); i--) {
                    args[i] = argumentsStack.pop();
                }
                argumentsStack.push(operation.getFirst().execute(args));
            }
        }

        return argumentsStack.pop();
    }


    protected static <T> T calculateWithExpressionObject(ExpressionObject<T> obj, String expression) throws WrongExpressionException {
        try {
            return obj.calculate(expression);
        } catch (WrongCountOperationArgumentsException | UserMethodException e) {
            System.err.println("Please write to developer");
            e.printStackTrace();
            return null;
        }
    }

    protected static final OperationManager<Integer> integersManager = new OperationManager<>(new Operation[][]{
            new Operation[] {
                    new Operation<Integer>("NOT", new String[]{"-", "-", "−"}, 1, new OperationFunction<Integer>() {
                        @Override
                        public Integer execute(Integer[] args) {
                            return -args[0];
                        }
                    })
            },
            new Operation[] {
                    new Operation<Integer>("LOG", new String[]{"log", "lg", "l"}, new OperationFunction<Integer>() {
                        @Override
                        public Integer execute(Integer[] args) {
                            return (int) (Math.log(args[0]) / Math.log(args[1]));
                        }
                    })
            },
            new Operation[] {
                    new Operation<Integer>("POW", new String[]{"^"}, new OperationFunction<Integer>() {
                        @Override
                        public Integer execute(Integer[] args) {
                            return (int) Math.pow(args[0], args[1]);
                        }
                    })
            },
            new Operation[]{
                    new Operation<Integer>("MULT", new String[]{"*", "x"}, new OperationFunction<Integer>() {
                        @Override
                        public Integer execute(Integer[] args) {
                            return args[0] * args[1];
                        }
                    }),
                    new Operation<Integer>("DIV", new String[]{"/", ":", "÷"}, new OperationFunction<Integer>() {
                        @Override
                        public Integer execute(Integer[] args) {
                            return args[0] / args[1];
                        }
                    })
            },
            new Operation[]{
                    new Operation<Integer>("SUM", new String[]{"+"}, new OperationFunction<Integer>() {
                        @Override
                        public Integer execute(Integer[] args) {
                            return args[0] + args[1];
                        }
                    }),
                    new Operation<Integer>("SUB", new String[]{"-", "−", "-"}, new OperationFunction<Integer>() {
                        @Override
                        public Integer execute(Integer[] args) {
                            return args[0] - args[1];
                        }
                    })
            }

    });


    protected static final ExpressionObject<Integer> integerExpressionObject = new ExpressionObject<Integer>(integersManager, Integer.class);

    /**
     * Calculate simple integer expression
     * @param expression String with expression
     * @return Integer value
     */
    public static Integer calculateInteger(String expression) throws WrongExpressionException {
        return calculateWithExpressionObject(integerExpressionObject, expression);
    }


    protected static final OperationManager<Double> doublesManager = new OperationManager<>(new Operation[][]{
            new Operation[] {
                    new Operation<Double>("NOT", new String[]{"-", "-", "−"}, 1, new OperationFunction<Double>() {
                        @Override
                        public Double execute(Double[] args) {
                            return -args[0];
                        }
                    })
            },
            new Operation[] {
                    new Operation<Double>("LOG", new String[]{"log", "lg", "l"}, new OperationFunction<Double>() {
                        @Override
                        public Double execute(Double[] args) {
                            return (Math.log(args[0]) / Math.log(args[1]));
                        }
                    })
            },
            new Operation[] {
                    new Operation<Double>("POW", new String[]{"^"}, new OperationFunction<Double>() {
                        @Override
                        public Double execute(Double[] args) {
                            return Math.pow(args[0], args[1]);
                        }
                    })
            },
            new Operation[]{
                    new Operation<Double>("MULT", new String[]{"*", "x"}, new OperationFunction<Double>() {
                        @Override
                        public Double execute(Double[] args) {
                            return args[0] * args[1];
                        }
                    }),
                    new Operation<Double>("DIV", new String[]{"/", ":", "÷"}, new OperationFunction<Double>() {
                        @Override
                        public Double execute(Double[] args) {
                            return args[0] / args[1];
                        }
                    })
            },
            new Operation[]{
                    new Operation<Double>("SUM", new String[]{"+"}, new OperationFunction<Double>() {
                        @Override
                        public Double execute(Double[] args) {
                            return args[0] + args[1];
                        }
                    }),
                    new Operation<Double>("SUB", new String[]{"-", "−", "-"}, new OperationFunction<Double>() {
                        @Override
                        public Double execute(Double[] args) {
                            return args[0] - args[1];
                        }
                    })
            }

    });


    protected static final ExpressionObject<Double> doubleExpressionObject = new ExpressionObject<Double>(doublesManager, Double.class);

    /**
     * Calculate simple double value
     * @param expression String with expression
     * @return Double value
     */
    public static Double calculateDouble(String expression) throws WrongExpressionException {
        return calculateWithExpressionObject(doubleExpressionObject, expression);
    }
}
