package com.ctsi.asm.plugin.ziputils;

/**
 * Created by jesse on 30/01/2017.
 */

public class Arrays {
    public static void checkOffsetAndCount(int arrayLength, int offset, int count) {
        if ((offset | count) < 0 || offset > arrayLength || arrayLength - offset < count) {
//            throw new ArrayIndexOutOfBoundsException(arrayLength, offset,
//                count);
            throw new ArrayIndexOutOfBoundsException(offset);
        }
    }
}
