package com.ctsi.asm.plugin.agent;

/**
 * Created by doulala on 2017/3/2.
 */

public class ClassInfo {

    String name;
    String superName;
    String[] interfaces;


    public ClassInfo(String name, String superName, String[] interfaces) {
        this.name = name;
        this.superName = superName;
        this.interfaces = interfaces;
    }

    public String getName() {
        return name;
    }

    public String getSuperName() {
        return superName;
    }

    public String[] getInterfaces() {
        return interfaces;
    }
}
