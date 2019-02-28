/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

import ru.liveproduction.livelib.math.ExpressionObject;
import ru.liveproduction.livelib.math.HashingFNV1;

import static ru.liveproduction.livelib.math.HashingFNV1.Bits.*;

public class TestMain {

    public static void main(String[] args) throws Exception {
        var start = System.currentTimeMillis();
        System.out.println(HashingFNV1.getInstance(x512).hashString("3qtVZhl1t3jKXJNGE3noXL9gguUWZW3ge28FA9RBYHEXNVFhZjLlJDYcp0MncR9CyvqSAXtgjsffOAQYWFIJyOm5K4GV1yxMJLWesPTR9XQx2AQFEjak6PRzbOHwtuKczaEeIm9LXBIFqJWFR3pMiwo2AgzUj6MOKT3EUTFtbB4yd9NnVKHO95PKkEJvLOI1oJaHACytaQoonUrMiY1UUKjEOHfkVXKH1b5oLfGrGPyoRmgJbsF7nqVpEtmKyhrtkxC0eW45X3YSbvvtUu5LZ8jt7fNO5Pt88T88oqKTQ2aMjUherhFsRnrpPkYRSjvxNf4523Kbv0tWWQH5Xiktvu9ugMBanPKCKjsboyAQlXjjRIlBB3tP9QWMd0Hb0LUGXbtRUg3dLGbiGlie").toString().length()); //93051914
        var end = System.currentTimeMillis();

        System.out.println(end - start);
        System.out.println("---------------");
    }
}
