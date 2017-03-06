package com.ctsi.asm.plugin.agent.classvisitor;

import com.ctsi.asm.plugin.util.Util;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by doulala on 2017/3/6.
 */

public class ActivityClassVisitor extends AuxiliaryClassInjectAdapter {

    boolean isActivity;

    public ActivityClassVisitor(String auxiliaryClassName, ClassWriter cw) {
        super(auxiliaryClassName, cw);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.isActivity = Util.isActivity(this.classInfo);
    }

    private boolean hasOnResume = false;

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

        if (isActivity && name.equals("onResume")) {
            hasOnResume = true;
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        if (isTargetClass&isActivity&&!hasOnResume) {
            MethodVisitor mv = super.visitMethod(Opcodes.ACC_PROTECTED, "onResume", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "android/app/Activity", "onResume", "()V", false);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitInsn(Opcodes.RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitMaxs(100, 100);
            mv.visitEnd();
        }
        super.visitEnd();
    }
}
