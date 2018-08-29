package com.hc.hcplugin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("--->", "===================");
        new Test();
        Log.e("--->", "===================");

        // /storage/emulated/0/Android/data/<packagename>/cache
        String cachePath1 = getExternalCacheDir().getPath();
        // /data/user/0/<packagename>/cache
        String cachePath2 = getCacheDir().getPath();
        File file = getDir("p_a", 0);
        //  /data/user/0/<packagename>/app_p_a
        String cachePath3 = file.getAbsolutePath();

        System.out.println("---> cachePath1="+cachePath1+", cachePath2="+cachePath2+", cachePath3="+cachePath3);
    }

}
