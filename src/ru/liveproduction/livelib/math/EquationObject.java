package ru.liveproduction.livelib.math;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class EquationObject {
    private static final List<String> operationsPriority = Arrays.asList("MULT", "SUB", "SUM");
    private static final Map<String, List<String>> operationsStrings = new HashMap<>();
    private static final Map<String, ExpressionObject.Operation<EquationObject>> operations = new HashMap<>();

    static {
        operationsStrings.put("MULT", Collections.singletonList("*"));
        operationsStrings.put("SUM", Collections.singletonList("+"));
        operationsStrings.put("SUB", Arrays.asList("-", "-"));

        operations.put("MULT", EquationObject::multiply);
        operations.put("SUM", EquationObject::add);
        operations.put("SUB", EquationObject::subtract);
    }

    private List<Double> variable = new ArrayList<>();

    private static String[] split(String strToSplit, String[] delimiters) {
        List<String> arr = new ArrayList<>();
        int startPosition = 0;
        for (int i = 0; i < strToSplit.length(); i++) {
            for (String obj : delimiters) {
                if (strToSplit.substring(i, i + obj.length()).equals(obj)) {
                    arr.add(strToSplit.substring(startPosition, i));
                    startPosition = i + obj.length();
                    i = startPosition - 1;
                    break;
                }
            }
        }
        arr.add(strToSplit.substring(startPosition));
        return arr.toArray(new String[0]);
    }

    private static Set<Long> getDividers(long number) {
        number = number < 0 ? -number : number;
        List<Long> result = new ArrayList<>();
        for (long i = 2; i * i <= number; i++) {
            if (number % i == 0) {
                result.add(i);
                result.add(number / i);
            }
        }

        for (int i = 0; i < result.size(); i++) {
            for (int j = i; j < result.size(); j++) {
                if (result.get(i) != null && result.get(j) != null) {
                    long tmp = result.get(i) * result.get(j);
                    if (number % tmp == 0) result.add(tmp);
                }
            }
        }

        result.add(number);
        return new HashSet<>(result);
    }

    private static Set<Double> getDividers(double number) {
        number = number < 0 ? -number : number;

        long power = 0;

        while (number % 1 != 0) {
            number *= 10;
            power++;
        }

        Set<Long> dividers = getDividers((long) number);
        Set<Double> ret = new HashSet<>();

        double shift = Math.pow(10, power);
        for (Long divider : dividers) {
            ret.add(divider / shift);
        }

        return ret;
    }

    private EquationObject(List<Double> variable) {
        int size = variable.size();
        while (variable.get(size - 1) == null || variable.get(size - 1) == 0) size--;
        this.variable = variable.subList(0, size);
    }

    /**
     * Create EquationObject with value
     * @param variable String variable. (Example: "2x5". 2 - count of x. 5 - power of x)
     */
    public EquationObject(String variable) {
        String[] tmp = split(variable.toLowerCase(), new String[]{"x"});
        Double count = tmp[0].length() > 0 ? new Double(tmp[0]) : 1;
        int power = tmp.length < 2 ? 0 : tmp[1].length() < 1 ? 1 : new Integer(tmp[1]);

        while (this.variable.size() < power) this.variable.add(0.0);

        this.variable.add(count);
    }

    /**
     * Add two EquationObject
     * @param obj
     * @return new EquationObject
     */
    public EquationObject add(EquationObject obj) {
        List<Double> variable = new ArrayList<>(Math.max(this.variable.size(), obj.variable.size()));
        for (int i = 0; i < Math.min(this.variable.size(), obj.variable.size()); i++) {
            variable.add(this.variable.get(i) + obj.variable.get(i));
        }

        for (int i = Math.min(this.variable.size(), obj.variable.size()); i < Math.max(this.variable.size(), obj.variable.size()); i++) {
            variable.add(this.variable.size() > obj.variable.size() ? this.variable.get(i) : obj.variable.get(i));
        }

        return new EquationObject(variable);
    }


    /**
     * subtract two EquationObject
     * @param obj
     * @return new EquationObject
     */
    public EquationObject subtract(EquationObject obj) {
        List<Double> variable = new ArrayList<>(Math.max(this.variable.size(), obj.variable.size()));
        for (int i = 0; i < Math.min(this.variable.size(), obj.variable.size()); i++) {
            variable.add(this.variable.get(i) - obj.variable.get(i));
        }

        for (int i = Math.min(this.variable.size(), obj.variable.size()); i < Math.max(this.variable.size(), obj.variable.size()); i++) {
            variable.add(this.variable.size() > obj.variable.size() ? this.variable.get(i) : -obj.variable.get(i));
        }

        return new EquationObject(variable);
    }

    /**
     * multiply two EquationObject
     * @param obj
     * @return new EquationObject
     */
    public EquationObject multiply(EquationObject obj) {
        Double[] variable = new Double[this.variable.size() * obj.variable.size()];
        for (int i = 0; i < Math.max(this.variable.size(), obj.variable.size()); i++) {
            for (int j = 0; j < Math.min(this.variable.size(), obj.variable.size()); j++) {
                if (variable[i + j] == null) variable[i + j] = this.variable.size() > obj.variable.size() ? this.variable.get(i) * obj.variable.get(j) : obj.variable.get(i) * this.variable.get(i);
                else variable[i + j] += this.variable.size() > obj.variable.size() ? this.variable.get(i) * obj.variable.get(j) : obj.variable.get(i) * this.variable.get(i);
            }
        }

        return new EquationObject(Arrays.asList(variable));
    }

    /**
     * Create string of this equation
     * @return Equation string (Example: "2.0x2 + 1.0x1 - 9.0")
     */
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = variable.size() - 1; i > -1; i--) {
            if (variable.get(i) != null && variable.get(i) != 0.0) {
                if (variable.get(i) > 0 && i != variable.size() - 1) stringBuilder.append("+");
                stringBuilder.append(variable.get(i));
                if (i != 0) {
                    stringBuilder.append("x");
                    stringBuilder.append(i);
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Try to get integer root of this equation
     * @return Set integers roots of this equation
     */
    public Set<Double> getX(){
        Set<Double> result = new HashSet<>();
        List<Double> tmpVariables = new ArrayList<>();

        int maxPower = this.variable.size() - 1;
        while (this.variable.get(maxPower) == 0) maxPower--;
        double countMaxPower = this.variable.get(maxPower);

        if (countMaxPower == 0) {
            result.add(0.0);
            return result;
        }

        for (int i = 0; i <= maxPower; i++) {
            tmpVariables.add(this.variable.get(i) / countMaxPower);
        }

        while (tmpVariables.size() > 0 && tmpVariables.get(0) == 0) {
            tmpVariables = tmpVariables.subList(1, tmpVariables.size());
        }

        Set<Double> dividers = getDividers(tmpVariables.get(0));
        _while:
        while (tmpVariables.size() > 3){
            for (Double divider : dividers) {
                Double[] newTmpVariablesAfterDivide = new Double[tmpVariables.size()];
                newTmpVariablesAfterDivide[tmpVariables.size() - 1] = tmpVariables.get(tmpVariables.size() - 1);
                for (int i = tmpVariables.size() - 2; i > -1; i--) {
                    newTmpVariablesAfterDivide[i] = newTmpVariablesAfterDivide[i + 1] * divider + tmpVariables.get(i);
                }
                if (newTmpVariablesAfterDivide[0] == 0.0){
                    result.add(divider);
                    tmpVariables = Arrays.asList(newTmpVariablesAfterDivide).subList(1, tmpVariables.size());
                    continue _while;
                }
            }
            break;
        }
        if (tmpVariables.size() == 3) {
            double discriminant = tmpVariables.get(1) * tmpVariables.get(1) - 4 * tmpVariables.get(0) * tmpVariables.get(2);
            if (discriminant >= 0) {
                result.add((-tmpVariables.get(1) + discriminant) / 2);
                result.add((-tmpVariables.get(1) - discriminant) / 2);
            }
        }else if (tmpVariables.size() == 2 && tmpVariables.get(0) != null && tmpVariables.get(1) != null){
            result.add(tmpVariables.get(0) / tmpVariables.get(1));
        }

        return result;
    }

    /**
     * Get EquationObject from full string expression
     * @param expression Full string expression (Example: "(2x + 3x2 - 8x3) * (3x + 9x2)")
     * @return EquationObject equals $expression
     */
    public static EquationObject valueOf(String expression) {
        try {
            return ExpressionObject.createExpression(EquationObject.class).executeCustomFullExpression(expression, new EquationObject("0"), operationsPriority, operationsStrings, operations);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | ExpressionObject.UserOperationException e) {
            System.err.println("Please write developers on email dantes2104@gmail.com");
        }

        return new EquationObject("0");
    }
}
