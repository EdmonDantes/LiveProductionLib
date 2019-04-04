/*
Copyright © 2019 Ilya Loginov. All rights reserved. 
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository

Created dantes on 31.03.19 17:40
*/

package ru.liveproduction.livelib.math.expression;

import java.util.*;

public class ExpressionManager<T> {
    protected TreeMap<Integer, List<Operator<T>>> operatorPriority;
    protected Set<String> allArgumentSeparator;
    protected int maxArgumentSeparatorLength = 0;
    protected int maxStringSynonymLength = 0;

    protected List<Function<T>> functions;

    protected ExpressionManager() {
        this.operatorPriority = new TreeMap<>();
        this.allArgumentSeparator = new TreeSet<>();
        this.functions = new ArrayList<>();
    }

    protected void addOperator(Operator<T> operator, Integer priority) {
        if (operator == null || priority == null) return;

        if (!operatorPriority.containsKey(priority))
            operatorPriority.put(priority, new ArrayList<>());

        operatorPriority.get(priority).add(operator);

        if (operator.argumentsSeparator != null) {
            allArgumentSeparator.addAll(Arrays.asList(operator.argumentsSeparator));
            for (String str : operator.argumentsSeparator)
                maxArgumentSeparatorLength = str.length() > maxArgumentSeparatorLength ? str.length() : maxArgumentSeparatorLength;
        }

        if (operator.defaultArgumentSeparator != null) {
            allArgumentSeparator.add(operator.defaultArgumentSeparator);
            maxArgumentSeparatorLength = operator.defaultArgumentSeparator.length() > maxArgumentSeparatorLength ? operator.defaultArgumentSeparator.length() : maxArgumentSeparatorLength;
        }

        for (String str : operator.stringSynonyms)
            maxStringSynonymLength = str.length() > maxStringSynonymLength ? str.length() : maxStringSynonymLength;

    }

    public ExpressionManager(List<List<Operator<T>>> operations, List<Function<T>> functions){
        this();
        for (int i = 0; i < operations.size(); i++) {
            for (Operator<T> operator : operations.get(i)) {
                addOperator(operator, i);
            }
        }

        this.functions = functions;
    }

    public ExpressionManager(List<List<Operator<T>>> operations, Function<T>[] functions){
        this();
        for (int i = 0; i < operations.size(); i++) {
            for (Operator<T> operator : operations.get(i)) {
                addOperator(operator, i);
            }
        }

        this.functions = Arrays.asList(functions);
    }

    public ExpressionManager(List<List<Operator<T>>> operations){
        this();
        for (int i = 0; i < operations.size(); i++) {
            for (Operator<T> operator : operations.get(i)) {
                addOperator(operator, i);
            }
        }
    }

    public ExpressionManager(Operator<T>[][] operations, List<Function<T>> functions) {
        this();
        for (int i = 0; i < operations.length; i++) {
            for (Operator<T> operator : operations[i]) {
                addOperator(operator, i);
            }
        }

        this.functions = functions;
    }

    public ExpressionManager(Operator<T>[][] operations, Function<T>[] functions) {
        this();
        for (int i = 0; i < operations.length; i++) {
            for (Operator<T> operator : operations[i]) {
                addOperator(operator, i);
            }
        }

        this.functions = Arrays.asList(functions);
    }

    public ExpressionManager(Operator<T>[][] operations) {
        this();
        for (int i = 0; i < operations.length; i++) {
            for (Operator<T> operator : operations[i]) {
                addOperator(operator, i);
            }
        }
    }

    public ExpressionManager(List<Operator<T>> operations, List<Function<T>> functions, int ignore) {
        this();
        for (int i = 0; i < operations.size(); i++) {
            addOperator(operations.get(i), i);
        }

        this.functions = functions;
    }

    public ExpressionManager(List<Operator<T>> operations, Function<T>[] functions, int ignore) {
        this();
        for (int i = 0; i < operations.size(); i++) {
            addOperator(operations.get(i), i);
        }

        this.functions = Arrays.asList(functions);
    }

    public ExpressionManager(List<Operator<T>> operations, int ignore) {
        this();
        for (int i = 0; i < operations.size(); i++) {
            addOperator(operations.get(i), i);
        }
    }

    public ExpressionManager(Operator<T>[] operations, List<Function<T>> functions) {
        this();
        for (int i = 0; i < operations.length; i++) {
            addOperator(operations[i], i);
        }

        this.functions = functions;
    }

    public ExpressionManager(Operator<T>[] operations, Function<T>[] functions) {
        this();
        for (int i = 0; i < operations.length; i++) {
            addOperator(operations[i], i);
        }

        this.functions = Arrays.asList(functions);
    }

    public ExpressionManager(Operator<T>[] operations) {
        this();
        for (int i = 0; i < operations.length; i++) {
            addOperator(operations[i], i);
        }
    }

    public Operator<T> getOperatorFromTag(String tag) {
        for (Map.Entry<Integer, List<Operator<T>>> list : operatorPriority.entrySet()) {
            for (Operator<T> operator : list.getValue()) {
                if (operator.getTag().equals(tag)) return operator;
            }
        }

        return null;
    }

