package com.onedream.yongshengdemo.yongsheng

import android.util.Log
import android.os.Process

object YongShengNativeLib {

    init {
        System.loadLibrary("yongsheng-native-lib")
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

    external fun createFileIfNotExist(filePath : String)

    external fun obserFile(filePath : String)

    external fun lockFile(filePath : String)

    fun haveProcessDead() {
        Log.e("YongSheng", "回调===>haveProcessDead====>" + Process.myPid())
        YongShengProcessAPI.process().haveProcessDead();
    }
}