package ru.liveproduction.livelib.math.expression;

import ru.liveproduction.livelib.utils.GenericUtils;

import java.util.*;

public class ExpressionObject<T> {
    protected OperationManager<T> manager;
    protected Class<T> _class;

    public ExpressionObject(OperationManager<T> manager, Class<T> _class) {
        this.manager = manager;
        this._class = _class;
    }

    public T execute(String expression) throws WrongCountOperationArgumentsException, UserMethodException {
        try {
            LinkedList<Object> polishNotation = PolishNotation.getInstance().createPolishNotation(expression, this.manager, null);
            Stack<T> argumentsStack = new Stack<>();

            for (Object obj : polishNotation) {
                if (obj instanceof String) argumentsStack.push(GenericUtils.createGenericFrom(this._class, (String) obj));
                else {
                    Trio<Operation<T>, Integer, Integer> tmp = (Trio) obj;
                    T[] args = GenericUtils.createGenericArray(this._class, tmp.getFirst().getCountOperationArgs());
                    for (int i = args.length - 1; i > -1 && !argumentsStack.empty(); i--) {
                        args[i] = argumentsStack.pop();
                    }
                    argumentsStack.push(tmp.getFirst().execute(args));
                }
            }

            return argumentsStack.pop();
        } catch (WrongExpressionForPolishNotationException e) {
            return null;
        }
    }

    protected static <T> T executeWithExpressionObject(ExpressionObject<T> obj, String expression) {
        try {
            return obj.execute(expression);
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

    public static Integer executeInteger(String expression) {
        return executeWithExpressionObject(integerExpressionObject, expression);
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

    public static Double executeDouble(String expression) {
        return executeWithExpressionObject(doubleExpressionObject, expression);
    }
}
