/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/
package ru.liveproduction.livelib.files;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class PreferenceFile {
    protected File file;
    protected Map<String, String> values = new TreeMap<>();
    protected boolean loaded = false;

    public PreferenceFile(String fileName) {
        this.file = new File(fileName);
    }

    public PreferenceFile(File file) {
        this.file = file;
    }

    public boolean loadFile() {
        this.loaded = false;
        if (file != null && file.exists() && file.canRead() && !file.isDirectory()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null ){
                    if (line.contains("#")) line = line.split(Pattern.quote("#"))[0];
                    if (line.contains("//")) line = line.split(Pattern.quote("//"))[0];

                    int index = line.indexOf(":");
                    String key = line.substring(0, index).trim();
                    String value = line.substring(index + 1).trim();
                    if (key.length() > 0 && value.length() > 0) values.put(key, value);
                }
                loaded = true;
            } catch (IOException ignored) {}
        }
        return false;
    }

    public PreferenceFile set(String key, String value) {
        values.put(key, value);
        return this;
    }

    public String get(String key) {
        if (!loaded) loadFile();
        return values.get(key);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (var obj : values.entrySet()) {
            result.append(obj.getKey()).append(": ").append(obj.getValue()).append("\n");
        }
        return result.toString();
    }

    public boolean writeFile() {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.write(this.toString());
            writer.flush();
            writer.close();
            return true;
        } catch (FileNotFoundException ignored) {}
        return false;
    }
}
