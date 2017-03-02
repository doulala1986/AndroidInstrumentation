//package com.ctsi.asm.plugin;
//
//import com.android.build.api.transform.Context;
//import com.android.build.api.transform.DirectoryInput;
//import com.android.build.api.transform.Format;
//import com.android.build.api.transform.JarInput;
//import com.android.build.api.transform.QualifiedContent;
//import com.android.build.api.transform.Transform;
//import com.android.build.api.transform.TransformException;
//import com.android.build.api.transform.TransformInput;
//import com.android.build.api.transform.TransformInvocation;
//import com.android.build.api.transform.TransformOutputProvider;
//import com.android.build.gradle.AppExtension;
//import com.android.build.gradle.api.ApplicationVariant;
//import com.android.build.gradle.api.BaseVariantOutput;
//import com.android.build.gradle.internal.pipeline.TransformManager;
//import com.android.build.gradle.internal.transforms.TransformInputUtil;
//import com.android.builder.core.JackProcessOptions;
//import com.android.builder.internal.compiler.JackConversionCache;
//import com.android.utils.FileUtils;
//import MD5;
//import com.google.common.collect.FluentIterable;
//import com.google.common.collect.ImmutableList;
//import com.google.common.collect.ImmutableSet;
//
//import org.gradle.api.Action;
//import org.gradle.api.DomainObjectSet;
//import org.gradle.api.Project;
//import org.gradle.internal.impldep.org.apache.http.util.TextUtils;
//import org.xml.sax.SAXException;
//
//import java.io.File;
//import java.io.FileFilter;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.Locale;
//import java.util.Set;
//
//import groovy.util.Node;
//import groovy.util.NodeList;
//import groovy.util.XmlParser;
//import groovy.xml.Namespace;
//
//
///**
// * Created by doulala on 2017/2/28.
// */
//
//public class AuxiliaryInjectTransform extends Transform {
//
//    private Project project;
//    private DomainObjectSet<ApplicationVariant> applicationVariants;
//    private String appClassPathName;
//
//    public AuxiliaryInjectTransform(Project project) {
//
//        this.project = project;
//
//        project.afterEvaluate(new Action<Project>() {
//            @Override
//            public void execute(Project project) {
//                AppExtension android = (AppExtension) project.getExtensions().findByName("android");
//                applicationVariants = android.getApplicationVariants();
//            }
//        });
//
//    }
//
//    @Override
//    public String getName() {
//        return "ASM";
//    }
//
//    @Override
//    public Set<QualifiedContent.ContentType> getInputTypes() {
//        return TransformManager.CONTENT_CLASS;
//    }
//
//    @Override
//    public Set<QualifiedContent.Scope> getScopes() {
//        return TransformManager.SCOPE_FULL_PROJECT;
//    }
//
//    @Override
//    public boolean isIncremental() {
//        return false;
//    }
//
//
//    HashSet<DirectoryInput> dirInputs = new HashSet<>();
//
//    HashSet<JarInput> jarInputs = new HashSet<>();
//    FileFilter classFileFilter = new FileFilter() {
//        @Override
//        public boolean accept(File pathname) {
//            return pathname.isFile();
//        }
//    };
//
//
//    @Override
//    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
//        super.transform(transformInvocation);
//
//
//        //收集Transform的输入数据
//        for (TransformInput input : transformInvocation.getInputs()) {
//
//            for (DirectoryInput dirInput : input.getDirectoryInputs()) {
//
//                dirInputs.add(dirInput);
//
//            }
//
//            for (JarInput jarInput : input.getJarInputs()) {
//
//                jarInputs.add(jarInput);
//
//            }
//        }
//
//        //copy class文件
//        File classOutputDir = initClassOutputDictionary(transformInvocation);
//        copyClassToClassOutputDir(classOutputDir, dirInputs);
//
//        //copy jar文件
//        copyJars(transformInvocation, jarInputs);
//
//
//        System.out.println("copyJars finished:");
//
//        //通过解析Manifest.xml 获取Application.class
//        initApplicationClassName(transformInvocation);
//        System.out.println("application class:" + this.appClassPathName);
//
//    }
//
//
//    /**
//     * 使用outputprovider创建出Transform输出class文件的路径
//     * 当Format为DIRECTORY时是返回存放class文件的路径. Format为JAR,返回并创建出Jar文件.
//     *
//     * @param transformInvocation
//     * @return
//     */
//    private File initClassOutputDictionary(TransformInvocation transformInvocation) {
//        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
//        File dirOutput = outputProvider.getContentLocation(
//                "classes", getOutputTypes(), getScopes(), Format.DIRECTORY);
//        if (!dirOutput.exists()) {
//            dirOutput.mkdirs();
//        }
//        return dirOutput;
//
//    }
//
//    private void copyClassToClassOutputDir(File classOutputDir, HashSet<DirectoryInput> dirInputs) throws IOException {
//        System.out.println("dirInputs size :" + dirInputs.size());
//        if (dirInputs.size() > 0) {
//
//            for (DirectoryInput dirInput : dirInputs) {
//                FluentIterable<File> classFiles = FileUtils.getAllFiles(dirInput.getFile());
////                System.out.println("----------classOutputDir path :"+ classOutputDir.getAbsolutePath()+"--------");
//                for (File fileInput : classFiles) {
//                    File fileOutput = new File(fileInput.getAbsolutePath().replace(dirInput.getFile().getAbsolutePath(), classOutputDir.getAbsolutePath()));
//                    if (!fileOutput.exists()) {
//                        fileOutput.getParentFile().mkdirs();
//                    }
//                    System.out.println("Copying class: " + fileInput.getAbsolutePath() + " to output");
//                    FileUtils.copyFile(fileInput, fileOutput);
//                }
////
////                System.out.println("----------FINISH-----------");
//            }
//        }
//    }
//
//
//    private void copyJars(TransformInvocation transformInvocation, HashSet<JarInput> jarInputs) throws IOException {
//        if (jarInputs.size() > 0) {
//            TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
//            for (JarInput jarInput : jarInputs) {
//
//                File jarInputFile = jarInput.getFile();
//                File jarOutputFile = outputProvider.getContentLocation(
//                        getUniqueHashName(jarInputFile), getOutputTypes(), getScopes(), Format.JAR);
//
//                if (!jarOutputFile.exists()) {
//                    jarOutputFile.getParentFile().mkdirs();
//                }
//                System.out.println("Copying jar: " + jarInputFile.getAbsolutePath() + " to output");
//                FileUtils.copyFile(jarInputFile, jarOutputFile);
//            }
//        }
//
//    }
//
//    private String getUniqueHashName(File fileInput) {
//        final String fileInputName = fileInput.getName();
//        if (fileInput.isDirectory()) {
//            return fileInputName;
//        }
//        final String parentDirPath = fileInput.getParentFile().getAbsolutePath();
//        final String pathMD5 = MD5.getMessageDigest(parentDirPath.getBytes());
//        final int extSepPos = fileInputName.lastIndexOf('.');
//        final String fileInputNamePrefix =
//                (extSepPos >= 0 ? fileInputName.substring(0, extSepPos) : fileInputName);
//        return fileInputNamePrefix + '_' + pathMD5;
//    }
//
//    // 解析上面拿到的manifest文件,解析xml文件, 拿到application中android.name 编译过后的路径都为绝对路径
//    // 如果没有指定Application返回NUll
//    private void initApplicationClassName(TransformInvocation transformInvocation) {
//        try {
//
//            String path = transformInvocation.getContext().getPath();
//            System.out.println("path:" + path);
//            String taskNamePrefix = getTaskNamePrefix(this);
//            System.out.println("taskNamePrefix:" + taskNamePrefix);
//            String variantName = path.split(taskNamePrefix)[1].toLowerCase();
//
//            System.out.println("variantName:" + variantName);
//
//
//            File manifestFile = null;
//            String appClassName = "";
//            for (ApplicationVariant variant : this.applicationVariants) {
//                if (variant.getName().equals(variantName)) {
//                    BaseVariantOutput variantOutput = variant.getOutputs().get(0);
//                    manifestFile = variantOutput.getProcessManifest().getManifestOutputFile();
//                    break;
//                }
//            }
//
//            if (manifestFile != null) {
//
//                System.out.println("initApplicationClassName:\n  manifestFilePath: " + manifestFile.getAbsolutePath());
//
//
//                Node parsedManifest = new XmlParser().parse(manifestFile);
//                Namespace androidTag = new Namespace(
//                        "http://schemas.android.com/apk/res/android", "android");
//
//                NodeList applicationNodeList = (NodeList) parsedManifest.get("application");
//                Node applicationNode = (Node) applicationNodeList.get(0);
//                appClassName = (String) applicationNode.attribute(androidTag.get("name"));
//
//                if (appClassName != null && appClassName.length() > 0) {
//                    this.appClassPathName = appClassName.replace('.', '/') + ".class";
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//    }
//
//
//    /**
//     * As same as TransformManager.getTaskNamePrefix
//     * :app:transformClassesWithASMForRelease
//     */
//    private String getTaskNamePrefix(Transform transform) {
//        StringBuilder sb = new StringBuilder(100);
//        sb.append("transform");
//
//        Iterator<QualifiedContent.ContentType> iterator = transform.getInputTypes().iterator();
//        // there's always at least one
//
//
//        sb.append(capitalise(iterator.next().name().toLowerCase(Locale.getDefault())));
//        while (iterator.hasNext()) {
//            sb.append("And").append(capitalise(iterator.next().name().toLowerCase(Locale.getDefault())));
//        }
//
//        sb.append("With").append(transform.getName()).append("For");
//
//        return sb.toString();
//    }
//
//    /**
//     * 返回首字母大写字符串
//     *
//     * @param str
//     * @return
//     */
//    private static String capitalise(String str) {
//        return str == null ? null : (str.length() == 0 ? "" : (new StringBuffer(str.length())).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1)).toString());
//    }
//}
