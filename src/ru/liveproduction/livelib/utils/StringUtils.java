/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

package ru.liveproduction.livelib.utils;

import java.util.LinkedList;
import java.util.Collection;
import java.util.List;

public final class StringUtils {
    private StringUtils(){}

    public static List<String> split(String strToSplit, String[] delimiters) {
        List<String> result = new LinkedList<>();
        if (strToSplit == null || strToSplit.length() < 1) return result;
        int startPosition = 0;
        for (int i = 0; i < strToSplit.length(); i++) {
            for (String delimiter : delimiters) {
                if (strToSplit.substring(i, Math.min(i + delimiter.length(), strToSplit.length())).equals(delimiter)) {
                    result.add(strToSplit.substring(startPosition, i));
                    startPosition = i + delimiter.length();
                    i = startPosition - 1;
                    break;
                }
            }
        }
        result.add(strToSplit.substring(Math.max(0, startPosition)));
        return result;
    }

    public static List<String> split(String strToSplit, List<String> delimiters) {
        List<String> result = new LinkedList<>();
        if (strToSplit == null || strToSplit.length() < 1) return result;
        int startPosition = 0;
        for (int i = 0; i < strToSplit.length(); i++) {
            for (String delimiter : delimiters) {
                if (strToSplit.substring(i, Math.min(i + delimiter.length(), strToSplit.length())).equals(delimiter)) {
                    result.add(strToSplit.substring(startPosition, i));
                    startPosition = i + delimiter.length();
                    i = startPosition - 1;
                    break;
                }
            }
        }
        result.add(strToSplit.substring(Math.max(0, startPosition)));
        return result;
    }

    public static List<String> splitWithSave(String strToSplit, String[] delimiters){
        List<String> result = new LinkedList<>();
        if (strToSplit == null || strToSplit.length() < 1) return result;
        int startPosition = 0;
        for (int i = 0; i < strToSplit.length(); i++) {
            for (String delimiter : delimiters) {
                String substring = strToSplit.substring(i, Math.min(i + delimiter.length(), strToSplit.length()));
                if (substring.equals(delimiter)) {
                    if(i != 0) result.add(strToSplit.substring(startPosition, i));
                    result.add(substring);
                    startPosition = i + delimiter.length();
                    i =  startPosition - 1;
                    break;
                }
            }
        }
        result.add(strToSplit.substring(Math.max(0, startPosition)));
        return result;
    }

    public static List<String> splitWithSave(String strToSplit, Collection<String> delimiters){
        List<String> result = new LinkedList<>();
        if (strToSplit == null || strToSplit.length() < 1) return result;
        int startPosition = 0;
        for (int i = 0; i < strToSplit.length(); i++) {
            for (String delimiter : delimiters) {
                String substring = strToSplit.substring(i, Math.min(i + delimiter.length(), strToSplit.length()));
                if (substring.equals(delimiter)) {
                    if(i != 0) result.add(strToSplit.substring(startPosition, i));
                    result.add(substring);
                    startPosition = i + delimiter.length();
                    i =  startPosition - 1;
                    break;
                }
            }
        }
        result.add(strToSplit.substring(Math.max(0, startPosition)));
        return result;
    }
}
