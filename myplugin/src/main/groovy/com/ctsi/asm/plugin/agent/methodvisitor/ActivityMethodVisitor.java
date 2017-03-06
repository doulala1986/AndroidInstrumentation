package com.ctsi.asm.plugin.agent.methodvisitor;

import com.ctsi.asm.plugin.agent.ClassInfo;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Created by doulala on 2017/3/6.
 */

public class ActivityMethodVisitor extends MethodVisitor {

    ClassInfo classInfo;
    String method;

    public ActivityMethodVisitor(ClassInfo classInfo, String method, MethodVisitor methodVisitor) {
        super(Opcodes.ASM5, methodVisitor);
        this.classInfo = classInfo;
        this.method = method;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        if (method.equals("onResume")) {
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLdcInsn(classInfo.getName());
            mv.visitLdcInsn("onResume");
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false);
            mv.visitInsn(Opcodes.POP);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitEnd();
        }
    }

    /**
     * 指定本地变量表与操作数栈的大小
     * z注意 如果visitMaxs设置的不合适，可能会造成以下错误：
     * Uncaught translation error: com.android.dx.cf.code.SimException: stack: overflow
     */
    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack + 100, maxLocals + 100);
    }


}