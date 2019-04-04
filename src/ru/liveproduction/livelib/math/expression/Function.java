/*
Copyright © 2019 Ilya Loginov. All rights reserved. 
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository

Created dantes on 31.03.19 17:40
*/

package ru.liveproduction.livelib.math.expression;

import java.util.List;
import java.util.Set;

public class Function<T> extends Operator{
    protected boolean haveDynamicCountOfArgs;

    public Function(String tag, Set stringSynonyms, int minCountArgs, Algorithm<T> algorithm, String[] argumentSeparator, String defaultArgumentSeparator, boolean haveDynamicCountOfArgs) {
        super(tag, stringSynonyms, minCountArgs, algorithm, argumentSeparator, defaultArgumentSeparator, false);
        this.haveDynamicCountOfArgs = haveDynamicCountOfArgs;
    }

    public Function(String tag, List stringSynonyms, int minCountArgs, Algorithm<T> algorithm, String[] argumentSeparator, String defaultArgumentSeparator, boolean haveDynamicCountOfArgs) {
        super(tag, stringSynonyms, minCountArgs, algorithm, argumentSeparator, defaultArgumentSeparator, false);
        this.haveDynamicCountOfArgs = haveDynamicCountOfArgs;
    }

    public Function(String tag, String[] stringSynonyms, int minCountArgs, Algorithm<T> algorithm, String[] argumentSeparator, String defaultArgumentSeparator, boolean haveDynamicCountOfArgs) {
        super(tag, stringSynonyms, minCountArgs, algorithm, argumentSeparator, defaultArgumentSeparator, false);
        this.haveDynamicCountOfArgs = haveDynamicCountOfArgs;
    }

    public Function(String tag, String stringSynonyms, int minCountArgs, Algorithm<T> algorithm, String[] argumentSeparator, String defaultArgumentSeparator, boolean haveDynamicCountOfArgs) {
        super(tag, stringSynonyms, minCountArgs, algorithm, argumentSeparator, defaultArgumentSeparator, false);
        this.haveDynamicCountOfArgs = haveDynamicCountOfArgs;
    }

    public Function(String tag, Set stringSynonyms, int minCountArgs, Algorithm<T> algorithm, String defaultArgumentSeparator, boolean haveDynamicCountOfArgs) {
        super(tag, stringSynonyms, minCountArgs, algorithm, defaultArgumentSeparator, false);
        this.haveDynamicCountOfArgs = haveDynamicCountOfArgs;
    }

    public Function(String tag, List stringSynonyms, int minCountArgs, Algorithm<T> algorithm, String defaultArgumentSeparator, boolean haveDynamicCountOfArgs) {
        super(tag, stringSynonyms, minCountArgs, algorithm, defaultArgumentSeparator, false);
        this.haveDynamicCountOfArgs = haveDynamicCountOfArgs;
    }

    public Function(String tag, String[] stringSynonyms, int minCountArgs, Algorithm<T> algorithm, String defaultArgumentSeparator, boolean haveDynamicCountOfArgs) {
        super(tag, stringSynonyms, minCountArgs, algorithm, defaultArgumentSeparator, false);
        this.haveDynamicCountOfArgs = haveDynamicCountOfArgs;
    }

    public Function(String tag, String stringSynonyms, int minCountArgs, Algorithm<T> algorithm, String defaultArgumentSeparator, boolean haveDynamicCountOfArgs) {
        super(tag, stringSynonyms, minCountArgs, algorithm, defaultArgumentSeparator, false);
        this.haveDynamicCountOfArgs = haveDynamicCountOfArgs;
    }

    public Function(String tag, Set stringSynonyms, Algorithm<T> algorithm, boolean haveDynamicCountOfArgs) {
        super(tag, stringSynonyms, algorithm, false);
        this.haveDynamicCountOfArgs = haveDynamicCountOfArgs;
    }

    public Function(String tag, List stringSynonyms, Algorithm<T> algorithm, boolean haveDynamicCountOfArgs) {
        super(tag, stringSynonyms, algorithm, false);
        this.haveDynamicCountOfArgs = haveDynamicCountOfArgs;
    }

    public Function(String tag, String[] stringSynonyms, Algorithm<T> algorithm, boolean haveDynamicCountOfArgs) {
        super(tag, stringSynonyms, algorithm, false);
        this.haveDynamicCountOfArgs = haveDynamicCountOfArgs;
    }

    public Function(String tag, String stringSynonyms, Algorithm<T> algorithm, boolean haveDynamicCountOfArgs) {
        super(tag, stringSynonyms, algorithm, false);
        this.haveDynamicCountOfArgs = haveDynamicCountOfArgs;
    }

    public Function(String tag, Set stringSynonyms, Algorithm<T> algorithm) {
        this(tag, stringSynonyms, algorithm, false);
    }

    public Function(String tag, List stringSynonyms, Algorithm<T> algorithm) {
        this(tag, stringSynonyms, algorithm, false);
    }

    public Function(String tag, String[] stringSynonyms, Algorithm<T> algorithm) {
        this(tag, stringSynonyms, algorithm, false);
    }

    public Function(String tag, String stringSynonyms, Algorithm<T> algorithm) {
        this(tag, stringSynonyms, algorithm, false);
    }

    @Override
    public boolean isFunction() {
        return true;
    }

    public boolean haveDynamicCountOfArgs() {
        return haveDynamicCountOfArgs;
    }
}
