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
    //锁住自己的文件，尝试锁住别的进程已锁住的文件
    // A进程    A文件（锁住）          B文件（尝试锁住观察，确保B进程已锁住）
    // B进程    A文件（尝试锁住观察，确保A进程已锁住）   B文件（尝试）
    //要确保对方进程已锁住自己的文件，引入了是否已锁住判断的文件*IsLockFile
    //A文件锁住时，创建A文件的*IsLockFile， 此时 file descriptor不为-1
   // 然后通过不断循环打开B文件的*IsLockFile，判断descriptor不为-1，即说明B文件已锁住，可以尝试锁住B文件观察B进程
    external fun lockFileAndObserFile(self_lock_file :String, other_lock_file :String, self_lock_file_is_lock_file:String, other_lock_file_is_lock_file:String);

    fun haveProcessDead() {
        Log.e("YongSheng", "回调===>haveProcessDead====>" + Process.myPid())
        YongShengProcessAPI.process().haveProcessDead();
    }
}