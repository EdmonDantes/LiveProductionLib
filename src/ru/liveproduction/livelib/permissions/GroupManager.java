package ru.liveproduction.livelib.permissions;

import java.util.Map;
import java.util.TreeMap;

public class GroupManager {
    public static class Group {
        protected Group parent;
        protected PermissionTree tree;

        public Group(Group parent, PermissionTree tree) {
            this.parent = parent;
            this.tree = tree;
        }

        public boolean addPermission(String permission) {
            boolean negate = permission.charAt(0) == '!';
            return this.tree.addPermission(negate ? permission.substring(1) : permission, negate);
        }

        public boolean havePermission(String permission) {
            return tree.havePermission(permission) || (parent != null && parent.havePermission(permission));
        }

        public boolean deletePermission(String permission) {
            return tree.deletePermisison(permission);
        }
    }

    protected Map<String, Group> groups = new TreeMap<>();

    public GroupManager(){}

    public boolean addGroup(String name, String parent, String... permissions){
        if (name == null) return false;
        PermissionTree permissionTree = new PermissionTree();
        for (String permission : permissions) {
            boolean negate = permission.charAt(0) == '!';
            permissionTree.addPermission(negate ? permission.substring(1) : permission, negate);
        }
        groups.put(name.toLowerCase(), new Group(parent != null ? groups.get(parent) : null, permissionTree));
        return true;
    }

    public Group getGroup(String name) {
        return name != null ? groups.get(name.toLowerCase()) : null;
    }

    public boolean deleteGroup(String name) {
        if (name != null) {
            groups.remove(name);
            return true;
        }
        return false;
    }

    public boolean addPermissions(String name, String... permissions) {
        Group tmp = getGroup(name);
        if (tmp == null) return false;
        for (String permission : permissions) {
            if (!tmp.addPermission(permission)) return false;
        }
        return true;
    }

    public boolean havePermission(String name, String permission) {
        Group tmp = getGroup(name);
        if (tmp == null) return false;
        return tmp.havePermission(permission);
    }

    public boolean deletePermission(String name, String permission) {
        Group tmp = getGroup(name);
        if (tmp == null) return false;
        return tmp.deletePermission(permission);
    }
}