    public Function<T> getFunctionFromTag(String tag) {
        for (Function<T> func : functions) {
            if (func.getTag().equals(tag)) return func;
        }
        return null;
    }

    public List<Function<T>> getFunctionFromStringSynonym(String stringSynonym) {
        List<Function<T>> result = new ArrayList<>();
        for (Function<T> func : functions) {
            if (func.haveStringSynonym(stringSynonym))
                result.add(func);
        }

        return result;
    }

    public List<Map.Entry<Function<T>, Integer>> getFunctionAndLastIndexFromString(String str, int startIndex, int lastIndex) {
        List<Map.Entry<Function<T>, Integer>> result = new ArrayList<>();
        for (Function<T> func : functions) {
            int index = func.indexOfStringSynonymInString(str, startIndex, lastIndex);
            if (index > 0)
                result.add(new AbstractMap.SimpleEntry<>(func, index));
        }

        result.sort((obj1, obj2) -> -obj1.getValue().compareTo(obj2.getValue()));

        return result;
    }

    public List<Map.Entry<Function<T>, Integer>> getFunctionAndLastIndexFromString(String str, int startIndex) {
        return getFunctionAndLastIndexFromString(str, startIndex, str.length() - 1);
    }

    public List<Map.Entry<Operator<T>, Integer>> getOperationFromStringSynonyms(String stringSynonym) {
        List<Map.Entry<Operator<T>, Integer>> result = new ArrayList<>();
        for (Map.Entry<Integer, List<Operator<T>>> tmp0 : operatorPriority.entrySet()) {
            for (Operator<T> operator : tmp0.getValue()) {
                if (operator.haveStringSynonym(stringSynonym)) {
                    result.add(new AbstractMap.SimpleEntry<Operator<T>, Integer>(operator, tmp0.getKey()));
                }
            }
        }

        result.sort((obj1, obj2) -> obj1.getValue().compareTo(obj2.getValue()) | -Integer.compare(obj1.getKey().getCountArgs(), obj2.getKey().getCountArgs()));

        return result;
    }

    public List<Map.Entry<Map.Entry<Operator<T>, Integer>, Integer>> getOperationPriorityAndLastIndexFromString(String str, int startIndex, int lastIndex) {
        List<Map.Entry<Map.Entry<Operator<T>, Integer>, Integer>> result = new ArrayList<>();
        int maxIndex = 0;
        for (Map.Entry<Integer, List<Operator<T>>> tmp0 : operatorPriority.entrySet()) {
            for (Operator<T> operator : tmp0.getValue()) {
                int index = operator.indexOfStringSynonymInString(str, startIndex, lastIndex);
                if (index > 0) {
                    result.add(new AbstractMap.SimpleEntry<>(new AbstractMap.SimpleEntry<>(operator, tmp0.getKey()), index));
                    maxIndex = index > maxIndex ? index : maxIndex;
                }
            }
        }

        int finalMaxIndex = maxIndex;
        result.removeIf((obj1) -> obj1.getValue() != finalMaxIndex);
        result.sort((obj1, obj2) -> -obj1.getValue().compareTo(obj2.getValue()) | obj1.getKey().getValue().compareTo(obj2.getKey().getValue()) | -Integer.compare(obj1.getKey().getKey().getCountArgs(), obj2.getKey().getKey().getCountArgs()));

        return result;
    }

    public List<Map.Entry<Map.Entry<Operator<T>, Integer>, Integer>> getOperationPriorityAndLastIndexFromString(String str, int startIndex) {
        return getOperationPriorityAndLastIndexFromString(str, startIndex, str.length() - 1);
    }

    public int isArgumentSeparator(String str, int startIndex, int lastIndex) {
        for (int i = startIndex + 1; i <= lastIndex && i <= str.length() && i - startIndex <= maxArgumentSeparatorLength; i++)
            if (allArgumentSeparator.contains(str.substring(startIndex, i))) return i;

        return -1;
    }

    public int isArgumentSeparator(String str, int startIndex) {
        return isArgumentSeparator(str, startIndex, str.length());
    }

    public static class OperatorWait<T>{
        private List<Map.Entry<Operator<T>, Integer>> operators;
        private boolean isOpenBracket;
        private int countArguments;
        private boolean isFunction;

        public OperatorWait(){
            this.isOpenBracket = true;
            this.isFunction = false;
        }

        public OperatorWait(Operator<T> operator, int priority, int countArguments, boolean isFunction){
            this.operators = Collections.singletonList(new AbstractMap.SimpleEntry<>(operator, priority));
            this.isFunction = isFunction;
            this.countArguments = countArguments;
            this.isOpenBracket = false;
        }

        public OperatorWait(Operator<T> operator, int priority, boolean isFunction) {
            this(operator, priority, 0, isFunction);
        }

        public OperatorWait(List<Map.Entry<Operator<T>, Integer>> operators, int countArguments) {
            this.operators = operators;
            this.countArguments = countArguments;
            this.isOpenBracket = this.isFunction = false;
        }

