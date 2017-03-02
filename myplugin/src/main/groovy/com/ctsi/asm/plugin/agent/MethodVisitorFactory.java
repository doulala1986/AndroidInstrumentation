package com.ctsi.asm.plugin.agent;

import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by doulala on 2017/3/2.
 */

public class MethodVisitorFactory {


    public static MethodVisitor DispatchMethodFactory(ClassInfo classInfo, String methodName, MethodVisitor parent) {

        if (isOnClickListener(classInfo, methodName)) {

            return new OnClickMethodVisitor(classInfo, methodName,parent);
        }
        return parent;
    }


    private static boolean isOnClickListener(ClassInfo classInfo, String methodName) {
        boolean shouldInstrumentOnClick =
                Arrays.asList(classInfo.getInterfaces()).contains("android/view/View$OnClickListener");

        return shouldInstrumentOnClick && methodName.equals("onClick");

    }

}
