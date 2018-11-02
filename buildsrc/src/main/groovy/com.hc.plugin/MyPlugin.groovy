package com.hc.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class MyPlugin implements Plugin<Project> {

    void apply(Project project) {
        def android = project.extensions.findByType(AppExtension)

        //-------------------> dir = E:\github\HCPlugin
        println "-------------------> dir = "+project.getRootDir().absolutePath


        /*
---------------> variant = QQDebug baseName = QQ-debug des= DebugQQ build appId()= com.hc.hcplugin flavorName =QQ variantData = ApplicationVariantData{QQDebug} scope = com.android.build.gradle.internal.scope.VariantScopeImpl@133b8413
---------------> variant = QQRelease baseName = QQ-release des= ReleaseQQ build appId()= com.hc.hcplugin flavorName =QQ variantData = ApplicationVariantData{QQRelease} scope = com.android.build.gradle.internal.scope.VariantScopeImpl@48640abb
---------------> variant = baiduDebug baseName = baidu-debug des= DebugBaidu build appId()= com.hc.hcplugin flavorName =baidu variantData = ApplicationVariantData{baiduDebug} scope = com.android.build.gradle.internal.scope.VariantScopeImpl@148ad4d
---------------> variant = baiduRelease baseName = baidu-release des= ReleaseBaidu build appId()= com.hc.hcplugin flavorName =baidu variantData = ApplicationVariantData{baiduRelease} scope = com.android.build.gradle.internal.scope.VariantScopeImpl@51793112
         */
        android.applicationVariants.all { variant ->
            println("---------------> variant = "+variant.getName()+" baseName = "+variant.getBaseName() +" des= " + variant.getDescription()
            +" appId()= "+variant.getApplicationId() +" flavorName =" + variant.getFlavorName()
            +" variantData = "+variant.variantData +" scope = "+variant.variantData.scope
            +" appPackageName = "+variant.generateBuildConfig.appPackageName)

            /*
            ---------------> path = /Users/chenxiaokai/AndroidStudioProjects/HCPlugin/app/build/generated/source/buildConfig/QQ/debug
            ---------------> path = /Users/chenxiaokai/AndroidStudioProjects/HCPlugin/app/build/generated/source/buildConfig/QQ/release
            ---------------> path = /Users/chenxiaokai/AndroidStudioProjects/HCPlugin/app/build/generated/source/buildConfig/baidu/debug
            ---------------> path = /Users/chenxiaokai/AndroidStudioProjects/HCPlugin/app/build/generated/source/buildConfig/baidu/release
             */
            println("---------------> path = "+ variant.getVariantData().getScope().getBuildConfigSourceOutputDir())

            /*
            ---------------> configName = generateQQDebugBuildConfig
            ---------------> configName = generateQQReleaseBuildConfig
            ---------------> configName = generateBaiduDebugBuildConfig
            ---------------> configName = generateBaiduReleaseBuildConfig
             */
            println("---------------> configName = "+ variant.getVariantData().getScope().getGenerateBuildConfigTask().name)

            /*
            ---------------> MergeAssetName = mergeQQDebugAssets
            ---------------> MergeAssetName = mergeQQReleaseAssets
            ---------------> MergeAssetName = mergeBaiduDebugAssets
            ---------------> MergeAssetName = mergeBaiduReleaseAssets
             */
            println("---------------> MergeAssetName = "+ variant.getVariantData().getScope().getMergeAssetsTask().name)

            def mergeAssetsTask = project.tasks.getByName(variant.getVariantData().getScope().getMergeAssetsTask().name)

            println("----------------> filtDir = "+mergeAssetsTask.outputDir)
        }

        android.registerTransform(new MyTransform(project))
    }
}