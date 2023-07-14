package com.onedream.yongshengdemo.yongsheng

import android.content.Context
import android.content.Intent
import android.os.Process
import android.util.Log
import java.io.File


class YongShengProcess : IYongShengProcess {
    private var mContext: Context? = null
    private var mConfigs: YongShengConfig? = null
    private var mAssistClass: Class<Any>? = null
    //
    private val DEAD_LOCK_FILE =  "dead_lock_file.txt"
    private val DEAD_LOCK_FILE_IS_LOCK_FILE = "dead_lock_is_lock_file.txt"
    private val ASSIST_LOCK_FILE =  "assist_lock_file.txt"
    private val ASSIST_LOCK_FILE_IS_LOCK_FILE = "assist_lock_is_lock_file.txt"

    override fun onInit(context: Context): Boolean {
        mContext = context
        mAssistClass = null
        val deaLockFilePath = getFullPath(context, DEAD_LOCK_FILE)
        YongShengNativeLib.createFileIfNotExist(deaLockFilePath)
        //
        val assistLockFilePath =getFullPath(context, ASSIST_LOCK_FILE)
        YongShengNativeLib.createFileIfNotExist(assistLockFilePath)
        return true
    }

    private fun getFullPath(context: Context, fileName :String) : String{
        return context.getExternalFilesDir("")!!.absolutePath + File.separator + fileName;
    }

    override fun onSelfCreate(context: Context, configs: YongShengConfig) {
        mContext = context
        //
        Thread {
            YongShengNativeLib.lockFileAndObserFile(
                getFullPath(context, DEAD_LOCK_FILE),
                getFullPath(context, ASSIST_LOCK_FILE),
                getFullPath(context, DEAD_LOCK_FILE_IS_LOCK_FILE),
                getFullPath(context, ASSIST_LOCK_FILE_IS_LOCK_FILE)
            )
        }.start()

        mConfigs = configs
        mAssistClass = mConfigs!!.assistConfigItem.serviceNameClass
        starOtherProcessService()
    }

    override fun onAssistCreate(context: Context, configs: YongShengConfig) {
        mContext = context
        Thread {
            YongShengNativeLib.lockFileAndObserFile(
                getFullPath(context, ASSIST_LOCK_FILE),
                getFullPath(context, DEAD_LOCK_FILE),
                getFullPath(context, ASSIST_LOCK_FILE_IS_LOCK_FILE),
                getFullPath(context, DEAD_LOCK_FILE_IS_LOCK_FILE)
            )
        }.start()

        mConfigs = configs
        mAssistClass = mConfigs!!.selfConfigItem.serviceNameClass
        starOtherProcessService()
    }

    override fun haveProcessDead() {
        starOtherProcessService()
        Process.killProcess(Process.myPid())
    }

    private fun starOtherProcessService() {
        Log.e("YongSheng", "调用启动对方进程的Service $mAssistClass")
        when (mAssistClass) {
            null -> {

            }
            else -> {
                mContext?.startForegroundService(
                    Intent(
                        mContext,
                        mAssistClass
                    )
                )
            }
        }

    }
}