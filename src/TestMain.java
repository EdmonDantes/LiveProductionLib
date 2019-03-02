import ru.liveproduction.livelib.permissions.GroupManager;

/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/
public class TestMain {

    public static void main(String[] args) throws Exception {
        GroupManager groupManager = new GroupManager();
        groupManager.addGroup("default", "", "user.1.2.3.4.5.6.7.8.9.10.11.12.13.14.15.16.27.68.4.ban", "user.kick");

        groupManager.addGroup("default2", "default", "s");
        groupManager.addGroup("default3", "default2","user.1.2.3.4.5.6.7.8.9.10.11.12.13.14.15.16.27.68.4.ban22314");
        groupManager.addGroup("default4", "default3", "s.sq.sqp.xoa");
        groupManager.addGroup("default5", "default4", "!s.s.s.s");
        groupManager.addGroup("default6", "default5", "sssq.1.2");
        groupManager.addGroup("default7", "default6", "11.21.3.4.5.rsa.d.s.afg.x.s");
        groupManager.addGroup("default8", "default7","a","b","c","d","s","w","w","d","dga","dfgas");
        groupManager.addGroup("default1", "default8", "s", "oxka", "kskx", "sxj", "sklf", "ss.s.s.12.s.s", "sdkoasjfd.xaksfas.askfjaskj.askjsfk.s.s.alx.s.a.x.a.ssf.asf.asf.asfasx.s");

        groupManager.addGroup("user", "default1", "user.get", "user.add.user");
        groupManager.addGroup("admin", "user", "user.s.s.s.s.s.s.s.s.ss.s.s.s.s.s.s.s.s.s.op", "user.permissions.*", "user.add.*");
        var start = System.nanoTime();
        boolean bool = groupManager.havePermission("admin", "user.1.2.3.4.5.6.7.8.9.10.11.12.13.14.15.16.27.68.4.ban");
        var end = System.nanoTime();
        var start1 = System.nanoTime();
        boolean bool1 = groupManager.havePermission("admin", "user.s.s.s.s.s.s.s.s.ss.s.s.s.s.s.s.s.s.s.op");
        var end1 = System.nanoTime();

        System.out.println(end - start);
        System.out.println(end1 - start1);

        System.out.println("---------------");
    }
}
