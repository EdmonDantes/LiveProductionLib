/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

package ru.liveproduction.livelib.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    private StringUtils(){}

    public static String[] split(String strToSplit, String[] delimiters) {
        if (strToSplit == null) return new String[]{};
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

    public static String[] split(String strToSplit, List<String> delimiters) {
        if (strToSplit == null) return new String[]{};
        List<String> arr = new ArrayList<>();
        int startPosition = 0;
        for (int i = 0; i < strToSplit.length(); i++) {
            for (String obj : delimiters) {
                if (strToSplit.substring(i, Math.min(i + obj.length(), strToSplit.length())).equals(obj)) {
                    arr.add(strToSplit.substring(startPosition, i));
                    startPosition = i + obj.length();
                    i = startPosition - 1;
                    break;
                }
            }
        }
        arr.add(strToSplit.substring(Math.max(0, startPosition)));
        return arr.toArray(new String[0]);
    }
}
