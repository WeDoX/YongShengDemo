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

    override fun onInit(context: Context): Boolean {
        mContext = context
        mAssistClass = null
        val pFilePath =
            context.getExternalFilesDir("")!!.absolutePath + File.separator + "dead_lock_file.txt"
        YongShengNativeLib.createFileIfNotExist(pFilePath)
        return true
    }

    override fun onSelfCreate(context: Context, configs: YongShengConfig) {
        mContext = context

        val pFilePath =
            context.getExternalFilesDir("")!!.absolutePath + File.separator + "dead_lock_file.txt"
        //
        Thread {
            YongShengNativeLib.lockFile(pFilePath)
        }.start()

        mConfigs = configs
        mAssistClass = mConfigs!!.assistConfigItem.serviceNameClass
        starOtherProcessService()
    }

    override fun onAssistCreate(context: Context, configs: YongShengConfig) {
        mContext = context
        Thread {
            val pFilePath =
                context.getExternalFilesDir("")!!.absolutePath + File.separator + "dead_lock_file.txt"
            YongShengNativeLib.obserFile(pFilePath)
        }.start()

        mConfigs = configs
        mAssistClass = mConfigs!!.selfConfigItem.serviceNameClass
        starOtherProcessService()
    }

    override fun haveProcessDead() {
        Process.killProcess(Process.myPid())
        starOtherProcessService()
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