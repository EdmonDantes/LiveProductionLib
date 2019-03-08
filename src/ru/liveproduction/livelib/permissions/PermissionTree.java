/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

package ru.liveproduction.livelib.permissions;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class PermissionTree implements Comparable<PermissionTree>, Serializable {
    protected static String SPLIT_STRING = ".";
    protected static String ALL_PERMISSION_STRING = "*";


    protected PermissionTree parent = null;
    protected String value = null;
    protected Set<PermissionTree> children = new TreeSet<>();
    protected boolean isNegate = false;
    protected boolean isPermission = false;

    protected PermissionTree(PermissionTree parent, String value){
        this.parent = parent;
        this.value = value;
    }

    public PermissionTree(String value) {
        this.value = value;
    }

    public PermissionTree(){}

    public static String getSplitString() {
        return SPLIT_STRING;
    }

    public static void setSplitString(String splitString) {
        SPLIT_STRING = splitString;
    }

    public static String getAllPermissionString() {
        return ALL_PERMISSION_STRING;
    }

    public static void setAllPermissionString(String allPermissionString) {
        ALL_PERMISSION_STRING = allPermissionString;
    }

    @Override
    public int compareTo(PermissionTree o) {
        return value.compareTo(o.value);
    }

    protected PermissionTree down(String value) {
        for (PermissionTree child : children) {
            if (child.value.equals(value)) return child;
        }
        PermissionTree tmp = new PermissionTree(this, value);
        children.add(tmp);
        return tmp;
    }

    protected PermissionTree getPermissionTree(String value) {
        for (PermissionTree child : children) {
            if (child.value.equals(value)) return child;
        }
        return null;
    }

    private String[] getStringParts(String str) {
        return str.split(Pattern.quote(getSplitString()));
    }

    public boolean addPermission(String permission, boolean isNegate) {
        if (permission != null && permission.length() > 0){
            String[] parts = getStringParts(permission);
            PermissionTree tmp = this;
            for (String part : parts) {
                if (tmp.value != null && tmp.value.equals(getAllPermissionString()) && !isNegate) break;
                tmp = tmp.down(part);
            }
            tmp.isNegate = isNegate;
            tmp.isPermission = true;

            if (!tmp.value.equals(getAllPermissionString())) {
                PermissionTree local = new PermissionTree(tmp, getAllPermissionString());
                local.isPermission = true;
                local.isNegate = isNegate;
                tmp.children.add(local);
            }

            return true;
        }
        return false;
    }

    public boolean havePermission(String permission) {
        if (permission != null && permission.length() > 0) {
            String[] parts = getStringParts(permission);
            PermissionTree tmp = this;
            for (String part: parts) {
                if (tmp == null) return false;

                PermissionTree local = tmp.getPermissionTree(part);
                if (local == null) {
                    tmp = tmp.getPermissionTree(getAllPermissionString());
                } else tmp = local;
            }

            return tmp != null && tmp.isPermission && !tmp.isNegate;
        }
        return false;
    }

    public boolean deletePermisison(String permission) {
        if (permission != null && permission.length() > 0) {
            String[] parts = getStringParts(permission);
            PermissionTree tmp = this;
            for (String part : parts) {
                if (part.equals("*")) break;
                tmp = tmp.down(part);
            }

            tmp.isPermission = true;
            tmp.isNegate = !tmp.isNegate;
            tmp.children.clear();
            PermissionTree local = tmp.down("*");
            local.isNegate = tmp.isNegate;
            local.isPermission = true;
        }
        return false;
    }
}
