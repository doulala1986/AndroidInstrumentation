package com.ctsi.asm.plugin.agent.classvisitor;

import com.ctsi.asm.plugin.agent.ClassInfo;
import com.ctsi.asm.plugin.agent.methodvisitor.MethodVisitorFactory;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by jesse on 09/01/2017.
 */

public class AuxiliaryClassInjectAdapter extends ClassVisitor {
    public boolean isTargetClass;

    public AuxiliaryClassInjectAdapter(String auxiliaryClassName, ClassWriter cw) {
        super(Opcodes.ASM5, cw);
    }

    public ClassInfo classInfo;

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        // 在首个回调方法处初始化状态
        this.isTargetClass = ((access & Opcodes.ACC_INTERFACE) == 0) && isNotAndroidSupport(name);
        this.classInfo = new ClassInfo(name, superName, interfaces);
    }

    private boolean isNotAndroidSupport(String className) {
        return !className.contains("android/support");// && className.contains("Activity");

    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

        // <init>或<clinit>是构造方法
        if (mv != null && this.isTargetClass) {
            System.out.println(this.classInfo.getName() + ":" + name);
            mv = MethodVisitorFactory.DispatchMethodFactory(this.classInfo, name, mv);
        }

        return mv;
    }
}
