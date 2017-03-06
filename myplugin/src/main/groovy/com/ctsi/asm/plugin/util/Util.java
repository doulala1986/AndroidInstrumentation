package com.ctsi.asm.plugin.util;

import com.ctsi.asm.plugin.agent.ClassInfo;

import java.util.Arrays;

/**
 * Created by doulala on 2017/3/6.
 */

public class Util {
    public static boolean isOnClickListener(ClassInfo classInfo, String methodName) {
        boolean shouldInstrumentOnClick =
                Arrays.asList(classInfo.getInterfaces()).contains("android/view/View$OnClickListener");

        return shouldInstrumentOnClick && methodName.equals("onClick");

    }

    public static boolean isActivity(ClassInfo classInfo) {

        return classInfo.getSuperName().contains("Activity");
    }
}
