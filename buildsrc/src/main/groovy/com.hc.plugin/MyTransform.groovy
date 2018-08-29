package com.hc.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

/**
 * Created by HuaChao on 2016/7/4.
 */
public class MyTransform extends Transform {

    Project project

    // 构造函数，我们将Project保存下来备用
    public MyTransform(Project project) {
        this.project = project
    }

    // 设置我们自定义的Transform对应的Task名称
    //生成一个  :app:transformClassesWithCxkTransForDebug 一个 task 然后执行 transform() 方法，这个task 生成的文件在 /build/intermediates/transforms/{getName()}(CxkTrans)/{buildType}/folders
    // 或者 /build/intermediates/transforms/{getName()}(CxkTrans)/{buildType}/jars  这就是为什么 transform() 方法 inputs 会分为 jarInputs 和 directoryInputs 的原因
    @Override
    String getName() {
        return "CxkTrans"
    }

    // 指定输入的类型，通过这里的设定，可以指定我们要处理的文件类型
    //这样确保其他类型的文件不会传入
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    // 指定Transform的作用范围
    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(Context context, Collection<TransformInput> inputs,
                   Collection<TransformInput> referencedInputs,
                   TransformOutputProvider outputProvider, boolean isIncremental)
            throws IOException, TransformException, InterruptedException {
        // Transform的inputs有两种类型，一种是目录，一种是jar包，要分开遍历
        inputs.each { TransformInput input ->
            //对类型为“文件夹”的input进行遍历
            input.directoryInputs.each { DirectoryInput directoryInput ->

                //-------------> DirectoryInput file path = E:\github\HCPlugin\app\build\intermediates\classes\release
                println "-------------> DirectoryInput file path = "+directoryInput.file.absolutePath
                //-------------> DirectoryInput Name = 100b1e8343a076baf6eff18361d02aad422f856e
                println "-------------> DirectoryInput Name = "+directoryInput.name

                //文件夹里面包含的是我们手写的类以及R.class、BuildConfig.class以及R$XXX.class等
                // 运行命令: gradlew assembleRelease
                MyInject.injectDir(directoryInput.file.absolutePath,"com\\hc\\hcplugin")
                // 获取output目录
                def dest = outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes,
                        Format.DIRECTORY)

                // directory dest=E:\github\HCPlugin\app\build\intermediates\transforms\CxkTrans\release\folders\1\1\100b1e8343a076baf6eff18361d02aad422f856e
                println("=======> directory dest="+dest)

                // 将input的目录复制到output指定目录
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
            //对类型为jar文件的input进行遍历
            input.jarInputs.each { JarInput jarInput ->

                //jar文件一般是第三方依赖库jar文件

                // 重命名输出文件（同目录copyFile会冲突）
                def jarName = jarInput.name
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())

                //jarName =75b8d80ac9b152522f1687ec044de6ee9872f850
                //jarName =4aef105f9794937e2b4c1436c4962f80d0c15993
                //jarName =b664fb4c38ef505243e71bf21b7b4327ada9acf3
                //jarName =3a56f972b7d83e3183459c5ec9270d8403ae8477
                //jarName =1907ff264942373c976989fc33e891bf7700fef7
                //jarName =131d0fbb9bf398c27c82e34d8004d826df476227
                //jarName =25ac5e3b4d2dd5b6fd78fd75ae20789e9d8b2d6f
                println("=======> jarName ="+jarName)
                //jarNamePath =C:\Users\lenovo\.android\build-cache\d62f8315880d812293c88f58ca48aa1a13b1dcc3\output\jars\classes.jar
                //jarNamePath =C:\Users\lenovo\AppData\Local\Android\sdk\extras\android\m2repository\com\android\support\support-annotations\24.0.0\support-annotations-24.0.0.jar
                //jarNamePath =C:\Users\lenovo\.android\build-cache\208208583d6a34d112b921e7c54958d4019ac6b2\output\jars\classes.jar
                //jarNamePath =C:\Users\lenovo\.android\build-cache\c44a7ef63fc4372225d5d85fb8b428ecc0e4e9a0\output\jars\classes.jar
                //jarNamePath =C:\Users\lenovo\.gradle\caches\modules-2\files-2.1\org.greenrobot\eventbus\3.0.0\ddd99896e9569eaababbe81b35d80e1b91c4ad85\eventbus-3.0.0.jar
                //jarNamePath =C:\Users\lenovo\.android\build-cache\860c04213a5fbf9c43157c12abb12a6acee666f2\output\jars\classes.jar
                //jarNamePath =C:\Users\lenovo\.android\build-cache\860c04213a5fbf9c43157c12abb12a6acee666f2\output\jars\libs\internal_impl-24.0.0.jar
                println("=======> jarNamePath ="+jarInput.file.getAbsolutePath())

                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                //生成输出路径
                def dest = outputProvider.getContentLocation(jarName + md5Name,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)
                //将输入内容复制到输出

                //jarInput dest=E:\github\HCPlugin\app\build\intermediates\transforms\CxkTrans\release\jars\1\10\75b8d80ac9b152522f1687ec044de6ee9872f850038967b67b4eb0c3a1c784982b77a0fb.jar
                //jarInput dest=E:\github\HCPlugin\app\build\intermediates\transforms\CxkTrans\release\jars\1\10\4aef105f9794937e2b4c1436c4962f80d0c15993aef5e588900d08c3223142ba2a5b3037.jar
                //jarInput dest=E:\github\HCPlugin\app\build\intermediates\transforms\CxkTrans\release\jars\1\10\b664fb4c38ef505243e71bf21b7b4327ada9acf33fd1e55027e631f83d5302eff6192446.jar
                //jarInput dest=E:\github\HCPlugin\app\build\intermediates\transforms\CxkTrans\release\jars\1\10\3a56f972b7d83e3183459c5ec9270d8403ae8477f2550781a0d602fc21da8a074fdf592f.jar
                //jarInput dest=E:\github\HCPlugin\app\build\intermediates\transforms\CxkTrans\release\jars\1\10\1907ff264942373c976989fc33e891bf7700fef77a37712dc77a1a93b360d495b64ee937.jar
                //jarInput dest=E:\github\HCPlugin\app\build\intermediates\transforms\CxkTrans\release\jars\1\10\131d0fbb9bf398c27c82e34d8004d826df47622792b1a14d2f606a54011d04b7a1fa1294.jar
                //jarInput dest=E:\github\HCPlugin\app\build\intermediates\transforms\CxkTrans\release\jars\1\10\25ac5e3b4d2dd5b6fd78fd75ae20789e9d8b2d6ff7115ee6d66a8422bdbc735d4b936eac.jar
                println("=======> jarInput dest="+dest)

                FileUtils.copyFile(jarInput.file, dest)
            }
        }
    }
}