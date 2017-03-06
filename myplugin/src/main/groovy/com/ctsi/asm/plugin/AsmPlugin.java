package com.ctsi.asm.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.AppPlugin;
/**
 * Created by doulala on 2017/2/28.
 */

public class AsmPlugin implements Plugin<Project> {
    public void apply(Project project) {
        /**
         * 注册transform接口
         */
        boolean isApp = project.getPlugins().hasPlugin(AppPlugin.class);
        if (isApp) {

            AppExtension  android = project.getExtensions().getByType(AppExtension.class);

            AuxiliaryInjectTransform transform = new AuxiliaryInjectTransform(project);

            android.registerTransform(transform);//注册transform后就可以对编译-打包之间的切面进行处理了。

        }
    }
}
