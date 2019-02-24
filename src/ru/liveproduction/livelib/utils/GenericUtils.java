/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

package ru.liveproduction.livelib.utils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

public final class GenericUtils {

    private GenericUtils(){}

    public static <T> T createGeneric(Class<T> _class) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return _class.getConstructor().newInstance();
    }

    public static <T, K> T createGenericFrom(Class<T> _class, K value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return _class.getConstructor(value.getClass()).newInstance(value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] createGenericArray(Class<T> _class, int length) {
        return (T[]) Array.newInstance(_class, length);
    }

    @SafeVarargs
    public static <T> T[] createGenericArrayWithValues(Class<T> _class, T ...args) {
        T[] result = createGenericArray(_class, args.length);
        for (int i = 0; i < args.length; i++) {
            result[i] = args[i];
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] createGenericArrayFromObjectArray(Class<T> _class, Object[] array){
        T[] result = createGenericArray(_class, array.length);
        for (int i = 0; i < array.length; i++){
            result[i] = (T) array[i];
        }
        return result;
    }
}
