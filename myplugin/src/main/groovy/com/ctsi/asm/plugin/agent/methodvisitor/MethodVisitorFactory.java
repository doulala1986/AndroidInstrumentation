package com.ctsi.asm.plugin.agent.methodvisitor;

import com.ctsi.asm.plugin.agent.ClassInfo;
import com.ctsi.asm.plugin.util.Util;

import org.objectweb.asm.MethodVisitor;

/**
 * Created by doulala on 2017/3/2.
 */

public class MethodVisitorFactory {


    public static MethodVisitor DispatchMethodFactory(ClassInfo classInfo, String methodName, MethodVisitor parent) {

        if (Util.isOnClickListener(classInfo, methodName)) {

            return new OnClickMethodVisitor(classInfo, methodName, parent);
        }
        if (Util.isActivity(classInfo)) {
            return new ActivityMethodVisitor(classInfo, methodName, parent);
        }

        return parent;
    }


}