        public OperatorWait(List<Function<T>> functions, int countArguments, Object ignore) {
            this.operators = new ArrayList<>();
            for (Function<T> func : functions) {
                operators.add(new AbstractMap.SimpleEntry<>(func, Integer.MAX_VALUE));
            }
            this.countArguments = countArguments;
            this.isFunction = true;
            this.isOpenBracket = false;
        }

        public boolean addArg(String argumentSplitter) {
            if (isOpenBracket) return false;
            List<Map.Entry<Operator<T>, Integer>> tmpOperators = new LinkedList<>();
            if (isFunction) {
                for (Map.Entry<Operator<T>, Integer> func: operators) {
                    Function<T> tmp = (Function<T>) func.getKey();
                    if (tmp.getArgumentSeparator(countArguments).equals(argumentSplitter) && (tmp.haveDynamicCountOfArgs() || tmp.getCountArgs() < countArguments))
                        tmpOperators.add(func);
                }
            } else {
                for (Map.Entry<Operator<T>, Integer> operator : operators) {
                    if ((operator.getKey().haveSuffixForm() && countArguments > 0 || !operator.getKey().haveSuffixForm())
                            && operator.getKey().getArgumentSeparator(operator.getKey().haveSuffixForm() ? countArguments - 1 : countArguments).equals(argumentSplitter) && operator.getKey().getCountArgs() > countArguments)
                        tmpOperators.add(operator);
                }
            }

            if (tmpOperators.size() > 0) {
                countArguments++;
                return true;
            }

            return false;
        }

        public boolean addLastArg() {
            if (isOpenBracket) return false;
            List<Map.Entry<Operator<T>, Integer>> tmpOperators = new LinkedList<>();
            if (isFunction) {
                for (Map.Entry<Operator<T>, Integer> func : operators) {
                    Function<T> function = (Function<T>) func.getKey();
                    if (function.getCountArgs() == countArguments + 1 || function.haveDynamicCountOfArgs() && function.getCountArgs() <= countArguments + 1)
                        tmpOperators.add(func);
                }
            } else {
                for (Map.Entry<Operator<T>, Integer> operator : operators) {
                    if (operator.getKey().countArgs == countArguments + 1)
                        tmpOperators.add(operator);
                }
            }

            if (tmpOperators.size() > 0) {
                countArguments++;
                return true;
            }

            return false;
        }

        public boolean isOperator(){
            return !isFunction;
        }

        public boolean isFunction(){
            return isFunction;
        }

        public boolean isOpenBracket(){
            return isOpenBracket;
        }

        public Operator<T> get(){
            operators.sort(new Comparator<Map.Entry<Operator<T>, Integer>>() {
                @Override
                public int compare(Map.Entry<Operator<T>, Integer> o1, Map.Entry<Operator<T>, Integer> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });

            for (Map.Entry<Operator<T>, Integer> operator : operators) {
                if (operator.getKey().getCountArgs() == countArguments) return operator.getKey();
            }

            return null;
        }
    }

    public LinkedList<Object> createPolishNotathion(String string) {
        String expression = string.replace(" ", "");

        LinkedList<Object> result = new LinkedList<>();

        Stack<OperatorWait<T>> tmpStack = new Stack<>();

        StringBuilder tmpSavePartOfValues = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char tmpChar = expression.charAt(i);

            if (tmpChar == '(') {
                if (tmpSavePartOfValues.length() > 0) {
                    tmpStack.add(new OperatorWait<>(getFunctionFromStringSynonym(tmpSavePartOfValues.toString()), 0, null));
                    tmpStack.add(new OperatorWait<>());
                    tmpSavePartOfValues.setLength(0);
                } else {
                    tmpStack.add(new OperatorWait<>());
                }
            } else if (tmpChar == ')'){
                if (tmpSavePartOfValues.length() > 0) {
                    result.addLast(tmpSavePartOfValues.toString());
                    tmpSavePartOfValues.setLength(0);
                }

                while (!tmpStack.empty() && !tmpStack.peek().isOpenBracket()) {
                    OperatorWait<T> wait = tmpStack.pop();
                    wait.addLastArg();
                    result.addLast(wait.get());
                }

                if (!tmpStack.empty()) tmpStack.pop();
                if (!tmpStack.empty() && tmpStack.peek().isFunction()) {
                    OperatorWait<T> wait = tmpStack.pop();
                    wait.addLastArg();
                    result.addLast(wait.get());
                }
            } else {


                List<Map.Entry<Map.Entry<Operator<T>, Integer>, Integer>> operator = getOperationPriorityAndLastIndexFromString(expression, i);
                if (operator.size() > 0) {
                    boolean leftWord = false;

                    if (tmpSavePartOfValues.length() > 0) {
                        result.addLast(tmpSavePartOfValues.toString());
                        tmpSavePartOfValues.setLength(0);
                        leftWord = true;
                    }

                    
                }
            }
        }
        return null;
    }
}
