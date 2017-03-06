package com.ctsi.asm.plugin.agent.methodvisitor;

import com.ctsi.asm.plugin.agent.ClassInfo;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Created by doulala on 2017/3/2.
 */

public class OnClickMethodVisitor extends MethodVisitor {

    ClassInfo classInfo;
    String method;

    public OnClickMethodVisitor(ClassInfo classInfo, String method, MethodVisitor methodVisitor) {
        super(Opcodes.ASM5, methodVisitor);
        this.classInfo = classInfo;
        this.method = method;
    }
    /**
     * 在方法执行前添加字节码命令
     */
    @Override
    public void visitCode() {
        super.visitCode();
        System.out.println("in methodvisitor visitcode");
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLdcInsn("aop");
        mv.visitLdcInsn("456");
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(Opcodes.POP);
        Label l3 = new Label();
        mv.visitLabel(l3);
        mv.visitEnd();
    }

    @Override
    public void visitInsn(int opcode) {
        // 在方法末尾处插入代码
        if (opcode == Opcodes.RETURN) {
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLdcInsn("aop");
            mv.visitLdcInsn("789");
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false);
            mv.visitInsn(Opcodes.POP);
        }
        super.visitInsn(opcode);
    }

    /**
     *
     *  指定本地变量表与操作数栈的大小
     *  z注意 如果visitMaxs设置的不合适，可能会造成以下错误：
     *  Uncaught translation error: com.android.dx.cf.code.SimException: stack: overflow
     */
    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack+100, maxLocals+100);
    }


}
