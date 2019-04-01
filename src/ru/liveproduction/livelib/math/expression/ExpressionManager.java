/*
Copyright © 2019 Ilya Loginov. All rights reserved. 
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository

Created dantes on 31.03.19 17:40
*/

package ru.liveproduction.livelib.math.expression;

import java.util.*;

public class ExpressionManager<T> {
    protected Map<Operator<T>, Integer> operatorPriority;

    protected List<Map.Entry<String, Operator<T>>> operationsStringSynonyms;
    protected Set<String> allArgumentSeparator;

    protected List<Function> functions;

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

    protected ExpressionManager() {
        this.operatorPriority = new HashMap<>();
        this.operationsStringSynonyms = new ArrayList<>();
        this.allArgumentSeparator = new TreeSet<>();
        this.functions = new ArrayList<>();
    }

    public ExpressionManager(List<List<Operator<T>>> list, List<Function> functions){
        this();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).size(); j++) {
                this.operatorPriority.put(list.get(i).get(j), i);

                for (String synonym : operationPriority.get(i).get(j).operationStringSynonyms) {
                    this.operationsStringSynonyms.add(new Map.Entry<String, Operator<T>>(synonym, list.get(i).get(j)));
                }

                if (operationPriority.get(i).get(j).haveArgumentSeparator() && (operationPriority.get(i).get(j).getCountOperationArgs() > 2 || (operationPriority.get(i).get(j).getCountOperationArgs() == 2 || !operationPriority.get(i).get(j).haveSuffixForm()))) {
                    String[] separators = operationPriority.get(i).get(j).argumentsSeparator;
                    String defaultSeparator = operationPriority.get(i).get(j).defaultArgumentSeparator;
                    if (separators != null && separators.length > 0) {
                        this.allArgumentSeparator.addAll(Arrays.asList(separators));
                        for (String sep : separators)
                            if (this.maxArgumentSeparatorLength < sep.length())
                                this.maxArgumentSeparatorLength = sep.length();
                    }
                    if (defaultSeparator != null && defaultSeparator.length() > 0) {
                        this.allArgumentSeparator.add(defaultSeparator);
                        if (this.maxArgumentSeparatorLength < defaultSeparator.length()) this.maxArgumentSeparatorLength = defaultSeparator.length();
                    }
                }
            }
        }
    }
}
