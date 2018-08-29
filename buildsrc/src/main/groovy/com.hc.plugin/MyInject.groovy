package com.hc.plugin

import javassist.ClassPool
import javassist.CtClass
import javassist.CtConstructor
public class MyInject {

    private static ClassPool pool = ClassPool.getDefault()
    private static String injectStr = "System.out.println(\"I Love HuaChao\" ); ";

    public static void injectDir(String path, String packageName) {

        //参数1:E:\github\HCPlugin\app\build\intermediates\classes\release     参数2: com\hc\hcplugin
        println("=======>"+path+", packageName="+packageName)

        pool.appendClassPath(path)
        File dir = new File(path)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { File file ->

                String filePath = file.absolutePath

                /*
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\graphics
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\graphics\drawable
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\graphics\drawable\animated
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\graphics\drawable\animated\R.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\graphics\drawable\R.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\v4
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\v4\R.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\v7
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\v7\appcompat
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\v7\appcompat\R$anim.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\v7\appcompat\R$attr.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\v7\appcompat\R$bool.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\v7\appcompat\R$color.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\v7\appcompat\R$dimen.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\v7\appcompat\R$drawable.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\v7\appcompat\R$id.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\v7\appcompat\R$integer.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\v7\appcompat\R$layout.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\v7\appcompat\R$string.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\v7\appcompat\R$style.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\v7\appcompat\R$styleable.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\android\support\v7\appcompat\R.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\com
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\com\hc
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\com\hc\hcplugin
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\com\hc\hcplugin\BuildConfig.class

               (这里进入if判断)=======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\com\hc\hcplugin\MainActivity.class

                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\com\hc\hcplugin\R$anim.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\com\hc\hcplugin\R$attr.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\com\hc\hcplugin\R$bool.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\com\hc\hcplugin\R$color.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\com\hc\hcplugin\R$dimen.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\com\hc\hcplugin\R$drawable.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\com\hc\hcplugin\R$id.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\com\hc\hcplugin\R$integer.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\com\hc\hcplugin\R$layout.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\com\hc\hcplugin\R$mipmap.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\com\hc\hcplugin\R$string.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\com\hc\hcplugin\R$style.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\com\hc\hcplugin\R$styleable.class
                =======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\com\hc\hcplugin\R.class

                (这里进入if判断)=======>  filePath = E:\github\HCPlugin\app\build\intermediates\classes\release\com\hc\hcplugin\Test.class

                 */
                println("=======>  filePath = "+filePath)

                //确保当前文件是class文件，并且不是系统自动生成的class文件
                if (filePath.endsWith(".class")
                        && !filePath.contains('R$')
                        && !filePath.contains('R.class')
                        && !filePath.contains("BuildConfig.class")) {
                    // 判断当前目录是否是在我们的应用包里面
                    int index = filePath.indexOf(packageName);
                    boolean isMyPackage = index != -1;
                    if (isMyPackage) {
                        int end = filePath.length() - 6 // .class = 6
                        String className = filePath.substring(index, end).replace('\\', '.').replace('/', '.')

                        // =======>  className = com.hc.hcplugin.MainActivity
                        //=======>  className = com.hc.hcplugin.Test
                        println("=======>  className = "+className)

                        //开始修改class文件
                        CtClass c = pool.getCtClass(className)

                        if (c.isFrozen()) {
                            c.defrost()
                        }

                        CtConstructor[] cts = c.getDeclaredConstructors()
                        pool.importPackage("android.util.Log");
                        if (cts == null || cts.length == 0) {
                            //手动创建一个构造函数
                            CtConstructor constructor = new CtConstructor(new CtClass[0], c)
                            constructor.insertBeforeBody(injectStr)
                            c.addConstructor(constructor)
                        } else {
                            cts[0].insertBeforeBody(injectStr)
                        }
                        c.writeFile(path)
                        c.detach()
                    }
                }
            }
        }
    }


}