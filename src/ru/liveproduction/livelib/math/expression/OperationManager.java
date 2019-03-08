package ru.liveproduction.livelib.math.expression;

import java.util.*;

public class OperationManager<K> {
    protected Map<Operation<K>, Integer> operationPriority;
    protected List<Pair<String, Operation<K>>> operationsStringSynonyms;
    protected Set<String> allArgumentSeparator;
    protected int maxArgumentSeparatorLength;
    protected int maxOperationStringSynonymLength;

    protected OperationManager() {
        this.operationPriority = new HashMap<>();
        this.operationsStringSynonyms = new ArrayList<>();
        this.allArgumentSeparator = new TreeSet<>();
        this.maxArgumentSeparatorLength = 0;
        this.maxOperationStringSynonymLength = 0;
    }

    public OperationManager(List<List<Operation<K>>> operationPriority, Object ignore) {
        this();
        for (int i = 0; i < operationPriority.size(); i++) {
            for (int j = 0; j < operationPriority.get(i).size(); j++) {
                this.operationPriority.put(operationPriority.get(i).get(j), i);

                for (String synonym : operationPriority.get(i).get(j).operationStringSynonyms) {
                    this.operationsStringSynonyms.add(new Pair<>(synonym, operationPriority.get(i).get(j)));
                    if (synonym.length() > this.maxOperationStringSynonymLength) this.maxOperationStringSynonymLength = synonym.length();
                }

                if (operationPriority.get(i).get(j).getCountOperationArgs() > 2) {
                    this.allArgumentSeparator.add(operationPriority.get(i).get(j).getArgumentSeparator());

                    if (this.maxArgumentSeparatorLength < operationPriority.get(i).get(j).getArgumentSeparator().length()) maxArgumentSeparatorLength = operationPriority.get(i).get(j).getArgumentSeparator().length();
                }
            }
        }
    }

    protected static <T> List<List<T>> transformToList(T[][] array) {
        List<List<T>> result = new ArrayList<>();

        for (int i = 0; i < array.length; i++) {
            result.add(Arrays.asList(array[i]));
        }

        return result;
    }

    protected static <T> List<List<T>> transformToList(List<T> list) {
        List<List<T>> result = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            List<T> local = new ArrayList<>();
            local.add(list.get(i));
            result.add(local);
        }

        return result;
    }

    public OperationManager(Operation<K>[][] operationPriority) {
        this(transformToList(operationPriority), false);
    }

    public OperationManager(List<Operation<K>> operationPriority) {
        this(transformToList(operationPriority), false);
    }

    public OperationManager(Operation<K>[] operationPriority) {
        this(Arrays.asList(operationPriority));
    }

    public Pair<Operation<K>, Integer> getOperationFromTag(String operationTag) {
        for (Map.Entry<Operation<K>, Integer> pair : operationPriority.entrySet()) {
            if (pair.getKey().getOperationTag().equals(operationTag)) return new Pair<>(pair.getKey(), pair.getValue());
        }

        return new Pair<>(null, -1);
    }

    public List<Pair<Operation<K>, Integer>> getOperationFromStringSynonym(String stringSynonym) {
        List<Pair<Operation<K>, Integer>> result = new ArrayList<>();
        for (Pair<String, Operation<K>> pair : operationsStringSynonyms) {
            if (pair.getFirst().equals(stringSynonym)) result.add(new Pair<Operation<K>, Integer>(pair.getSecond(), operationPriority.get(pair.getSecond())));
        }

        return result;
    }

    public Pair<Operation<K>, Integer> getOperationFromStringSynonym(String stringSynonym, int countArgs) {
        for (Pair<Operation<K>, Integer> pair : getOperationFromStringSynonym(stringSynonym)) {
            if (pair.getFirst().getCountOperationArgs() == countArgs) return pair;
        }
        return new Pair<>(null, -1);
    }

    public Trio<Operation<K>, Integer, Integer> getOperationPriorityAndLastIndexFromString(String str, int startIndex, int lastIndex, int countArgs) {
        Pair<Operation<K>, Integer> maxLengthOperationStringSynonyms = null;
        int i = startIndex + 1, index = i;
        for (; i <= lastIndex && i <= str.length() && i - startIndex <= this.maxOperationStringSynonymLength; i++) {
            Pair<Operation<K>, Integer> tmp = getOperationFromStringSynonym(str.substring(startIndex, i), countArgs);
            if (tmp != null) {
                index = i;
                maxLengthOperationStringSynonyms = tmp;
            }
        }
        if (maxLengthOperationStringSynonyms != null)
            return new Trio<>(maxLengthOperationStringSynonyms.getFirst(), maxLengthOperationStringSynonyms.getSecond(), index);
        return new Trio<>(null, -1, -1);
    }

    public List<Trio<Operation<K>, Integer, Integer>> getOperationPriorityAndLastIndexFromString(String str, int startIndex, int lastIndex) {
        List<Trio<Operation<K>, Integer, Integer>> result = new ArrayList<>();
        for (int i = startIndex + 1; i <= lastIndex && i <= str.length() && i - startIndex <= this.maxOperationStringSynonymLength; i++) {
            String tmp = str.substring(startIndex, i);
            for (Pair<String, Operation<K>> pair : operationsStringSynonyms) {
                if (pair.getFirst().equals(tmp)) result.add(new Trio<>(pair.getSecond(), operationPriority.get(pair.getSecond()), i));
            }
        }

        return result;
    }

    public List<Trio<Operation<K>, Integer, Integer>> getOperationPriorityAndLastIndexFromString(String str, int startIndex) {
        return getOperationPriorityAndLastIndexFromString(str, startIndex, str.length());
    }

    public List<Trio<Operation<K>, Integer, Integer>> getOperationPriorityAndLastIndexFromString(String str) {
        return getOperationPriorityAndLastIndexFromString(str, 0);
    }

    public int isArgumentsSeparator(String str, int startIndex, int lastIndex) {
        for (int i = startIndex + 1; i <= lastIndex && i <= str.length() && i - startIndex <= maxArgumentSeparatorLength; i++) {
            if (allArgumentSeparator.contains(str.substring(startIndex, i))) return i;
        }

        return -1;
    }

    public int isArgumentsSeparator(String str, int startIndex) {
        return isArgumentsSeparator(str, startIndex, str.length());
    }

    @SafeVarargs
    public final K executeOperation(String operationTag, K... args) throws WrongCountOperationArgumentsException, UserMethodException {
        Operation<K> tmp = getOperationFromTag(operationTag).getFirst();
        return tmp != null ? tmp.execute(args) : null;
    }
}
