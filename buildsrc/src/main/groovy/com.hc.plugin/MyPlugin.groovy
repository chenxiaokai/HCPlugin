package com.hc.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class MyPlugin implements Plugin<Project> {

    void apply(Project project) {
        def android = project.extensions.findByType(AppExtension)

        //-------------------> dir = E:\github\HCPlugin
        println "-------------------> dir = "+project.getRootDir().absolutePath

        android.registerTransform(new MyTransform(project))
    }
}